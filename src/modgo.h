#ifndef _MOD_GO_H
#define _MOD_GO_H
#include "buffer.h"
//#include "zhelpers.h"
#include "threadqueue.h"
typedef struct mod_go{
	void* zcontext;
	void* req_sub;
	void* rsp_push;
	void* srv;
	struct threadqueue* reqs_queue;
}mod_go;
#endif
