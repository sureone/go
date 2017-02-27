#include "base.h"
#include "log.h"
#include "array.h"

#include <sys/types.h>

#include <errno.h>
#include <fcntl.h>
#include <time.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>

#include <stdarg.h>
#include <stdio.h>
#include <dirent.h>

#ifdef HAVE_SYSLOG_H
# include <syslog.h>
#endif

#ifdef HAVE_VALGRIND_VALGRIND_H
# include <valgrind/valgrind.h>
#endif
#ifdef DMALLOC
#include "dmalloc.h"
#endif

#ifndef O_LARGEFILE
# define O_LARGEFILE 0
#endif
#define MAX_ERROR_LOG_LINES 800000
/* Close fd and _try_ to get a /dev/null for it instead.
 * close() alone may trigger some bugs when a
 * process opens another file and gets fd = STDOUT_FILENO or STDERR_FILENO
 * and later tries to just print on stdout/stderr
 *
 * Returns 0 on success and -1 on failure (fd gets closed in all cases)
 */
int openDevNull(int fd) {
	int tmpfd;
	close(fd);
#if defined(__WIN32)
	/* Cygwin should work with /dev/null */
	tmpfd = open("nul", O_RDWR);
#else
	tmpfd = open("/dev/null", O_RDWR);
#endif
	if (tmpfd != -1 && tmpfd != fd) {
		dup2(tmpfd, fd);
		close(tmpfd);
	}
	return (tmpfd != -1) ? 0 : -1;
}

int open_logfile_or_pipe(const char* logfile) {
	int fd;

	if (logfile[0] == '|') {
#ifdef HAVE_FORK
		/* create write pipe and spawn process */

		int to_log_fds[2];

		if (pipe(to_log_fds)) {
			log_error_write(__FILE__, __LINE__, "ss", "pipe failed: ", strerror(errno));
			return -1;
		}

		/* fork, execve */
		switch (fork()) {
		case 0:
			/* child */
			close(STDIN_FILENO);

			/* dup the filehandle to STDIN */
			if (to_log_fds[0] != STDIN_FILENO) {
				if (STDIN_FILENO != dup2(to_log_fds[0], STDIN_FILENO)) {
					log_error_write( __FILE__, __LINE__, "ss",
						"dup2 failed: ", strerror(errno));
					exit(-1);
				}
				close(to_log_fds[0]);
			}
			close(to_log_fds[1]);

#ifndef FD_CLOEXEC
			{
				int i;
				/* we don't need the client socket */
				for (i = 3; i < 256; i++) {
					close(i);
				}
			}
#endif

			/* close old stderr */
			openDevNull(STDERR_FILENO);

			/* exec the log-process (skip the | ) */
			execl("/bin/sh", "sh", "-c", logfile + 1, NULL);
			log_error_write( __FILE__, __LINE__, "sss",
					"spawning log process failed: ", strerror(errno),
					logfile + 1);

			exit(-1);
			break;
		case -1:
			/* error */
			log_error_write( __FILE__, __LINE__, "ss", "fork failed: ", strerror(errno));
			return -1;
		default:
			close(to_log_fds[0]);
			fd = to_log_fds[1];
			break;
		}

#else
		return -1;
#endif
	} else if (-1 == (fd = open(logfile, O_APPEND | O_WRONLY | O_CREAT | O_LARGEFILE, 0644))) {
		log_error_write( __FILE__, __LINE__, "SSSS",
				"opening errorlog '", logfile,
				"' failed: ", strerror(errno));

		return -1;
	}

#ifdef FD_CLOEXEC
	fcntl(fd, F_SETFD, FD_CLOEXEC);
#endif

	return fd;
}

