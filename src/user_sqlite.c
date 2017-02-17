#include "log.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <errno.h>
#include <assert.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "settings.h"
#include "user.h"
#include "rank.h"
#include "sqlite3.h"
#ifdef DMALLOC
#include "dmalloc.h"
#endif
void print_error ();
void init_mysql();

//users table

USER_T** u_table = NULL;
static int u_nums = 0;
static char ssql[1024];


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
    user->iconurl=NULL;
    user->descurl=NULL;
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
  
    user->isblock=0;
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


int db_execute(char* sql){
    int ret=0;
    char *err=0;
    WS(sql);
    sqlite3_exec(g_db_config.sql, sql, NULL,NULL,&err);
    if(err!=0){
        WS(err);
        ret=-1;
    }
    sqlite3_free(err);

    return ret;
}
typedef struct _qset{
    char** rows;
    int nrow;
    int ncolumn;
}QSET_T;
void freeResult(struct _qset* qs){
    sqlite3_free_table( qs->rows);
    free(qs);
}
QSET_T* db_query(char* sql){
    
    char *zErrMsg = 0;
    QSET_T* qs = (QSET_T*)malloc(sizeof(QSET_T));
    //查询数据;
    sqlite3_get_table( g_db_config.sql , sql , &(qs->rows) , &(qs->nrow) , &(qs->ncolumn) , &zErrMsg );

    log_error_write(__FILE__,__LINE__,"ssdsd",sql,"row:",qs->nrow,"column:",qs->ncolumn);
    
    if(zErrMsg!=0){
        dprintf("%s\n",zErrMsg);
    }
    return qs;    

}

int get_max_id_from_db()
{
    QSET_T* qs = db_query("SELECT * FROM max_id");
    g_max_id = atoi(qs->rows[qs->ncolumn+0]);
    freeResult(qs);
    return g_max_id;

}
void
print_error ()
{

}


int init_db(char* sqlite_db_name){
    char* err=0;
    g_db_config.sqlite_db_name = buffer_init();
    buffer_copy_string(g_db_config.sqlite_db_name, (const char *)(sqlite_db_name));

    if (SQLITE_OK != sqlite3_open(g_db_config.sqlite_db_name->ptr, &(g_db_config.sql)))
    {
        return -1;
    }
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  "CREATE TABLE users (" 
                                  "  id INTEGER NOT NULL," //0
                                  "  email TEXT NOT NULL,"
                                  "  password TEXT,"
                                  "  wins INTEGER,"
                                  "  loses INTEGER,"
                                  "  total INTEGER,"
                                  "  rank INTEGER,"
                                  "  score INTEGER,"
                                  "  ip TEXT,"
                                  "  serial TEXT,"
                                  "  iconurl TEXT,"
                                  "  descurl TEXT,"
                                  "  lastdt DATETIME,"
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
                                  "CREATE TABLE `matches` (\
                                      `id` INTEGER NOT NULL,\
                                      `win` INTEGER NOT NULL,\
                                      `lose` INTEGER NOT NULL,\
                                      `dt_created` INTEGER\
                                      )",
                                  NULL, NULL, &err))
    {

        WS("create table matches");
        if (0 != strstr(err, "already exists"))
        {
            WS(err);
        }
        WS(err);
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
                                  "CREATE TABLE block_users ("
                                  "  block_id TEXT,"
                                  "  serial TEXT NOT NULL"
                                  ")",
                                  NULL, NULL, &err))
    {
        WS("create table block_users");
        if (0 != strstr(err, "already exists"))
        {
            WS(err);
        }

        WS(err);
        sqlite3_free(err);
    }
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  "CREATE TABLE app_property ("
                                  "  key TEXT NOT NULL,"
                                  "  value TEXT NOT NULL,"
                                  "UNIQUE (key))",
                                  NULL, NULL, &err))
    {
        WS("create table app_property");
        if (0 != strstr(err, "already exists"))
        {
            WS(err);
        }

        WS(err);
        sqlite3_free(err);
    }
    if (SQLITE_OK != sqlite3_exec(g_db_config.sql,
                                  "CREATE TABLE max_id ("
                                  "  id INTEGER NOT NULL)", NULL, NULL, &err))
    {
        WS("create table max_id");
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
        sqlite3_free(err);
    }


    u_table = (USER_T**)malloc(sizeof(USER_T*)*MAX_USERS);
    for ( int i = 0 ; i< MAX_USERS; i++)
    {
        u_table[i]=NULL;
    }

    return 0;
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


int is_first_of_device(char* sn){
    
   
    QSET_T* qs = 0;
    int bOK=-1;
    
    sprintf(ssql,"SELECT * FROM users where serial =\"%s\"", sn);
    qs=db_query(ssql);
    if(qs->nrow>0){
        bOK=0;
    }
  
    // for( i=0 ; i<( qs->nrow + 1 ) * qs->ncolumn ; i++ ){
    //     printf( "rows[%d] = %s", i , rows[i] );
    // }
    
    freeResult( qs );


    return bOK;
}

