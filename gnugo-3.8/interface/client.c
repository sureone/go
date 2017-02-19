#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void error(char *msg)
{
    perror(msg);
    exit(0);
}

#define DATA_LEN 1024
char data[DATA_LEN];
char* http_header="request:login\r\n"
                  "auth:fdsafds\r\n"
                  "email:jerry\r\n"                  
                  "password:q\r\n\r\n";
int sockfd = 0;

char* dianmu_req="notify:dianmu_req,";
char* dianmu_result="notify:dianmu_result,";
char* step_rsp = "response:step,200,";
char* login_resume = "response:login,resume,";
char* resume_rsp = "response:resume,";
char* checkcheck = "notify:areyouok,";
char* notify_next = "notify:next,";
int on_dianmu_req=0;
int on_dianmu_result=0;
int mydesk;
int myside;
int join_nums=0;
char* chesses;
char* mu;
int gsize=19;
typedef struct
{
    char* buff;
    int offset;
} OBUFF;

int getInt(OBUFF* ob,char* token)
{
    char *tmp = ob->buff+ob->offset;
    char* nxt = strstr(tmp,token);
    char* dt=0;
    int ret=0;
    if(nxt>0)
    {
        int sz = nxt-tmp;
        ob->offset+=sz+strlen(token);
        dt = (char*)malloc(sz+1);
        strncpy(dt,tmp,sz);
        dt[sz]=0;
        ret=atoi(dt);
        free(dt);
    }
    else
    {
        ob->offset=-1;
        dt = tmp;
        ret=atoi(dt);
    }
    return ret;

}
char* getString(OBUFF* ob,char* token)
{
    char *tmp = ob->buff+ob->offset;
    char* nxt = strstr(tmp,token);
    char* dt=0;
    if(nxt>0)
    {
        int sz = nxt-tmp;
        ob->offset+=sz+strlen(token);
        dt = (char*)malloc(sz+1);
        strncpy(dt,tmp,sz);
        dt[sz]=0;
    }
    else
    {
        ob->offset=-1;
        int sz = strlen(tmp)+1;
        dt = (char*)malloc(sz);
        strcpy(dt,tmp);
    }
    return dt;

}
char* skipAndGetInt(int* v,char* buff,char token)
{
    int i = 0;
    char* data = buff;
    char vv[4];
    int j=0;
    bzero(vv,4);
    while(data[i]!=0)
    {
        if(data[i]>='0' && data[i]<='9')
        {
            vv[j]=data[i];
            j++;
        }
        if(data[i]==token)
        {
            if(j>0) *v = atoi(vv);
            i++;
            break;
        }
        i++;
    }
    return (data+i);
}
int parseDianmuResult(char* buffer)
{
//notify:dianmu_result,0,360,0,1,360
    char* data = buffer+strlen(dianmu_result);
    int isok,bmu,wmu;
    bmu=wmu=0;
    isok=0;
    data=skipAndGetInt(&isok,data,',');
    data=skipAndGetInt(&bmu,data,',');
    data=skipAndGetInt(&wmu,data,',');

    bzero(mu,gsize*gsize);
    printf("bmu=%d,wmu=%d\n",bmu,wmu);
    if(bmu>0)
    {
        int side,num,x,y;
        side=num=0;
        data=skipAndGetInt(&side,data,',');
        data=skipAndGetInt(&num,data,',');
        printf("side=%d,mus=%d\n",side,num);
        while(num>0)
        {
            data=skipAndGetInt(&x,data,',');
            data=skipAndGetInt(&y,data,',');
            num--;
            (mu)[y*gsize+x]=side;
        }
    }
    if(wmu>0)
    {
        int side,num,x,y;
        side=num=0;
        data=skipAndGetInt(&side,data,',');
        data=skipAndGetInt(&num,data,',');
        printf("side=%d,mus=%d\n",side,num);
        while(num>0)
        {
            data=skipAndGetInt(&x,data,',');
            data=skipAndGetInt(&y,data,',');
            num--;
            (mu)[y*gsize+x]=side;
        }
    }
    return (bmu+wmu);
}

