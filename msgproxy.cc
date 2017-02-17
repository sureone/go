#include <zmq.hpp>
#include <assert.h>
#include <pthread.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>
#ifdef DMALLOC
#include "dmalloc.h"
#endif
int msgprox(){
	zmq::context_t context(1);
	zmq::socket_t socket(context,ZMQ_REP);
	socket.bind("tcp://*:5555");
	while(true){
		zmq::message_t request;
		socket.recv(&request);
		printf("Received request:[%s]\n",
			(char*)request.data());
		sleep(1);
		zmq::message_t reply(6);
		memcpy((void*) reply.data(), "world", 6);
		socket.send(reply);
	}
	return 0;
}

