#include "server.h"
#include "msg_tpl.h"
#include "log.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#include <errno.h>
#include <assert.h>
#include "settings.h"
#include "list.h"
#include "desk.h"
#include "user.h"
#include "rank.h"
#include "goapi.h"
#include "timer_check.h"
#include "app.h"

#include "modgo.h"

#ifdef DMALLOC
#include "dmalloc.h"
#endif

#undef GO_DEBUG_ON
#ifdef GO_DEBUG_ON
#define logd printf
#else
#define logd
#endif

#if(1)
#define MAX_STEP_TIMEOUT 60*3
#define MAX_JOIN_TIMEOUT 60*3
#define MAX_DIANMU_TIMEOUT 30*3
#define MAX_WAIT_ME_BACK_TIMEOUT 60*3
#define MIN_GAME_TIME 60*3
#else
#define MAX_STEP_TIMEOUT 15
#define MAX_JOIN_TIMEOUT 15
#define MAX_DIANMU_TIMEOUT 15
#define MAX_WAIT_ME_BACK_TIMEOUT 15
#define MIN_GAME_TIME 30 
#endif

#define TIMER_TYPE_STEP 0
#define TIMER_TYPE_RESUME 1
#define TIMER_TYPE_JOIN 2
#define TIMER_TYPE_DIANMU 3


#define JSON_DID "\"1\":"
#define JSON_STEP_TIMEOUT "\"2\":"

void calDianMu(int did,buffer* b);
void enterDianMu(int did,buffer* b);


char* debug_timer[]={
	"STEP",
	"RESUME",
	"JOIN",
	"DIANMU",
};

struct tc_t{
	struct list_head list;
	char type;
	long start;
	long duration;
	char remove;
	int uid;
};
int getMaxStepTimeout(int did){
	return getDeskStepTimeOut(did);
}

static pthread_mutex_t tc_mutex;


//timer list
static LIST_HEAD(tc_list);
//timeout waiting for process list
static LIST_HEAD(to_list);

void init_appgo(){
    pthread_mutex_init(&tc_mutex, NULL);
}

void clearAllTimer(USER_T* user){
	struct list_head *pos;
	struct list_head *temp;

    pthread_mutex_lock(&tc_mutex);
	list_for_each_safe(pos,temp, &tc_list){
		struct tc_t * tc= list_entry(pos, struct tc_t, list);
		if(tc->uid == user->id && tc->remove==0){
			dprintf("cancel the timer[%s] for %s\n",debug_timer[tc->type],user->email->ptr);
			tc->remove=1;
			//list_del(pos);
			//free(tc);
		}
	}
    pthread_mutex_unlock(&tc_mutex);
}

void clearTimer(USER_T* user,char type){
	struct list_head *pos;
	struct list_head *temp;

    pthread_mutex_lock(&tc_mutex);
	list_for_each_safe(pos,temp, &tc_list){
		struct tc_t * tc= list_entry(pos, struct tc_t, list);
		if(tc->type==type && tc->uid == user->id && tc->remove==0){
			dprintf("cancel the timer[%s] for %s\n",debug_timer[type],user->email->ptr);
			tc->remove=1;
			//list_del(pos);
			//free(tc);
			break;
		}
	}
    pthread_mutex_unlock(&tc_mutex);
}

void setTimer(USER_T* user,long start,long duration,char type){

	struct tc_t* newtc = (struct tc_t*)malloc(sizeof(struct tc_t));
	if(type==TIMER_TYPE_STEP) user->ts_wait=start;
	newtc->start=start;
	newtc->duration = duration;
	newtc->uid = user->id;
	newtc->remove=0;
	newtc->type = type;
    pthread_mutex_lock(&tc_mutex);
	list_add(&newtc->list, &tc_list);
    pthread_mutex_unlock(&tc_mutex);
	dprintf("set the timer[%s] for %s\n",debug_timer[type],user->email->ptr);
}


/* parameters */
struct {
	unsigned char isBroadcast;
	unsigned int loginNum;
	unsigned int gameNum;

} goParam= {
	1,
	0,
	0,
};
extern USER_T** u_table;
int join_desk(void* srv,user_msg_t* umsg,int d_id, int s_id,USER_T* me,int isInvite,buffer* bresponse);

void* global_server = NULL;
static int getWaitBackTime(){
	return MAX_WAIT_ME_BACK_TIMEOUT;
}
static int isEnableGoBack(){
	return 1;
}
static inline void setServer(void* srv)
{
	global_server = srv;
}

static inline void* getServer()
{
	return global_server;
}
static inline int getConnection(USER_T* usr)
{
	if(usr==NULL) return NULL;
	return usr->conn_ndx;
}
static inline int getPeerConnection(USER_T* usr)
{
	USER_T* pu = getPeerUser(usr);
	if(pu!=NULL) return getConnection(pu);
	return NULL;
}

static inline int setUserStatus(USER_T* who,int status)
{
	int pre_status = who->status;
	who->status = status;
	return pre_status;
}
void broadcastAlarmToAdmin(void* srv,int did)
{
	int ndx=0;
	int *conn_ndxs=0;
	buffer *bb=buffer_init();
	buffer_append_string(bb,"notify:alarm,");
	buffer_append_long(bb,did);
	buffer_append_string(bb,",");
	int cnt = getAllAdmins(&conn_ndxs);
	for (ndx = 0; ndx < cnt; ndx++) {
		send_response(srv,conn_ndxs[ndx],bb);
		//connection_state_machine(srv,umsg,bresponse);
	}
	free(conn_ndxs);
	buffer_free(bb);

}
void broadcastToAll(void* srv,buffer* bb,int myConn)
{
	int ndx=0;
	int *conn_ndxs=0;
	int cnt = getAllConns(&conn_ndxs);
	for (ndx = 0; ndx < cnt; ndx++) {
		send_response(srv,conn_ndxs[ndx],bb);
		//connection_state_machine(srv,umsg,bresponse);
	}
	free(conn_ndxs);
}
void broadcastToRoom(void* srv,buffer* bb,int myConn)
{
	int ndx=0;
	int *conn_ndxs=0;
	int cnt = getAllConnsInRoom(&conn_ndxs);
	for (ndx = 0; ndx < cnt; ndx++) {

		if(conn_ndxs[ndx]==myConn) continue;
		send_response(srv,conn_ndxs[ndx],bb);
		//connection_state_machine(srv,umsg,bresponse);
	}
	free(conn_ndxs);
}

int sendDataOverConnection(void* srv,
		USER_T* u,
		buffer* bb)
{
	if (u->conn_ndx!=-1) {
		send_response(srv,u->conn_ndx,bb);
		return 0;
	}
	return -1;
}
int notify_user(void* srv,USER_T* u,buffer* bb)
{
	if (u->conn_ndx!=-1) {
		sendDataOverConnection(srv,u,bb);
		return 0;
	}
	return -1;
}
int app_handle_pay(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	USER_T *u=NULL;
	int ret = 0;

	return ret;
}
int app_handle_list_group(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = 0;
	
	return ret;
}

int app_handle_list_group_user(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = 0;
	
	return ret;
}


int app_handle_join_group(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = 0;
	
	return ret;
}

int app_handle_leave_group(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = 0;
	
	return ret;
}

int app_handle_register_group(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = 0;
	
	return ret;
}

int app_handle_register(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	USER_T *u=NULL;
	int ret = 0;
	data_string *email= (data_string *)array_get_element(
			umsg->headers,"email");
	data_string *sn= (data_string *)array_get_element(
			umsg->headers,"sn");
	data_string *password= (data_string *)array_get_element(
			umsg->headers,"password");
	buffer *b = bresponse;
	if(email != NULL && password != NULL) {
		int bValid=1;
		for(int i = 0 ; i<email->value->used; i++) {
			char c = email->value->ptr[i];
			if(c=='*' || c==',' || c==')' || c==' ' || c=='@' || c=='(' || c=='&' || c=='%' || c=='!') {
				bValid=0;
				break;
			}

			if(i==150){
				email->value->ptr[i]=0;				
				break;
			}
		}
		if(bValid==1)
		{
			if(sn!=NULL)
			u = u_register(email->value->ptr,password->value->ptr,sn->value->ptr);
			else
			u = u_register(email->value->ptr,password->value->ptr,NULL);
		}
	}
	if ( u == NULL) {
		ret = -1;
		debug_log("s", "registe failed");
		buffer_append_string(b,"response:register,no,");
	} else {
		u->conn_ndx=umsg->ndx;
		buffer_append_string(b,"response:register,ok,");
		buffer_append_long(b,u->id);
		buffer_append_string(b,",");
		ret = 0;
	}
	return ret;
}
/* response:resume,uid,did,sid,next,game_data,"*/
int app_handle_resume(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	USER_T* u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	void* game = getGame(u->did);
	if(game==NULL) return -1;
	buffer* bb = buffer_init();
	dumpToHex(game,bb);
	int next = getCurTurn(game);
	buffer* b = bresponse;
	buffer_append_string(b,"response:resume,");
	buffer_append_long(b,200);
	buffer_append_string(b,",");
	buffer_append_long(b,u->id);
	buffer_append_string(b,",");
	buffer_append_long(b,u->did);
	buffer_append_string(b,",");
	buffer_append_long(b,u->sid);
	buffer_append_string(b,",");
	buffer_append_long(b,next);
	buffer_append_string(b,",");
	buffer_append_string(b,bb->ptr);
	buffer_append_string(b,",");
	buffer_free(bb);

	//append last step
	int x,y;
	getLastStep(game,&x,&y);
	buffer_append_long(b,x);
	buffer_append_string(b,",");
	buffer_append_long(b,y);
	buffer_append_string(b,",");

	//append game status
	if(getDeskStatus(u->did)==DESK_DIANMU){
		buffer_append_string(b,"dianmu,");
		buffer* bb2 = buffer_init();
		int num = dumpDeads(game,bb2);
		buffer_append_long(b,num);
		buffer_append_string(b,",");
		buffer_append_string(b,bb2->ptr);
		
		buffer_free(bb2);	
		
		
	}else{
		buffer_append_string(b,"play,");
	}

	//append step resume
	if(next==u->sid){
		buffer_append_long(b,getMaxStepTimeout(u->did)-time(NULL)+u->ts_wait);
	}else{
		buffer_append_long(b,getMaxStepTimeout(u->did));
	}
	buffer_append_string(b,",");

	return 0;
}
int app_handle_login(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	buffer *b=bresponse;
	int ret = -1;
	data_string *email= (data_string *)array_get_element(
			umsg->headers,"email");
	data_string *sn= (data_string *)array_get_element(
			umsg->headers,"sn");

	data_string *password= (data_string *)array_get_element(
			umsg->headers,"password");

	USER_T *me=NULL;
	USER_T *me2=NULL;
	goParam.loginNum++;
	if(email != NULL && password != NULL) {
		
		// login will return the user's id if login OK.
		me=existUser(email->value->ptr);
		if(sn != NULL)	
			me2 = login(email->value->ptr,password->value->ptr,sn->value->ptr);
		else
			me2 = login(email->value->ptr,password->value->ptr,NULL);
	}else{
		return -1;
	}
	debug_log("sss","login",email->value->ptr,password->value->ptr);
	if (me2 == NULL) {
		debug_log("s", "login failed");
		buffer_append_string(b,"response:login,no,");
	} else {
		if(me!=NULL) {
			int old = getConnection(me);
			//!!!hack hack todo, there something error
			//happend caused duplicate login
			if(isInGame(me)==1) {
				clearTimer(me,TIMER_TYPE_RESUME);
				debug_log("s","I am back to the game");
			} else if(isInGame(me)==0 && isInDesk(me)==1) {
				debug_log("ss",": user exist in desk but no in game"
						" happened in the case both side exit suddenly",__func__);
				app_handle_leave(srv,me);
			}
			if(old!=-1 && old!=umsg->ndx) {
				dprintf("%s", "Error: A dummy connection exist!");
			}
		}
	
		//对于以后的用户， me 和me2指向同一个对象。	
		me2->conn_ndx = umsg->ndx;
		buffer_append_string(b,"response:login,ok,");
		debug_log("sd", "login ok",me2->id);

		buffer_append_long(b,me2->id);
		buffer_append_string(b,",");
		int resumeType=0;
		if(me!=NULL && isInGame(me)==1) {
			buffer_append_string(b,"resume,");
			resumeType=1;
		}else{
			buffer_append_string(b,"normal,");
		}
		rank_map_t* rmap=NULL;
		rmap=getRankMap(me2->rank);
		buffer_append_string(b,rmap->label);
		buffer_append_string(b,",");
		char* utype = "P";
		if(is_admin(me2->id)==0) {
			me2->isadmin=1;
			buffer_append_string(b,"A,");
			utype="A";
		} else {
			me2->isadmin=0;
			buffer_append_string(b,"P,");
		}
		buffer_append_long(b,me2->wins);
		buffer_append_string(b,",");
		buffer_append_long(b,me2->loses);
		buffer_append_string(b,",");

		char json[512];
		sprintf(json,M_RESP_LOGIN_OK,200,me2->id,resumeType,rmap->label,utype,me2->wins,me2->loses);
		dprintf(json);
		if(isInGame(me2)==0)
			setUserStatus(me2,USER_LOGIN);
	}
	return ret;
}
int app_handle_observe(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T* me = getUserByConn(umsg->ndx);
	data_string *did= (data_string *)array_get_element(
			umsg->headers,"did");
	buffer *b = bresponse;
	if(did != NULL ) {
		int d_id=atoi(did->value->ptr);
		int u_id=me->id;
		if(me->did>0 && me->sid>0) {
			app_handle_leave(srv,me);
		}
		if(getDeskStatus(d_id)!=DESK_PLAYING) {
			buffer_append_string(b,"response:observe,404,not on playing");
			return 0;
		} else {
			/*
			if(me->coin<OBSERVE_COST){
				buffer_append_string(b,"response:observe,401,coin_no");
				return 0;
			}
			*/
			
			int s_id = joinObserver(d_id,me);
			if(s_id>0) {
				buffer* bb = buffer_init();
				void* game = getGame(d_id);
				dumpToHex(game,bb);
				buffer_append_string(b,"response:observe,200,");
				buffer_append_long(b,d_id);
				buffer_append_string(b,",\r\n\r\nnotify:observe_start,");
				buffer_append_long(b,d_id);
				buffer_append_string(b,",");
				buffer_append_string(b,bb->ptr);
				buffer_append_string(b,",");
				//append last step

				int x,y;
				getLastStep(game,&x,&y);
				buffer_append_long(b,x);
				buffer_append_string(b,",");
				buffer_append_long(b,y);
				buffer_append_string(b,",");

				setUserStatus(me,USER_OBSERVE);
				buffer_free(bb);
				return 0;
			} else {
				buffer_append_string(b,"response:observe,404,observer full");
				return 0;
			}
		}
	}
}
int app_handle_users(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *me=getUserByConn(umsg->ndx);
	buffer *b = bresponse;
	if(me == NULL) return ret;
	USER_T* u;
	char name[MAX_NAME_LEN];
	int wins,totals,status;
	buffer_append_string(b,"response:users,\r\n");
	for(int i = 0 ; i< MAX_USERS ; i++) {
		u = u_table[i];
		if(u!=NULL) {
			getUserInfo(u,name,
					MAX_NAME_LEN,&wins,&totals,&status);
			buffer_append_long(b,u->id);
			buffer_append_string(b,",");
			buffer_append_string(b,name);
			buffer_append_string(b,",");
			buffer_append_long(b,u->wins);
			buffer_append_string(b,",");
			buffer_append_long(b,u->loses);
			buffer_append_string(b,",");
			buffer_append_long(b,u->sid);
			buffer_append_string(b,",");
			buffer_append_long(b,u->did);
			buffer_append_string(b,",");
			buffer_append_long(b,u->score);
			buffer_append_string(b,",");
			rank_map_t* rmap=NULL;
			rmap=getRankMap(u->rank);
			buffer_append_long(b,rmap->score);
			buffer_append_string(b,",");
			buffer_append_string(b,rmap->label);
			buffer_append_string(b,",\r\n");

		}
	}
	buffer_append_string(b,"0,EOF,");
	return 0;
}

