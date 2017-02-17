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
    void *pub = zmq_socket (context, ZMQ_PUB);
    zmq_bind(pub,"tcp://*:8004");

	    //  Socket to send messages on
    //void *sender = zmq_socket (context, ZMQ_PUSH);
    //zmq_bind (sender, "tcp://*:8004");
    //zmq_connect (subscriber, "ipc://req_publisher.ipc");
    // receive all

    //  Process 100 updates
    int update_nbr;
    long total_temp = 0;

    while(1) {
        printf("%s\n","HELLO WORLD");
        s_send(pub,"HELLO WORLD");
        sleep(1);
    }
    //zmq_close (subscriber);
    zmq_term (context);
    return 0;
}
