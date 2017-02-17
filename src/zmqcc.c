//
//  Weather update client
//  Connects SUB socket to tcp://localhost:5556
//  Collects weather updates and finds avg temp in zipcode
//
#include "zhelpers.h"

int main (int argc, char *argv [])
{
    void *context = zmq_init (1);

    //  Socket to talk to server
    printf ("Collecting updates from weather server…\n");
    void *subscriber = zmq_socket (context, ZMQ_SUB);
    zmq_connect (subscriber, "tcp://127.0.0.1:8004");
    zmq_setsockopt (subscriber, ZMQ_SUBSCRIBE,"",0);

	    //  Socket to send messages on
    //void *sender = zmq_socket (context, ZMQ_PUSH);
    //zmq_bind (sender, "tcp://*:8004");
    //zmq_connect (subscriber, "ipc://req_publisher.ipc");
    // receive all

    //  Process 100 updates
    int update_nbr;
    long total_temp = 0;
    while(1) {
        char *string = s_recv (subscriber);
	printf("%s\n",string);
        free (string);
    }
    //zmq_close (subscriber);
    zmq_term (context);
    return 0;
}