int app_handle_join_invite(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *me=getUserByConn(umsg->ndx);
	buffer *b = bresponse;
	if(me == NULL) return ret;
	if(me->invite_id==-1) {
		ret=-1;
		goto _error1;
	}
	USER_T *pu=getUser(me->invite_id);
	if(pu == NULL) goto _error1;
	if(pu->invite_id!=me->id) {
		ret=-2;
		goto _error1;
	}

	int did,sid;
	//peer user already find a desk, we join the same desk
	if(pu->invite_did>0) {
		did=pu->invite_did;
		sid=2;
		//reset flag here
		pu->invite_did=-1;
		pu->is_wait_accept=-1;
		pu->invite_id=-1;
		me->invite_did=-1;
		me->is_wait_accept=-1;
		me->invite_id=-1;
	} else {
		did=find_free_desk();
		me->invite_did=did;
		if(did<=0) {
			ret=-3;
			goto _error1;
		}
		sid=1;
	}

	if(join_desk(srv,umsg,did,sid,me,1,bresponse)<0) {
		ret = -4;
		goto _error1;
	}

	return 0;
_error1:
	return ret;
}
int join_desk(void* srv,user_msg_t* umsg,int d_id, int s_id,USER_T* me,int isInvite,buffer* bresponse)
{
	int ret = -1;
	int u_id=me->id;
	int odid=me->did;
	int osid=me->sid;
	debug_log("ssddd",__func__,"enter",d_id,s_id,u_id);
	buffer *b = bresponse;
	if(odid>0 && osid>0) {
		if(!(odid==d_id && osid==s_id)) app_handle_leave(srv,me);
	}
	if(joinDesk(d_id,s_id,me) == 0) {
		rank_map_t* rmap=NULL;
		rmap=getRankMap(me->rank);
		USER_T *pu;
		ret = 0;
		DESK_T* desk = getDesk(me->did);
		if(isInvite==0)
			buffer_append_string(b,"response:join,200,");
		else
			buffer_append_string(b,"response:join_invite,200,");
		buffer_append_long(b,d_id);
		buffer_append_string(b,",");
		buffer_append_long(b,s_id);
		buffer_append_string(b,",");
		setUserStatus(me,USER_JOIN);
		pu = getPeerUser(me);
		//notify:join,did,sid,uid,status,name,wins/totals
		if(pu==NULL){
			buffer_append_string(b,"\r\n\r\nnotify:req_time_out,");
		}
		if(pu!=NULL || goParam.isBroadcast==1) {
			char name[MAX_NAME_LEN];
			int wins,totals,status;
			buffer* b = buffer_init();
			buffer_append_string(b,"notify:join,");
			buffer_append_long(b,d_id);
			buffer_append_string(b,",");
			buffer_append_long(b,s_id);
			buffer_append_string(b,",");
			buffer_append_long(b,u_id);
			buffer_append_string(b,",");
			getUserInfo(me,name,
					MAX_NAME_LEN,&wins,&totals,&status);
			buffer_append_long(b,status);
			buffer_append_string(b,",");
			buffer_append_string(b,rmap->label);
			buffer_append_string(b,"|");
			buffer_append_string(b,name);
			buffer_append_string(b,",");
			buffer_append_long(b,wins);
			buffer_append_string(b,"/");
			buffer_append_long(b,totals);
			buffer_append_string(b,",");
			if(goParam.isBroadcast==1) {
				debug_log("s","broadcast the join event to all");
				broadcastToRoom(srv,b,me->conn_ndx);
			}
			if(pu!=NULL) {
				notify_user(srv,pu,b);
			}
			if(desk!=NULL && desk->observer_num>0) {
				for(int k=0; k<MAX_OBSERVER; k++) {
					USER_T* uo=desk->observers[k];
					if(uo!=NULL && uo!=me)
						notify_user(srv,uo,b);
				}
			}
			buffer_free(b);
			debug_log("s","start join wait timer");
		}
		clearTimer(me,TIMER_TYPE_JOIN);
		setTimer(me,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		return 0;
	}
	return -1;
}
int app_handle_join(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *me=getUserByConn(umsg->ndx);
	data_string *did= (data_string *)array_get_element(
			umsg->headers,"did");
	data_string *sid= (data_string *)array_get_element(
			umsg->headers,"sid");
	if(me == NULL) return ret;

	if(did != NULL && sid != NULL) {
		int d_id=atoi(did->value->ptr);
		int s_id=atoi(sid->value->ptr);
		ret = join_desk(srv,umsg,d_id,s_id,me,0,bresponse);
	}
	if (ret < 0) {
		debug_log("s","join failed");
	}
	return ret;
}
int app_handle_read_property(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *me=getUserByConn(umsg->ndx);
	data_string *key= (data_string *)array_get_element(
			umsg->headers,"key");
	if(me == NULL) return ret;
	buffer* bsend = bresponse;
	buffer_append_string(bsend,"response:read-property,");
	if(key!=NULL) {
		char* value = read_property(key->value->ptr);
		if(value!=NULL){
			buffer_append_string(bsend,"200,");
			buffer_append_string(bsend,value);
			ret = 0;
		}else
			buffer_append_string(bsend,"404,");
		
	} else {
		buffer_append_string(bsend,"404,");
	}
	return ret;
}
int app_handle_update_property(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *me=getUserByConn(umsg->ndx);
	data_string *key= (data_string *)array_get_element(
			umsg->headers,"key");
	data_string *value= (data_string *)array_get_element(
			umsg->headers,"value");
	if(me == NULL) return ret;

	buffer* bsend = bresponse;
	buffer_append_string(bsend,"response:update-property,");
	if(key!=NULL && value!=NULL) {
		update_property(key->value->ptr,value->value->ptr);
		buffer_append_string(bsend,"200,");
		ret = 0;
	} else {
		buffer_append_string(bsend,"404,");
	}
	return ret;
}
void* startGoGame(void* srv,int did,user_msg_t* umsg)
{
	void* p = logicgo_init();
	setDeskStatus(did,DESK_PLAYING);
	setGame(did,p);
	setSize(p,19);
	startGame(p,time(NULL));
	DESK_T* desk = getDesk(did);
	setGameUser(p,desk->bu->id,desk->wu->id);
	debug_log("s","startGoGame");
	return p;
}
int app_handle_dump(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	data_string *sdid= (data_string *)array_get_element(
			umsg->headers,"did");
	int did = atoi(sdid->value->ptr);
	buffer* bsend = bresponse;
	if(did <=0) {
		buffer_append_string(bsend,"response:dump,404,");
	} else if(getDeskStatus(did)==DESK_PLAYING) {
		buffer* b = buffer_init();
		void* game = getGame(did);
		int size = dumpGo(game,b);
		buffer_append_long(bsend,size);
		buffer_append_string(bsend,",");
		buffer_append_string(bsend,"\r\n");
		buffer_append_string(bsend,b->ptr);
	} else {
		buffer_append_string(bsend,"response:dump,404,");
	}

}

int app_handle_message(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	buffer *b;
	b = bresponse;
	int ret = -1;
	data_string *suid= (data_string *)array_get_element(
			umsg->headers,"uid");
	data_string *content= (data_string *)array_get_element(
			umsg->headers,"content");
	USER_T* me = getUserByConn(umsg->ndx);
	if(me==NULL) return -1;
	if(suid==NULL || content==NULL) {
		ret = -1;
		goto _error1;
	}
	int uid = atoi(suid->value->ptr);
	USER_T* pu = getUser(uid);
	if(pu==NULL) {
		ret = -2;
		goto _error1;
	}
	buffer* bnotify = buffer_init();
	buffer_append_string(bnotify,"notify:message,");
	buffer_append_long(bnotify,me->id);
	buffer_append_string(bnotify,"[");
	buffer_append_string(bnotify,content->value->ptr);
	buffer_append_string(bnotify,"]");
	buffer_append_string(b,"response:message,ok,");
	return 0;
_error1:
	buffer_append_string(b,"response:message,error,");
	return ret;

}

void publish_live_match_start(void* srv,int did,USER_T* bu,USER_T* wu,USER_T** oids){


	mod_go* mgo = (mod_go*)srv;
	buffer* json = buffer_init();
	char ss[25];
	buffer_append_string(json,"{");
	
	
	buffer_append_string(json,"\"action\":\"match-start\",");


	sprintf(ss,"%d",did);
	buffer_append_json_string(json,"did",ss);
	buffer_append_string(json,",");

	if(bu != NULL){
		memset(ss,0,sizeof(ss));
		sprintf(ss,"%d",bu->id);
		buffer_append_json_string(json,"bid",ss);
		buffer_append_string(json,",");
	}

	if(bu != NULL && bu->email != NULL)
		buffer_append_json_string(json,"bname",bu->email->ptr);
	else
		buffer_append_json_string(json,"bname"," ");
	buffer_append_string(json,",");

	if(bu != NULL){
		char* label = getRankMap(bu->rank)->label;
		buffer_append_json_string(json,"brank",label);
	}
	else
		buffer_append_json_string(json,"brank","17k");

	buffer_append_string(json,",");


	if(wu != NULL){
		memset(ss,0,sizeof(ss));
		sprintf(ss,"%d",wu->id);
		buffer_append_json_string(json,"wid",ss);
		buffer_append_string(json,",");
	}

	if(wu != NULL && wu->email != NULL)
		buffer_append_json_string(json,"bname",wu->email->ptr);
	else
		buffer_append_json_string(json,"bname"," ");
	buffer_append_string(json,",");

	if(wu != NULL){
		char* label = getRankMap(wu->rank)->label;
		buffer_append_json_string(json,"brank",label);
	}
	else
		buffer_append_json_string(json,"brank","17k");
	
	buffer_append_string(json,",");

	

	buffer_append_string(json,"\"obserers\":[");

	if(oids!=NULL){

		int i =0;

		while(oids[i]!=0){

			USER_T* u = oids[i];
			char* label = getRankMap(u->rank)->label;
			
			if(i>0) buffer_append_string(json,",");

			buffer_append_string(json,"{");


			memset(ss,0,sizeof(ss));			
			
			sprintf(ss,"%d",u->id);
			buffer_append_json_string(json,"uid",ss);
			buffer_append_string(json,",");

			if(u->email!=NULL){
				buffer_append_json_string(json,"name",u->email->ptr);
				buffer_append_string(json,",");

			}	

			buffer_append_json_string(json,"rank",label);
			

			buffer_append_string(json,"}");

			i++;
		}

	}

	buffer_append_string(json,"]");
	buffer_append_string(json,"}");	
	publish_live_string(mgo->srv,json->ptr);
	buffer_free(json);

}

void publish_live_match_end(void* srv, int did,int winner_uid,int loser_uid,buffer* brecord){
		mod_go* mgo = (mod_go*)srv;
	buffer* json = buffer_init();
	USER_T* u = getUser(winner_uid);
	char ss[25];
	buffer_append_string(json,"{");
	
	
	buffer_append_string(json,"\"action\":\"match-end\",");


	sprintf(ss,"%d",did);
	buffer_append_json_string(json,"did",ss);
	buffer_append_string(json,",");

	memset(ss,0,sizeof(ss));
	sprintf(ss,"%d",winner_uid);
	buffer_append_json_string(json,"winner_uid",ss);
	buffer_append_string(json,",");

	if(u!=NULL && u->email!=NULL){
		buffer_append_json_string(json,"winner_name",u->email->ptr);			
	}else
		buffer_append_json_string(json,"winner_name"," ");

	buffer_append_string(json,",");


	if(u!=NULL){

		memset(ss,0,sizeof(ss));
		sprintf(ss,"%d",u->sid);
		buffer_append_json_string(json,"winner_sid",ss);
		buffer_append_string(json,",");
				
	}

	






	memset(ss,0,sizeof(ss));
	sprintf(ss,"%d",loser_uid);
	buffer_append_json_string(json,"loser_uid",ss);
	buffer_append_string(json,",");


	buffer_append_json_string(json,"record",brecord->ptr);



	buffer_append_string(json,"}");	
	publish_live_string(mgo->srv,json->ptr);
	buffer_free(json);
}

void publish_live_match_play(void* srv,int did,int side,int x,int y,int kill_num,buffer* bkill,buffer* brecord,USER_T* bu,USER_T* wu,USER_T** oids){


	mod_go* mgo = (mod_go*)srv;
	buffer* json = buffer_init();
	char ss[25];
	buffer_append_string(json,"{");
	
	
	buffer_append_string(json,"\"action\":\"match-play\",");







	sprintf(ss,"%d",did);
	buffer_append_json_string(json,"did",ss);
	buffer_append_string(json,",");


	buffer_append_string(json,"\"black\":{");

	if(bu!=NULL){
		USER_T* u = bu;
			char* label = getRankMap(u->rank)->label;
			
			memset(ss,0,sizeof(ss));			
			
			sprintf(ss,"%d",u->id);
			buffer_append_json_string(json,"uid",ss);
			buffer_append_string(json,",");

			if(u->email!=NULL){
				buffer_append_json_string(json,"name",u->email->ptr);
				buffer_append_string(json,",");

			}	

			buffer_append_json_string(json,"rank",label);
			

			
	}
	buffer_append_string(json,"},");



		buffer_append_string(json,"\"white\":{");

	if(wu!=NULL){
		USER_T* u = wu;
			char* label = getRankMap(u->rank)->label;
			
			memset(ss,0,sizeof(ss));			
			
			sprintf(ss,"%d",u->id);
			buffer_append_json_string(json,"uid",ss);
			buffer_append_string(json,",");

			if(u->email!=NULL){
				buffer_append_json_string(json,"name",u->email->ptr);
				buffer_append_string(json,",");

			}	

			buffer_append_json_string(json,"rank",label);
			

			
	}
	buffer_append_string(json,"},");


buffer_append_string(json,"\"observers\":[");

	if(oids!=NULL){

		int i =0;

		while(oids[i]!=0){

			USER_T* u = oids[i];
			char* label = getRankMap(u->rank)->label;
			
			if(i>0) buffer_append_string(json,",");

			buffer_append_string(json,"{");


			memset(ss,0,sizeof(ss));			
			
			sprintf(ss,"%d",u->id);
			buffer_append_json_string(json,"uid",ss);
			buffer_append_string(json,",");

			if(u->email!=NULL){
				buffer_append_json_string(json,"name",u->email->ptr);
				buffer_append_string(json,",");

			}	

			buffer_append_json_string(json,"rank",label);
			

			buffer_append_string(json,"}");

			i++;
		}

	}

		buffer_append_string(json,"],");



	memset(ss,0,sizeof(ss));
	sprintf(ss,"%d",side);
	buffer_append_json_string(json,"sid",ss);
	buffer_append_string(json,",");


	memset(ss,0,sizeof(ss));
	sprintf(ss,"%d",x);
	buffer_append_json_string(json,"x",ss);
	buffer_append_string(json,",");


	memset(ss,0,sizeof(ss));
	sprintf(ss,"%d",y);
	buffer_append_json_string(json,"y",ss);
	buffer_append_string(json,",");



	memset(ss,0,sizeof(ss));
	sprintf(ss,"%d",kill_num);
	buffer_append_json_string(json,"kn",ss);
	buffer_append_string(json,",");


	if(bkill->used>0) {
		buffer_append_json_string(json,"kill",bkill->ptr);
		
	}else{
		buffer_append_json_string(json,"kill"," ");
	}
	buffer_append_string(json,",");


	buffer_append_json_string(json,"record",brecord->ptr);



	buffer_append_string(json,"}");	
	publish_live_string(mgo->srv,json->ptr);
	buffer_free(json);

}

void publish_live_chat(void* srv,int uid,int did,char* name,char* content){
	
	mod_go* mgo = (mod_go*)srv;
	buffer* json = buffer_init();
	char ss[25];
	buffer_append_string(json,"{");
	
	buffer_append_string(json,"\"action\":\"chat\",");
	

	sprintf(ss,"%d",uid);
	buffer_append_json_string(json,"uid",ss);
	buffer_append_string(json,",");

	memset(ss,0,sizeof(ss));
	sprintf(ss,"%d",did);
	buffer_append_json_string(json,"did",ss);
	buffer_append_string(json,",");
	buffer_append_json_string(json,"name",name);
	buffer_append_string(json,",");
	buffer_append_json_string(json,"content",(content));
	// buffer_append_string(json,",");
	
	buffer_append_string(json,"}");


	
	publish_live_string(mgo->srv,json->ptr);
	buffer_free(json);


}

int app_handle_post_talk(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	data_string *ds;
	buffer* content=NULL;
	int cl=0;
	buffer* b = NULL;
	USER_T* me = getUserByConn(umsg->ndx);
	if(me==NULL) return -1;

	if(me->isblock>0) return -1;
	ds = (data_string *)array_get_element(
			umsg->headers,"content-length");
	if(ds!=NULL) {
		cl=atoi(ds->value->ptr);
		if(cl>0) {
			content=buffer_init();
			buffer_append_string(content,umsg->postdata);
		}

	}


	if(content!=NULL) {
		USER_T* pu;
		b = bresponse;
		buffer_append_string(b,"notify:talk,");
		buffer_append_string(b,"FROM[");
		buffer_append_long(b,me->sid);
		buffer_append_string(b,"]");
		buffer_append_string(b,"CONTENT[");
		if(me->sid>=3) { //means a observer is talking
			int len = MAX_NAME_LEN;
			char name[MAX_NAME_LEN+1];
			for(int i = 0; i<len ; i++) {
				name[i] = me->email->ptr[i];
				if (name[i]==0) break;
				if (name[i]=='@') {
					name[i]=0;
					break;
				}
			}
			name[len]=0;
			buffer_append_string(b,name);
			buffer_append_string(b,":");
			buffer_append_string(b,content->ptr);
		} else
			buffer_append_string(b,content->ptr);

		//add to live-chat of database 
		{
			
			dbAddChatRecord(me->id,me->did,me->email->ptr,content->ptr);
			publish_live_chat(srv,me->id,me->did,me->email->ptr,content->ptr);

			


		}

		buffer_append_string(b,"]");
		if(me->sid>=3) { //means a observer is talking
			USER_T* uu = getSide(me->did,1);
			if(uu!=NULL) notify_user(srv,uu,b);
			uu = getSide(me->did,2);
			if(uu!=NULL) notify_user(srv,uu,b);
			//send to other observer beside of me
			{
				DESK_T* desk = getDesk(me->did);
				if(desk!=NULL && desk->observer_num>0) {
					for(int k=0; k<MAX_OBSERVER; k++) {
						USER_T* uo=desk->observers[k];
						if(uo!=NULL && uo!=me)
							notify_user(srv,uo,b);
					}
				}
			}
		} else {
			pu = getPeerUser(me);
			//notify:ready,uid,name,wins/totals
			if(pu!=NULL) {
				notify_user(srv,pu,b);
			}
			//notify observer
			{
				DESK_T* desk = getDesk(me->did);
				if(desk!=NULL && desk->observer_num>0) {
					for(int k=0; k<MAX_OBSERVER; k++) {
						USER_T* uo = desk->observers[k];
						if(uo!=NULL) notify_user(srv,uo,b);
					}
				}
			}
		}
		debug_log("s",b->ptr);
		buffer_free(content);
	}
	return 0;
}
int app_handle_reboot(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	
	return -1;

}
int app_handle_addadmin(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	USER_T *pu,*u;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	if(u->id!=14370) {
		return -1;
	}
	data_string *sid= (data_string *)array_get_element(
			umsg->headers,"id");
	data_string *sperm= (data_string *)array_get_element(
			umsg->headers,"permission");
	int id =atoi(sid->value->ptr);
	int iperm=atoi(sperm->value->ptr);
	add_admin(id,iperm);
	return 0;

}
int app_handle_alarm(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	USER_T *pu,*u;
	u = getUserByConn(umsg->ndx);
	logd("app_handle_alarm\n");
	if(u==NULL) return -1;
	if(isInDesk(u)!=1) return -1;
	broadcastAlarmToAdmin(srv,u->did);
	return 0;
}
int app_handle_judge(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	USER_T *pu,*u;
	u = getUserByConn(umsg->ndx);
	logd("app_handle_judge\n");
	if(u==NULL) return -1;
	if(u->isadmin==0) return -1;
	if(u->status!=USER_OBSERVE) return -1;
	data_string *sdid= (data_string *)array_get_element(
			umsg->headers,"did");
	data_string *swin= (data_string *)array_get_element(
			umsg->headers,"win");
	int did =atoi(sdid->value->ptr);
	did=u->did;
	int win=atoi(swin->value->ptr);
	void* game = getGame(did);
	if(game==NULL) return -1;

	DESK_T* desk = getDesk(did);
	USER_T* lose_u=NULL;
	if(win==1)
		lose_u = desk->wu;
	if(win==2)
		lose_u = desk->bu;
	if(lose_u==NULL) return -1;
	int lose_con=getConnection(lose_u);
	if(lose_con!=-1) {
		judge_giveup(srv,lose_con);
	}
	return 0;
}
int app_handle_stat(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	buffer* bsend = bresponse;
	buffer_append_string(bsend,"response:stat,");
	buffer_append_long(bsend,goParam.loginNum);
	buffer_append_string(bsend,",");
	buffer_append_long(bsend,goParam.gameNum);
	buffer_append_string(bsend,",");
	return 0;
}
int app_handle_info(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	USER_T *pu,*u;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	if(getDeskStatus(u->did)==DESK_PLAYING) {
		void* game = getGame(u->did);
		if(game==NULL) {
			buffer* bsend = bresponse;
			buffer_append_string(bsend,"notify:error,reset,info");
			return -1;
		} else {
			buffer* bsend = bresponse;
			int x,y;
			getLastStep(game,&x,&y);
			buffer_append_string(bsend,"response:info,");
			buffer_append_long(bsend,x);
			buffer_append_string(bsend,",");
			buffer_append_long(bsend,y);
			buffer_append_string(bsend,",");
			return 0;
		}
	}
	return -1;
}
int app_handle_score(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	USER_T *pu,*u;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	pu = getPeerUser(u);
	void* game = getGame(u->did);
	if(game==NULL || pu == NULL || u->sid>=3) {
		return -1;
	}
	u->isdone=0;
	pu->isdone=0;
	if(getDeskStatus(u->did)==DESK_PLAYING || getDeskStatus(u->did)==DESK_DIANMU) {
		buffer* bsend = bresponse;
		buffer* b = buffer_init();
		calDianMu(u->did,b);
		buffer_append_string(bsend,"response:score,");
		buffer_append_string(bsend,b->ptr);
		buffer_free(b);
	} else {
		return -1;
	}
}
int app_handle_continue_go(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	USER_T *pu,*u;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	pu = getPeerUser(u);
	void* game = getGame(u->did);
	if(game==NULL || pu == NULL || u->sid>=3) {
		buffer* bsend = bresponse;
		buffer_append_string(bsend,"notify:error,reset,pass,");
		return -1;
	}
	u->isdone=0;
	pu->isdone=0;
	if(getDeskStatus(u->did)==DESK_DIANMU) {
		clearTimer(u,TIMER_TYPE_DIANMU);
		clearTimer(pu,TIMER_TYPE_DIANMU);
		buffer* bsend = bresponse;
		buffer* bnotify = buffer_init();
		buffer* b = buffer_init();
		int next = getCurTurn(game);
		int num = undoDead(game,1,b);
		setDeskStatus(u->did,DESK_PLAYING);
		setUserStatus(u,USER_STEP);
		setUserStatus(pu,USER_STEP);
		buffer_append_string(bnotify,"notify:continuego,");
		buffer_append_long(bnotify,u->sid);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,num);
		buffer_append_string(bnotify,",");
		if(num>=0) buffer_append_string(bnotify,b->ptr);
		b = buffer_init();
		num = undoDead(game,2,b);
		buffer_append_long(bnotify,num);
		buffer_append_string(bnotify,",");
		if(num>=0) buffer_append_string(bnotify,b->ptr);
		//add next side notify
		//notify:next,sid
		buffer_append_string(bsend,bnotify->ptr);
		buffer_append_string(bsend,"\r\n\r\nnotify:next,");
		buffer_append_long(bsend,next);
		buffer_append_string(bsend,",");
		buffer_append_long(bsend,getMaxStepTimeout(u->did));
		buffer_append_string(bsend,",");

		buffer_append_string(bnotify,"\r\n\r\nnotify:next,");
		buffer_append_long(bnotify,next);
		buffer_append_string(bnotify,",");
		//append 每步超时时间
		buffer_append_long(bnotify,getMaxStepTimeout(u->did));
		buffer_append_string(bnotify,",");
		notify_user(srv,pu,bnotify);
		buffer_free(bnotify);
		buffer_free(b);
		if(next==u->sid){
			setTimer(u,time(NULL),getMaxStepTimeout(u->did),TIMER_TYPE_STEP);
		}else{
			setTimer(pu,time(NULL),getMaxStepTimeout(pu->did),TIMER_TYPE_STEP);
		}
	}
}
int app_handle_pass(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	int puid;
	USER_T *pu,*u;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	pu = getPeerUser(u);
	void* game = getGame(u->did);
	if(game==NULL || pu == NULL || u->sid>=3) {
		buffer* bsend = bresponse;
		buffer_append_string(bsend,"notify:error,reset,pass,");
		return -1;
	}
	u->isdone=0;
	pu->isdone=0;
	if(getDeskStatus(u->did)==DESK_PLAYING) {
		buffer* bsend = bresponse;
		if(goPass(game,u->sid)==1) {
			clearTimer(u,TIMER_TYPE_STEP);
			buffer* bnotify = buffer_init();
			int next = getCurTurn(game);
			buffer_append_string(bsend,"response:pass,yes,");
			//notify:next,sid
			buffer_append_string(bsend,"\r\n\r\nnotify:next,");
			buffer_append_long(bsend,next);
			buffer_append_string(bsend,",");
			buffer_append_long(bsend,getMaxStepTimeout(u->did));
			buffer_append_string(bsend,",");
			//If the opponent not pass, notify opponent to press pass two
			if(pu->ispass==0) {
				buffer_append_string(bnotify,"notify:pass,");
				buffer_append_long(bnotify,u->sid);
				buffer_append_string(bnotify,",\r\n\r\n");
			}

			buffer_append_string(bnotify,"notify:next,");
			buffer_append_long(bnotify,next);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,getMaxStepTimeout(u->did));
			buffer_append_string(bnotify,",");
			//add next side notify

			u->ispass = 1;
			//both are pass, enter dianmu
			if(pu->ispass == 1) {
				buffer* b = buffer_init();
				enterDianMu(u->did,b);
				buffer_append_string(bnotify,"\r\n\r\n");
				buffer_append_string(bnotify,b->ptr);
				buffer_append_string(bsend,"\r\n\r\n");
				buffer_append_string(bsend,b->ptr);
				setDeskStatus(u->did,DESK_DIANMU);
				setUserStatus(u,USER_DIANMU);
				setUserStatus(pu,USER_DIANMU);
				pu->ispass=0;
				u->ispass=0;
			}else{
				setTimer(pu,time(NULL),getMaxStepTimeout(pu->did),TIMER_TYPE_STEP);
			}
			notify_user(srv,pu,bnotify);
		} else {
			buffer_append_string(bsend,"response:pass,no,");
		}
	} else {
		buffer* bsend = bresponse;
		buffer_append_string(bsend,"response:pass,403,");
		return -1;
	}
}
int judge_giveup(void* srv,int conn_ndx,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int did;
	u = getUserByConn(conn_ndx);
	logd("judge_giveup enter\n");
	if(u==NULL) return -1;
	if(u->sid>=3) return -1;
	did = u->did;
	pu = getPeerUser(u);
	if(u==NULL) return -1;
	if(getDeskStatus(did)==DESK_PLAYING
			|| getDeskStatus(did)==DESK_DIANMU) {
		buffer* bnotify = buffer_init();
		int winner=1;
		buffer_append_string(bnotify,"notify:game_end,");
		if(u->sid==1)
			winner=2;
		buffer_append_long(bnotify,winner);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,0);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,0);
		buffer_append_string(bnotify,",");


		/* save the record before free the game */
		void* game = getGame(u->did);
		buffer* bRecord = buffer_init();
		if(game!=NULL) getRecord(game,bRecord);

		endGoGame(srv,u->did,u->puid,u->id,bRecord);
		/* append rank */
		rank_map_t* rmap=getRankMap(u->rank);
		buffer_append_long(bnotify,u->id);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,rmap->label);
		buffer_append_string(bnotify,",");
		rmap=getRankMap(pu->rank);
		buffer_append_long(bnotify,pu->id);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,rmap->label);
		buffer_append_string(bnotify,",");

		/* append match record */
		buffer_append_string(bnotify,bRecord->ptr);
		buffer_free(bRecord);
		/*end*/

		{
			DESK_T* desk = getDesk(u->did);
			if(desk->observer_num>0) {
				for(int k=0; k<MAX_OBSERVER; k++) {
					USER_T* uu=desk->observers[k];
					if(uu!=NULL)
						notify_user(srv,uu,bnotify);
				}
			}
		}
		notify_user(srv,u,bnotify);
		setDeskStatus(did,DESK_FULL);
		setUserStatus(u,USER_JOIN);
		startWaitTimer(srv,u,MAX_JOIN_TIMEOUT);
		setTimer(u,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		if(pu!=NULL) {
			notify_user(srv,pu,bnotify);
			setUserStatus(pu,USER_JOIN);
			startWaitTimer(srv,pu,MAX_JOIN_TIMEOUT);
			setTimer(pu,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		}
		buffer_free(bnotify);
	}
}
int app_handle_giveup(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int did;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	if(u->sid>=3) return -1;
	did = u->did;
	pu = getPeerUser(u);
	if(u==NULL) return -1;
	if(getDeskStatus(did)==DESK_PLAYING
			|| getDeskStatus(did)==DESK_DIANMU) {



		buffer* bnotify = buffer_init();
		buffer* b = buffer_init();
		int winner=1;
		buffer_append_string(bnotify,"notify:game_end,");
		if(u->sid==1)
			winner=2;
		buffer_append_long(bnotify,winner);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,0);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,0);
		buffer_append_string(bnotify,",");

		/* save the record before free the game */
		void* game = getGame(u->did);
		buffer* bRecord = buffer_init();
		if(game!=NULL) getRecord(game,bRecord);
		endGoGame(srv,u->did,u->puid,u->id,bRecord);
		/* append rank */
		rank_map_t* rmap=getRankMap(u->rank);
		buffer_append_long(bnotify,u->id);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,rmap->label);
		buffer_append_string(bnotify,",");
		rmap=getRankMap(pu->rank);
		buffer_append_long(bnotify,pu->id);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,rmap->label);
		buffer_append_string(bnotify,",");

		/* append match record */
		buffer_append_string(bnotify,bRecord->ptr);
		buffer_free(bRecord);

		/*end*/

		buffer* bsend = bresponse;
		buffer_append_string(bsend,bnotify->ptr);


		buffer_append_string(b,"notify:giveup,");
		buffer_append_long(b,u->sid);
		buffer_append_string(b,",\r\n\r\n");
		buffer_append_string(b,bnotify->ptr);
		if(pu!=NULL) notify_user(srv,pu,b);

		//notify observer
		{
			DESK_T* desk = getDesk(u->did);
			if(desk->observer_num>0) {
				for(int k=0; k<MAX_OBSERVER; k++) {
					USER_T* uu=desk->observers[k];
					if(uu!=NULL)
						notify_user(srv,uu,bnotify);
				}
			}
		}

		setDeskStatus(did,DESK_FULL);
		setUserStatus(u,USER_JOIN);
		clearTimer(u,TIMER_TYPE_STEP);
		clearTimer(u,TIMER_TYPE_DIANMU);
		startWaitTimer(srv,u,MAX_JOIN_TIMEOUT);
		setTimer(u,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		if(pu!=NULL) {
			clearTimer(pu,TIMER_TYPE_STEP);
			clearTimer(pu,TIMER_TYPE_DIANMU);
			setUserStatus(pu,USER_JOIN);
			startWaitTimer(srv,pu,MAX_JOIN_TIMEOUT);
			setTimer(pu,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		}
		buffer_free(b);
		buffer_free(bnotify);
	} else {
		//not allowed in this status
		buffer* bsend = bresponse;
		buffer_append_string(bsend,"response:giveup,403,");
	}
}
int endGoGame(void* srv,int did,int winner_uid,int loser_uid,buffer* bRecord)
{
	void* game = getGame(did);
	time_t cur_ts = time(NULL);
	if(game==NULL) return -1;
	time_t start_ts = getGameStartTime(game);
	USER_T* u = getUser(winner_uid);
	long duration = cur_ts - start_ts;
	int stepNum = getStepNum(game);
	dprintf("USER endGoGame duration=%d,stepNum=%d\n",duration,stepNum);
	if(cur_ts - start_ts >= MIN_GAME_TIME && getStepNum(game)>30){
	// if(1){
		dprintf("USER endGoGame win=%d,los=%d\n",winner_uid,loser_uid);
		goParam.gameNum++;
		//if winner in offline,
		if(u!=NULL){
				clearTimer(u,TIMER_TYPE_STEP);
				clearTimer(u,TIMER_TYPE_DIANMU);
		}
	
		USER_T* u2 = getUser(loser_uid);
		if(u2!=NULL){
				clearTimer(u2,TIMER_TYPE_STEP);
				clearTimer(u2,TIMER_TYPE_DIANMU);
		}
		if(u!=NULL && u->conn_ndx!=-1) {
			userSetRank(winner_uid,loser_uid);
			addMatchRecord(winner_uid,loser_uid,bRecord);

			
			/*
			   userEndGame(winner_uid,1);
			   userEndGame(loser_uid,0);
			 */
		}
	}
	publish_live_match_end(srv,did,winner_uid,loser_uid,bRecord);
	logicgo_free(game);
	setGame(did,NULL);


}

