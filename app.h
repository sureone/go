#ifndef _APP_H_
#define _APP_H_
typedef struct{
	array* headers;
	short ndx;
	short postlen;
	char* postdata;
}user_msg_t;
#endif
