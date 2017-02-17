#include "log.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <errno.h>
#include <assert.h>
#include "sqlite3.h"
#include "settings.h"
#include "list.h"
#include "user.h"
#ifdef DMALLOC
#include "dmalloc.h"
#endif
//users table

USER_T** u_table = NULL;
static int u_nums = 0;

int getUserInfo(USER_T* u,char* name,int len,int* wins,int* totals,int* status)
{
    *wins=u->wins;
    *totals=u->total;
    *status=u->status;
    for(int i = 0; i<len ; i++)
    {
        name[i] = u->email->ptr[i];
        if (name[i]==0) return i;
        if (name[i]=='@')
        {
            name[i]=0;
            return i;
        }
    }
    name[len-1]=0;
    return (len-1);
}

USER_T* getUser(int uid)
{
    USER_T* u;
    for(int i = 0 ; i< MAX_USERS ; i++) {
        u = u_table[i];
        if(u!=NULL && u->id == uid) return u;
    }
    return NULL;
}
USER_T* getUserByConn(int ndx)
{
    USER_T* u;
    for(int i = 0 ; i< MAX_USERS ; i++) {
        u = u_table[i];
        if(u!=NULL && u->conn_ndx == ndx) return u;
    }
    return NULL;
}


static USER_T* newUser()
{
    USER_T* user = calloc(1,sizeof(*user));
    user->email = buffer_init();
    user->ispass = 0;
    user->isdone = 0;
    user->life = 0;
    user->id=0;
    user->did=0;
    user->sid=0;
    user->conn_ndx=-1;
    user->tc.count=0;
    user->isadmin=0;
    user->tc.start=0;
    user->noresponse=0;
    user->wait_action_idle=-1;
		user->score=0;
		user->is_wait_accept=-1;
		user->invite_id=-1;
		user->invite_did=-1;
    return user;
}


void resetUser(USER_T* user){
		user->is_wait_accept=-1;
		user->invite_id=-1;
		user->invite_did=-1;	
    user->did=0;
    user->sid=0;
    user->ispass = 0;
    user->isdone = 0;
}
int freeUser(USER_T* u)
{
    if(u->life==0)
    {
        buffer_free(u->email);
        free(u);
        return 1;
    }
    return 0;
}

typedef struct
{
    buffer *sqlite_db_name;
    sqlite3 *sql;
    sqlite3_stmt *stmt_create_user;
} db_config;

db_config g_db_config=
{
    NULL,
    NULL,
    NULL
};
int g_max_id=0;

int get_max_id_from_db()
{
    const char *next_stmt=NULL;
    sqlite3_stmt *stmt = NULL;
    if (SQLITE_OK != sqlite3_prepare(g_db_config.sql,
                                     CONST_STR_LEN("SELECT * FROM max_id"),
                                     &(stmt), &next_stmt))
    {
        WS("prepare failed");
    }
    while (SQLITE_ROW == sqlite3_step(stmt))
    {
        g_max_id = sqlite3_column_int(stmt,0);
        break;
    }
    sqlite3_finalize(stmt);
    return g_max_id;
}
int init_db(char* sqlite_db_name)
{
    const char *next_stmt;
    char *err;
    g_db_config.sqlite_db_name = buffer_init();
    buffer_copy_string(g_db_config.sqlite_db_name, (const char *)(sqlite_db_name));

    if (SQLITE_OK != sqlite3_open(g_db_config.sqlite_db_name->ptr, &(g_db_config.sql)))
    {
        return -1;
    }
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  "CREATE TABLE users ("
                                  "  id INTEGER NOT NULL,"
                                  "  email TEXT NOT NULL,"
                                  "  password TEXT,"
                                  "  wins INTEGER,"
                                  "  loses INTEGER,"
                                  "  total INTEGER,"
								  "  rank INTEGER,"
								  "  score INTEGER,"
								  "  res1 TEXT,"
								  "  res2 TEXT,"
                                  " PRIMARY KEY(id),"
                                  "  UNIQUE (email))",
                                  NULL, NULL, &err))
    {

        if (0 != strstr(err, "already exists"))
        {
            WS(err);
        }
        sqlite3_free(err);
    }
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  "CREATE TABLE admins ("
                                  "  id INTEGER NOT NULL,"
								  "  permission INTEGER,"
                                  "UNIQUE (id))",
                                  NULL, NULL, &err))
    {

        if (0 != strstr(err, "already exists"))
        {
            WS(err);
        }
        sqlite3_free(err);
    }
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  "CREATE TABLE max_id ("
                                  "  id INTEGER NOT NULL)", NULL, NULL, &err))
    {
        if (0 != strcmp(err, "already exists"))
        {
		WS(err);
            g_max_id = get_max_id_from_db();
        }
        sqlite3_free(err);
    }
    else
    {
        g_max_id=0;
        sqlite3_exec(g_db_config.sql,"INSERT INTO max_id (id) values(0)",NULL,NULL,&err);
    }

    u_table = (USER_T**)malloc(sizeof(USER_T*)*MAX_USERS);
    for ( int i = 0 ; i< MAX_USERS; i++)
    {
        u_table[i]=NULL;
    }

}