void calDianMu(int did,buffer* b)
{
	buffer* result=buffer_init();
	void* game = getGame(did);
	int bmu,wmu,isOK;
	bmu=wmu=0;
	isOK = getDianMuResult(game,&bmu,&wmu,result);
	//tell both
	buffer_append_long(b,isOK);
	buffer_append_string(b,",");
	buffer_append_long(b,bmu);
	buffer_append_string(b,",");
	buffer_append_long(b,wmu);
	buffer_append_string(b,",");
	buffer_append_string(b,result->ptr);
	buffer_free(result);
}

void enterDianMu(int did,buffer* b)
{
	buffer* result=buffer_init();
	void* game = getGame(did);
	int bmu,wmu,isOK;
	bmu=wmu=0;
	isOK = getDianMuResult(game,&bmu,&wmu,result);
	//tell both
	buffer_append_string(b,"notify:start_dianmu,");
	buffer_append_long(b,isOK);
	buffer_append_string(b,",");
	buffer_append_long(b,bmu);
	buffer_append_string(b,",");
	buffer_append_long(b,wmu);
	buffer_append_string(b,",");
	buffer_append_string(b,result->ptr);
	buffer_free(result);
}

int app_handle_done(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int x,y;
	void *game;
	u = getUserByConn(umsg->ndx);
	USER_T* me = u;
	if(u==NULL) return -1;
	pu = getPeerUser(u);
	game = getGame(u->did);
	if(game==NULL || pu == NULL) {
		return -1;
	}
	if(getDeskStatus(u->did)!=DESK_DIANMU || u->sid>=3) {
		return -1;
	}
	debug_log("s","enter");
	if(getDeskStatus(u->did)==DESK_DIANMU) {
		buffer* bsend = bresponse;
		u->isdone=1;
		//both done,
		buffer* bnotify = buffer_init();
		clearTimer(u,TIMER_TYPE_DIANMU);
		if(pu->isdone==1) {
			u->isdone=0;
			pu->isdone=0;
			//Game is ended here
			int bmu,wmu,b1,w1;
			bmu=wmu=0;
			b1=w1=0;
			buffer* bbb = buffer_init();

			getDianMuResult2(game,&b1,&w1,&bmu,&wmu,bbb);
			buffer_free(bbb);
			//getGoGameResult(game,&bmu,&wmu);
			float bb,ww;
			bb=bmu-180.5f-3.75f;
			ww=wmu;
			int winner_uid,loser_uid;
			int winner;
			if(bb>0) {
				winner=1;
				if(me->sid==1) {
					winner_uid = me->id;
					loser_uid = pu->id;
				} else {
					winner_uid = pu->id;
					loser_uid = me->id;
				}
			} else {
				winner=2;
				if(me->sid==2) {
					winner_uid = me->id;
					loser_uid = pu->id;
				} else {
					winner_uid = pu->id;
					loser_uid = me->id;
				}
			}
			buffer_append_string(bnotify,"notify:game_end,");
			buffer_append_long(bnotify,winner);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,bmu);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,wmu);
			buffer_append_string(bnotify,",");

			/* save the record before free the game */
			buffer* bRecord = buffer_init();
			if(game!=NULL) getRecord(game,bRecord);

			/* update the database and free the game */
			endGoGame(srv,me->did,winner_uid,loser_uid,bRecord);

			/* append rank */
			rank_map_t* rmap=getRankMap(me->rank);
			buffer_append_long(bnotify,me->id);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,rmap->label);
			buffer_append_string(bnotify,",");
			rmap=getRankMap(pu->rank);
			buffer_append_long(bnotify,pu->id);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,rmap->label);
			buffer_append_string(bnotify,",");

			/* append match record */
			buffer_append_string(bnotify,bRecord->ptr);
			buffer_free(bRecord);
			/*end*/

			buffer_append_string(bsend,bnotify->ptr);
			notify_user(srv,pu,bnotify);

			setDeskStatus(me->did,DESK_FULL);
			setUserStatus(me,USER_JOIN);
			setUserStatus(pu,USER_JOIN);
			startWaitTimer(srv,me,MAX_JOIN_TIMEOUT);
			setTimer(me,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
			startWaitTimer(srv,pu,MAX_JOIN_TIMEOUT);
			setTimer(pu,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
			//notify observer
			{
				DESK_T* desk = getDesk(u->did);
				if(desk->observer_num>0) {
					for(int k=0; k<MAX_OBSERVER; k++) {
						USER_T* uu=desk->observers[k];
						if(uu!=NULL)
							notify_user(srv,uu,bnotify);
					}
				}
			}
		} else {
			setTimer(pu,time(NULL),MAX_DIANMU_TIMEOUT,TIMER_TYPE_DIANMU);
			//notify:done,
			buffer_append_string(bsend,"notify:done,");
			buffer_append_long(bsend,u->sid);

			//notify opponent
			buffer_append_string(bnotify,"notify:done,");
			buffer_append_long(bnotify,u->sid);
			notify_user(srv,pu,bnotify);

			//notify observer
			{
				DESK_T* desk = getDesk(u->did);
				if(desk->observer_num>0) {
					for(int k=0; k<MAX_OBSERVER; k++) {
						USER_T* uu=desk->observers[k];
						if(uu!=NULL)
							notify_user(srv,uu,bnotify);
					}
				}
			}
			buffer_free(bnotify);
		}
	}
}

