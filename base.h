#ifndef _BASE_H_
#define _BASE_H_

#ifdef HAVE_CONFIG_H
# include "config.h"
#endif
#include "settings.h"

#include <sys/types.h>
#include <sys/time.h>
#include <sys/stat.h>

#include <limits.h>

#ifdef HAVE_STDINT_H
# include <stdint.h>
#endif

#ifdef HAVE_INTTYPES_H
# include <inttypes.h>
#endif

#include "sys-socket.h"

#include "buffer.h"
#include "array.h"
#include "chunk.h"
#include "keyvalue.h"
#include "fdevent.h"
#include "splaytree.h"
#include "etag.h"
//#include "zhelpers.h"
#include "threadqueue.h"

/* the order of the items should be the same as they are processed
 * read before write as we use this later */
typedef enum {
	CON_STATE_CONNECT,
	CON_STATE_REQUEST_START,
	CON_STATE_READ,
	CON_STATE_REQUEST_END,
	CON_STATE_READ_POST,
	CON_STATE_HANDLE_REQUEST,
	CON_STATE_RESPONSE_START,
	CON_STATE_WRITE,
	CON_STATE_RESPONSE_END,
	CON_STATE_ERROR,
	CON_STATE_CLOSE
} connection_state_t;

typedef enum { T_CONFIG_UNSET,
		T_CONFIG_STRING,
		T_CONFIG_SHORT,
		T_CONFIG_INT,
		T_CONFIG_BOOLEAN,
		T_CONFIG_ARRAY,
		T_CONFIG_LOCAL,
		T_CONFIG_DEPRECATED,
		T_CONFIG_UNSUPPORTED
} config_values_type_t;

typedef enum { T_CONFIG_SCOPE_UNSET,
		T_CONFIG_SCOPE_SERVER,
		T_CONFIG_SCOPE_CONNECTION
} config_scope_type_t;

typedef struct {
	time_t  mtime;  /* the key */
	buffer *str;    /* a buffer for the string represenation */
} mtime_cache_type;

typedef struct {
        buffer *name;
        buffer *etag;

        struct stat st;

        time_t stat_ts;

#ifdef HAVE_LSTAT
        char is_symlink;
#endif

#ifdef HAVE_FAM_H
        int    dir_version;
        int    dir_ndx;
#endif

        buffer *content_type;
} stat_cache_entry;

typedef struct {
        splay_tree *files; /* the nodes of the tree are stat_cache_entry's */

        buffer *dir_name; /* for building the dirname from the filename */
#ifdef HAVE_FAM_H
        splay_tree *dirs; /* the nodes of the tree are fam_dir_entry */

        FAMConnection *fam;
        int    fam_fcce_ndx;
#endif
        buffer *hash_key;  /* temp-store for the hash-key */
} stat_cache;



typedef struct {
	const char *key;
	void *destination;

	config_values_type_t type;
	config_scope_type_t scope;
} config_values_t;

typedef union {
#ifdef HAVE_IPV6
	struct sockaddr_in6 ipv6;
#endif
	struct sockaddr_in ipv4;
#ifdef HAVE_SYS_UN_H
	struct sockaddr_un un;
#endif
	struct sockaddr plain;
} sock_addr;

typedef struct {
	unsigned short port;
	unsigned short live_pub_port;
	unsigned char use_zmq;
	buffer *bindhost;

	unsigned short max_worker;
	unsigned short max_fds;
	unsigned short max_conns;
	unsigned int max_request_size;

	/* close the connection when not any data be read after connection */
	int max_read_idle;
	/* idle time from last reading */
	int keep_alive_idle;
	int heart_beat_idle;


	buffer *network_backend;

	unsigned short log_state_handling;
	buffer *errorlog_file;
	unsigned short errorlog_use_syslog;
	buffer *breakagelog_file;
	unsigned short dont_daemonize;
	unsigned short enable_cores;

	array *upload_tempdirs;
	enum { STAT_CACHE_ENGINE_UNSET,
			STAT_CACHE_ENGINE_NONE,
			STAT_CACHE_ENGINE_SIMPLE
#ifdef HAVE_FAM_H
			, STAT_CACHE_ENGINE_FAM
#endif
	} stat_cache_engine;
} server_config;

typedef struct {
	sock_addr addr;
	int       fd;
	int       fde_ndx;

	buffer *ssl_pemfile;
	buffer *ssl_ca_file;
	buffer *ssl_cipher_list;
	buffer *ssl_dh_file;
	buffer *ssl_ec_curve;
	unsigned short ssl_use_sslv2;
	unsigned short ssl_use_sslv3;
	unsigned short use_ipv6;
	unsigned short is_ssl;

	buffer *srv_token;

#ifdef USE_OPENSSL
	SSL_CTX *ssl_ctx;
#endif
       unsigned short is_proxy_ssl;
} server_socket;

typedef struct {
	server_socket **ptr;

	size_t size;
	size_t used;
} server_socket_array;