void dump()
{
    int x,y;
    printf("    ");
    for(y=0; y<gsize; y++)
    {
        printf("%c ",'a'+y);
    }
    printf("\n");
    for(y=0; y<gsize; y++)
    {
        for(x=0; x<gsize; x++)
        {
            if(x==0)
            {
                if(y<=8) printf("  %d",y+1);
                else
                    printf(" %d",y+1);
            }
            if(chesses[y*gsize+x]==1)
                printf(" G");
            if(chesses[y*gsize+x]==2)
                printf(" O");
            if(chesses[y*gsize+x]==0)
            {
                if((y==3&&x==3) ||
                        (y==15&&x==15) ||
                        (y==9&&x==9) ||
                        (y==9&&x==3) ||
                        (y==3&&x==9) ||
                        (y==9&&x==15) ||
                        (y==15&&x==9) ||
                        (y==15&&x==3) ||
                        (y==3&&x==15))
                    printf(" x");
                else
                    printf(" .");
            }
            if(x==gsize-1)
            {
                printf(" %d",y+1);
            }
        }
        printf("\n");
    }
    printf("    ");
    for(y=0; y<gsize; y++)
        printf("%c ",'a'+y);
    printf("\n");
}
void dumpMu()
{
    int x,y;
    printf("   ");
    for(y=0; y<gsize; y++)
    {
        if(y<=9) printf("%d ",y);
        else
            printf("%c ",'a'+(y-10));
    }
    printf("\n");
    for(y=0; y<gsize; y++)
    {
        for(x=0; x<gsize; x++)
        {
            if(x==0)
            {
                if(y<=9) printf("%d ",y);
                else
                    printf("%c ",'a'+(y-10));
            }
            if(chesses[y*gsize+x]==1)
                printf(" G");
            if(chesses[y*gsize+x]==2)
                printf(" O");
            if(chesses[y*gsize+x]==0 && mu[y*gsize+x]==0)
                printf(" .");
            if(mu[y*gsize+x]==1)
                printf(" G");
            if(mu[y*gsize+x]==2)
                printf(" O");
        }
        printf("\n");
    }
}
int steps=1;
int putChess(int side)
{
    int x,y;
    char* command;
    if(steps>=(19*19/2)) return 0;
    if(side==1)
    {
        x = steps/19;
        y = steps%19;
    }
    else
    {
        int rsteps = 19*19-1-steps;
        x = rsteps/19;
        y = rsteps%19;
    }
    usleep(1000*100);
    command="request:step\r\n"
            "x:%d\r\n"
            "y:%d\r\n\r\n";
    bzero(data,1024);
    sprintf(data,command,x,y);
    printf("send %s\n",data);
    x = write(sockfd,data,strlen(data));
    if (x < 0)
    {
        error("ERROR writing to socket");
        return -1;
    }
    steps++;
}
void *worker_proc(void *ptr)
{
#define BUFF_LEN 4096
    int n = 0;
    printf("enter %s\n",__func__);
    char buffer[BUFF_LEN];
    int bpass=0;
    while(1)
    {
        bzero(buffer,4096);
        n = read(sockfd,buffer,4095);
        if (n <= 0)
        {
            error("ERROR reading from socket");
            break;
        }
        printf("##:\n");
        printf("%s\n",buffer);
        printf("**:\n");
        if(strncmp(buffer,dianmu_req,strlen(dianmu_req))==0)
        {
            on_dianmu_req=1;
            on_dianmu_result=0;
            char* http_header="request:dianmu_rsp\r\n"
                              "answer:yes\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
        }
        if(strstr(buffer,dianmu_result)>0)
        {
            printf("received dianmu result\n");
            on_dianmu_result=1;
            on_dianmu_req=0;
            parseDianmuResult(buffer);
            char* http_header="request:dianmu_accept\r\n"
                              "answer:yes\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
            dumpMu();
        }
		/*
        if(strstr(buffer,"notify:ready,")>0)
        {
            char* http_header="request:ready\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
        }
		*/
        if(strstr(buffer,checkcheck)>0)
        {
            char* http_header="request:iamok\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
        }
        //notify:join,1,1,4065,4,saitoh,4/20,
        if(strstr(buffer,"notify:join,")>0)
        {
            char* p = strstr(buffer,"notify:join,")+strlen("notify:join,");
            join_nums++;
        }
        if(strstr(buffer,login_resume)>0)
        {
            char* http_header="request:resume\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
        }
        if(strstr(buffer,resume_rsp)>0)
        {
        }
        if(strstr(buffer,step_rsp)>0)
        {
            char* p = buffer+strlen(step_rsp);
            int x,y;
            int killed;
            int side;
            bzero(data,1024);
            x=y=0;
            p=skipAndGetInt(&x,p,',');
            p=skipAndGetInt(&y,p,',');
            chesses[y*gsize+x]=myside;
            p=skipAndGetInt(&killed,p,',');
            if(killed>0)
                p=skipAndGetInt(&side,p,',');
            while(killed>0)
            {
                p=skipAndGetInt(&x,p,',');
                p=skipAndGetInt(&y,p,',');
                killed--;
                chesses[y*gsize+x]=0;
            }
            dump();

        }
        if(strstr(buffer,"notify:pass,")>0)
        {
            bpass=1;
        }
        if(strstr(buffer,notify_next)>0)
        {

            char* p = strstr(buffer,notify_next)+
                      strlen("notify:next,");
            int side=0;
            p=skipAndGetInt(&side,p,',');
            printf("got notify next=%d\n",side);
            if(bpass==0 && side==myside)
            {
                //putChess(side);
            }
            bpass=0;
        }
        if(strstr(buffer,"notify:game_end,")>0)
        {
            steps=0;
            memset(chesses,0,gsize*gsize);
        }
        if(strstr(buffer,"notify:step,")>0)
        {
            char* p = buffer+strlen("notify:step,");
            int x,y;
            int killed;
            int side;
            bpass=0;
            bzero(data,1024);
            x=y=0;
            p=skipAndGetInt(&side,p,',');
            p=skipAndGetInt(&x,p,',');
            p=skipAndGetInt(&y,p,',');
            chesses[y*gsize+x]=side;
            p=skipAndGetInt(&killed,p,',');
            if(killed>0)
                p=skipAndGetInt(&side,p,',');
            while(killed>0)
            {
                p=skipAndGetInt(&x,p,',');
                p=skipAndGetInt(&y,p,',');
                killed--;
                chesses[y*gsize+x]=0;
            }
            dump();
        }
        {
            OBUFF* ob = (OBUFF*)malloc(sizeof(OBUFF));
            ob->buff = buffer;
            ob->offset = 0;
            free(ob);
        }

    }

}