int app_handle_set_step_time(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int x,y;
	void *game;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	pu = getPeerUser(u);





    DESK_T* desk = getDesk(u->did);

    if(desk==NULL) return -1;

    	//step time can not set here
	if(pu!=NULL){

		buffer_append_string(bresponse,"notify:set_step_time,");
		buffer_append_long(bresponse,u->did);
		buffer_append_string(bresponse,",");
		buffer_append_long(bresponse,getDeskStepTimeOut(u->did));
		buffer_append_string(bresponse,",");
		return 0;


	}



    data_string *minutes= (data_string *)array_get_element(
            umsg->headers,"minutes");

    int min=atoi(minutes->value->ptr);

    if(getDeskStatus(u->did)==DESK_PLAYING){
    	return -1;
    }


    if(min>=1 && min<=3){

    	desk->step_time_out = min*60;
    }else{
    	return -1;
    }

    
    buffer* bsend = buffer_init();

	buffer_append_string(bsend,"notify:set_step_time,");
	buffer_append_long(bsend,u->did);
	buffer_append_string(bsend,",");
	buffer_append_long(bsend,min*60);
	buffer_append_string(bsend,",");
	broadcastToAll(srv,bsend,NULL);
	buffer_free(bsend);

}

int app_handle_undo_dead(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int x,y;
	void *game;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	pu = getPeerUser(u);
	game = getGame(u->did);
	if(game==NULL || pu == NULL) {
		return -1;
	}
	if(getDeskStatus(u->did)!=DESK_DIANMU || u->sid>=3) {
		return -1;
	}
	debug_log("s","enter");
	if(getDeskStatus(u->did)==DESK_DIANMU) {
		buffer* b = buffer_init();
		int num = undoDead(game,u->sid,b);
		if(num>=0) {
			buffer* bnotify = buffer_init();
			buffer* bsend = bresponse;
			//notify:setdead,x,y,
			buffer_append_string(bsend,"notify:undodead,");
			buffer_append_long(bsend,u->sid);
			buffer_append_string(bsend,",");
			buffer_append_long(bsend,num);
			buffer_append_string(bsend,",");
			buffer_append_string(bsend,b->ptr);

			//notify opponent
			buffer_append_string(bnotify,"notify:undodead,");
			buffer_append_long(bnotify,u->sid);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,num);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,b->ptr);
			notify_user(srv,pu,bnotify);

			//notify observer
			{
				DESK_T* desk = getDesk(u->did);
				if(desk->observer_num>0) {
					for(int k=0; k<MAX_OBSERVER; k++) {
						USER_T* uu=desk->observers[k];
						if(uu!=NULL)
							notify_user(srv,uu,bnotify);
					}
				}
			}
			buffer_free(bnotify);
		}
		buffer_free(b);

	}
}
int app_handle_set_dead(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int x,y;
	void *game;
	data_string *sx= (data_string *)array_get_element(
			umsg->headers,"x");
	data_string *sy= (data_string *)array_get_element(
			umsg->headers,"y");
	buffer* bsend = bresponse;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	pu = getPeerUser(u);
	x=atoi(sx->value->ptr);
	y=atoi(sy->value->ptr);
	game = getGame(u->did);
	if(game==NULL || pu == NULL ||
			x < 0 || x>=19 || y<0 || y>=19) {
		return -1;
	}
	if(getDeskStatus(u->did)!=DESK_DIANMU || u->sid>=3) {
		return -1;
	}


	debug_log("s","enter");
	if(getDeskStatus(u->did)==DESK_DIANMU) {
		if(setDead(game,x,y,u->sid)>0) {
			buffer* b = buffer_init();
			buffer* bnotify = buffer_init();
			//notify:setdead,x,y,
			buffer_append_long(b,u->sid);
			buffer_append_string(b,",");
			buffer_append_long(b,x);
			buffer_append_string(b,",");
			buffer_append_long(b,y);
			buffer_append_string(b,",");
			buffer_append_string(bsend,"notify:setdead,");
			buffer_append_string(bsend,b->ptr);

			//notify opponent
			buffer_append_string(bnotify,"notify:setdead,");
			buffer_append_string(bnotify,b->ptr);
			notify_user(srv,pu,bnotify);

			//notify observer
			{
				DESK_T* desk = getDesk(u->did);
				if(desk->observer_num>0) {
					for(int k=0; k<MAX_OBSERVER; k++) {
						USER_T* uu=desk->observers[k];
						if(uu!=NULL)
							notify_user(srv,uu,bnotify);
					}
				}
			}
			buffer_free(bnotify);
			buffer_free(b);
		} else {
			return -1;
		}
	}
}
int app_handle_broadcast(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int x,y;
	void *game;
	data_string *scontent= (data_string *)array_get_element(
			umsg->headers,"content");
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	if(u->isadmin==0) return -1;
	buffer* b = buffer_init();
	buffer_append_string(b,"notify:broadcast,");
	int len = MAX_NAME_LEN;
	char name[MAX_NAME_LEN+1];
	for(int i = 0; i<len ; i++) {
		name[i] = u->email->ptr[i];
		if (name[i]==0) break;
		if (name[i]=='@') {
			name[i]=0;
			break;
		}
	}
	name[len]=0;
	buffer_append_string(b,name);
	buffer_append_string(b,":");
	buffer_append_string(b,scontent->value->ptr);
	buffer_append_string(b,"]");
	broadcastToAll(srv,b,NULL);
	buffer_free(b);
}

