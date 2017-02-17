#ifndef __USER_H
#define __USER_H
#include "timer_check.h"
#define MAX_NAME_LEN 32
typedef enum
{
    USER_IN_ROOM,
    USER_CONNECTED,
    USER_LOGIN,
    USER_IN_DESK,
    USER_OBSERVE,
    USER_JOIN,
    USER_READY,
    USER_IN_GAME,
    USER_PLAYING,
    USER_STEP,
    USER_DIANMU_REQ,
    USER_DIANMU,
    USER_ACCEPT_DIANMU,
    USER_WAIT_ME_BACK,
    USER_OUT_GAME,
    USER_OUT_DESK,
    USER_CLOSED,
} user_status_t;
#define MAX_USERS 400
typedef struct user_t
{
    int id;
    int did;
    int sid;
    int puid;
    int wins;
    int loses;
    int total;
    int status;
    int ispass;
    int isdone;
    int life;
    int conn_ndx;
    int idx;
	int rank;
	int score;
	int is_wait_accept;
	int invite_id;
	int invite_did;
    int isadmin;
    int wait_action_idle;
    int noresponse;
    buffer* email;
	buffer* iconurl;
	buffer* descurl;
    time_t wait_action_ts;
    struct timer_check tc;
    long ts_wait;
    int coin;
    int ch_coin;
    
	int payFrom;
	int payTo;
	int groupid;
	int groupowner;
    int isblock;
	buffer* groupname;
} USER_T;
typedef struct group_t{
	int id;
	buffer* name;
	int score;
	int member_num;
	USER_T* owner;
}GROUP_T;
USER_T* login(char* email,char* password,char* sn);
USER_T* getUserByConn(int ndx);
USER_T* getUser(int uid);
int getUserInfo(USER_T* u,char* name,int len,int* wins,int* totals,int* status);
void resetUser(USER_T* user);
int freeUser(USER_T* u);
int insertUser(USER_T* u);
void add_admin(int id,int perm);
USER_T* u_register(char* email,char* password,char* sn);
USER_T* existUser(char* email);
#define GAME_COST 10
#define OBSERVE_COST 2
#endif

