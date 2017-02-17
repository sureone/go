#include "server.h"
#include "buffer.h"
#include "network.h"
#include "log.h"
#include "keyvalue.h"
#include "response.h"
#include "request.h"
#include "chunk.h"
#include "http_chunk.h"
#include "fdevent.h"
#include "connections.h"
#include "stat_cache.h"
#include "plugin.h"
#include "joblist.h"
#include "network_backends.h"

#include <sys/types.h>
#include <sys/time.h>
#include <sys/stat.h>

#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <time.h>
#include <signal.h>
#include <assert.h>
#include <locale.h>

#include <stdio.h>

#ifdef HAVE_GETOPT_H
# include <getopt.h>
#endif

#ifdef HAVE_VALGRIND_VALGRIND_H
# include <valgrind/valgrind.h>
#endif

#ifdef HAVE_SYS_WAIT_H
# include <sys/wait.h>
#endif

#ifdef HAVE_PWD_H
# include <grp.h>
# include <pwd.h>
#endif

#ifdef HAVE_SYS_RESOURCE_H
# include <sys/resource.h>
#endif

#ifdef HAVE_SYS_PRCTL_H
# include <sys/prctl.h>
#endif

#ifdef USE_OPENSSL
# include <openssl/err.h> 
#endif



#ifdef DMALLOC
#include "dmalloc.h"
#endif

#ifndef __sgi
/* IRIX doesn't like the alarm based time() optimization */
/* #define USE_ALARM */
#endif

#ifdef HAVE_GETUID
# ifndef HAVE_ISSETUGID

static int l_issetugid() {
	return (geteuid() != getuid() || getegid() != getgid());
}

#  define issetugid l_issetugid
# endif
#endif


static volatile sig_atomic_t srv_shutdown = 0;
static volatile sig_atomic_t graceful_shutdown = 0;
static volatile sig_atomic_t handle_sig_alarm = 1;
static volatile sig_atomic_t handle_sig_hup = 0;
static volatile sig_atomic_t forwarded_sig_hup = 0;

#if defined(HAVE_SIGACTION) && defined(SA_SIGINFO)
static volatile siginfo_t last_sigterm_info;
static volatile siginfo_t last_sighup_info;

static void* init_threads(server* srv,int num);

static void sigaction_handler(int sig, siginfo_t *si, void *context) {
	static siginfo_t empty_siginfo;
	UNUSED(context);

	if (!si) si = &empty_siginfo;

	switch (sig) {
	case SIGTERM:
		srv_shutdown = 1;
		last_sigterm_info = *si;
		break;
	case SIGINT:
		dprintf("You are going to shutdown the server?\n");
		if (graceful_shutdown) {
			srv_shutdown = 1;
		} else {
			graceful_shutdown = 1;
		}
		last_sigterm_info = *si;

		break;
	case SIGALRM: 
		handle_sig_alarm = 1; 
		break;
	case SIGHUP:
		/** 
		 * we send the SIGHUP to all procs in the process-group
		 * this includes ourself
		 * 
		 * make sure we only send it once and don't create a 
		 * infinite loop
		 */
		if (!forwarded_sig_hup) {
			handle_sig_hup = 1;
			last_sighup_info = *si;
		} else {
			forwarded_sig_hup = 0;
		}
		break;
	case SIGCHLD:
		break;
	}
}
#elif defined(HAVE_SIGNAL) || defined(HAVE_SIGACTION)
static void signal_handler(int sig) {
	switch (sig) {
	case SIGTERM: srv_shutdown = 1; break;
	case SIGINT:
	     if (graceful_shutdown) srv_shutdown = 1;
	     else graceful_shutdown = 1;

	     break;
	case SIGALRM: handle_sig_alarm = 1; break;
	case SIGHUP:  handle_sig_hup = 1; break;
	case SIGCHLD:  break;
	}
}
#endif

#ifdef HAVE_FORK
static void daemonize(void) {
#ifdef SIGTTOU
	signal(SIGTTOU, SIG_IGN);
#endif
#ifdef SIGTTIN
	signal(SIGTTIN, SIG_IGN);
#endif
#ifdef SIGTSTP
	signal(SIGTSTP, SIG_IGN);
#endif
	if (0 != fork()) exit(0);

	if (-1 == setsid()) exit(0);

	signal(SIGHUP, SIG_IGN);

	if (0 != fork()) exit(0);

	if (0 != chdir("/")) exit(0);
}
#endif

int areYouOK(server* srv,connection* con)
{
    if (con!=NULL)
    {
	buffer *b = buffer_init();
	buffer_append_long(b,1);
        buffer_append_string(b, ",");
	buffer_append_long(b,con->fd);
        buffer_append_string(b, ",");
        buffer_append_string(b, "notify:areyouok,");
        buffer_append_string(b, "\r\n\r\n");
	char* data = calloc(1,b->used+2);
	strcpy(data,b->ptr);
	buffer_free(b);
	thread_queue_add(srv->send_queue,(void*)data,0);
        return 0;
    }
    return -1;
}

int heartBeat(server* srv,connection* con)
{
    if (con!=NULL)
    {
        buffer *b = buffer_init();
        buffer_append_long(b,1);
        buffer_append_string(b, ",");
        buffer_append_long(b,con->fd);
        buffer_append_string(b, ",");
        buffer_append_string(b, "notify:heartbeat,");
        buffer_append_string(b, "\r\n\r\n");
        char* data = calloc(1,b->used+2);
        strcpy(data,b->ptr);
        buffer_free(b);
        thread_queue_add(srv->send_queue,(void*)data,0);
        return 0;
    }
    return -1;
}