static char* case1[]=
{
    "11",
    "j 1 1",
    "le",
    "j 1 2",
    "r"
    "le",
    "j 2 1",
    "r"
    "j 2 2",
    "r"
    "talk talk1.",
    "talk talk2.",
    "talk talk3.",
    "talk talk4.",
    "talk talk5.",
    "talk talk6.",
    NULL,
};

static char* case2[]=
{
    "11",
    "j 1 1",
    "r",
    "s a 1",
    NULL,
};

static char* case3[]=
{
    "22",
    "j 1 2",
    "r",
    NULL,
};
static char* case4[]=
{
    "33",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2", "obs 1", "j 2 2",
    NULL,
};
static char* case5[]=
{
    "store 1 sureone",
    "store 2 colin",
    "store 3 jerry",
    NULL,
};
static char* case6[]=
{
    "query 1",
    "query 2",
    "query 3",
    NULL,
};

static char* case7[]=
{
	"start","88","users","lsdesk","leave","end",
	"start","88","users","lsdesk","leave","end",
	"start","88","users","lsdesk","leave","end",
	"start","88","users","lsdesk","leave","end",
	"start","88","users","lsdesk","leave","end",
	NULL,
};
static char** cases[] =
{
    case1,
    case2,
    case3,
    case4,
    case5,
    case6,
    case7,
    NULL,
};
int processCommand(OBUFF* o);
struct sockaddr_in serv_addr;
void injectCmd(char* buf){
    OBUFF* o = (OBUFF*)malloc(sizeof(OBUFF));
    o->buff=buf;
    o->offset=0;
    processCommand(o);
    free(o);
}