int app_handle_reject_invite(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int uid;
	data_string *suid= (data_string *)array_get_element(
			umsg->headers,"uid");
	buffer* bsend = bresponse;
	u = getUserByConn(umsg->ndx);
	buffer* bnotify = NULL;
	if(u==NULL) {
		ret = -1;
		goto _error1;
	}
	uid=atoi(suid->value->ptr);
	pu = getUser(uid);
	if(pu==NULL) {
		ret = -2;
		goto _error1;
	}

	bnotify = buffer_init();
	//if the peer use is not in wait someone's invite
	if(pu->is_wait_accept==-1) {
		ret=-4;
		goto _error1;
	}
	//If the peer user is wait just this one people, done
	else if(pu->is_wait_accept==u->id) {
		pu->is_wait_accept=-1;
		u->is_wait_accept=-1;
		pu->invite_id=-1;
		buffer_append_string(bnotify,"notify:reject_invite,");
		buffer_append_long(bnotify,u->id);
		buffer_append_string(bnotify,",");
		notify_user(srv,pu,bnotify);
	}
	buffer_append_string(bsend,"response:reject_invite,ok,");

	buffer_free(bnotify);
	return 0;
_error1:
	if(bnotify!=NULL) {
		buffer_free(bnotify);
	}
	buffer_append_string(bsend,"response:reject_invite,error,");
	return ret;
}

