#ifndef _LOG_H_
#define _LOG_H_
#include "base.h"

/* Close fd and _try_ to get a /dev/null for it instead.
 * Returns 0 on success and -1 on failure (fd gets closed in all cases)
 */
int openDevNull(int fd);

#define WP() log_error_write(__FILE__, __LINE__, "");
#define WS(s) log_error_write(__FILE__, __LINE__, "s",(s));
#define WPF() log_error_write(__FILE__, __LINE__, "ss",__func__," ENTER");

int open_logfile_or_pipe(const char* logfile);

int log_error_open();
int log_error_close();
int log_error_write(const char *filename, unsigned int line, const char *fmt, ...);
int log_info_write(const char *filename, unsigned int line, const char *fmt, ...);
int log_error_cycle();

#define debug_log(arg...) \
        do { log_error_write(__FILE__, __LINE__, ## arg); } while (0)
#define dprintf(arg...) do { char buf[1024];snprintf(buf,1000,## arg); log_error_write(__FILE__, __LINE__,"s",buf); } while (0)
#endif

	
