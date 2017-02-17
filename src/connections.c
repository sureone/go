#include "buffer.h"
#include "server.h"
#include "log.h"
#include "connections.h"
#include "fdevent.h"

#include "request.h"
#include "response.h"
#include "network.h"
#include "http_chunk.h"
#include "stat_cache.h"
#include "joblist.h"

#include "plugin.h"

#include "inet_ntop_cache.h"

#include <sys/stat.h>

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <fcntl.h>
#include <assert.h>

#ifdef USE_OPENSSL
# include <openssl/ssl.h>
# include <openssl/err.h>
#endif

#ifdef HAVE_SYS_FILIO_H
# include <sys/filio.h>
#endif

#include "sys-socket.h"
#include "shelper.h"

#ifdef DMALLOC
#include "dmalloc.h"
#endif

typedef struct {
	        PLUGIN_DATA;
} plugin_data;

void tdiff(struct timespec* start, struct timespec* end,struct timespec* temp)
{
	if ((end->tv_nsec-start->tv_nsec)<0) {
		temp->tv_sec = end->tv_sec-start->tv_sec-1;
		temp->tv_nsec = 1000000000+end->tv_nsec-start->tv_nsec;
	} else {
		temp->tv_sec = end->tv_sec-start->tv_sec;
		temp->tv_nsec = end->tv_nsec-start->tv_nsec;
	}
}

static int connection_handle_write(server *srv, connection *con,chunkqueue* cq)
{
    int ret = 0;
    struct timespec time1, time2,timed;
    //clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time1);
    ret = network_write_chunkqueue(srv, con, cq,256*1024);
    //clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time2);
    //tdiff(&time1,&time2,&timed);
    //printf("TOW=%d:%d\n",timed.tv_sec,timed.tv_nsec);
    switch(ret)
    {
    case 0:
        break;
    case -1: /* error on our side */
        log_error_write(__FILE__, __LINE__, "sd",
                        "connection closed: write failed on fd", con->fd);
        connection_set_state(srv, con, CON_STATE_ERROR);
        joblist_append(srv, con);
        break;
    case -2: /* remote close */
        connection_set_state(srv, con, CON_STATE_ERROR);
        joblist_append(srv, con);
        break;
    }

    return 0;
}


static connection *get_connection_by_fd(server* srv,int fd){
		connections *conns = srv->conns;
		for (size_t i = 0; i < conns->used; i++) {
			connection* conn = conns->ptr[i];
			
			if(conn!=NULL){
				debug_log("sd","connection fd=",conn->fd);
				if(conn->fd == fd) return conn;
			}
		}
		return NULL;
}
void send_response_to_client(server* srv,char* data){
	char* next = NULL;
	connection* con = NULL;
	if(data==NULL) return;
	int type = getInt((char*)data,',',&next);
	int ndx = getInt((char*)next,',',&next);
	if(type==1 && ndx!=-1){
		WS(next);
		chunkqueue* write_queue = chunkqueue_init();
        	buffer * b = chunkqueue_get_append_buffer(write_queue);
		buffer_append_string(b,next);
		con = get_connection_by_fd(srv,ndx);
		if(con!=NULL){
			connection_handle_write(srv,con,write_queue);
		}else{
			WS("connection not exist");
		}
		chunkqueue_free(write_queue);
	}
	free(data);
}


void thread_send_msg(server* srv,struct threadmsg* msg){
	char* data = msg->data;
	send_response_to_client(srv,data);
}


static connection *connections_get_new_connection(server *srv) {
	connections *conns = srv->conns;
	size_t i;

	if (conns->size == 0) {
		conns->size = 128;
		conns->ptr = NULL;
		conns->ptr = malloc(sizeof(*conns->ptr) * conns->size);
		for (i = 0; i < conns->size; i++) {
			conns->ptr[i] = connection_init(srv);
		}
	} else if (conns->size == conns->used) {
		conns->size += 128;
		conns->ptr = realloc(conns->ptr, sizeof(*conns->ptr) * conns->size);

		for (i = conns->used; i < conns->size; i++) {
			conns->ptr[i] = connection_init(srv);
		}
	}

	connection_reset(srv, conns->ptr[conns->used]);
#if 0
	fprintf(stderr, "%s.%d: add: ", __FILE__, __LINE__);
	for (i = 0; i < conns->used + 1; i++) {
		fprintf(stderr, "%d ", conns->ptr[i]->fd);
	}
	fprintf(stderr, "\n");
#endif

	conns->ptr[conns->used]->ndx = conns->used;
	return conns->ptr[conns->used++];
}

static int connection_del(server *srv, connection *con) {
	size_t i;
	connections *conns = srv->conns;
	connection *temp;

	if (con == NULL) return -1;

	if (-1 == con->ndx) return -1;

	i = con->ndx;

	/* not last element */

	if (i != conns->used - 1) {
		temp = conns->ptr[i];
		conns->ptr[i] = conns->ptr[conns->used - 1];
		conns->ptr[conns->used - 1] = temp;

		conns->ptr[i]->ndx = i;
		conns->ptr[conns->used - 1]->ndx = -1;
	}

	conns->used--;

	con->ndx = -1;
#if 0
	fprintf(stderr, "%s.%d: del: (%d)", __FILE__, __LINE__, conns->used);
	for (i = 0; i < conns->used; i++) {
		fprintf(stderr, "%d ", conns->ptr[i]->fd);
	}
	fprintf(stderr, "\n");
#endif
	return 0;
}