typedef struct {
	array *mimetypes;
	unsigned short max_keep_alive_requests;
	unsigned short max_keep_alive_idle;
	unsigned short max_read_idle;
	unsigned short max_write_idle;
	unsigned short follow_symlink;
	unsigned short use_xattr;

	/* server wide */
	buffer *ssl_pemfile;
	buffer *ssl_ca_file;
	buffer *ssl_cipher_list;
	buffer *ssl_dh_file;
	buffer *ssl_ec_curve;
	unsigned short ssl_honor_cipher_order; /* determine SSL cipher in server-preferred order, not client-order */
	unsigned short ssl_use_sslv2;
	unsigned short ssl_use_sslv3;
	unsigned short ssl_verifyclient;
	unsigned short ssl_verifyclient_enforce;
	unsigned short ssl_verifyclient_depth;
	buffer *ssl_verifyclient_username;
	unsigned short ssl_verifyclient_export_cert;
	unsigned short ssl_disable_client_renegotiation;

	unsigned short use_ipv6, set_v6only; /* set_v6only is only a temporary option */
	unsigned short defer_accept;
	unsigned short is_ssl;

	unsigned short kbytes_per_second; /* connection kb/s limit */

	/* configside */
	unsigned short global_kbytes_per_second; /*  */

	off_t  global_bytes_per_second_cnt;
	/* server-wide traffic-shaper
	 *
	 * each context has the counter which is inited once
	 * a second by the global_kbytes_per_second config-var
	 *
	 * as soon as global_kbytes_per_second gets below 0
	 * the connected conns are "offline" a little bit
	 *
	 * the problem:
	 * we somehow have to loose our "we are writable" signal
	 * on the way.
	 *
	 */
	off_t *global_bytes_per_second_cnt_ptr; /*  */

#ifdef USE_OPENSSL
	SSL_CTX *ssl_ctx;
#endif

} specific_config;

typedef struct {
	/** HEADER */
	/* the request-line */
	buffer *request;

	http_method_t  http_method;

	buffer *request_line;
	array* headers;

	/* CONTENT */
	size_t content_length; /* returned by strtoul() */

} request;

typedef struct {
	connection_state_t state;

	int fd;                      /* the FD for this connection */
	int fde_ndx;                 /* index for the fdevent-handler */
	int ndx;                     /* reverse mapping to server->connection[ndx] */

	/* fd states */
	int is_readable;
	int is_writable;

	int keep_alive;              /* only request.c can enable it, all other just disable */
	int keep_alive_idle;         /* remember max_keep_alive_idle from config */

	specific_config conf;        /* global connection specific config */

	void *srv_socket;   /* reference to the server-socket (typecast to server_socket) */
	int http_status;



	off_t bytes_written;          /* used by mod_accesslog, mod_rrd */
	off_t bytes_written_cur_second; /* used by mod_accesslog, mod_rrd */
	off_t bytes_read;             /* used by mod_accesslog, mod_rrd */

	int traffic_limit_reached;

        /* timestamps */
        time_t read_idle_ts;
        time_t hb_idle_ts;
        time_t close_timeout_ts;
        time_t write_request_ts;
        
        time_t connection_start;

	/* start time of one new request */
        time_t request_start;
        
        struct timeval start_tv;
        
        size_t request_count;        /* number of requests handled in this connection */
        size_t loops_per_request;    /* to catch endless loops in a single request
                                      *
                                      * used by mod_rewrite, mod_fastcgi, ... and others
                                      * this is self-protection
                                      */
	int    in_joblist;

	request  request;

	chunkqueue *write_queue;      /* a large queue for low-level write ( HTTP response ) [ file, mem ] */
	chunkqueue *read_queue;       /* a small queue for low-level read ( HTTP request ) [ mem ] */
	chunkqueue *request_content_queue; /* takes request-content into tempfile if necessary [ tempfile, mem ]*/

	/* etag handling */
	etag_flags_t etag_flags;
	
	buffer* ip;

	int killme;

}connection;

typedef struct {
	connection **ptr;
	size_t size;
	size_t used;
} connections;

typedef struct server {

	/* keep all config */
	server_config  srvconf;

	specific_config **config_storage;
	array *config_context;

	server_socket_array srv_sockets;

	fdevents *ev, *ev_ins;

        /* counters */
        int con_opened;
        int con_read;
        int con_written;
        int con_closed;

        int ssl_is_init;


	int max_fds;    /* max possible fds */
	int cur_fds;    /* currently used fds */
	int want_fds;   /* waiting fds */

	size_t max_conns;

	connections *conns;
	connections *joblist;
	connections *fdwaitqueue;

	/* the errorlog */
	int errorlog_fd;
	enum { ERRORLOG_FILE, ERRORLOG_FD, ERRORLOG_SYSLOG, ERRORLOG_PIPE ,ERRORLOG_ZMQ,ERRORLOG_NONE,} errorlog_mode;
	buffer *errorlog_buf;

	/* Timestamps */
	time_t cur_ts;
	time_t last_generated_date_ts;
	time_t last_generated_debug_ts;
	buffer *ts_debug_str;
	buffer *ts_date_str;
	time_t startup_ts;


	fdevent_handler_t event_handler;

	int (* network_backend_write)(struct server *srv, connection *con, int fd, chunkqueue *cq, off_t max_bytes);
#ifdef USE_OPENSSL
	int (* network_ssl_backend_write)(struct server *srv, connection *con, SSL *ssl, chunkqueue *cq, off_t max_bytes);
#endif

	stat_cache  *stat_cache;
	mtime_cache_type mtime_cache[FILE_CACHE_MAX];
	int sockets_disabled;

	/* zmq */
	void * zmq_context;
	void * req_publisher;
	void * live_publisher;
	void * rsp_pull;
	void * log_pub;

	/* sendqueue */
	struct threadqueue* send_queue;
	struct threadqueue* live_send_queue;

	int live_client;
} server;


typedef struct{
        unsigned short hlen;
        unsigned short dlen;
        char* header;
        char* data;
}req_msg_t;



#endif
