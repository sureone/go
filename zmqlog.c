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


static user_msg_t* init_user_msg(array* headers,int ndx,int postlen,char* postdata){
	user_msg_t *msg = calloc(1,sizeof(*msg));
	msg->headers=headers;
	msg->ndx=ndx;
	msg->postlen=postlen;
	msg->postdata=postdata;
	return msg;
}

static void handle_user_msg(mod_go* mgo,user_msg_t* msg){
}

static void handle_sys_msg(mod_go* mgo,char* header){
}

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



static void parse_and_handle(mod_go* mgo,req_msg_t* msg){
	char* next=0;
	int type,ndx;
	char* data = msg->header;
	printf("%d,%d,%s\n",msg->hlen,msg->dlen,msg->header);
	free(msg->header);
	if(msg->dlen>0){
		printf("%s\n",msg->data);
		 free(msg->data);
	}
		
	
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

static void *reqs_recv_proc(void *ptr){
	mod_go* mgo = (mod_go*)ptr;
	while(1){
		req_msg_t* msg = req_msg_recv(mgo->req_sub);
		thread_queue_add(mgo->reqs_queue,(void*)msg,0);
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
	
    	tid = pthread_create(&worker,NULL,reqs_recv_proc,(void*)mgo);
}

void * log_pub=0;
void zmq_write_log(char* s){
	if(log_pub!=0) s_send(log_pub,s);
}
static mod_go* init_mod_go(){

    mod_go* mgo = calloc(1, sizeof(*mgo));
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


    init_threads(mgo,1);

    return mgo;
}

static void free_mod_go(mod_go* mgo){
	zmq_close(mgo->req_sub);
	zmq_close(mgo->rsp_push);
	zmq_term(mgo->zcontext);
	free(mgo);
}



int main (int argc, char *argv []) {
    mod_go* mgo = init_mod_go();

#ifdef HAVE_FORK
    //daemonize();
#endif


    while(1){
	sleep(1);
    }

    free_mod_go(mgo);
    return 0;
}