int connection_close(server *srv, connection *con) {
#ifdef USE_OPENSSL
	server_socket *srv_sock = con->srv_socket;
#endif

#ifdef USE_OPENSSL
	if (srv_sock->is_ssl) {
		if (con->ssl) SSL_free(con->ssl);
		con->ssl = NULL;
	}
#endif

	fdevent_event_del(srv->ev, &(con->fde_ndx), con->fd);
	fdevent_unregister(srv->ev, con->fd);
#ifdef __WIN32
	if (closesocket(con->fd)) {
		log_error_write( __FILE__, __LINE__, "sds",
				"(warning) close:", con->fd, strerror(errno));
	}
#else
	if (close(con->fd)) {
		log_error_write( __FILE__, __LINE__, "sds",
				"(warning) close:", con->fd, strerror(errno));
	}
#endif

	srv->cur_fds--;
#if 0
	log_error_write( __FILE__, __LINE__, "sd",
			"closed()", con->fd);
#endif

	buffer* buff = buffer_init();
	//connection status report
	buffer_append_long(buff,0);
	buffer_append_string(buff,",");
	//connection index
	buffer_append_long(buff,(con->fd));
	buffer_append_string(buff,",close,");
	req_msg_t* msg = calloc(1,sizeof(*msg));
	msg->hlen=strlen(buff->ptr);
	msg->header=buff->ptr;
	msg->dlen=0;
	msg->data=0;
	//req_msg_send(srv->req_publisher,msg);
	mgo_process_msg(msg);
	//free(msg);
	buffer_free_struct(buff);

	connection_del(srv, con);
	connection_set_state(srv, con, CON_STATE_CONNECT);

	return 0;
}

#if 0
static void dump_packet(const unsigned char *data, size_t len) {
	size_t i, j;

	if (len == 0) return;

	for (i = 0; i < len; i++) {
		if (i % 16 == 0) fprintf(stderr, "  ");

		fprintf(stderr, "%02x ", data[i]);

		if ((i + 1) % 16 == 0) {
			fprintf(stderr, "  ");
			for (j = 0; j <= i % 16; j++) {
				unsigned char c;

				if (i-15+j >= len) break;

				c = data[i-15+j];

				fprintf(stderr, "%c", c > 32 && c < 128 ? c : '.');
			}

			fprintf(stderr, "\n");
		}
	}

	if (len % 16 != 0) {
		for (j = i % 16; j < 16; j++) {
			fprintf(stderr, "   ");
		}

		fprintf(stderr, "  ");
		for (j = i & ~0xf; j < len; j++) {
			unsigned char c;

			c = data[j];
			fprintf(stderr, "%c", c > 32 && c < 128 ? c : '.');
		}
		fprintf(stderr, "\n");
	}
}
#endif

static int connection_handle_read_ssl(server *srv, connection *con) {
#ifdef USE_OPENSSL
	int r, ssl_err, len, count = 0, read_offset, toread;
	buffer *b = NULL;

	if (!con->conf.is_ssl) return -1;

	ERR_clear_error();
	do {
		if (NULL != con->read_queue->last) {
			b = con->read_queue->last->mem;
		}

		if (NULL == b || b->size - b->used < 1024) {
			b = chunkqueue_get_append_buffer(con->read_queue);
			len = SSL_pending(con->ssl);
			if (len < 4*1024) len = 4*1024; /* always alloc >= 4k buffer */
			buffer_prepare_copy(b, len + 1);

			/* overwrite everything with 0 */
			memset(b->ptr, 0, b->size);
		}

		read_offset = (b->used > 0) ? b->used - 1 : 0;
		toread = b->size - 1 - read_offset;

		len = SSL_read(con->ssl, b->ptr + read_offset, toread);

		if (con->renegotiations > 1 && con->conf.ssl_disable_client_renegotiation) {
			connection_set_state(srv, con, CON_STATE_ERROR);
			log_error_write( __FILE__, __LINE__, "s", "SSL: renegotiation initiated by client");
			return -1;
		}

		if (len > 0) {
			if (b->used > 0) b->used--;
			b->used += len;
			b->ptr[b->used++] = '\0';

			con->bytes_read += len;

			count += len;
		}
	} while (len == toread && count < MAX_READ_LIMIT);


	if (len < 0) {
		int oerrno = errno;
		switch ((r = SSL_get_error(con->ssl, len))) {
		case SSL_ERROR_WANT_READ:
		case SSL_ERROR_WANT_WRITE:
			con->is_readable = 0;

			/* the manual says we have to call SSL_read with the same arguments next time.
			 * we ignore this restriction; no one has complained about it in 1.5 yet, so it probably works anyway.
			 */

			return 0;
		case SSL_ERROR_SYSCALL:
			/**
			 * man SSL_get_error()
			 *
			 * SSL_ERROR_SYSCALL
			 *   Some I/O error occurred.  The OpenSSL error queue may contain more
			 *   information on the error.  If the error queue is empty (i.e.
			 *   ERR_get_error() returns 0), ret can be used to find out more about
			 *   the error: If ret == 0, an EOF was observed that violates the
			 *   protocol.  If ret == -1, the underlying BIO reported an I/O error
			 *   (for socket I/O on Unix systems, consult errno for details).
			 *
			 */
			while((ssl_err = ERR_get_error())) {
				/* get all errors from the error-queue */
				log_error_write( __FILE__, __LINE__, "sds", "SSL:",
						r, ERR_error_string(ssl_err, NULL));
			}

			switch(oerrno) {
			default:
				log_error_write( __FILE__, __LINE__, "sddds", "SSL:",
						len, r, oerrno,
						strerror(oerrno));
				break;
			}

			break;
		case SSL_ERROR_ZERO_RETURN:
			/* clean shutdown on the remote side */

			if (r == 0) {
				/* FIXME: later */
			}

			/* fall thourgh */
		default:
			while((ssl_err = ERR_get_error())) {
				switch (ERR_GET_REASON(ssl_err)) {
				case SSL_R_SSL_HANDSHAKE_FAILURE:
				case SSL_R_TLSV1_ALERT_UNKNOWN_CA:
				case SSL_R_SSLV3_ALERT_CERTIFICATE_UNKNOWN:
				case SSL_R_SSLV3_ALERT_BAD_CERTIFICATE:
					if (!con->conf.log_ssl_noise) continue;
					break;
				default:
					break;
				}
				/* get all errors from the error-queue */
				log_error_write( __FILE__, __LINE__, "sds", "SSL:",
				                r, ERR_error_string(ssl_err, NULL));
			}
			break;
		}

		connection_set_state(srv, con, CON_STATE_ERROR);

		return -1;
	} else if (len == 0) {
		con->is_readable = 0;
		/* the other end close the connection -> KEEP-ALIVE */

		return -2;
	} else {
		joblist_append(srv, con);
	}

	return 0;
#else
	UNUSED(srv);
	UNUSED(con);
	return -1;
#endif
}

