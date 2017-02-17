//
//  Weather update client
//  Connects SUB socket to tcp://localhost:5556
//  Collects weather updates and finds avg temp in zipcode
//
#include "log.h"
#include "modgo.h"
#include "shelper.h"
#include "array.h"
#include "app.h"
#ifdef DMALLOC
#include "dmalloc.h"
#endif
mod_go* g_mgo=0;
static void* g_context=0; 

extern void init_appgo();


static user_msg_t* init_user_msg(array* headers,int ndx,int postlen,char* postdata){
	user_msg_t *msg = calloc(1,sizeof(*msg));
	msg->headers=headers;
	msg->ndx=ndx;
	msg->postlen=postlen;
	msg->postdata=postdata;
	return msg;
}

send_response(void* srv,int ndx,buffer* b){
	mod_go* mgo = (mod_go*)srv;
	buffer* bb = buffer_init();
	buffer_append_long(bb,1);
	buffer_append_string(bb,",");
	buffer_append_long(bb,ndx);
	buffer_append_string(bb,",");
	buffer_append_string(bb,b->ptr);
	buffer_append_string(bb,"\r\n\r\n");
	dprintf(bb->ptr);
	//s_send(mgo->rsp_push,bb->ptr);
        process_response(g_context,bb);
	buffer_free(bb);
}

static void handle_user_msg(mod_go* mgo,user_msg_t* msg){
	appgo_handle_request(mgo,msg);
}

static void handle_sys_msg(mod_go* mgo,char* header){
	char* next=0;
	char* next2=0;
	int ndx = getInt(header,',',&next);
	if(strstr(next,"close,")>0){
		dprintf("%s\n",header);
		app_handle_con_close(mgo,ndx);
	}
	if(strstr(next,"timer,")>0){
		appgo_handle_timer(mgo);
	}
	if(strstr(next,"timeout,")>0){
		dprintf("%s\n",header);
		int type=getInt(next,',',&next2);
		int uid = ndx;	
		app_handle_timeout(mgo,uid,type);
	}
}



static void parse_and_handle(mod_go* mgo,req_msg_t* msg){
	char* next=0;
	int type,ndx;
	char* data = msg->header;
	//printf("%d,%d,%s\n",msg->hlen,msg->dlen,msg->header);
	//s_send(mgo->rsp_push,data);
	type=getInt(data,',',&next);
	switch(type){

	case 0:
		handle_sys_msg(mgo,next);
	break;
	// user message
	case 1:
	{
		array* headers = array_init();
		int content_length=0;
		ndx=getInt(next,',',&next);
		if(parse_http_header(next,headers,&content_length)>=0){
			user_msg_t *umsg = init_user_msg(headers,ndx,content_length,msg->data);
			if(umsg!=0){
				 handle_user_msg(mgo,umsg);
				free(umsg);
			}
		}
		array_free(headers);
	}
	break;

	}
	free(msg->header);
	if(msg->dlen>0) free(msg->data);
	
}

static void *timer_proc(void* ptr){
	mod_go* mgo = (mod_go*)ptr;
	while(1){
		sleep(1);
		appgo_check_timer(mgo);
	}
}

void modgo_tick(){
	appgo_check_timer(g_mgo);
}



static void *reqs_handle_proc(void *ptr){
	mod_go* mgo = (mod_go*)ptr;
	while(1){
		struct threadmsg *msg = (struct threadmsg*)malloc(sizeof(struct threadmsg));
		thread_queue_get(mgo->reqs_queue,NULL,msg);
		parse_and_handle(mgo,(req_msg_t*)(msg->data));
		free(msg->data);
		free(msg);
	}
}

void mgo_process_msg(void* msg){
	
	thread_queue_add(g_mgo->reqs_queue,(void*)msg,0);
	//parse_and_handle(g_mgo,(req_msg_t*)(msg));
	//free(msg);

}

static void *reqs_recv_proc(void *ptr){
	mod_go* mgo = (mod_go*)ptr;
	while(1){
		//req_msg_t* msg = req_msg_recv(mgo->req_sub);
		//thread_queue_add(mgo->reqs_queue,(void*)msg,0);
	}
}

static void* init_threads(mod_go* mgo,int num){
    	pthread_t worker = 0;
    	int tid;
	mgo->reqs_queue = (struct threadqueue*)malloc(sizeof(struct threadqueue));
	thread_queue_init(mgo->reqs_queue);

	for(int i=0;i<num;i++){
    		tid = pthread_create(&worker,NULL,reqs_handle_proc,(void*)mgo);
	}
	
    	//tid = pthread_create(&worker,NULL,reqs_recv_proc,(void*)mgo);
    	//tid = pthread_create(&worker,NULL,timer_proc,(void*)mgo);

}

void * log_pub=0;
mod_go* init_mod_go(){
    init_db("sql.db");
    init_desks(100);
    init_appgo();

    mod_go* mgo = calloc(1, sizeof(*mgo));
    #if(0)
    mgo->zcontext = zmq_init (1);
    //  Socket to talk to server
    mgo->req_sub= zmq_socket (mgo->zcontext, ZMQ_SUB);
    zmq_connect (mgo->req_sub, "tcp://127.0.0.1:8002");
    zmq_setsockopt (mgo->req_sub, ZMQ_SUBSCRIBE,"",0);
   //  Socket to send messages on
    //mgo->rsp_push = zmq_socket(mgo->zcontext,ZMQ_PUB);
    //zmq_bind(mgo->rsp_push,"tcp://*:8004");

    log_pub = zmq_socket(mgo->zcontext,ZMQ_PUB);
    zmq_bind(log_pub,"tcp://*:8012");

    mgo->rsp_push = zmq_socket (mgo->zcontext, ZMQ_PUSH);
    zmq_bind (mgo->rsp_push, "tcp://*:8004");
    #endif


    init_threads(mgo,1);

    return mgo;
}

static void free_mod_go(mod_go* mgo){
	#ifdef HAVE_ZMQ
	zmq_close(mgo->req_sub);
	zmq_close(mgo->rsp_push);
	zmq_term(mgo->zcontext);
	free(mgo);
	#endif
}

int mgo_init (void* context) {
    //setup_log(ERRORLOG_FD,"modgo.log",NULL,0);
    // setup_log(ERRORLOG_FD,STDERR_FILENO,NULL,0);
    g_mgo = init_mod_go();
    g_context = context;
    g_mgo->srv=context;
    return 0;
}