int app_handle_accept_invite(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int uid;
	data_string *suid= (data_string *)array_get_element(
			umsg->headers,"uid");
	buffer* bsend = bresponse;
	u = getUserByConn(umsg->ndx);
	buffer* bnotify=NULL;
	if(u==NULL) {
		ret = -1;
		goto _error1;
	}
	uid=atoi(suid->value->ptr);
	pu = getUser(uid);
	if(pu==NULL) {
		ret = -2;
		goto _error1;
	}
	if(isInGame(pu)) {
		return -3;
		goto _error1;
	}
	bnotify = buffer_init();

	//if the peer use is not in wait someone's invite
	if(pu->is_wait_accept==-1) {
		ret=-4;
		goto _error1;
	}
	//If the peer user is wait just this one people, done
	else if(pu->is_wait_accept==u->id) {
		pu->is_wait_accept=-1;
		u->is_wait_accept=-1;
		u->invite_id=pu->id;
		pu->invite_id=u->id;
		int did = find_free_desk();
		if(did==-1) {
			ret = -4;
			goto _error1;
		} else {
			buffer_append_string(bnotify,"notify:invite_done,");
			char name[MAX_NAME_LEN+1];
			getName(u,name);
			rank_map_t* rmap=NULL;
			rmap=getRankMap(u->rank);
			buffer_append_long(bnotify,u->id);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,rmap->label);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,u->wins);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,u->loses);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,name);
			buffer_append_string(bnotify,",");

			getName(pu,name);
			rmap=getRankMap(pu->rank);
			buffer_append_long(bnotify,pu->id);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,rmap->label);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,pu->wins);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,pu->loses);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,name);
			buffer_append_string(bnotify,",");
			buffer_append_string(bsend,bnotify->ptr);
			notify_user(srv,pu,bnotify);
		}
	}

	buffer_free(bnotify);
	return 0;
_error1:
	if(bnotify!=NULL) {
		buffer_free(bnotify);
	}
	buffer_append_string(bsend,"response:accept_invite,error,");
	return ret;
}

int app_handle_invite(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int uid;
	data_string *suid= (data_string *)array_get_element(
			umsg->headers,"uid");
	buffer* bsend = bresponse;
	u = getUserByConn(umsg->ndx);
	buffer* bnotify = NULL;
	if(u==NULL) {
		ret = -1;
		goto _error1;
	}
	uid=atoi(suid->value->ptr);
	pu = getUser(uid);
	if(pu==NULL) {
		ret = -2;
		goto _error1;
	}
	if(isInGame(pu)) {
		return -3;
		goto _error1;
	}

	bnotify = buffer_init();
	//if the peer use is not in wait someone's invite
	if(pu->is_wait_accept==-1) {
		buffer_append_string(bnotify,"notify:invite,");
		char name[MAX_NAME_LEN+1];
		getName(u,name);
		rank_map_t* rmap=NULL;
		rmap=getRankMap(u->rank);
		buffer_append_long(bnotify,u->id);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,rmap->label);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,u->wins);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,u->loses);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,name);
		buffer_append_string(bnotify,",");
		notify_user(srv,pu,bnotify);
		u->is_wait_accept=pu->id;
		buffer_append_string(bsend,"response:invite,wait,");
	}
	//If the peer user is wait just this one people, done
	else if(pu->is_wait_accept==u->id) {
		pu->is_wait_accept=-1;
		u->is_wait_accept=-1;
		u->invite_id=pu->id;
		pu->invite_id=u->id;
		int did = find_free_desk();
		if(did==-1) {
			ret = -4;
			goto _error1;
		} else {
			buffer_append_string(bnotify,"notify:invite_done,");
			char name[MAX_NAME_LEN+1];
			getName(u,name);
			rank_map_t* rmap=NULL;
			rmap=getRankMap(u->rank);
			buffer_append_long(bnotify,u->id);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,rmap->label);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,u->wins);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,u->loses);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,name);
			buffer_append_string(bnotify,",");

			getName(pu,name);
			rmap=getRankMap(pu->rank);
			buffer_append_long(bnotify,pu->id);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,rmap->label);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,pu->wins);
			buffer_append_string(bnotify,",");
			buffer_append_long(bnotify,pu->loses);
			buffer_append_string(bnotify,",");
			buffer_append_string(bnotify,name);
			buffer_append_string(bnotify,",");
			buffer_append_string(bsend,bnotify->ptr);
			notify_user(srv,pu,bnotify);

		}
	}
	buffer_free(bnotify);

	return 0;