/* 0: everything ok, -1: error, -2: con closed */
static int connection_handle_read(server *srv, connection *con) {
	int len;
	buffer *b;
	int toread, read_offset;

	if (con->conf.is_ssl) {
		return connection_handle_read_ssl(srv, con);
	}

	b = (NULL != con->read_queue->last) ? con->read_queue->last->mem : NULL;

	/* default size for chunks is 4kb; only use bigger chunks if FIONREAD tells
	 *  us more than 4kb is available
	 * if FIONREAD doesn't signal a big chunk we fill the previous buffer
	 *  if it has >= 1kb free
	 */
#if defined(__WIN32)
	if (NULL == b || b->size - b->used < 1024) {
		b = chunkqueue_get_append_buffer(con->read_queue);
		buffer_prepare_copy(b, 4 * 1024);
	}

	read_offset = (b->used == 0) ? 0 : b->used - 1;
	len = recv(con->fd, b->ptr + read_offset, b->size - 1 - read_offset, 0);
#else
	if (ioctl(con->fd, FIONREAD, &toread) || toread == 0 || toread <= 4*1024) {
		if (NULL == b || b->size - b->used < 1024) {
			b = chunkqueue_get_append_buffer(con->read_queue);
			buffer_prepare_copy(b, 4 * 1024);
		}
	} else {
		if (toread > MAX_READ_LIMIT) toread = MAX_READ_LIMIT;
		b = chunkqueue_get_append_buffer(con->read_queue);
		buffer_prepare_copy(b, toread + 1);
	}

	read_offset = (b->used == 0) ? 0 : b->used - 1;
	len = read(con->fd, b->ptr + read_offset, b->size - 1 - read_offset);
#endif
	// log_error_write(__FILE__,__LINE__,"sdd", "receive from",con->fd,len);

	if (len < 0) {
		con->is_readable = 0;
		log_error_write( __FILE__, __LINE__, "ssd", "read failed: ", strerror(errno), errno);
		if (errno == EAGAIN) return 0;
		if (errno == EINTR) {
			/* we have been interrupted before we could read */
			con->is_readable = 1;
			return 0;
		}

		if (errno != ECONNRESET) {
			/* expected for keep-alive */
			log_error_write( __FILE__, __LINE__, "ssd", "connection closed - read failed: ", strerror(errno), errno);
		}

		connection_set_state(srv, con, CON_STATE_ERROR);

		return -1;
	} else if (len == 0) {
		con->is_readable = 0;
		/* the other end close the connection -> KEEP-ALIVE */

		/* pipelining */

		return -2;
	} else if ((size_t)len < b->size - 1) {
		/* we got less then expected, wait for the next fd-event */

		con->is_readable = 0;
	}


	if (b->used > 0) b->used--;
	b->used += len;
	b->ptr[b->used++] = '\0';

	con->bytes_read += len;
#if 0
	dump_packet(b->ptr, len);
#endif

	return 0;
}