int update_property(char* key,char* value){
    QSET_T* qs = 0;
    
    sprintf(ssql,"SELECT value FROM app_property where key = '%s'",
            key);
    qs=db_query(ssql);
    if(qs->nrow>0){
        sprintf(ssql,"update app_property set value='%s' where key = '%s'",
            value,key);
    }else{
        sprintf(ssql,"insert into app_property (key,value) values('%s','%s')",
            key,value);
    }
    db_execute(ssql);
    freeResult( qs );
}

char* read_property(char* key){
    QSET_T* qs = 0;
    char* value = 0;
    sprintf(ssql,"SELECT value FROM app_property where key = '%s'",
            key);
    qs=db_query(ssql);
    if(qs->nrow>0){
        value = qs->rows[qs->ncolumn];
    }
    freeResult( qs );
    return value;

}

int is_admin(int id){
    

    int bOK=-1;
    QSET_T* qs = 0;
    
    sprintf(ssql,"SELECT * FROM admins where id =%d",
            id);
    qs=db_query(ssql);
    if(qs->nrow>0){
        bOK=0;
    }
  

    
    freeResult( qs );

  
    
    return bOK;
}

int removeUser(USER_T* u)
{
    if(u!=NULL)
    {
        //panic happened here
        //printf("removeUser %s,u->conn_ndx=%d\n",u->email->ptr,u->conn_ndx);
        // printf("removeUser u>conn_ndx=%d\n",u->conn_ndx);

        u_table[u->idx] = NULL;
        u->life--;
        u_nums--;
        freeUser(u);
        return 0;
    }
    return -1;
}