int main(int argc, char *argv[])
{
    int portno, n;
    char* hostname="127.0.0.1";
    struct hostent *server;

    char buffer[BUFF_LEN];

    portno = 9091;
	
    if (argc > 1)
    {
    portno = atoi(argv[2]);
	hostname=argv[1];
        fprintf(stderr,"usage %s hostname port\n", argv[0]);
    }
    gsize = 19;
    chesses = (char*)malloc(gsize*gsize);
    mu = (char*)malloc(gsize*gsize);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
        error("ERROR opening socket");
    server = gethostbyname(hostname);
    if (server == NULL)
    {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,
          (char *)&serv_addr.sin_addr.s_addr,
          server->h_length);
    serv_addr.sin_port = htons(portno);

    injectCmd(".start");

    while(1)
    {
        char* cmd = 0;
        int ret=0;
        bzero(buffer,4096);
        gets(buffer);
        OBUFF* o = (OBUFF*)malloc(sizeof(OBUFF));
        o->buff=buffer;
        o->offset=0;
        cmd=getString(o," ");

        if (strcmp(cmd,"auto")==0)
        {
            int i = 0;
            int caseid = getInt(o," ");
            char** cas = cases[caseid-1];
		for(int j=0;j<1000;j++){
		i=0;
            do
            {
                char* command = cas[i];
                if(command==NULL) break;
                OBUFF* oo = (OBUFF*)malloc(sizeof(OBUFF));
                oo->buff=command;
                oo->offset=0;
                printf("auto test: %s\n",command);
                usleep(300);
                ret =processCommand(oo);
                free(oo);
                if(ret==-1) break;
                i++;
            }
            while(1);
		}
        }
        else
        {
            o->offset=0;
            ret = processCommand(o);
        }
        free(o);
        if(ret==-1) break;
    }
    return 0;
}