connection *connection_init(server *srv) {
	connection *con;

	UNUSED(srv);

	con = calloc(1, sizeof(*con));

	con->fd = 0;
	con->ndx = -1;
	con->fde_ndx = -1;
	con->bytes_written = 0;
	con->bytes_read = 0;
	con->loops_per_request = 0;
	con->killme=0;

#define CLEAN(x) \
	con->x = buffer_init();

	CLEAN(ip);
	CLEAN(request.request_line);
	CLEAN(request.request);
#if defined USE_OPENSSL && ! defined OPENSSL_NO_TLSEXT
	CLEAN(tlsext_server_name);
#endif

#undef CLEAN
	con->write_queue = chunkqueue_init();
	con->read_queue = chunkqueue_init();
	con->request_content_queue = chunkqueue_init();
	chunkqueue_set_tempdirs(con->request_content_queue, srv->srvconf.upload_tempdirs);
        con->request.headers      = array_init();

	/* init plugin specific connection structures */

	config_setup_connection(srv, con);
	con->hb_idle_ts = srv->cur_ts;

	return con;
}

void connections_free(server *srv) {
	connections *conns = srv->conns;
	size_t i;

	for (i = 0; i < conns->size; i++) {
		connection *con = conns->ptr[i];

		connection_reset(srv, con);

		chunkqueue_free(con->write_queue);
		chunkqueue_free(con->read_queue);
		chunkqueue_free(con->request_content_queue);
        	array_free(con->request.headers);

#define CLEAN(x) \
	buffer_free(con->x);

		CLEAN(request.request_line);
		CLEAN(request.request);
		CLEAN(ip);
#if defined USE_OPENSSL && ! defined OPENSSL_NO_TLSEXT
		CLEAN(tlsext_server_name);
#endif
#undef CLEAN

		free(con);
	}

	free(conns->ptr);
}


int connection_reset(server *srv, connection *con) {
	size_t i;


	con->is_readable = 1;
	con->is_writable = 1;

	con->bytes_written = 0;
	con->bytes_written_cur_second = 0;
	con->bytes_read = 0;
	con->loops_per_request = 0;

	con->request.http_method = HTTP_METHOD_UNSET;


#define CLEAN(x) \
	if (con->x) buffer_reset(con->x);

	CLEAN(request.request_line);
	CLEAN(request.request);

#if defined USE_OPENSSL && ! defined OPENSSL_NO_TLSEXT
	CLEAN(tlsext_server_name);
#endif
#undef CLEAN

#define CLEAN(x) \
	if (con->x) con->x->used = 0;

#undef CLEAN

#define CLEAN(x) \
		con->request.x = NULL;

#undef CLEAN
	con->request.content_length = 0;
        array_reset(con->request.headers);

	chunkqueue_reset(con->write_queue);
	chunkqueue_reset(con->request_content_queue);

	config_setup_connection(srv, con);

	return 0;
}

/**
 * handle all header and content read
 *
 * we get called by the state-engine and by the fdevent-handler
 */