void *rsp_send_proc(void *ptr){
	server* srv = (server*)ptr;
	while(1){
		struct threadmsg *msg = (struct threadmsg*)malloc(sizeof(struct threadmsg));
		thread_queue_get(srv->send_queue,NULL,msg);
		thread_send_msg(srv,msg);
		free(msg);
	}
}

void process_live_msg(void* ptr,int socket);

void *live_udp_pub_proc(void *ptr){

   server* srv = (server*)ptr;	
int parentfd; /* parent socket */
  int childfd; /* child socket */
  int portno; /* port to listen on */
  int clientlen; /* byte size of client's address */
  struct sockaddr_in serveraddr; /* server's addr */
  struct sockaddr_in clientaddr; /* client addr */
  struct hostent *hostp; /* client host info */
  char buf[200]; /* message buffer */
  char *hostaddrp; /* dotted decimal host addr string */
  int optval; /* flag value for setsockopt */
  int n; /* message byte size */
   srv->live_client=0;
 
  portno = 8004;

  /* 
   * socket: create the parent socket 
   */
  parentfd = socket(AF_INET, SOCK_STREAM, 0);
  if (parentfd < 0) 
    // fprintf(stderr,"ERROR opening socket");
    return 0;

  /* setsockopt: Handy debugging trick that lets 
   * us rerun the server immediately after we kill it; 
   * otherwise we have to wait about 20 secs. 
   * Eliminates "ERROR on binding: Address already in use" error. 
   */
  optval = 1;
  setsockopt(parentfd, SOL_SOCKET, SO_REUSEADDR, 
	     (const void *)&optval , sizeof(int));

  /*
   * build the server's Internet address
   */
  bzero((char *) &serveraddr, sizeof(serveraddr));

  /* this is an Internet address */
  serveraddr.sin_family = AF_INET;

  /* let the system figure out our IP address */
  serveraddr.sin_addr.s_addr = htonl(INADDR_ANY);

  /* this is the port we will listen on */
  serveraddr.sin_port = htons((unsigned short)portno);

  /* 
   * bind: associate the parent socket with a port 
   */
  if (bind(parentfd, (struct sockaddr *) &serveraddr, 
	   sizeof(serveraddr)) < 0) 
    // fprintf(stderr,"ERROR on binding");
    return 0;

  /* 
   * listen: make this socket ready to accept connection requests 
   */
  if (listen(parentfd, 5) < 0) /* allow 5 requests to queue up */ 
    // fprintf(stderr,"ERROR on listen");
   return 0;

  /* 
   * main loop: wait for a connection request, echo input line, 
   * then close connection.
   */
  clientlen = sizeof(clientaddr);
  while (1) {

    /* 
     * accept: wait for a connection request 
     */
    childfd = accept(parentfd, (struct sockaddr *) &clientaddr, &clientlen);
    if (childfd < 0) 
      // fprintf(stderr,"ERROR on accept");//
    	continue;

  
    /* 
     * gethostbyaddr: determine who sent the message 
     */
    // hostp = gethostbyaddr((const char *)&clientaddr.sin_addr.s_addr, 
			  // sizeof(clientaddr.sin_addr.s_addr), AF_INET);
    // if (hostp == NULL)
      // fprintf(stderr,"ERROR on gethostbyaddr");
    	// continue;
    // hostaddrp = inet_ntoa(clientaddr.sin_addr);
    // if (hostaddrp == NULL)
      // fprintf(stderr,"ERROR on inet_ntoa\n");
    	// continue;
    // fprintf(stderr,"server established connection with %s (%s)\n", 
	   // hostp->h_name, hostaddrp);
    
    srv->live_client=childfd;
    process_live_msg(ptr,childfd);
    srv->live_client=0;


    close(childfd);
  }

   
}


void process_live_msg(void* ptr,int childfd){
		server* srv = (server*)ptr;
		int n=0;
	while(1){
		struct threadmsg *msg = (struct threadmsg*)malloc(sizeof(struct threadmsg));


		thread_queue_get(srv->live_send_queue,NULL,msg);

		char* data = msg->data;
		

		n = write(childfd, data, strlen(data));

		free(data);
		free(msg);



	    if (n < 0){ 
	      fprintf(stderr,"ERROR writing to socket");
	      break;
	   }

	   
	}

}


void *rsp_read_proc(void *ptr){
	#if(0)
	server* srv = (server*)ptr;
	WS("response read thread start");
	while(1){
		char* data = s_recv(srv->rsp_pull);
		thread_queue_add(srv->send_queue,(void*)data,0);
		WS(data);
	}
	#endif
}
int process_response(void* context,buffer* b){
	server* srv=(server*)context;
	char* data = calloc(1,b->used+2);
	strcpy(data,b->ptr);
	//send_response_to_client(srv,data);
	thread_queue_add(srv->send_queue,(char*)data,0);


	
}




static server *gs=0;