int insertUser(USER_T* u) {
    for ( int i = 0 ; i< MAX_USERS; i++)
    {
        if(u_table[i]==NULL)
        {
            u->idx = i;
            u_table[i] = u;
            u_nums++;
            return i+1;
        }
    }
    return 0;
}

void add_admin(int id,int perm){
    char ssql[512];
    char *err;
    sqlite3_stmt *stmt = NULL;
    sprintf(ssql,"INSERT INTO admins (id,permission) VALUES(%d,%d)",
            id,perm);
    sqlite3_exec(g_db_config.sql, ssql, NULL,NULL,&err);
    sqlite3_free(err);
}

int is_admin(int id){
    char ssql[512];
    sqlite3_stmt *stmt = NULL;
    const char *next_stmt=NULL;
    int bOK=-1;
    sprintf(ssql,"SELECT * FROM admins where id =%d",
			id);
    if (SQLITE_OK == sqlite3_prepare(g_db_config.sql, CONST_STR_LEN(ssql), &(stmt), &next_stmt)) {
    	if (SQLITE_ROW == sqlite3_step(stmt)) {
			bOK=0;
		}
	}
	return bOK;
}

int removeUser(USER_T* u)
{
    if(u!=NULL)
    {
        //panic happened here
        //printf("removeUser %s,u->conn_ndx=%d\n",u->email->ptr,u->conn_ndx);
        printf("removeUser u>conn_ndx=%d\n",u->conn_ndx);
        u_table[u->idx] = NULL;
        u->life--;
        u_nums--;
        freeUser(u);
        return 0;
    }
    return -1;
}
int group_register(char* name,int id,int rank)
{
    char ssql[512];
    char *err;
    sqlite3_stmt *stmt = NULL;
    g_max_id++;
    sprintf(ssql,"INSERT INTO group (name,owner) VALUES(\"%s\",%d)",name,id,
	WS(ssql);
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  ssql,
                                  NULL,NULL,&err))
    {
	WS(err);
        sqlite3_free(err);
	return -1;
    }
    GROUP_T* g = NULL;
    sqlite3_free(err);
    {
        u = newUser();
        u->id = g_max_id;
        buffer_append_string(u->email,email);
        u->wins = 0;
        u->loses = 0;
        u->total = 0;
		u->score=0;
		u->rank=26;
        insertUser(u);
        u->life++;
        resetTimeout(&(u->tc));
    }
    return u;
}

USER_T* u_register(char* email,char* password,char* sn)
{
    char ssql[512];
    char *err;
    sqlite3_stmt *stmt = NULL;
    g_max_id++;
    sprintf(ssql,"INSERT INTO users (id,email,password,score,rank) VALUES(%d,\"%s\",\"%s\",0,26)",
            g_max_id,
            email,
            password);
	WS(ssql);
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  ssql,
                                  NULL,NULL,&err))
    {
	WS(err);
        sqlite3_free(err);
    	g_max_id--;
        return NULL;
    }
    sprintf(ssql,"UPDATE max_id SET id = %d",g_max_id);
	WS(ssql);
    USER_T* u = NULL;
    sqlite3_exec(g_db_config.sql,ssql,NULL,NULL,&err);
    sqlite3_free(err);
    {
        u = newUser();
        u->id = g_max_id;
        buffer_append_string(u->email,email);
        u->wins = 0;
        u->loses = 0;
        u->total = 0;
		u->score=0;
		u->rank=26;
        insertUser(u);
        u->life++;
        resetTimeout(&(u->tc));
    }
    return u;
}