static int connection_handle_read_state(server *srv, connection *con)  {
	connection_state_t ostate = con->state;
	chunk *c, *last_chunk;
	off_t last_offset;
	chunkqueue *cq = con->read_queue;
	chunkqueue *dst_cq = con->request_content_queue;
	int is_closed = 0; /* the connection got closed, if we don't have a complete header, -> error */

	if (con->is_readable) {
		con->read_idle_ts = srv->cur_ts;

		switch(connection_handle_read(srv, con)) {
		case -1:
			return -1;
		case -2:
			is_closed = 1;
			break;
		default:
			break;
		}
	}

	/* the last chunk might be empty */
	for (c = cq->first; c;) {
		if (cq->first == c && c->mem->used == 0) {
			/* the first node is empty */
			/* ... and it is empty, move it to unused */

			cq->first = c->next;
			if (cq->first == NULL) cq->last = NULL;

			c->next = cq->unused;
			cq->unused = c;
			cq->unused_chunks++;

			c = cq->first;
		} else if (c->next && c->next->mem->used == 0) {
			chunk *fc;
			/* next node is the last one */
			/* ... and it is empty, move it to unused */

			fc = c->next;
			c->next = fc->next;

			fc->next = cq->unused;
			cq->unused = fc;
			cq->unused_chunks++;

			/* the last node was empty */
			if (c->next == NULL) {
				cq->last = c;
			}

			c = c->next;
		} else {
			c = c->next;
		}
	}

	/* we might have got several packets at once
	 */

	switch(ostate) {
	case CON_STATE_READ:
		/* if there is a \r\n\r\n in the chunkqueue
		 *
		 * scan the chunk-queue twice
		 * 1. to find the \r\n\r\n
		 * 2. to copy the header-packet
		 *
		 */

		last_chunk = NULL;
		last_offset = 0;

		for (c = cq->first; c; c = c->next) {
			buffer b;
			size_t i;

			b.ptr = c->mem->ptr + c->offset;
			b.used = c->mem->used - c->offset;
			if (b.used > 0) b.used--; /* buffer "used" includes terminating zero */

			for (i = 0; i < b.used; i++) {
				char ch = b.ptr[i];

				if ('\r' == ch) {
					/* chec if \n\r\n follows */
					size_t j = i+1;
					chunk *cc = c;
					const char header_end[] = "\r\n\r\n";
					int header_end_match_pos = 1;

					for ( ; cc; cc = cc->next, j = 0 ) {
						buffer bb;
						bb.ptr = cc->mem->ptr + cc->offset;
						bb.used = cc->mem->used - cc->offset;
						if (bb.used > 0) bb.used--; /* buffer "used" includes terminating zero */

						for ( ; j < bb.used; j++) {
							ch = bb.ptr[j];

							if (ch == header_end[header_end_match_pos]) {
								header_end_match_pos++;
								if (4 == header_end_match_pos) {
									last_chunk = cc;
									last_offset = j+1;
									goto found_header_end;
								}
							} else {
								goto reset_search;
							}
						}
					}
				}
reset_search: ;
			}
		}
found_header_end:

		/* found */
		if (last_chunk) {
			buffer_reset(con->request.request);

			for (c = cq->first; c; c = c->next) {
				buffer b;

				b.ptr = c->mem->ptr + c->offset;
				b.used = c->mem->used - c->offset;

				if (c == last_chunk) {
					b.used = last_offset + 1;
				}

				buffer_append_string_buffer(con->request.request, &b);

				if (c == last_chunk) {
					c->offset += last_offset;

					break;
				} else {
					/* the whole packet was copied */
					c->offset = c->mem->used - 1;
				}
			}

			connection_set_state(srv, con, CON_STATE_REQUEST_END);
		} else if (chunkqueue_length(cq) > 64 * 1024) {
			log_error_write( __FILE__, __LINE__, "s", "oversized request-header -> sending Status 414");

			con->http_status = 414; /* Request-URI too large */
			con->keep_alive = 0;
			connection_set_state(srv, con, CON_STATE_HANDLE_REQUEST);
		}
		break;
	case CON_STATE_READ_POST:
		for (c = cq->first; c && (dst_cq->bytes_in != (off_t)con->request.content_length); c = c->next) {
			off_t weWant, weHave, toRead;

			weWant = con->request.content_length - dst_cq->bytes_in;

			assert(c->mem->used);

			weHave = c->mem->used - c->offset - 1;

			toRead = weHave > weWant ? weWant : weHave;

			/* the new way, copy everything into a chunkqueue whcih might use tempfiles */
			if (con->request.content_length > 64 * 1024) {
				chunk *dst_c = NULL;
				/* copy everything to max 1Mb sized tempfiles */

				/*
				 * if the last chunk is
				 * - smaller than 1Mb (size < 1Mb)
				 * - not read yet (offset == 0)
				 * -> append to it
				 * otherwise
				 * -> create a new chunk
				 *
				 * */

				if (dst_cq->last &&
				    dst_cq->last->type == FILE_CHUNK &&
				    dst_cq->last->file.is_temp &&
				    dst_cq->last->offset == 0) {
					/* ok, take the last chunk for our job */

			 		if (dst_cq->last->file.length < 1 * 1024 * 1024) {
						dst_c = dst_cq->last;

						if (dst_c->file.fd == -1) {
							/* this should not happen as we cache the fd, but you never know */
							dst_c->file.fd = open(dst_c->file.name->ptr, O_WRONLY | O_APPEND);
#ifdef FD_CLOEXEC
							fcntl(dst_c->file.fd, F_SETFD, FD_CLOEXEC);
#endif
						}
					} else {
						/* the chunk is too large now, close it */
						dst_c = dst_cq->last;

						if (dst_c->file.fd != -1) {
							close(dst_c->file.fd);
							dst_c->file.fd = -1;
						}
						dst_c = chunkqueue_get_append_tempfile(dst_cq);
					}
				} else {
					dst_c = chunkqueue_get_append_tempfile(dst_cq);
				}

				/* we have a chunk, let's write to it */

				if (dst_c->file.fd == -1) {
					/* we don't have file to write to,
					 * EACCES might be one reason.
					 *
					 * Instead of sending 500 we send 413 and say the request is too large
					 *  */

					log_error_write( __FILE__, __LINE__, "sbs",
							"denying upload as opening to temp-file for upload failed:",
							dst_c->file.name, strerror(errno));

					con->http_status = 413; /* Request-Entity too large */
					con->keep_alive = 0;
					connection_set_state(srv, con, CON_STATE_HANDLE_REQUEST);

					break;
				}

				if (toRead != write(dst_c->file.fd, c->mem->ptr + c->offset, toRead)) {
					/* write failed for some reason ... disk full ? */
					log_error_write( __FILE__, __LINE__, "sbs",
							"denying upload as writing to file failed:",
							dst_c->file.name, strerror(errno));

					con->http_status = 413; /* Request-Entity too large */
					con->keep_alive = 0;
					connection_set_state(srv, con, CON_STATE_HANDLE_REQUEST);

					close(dst_c->file.fd);
					dst_c->file.fd = -1;

					break;
				}

				dst_c->file.length += toRead;

				if (dst_cq->bytes_in + toRead == (off_t)con->request.content_length) {
					/* we read everything, close the chunk */
					close(dst_c->file.fd);
					dst_c->file.fd = -1;
				}
			} else {
				buffer *b;

				if (dst_cq->last &&
				    dst_cq->last->type == MEM_CHUNK) {
					b = dst_cq->last->mem;
				} else {
					b = chunkqueue_get_append_buffer(dst_cq);
					/* prepare buffer size for remaining POST data; is < 64kb */
					buffer_prepare_copy(b, con->request.content_length - dst_cq->bytes_in + 1);
				}
				buffer_append_string_len(b, c->mem->ptr + c->offset, toRead);
			}

			c->offset += toRead;
			dst_cq->bytes_in += toRead;
		}

		/* Content is ready */
		if (dst_cq->bytes_in == (off_t)con->request.content_length) {
			connection_set_state(srv, con, CON_STATE_HANDLE_REQUEST);


		}

		break;
	default: break;
	}

	/* the connection got closed and we didn't got enough data to leave one of the READ states
	 * the only way is to leave here */
	if (is_closed && ostate == con->state) {
		connection_set_state(srv, con, CON_STATE_ERROR);
	}

	chunkqueue_remove_finished_chunks(cq);

	return 0;
}