static int errorlog_mode = ERRORLOG_FD;
static int errorlog_fd = STDERR_FILENO;
static int errorlog_use_syslog = 0;
buffer* errorlog_file = NULL;
buffer* breakagelog_file = NULL;
buffer* errorlog_buf = NULL;
time_t last_generated_debug_ts;
buffer* ts_debug_str = NULL;
static int last_log_no = 0;
static int log_lines=0;
void setup_log(int mode,int fd,char* logfile,int usesyslog){
	errorlog_use_syslog = usesyslog;
	errorlog_mode=mode;
	errorlog_fd = fd;
	errorlog_buf = buffer_init();
	if(breakagelog_file==NULL) breakagelog_file = buffer_init();
	if(errorlog_file==NULL) errorlog_file = buffer_init();
	if(logfile!=NULL){
		DIR *dir;
		if( (access( "logs", 0 )) !=0 ){
            if(mkdir("logs", 0755)==-1)  
            {   
                WS("mkdir 'logs' error");
                exit(1);
            }  
        }

        if ((dir=opendir("logs")) == NULL){
        	exit(1);
        }
         struct dirent *ptr;
         char base[100];
        while ((ptr=readdir(dir)) != NULL){
			if(strcmp(ptr->d_name,".")==0 || strcmp(ptr->d_name,"..")==0) continue;
			else if(ptr->d_type == 8){
				char logno[10];
                memset(logno,0,10);
				strncpy(logno,ptr->d_name,strchr(ptr->d_name,'.')-ptr->d_name);
				int n = atoi(logno);

				if(n>last_log_no) last_log_no = n;
			}
        }

        last_log_no++;
        buffer_append_string(errorlog_file,"logs/");
		buffer_append_long(errorlog_file,last_log_no);
		buffer_append_string(errorlog_file,".log");

		// printf("log to %s\n",errorlog_file->ptr);
			
	}
	last_generated_debug_ts = 0;
	ts_debug_str = buffer_init();
	return 0;
}



void clear_log(){
	buffer_free(breakagelog_file);
	buffer_free(errorlog_file);
	buffer_free(ts_debug_str);
	buffer_free(errorlog_buf);
}


/**
 * open the errorlog
 *
 * we have 4 possibilities:
 * - stderr (default)
 * - syslog
 * - logfile
 * - pipe
 *
 * if the open failed, report to the user and die
 *
 */

int log_error_open() {
#ifdef HAVE_SYSLOG_H
	/* perhaps someone wants to use syslog() */
	openlog("lighttpd", LOG_CONS | LOG_PID, LOG_DAEMON);
#endif


	if (errorlog_use_syslog) {
		errorlog_mode = ERRORLOG_SYSLOG;
	} else if (!buffer_is_empty(errorlog_file)) {
		buffer_reset(errorlog_file);
		buffer_append_string(errorlog_file,"logs/");
		buffer_append_long(errorlog_file,last_log_no);
		buffer_append_string(errorlog_file,".log");
		const char *logfile = errorlog_file->ptr;
		printf("log to file %s\n",logfile);
		if (-1 == (errorlog_fd = open_logfile_or_pipe(logfile))) {
			return -1;
		}
		errorlog_mode = (logfile[0] == '|') ? ERRORLOG_PIPE : ERRORLOG_FILE;
	}

	if (!buffer_is_empty(breakagelog_file)) {
		int breakage_fd;
		const char *logfile = breakagelog_file->ptr;

		if (errorlog_mode == ERRORLOG_FD) {
			errorlog_fd = dup(STDERR_FILENO);
#ifdef FD_CLOEXEC
			fcntl(errorlog_fd, F_SETFD, FD_CLOEXEC);
#endif
		}

		if (-1 == (breakage_fd = open_logfile_or_pipe(logfile))) {
			return -1;
		}

		if (STDERR_FILENO != breakage_fd) {
			dup2(breakage_fd, STDERR_FILENO);
			close(breakage_fd);
		}
	} 
#if 0
	else if (!srv->srvconf.dont_daemonize) {
		/* move stderr to /dev/null */
		openDevNull(STDERR_FILENO);
	}
#endif
	return 0;
}

/**
 * open the errorlog
 *
 * if the open failed, report to the user and die
 * if no filename is given, use syslog instead
 *
 */

int log_error_cycle() {
	/* only cycle if the error log is a file */

	if (errorlog_mode == ERRORLOG_FILE) {
		const char *logfile = errorlog_file->ptr;
		/* already check of opening time */

		int new_fd;

		if (-1 == (new_fd = open_logfile_or_pipe(logfile))) {
			/* write to old log */
			log_error_write(__FILE__, __LINE__, "SSSSS",
					"cycling errorlog '", logfile,
					"' failed: ", strerror(errno),
					", falling back to syslog()");

			close(errorlog_fd);
			errorlog_fd = -1;
#ifdef HAVE_SYSLOG_H
			errorlog_mode = ERRORLOG_SYSLOG;
#endif
		} else {
			/* ok, new log is open, close the old one */
			close(errorlog_fd);
			errorlog_fd = new_fd;
#ifdef FD_CLOEXEC
			/* close fd on exec (cgi) */
			fcntl(errorlog_fd, F_SETFD, FD_CLOEXEC);
#endif
		}
	}

	return 0;
}