_error1:
	if(bnotify!=NULL) {
		buffer_free(bnotify);
	}
	buffer_append_string(bsend,"response:invite,error,");
	return ret;
}
int app_handle_step(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *pu,*u;
	int x,y;
	void *game;
	data_string *sx= (data_string *)array_get_element(
			umsg->headers,"x");
	data_string *sy= (data_string *)array_get_element(
			umsg->headers,"y");
	buffer* bsend = bresponse;
	u = getUserByConn(umsg->ndx);
	if(u==NULL) return -1;
	pu = getPeerUser(u);
	x=atoi(sx->value->ptr);
	y=atoi(sy->value->ptr);
	game = getGame(u->did);
	if(game==NULL || pu == NULL || u->sid>=3 ||
			x < 0 || x>=19 || y<0 || y>=19) {
		buffer_append_string(bsend,"notify:error,reset,");
		return -1;
	}

	buffer* b = buffer_init();
	buffer* bkill = buffer_init();
	buffer* bnotify = buffer_init();
	u->isdone=0;
	pu->isdone=0;

	if(getDeskStatus(u->did)==DESK_PLAYING) {
		if(getCurTurn(game)==u->sid) {
			debug_log("s","enter");
			if(stepTo(game,x,y)==1) {
				clearTimer(u,TIMER_TYPE_STEP);
				u->ispass=0;
				stopWaitTimer(srv,u);
				int next = getCurTurn(game);
				//response:step,200,x,y,killed_len,killed...
				buffer_append_long(b,x);
				buffer_append_string(b,",");
				buffer_append_long(b,y);
				buffer_append_string(b,",");
				int num = getKilled(game,bkill);
				buffer_append_long(b,num);
				buffer_append_string(b,",");
				if(bkill->used>0) {
					buffer_append_string(b,bkill->ptr);
				}
				buffer_append_string(bsend,"response:step,200,");
				buffer_append_string(bsend,b->ptr);


				

				//add next side notify
				//notify:next,sid
				buffer_append_string(bsend,"\r\n\r\nnotify:next,");
				buffer_append_long(bsend,next);
				buffer_append_string(bsend,",");
				buffer_append_long(bsend,getMaxStepTimeout(u->did));
				buffer_append_string(bsend,",");

				//populate the step notify to peer side
				//notify:setp,sid,x,y,killed_len,killled...
				buffer_append_string(bnotify,"notify:step,");
				buffer_append_long(bnotify,u->sid);
				buffer_append_string(bnotify,",");
				buffer_append_string(bnotify,b->ptr);
				buffer_append_string(bnotify,
						"\r\n\r\nnotify:next,");
				buffer_append_long(bnotify,next);
				buffer_append_string(bnotify,",");
				buffer_append_long(bnotify,getMaxStepTimeout(u->did));
				buffer_append_string(bnotify,",");
				notify_user(srv,pu,bnotify);
				//notify observer


				{
					USER_T** obs = NULL;
					DESK_T* desk = getDesk(u->did);
					if(desk->observer_num>0) {
						obs = (USER_T**)malloc((desk->observer_num+1)*sizeof(USER_T*));
						int i=0;
						for(int k=0; k<MAX_OBSERVER; k++) {
							USER_T* uu=desk->observers[k];
							if(uu!=NULL){
								obs[i]=uu;
								i++;
								notify_user(srv,uu,bnotify);
							}
						}
						obs[desk->observer_num]=NULL;
					}


					{
						USER_T *bu,*wu;
 						if(u!=NULL){
 							if(u->sid==1){

 								bu = u; 
 								wu = pu;
 							}else{
 								bu=pu;
 								wu = u;
 							}
 						}
						buffer* bRecord = buffer_init();
						if(game!=NULL) getRecord(game,bRecord);
						publish_live_match_play(srv,u->did,u->sid,x,y,num,bkill,bRecord,bu,wu,obs);
						buffer_free(bRecord);
					}

					if(obs!=NULL) free(obs);
				}


				startWaitTimer(srv,pu,getMaxStepTimeout(pu->did));
				setTimer(pu,time(NULL),getMaxStepTimeout(pu->did),TIMER_TYPE_STEP);
				setUserStatus(u,USER_STEP);
			}
		}
	}
	buffer_free(bkill);
	buffer_free(bnotify);
	buffer_free(b);

	return ret;
}
int startWaitTimer(void* srv,USER_T* user,int timeout)
{
	if (user->conn_ndx!=-1) {
		debug_log("sd","enter=","timeout");
		user->wait_action_ts = time(NULL);
		user->wait_action_idle = timeout;
	}
}
int stopWaitTimer(void* srv,USER_T* user)
{
	debug_log("s","enter");
	if (user->conn_ndx!=-1) {
		user->wait_action_idle = -1;
	}
}
int app_handle_timeout(void* srv,int uid,char type){
	USER_T *u = getUser(uid);
	if(u==NULL) return -1;
	USER_T *pu = getPeerUser(u);
	debug_log("%s","enter");
	buffer* bnotify = buffer_init();
	//处理单步,断线，点目超时统一判负
	if((type==TIMER_TYPE_STEP || type==TIMER_TYPE_RESUME || type==TIMER_TYPE_DIANMU) && isInGame(u)) {
		int winner = 1;
		buffer_append_string(bnotify,"notify:timeout,step,");
		buffer_append_long(bnotify,u->sid);
		buffer_append_string(bnotify,",\r\n\r\n");
		buffer_append_string(bnotify,"notify:game_end,");
		if(u->sid==1)
			winner=2;
		buffer_append_long(bnotify,winner);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,0);
		buffer_append_string(bnotify,",");
		buffer_append_long(bnotify,0);
		buffer_append_string(bnotify,",");
		if(u!=NULL) {
			setUserStatus(u,USER_JOIN);
			startWaitTimer(srv,u,MAX_JOIN_TIMEOUT);
			setTimer(u,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		}
		if(pu!=NULL) {
			setUserStatus(pu,USER_JOIN);
			startWaitTimer(srv,pu,MAX_JOIN_TIMEOUT);
			setTimer(pu,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		}



		/* save the record before free the game */
		void* game = getGame(u->did);
		buffer* bRecord = buffer_init();
		if(game!=NULL){
			 dprintf("FIXME: game is null when timeout\n");
			 getRecord(game,bRecord);
		}

		endGoGame(srv,u->did,u->puid,u->id,bRecord);
		/* append rank */
		rank_map_t* rmap=getRankMap(u->rank);
		buffer_append_long(bnotify,u->id);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,rmap->label);
		buffer_append_string(bnotify,",");
		rmap=getRankMap(pu->rank);
		buffer_append_long(bnotify,pu->id);
		buffer_append_string(bnotify,",");
		buffer_append_string(bnotify,rmap->label);
		buffer_append_string(bnotify,",");

		/* append match record */
		buffer_append_string(bnotify,bRecord->ptr);
		buffer_free(bRecord);

		/*end*/
		notify_user(srv,u,bnotify);
		notify_user(srv,pu,bnotify);
		setDeskStatus(u->did,DESK_FULL);
	} else if (type==TIMER_TYPE_JOIN) {
		if(strstr(u->email,"robot-")>0){
                   clearTimer(u,TIMER_TYPE_JOIN);
		}else{ 
                   app_handle_leave(srv,u);
		   buffer_append_string(bnotify,"notify:timeout,join,");
		   buffer_append_long(bnotify,u->sid);
		   buffer_append_string(bnotify,",\r\n\r\n");
		   notify_user(srv,u,bnotify);
		}

                
	} else if (type==TIMER_TYPE_RESUME) {
		clearAllTimer(u);
		if(isInDesk(u)==1) {
			app_handle_leave(srv,u);
		}
		removeUser(u);
	}
	//FYI 不再对点目单独处理
#if(0)
	else if (type==TIMER_TYPE_DIANMU) {
		void* game = getGame(u->did);
		int next = getCurTurn(game);
		buffer_append_string(bnotify,"notify:timeout,dianmu,");
		buffer_append_long(bnotify,u->sid);
		buffer_append_string(bnotify,",\r\n\r\n");
		buffer_append_string(bnotify,"notify:next,");
		buffer_append_long(bnotify,next);
		buffer_append_string(bnotify,",\r\n\r\n");
		notify_user(srv,u,bnotify);
		notify_user(srv,pu,bnotify);
		if(u->sid==next) {
			stopWaitTimer(srv,pu);
			startWaitTimer(srv,u,MAX_STEP_TIMEOUT);
			setTimer(u,time(NULL),getMaxStepTimeout(u->did),TIMER_TYPE_STEP);
		} else {
			stopWaitTimer(srv,u);
			startWaitTimer(srv,pu,MAX_STEP_TIMEOUT);
			setTimer(pu,time(NULL),getMaxStepTimeout(pu->did),TIMER_TYPE_STEP);
		}
		if(pu!=NULL) {
			setUserStatus(pu,USER_STEP);
		}
		if(u!=NULL) {
			setUserStatus(u,USER_STEP);
		}
		setDeskStatus(u->did,DESK_PLAYING);
	}
#endif
	buffer_free(bnotify);
}
int app_handle_ready(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	USER_T *u = getUserByConn(umsg->ndx);

	if(u==NULL) return -1;
	if (u->sid==0 || u->did ==0) {
		ret = -1;
	} else {
		buffer *bb;
		USER_T* pu = getUserByConn(u);
		bb = bresponse;
		/*
		if(u->coin<GAME_COST){
			buffer_append_string(bb,"response:ready,401,");
			return -1;
		}	
		*/
		/*
		if(u->payTo<time(NULL)){
			buffer_append_string(bb,"response:needpay,401,");
			return -1;
		}
		*/
		
		if(u->status!=USER_JOIN) {
			buffer_append_string(bb,"response:ready,403,");
			return -1;
		}
		setUserStatus(u,USER_READY);
		buffer_append_string(bb,"response:ready,200,");
		pu = getPeerUser(u);
		//notify:ready,uid,name,wins/totals
		clearTimer(u,TIMER_TYPE_JOIN);
		if(pu!=NULL) {
			debug_log("ss","pu.name=",pu->email->ptr);
			char name[MAX_NAME_LEN];
			int wins,totals,status;
			buffer* b = buffer_init();
			rank_map_t* rmap=NULL;
			rmap=getRankMap(u->rank);
			buffer_append_string(b,"notify:ready");
			buffer_append_string(b,",");
			buffer_append_long(b,u->id);
			buffer_append_string(b,",");
			getUserInfo(u,name,MAX_NAME_LEN,
					&wins,&totals,&status);
			buffer_append_long(b,status);
			buffer_append_string(b,",");
			buffer_append_string(b,rmap->label);
			buffer_append_string(b,"|");
			buffer_append_string(b,name);
			buffer_append_string(b,",");
			buffer_append_long(b,wins);
			buffer_append_string(b,"/");
			buffer_append_long(b,totals);
			buffer_append_string(b,",");
			if(pu->status == USER_READY) {
				setDeskStatus(u->did,DESK_READY);
				clearAllTimer(pu);
				clearAllTimer(u);
				startGoGame(srv,u->did,umsg);


				buffer_append_string(bb,"\r\n\r\nnotify:start,");
				buffer_append_long(bb,u->did);
				buffer_append_string(bb,",");
				buffer_append_long(bb,1);
				buffer_append_string(bb,",");
				buffer_append_long(bb,getMaxStepTimeout(u->did));
				buffer_append_string(bb,",");

				buffer_append_string(b,"\r\n\r\nnotify:start,");
				buffer_append_long(b,u->did);
				buffer_append_string(b,",");
				buffer_append_long(b,1);
				buffer_append_string(b,",");
				buffer_append_long(b,getMaxStepTimeout(u->did));
				buffer_append_string(b,",");
				u->puid=pu->id;
				pu->puid=u->id;
				clearTimer(pu,TIMER_TYPE_JOIN);
				if(u->sid==1){
					startWaitTimer(srv,u,MAX_STEP_TIMEOUT);
					setTimer(u,time(NULL),getMaxStepTimeout(u->did),TIMER_TYPE_STEP);
				}
				else{
					startWaitTimer(srv,pu,MAX_STEP_TIMEOUT);
					setTimer(pu,time(NULL),getMaxStepTimeout(pu->did),TIMER_TYPE_STEP);
				}
				setUserStatus(pu,USER_STEP);
				setUserStatus(u,USER_STEP);
				//notify observer
				{
					buffer* ob = buffer_init();
					DESK_T* desk = getDesk(u->did);

					USER_T** obs = NULL;

					

					buffer_append_string(ob,
							"notify:observe_start,");
					buffer_append_long(ob,u->did);
					buffer_append_string(ob,",0,");


					if(desk->observer_num>0) {
						int ii=0;
						obs = (USER_T**)malloc((desk->observer_num+1)*sizeof(USER_T*));

						for(int k=0; k<MAX_OBSERVER; k++) {
							USER_T *ou=desk->observers[k];


							if(ou!=NULL){
								
								if(ii<desk->observer_num)
									obs[ii]=ou;
								ii++;
								notify_user(srv,ou,ob);
							}else if(ou!=NULL){
								app_handle_leave(srv,ou);
							}
						}
						obs[desk->observer_num]=0;
					}

					publish_live_match_start(srv,u->did,desk->bu,desk->wu,obs);

					if(obs!=NULL)
						free(obs);



					buffer_free(ob);
				}
			}else{
				setTimer(u,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
			}
			notify_user(srv,pu,b);
			buffer_free(b);
		}else{
			setTimer(u,time(NULL),MAX_JOIN_TIMEOUT,TIMER_TYPE_JOIN);
		}
	}
}

int app_handle_logout(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret;
	return 0;
}
int app_handle_leave(void* srv,USER_T* who)
{
	int ret=0;
	USER_T* pu = getPeerUser(who);
	if(who == NULL) return -1;
	if(who->sid>=3) { //means a observer
		leaveObserver(who);
		setUserStatus(who,USER_LOGIN);
		return 0;
	}
#ifdef GO_DEBUG_ON
	printf("%s: ENTER name=%s\n",__func__,who->email->ptr);
#endif
	buffer* b = buffer_init();
	buffer* bb = buffer_init();


	if(isInDesk(who)==1) {
		clearTimer(who,TIMER_TYPE_JOIN);
#ifdef GO_DEBUG_ON
		printf("%s:%s\n",__func__,"inDesk");
#endif
		buffer_append_string(bb,"notify:leave,");
		buffer_append_long(bb,who->id);
		buffer_append_string(bb,",");
		buffer_append_string(b,bb->ptr);
		if(isInGame(who)==1) {
			int winner=1;
			if(who->sid==1) winner = 2;
#ifdef GO_DEBUG_ON
			printf("%s:%s\n",__func__,"inGame");
#endif
			buffer_append_string(b,"\r\n\r\nnotify:game_end,");
			buffer_append_long(b,winner);
			buffer_append_string(b,",");
			buffer_append_long(b,0);
			buffer_append_string(b,",");
			buffer_append_long(b,0);
			buffer_append_string(b,",");

			/* save the record before free the game */
			void* game = getGame(who->did);
			buffer* bRecord = buffer_init();
			if(game!=NULL) getRecord(game,bRecord);

			endGoGame(srv,who->did,who->puid,who->id,bRecord);
			/* append rank */
			rank_map_t* rmap=getRankMap(who->rank);
			buffer_append_long(b,who->id);
			buffer_append_string(b,",");
			buffer_append_string(b,rmap->label);
			buffer_append_string(b,",");

			if(pu!=NULL) {
				setUserStatus(pu,USER_JOIN);
				rmap=getRankMap(pu->rank);
				buffer_append_long(b,pu->id);
				buffer_append_string(b,",");
				buffer_append_string(b,rmap->label);
				buffer_append_string(b,",");
			} else {
				buffer_append_long(b,who->puid);
				buffer_append_string(b,",");
				buffer_append_string(b,"NR");
				buffer_append_string(b,",");
			}

			/* append match record */
			buffer_append_string(b,bRecord->ptr);
			buffer_free(bRecord);
			/* END */
		}
		if(pu!=NULL) {
			notify_user(srv,pu,b);
		}

		{
			DESK_T* desk = getDesk(who->did);
			if(desk==NULL) return 0;
			if(desk->observer_num>0) {
				for(int k=0; k<MAX_OBSERVER; k++) {
					USER_T* ou=desk->observers[k];
					if(ou!=NULL) notify_user(srv,ou,b);
				}
			}
		}
	}
	if(goParam.isBroadcast==1) {
		if(isInDesk(who)==1) broadcastToRoom(srv,bb,NULL);
	}
	if(isInDesk(who)==1) {
		int did=who->did;
		DESK_T* desk = getDesk(who->did);


		leaveDesk(who);
		setUserStatus(who,USER_LOGIN);

		if(desk!=NULL && desk->bu==NULL && desk->wu==NULL){

			if(desk->step_time_out!=3*60){
				desk->step_time_out=3*60;
				buffer* bbc = buffer_init();
				buffer_append_string(bbc,"notify:set_step_time,");
				buffer_append_long(bbc,did);
				buffer_append_string(bbc,",180,");
				broadcastToAll(srv,bbc,NULL);
				buffer_free(bbc);
				

			}


		}

	}


	if(pu!=NULL) {
		setUserStatus(pu,USER_JOIN);
	}
	buffer_free(b);
	buffer_free(bb);
	return 0;
}

void waitMeBackShort(USER_T* me,int sec)
{
	void* srv = getServer();
	me->status = USER_WAIT_ME_BACK;
	setTimeout(&(me->tc),time(NULL),sec);
	
	
	setTimer(me,time(NULL),sec,TIMER_TYPE_RESUME);
#if 0
	printf("%s enter: sid=%d,uid=%d,name=%s\n",
			__func__,me->sid,me->id,me->email->ptr);
#endif
}

void waitMeBack(USER_T* me)
{
	void* srv = getServer();
	me->status = USER_WAIT_ME_BACK;
	setTimeout(&(me->tc),time(NULL),getWaitBackTime());
	setTimer(me,time(NULL),getWaitBackTime(),TIMER_TYPE_RESUME);
#if 0
	printf("%s enter: sid=%d,uid=%d,name=%s\n",
			__func__,me->sid,me->id,me->email->ptr);
#endif
}

int isInRoom(USER_T* me)
{
	if(me!=NULL) {
		int status = me->status;
		if(status>USER_IN_ROOM && status<USER_IN_DESK) {
			return 1;
		}
	}
	return 0;
}
int isInDesk(USER_T* me)
{
	if(me!=NULL) {
		int status = me->status;
		if(status>USER_IN_DESK && status<USER_OUT_DESK) {
			return 1;
		}
	}
	return 0;
}

int isInGame(USER_T* me)
{
	if(me!=NULL) {
		int status = me->status;
		if(status>USER_IN_GAME && status < USER_OUT_GAME) {
			return 1;
		}
	}
	return 0;
}

int app_handle_con_close(void* srv,int conn_ndx)
{
	int ret;
	USER_T* me = getUserByConn(conn_ndx);
	if(me!=NULL) {
		dprintf("%s@%d lost the connection",me->email->ptr,conn_ndx);
		if(isInGame(me)==1 && isEnableGoBack()==1) {
			me->noresponse=1;
			waitMeBack(me);
		}else{
			if(isInDesk(me)==1) {
				app_handle_leave(srv,me);
				removeUser(me);
			} else {
				removeUser(me);
			}
			clearAllTimer(me);
		}
		me->conn_ndx = -1;

	}
	return ret;
}

int app_handle_ls(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	return 0;
}


int app_handle_stat_room(void* srv,user_msg_t* umsg,buffer* bresponse){
	int ret = -1;
	
			int status;
			int dnum = getDeskNumber();
			buffer* b = buffer_init();
			buffer_append_string(b,"response:stat-room,{");

			buffer_append_string(b,"\"dnum\":");
			buffer_append_long(b,dnum);
			buffer_append_string(b,",");
			buffer_append_string(b,"\"desks\":[");
			for(int i = 0 ; i < dnum; i++) {

				if(i>0) buffer_append_string(b,",");

				buffer_append_string(b,"{");
				
					buffer_append_string(b,JSON_DID);
					buffer_append_long(b,(i+1));
					buffer_append_string(b,",");

					buffer_append_string(b,JSON_STEP_TIMEOUT);
					buffer_append_long(b,getDeskStepTimeOut(i+1));
				buffer_append_string(b,"}");
			}
				buffer_append_string(b,"]");
				buffer_append_string(b,"}");
			
			buffer_append_string(bresponse,b->ptr);
	buffer_free(b);
	return ret;	
}

/* type:[user|desk]
 * sid:
 * did:
 */

int app_handle_list(void* srv,user_msg_t* umsg,buffer* bresponse)
{
	int ret = -1;
	data_string *type= (data_string *)array_get_element(
			umsg->headers,"type");
	buffer* b = buffer_init();
	buffer* bb = NULL;
	if(type != NULL) {
		if(strcmp(type->value->ptr,"user") == 0) {
		} else if(strcmp(type->value->ptr,"desk" )== 0 ) {
			/* desk number;
			 * did,status,side_number,sid,uid,name,win/total,...;
			 */
			int status;
			int dnum = getDeskNumber();
			buffer_append_string(b,"response:list,200,");
			buffer_append_long(b,dnum);
			buffer_append_string(b,";");
			for(int i = 0 ; i < dnum ; i++) {
				buffer_append_long(b,i+1);
				buffer_append_string(b,",");
				status = getDeskStatus(i+1);
				buffer_append_long(b,status);
				buffer_append_string(b,",");
				if(status!=DESK_NULL) {
					char name[MAX_NAME_LEN];
					int wins,totals,status;
					int side_n = getSideNumber(i+1);
					int sides = getTotalSides();
					wins=totals=0;
					buffer_append_long(b,side_n);
					buffer_append_string(b,",");
					for(int j=1; j<=sides; j++) {
						USER_T* u = getSide(i+1,j);
						rank_map_t* rmap=NULL;
						if(u==NULL) continue;
						rmap=getRankMap(u->rank);
						buffer_append_long(b,j);
						buffer_append_string(b,",");
						buffer_append_long(b,u->id);
						buffer_append_string(b,",");
						getUserInfo(u,name,
								MAX_NAME_LEN,&wins,&totals,&status);
						buffer_append_long(b,status);
						buffer_append_string(b,",");
						if(rmap!=NULL)
							buffer_append_string(b,rmap->label);
						buffer_append_string(b,"|");
						buffer_append_string(b,name);
						buffer_append_string(b,",");
						buffer_append_long(b,wins);
						buffer_append_string(b,"/");
						buffer_append_long(b,totals);
						buffer_append_string(b,",");
						buffer_append_long(b,u->groupid);
						buffer_append_string(b,",");
						if(u->groupid!=0)
							buffer_append_string(b,u->groupname->ptr);
						else
							buffer_append_string(b,"X");
						buffer_append_string(b,",");
					}
				}
				buffer_append_string(b,";");
			}
			debug_log("s",b->ptr);
			bb = bresponse;
			buffer_append_string(bb,b->ptr);
		}
	}
	buffer_free(b);
	return ret;
}

extern void mgo_process_msg(void* msg);
int appgo_check_timer(void* srv)
{
	buffer* buff = buffer_init();
	//connection status report
	buffer_append_long(buff,0);
	buffer_append_string(buff,",");
	buffer_append_long(buff,0);
	buffer_append_string(buff,",timer,");
	req_msg_t* msg = calloc(1,sizeof(*msg));
	msg->hlen=strlen(buff->ptr);
	msg->header=buff->ptr;
	msg->dlen=0;
	msg->data=0;
	//req_msg_send(srv->req_publisher,msg);
	mgo_process_msg(msg);
	//free(msg);
	buffer_free_struct(buff);
}
static unsigned long tick_count=0;
int appgo_handle_timer(void* srv){
	struct list_head *pos;
	struct list_head *temp;
	time_t cur_ts = time(NULL);
	list_for_each_safe(pos,temp, &tc_list){
		struct tc_t * tc= list_entry(pos, struct tc_t, list);
		if(tc->duration>0 && cur_ts-tc->start>tc->duration && tc->remove==0){
			dprintf("timer[%s] is trigger for %d\n",debug_timer[tc->type],tc->uid);
			app_handle_timeout(srv,tc->uid,tc->type);
			tc->remove=1;
		}
	}
    pthread_mutex_lock(&tc_mutex);
	list_for_each_safe(pos,temp, &tc_list){
		struct tc_t * tc= list_entry(pos, struct tc_t, list);
		if(tc->remove==1){
			list_del(pos);
			free(tc);
		}
	}
    pthread_mutex_unlock(&tc_mutex);

	return 0;
}

int appgo_handle_request(void* srv,user_msg_t* umsg)
{
	buffer *l;
	data_string *ds;
	setServer(srv);
	ds = (data_string *)array_get_element(
			umsg->headers,"request");
	if ( NULL == ds) {
		return -1;
	}
	buffer *bresponse = buffer_init();
	if (strcmp(ds->value->ptr,"stat")==0) {
		app_handle_stat(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"ls")==0) {
		app_handle_ls(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"register")==0) {
		app_handle_register(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"login")==0) {
		app_handle_login(srv,umsg,bresponse);
	}else{
		USER_T* u = getUserByConn(umsg->ndx);
		if(u==NULL){
			buffer *b=bresponse;
			debug_log("sd","User must login into system firstly!",umsg->ndx);
			buffer_append_string(b,"notify:error,reset,");
			send_response(srv,umsg->ndx,b);
			buffer_free(bresponse);
			return -1;
		}
	}

	if (strcmp(ds->value->ptr,"reboot")==0) {
		app_handle_reboot(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"addadmin")==0) {
		app_handle_addadmin(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"judge")==0) {
		app_handle_judge(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"alarm")==0) {
		app_handle_alarm(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"join")==0) {
		app_handle_join(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"resume")==0) {
		app_handle_resume(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"observe")==0) {
		app_handle_observe(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"users")==0) {
		app_handle_users(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"list")==0) {
		app_handle_list(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"ready")==0) {
		app_handle_ready(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"dump")==0) {
		app_handle_dump(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"leave")==0) {
		USER_T* u = getUserByConn(umsg->ndx);
		if(app_handle_leave(srv,u) == 0) {
			buffer_append_string(bresponse,"response:leave,200,");
		}
	} else if(strcmp(ds->value->ptr,"logout")==0) {
		app_handle_logout(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"step")==0) {
		app_handle_step(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"pass")==0) {
		app_handle_pass(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"setdead")==0) {
		app_handle_set_dead(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"done")==0) {
		app_handle_done(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"score")==0) {
		app_handle_score(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"invite")==0) {
		app_handle_invite(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"join_invite")==0) {
		app_handle_join_invite(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"accept_invite")==0) {
		app_handle_accept_invite(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"reject_invite")==0) {
		app_handle_reject_invite(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"message")==0) {
		app_handle_message(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"undodead")==0) {
		app_handle_undo_dead(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"continuego")==0) {
		app_handle_continue_go(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"info")==0) {
		app_handle_info(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"post-talk")==0) {
		app_handle_post_talk(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"broadcast")==0) {
		app_handle_broadcast(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"giveup")==0) {
		app_handle_giveup(srv,umsg,bresponse);
	} else if (strcmp(ds->value->ptr,"set_step_time")==0) {
    	app_handle_set_step_time(srv,umsg,bresponse);
	}
	else if (strcmp(ds->value->ptr,"stat-room")==0) {
		app_handle_stat_room(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"read-property")==0) {
		app_handle_read_property(srv,umsg,bresponse);
	}else if (strcmp(ds->value->ptr,"update-property")==0) {
		app_handle_update_property(srv,umsg,bresponse);
	}
	if(bresponse->used>0) send_response(srv,umsg->ndx,bresponse);
	buffer_free(bresponse);
	return 0;
}