static handler_t connection_handle_fdevent(server *srv, void *context, int revents) {
	connection *con = context;

	joblist_append(srv, con);

	if (con->conf.is_ssl) {
		/* ssl may read and write for both reads and writes */
		if (revents & (FDEVENT_IN | FDEVENT_OUT)) {
			con->is_readable = 1;
			con->is_writable = 1;
		}
	} else {
		if (revents & FDEVENT_IN) {
			con->is_readable = 1;
		}
		if (revents & FDEVENT_OUT) {
			con->is_writable = 1;
			/* we don't need the event twice */
		}
	}


	if (revents & ~(FDEVENT_IN | FDEVENT_OUT)) {
		/* looks like an error */

		/* FIXME: revents = 0x19 still means that we should read from the queue */
		if (revents & FDEVENT_HUP) {
			if (con->state == CON_STATE_CLOSE) {
				con->close_timeout_ts = srv->cur_ts - (HTTP_LINGER_TIMEOUT+1);
			} else {
				/* sigio reports the wrong event here
				 *
				 * there was no HUP at all
				 */
#ifdef USE_LINUX_SIGIO
				if (srv->ev->in_sigio == 1) {
					log_error_write( __FILE__, __LINE__, "sd",
						"connection closed: poll() -> HUP", con->fd);
				} else {
					connection_set_state(srv, con, CON_STATE_ERROR);
				}
#else
				connection_set_state(srv, con, CON_STATE_ERROR);
#endif

			}
		} else if (revents & FDEVENT_ERR) {
			/* error, connection reset, whatever... we don't want to spam the logfile */
#if 0
			log_error_write( __FILE__, __LINE__, "sd",
					"connection closed: poll() -> ERR", con->fd);
#endif
			connection_set_state(srv, con, CON_STATE_ERROR);
		} else {
			log_error_write( __FILE__, __LINE__, "sd",
					"connection closed: poll() -> ???", revents);
		}
	}

	if (con->state == CON_STATE_READ ||
	    con->state == CON_STATE_READ_POST) {
		connection_handle_read_state(srv, con);
	}

	if (con->state == CON_STATE_WRITE){
	}

	if (con->state == CON_STATE_CLOSE) {
		/* flush the read buffers */
		int len;
		char buf[1024];

		len = read(con->fd, buf, sizeof(buf));
		if (len == 0 || (len < 0 && errno != EAGAIN && errno != EINTR) ) {
			con->close_timeout_ts = srv->cur_ts - (HTTP_LINGER_TIMEOUT+1);
		}
	}

	return HANDLER_FINISHED;
}


connection *connection_accept(server *srv, server_socket *srv_socket) {
	/* accept everything */

	/* search an empty place */
	int cnt;
	sock_addr cnt_addr;
	socklen_t cnt_len;
	/* accept it and register the fd */

	/**
	 * check if we can still open a new connections
	 *
	 * see #1216
	 */

	if (srv->conns->used >= srv->max_conns) {
		return NULL;
	}

	cnt_len = sizeof(cnt_addr);

	if (-1 == (cnt = accept(srv_socket->fd, (struct sockaddr *) &cnt_addr, &cnt_len))) {
		switch (errno) {
		case EAGAIN:
#if EWOULDBLOCK != EAGAIN
		case EWOULDBLOCK:
#endif
		case EINTR:
			/* we were stopped _before_ we had a connection */
		case ECONNABORTED: /* this is a FreeBSD thingy */
			/* we were stopped _after_ we had a connection */
			break;
		case EMFILE:
			/* out of fds */
			break;
		default:
			log_error_write( __FILE__, __LINE__, "ssd", "accept failed:", strerror(errno), errno);
		}
		return NULL;
	} else {
		connection *con;
		char* ip = NULL;

		srv->cur_fds++;
		


		ip= inet_ntoa(((struct sockaddr_in*)(&cnt_addr))->sin_addr);
		
		
		
		log_error_write( __FILE__, __LINE__, "ss",
				"appected()", ip);

		/* ok, we have the connection, register it */
		log_error_write( __FILE__, __LINE__, "sd",
				"appected()", cnt);
		srv->con_opened++;

		con = connections_get_new_connection(srv);
		buffer_append_string(con->ip,ip);

		con->fd = cnt;
		con->fde_ndx = -1;
#if 0
		gettimeofday(&(con->start_tv), NULL);
#endif
		fdevent_register(srv->ev, con->fd, connection_handle_fdevent, con);

		connection_set_state(srv, con, CON_STATE_REQUEST_START);

		con->connection_start = srv->cur_ts;
		con->srv_socket = srv_socket;

		if (-1 == (fdevent_fcntl_set(srv->ev, con->fd))) {
			log_error_write( __FILE__, __LINE__, "ss", "fcntl failed: ", strerror(errno));
			return NULL;
		}
#ifdef USE_OPENSSL
		/* connect FD to SSL */
		if (srv_socket->is_ssl) {
			if (NULL == (con->ssl = SSL_new(srv_socket->ssl_ctx))) {
				log_error_write( __FILE__, __LINE__, "ss", "SSL:",
						ERR_error_string(ERR_get_error(), NULL));

				return NULL;
			}

			con->renegotiations = 0;
#ifndef OPENSSL_NO_TLSEXT
			SSL_set_app_data(con->ssl, con);
#endif
			SSL_set_accept_state(con->ssl);
			con->conf.is_ssl=1;

			if (1 != (SSL_set_fd(con->ssl, cnt))) {
				log_error_write( __FILE__, __LINE__, "ss", "SSL:",
						ERR_error_string(ERR_get_error(), NULL));
				return NULL;
			}
		}
#endif
		return con;
	}
}