int log_error_close() {
	switch(errorlog_mode) {
	case ERRORLOG_PIPE:
	case ERRORLOG_FILE:
	case ERRORLOG_FD:
		if (-1 != errorlog_fd) {
			/* don't close STDERR */
			if (STDERR_FILENO != errorlog_fd)
				close(errorlog_fd);
			errorlog_fd = -1;
		}
		break;
	case ERRORLOG_SYSLOG:
#ifdef HAVE_SYSLOG_H
		closelog();
#endif
		break;
	}

	return 0;
}

extern void zmq_write_log(char* s);
int log_error_write(const char *filename, unsigned int line, const char *fmt, ...) {

	time_t cur_ts = time(NULL);
	va_list ap;

	switch(errorlog_mode) {
	case ERRORLOG_PIPE:
	case ERRORLOG_FILE:
	case ERRORLOG_FD:
		if (-1 == errorlog_fd) return 0;
		/* cache the generated timestamp */
		if (cur_ts != last_generated_debug_ts) {
			buffer_prepare_copy(ts_debug_str, 255);
			strftime(ts_debug_str->ptr, ts_debug_str->size - 1, "%Y-%m-%d %H:%M:%S", localtime(&(cur_ts)));
			ts_debug_str->used = strlen(ts_debug_str->ptr) + 1;

			last_generated_debug_ts = cur_ts;
		}

		buffer_copy_string_buffer(errorlog_buf, ts_debug_str);
		buffer_append_string_len(errorlog_buf, CONST_STR_LEN(": ("));
		break;
	case ERRORLOG_SYSLOG:
		/* syslog is generating its own timestamps */
		buffer_copy_string_len(errorlog_buf, CONST_STR_LEN("("));
		break;
	case ERRORLOG_NONE:
		return 0;
        case ERRORLOG_ZMQ:
		break;
	}

	buffer_append_string(errorlog_buf, filename);
	buffer_append_string_len(errorlog_buf, CONST_STR_LEN("."));
	buffer_append_long(errorlog_buf, line);
	buffer_append_string_len(errorlog_buf, CONST_STR_LEN(") "));


	for(va_start(ap, fmt); *fmt; fmt++) {
		int d;
		char *s;
		buffer *b;
		off_t o;

		switch(*fmt) {
		case 's':           /* string */
			s = va_arg(ap, char *);
			buffer_append_string(errorlog_buf, s);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'b':           /* buffer */
			b = va_arg(ap, buffer *);
			buffer_append_string_buffer(errorlog_buf, b);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'd':           /* int */
			d = va_arg(ap, int);
			buffer_append_long(errorlog_buf, d);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'o':           /* off_t */
			o = va_arg(ap, off_t);
			buffer_append_off_t(errorlog_buf, o);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'x':           /* int (hex) */
			d = va_arg(ap, int);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN("0x"));
			buffer_append_long_hex(errorlog_buf, d);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'S':           /* string */
			s = va_arg(ap, char *);
			buffer_append_string(errorlog_buf, s);
			break;
		case 'B':           /* buffer */
			b = va_arg(ap, buffer *);
			buffer_append_string_buffer(errorlog_buf, b);
			break;
		case 'D':           /* int */
			d = va_arg(ap, int);
			buffer_append_long(errorlog_buf, d);
			break;
		case 'O':           /* off_t */
			o = va_arg(ap, off_t);
			buffer_append_off_t(errorlog_buf, o);
			break;
		case 'X':           /* int (hex) */
			d = va_arg(ap, int);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN("0x"));
			buffer_append_long_hex(errorlog_buf, d);
			break;
		case '(':
		case ')':
		case '<':
		case '>':
		case ',':
		case ' ':
			buffer_append_string_len(errorlog_buf, fmt, 1);
			break;
		}
	}
	va_end(ap);

	switch(errorlog_mode) {
	case ERRORLOG_PIPE:
	case ERRORLOG_FILE:
	case ERRORLOG_FD:
		log_lines++;
		buffer_append_string_len(errorlog_buf, CONST_STR_LEN("\n"));
		write(errorlog_fd, errorlog_buf->ptr, errorlog_buf->used - 1);

		if(log_lines>MAX_ERROR_LOG_LINES){
			log_lines=0;
			last_log_no++;
			log_error_open();
			
		}
		break;
	case ERRORLOG_SYSLOG:
		syslog(LOG_ERR, "%s", errorlog_buf->ptr);
		break;
	case ERRORLOG_ZMQ:
		buffer_append_string_len(errorlog_buf, CONST_STR_LEN("\n"));
		errorlog_buf->ptr[errorlog_buf->used]=0;
		zmq_write_log(errorlog_buf->ptr);
		break;
	}

	return 0;
}
int log_info_write(const char *filename, unsigned int line, const char *fmt, ...) {
	time_t cur_ts = time(NULL);
	va_list ap;

	switch(errorlog_mode) {
	case ERRORLOG_PIPE:
	case ERRORLOG_FILE:
	case ERRORLOG_FD:
		if (-1 == errorlog_fd) return 0;
		/* cache the generated timestamp */
		if (cur_ts != last_generated_debug_ts) {
			buffer_prepare_copy(ts_debug_str, 255);
			strftime(ts_debug_str->ptr, ts_debug_str->size - 1, "%Y-%m-%d %H:%M:%S", localtime(&(cur_ts)));
			ts_debug_str->used = strlen(ts_debug_str->ptr) + 1;

			last_generated_debug_ts = cur_ts;
		}

		buffer_copy_string_buffer(errorlog_buf, ts_debug_str);
		buffer_append_string_len(errorlog_buf, CONST_STR_LEN(": ("));
		break;
	case ERRORLOG_SYSLOG:
		/* syslog is generating its own timestamps */
		buffer_copy_string_len(errorlog_buf, CONST_STR_LEN("("));
		break;
	case ERRORLOG_NONE:
		return 0;
        case ERRORLOG_ZMQ:
		break;
	}

	buffer_append_string(errorlog_buf, filename);
	buffer_append_string_len(errorlog_buf, CONST_STR_LEN("."));
	buffer_append_long(errorlog_buf, line);
	buffer_append_string_len(errorlog_buf, CONST_STR_LEN(") "));


	for(va_start(ap, fmt); *fmt; fmt++) {
		int d;
		char *s;
		buffer *b;
		off_t o;

		switch(*fmt) {
		case 's':           /* string */
			s = va_arg(ap, char *);
			buffer_append_string(errorlog_buf, s);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'b':           /* buffer */
			b = va_arg(ap, buffer *);
			buffer_append_string_buffer(errorlog_buf, b);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'd':           /* int */
			d = va_arg(ap, int);
			buffer_append_long(errorlog_buf, d);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'o':           /* off_t */
			o = va_arg(ap, off_t);
			buffer_append_off_t(errorlog_buf, o);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'x':           /* int (hex) */
			d = va_arg(ap, int);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN("0x"));
			buffer_append_long_hex(errorlog_buf, d);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN(" "));
			break;
		case 'S':           /* string */
			s = va_arg(ap, char *);
			buffer_append_string(errorlog_buf, s);
			break;
		case 'B':           /* buffer */
			b = va_arg(ap, buffer *);
			buffer_append_string_buffer(errorlog_buf, b);
			break;
		case 'D':           /* int */
			d = va_arg(ap, int);
			buffer_append_long(errorlog_buf, d);
			break;
		case 'O':           /* off_t */
			o = va_arg(ap, off_t);
			buffer_append_off_t(errorlog_buf, o);
			break;
		case 'X':           /* int (hex) */
			d = va_arg(ap, int);
			buffer_append_string_len(errorlog_buf, CONST_STR_LEN("0x"));
			buffer_append_long_hex(errorlog_buf, d);
			break;
		case '(':
		case ')':
		case '<':
		case '>':
		case ',':
		case ' ':
			buffer_append_string_len(errorlog_buf, fmt, 1);
			break;
		}
	}
	va_end(ap);

	switch(errorlog_mode) {
	case ERRORLOG_PIPE:
	case ERRORLOG_FILE:
	case ERRORLOG_FD:
		buffer_append_string_len(errorlog_buf, CONST_STR_LEN("\n"));
		write(errorlog_fd, errorlog_buf->ptr, errorlog_buf->used - 1);
		break;
	case ERRORLOG_SYSLOG:
		syslog(LOG_ERR, "%s", errorlog_buf->ptr);
		break;
	case ERRORLOG_ZMQ:
		buffer_append_string_len(errorlog_buf, CONST_STR_LEN("\n"));
		errorlog_buf->ptr[errorlog_buf->used]=0;
		zmq_write_log(errorlog_buf->ptr);
		break;
	}

	return 0;
}