USER_T* existUser(char* email)
{
    USER_T* u=NULL;
    for(int i = 0 ; i< MAX_USERS ; i++) {
        u = u_table[i];
        if(u!=NULL && strcmp(u->email->ptr,email)==0) return u;
    }
    return NULL;
}

int getAllConns(int** conn_ndxs){
    USER_T* u=NULL;
    *conn_ndxs = calloc(MAX_USERS,sizeof(int));
    int cnt = 0;
    for(int i = 0 ; i< MAX_USERS ; i++) {
        u = u_table[i];
        if(u!=NULL && u->conn_ndx!=-1){
		(*conn_ndxs)[cnt]=u->conn_ndx;
		cnt++;
	}
    }
    return cnt;
}

int getAllConnsInRoom(int** conn_ndxs){
    USER_T* u=NULL;
    *conn_ndxs = calloc(MAX_USERS,sizeof(int));
    int cnt = 0;
    for(int i = 0 ; i< MAX_USERS ; i++) {
        u = u_table[i];
        if(u!=NULL){
		if(u->status>USER_IN_ROOM && u->status<USER_IN_DESK) {
			(*conn_ndxs)[cnt]=u->conn_ndx;
			cnt++;
		}
	}
    }
    return cnt;
}


int getAllAdmins(int** conn_ndxs){
    USER_T* u=NULL;
    *conn_ndxs = calloc(20,sizeof(int));
    int cnt = 0;
    for(int i = 0 ; i< MAX_USERS ; i++) {
        u = u_table[i];
        if(u!=NULL && u->isadmin==1 && u->conn_ndx!=-1){
		(*conn_ndxs)[cnt]=u->conn_ndx;
		cnt++;
		if(cnt==20) break;
	}
    }
    return cnt;
}


int userSetRank(int wid,int lid)
{
	USER_T* wu = getUser(wid);
	USER_T* lu = getUser(lid);
	cal_rank(wu,lu);
}
int userEndGame(int uid,int isWin)
{
    char ssql[512];
    char *err;
    int bOK=-1;
    USER_T* u = getUser(uid);
    if(u==NULL) return bOK;
    u->total++;
    if(isWin==1)
    {
        u->wins++;
        sprintf(ssql,"UPDATE users SET wins=%d,total=%d,score=%d,rank=%d WHERE id=%d",
                u->wins,
                u->total,u->score,u->rank,uid);
    }
    else
    {
        u->loses++;
        sprintf(ssql,"UPDATE users SET loses=%d,total=%d,score=%d,rank=%d WHERE id=%d",
                u->loses,
                u->total,u->score,u->rank,uid);
    }
    sqlite3_exec(g_db_config.sql,ssql,NULL,NULL,&err);
    sqlite3_free(err);
    return 0;
}

USER_T* login(char* email,char* password,char* sn)
{
    char ssql[512];
    char *err;
    sqlite3_stmt *stmt = NULL;
    const char *next_stmt=NULL;
    USER_T* uu = NULL;
    int bOK=-1;
    sprintf(ssql,"SELECT * FROM users where email =\"%s\" and password=\"%s\"",
            email,
            password);
	WS(ssql);
    if (SQLITE_OK != sqlite3_prepare(g_db_config.sql, CONST_STR_LEN(ssql), &(stmt), &next_stmt))
    {
        WS("sqlite3 prepare failed");
	
    }
    if (SQLITE_ROW == sqlite3_step(stmt))
    {
        uu=existUser(email);
        if(uu==NULL && u_nums<MAX_USERS)
        {
            USER_T* u = newUser();
            u->id = sqlite3_column_int(stmt,0);
            buffer_append_string(u->email,email);
            u->wins = sqlite3_column_int(stmt,3);
            u->loses = sqlite3_column_int(stmt,4);
            u->total = sqlite3_column_int(stmt,5);
            u->rank = sqlite3_column_int(stmt,6);
            u->score = sqlite3_column_int(stmt,7);
            insertUser(u);
            u->life++;
            uu = u;
        }
    }
    sqlite3_finalize(stmt);
    return uu;
}