int connection_state_machine(server *srv, connection *con) {
	int done = 0, r;
#ifdef USE_OPENSSL
	server_socket *srv_sock = con->srv_socket;
#endif

	if (srv->srvconf.log_state_handling) {
		log_error_write( __FILE__, __LINE__, "sds",
				"state at start",
				con->fd,
				connection_get_state(con->state));
	}

	while (done == 0) {
		size_t ostate = con->state;

		switch (con->state) {
		case CON_STATE_REQUEST_START: /* transient */
			if (srv->srvconf.log_state_handling) {
				log_error_write( __FILE__, __LINE__, "sds",
						"state for fd", con->fd, connection_get_state(con->state));
			}

			con->request_start = srv->cur_ts;
			con->read_idle_ts = srv->cur_ts;

			con->request_count++;
			con->loops_per_request = 0;

			connection_set_state(srv, con, CON_STATE_READ);

			/* patch con->conf.is_ssl if the connection is a ssl-socket already */

#ifdef USE_OPENSSL
			con->conf.is_ssl = srv_sock->is_ssl;
#endif

			break;
		case CON_STATE_REQUEST_END: /* transient */
			if (srv->srvconf.log_state_handling) {
				log_error_write( __FILE__, __LINE__, "sds",
						"state for fd", con->fd, connection_get_state(con->state));
			}


			buffer* temp = buffer_init_buffer(con->request.request);	
			if (parse_http_header(temp->ptr,con->request.headers,&(con->request.content_length))) {
				/* we have to read some data from the POST request */
				connection_set_state(srv, con, CON_STATE_READ_POST);
				buffer_free(temp);
				

				break;
			}
			buffer_free(temp);
			

			connection_set_state(srv, con, CON_STATE_HANDLE_REQUEST);

			break;
		case CON_STATE_HANDLE_REQUEST:
			{
			con->killme=0;
			buffer* buff = buffer_init();
			//just forward user data
			buffer_append_long(buff,1);
			buffer_append_string(buff,",");
			buffer_append_long(buff,(con->fd));
			buffer_append_string(buff,",");
			buffer_append_string(buff,con->request.request->ptr);

			req_msg_t* msg = calloc(1,sizeof(*msg));
			msg->hlen = strlen(buff->ptr);
			msg->header = buff->ptr;
			msg->dlen=0;
			msg->data=0;

			WS(msg->header);
			buffer* content = buffer_init();
			if(con->request.content_length>0)
			{
            			chunkqueue *cq = con->request_content_queue;
            			chunk *c, *pc;
            			buffer_prepare_copy(content, con->request.content_length + 1);
            			for (c = cq->first; c; ) {
                			pc = c;
                			buffer_append_string_buffer(content,pc->mem);
                			c = c->next;
            			}
				msg->dlen=content->used;
				msg->data=content->ptr;
				WS(msg->data);
			}
			//req_msg_send(srv->req_publisher,msg);
			mgo_process_msg(msg);
			buffer_free_struct(buff);
			buffer_free_struct(content);
			//free(msg);
			connection_set_state(srv, con, CON_STATE_RESPONSE_START);
			}
			break;
		case CON_STATE_RESPONSE_START:
			connection_set_state(srv, con, CON_STATE_RESPONSE_END);
			break;
		case CON_STATE_RESPONSE_END: /* transient */
			/* log the request */

			if (srv->srvconf.log_state_handling) {
				log_error_write( __FILE__, __LINE__, "sds",
						"state for fd", con->fd, connection_get_state(con->state));
			}

			srv->con_written++;

			if (con->keep_alive) {
				connection_set_state(srv, con, CON_STATE_REQUEST_START);
			} else {

#ifdef USE_OPENSSL
				if (srv_sock->is_ssl) {
					switch (SSL_shutdown(con->ssl)) {
					case 1:
						/* done */
						break;
					case 0:
						/* wait for fd-event
						 *
						 * FIXME: wait for fdevent and call SSL_shutdown again
						 *
						 */

						break;
					default:
						log_error_write( __FILE__, __LINE__, "ss", "SSL:",
								ERR_error_string(ERR_get_error(), NULL));
					}
				}
#endif
				if ((0 == shutdown(con->fd, SHUT_WR))) {
					con->close_timeout_ts = srv->cur_ts;
					connection_set_state(srv, con, CON_STATE_CLOSE);
				} else {
					connection_close(srv, con);
				}

				srv->con_closed++;
			}

			connection_reset(srv, con);

			break;
		case CON_STATE_CONNECT:
			if (srv->srvconf.log_state_handling) {
				log_error_write( __FILE__, __LINE__, "sds",
						"state for fd", con->fd, connection_get_state(con->state));
			}

			chunkqueue_reset(con->read_queue);

			con->request_count = 0;

			break;
		case CON_STATE_CLOSE:
			if (srv->srvconf.log_state_handling) {
				log_error_write( __FILE__, __LINE__, "sds",
						"state for fd", con->fd, connection_get_state(con->state));
			}

			/* we have to do the linger_on_close stuff regardless
			 * of con->keep_alive; even non-keepalive sockets may
			 * still have unread data, and closing before reading
			 * it will make the client not see all our output.
			 */
			{
				int len;
				char buf[1024];

				len = read(con->fd, buf, sizeof(buf));
				if (len == 0 || (len < 0 && errno != EAGAIN && errno != EINTR) ) {
					con->close_timeout_ts = srv->cur_ts - (HTTP_LINGER_TIMEOUT+1);
				}
			}

			if (srv->cur_ts - con->close_timeout_ts > HTTP_LINGER_TIMEOUT) {
				connection_close(srv, con);

				if (srv->srvconf.log_state_handling) {
					log_error_write( __FILE__, __LINE__, "sd",
							"connection closed for fd", con->fd);
				}
			}

			break;
		case CON_STATE_READ_POST:
		case CON_STATE_READ:
			if (srv->srvconf.log_state_handling) {
				log_error_write( __FILE__, __LINE__, "sds",
						"state for fd", con->fd, connection_get_state(con->state));
			}

			connection_handle_read_state(srv, con);
			break;
		case CON_STATE_ERROR: /* transient */

#ifdef USE_OPENSSL
			if (srv_sock->is_ssl) {
				int ret, ssl_r;
				unsigned long err;
				ERR_clear_error();
				switch ((ret = SSL_shutdown(con->ssl))) {
				case 1:
					/* ok */
					break;
				case 0:
					ERR_clear_error();
					if (-1 != (ret = SSL_shutdown(con->ssl))) break;

					/* fall through */
				default:

					switch ((ssl_r = SSL_get_error(con->ssl, ret))) {
					case SSL_ERROR_WANT_WRITE:
					case SSL_ERROR_WANT_READ:
						break;
					case SSL_ERROR_SYSCALL:
						/* perhaps we have error waiting in our error-queue */
						if (0 != (err = ERR_get_error())) {
							do {
								log_error_write( __FILE__, __LINE__, "sdds", "SSL:",
										ssl_r, ret,
										ERR_error_string(err, NULL));
							} while((err = ERR_get_error()));
						} else if (errno != 0) { /* ssl bug (see lighttpd ticket #2213): sometimes errno == 0 */
							log_error_write( __FILE__, __LINE__, "sddds", "SSL (error):",
									ssl_r, ret, errno,
									strerror(errno));
						}
	
						break;
					default:
						while((err = ERR_get_error())) {
							log_error_write( __FILE__, __LINE__, "sdds", "SSL:",
									ssl_r, ret,
									ERR_error_string(err, NULL));
						}
	
						break;
					}
				}
			}
			ERR_clear_error();
#endif

			connection_reset(srv, con);

			/* close the connection */
			if ((0 == shutdown(con->fd, SHUT_WR))) {
				con->close_timeout_ts = srv->cur_ts;
				connection_set_state(srv, con, CON_STATE_CLOSE);

				if (srv->srvconf.log_state_handling) {
					log_error_write( __FILE__, __LINE__, "sd",
							"shutdown for fd", con->fd);
				}
			} else {
				connection_close(srv, con);
			}

			con->keep_alive = 0;

			srv->con_closed++;

			break;
		default:
			log_error_write( __FILE__, __LINE__, "sdd",
					"unknown state:", con->fd, con->state);

			break;
		}

		if (done == -1) {
			done = 0;
		} else if (ostate == con->state) {
			done = 1;
		}
	}

	if (srv->srvconf.log_state_handling) {
		log_error_write( __FILE__, __LINE__, "sds",
				"state at exit:",
				con->fd,
				connection_get_state(con->state));
	}

	switch(con->state) {
	case CON_STATE_READ_POST:
	case CON_STATE_READ:
	case CON_STATE_CLOSE:
		fdevent_event_set(srv->ev, &(con->fde_ndx), con->fd, FDEVENT_IN);
		break;
	case CON_STATE_WRITE:
		/* request write-fdevent only if we really need it
		 * - if we have data to write
		 * - if the socket is not writable yet
		 */
		if (!chunkqueue_is_empty(con->write_queue) &&
		    (con->is_writable == 0) &&
		    (con->traffic_limit_reached == 0)) {
			fdevent_event_set(srv->ev, &(con->fde_ndx), con->fd, FDEVENT_OUT);
		} else {
			fdevent_event_del(srv->ev, &(con->fde_ndx), con->fd);
		}
		break;
	default:
		fdevent_event_del(srv->ev, &(con->fde_ndx), con->fd);
		break;
	}

	return 0;
}