int processCommand(OBUFF* o)
{
    int n = 0;
    char* cmd=getString(o," ");
    printf("%s\n",cmd);
    if (strcmp(cmd,".start")==-1){
        if(sockfd<=0) return -1;
    }
    if (strcmp(cmd,".start")==0){
    	pthread_t worker = 0;
    	int tid;
        sockfd = socket(AF_INET, SOCK_STREAM, 0);
    	printf("connect ok\n");
    	if (connect(sockfd,&serv_addr,sizeof(serv_addr)) < 0) {
        	error("ERROR connecting");
        	return -1;
    	}
    	printf("connect ok\n");
        tid = pthread_create(&worker,NULL,worker_proc,(void*)sockfd);
    }else if (strcmp(cmd,".end")==0){
    	close(sockfd);
    	sockfd=0;
    }else if (strcmp(cmd,".r")==0)
    {
        printf("send ready");
        char* command="request:ready\r\n\r\n";
        n = write(sockfd,command,strlen(command));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
        bzero(chesses,gsize*gsize);
    }else if (strcmp(cmd,".s")==0)
    {
        int x,y;
        int offset = 2;
        char* command="request:step\r\n"
                      "x:%d\r\n"
                      "y:%d\r\n\r\n";
        char* sx=0;
        bzero(data,1024);
        sx=getString(o," ");
        y=getInt(o," ");
        x = sx[0]-'a';
        free(sx);
        y--;
        sprintf(data,command,x,y);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }else if (strcmp(cmd,"..")==0)
    {
        int x,y;
        int offset = 2;
        char* command="request:step\r\n"
                      "x:%d\r\n"
                      "y:%d\r\n\r\n";
        char* sx=0;
        bzero(data,1024);
        sx=getString(o," ");
        y=getInt(o," ");
        x = sx[0]-'a';
        free(sx);
        y--;
        sprintf(data,command,x,y);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }else if (strcmp(cmd,".report")==0)
    {
        printf("total joins: %d\n",join_nums);
    }
    else if (strcmp(cmd,"yes")==0)
    {
        if(on_dianmu_req==1)
        {
            char* http_header="request:dianmu_rsp\r\n"
                              "answer:yes\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
            on_dianmu_req=0;
        }
        else if(on_dianmu_result==1)
        {
            printf("accept dianmu result\n");
            char* http_header="request:dianmu_accept\r\n"
                              "answer:yes\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
            on_dianmu_result=0;
        }
    }
    else if (strcmp(cmd,".reboot")==0)
    {
        char* http_header="request:reboot\r\n"
                          "\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
		}
	}
    else if (strcmp(cmd,".stat")==0)
    {
        char* http_header="request:stat\r\n"
                          "\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
		}
	}
    else if (strcmp(cmd,".str")==0)
    {
        char* http_header="request:stat-room\r\n"
                          "\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".ls")==0)
    {
        char* http_header="request:ls\r\n"
                          "\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".nc")==0)
    {
        char* http_header="request:nonce\r\n"
                          "\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".lsgf")==0)
    {
        char* http_header="request:ls\r\n"
                          "\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".gsgf")==0)
    {
        char* http_header="request:get\r\n"
                          "type:sgf\r\n"
                          "id:20\r\n"
                          "\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }

    else if (strcmp(cmd,".no")==0)
    {
        if(on_dianmu_req==1)
        {
            char* http_header="request:dianmu_rsp\r\n"
                              "answer:no\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
            on_dianmu_req=0;
        }
        else if(on_dianmu_result==1)
        {
            char* http_header="request:dianmu_accept\r\n"
                              "answer:no\r\n\r\n";
            n = write(sockfd,http_header,strlen(http_header));
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
            on_dianmu_result=0;
        }
    }
    else if (strcmp(cmd,".ps")==0)
    {
        char* http_header="request:pass\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".post")==0)
    {
        char* http_header="request:post\r\n"
                          "content-length:4096\r\n"
                          "type:sgf\r\n"
                          "SZ:19\r\n"
                          "EV:evev\r\n"
                          "DT:dtdt\r\n"
                          "RE:rere\r\n"
                          "PB:pbpb\r\n"
                          "BR:brbr\r\n"
                          "PW:pwpw\r\n"
                          "WR:wrwr\r\n\r\n";
        char *data = (char*)malloc(4096);
        char* p =data;
        int len = 4096;
        memset(data,'A',4096);
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }

        while(1)
        {
            n = write(sockfd,p,len);
            if (n < 0)
            {
                error("ERROR writing to socket");
                return -1;
            }
            else
            {
                len = len-n;
                if(len==0) break;
                p+=n;
            }
        }
        printf("write data ok for post\n");


    }
    else if (strcmp(cmd,".dm")==0)
    {
        char* http_header="request:dianmu_req\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }

    }
    else if (strcmp(cmd,".query")==0)
    {
        char* command="request:query\r\n"
                      "key:%s\r\n\r\n";
        char* key=getString(o," ");
        bzero(data,1024);
        sprintf(data,command,key);
        free(key);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".invite")==0) {
        char* command="request:invite\r\n"
                      "uid:%d\r\n\r\n";
        int uid = getInt(o," ");
        bzero(data,1024);
        sprintf(data,command,uid);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".ainvite")==0) {
        char* command="request:accept_invite\r\n"
                      "uid:%d\r\n\r\n";
        int uid = getInt(o," ");
        bzero(data,1024);
        sprintf(data,command,uid);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".rinvite")==0) {
        char* command="request:reject_invite\r\n"
                      "uid:%d\r\n\r\n";
        int uid = getInt(o," ");
        bzero(data,1024);
        sprintf(data,command,uid);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".jinvite")==0) {
        char* command="request:join_invite\r\n\r\n";
        n = write(sockfd,command,strlen(command));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".store")==0)
    {
        char* command="request:store\r\n"
                      "key:%s\r\n"
                      "value:%s\r\n\r\n";
        char* key=getString(o," ");
        char* value=getString(o," ");
        bzero(data,1024);
        sprintf(data,command,key,value);
        free(key);
        free(value);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".signup")==0)
    {
        char* command="request:register\r\n"
                      "email:%s\r\n"
                      "password:vvv\r\n\r\n";
        char* name = getString(o," ");
        bzero(data,1024);
        sprintf(data,command,name);
        n = write(sockfd,data,strlen(data));
        free(name);
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }else if (strcmp(cmd,".ak")==0)
    {
        char* command="request:update-property\r\n"
                      "key:%s\r\n"
                      "value:%s\r\n\r\n";
        char* key = getString(o," ");
        char* value = getString(o," ");
        bzero(data,1024);
        sprintf(data,command,key,value);
        n = write(sockfd,data,strlen(data));
        free(key);
        free(value);
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }else if (strcmp(cmd,".rk")==0)
    {
        char* command="request:read-property\r\n"
                      "key:%s\r\n\r\n";
        char* key = getString(o," ");
        bzero(data,1024);
        sprintf(data,command,key);
        n = write(sockfd,data,strlen(data));
        free(key);
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
     else if (strcmp(cmd,".regg")==0)
    {
        char* command="request:registergroup\r\n"
                      "name:%s\r\n\r\n";
        char* name = getString(o," ");
        bzero(data,1024);
        sprintf(data,command,name);
        n = write(sockfd,data,strlen(data));
        free(name);
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".33")==0)
    {
        char* http_header="request:login\r\n"
                          "email:Gvvv\r\n"
			              "sn:uvafdsafds\r\n"
                          "password:vvv\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".11")==0)
    {
        char* http_header="request:login\r\n"
                          "email:apple\r\n"
                          "sn:xxxxxxx\r\n"
                          "password:vvv\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".22")==0)
    {
        char* http_header="request:login\r\n"
                          "email:dog\r\n"
                          "sn:xxxxxxvv\r\n"
                          "password:vvv\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".88")==0)
    {
        char* http_header="request:login\r\n"
                          "email:lisb911@163.com\r\n"


                          "password:282901473\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".done")==0)
    {
        char* http_header="request:done\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".score")==0)
    {
        char* http_header="request:score\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".pass")==0)
    {
        char* http_header="request:pass\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".44")==0)
    {
        char* http_header="request:login\r\n"
                          "email:hh\r\n"
                          "sn:codfsaa\r\n"
                          "password:g\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".55")==0)
    {
        char* http_header="request:login\r\n"
                          "email:xxx\r\n"
                          "sn:vdddddd\r\n"
                          "password:vvv\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".66")==0)
    {
        char* http_header="request:login\r\n"
                          "email:uu\r\n"
                          "sn:fdafdsafdsa\r\n"
                          "password:vvv\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".77")==0)
    {
        char* http_header="request:login\r\n"
                          "email:dfasfd\r\n"
                          "password:vvv\r\n\r\n";
        n = write(sockfd,http_header,strlen(http_header));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".bb")==0)
    {
        char* command = "request:broadcast\r\n"
                        "content:%s"
                        "\r\n\r\n";
        char* content = getString(o,".");
        bzero(data,1024);
        sprintf(data,command,content);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }

    }


    else if (strcmp(cmd,".dump")==0)
    {
        int did;
        int offset = 5;
        char* command="request:dump\r\n"
                      "did:%d\r\n\r\n";
        bzero(data,1024);
        did = getInt(o," ");
        sprintf(data,command,did);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".judge")==0) {
        int x,y;
        int offset = 1;
        char* command="request:judge\r\n"
                      "did:%d\r\n"
                      "win:%d\r\n\r\n";
        char* sx=0;
        bzero(data,1024);
        x=getInt(o," ");
        y=getInt(o," ");
        sprintf(data,command,x,y);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
        else if (strcmp(cmd,".stt")==0) {
        int x,y;
        int offset = 1;
        char* command="request:set_step_time\r\n"
                      "minutes:%d\r\n\r\n";
        char* sx=0;
        bzero(data,1024);
        x=getInt(o," ");
        sprintf(data,command,x);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".addadmin")==0) {
        int x,y;
        int offset = 1;
        char* command="request:addadmin\r\n"
                      "id:%d\r\n"
                      "permission:%d\r\n\r\n";
        char* sx=0;
        bzero(data,1024);
        x=getInt(o," ");
        y=getInt(o," ");
        sprintf(data,command,x,y);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    
    else if (strcmp(cmd,".observe")==0 ||
            strcmp(cmd,"obs")==0)
    {
        int did;
        char* command="request:observe\r\n"
                      "did:%d\r\n\r\n";
        bzero(data,1024);
        did=getInt(o," ");
        mydesk=did;
        myside=3;
        sprintf(data,command,did);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }

	else if (strcmp(cmd,".lsgu")==0 )
    {
        int did;
        char* command="request:listgroupuser\r\n"
                      "gid:%d\r\n\r\n";
        bzero(data,1024);
        did=getInt(o," ");
        sprintf(data,command,did);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }

    else if (strcmp(cmd,".lg")==0 )
    {
        int did;
        char* command="request:leavegroup\r\n"
                      "gid:%d\r\n\r\n";
        bzero(data,1024);
        did=getInt(o," ");
        sprintf(data,command,did);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }

    else if (strcmp(cmd,".jg")==0 )
    {
        int did;
        char* command="request:joingroup\r\n"
                      "gid:%d\r\n\r\n";
        bzero(data,1024);
        did=getInt(o," ");
        sprintf(data,command,did);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }


    else if (strcmp(cmd,".j")==0)
    {
        int did,sid;
        char* command="request:join\r\n"
                      "did:%d\r\n"
                      "sid:%d\r\n\r\n";
        bzero(data,1024);
        did=getInt(o," ");
        sid=getInt(o," ");
        mydesk=did;
        myside = sid;
        sprintf(data,command,did,sid);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }

    }
    else if (strcmp(cmd,".ls")==0)
    {
        char* command="request:users\r\n\r\n";
        n = write(sockfd,command,strlen(command));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".gg")==0)
    {
        char* command="request:leave\r\n\r\n";
        n = write(sockfd,command,strlen(command));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".stat")==0)
    {
        char* command="request:stat\r\n\r\n";
        n = write(sockfd,command,strlen(command));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }

    else if (strcmp(cmd,".alarm")==0)
    {
        char* command="request:alarm\r\n\r\n";
        n = write(sockfd,command,strlen(command));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else if (strcmp(cmd,".ld")==0)
    {
        char* command="request:list\r\n"
                      "type:desk\r\n\r\n";
        n = write(sockfd,command,strlen(command));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }
    }
    else
    {
        char* command = "request:post-talk\r\n"
                        "content-length:%d"
                        "\r\n\r\n%s\r\n";
        char* content = cmd;        bzero(data,1024);
        sprintf(data,command,strlen(content),content);
        n = write(sockfd,data,strlen(data));
        if (n < 0)
        {
            error("ERROR writing to socket");
            return -1;
        }

    }
    return 0;
}