static server *server_init(void) {
	int i;
	FILE *frandom = NULL;
	//setup_log(ERRORLOG_NONE,STDERR_FILENO,NULL,0);
	setup_log(ERRORLOG_FD,STDERR_FILENO,NULL,0);
	// setup_log(ERRORLOG_FILE,NULL,"ipface.log",0);

	server *srv = calloc(1, sizeof(*srv));
	gs = srv;
	assert(srv);
#define CLEAN(x) \
	srv->x = buffer_init();

	CLEAN(ts_debug_str);
	CLEAN(ts_date_str);
	CLEAN(errorlog_buf);

	CLEAN(srvconf.errorlog_file);
	CLEAN(srvconf.bindhost);
#undef CLEAN

#define CLEAN(x) \
	srv->x = array_init();

	CLEAN(config_context);
#undef CLEAN

	for (i = 0; i < FILE_CACHE_MAX; i++) {
		srv->mtime_cache[i].mtime = (time_t)-1;
		srv->mtime_cache[i].str = buffer_init();
	}


	srv->cur_ts = time(NULL);
	srv->startup_ts = srv->cur_ts;

	srv->conns = calloc(1, sizeof(*srv->conns));
	assert(srv->conns);

	srv->joblist = calloc(1, sizeof(*srv->joblist));
	assert(srv->joblist);

	srv->fdwaitqueue = calloc(1, sizeof(*srv->fdwaitqueue));
	assert(srv->fdwaitqueue);

	srv->srvconf.network_backend = buffer_init();

	srv->srvconf.errorlog_use_syslog=0;	
	srv->errorlog_fd = STDERR_FILENO;
	srv->errorlog_mode = ERRORLOG_FD;




	return srv;
}

void publish_live_string(void* ptr,char* s){
}



static void* init_threads(server* srv,int num){
    	pthread_t worker = 0;
    	int tid;
	srv->send_queue = (struct threadqueue*)malloc(sizeof(struct threadqueue));
	thread_queue_init(srv->send_queue);
	srv->live_send_queue = (struct threadqueue*)malloc(sizeof(struct threadqueue));
	thread_queue_init(srv->live_send_queue);
	
	for(int i=0;i<num;i++){
    		tid = pthread_create(&worker,NULL,rsp_send_proc,(void*)srv);
	}


	tid = pthread_create(&worker,NULL,live_udp_pub_proc,(void*)srv);

    //tid = pthread_create(&worker,NULL,rsp_read_proc,(void*)srv);
}

static void app_init(server* srv){

    init_threads(srv,1);    
}

void zmq_write_log(char* s){
}



static void server_free(server *srv) {
	size_t i;

	for (i = 0; i < FILE_CACHE_MAX; i++) {
		buffer_free(srv->mtime_cache[i].str);
	}

#define CLEAN(x) \
	buffer_free(srv->x);

	CLEAN(ts_debug_str);
	CLEAN(ts_date_str);
	CLEAN(errorlog_buf);

	CLEAN(srvconf.errorlog_file);
	CLEAN(srvconf.bindhost);
	CLEAN(srvconf.network_backend);

#undef CLEAN

#if 0
	fdevent_unregister(srv->ev, srv->fd);
#endif
	fdevent_free(srv->ev);

	free(srv->conns);

	if (srv->config_storage) {
		for (i = 0; i < srv->config_context->used; i++) {
			specific_config *s = srv->config_storage[i];

			if (!s) continue;

			buffer_free(s->ssl_pemfile);
			buffer_free(s->ssl_ca_file);
			buffer_free(s->ssl_cipher_list);
			buffer_free(s->ssl_dh_file);
			buffer_free(s->ssl_ec_curve);
			array_free(s->mimetypes);
			buffer_free(s->ssl_verifyclient_username);
#ifdef USE_OPENSSL
			SSL_CTX_free(s->ssl_ctx);
#endif
			free(s);
		}
		free(srv->config_storage);
		srv->config_storage = NULL;
	}

#define CLEAN(x) \
	array_free(srv->x);

	CLEAN(config_context);
#undef CLEAN

	joblist_free(srv, srv->joblist);
	fdwaitqueue_free(srv, srv->fdwaitqueue);

	if (srv->stat_cache) {
		stat_cache_free(srv->stat_cache);
	}


#ifdef USE_OPENSSL
	if (srv->ssl_is_init) {
		CRYPTO_cleanup_all_ex_data();
		ERR_free_strings();
		ERR_remove_state(0);
		EVP_cleanup();
	}
#endif


	clear_log();
	free(srv);
}