#define FREE_COIN 2000
USER_T* u_register(char* email,char* password,char* sn)
{
    int bOK=0;
    g_max_id++;
    if(sn!=NULL)
        if( is_first_of_device(sn)!=0)
            sprintf(ssql,"INSERT INTO users (id,email,password,score,rank,wins,loses,total,serial) VALUES(%d,\"%s\",\"%s\",0,26,0,0,0,\"%s\")", g_max_id, email, password, sn);
        else
            sprintf(ssql,"INSERT INTO users (id,email,password,score,rank,wins,loses,total,serial) VALUES(%d,\"%s\",\"%s\",0,26,0,0,0,\"%s\")", g_max_id, email, password, sn);
    else
            sprintf(ssql,"INSERT INTO users (id,email,password,score,rank,wins,loses,total) VALUES(%d,\"%s\",\"%s\",0,26,0,0,0)", g_max_id, email, password);
    WS(ssql);

    if(db_execute(ssql)!=0){
        bOK=-1;
    }
    
    if (bOK!=0)
    {
        g_max_id--;
        return NULL;
    }
    sprintf(ssql,"UPDATE max_id SET id = %d",g_max_id);
    WS(ssql);
    USER_T* u = NULL;
    db_execute(ssql);
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


void getName(USER_T* u,char* name)
{
    for(int i = 0; i<MAX_NAME_LEN ; i++) {
        name[i] = u->email->ptr[i];
        if (name[i]==0) break;
        if (name[i]=='@') {
            name[i]=0;
            break;
        }
    }
    name[MAX_NAME_LEN]=0;
}

void update_icon(int uid,char* icon){
    //char ssql[512];
    USER_T* u = getUser(uid);
    if(u==NULL) return;
    sprintf(ssql,"UPDATE users SET iconurl=\"%s\" WHERE id=%d",icon,uid);   
    db_execute(ssql);
}

void update_desc(int uid,char* desc){
    //char ssql[512];
    USER_T* u = getUser(uid);
    if(u==NULL) return;
    sprintf(ssql,"UPDATE users SET descurl=\"%s\" WHERE id=%d",desc,uid);   
    db_execute(ssql);
}

int userEndGame(int uid,int isWin)
{
    //char ssql[512];
    char *err;
    int bOK=-1;
    USER_T* u = getUser(uid);
    if(u==NULL) return bOK;
    u->coin-=GAME_COST;
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
    db_execute(ssql);
    return 0;
}


void dbAddChatRecord(int uid,int did,char* name,char* content){

    int bOK=-1;
    buffer* sqlbuf = buffer_init();
    buffer_append_string(sqlbuf,"INSERT into live_chats (did,uid,name,content) VALUES(");
    buffer_append_long(sqlbuf,did);
    buffer_append_string(sqlbuf,",");
    buffer_append_long(sqlbuf,uid);
    buffer_append_string(sqlbuf,",\"");
    buffer_append_string(sqlbuf,name);
    buffer_append_string(sqlbuf,"\"");
    buffer_append_string(sqlbuf,",\"");
    buffer_append_string(sqlbuf,content);
    buffer_append_string(sqlbuf,"\")");
    buffer_free(sqlbuf);
    
}



int addMatchRecord(int win,int lose,buffer* bRecord){ 
    int bOK=-1;
    
    userEndGame(win,1);
    userEndGame(lose,0);

    int rowid=-1;


    
    buffer* sgf = buffer_init();
        
    USER_T* u1 = getUser(win);
    if(u1==NULL) return rowid;
    
    USER_T* u2 = getUser(lose);
    if(u2==NULL) return rowid;
    
    USER_T* uu = NULL ;
    
    if(u1->sid!=1){
        uu=u2;
        u2=u1;
        u1=uu;
    }
    rowid = time(NULL);
    getName(u1,ssql);   
    buffer_append_string(sgf,"{[PB:");
    buffer_append_string(sgf,ssql);
    getName(u2,ssql);
    buffer_append_string(sgf,"][PW:");
    buffer_append_string(sgf,ssql);     
    rank_map_t* rmap=NULL;
    rmap=getRankMap(u1->rank);
    buffer_append_string(sgf,"][BR:");
    buffer_append_string(sgf,rmap->label);  
    rmap=getRankMap(u2->rank);
    buffer_append_string(sgf,"][WR:");
    buffer_append_string(sgf,rmap->label);  
    buffer_append_string(sgf,"]}");
    buffer_append_string(sgf,bRecord->ptr); 

    buffer* sqlbuf = buffer_init();
    buffer_append_string(sqlbuf,"INSERT into matches (id,win,lose,dt_created) VALUES(");
    buffer_append_long(sqlbuf,rowid);
    buffer_append_string(sqlbuf,",");
    buffer_append_long(sqlbuf,win);
    buffer_append_string(sqlbuf,",");
    buffer_append_long(sqlbuf,lose);
    buffer_append_string(sqlbuf,",");
    buffer_append_long(sqlbuf,(time(NULL)+3*24*56*60));
    buffer_append_string(sqlbuf,")");

    
    db_execute(sqlbuf->ptr);

    {
        if( (access( "matches", 0 )) !=0 ){
            if(mkdir("matches", 0755)==-1)  
            {   
                WS("mkdir 'matches' error");
            }  
        }else{
            char fname[512];
            sprintf(fname,"matches/%ld.sgf",rowid);
            // WS(fname);
            // WS(sgf->ptr,sgf->size);
            int fd=open(fname,O_WRONLY|O_CREAT,S_IRUSR|S_IWUSR);
            write(fd,sgf->ptr,sgf->used);
            
            close(fd);
        }
    }

    
    buffer_free(sgf);
    buffer_free(sqlbuf);

    return rowid;

    
}
USER_T* login(char* email,char* password,char* sn)
{
    char *err;
    
    int num_fields;
    int i;
    
    USER_T* uu = NULL;
    int bOK=-1;
    QSET_T* qs = 0;
    
    if(sn==NULL){
        sprintf(ssql,"SELECT t.id as uid, t.wins as wins,t.loses as loses,t.total as total,\
        t.rank as rank,t.score as score,t.serial as serial\
        FROM users t where t.email =\"%s\" and t.password=\"%s\"", email, password);
    }else{
            sprintf(ssql,"SELECT t.id as uid, t.wins as wins,t.loses as loses,t.total as total,\
        t.rank as rank,t.score as score,t.serial as serial,\
        t2.block_id as block_id\
        FROM users t LEFT JOIN block_users t2 ON t2.serial=t.serial where t.email =\"%s\" and t.password=\"%s\"", email, password);

    }

    WS(ssql);
    qs = db_query(ssql);

    if(qs->nrow>0)
    {
        char* oldsn=NULL;
        bOK=0;
        
        uu=existUser(email);
        if(uu==NULL && u_nums<MAX_USERS)
        {

            USER_T* u = newUser();
            u->id = atoi(qs->rows[qs->ncolumn+0]);
            buffer_append_string(u->email,email);
            
             u->wins= atoi(qs->rows[qs->ncolumn+1]);
            u->loses= atoi(qs->rows[qs->ncolumn+2]);
            u->total= atoi(qs->rows[qs->ncolumn+3]);
            u->rank =  atoi(qs->rows[qs->ncolumn+4]);
            u->score =  atoi(qs->rows[qs->ncolumn+5]);    
            oldsn = qs->rows[qs->ncolumn+6];
        
            if(sn!=NULL){
                if(qs->rows[qs->ncolumn+7]!=0)
                    u->isblock =atoi(qs->rows[qs->ncolumn+7]);
                else
                    u->isblock = 0;
            }

            insertUser(u);
            u->life++;
            uu = u;
        }

        if(sn!=NULL && (oldsn==NULL || strcmp(sn,oldsn)!=0)){
                sprintf(ssql,"update users set serial=\"%s\" where id=%d", sn,uu->id);
                db_execute( ssql);
        }



    }

    freeResult(qs);
       
    if (0 != bOK)
    {
        return NULL;
    }
        
    bOK=0;
            
    
    return uu;
}