static void show_features (void) {
  const char features[] = ""
#ifdef USE_SELECT
      "\t+ select (generic)\n"
#else
      "\t- select (generic)\n"
#endif
#ifdef USE_POLL
      "\t+ poll (Unix)\n"
#else
      "\t- poll (Unix)\n"
#endif
#ifdef USE_LINUX_SIGIO
      "\t+ rt-signals (Linux 2.4+)\n"
#else
      "\t- rt-signals (Linux 2.4+)\n"
#endif
#ifdef USE_LINUX_EPOLL
      "\t+ epoll (Linux 2.6)\n"
#else
      "\t- epoll (Linux 2.6)\n"
#endif
#ifdef USE_SOLARIS_DEVPOLL
      "\t+ /dev/poll (Solaris)\n"
#else
      "\t- /dev/poll (Solaris)\n"
#endif
#ifdef USE_SOLARIS_PORT
      "\t+ eventports (Solaris)\n"
#else
      "\t- eventports (Solaris)\n"
#endif
#ifdef USE_FREEBSD_KQUEUE
      "\t+ kqueue (FreeBSD)\n"
#else
      "\t- kqueue (FreeBSD)\n"
#endif
#ifdef USE_LIBEV
      "\t+ libev (generic)\n"
#else
      "\t- libev (generic)\n"
#endif
      "\nNetwork handler:\n\n"
#if defined(USE_LINUX_SENDFILE) || defined(USE_FREEBSD_SENDFILE) || defined(USE_SOLARIS_SENDFILEV) || defined(USE_AIX_SENDFILE)
      "\t+ sendfile\n"
#else
  #ifdef USE_WRITEV
      "\t+ writev\n"
  #else
      "\t+ write\n"
  #endif
  #ifdef USE_MMAP
      "\t+ mmap support\n"
  #else
      "\t- mmap support\n"
  #endif
#endif
      "\nFeatures:\n\n"
#ifdef HAVE_IPV6
      "\t+ IPv6 support\n"
#else
      "\t- IPv6 support\n"
#endif
#if defined HAVE_ZLIB_H && defined HAVE_LIBZ
      "\t+ zlib support\n"
#else
      "\t- zlib support\n"
#endif
#if defined HAVE_BZLIB_H && defined HAVE_LIBBZ2
      "\t+ bzip2 support\n"
#else
      "\t- bzip2 support\n"
#endif
#ifdef HAVE_LIBCRYPT
      "\t+ crypt support\n"
#else
      "\t- crypt support\n"
#endif
#ifdef USE_OPENSSL
      "\t+ SSL Support\n"
#else
      "\t- SSL Support\n"
#endif
#ifdef HAVE_LIBPCRE
      "\t+ PCRE support\n"
#else
      "\t- PCRE support\n"
#endif
#ifdef HAVE_MYSQL
      "\t+ mySQL support\n"
#else
      "\t- mySQL support\n"
#endif
#if defined(HAVE_LDAP_H) && defined(HAVE_LBER_H) && defined(HAVE_LIBLDAP) && defined(HAVE_LIBLBER)
      "\t+ LDAP support\n"
#else
      "\t- LDAP support\n"
#endif
#ifdef HAVE_MEMCACHE_H
      "\t+ memcached support\n"
#else
      "\t- memcached support\n"
#endif
#ifdef HAVE_FAM_H
      "\t+ FAM support\n"
#else
      "\t- FAM support\n"
#endif
#ifdef HAVE_LUA_H
      "\t+ LUA support\n"
#else
      "\t- LUA support\n"
#endif
#ifdef HAVE_LIBXML_H
      "\t+ xml support\n"
#else
      "\t- xml support\n"
#endif
#ifdef HAVE_SQLITE3_H
      "\t+ SQLite support\n"
#else
      "\t- SQLite support\n"
#endif
#ifdef HAVE_GDBM_H
      "\t+ GDBM support\n"
#else
      "\t- GDBM support\n"
#endif
      "\n";
  printf("\nEvent Handlers:\n\n%s", features);
}

#define MAIN_PORT 9091

#define LIVE_PUB_PORT 8004

int config_set_defaults(server *srv){
	srv->srvconf.port = MAIN_PORT;
	srv->srvconf.live_pub_port = LIVE_PUB_PORT;
	srv->srvconf.use_zmq=0;
	srv->srvconf.max_read_idle=60;
	srv->srvconf.keep_alive_idle=30;
	srv->srvconf.heart_beat_idle=5;
	buffer_append_string(srv->srvconf.bindhost,"0.0.0.0");
	srv->srvconf.dont_daemonize = 1;
	srv->srvconf.log_state_handling = 1;
	//srv->event_handler = FDEVENT_HANDLER_LINUX_SYSEPOLL;
	srv->event_handler = FDEVENT_HANDLER_SELECT;

	srv->config_storage = calloc(1, 1 * sizeof(specific_config *));
        assert(srv->config_storage);
                specific_config *s = srv->config_storage[0];
                s = calloc(1, sizeof(specific_config));
                assert(s);
                s->ssl_pemfile   = buffer_init();
                s->ssl_ca_file   = buffer_init();
                s->ssl_cipher_list = buffer_init();
                s->ssl_dh_file   = buffer_init();
                s->ssl_ec_curve  = buffer_init();
                s->max_keep_alive_requests = 16;
                s->max_keep_alive_idle = 5;
                s->max_read_idle = 60;
                s->max_write_idle = 360;
                s->use_xattr     = 0;
                s->is_ssl        = 0;
                s->ssl_honor_cipher_order = 1;
                s->ssl_use_sslv2 = 0;
                s->ssl_use_sslv3 = 1;
                s->use_ipv6      = 0;
                s->set_v6only    = 1;
                s->defer_accept  = 0;
#ifdef HAVE_LSTAT
                s->follow_symlink = 1;
#endif
                s->kbytes_per_second = 0;
                s->global_kbytes_per_second = 0;
                s->global_bytes_per_second_cnt = 0;
                s->global_bytes_per_second_cnt_ptr = &s->global_bytes_per_second_cnt;
                s->ssl_verifyclient = 0;
                s->ssl_verifyclient_enforce = 1;
                s->ssl_verifyclient_username = buffer_init();
                s->ssl_verifyclient_depth = 9;
                s->ssl_verifyclient_export_cert = 0;
                s->ssl_disable_client_renegotiation = 1;
		srv->config_storage[0] = s;

	return 0;
}

int main (int argc, char **argv) {
	server *srv = NULL;
	int o;
	int num_childs = 0;
	int i_am_root = 0;	
	size_t i;
	int fd;
#ifdef HAVE_SIGACTION
	struct sigaction act;
#endif
#ifdef HAVE_GETRLIMIT
	struct rlimit rlim;
#endif

#ifdef USE_ALARM
	struct itimerval interval;

	interval.it_interval.tv_sec = 1;
	interval.it_interval.tv_usec = 0;
	interval.it_value.tv_sec = 1;
	interval.it_value.tv_usec = 0;
#endif


	/* for nice %b handling in strfime() */
	setlocale(LC_TIME, "C");

	if (NULL == (srv = server_init())) {
		fprintf(stderr, "did this really happen?\n");
		return -1;
	}

	/* load default config */
	config_set_defaults(srv);

	/* init structs done */
#ifdef HAVE_GETUID
	i_am_root = (getuid() == 0);
#else
	i_am_root = 0;
#endif

	while(-1 != (o = getopt(argc, argv, "f:m:hvVDp:u:Z:t"))) {
		switch(o) {
		case 'D': srv->srvconf.dont_daemonize = 1; break;
		case 'V': show_features();{
			break;
		}
		case 'p':{
			srv->srvconf.port = atoi(optarg);
                	log_error_write(__FILE__, __LINE__, "ss",
				 "Listening on port ",optarg);
			
			
			break;
		}
		case 'u':{
			srv->srvconf.live_pub_port = atoi(optarg);
              
			break;
		}
		case 'Z':{
			srv->srvconf.use_zmq=atoi(optarg);
              
			break;
		}
		default:
			break;
		}
	}


	/* close stdin and stdout, as they are not needed */
	openDevNull(STDIN_FILENO);
	openDevNull(STDOUT_FILENO);

        /* Close stderr ASAP in the child process to make sure that nothing
         * is being written to that fd which may not be valid anymore. */
        if (-1 == log_error_open()) {
                log_error_write(__FILE__, __LINE__, "s", "Opening errorlog failed. Going down.");

                server_free(srv);
                return -1;
        }



	if (srv->event_handler == FDEVENT_HANDLER_SELECT) {
		/* select limits itself
		 *
		 * as it is a hard limit and will lead to a segfault we add some safety
		 * */
		srv->max_fds = FD_SETSIZE - 200;
	} else {
		srv->max_fds = 4096;
	}

	if (i_am_root) {
		struct group *grp = NULL;
		struct passwd *pwd = NULL;
		int use_rlimit = 1;

#ifdef HAVE_VALGRIND_VALGRIND_H
		if (RUNNING_ON_VALGRIND) use_rlimit = 0;
#endif

#ifdef HAVE_GETRLIMIT
		if (0 != getrlimit(RLIMIT_NOFILE, &rlim)) {
			log_error_write(__FILE__, __LINE__,
					"ss", "couldn't get 'max filedescriptors'",
					strerror(errno));
			return -1;
		}

		if (use_rlimit && srv->srvconf.max_fds) {
			/* set rlimits */
			rlim.rlim_cur = srv->srvconf.max_fds;
			rlim.rlim_max = srv->srvconf.max_fds;

			if (0 != setrlimit(RLIMIT_NOFILE, &rlim)) {
				log_error_write(__FILE__, __LINE__,
						"ss", "couldn't set 'max filedescriptors'",
						strerror(errno));
				return -1;
			}
		}

		if (srv->event_handler == FDEVENT_HANDLER_SELECT) {
			srv->max_fds = rlim.rlim_cur < ((int)FD_SETSIZE) - 200 ? rlim.rlim_cur : FD_SETSIZE - 200;
		} else {
			srv->max_fds = rlim.rlim_cur;
		}

		/* set core file rlimit, if enable_cores is set */
		if (use_rlimit && srv->srvconf.enable_cores && getrlimit(RLIMIT_CORE, &rlim) == 0) {
			rlim.rlim_cur = rlim.rlim_max;
			setrlimit(RLIMIT_CORE, &rlim);
		}
#endif
		if (srv->event_handler == FDEVENT_HANDLER_SELECT) {
			/* don't raise the limit above FD_SET_SIZE */
			if (srv->max_fds > ((int)FD_SETSIZE) - 200) {
				log_error_write(__FILE__, __LINE__, "sd",
						"can't raise max filedescriptors above",  FD_SETSIZE - 200,
						"if event-handler is 'select'. Use 'poll' or something else or reduce server.max-fds.");
				return -1;
			}
		}

		/* we need root-perms for port < 1024 */
		if (0 != network_init(srv)) {
			server_free(srv);
			return -1;
		}
#if defined(HAVE_SYS_PRCTL_H) && defined(PR_SET_DUMPABLE)
		/**
		 * on IRIX 6.5.30 they have prctl() but no DUMPABLE
		 */
		if (srv->srvconf.enable_cores) {
			prctl(PR_SET_DUMPABLE, 1, 0, 0, 0);
		}
#endif
	} else {

#ifdef HAVE_GETRLIMIT
		if (0 != getrlimit(RLIMIT_NOFILE, &rlim)) {
			log_error_write(__FILE__, __LINE__,
					"ss", "couldn't get 'max filedescriptors'",
					strerror(errno));
			return -1;
		}

		/**
		 * we are not root can can't increase the fd-limit, but we can reduce it
		 */
		if (srv->srvconf.max_fds && srv->srvconf.max_fds < rlim.rlim_cur) {
			/* set rlimits */

			rlim.rlim_cur = srv->srvconf.max_fds;

			if (0 != setrlimit(RLIMIT_NOFILE, &rlim)) {
				log_error_write(__FILE__, __LINE__,
						"ss", "couldn't set 'max filedescriptors'",
						strerror(errno));
				return -1;
			}
		}

		if (srv->event_handler == FDEVENT_HANDLER_SELECT) {
			srv->max_fds = rlim.rlim_cur < ((int)FD_SETSIZE) - 200 ? rlim.rlim_cur : FD_SETSIZE - 200;
		} else {
			srv->max_fds = rlim.rlim_cur;
		}

		/* set core file rlimit, if enable_cores is set */
		if (srv->srvconf.enable_cores && getrlimit(RLIMIT_CORE, &rlim) == 0) {
			rlim.rlim_cur = rlim.rlim_max;
			setrlimit(RLIMIT_CORE, &rlim);
		}

#endif

#ifndef __CYGWIN__
	if (srv->event_handler == FDEVENT_HANDLER_SELECT) {
		/* don't raise the limit above FD_SET_SIZE */
		if (srv->max_fds > ((int)FD_SETSIZE) - 200) {
			log_error_write(__FILE__, __LINE__, "sd",
					"can't raise max filedescriptors above",  FD_SETSIZE - 200,
					"if event-handler is 'select'. Use 'poll' or something else or reduce server.max-fds.");
			return -1;
		}
	}
#endif

		if (0 != network_init(srv)) {
			server_free(srv);

			return -1;
		}
	}

	/* set max-conns */
	if (srv->srvconf.max_conns > srv->max_fds/2) {
		/* we can't have more connections than max-fds/2 */
		log_error_write(__FILE__, __LINE__, "sdd", "can't have more connections than fds/2: ", srv->srvconf.max_conns, srv->max_fds);
		srv->max_conns = srv->max_fds/2;
	} else if (srv->srvconf.max_conns) {
		/* otherwise respect the wishes of the user */
		srv->max_conns = srv->srvconf.max_conns;
	} else {
		/* or use the default: we really don't want to hit max-fds */
		srv->max_conns = srv->max_fds/3;
	}


#ifdef HAVE_FORK
	/* network is up, let's deamonize ourself */
	if (srv->srvconf.dont_daemonize == 0) daemonize();
#endif


	/* Close stderr ASAP in the child process to make sure that nothing
	 * is being written to that fd which may not be valid anymore. */
	if (-1 == log_error_open(srv)) {
		log_error_write(__FILE__, __LINE__, "s", "Opening errorlog failed. Going down.");

		network_close(srv);
		server_free(srv);
		return -1;
	}


#ifdef HAVE_SIGACTION
	memset(&act, 0, sizeof(act));
	act.sa_handler = SIG_IGN;
	sigaction(SIGPIPE, &act, NULL);
	sigaction(SIGUSR1, &act, NULL);
# if defined(SA_SIGINFO)
	act.sa_sigaction = sigaction_handler;
	sigemptyset(&act.sa_mask);
	act.sa_flags = SA_SIGINFO;
# else
	act.sa_handler = signal_handler;
	sigemptyset(&act.sa_mask);
	act.sa_flags = 0;
# endif
	sigaction(SIGINT,  &act, NULL);
	sigaction(SIGTERM, &act, NULL);
	sigaction(SIGHUP,  &act, NULL);
	sigaction(SIGALRM, &act, NULL);
	sigaction(SIGCHLD, &act, NULL);
	//sureone ignore simply ignore SIGPIPE. It's a useless, annoying signal.
	//http://stackoverflow.com/questions/8829238/how-can-i-trap-a-signal-sigpipe-for-a-socket-that-closes
	//http://stackoverflow.com/questions/6821469/how-to-prevent-sigpipe-or-prevent-the-server-from-ending
	//signal(SIGPIPE, SIG_IGN);

#elif defined(HAVE_SIGNAL)
	/* ignore the SIGPIPE from sendfile() */
	signal(SIGPIPE, SIG_IGN);
	signal(SIGUSR1, SIG_IGN);
	signal(SIGALRM, signal_handler);
	signal(SIGTERM, signal_handler);
	signal(SIGHUP,  signal_handler);
	signal(SIGCHLD,  signal_handler);
	signal(SIGINT,  signal_handler);
#endif

#ifdef USE_ALARM
	signal(SIGALRM, signal_handler);

	/* setup periodic timer (1 second) */
	if (setitimer(ITIMER_REAL, &interval, NULL)) {
		log_error_write(__FILE__, __LINE__, "s", "setting timer failed");
		return -1;
	}

	getitimer(ITIMER_REAL, &interval);
#endif

#ifdef HAVE_FORK
	/* start watcher and workers */
	num_childs = srv->srvconf.max_worker;
	if (num_childs > 0) {
		int child = 0;
		while (!child && !srv_shutdown && !graceful_shutdown) {
			if (num_childs > 0) {
				switch (fork()) {
				case -1:
					return -1;
				case 0:
					child = 1;
					break;
				default:
					num_childs--;
					break;
				}
			} else {
				int status;

				if (-1 != wait(&status)) {
					/** 
					 * one of our workers went away 
					 */
					num_childs++;
				} else {
					switch (errno) {
					case EINTR:
						/**
						 * if we receive a SIGHUP we have to close our logs ourself as we don't 
						 * have the mainloop who can help us here
						 */
						if (handle_sig_hup) {
							handle_sig_hup = 0;

							log_error_cycle(srv);

							/**
							 * forward to all procs in the process-group
							 * 
							 * we also send it ourself
							 */
							if (!forwarded_sig_hup) {
								forwarded_sig_hup = 1;
								kill(0, SIGHUP);
							}
						}
						break;
					default:
						break;
					}
				}
			}
		}

		/**
		 * for the parent this is the exit-point 
		 */
		if (!child) {
			/** 
			 * kill all children too 
			 */
			if (graceful_shutdown) {
				kill(0, SIGINT);
			} else if (srv_shutdown) {
				kill(0, SIGTERM);
			}

			log_error_close(srv);
			network_close(srv);
			connections_free(srv);
			server_free(srv);
			return 0;
		}
	}
#endif

	log_error_write(__FILE__,__LINE__,"s","server started ok");

	if (NULL == (srv->ev = fdevent_init(srv, srv->max_fds + 1, srv->event_handler))) {
		log_error_write(__FILE__, __LINE__,
				"s", "fdevent_init failed");
		return -1;
	}

	/* libev backend overwrites our SIGCHLD handler and calls waitpid on SIGCHLD; we want our own SIGCHLD handling. */
#ifdef HAVE_SIGACTION
	sigaction(SIGCHLD, &act, NULL);
#elif defined(HAVE_SIGNAL)
	signal(SIGCHLD,  signal_handler);
#endif

	/*
	 * kqueue() is called here, select resets its internals,
	 * all server sockets get their handlers
	 *
	 * */
	if (0 != network_register_fdevents(srv)) {
		network_close(srv);
		server_free(srv);

		return -1;
	}

	/* might fail if user is using fam (not gamin) and famd isn't running */
	if (NULL == (srv->stat_cache = stat_cache_init())) {
		log_error_write(__FILE__, __LINE__, "s",
			"stat-cache could not be setup, dieing.");
		return -1;
	}

#ifdef HAVE_FAM_H
	/* setup FAM */
	if (srv->srvconf.stat_cache_engine == STAT_CACHE_ENGINE_FAM) {
		if (0 != FAMOpen2(srv->stat_cache->fam, "lighttpd")) {
			log_error_write(__FILE__, __LINE__, "s",
					 "could not open a fam connection, dieing.");
			return -1;
		}
#ifdef HAVE_FAMNOEXISTS
		FAMNoExists(srv->stat_cache->fam);
#endif

		srv->stat_cache->fam_fcce_ndx = -1;
		fdevent_register(srv->ev, FAMCONNECTION_GETFD(srv->stat_cache->fam), stat_cache_handle_fdevent, NULL);
		fdevent_event_set(srv->ev, &(srv->stat_cache->fam_fcce_ndx), FAMCONNECTION_GETFD(srv->stat_cache->fam), FDEVENT_IN);
	}
#endif


	/* get the current number of FDs */
	srv->cur_fds = open("/dev/null", O_RDONLY);
	close(srv->cur_fds);

	for (i = 0; i < srv->srv_sockets.used; i++) {
		server_socket *srv_socket = srv->srv_sockets.ptr[i];
		if (-1 == fdevent_fcntl_set(srv->ev, srv_socket->fd)) {
			log_error_write(__FILE__, __LINE__, "ss", "fcntl failed:", strerror(errno));
			return -1;
		}
	}

	app_init(srv);
	mgo_init(srv);


	/* main-loop */
	while (!srv_shutdown) {
		int n;
		size_t ndx;
		time_t min_ts;

		if (handle_sig_hup) {
			handler_t r;
			/* reset notification */
			handle_sig_hup = 0;
		}

		if (handle_sig_alarm) {
			/* a new second */

#ifdef USE_ALARM
			/* reset notification */
			handle_sig_alarm = 0;
#endif

			/* get current time */
			min_ts = time(NULL);
			

			
			if (min_ts != srv->cur_ts) {
			
				int cs = 0;
				connections *conns = srv->conns;
				handler_t r;
			

				/* trigger waitpid */
				srv->cur_ts = min_ts;

				/* cleanup stat-cache */
				stat_cache_trigger_cleanup(srv);
				/**
				 * check all connections for timeouts
				 *
				 */
				modgo_tick();
				for (ndx = 0; ndx < conns->used; ndx++) {
					int changed = 0;
					connection *con;
					int t_diff;

					con = conns->ptr[ndx];

					if (con->state == CON_STATE_READ ||
					    con->state == CON_STATE_READ_POST) {
						if (con->request_count == 1) {
							if (srv->cur_ts - con->read_idle_ts > srv->srvconf.max_read_idle) {
								/* time - out */
								log_error_write(__FILE__, __LINE__, "sd",
										"connection closed - read-timeout:", con->fd);
								connection_set_state(srv, con, CON_STATE_ERROR);
								changed = 1;
							}
						} else {
                            				int idle_cnt = srv->cur_ts - con->read_idle_ts;
							/*
							if (srv->cur_ts - con->hb_idle_ts > srv->srvconf.heart_beat_idle) {
								heartBeat(srv,con);
								con->hb_idle_ts=srv->cur_ts;

						        }
							*/
							if (srv->cur_ts - con->read_idle_ts > srv->srvconf.keep_alive_idle) {
								/* time - out */
								log_error_write(__FILE__, __LINE__, "sd",
										"connection closed - read-timeout:", con->fd);
								if(con->killme<1){
									areYouOK(srv,con);
									con->killme=1;
									con->read_idle_ts=srv->cur_ts;
								} else {
									connection_set_state(srv, con, CON_STATE_ERROR);
									changed = 1;
								}
							} 

						}
					}

					if ((con->state == CON_STATE_WRITE) &&
					    (con->write_request_ts != 0)) {
#if 0
						if (srv->cur_ts - con->write_request_ts > 60) {
							log_error_write(__FILE__, __LINE__, "sdd",
									"connection closed - pre-write-request-timeout:", con->fd, srv->cur_ts - con->write_request_ts);
						}
#endif

						if (srv->cur_ts - con->write_request_ts > con->conf.max_write_idle) {
							/* time - out */
							log_error_write(__FILE__, __LINE__, "sosds",
								"timed out after writing",
								con->bytes_written,
								"bytes. We waited",
								(int)con->conf.max_write_idle,
								"seconds. If this a problem increase server.max-write-idle");
							connection_set_state(srv, con, CON_STATE_ERROR);
							changed = 1;
						}
					}

					if (con->state == CON_STATE_CLOSE && (srv->cur_ts - con->close_timeout_ts > HTTP_LINGER_TIMEOUT)) {
						changed = 1;
					}

					/* we don't like div by zero */
					if (0 == (t_diff = srv->cur_ts - con->connection_start)) t_diff = 1;

					if (con->traffic_limit_reached &&
					    (con->conf.kbytes_per_second == 0 ||
					     ((con->bytes_written / t_diff) < con->conf.kbytes_per_second * 1024))) {
						/* enable connection again */
						con->traffic_limit_reached = 0;

						changed = 1;
					}

					if (changed) {
						connection_state_machine(srv, con);
					}
					con->bytes_written_cur_second = 0;
					*(con->conf.global_bytes_per_second_cnt_ptr) = 0;

#if 0
					if (cs == 0) {
						fprintf(stderr, "connection-state: ");
						cs = 1;
					}

					fprintf(stderr, "c[%d,%d]: %s ",
						con->fd,
						con->fcgi.fd,
						connection_get_state(con->state));
#endif
				}

				if (cs == 1) fprintf(stderr, "\n");
			}
		}

		if (srv->sockets_disabled) {
			/* our server sockets are disabled, why ? */

			if ((srv->cur_fds + srv->want_fds < srv->max_fds * 8 / 10) && /* we have enough unused fds */
			    (srv->conns->used <= srv->max_conns * 9 / 10) &&
			    (0 == graceful_shutdown)) {
				for (i = 0; i < srv->srv_sockets.used; i++) {
					server_socket *srv_socket = srv->srv_sockets.ptr[i];
					fdevent_event_set(srv->ev, &(srv_socket->fde_ndx), srv_socket->fd, FDEVENT_IN);
				}

				log_error_write(__FILE__, __LINE__, "s", "[note] sockets enabled again");

				srv->sockets_disabled = 0;
			}
		} else {
			if ((srv->cur_fds + srv->want_fds > srv->max_fds * 9 / 10) || /* out of fds */
			    (srv->conns->used >= srv->max_conns) || /* out of connections */
			    (graceful_shutdown)) { /* graceful_shutdown */

				/* disable server-fds */

				for (i = 0; i < srv->srv_sockets.used; i++) {
					server_socket *srv_socket = srv->srv_sockets.ptr[i];
					fdevent_event_del(srv->ev, &(srv_socket->fde_ndx), srv_socket->fd);

					if (graceful_shutdown) {
						/* we don't want this socket anymore,
						 *
						 * closing it right away will make it possible for
						 * the next lighttpd to take over (graceful restart)
						 *  */

						fdevent_unregister(srv->ev, srv_socket->fd);
						close(srv_socket->fd);
						srv_socket->fd = -1;

						/* network_close() will cleanup after us */

					}
				}

				if (graceful_shutdown) {
					log_error_write(__FILE__, __LINE__, "s", "[note] graceful shutdown started");
				} else if (srv->conns->used >= srv->max_conns) {
					log_error_write(__FILE__, __LINE__, "s", "[note] sockets disabled, connection limit reached");
				} else {
					log_error_write(__FILE__, __LINE__, "s", "[note] sockets disabled, out-of-fds");
				}

				srv->sockets_disabled = 1;
			}
		}

		if (graceful_shutdown && srv->conns->used == 0) {
			/* we are in graceful shutdown phase and all connections are closed
			 * we are ready to terminate without harming anyone */
			srv_shutdown = 1;
		}

		/* we still have some fds to share */
		if (srv->want_fds) {
			/* check the fdwaitqueue for waiting fds */
			int free_fds = srv->max_fds - srv->cur_fds - 16;
			connection *con;

			for (; free_fds > 0 && NULL != (con = fdwaitqueue_unshift(srv, srv->fdwaitqueue)); free_fds--) {
				connection_state_machine(srv, con);

				srv->want_fds--;
			}
		}

		if ((n = fdevent_poll(srv->ev, 1000)) > 0) {
			/* n is the number of events */
			int revents;
			int fd_ndx;
			if (n > 0) {
				log_error_write(__FILE__, __LINE__, "sd",
						"polls:", n);
			}
			fd_ndx = -1;
			do {
				fdevent_handler handler;
				void *context;
				handler_t r;

				fd_ndx  = fdevent_event_next_fdndx (srv->ev, fd_ndx);
				if (-1 == fd_ndx) break; /* not all fdevent handlers know how many fds got an event */

				revents = fdevent_event_get_revent (srv->ev, fd_ndx);
				fd      = fdevent_event_get_fd     (srv->ev, fd_ndx);
				handler = fdevent_get_handler(srv->ev, fd);
				context = fdevent_get_context(srv->ev, fd);

				/* connection_handle_fdevent needs a joblist_append */
				log_error_write(__FILE__, __LINE__, "sdd",
						"event for", fd, revents);
				switch (r = (*handler)(srv, context, revents)) {
				case HANDLER_FINISHED:
				case HANDLER_GO_ON:
				case HANDLER_WAIT_FOR_EVENT:
				case HANDLER_WAIT_FOR_FD:
					break;
				case HANDLER_ERROR:
					/* should never happen */
					SEGFAULT();
					break;
				default:
					log_error_write(__FILE__, __LINE__, "d", r);
					break;
				}
			} while (--n > 0);
		} else if (n < 0 && errno != EINTR) {
			log_error_write(__FILE__, __LINE__, "ss",
					"fdevent_poll failed:",
					strerror(errno));
		}

		for (ndx = 0; ndx < srv->joblist->used; ndx++) {
			connection *con = srv->joblist->ptr[ndx];
			handler_t r;

			connection_state_machine(srv, con);

			con->in_joblist = 0;
		}

		srv->joblist->used = 0;
	}


#ifdef HAVE_SIGACTION
	log_error_write(__FILE__, __LINE__, "sdsd", 
			"server stopped by UID =",
			last_sigterm_info.si_uid,
			"PID =",
			last_sigterm_info.si_pid);
#else
	log_error_write(__FILE__, __LINE__, "s", 
			"server stopped");
#endif

	/* clean-up */
	log_error_close(srv);
	network_close(srv);
	connections_free(srv);
	server_free(srv);

	return 0;
}
