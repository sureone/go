#include "log.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <errno.h>
#include <assert.h>

#include "settings.h"
#include "user.h"
#include "rank.h"
#include "sqlite3.h"
#include <zdb.h>
#ifdef DMALLOC
#include "dmalloc.h"
#endif
void print_error ();
void init_mysql();

//users table

USER_T** u_table = NULL;
static int u_nums = 0;
static char ssql[1024];
static ConnectionPool_T pool;

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
	user->groupid=0;
	user->groupname=buffer_init();
	user->groupowner=0;
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
	char *opt_host_name ;    /* server host (default=localhost) */
	char *opt_user_name;    /* username (default=login name) */
	char *opt_password;     /* password (default=none) */
	unsigned int opt_port_num; /* port number (use built-in value) */
	char *opt_socket_name;  /* socket name (use built-in value) */
	char *opt_db_name;      /* database name (default=none) */
	unsigned int opt_flags;    /* connection flags (none) */
	MYSQL *conn;                   /* pointer to connection handler */
	
} db_config;

db_config g_db_config=
{
	"ali.3636360.com",
	"goapp",
	"androidgood123",
	0,
	NULL,
	"goapp",
	0,    
    NULL
};
int g_max_id=0;

int db_query(char* ssql){
	return mysql_query(g_db_config.conn,ssql);
}

int get_max_id_from_db()
{
        MYSQL_RES *result;
        MYSQL_ROW row;
        int num_fields;
        int i;

        if(0!=db_query("SELECT * FROM max_id")){
                print_error();
        }

        result = mysql_store_result(g_db_config.conn);

        num_fields = mysql_num_fields(result);

        while ((row = mysql_fetch_row(result)))
        {
                g_max_id = atoi(row[0]);

        }
        mysql_free_result(result);
    return g_max_id;

}
void
print_error ()
{
	if (g_db_config.conn != NULL)
	{
		fprintf (stderr, "Error %u (%s): %s\n",
		mysql_errno (g_db_config.conn), mysql_sqlstate (g_db_config.conn), mysql_error (g_db_config.conn));
	}
}


int init_db(char* sqlite_db_name){
	init_mysql();
    u_table = (USER_T**)malloc(sizeof(USER_T*)*MAX_USERS);
    for ( int i = 0 ; i< MAX_USERS; i++)
    {
        u_table[i]=NULL;
    }

	return 0;
}
void init_mysql(){

	MYSQL_STMT *stmt;

	/* initialize client library */
	if (mysql_library_init (0, NULL, NULL))
	{
		fprintf (stderr, "mysql_library_init() failed\n");
		exit (1);
	}
	/* initialize connection handler */
	g_db_config.conn = mysql_init (NULL);
	if (g_db_config.conn == NULL)
	{
		fprintf (stderr, "mysql_init() failed (probably out of memory)\n");
		exit (1);
	}
	/* connect to server */
	if (mysql_real_connect (g_db_config.conn, g_db_config.opt_host_name, g_db_config.opt_user_name,
		g_db_config.opt_password,g_db_config.opt_db_name, g_db_config.opt_port_num, 
		g_db_config.opt_socket_name, g_db_config.opt_flags) == NULL)
	{
		fprintf (stderr, "mysql_real_connect() failed\n");
		mysql_close (g_db_config.conn);
		exit(1);
	}
	/* disconnect from server, terminate client library */
	/*
	
	*/

	if(0!=db_query(
				  "CREATE TABLE users ("
				  "  id INTEGER NOT NULL," //0
				  "  email VARCHAR(200) NOT NULL COLLATE utf8_bin,"
				  "  password VARCHAR(30),"
				  "  wins INTEGER,"
				  "  loses INTEGER,"
				  "  total INTEGER,"
				  "  rank INTEGER,"
				  "  score INTEGER,"
				  "	 ip VARCHAR(200),"
				  "	 serial VARCHAR(100),"
				  "  iconurl VARCHAR(200),"
				  "  descurl VARCHAR(200),"
				  "  lastdt DATETIME,"
				  " PRIMARY KEY(id),"
				  "  UNIQUE (email))"))                                  
    {
		print_error();
    }

	char* ssql = "CREATE TABLE IF NOT EXISTS `matches` (\
				  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,\
				  `win` int(10) unsigned NOT NULL,\
				  `lose` int(10) unsigned NOT NULL,\
				  `sgf` text CHARACTER SET utf8 COLLATE utf8_bin,\
				  `dt_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\
				  PRIMARY KEY (`id`)\
				) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1";
	if(0!=db_query(ssql))                          
    {
		print_error();
    }	

	if(0!=db_query(
                                  "CREATE TABLE admins ("
                                  "  id INTEGER NOT NULL,"
								  "  permission INTEGER,"								  
                                  "UNIQUE (id))"))
    {

        print_error();
    }

	if(0!=db_query( "CREATE TABLE max_id ("
                     "  id INTEGER NOT NULL)"))
    {
		print_error();
		g_max_id = get_max_id_from_db();
    }else{
		db_query("INSERT INTO max_id (id) values(0)");
	}

		mysql_close (g_db_config.conn);
 URL_T url = URL_new("mysql://ali.3636360.com/goapp?user=goapp&password=androidgood123");
        pool = ConnectionPool_new(url);
        ConnectionPool_start(pool);
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
    sprintf(ssql,"INSERT INTO admins (id,permission) VALUES(%d,%d)",
            id,perm);
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);

        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;

}

int db_execute(char* sql){
	int ret=0;
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, sql);

        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
		ret = -1;
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
	return ret;
}

int is_first_of_device(char* sn){
	
	int num_fields;
	int i;
	int bOK=-1;
	
    sprintf(ssql,"SELECT * FROM users where serial =\"%s\"", sn);

        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        ResultSet_T r = Connection_executeQuery(con, ssql);

        while (ResultSet_next(r))
        {
		bOK=0;
		break;

        }
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
	
    return bOK;
}

int is_admin(int id){
	
	int num_fields;
	int i;
	int bOK=-1;
	
    sprintf(ssql,"SELECT * FROM admins where id =%d",
			id);

        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        ResultSet_T r = Connection_executeQuery(con, ssql);

        while (ResultSet_next(r))
        {
		bOK=0;

        }
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
	
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
int group_del(int group_id,USER_T* u)
{
	int bOK=0;
	int owner=u->id;


        Connection_T con ;

    		sprintf(ssql,"select id from groups where owner=%d and id=%d", owner,group_id);
con = ConnectionPool_getConnection(pool);
       TRY
        {
	ResultSet_T r;
	r = Connection_executeQuery(con, ssql);
	bOK=-1;
        while (ResultSet_next(r))
        {
	    group_id = ResultSet_getIntByName(r,"id");
		bOK=0;
	
		break;
	}
        }
        CATCH(SQLException)
        {
                bOK=-1;
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;

        
    if (bOK!=0)
    {
        return -1;
    }



    	sprintf(ssql,"update users set group_id=0 where group_id=%d",group_id);
	WS(ssql);


	con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);

        }
        CATCH(SQLException)
        {
		bOK=-1;
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;

	
    if (bOK!=0)
    {
        return -1;
    }



    sprintf(ssql,"delete groups where group_id = %d",group_id);
	WS(ssql);
        con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
    return 0;
}
int group_list_user(buffer* b,int group_id){
	int bOK=-1;
	
    sprintf(ssql,"SELECT * from users where group_id= %d order by rank ASC",group_id);

        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        ResultSet_T r = Connection_executeQuery(con, ssql);

        while (ResultSet_next(r))
        {
		buffer_append_string(b,"\r\n");
		buffer_append_long(b,ResultSet_getIntByName(r,"id"));
		buffer_append_string(b,",");
		buffer_append_string(b,ResultSet_getStringByName(r,"email"));
		buffer_append_string(b,",");
		buffer_append_long(b,ResultSet_getIntByName(r,"rank"));
		buffer_append_string(b,",");
        }
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
	
    return bOK;
}
int group_list(buffer* b){
	int bOK=-1;
	
    sprintf(ssql,"SELECT t.id AS groupid,t.name AS groupname,t.members_num as membersnum,\
	t.score as groupscore,t1.email as ownername,t1.rank as ownerrank,t1.id as ownerid FROM groups t \
	LEFT JOIN users t1 ON t1.id=t.owner ORDER BY t.score DESC");

        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        ResultSet_T r = Connection_executeQuery(con, ssql);

        while (ResultSet_next(r))
        {
		buffer_append_string(b,"\r\n");
		buffer_append_long(b,ResultSet_getIntByName(r,"groupid"));
		buffer_append_string(b,",");
		buffer_append_string(b,ResultSet_getStringByName(r,"groupname"));
		buffer_append_string(b,",");
		buffer_append_long(b,ResultSet_getIntByName(r,"groupscore"));
		buffer_append_string(b,",");
		buffer_append_long(b,ResultSet_getIntByName(r,"membersnum"));
		buffer_append_string(b,",");
		buffer_append_long(b,ResultSet_getIntByName(r,"ownerid"));
		buffer_append_string(b,",");
		buffer_append_string(b,ResultSet_getStringByName(r,"ownername"));
		buffer_append_string(b,",");
		buffer_append_long(b,ResultSet_getIntByName(r,"ownerrank"));
		buffer_append_string(b,",");
        }
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
	
    return bOK;
}
int group_join(int group_id,USER_T* u){
	int ret = 0;	
	if(u->groupid!=0){
    		sprintf(ssql,"update groups set members_num=members_num-1 where id=%d", u->groupid);
		ret = db_execute(ssql);
	}
	
	if(ret==0){
    	sprintf(ssql,"update users set group_id=%d where id=%d", group_id,u->id);
	ret = db_execute(ssql);
	}
	if(ret==0){	
    	sprintf(ssql,"update groups set members_num=members_num+1 where id=%d", group_id);
	ret = db_execute(ssql);
	}

	return ret;
}
int group_leave(USER_T* u){
	int ret = -1;	
	if(u->groupid!=0){
    		sprintf(ssql,"update groups set members_num=members_num-1 where id=%d", u->groupid);
		ret = db_execute(ssql);
	}
	if(ret==0){

    		sprintf(ssql,"update users set group_id=0 where id=%d", u->id);
		ret = db_execute(ssql);
	}
	return ret;

}

int group_register(char* name,USER_T* u,int rank)
{
	int bOK=0;
	int group_id;
	int owner=u->id;
	if(rank>19 && u->isadmin==0) return -1;
    		sprintf(ssql,"INSERT INTO groups (owner,name,members_num) VALUES (%d,\"%s\",1)", owner,name);
	WS(ssql);

        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);

        }
        CATCH(SQLException)
        {
		bOK=-1;
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;

	
    if (bOK!=0)
    {
        return -1;
    }

    		sprintf(ssql,"select id from groups where owner=%d", owner);
con = ConnectionPool_getConnection(pool);
       TRY
        {
	ResultSet_T r;
	r = Connection_executeQuery(con, ssql);
        while (ResultSet_next(r))
        {
	    group_id = ResultSet_getIntByName(r,"id");
		break;
	}
        }
        CATCH(SQLException)
        {
                bOK=-1;
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;

        
    if (bOK!=0)
    {
        return -1;
    }



    sprintf(ssql,"UPDATE users SET group_id = %d where id=%d",group_id,owner);
	WS(ssql);
        con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
    return group_id;
}

#define FREE_COIN 2000
USER_T* u_register(char* email,char* password,char* sn)
{
	int bOK=0;
    g_max_id++;
	if(sn!=NULL)
		if( is_first_of_device(sn)!=0)
    		sprintf(ssql,"INSERT INTO users (id,email,password,score,rank,wins,loses,total,serial,coin) VALUES(%d,\"%s\",\"%s\",0,26,0,0,0,\"%s\",%d)", g_max_id, email, password, sn,FREE_COIN);
		else
    		sprintf(ssql,"INSERT INTO users (id,email,password,score,rank,wins,loses,total,serial,coin,payFrom,payTo) VALUES(%d,\"%s\",\"%s\",0,26,0,0,0,\"%s\",%d,%d,%d)", g_max_id, email, password, sn,FREE_COIN,time(NULL),(time(NULL)+3*24*56*60));
	else
    		sprintf(ssql,"INSERT INTO users (id,email,password,score,rank,wins,loses,total,coin) VALUES(%d,\"%s\",\"%s\",0,26,0,0,0,0)", g_max_id, email, password);
	WS(ssql);

        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);

        }
        CATCH(SQLException)
        {
		bOK=-1;
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;

	
    if (bOK!=0)
    {
    	g_max_id--;
        return NULL;
    }
    sprintf(ssql,"UPDATE max_id SET id = %d",g_max_id);
	WS(ssql);
    USER_T* u = NULL;
        con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
    {
        u = newUser();
        u->id = g_max_id;
        buffer_append_string(u->email,email);
        u->wins = 0;
        u->loses = 0;
        u->total = 0;
		u->score=0;
		u->rank=26;
	u->groupid=0;
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

void update_ch_coin(USER_T* u){
    //char ssql[512];
    if(u==NULL) return;
    sprintf(ssql,"UPDATE users SET ch_coin=%d WHERE id=%d",u->ch_coin,u->id);	
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
}

void update_coin(USER_T* u){
    //char ssql[512];
    if(u==NULL) return;
    sprintf(ssql,"UPDATE users SET coin=%d WHERE id=%d",u->coin,u->id);	
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
}
void update_icon(int uid,char* icon){
    //char ssql[512];
    USER_T* u = getUser(uid);
    if(u==NULL) return;
    sprintf(ssql,"UPDATE users SET iconurl=\"%s\" WHERE id=%d",icon,uid);	
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
}

void update_desc(int uid,char* desc){
    //char ssql[512];
    USER_T* u = getUser(uid);
    if(u==NULL) return;
    sprintf(ssql,"UPDATE users SET descurl=\"%s\" WHERE id=%d",desc,uid);	
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
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
        sprintf(ssql,"UPDATE users SET wins=%d,total=%d,score=%d,rank=%d ,coin=coin-%d WHERE id=%d",
                u->wins,
                u->total,u->score,u->rank,GAME_COST,uid);
    }
    else
    {
        u->loses++;
        sprintf(ssql,"UPDATE users SET loses=%d,total=%d,score=%d,rank=%d,coin=coin-%d WHERE id=%d",
                u->loses,
                u->total,u->score,u->rank,GAME_COST,uid);
    }
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_executeQuery(con, ssql);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
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
    
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, sqlbuf->ptr);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;

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
		u1=u2;
	}
	
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
	buffer_append_string(sqlbuf,"INSERT into matches (win,lose,sgf) VALUES(");
	buffer_append_long(sqlbuf,win);
	buffer_append_string(sqlbuf,",");
	buffer_append_long(sqlbuf,lose);
	buffer_append_string(sqlbuf,",\"");
	buffer_append_string(sqlbuf,sgf->ptr);
	buffer_append_string(sqlbuf,"\")");
	
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        Connection_execute(con, sqlbuf->ptr);
        rowid=Connection_lastRowId(con);
                
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
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
    
    if(sn==NULL){
    sprintf(ssql,"SELECT t.id as uid, t.wins as wins,t.loses as loses,t.total as total,\
		t.rank as rank,t.score as score,t.serial as serial,\
	        t.coin as coin,t.ch_coin as ch_coin,t.payFrom as payFrom,\
		t.payTo as payTo,t.group_id as group_id,\
		CONVERT(CAST(CONVERT(t1.name USING utf8) AS BINARY) USING latin1) as group_name,t1.owner as group_owner \	 
		FROM users t LEFT JOIN groups t1 ON t1.id = t.group_id where t.email =\"%s\" and t.password=\"%s\"", email, password);
    }else{
            sprintf(ssql,"SELECT t.id as uid, t.wins as wins,t.loses as loses,t.total as total,\
        t.rank as rank,t.score as score,t.serial as serial,\
            t.coin as coin,t.ch_coin as ch_coin,t.payFrom as payFrom,\
        t.payTo as payTo,t.group_id as group_id, t2.block_id as block_id,\
        CONVERT(CAST(CONVERT(t1.name USING utf8) AS BINARY) USING latin1) as group_name,t1.owner as group_owner \    
        FROM users t LEFT JOIN groups t1 ON t1.id = t.group_id LEFT JOIN block_users t2 ON t2.serial=t.serial where t.email =\"%s\" and t.password=\"%s\"", email, password);

    }

	WS(ssql);
	ResultSet_T r;
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        r = Connection_executeQuery(con, ssql);
        while (ResultSet_next(r))
        {
		char* oldsn=NULL;
		bOK=0;
    	
        uu=existUser(email);
        if(uu==NULL && u_nums<MAX_USERS)
        {

            USER_T* u = newUser();
            u->id = ResultSet_getIntByName(r, "uid");
            buffer_append_string(u->email,email);
			
			 u->wins=ResultSet_getIntByName(r, "wins");
			u->loses=ResultSet_getIntByName(r, "loses");
			u->total=ResultSet_getIntByName(r, "total");
            u->rank = ResultSet_getIntByName(r, "rank");
            u->score = ResultSet_getIntByName(r, "score");

			if(ResultSet_getStringByName(r, "serial")!=NULL){
				oldsn = ResultSet_getStringByName(r, "serial");
			}
			
            u->coin = ResultSet_getIntByName(r, "coin");
            u->ch_coin = ResultSet_getIntByName(r, "ch_coin");
		u->payFrom = ResultSet_getIntByName(r,"payFrom");
		u->payTo = ResultSet_getIntByName(r,"payTo");
		if(ResultSet_getStringByName(r,"group_name")!=NULL){
			buffer_append_string(u->groupname,ResultSet_getStringByName(r,"group_name"));
			u->groupid = ResultSet_getIntByName(r,"group_id");
			u->groupowner=ResultSet_getIntByName(r,"group_owner");
		}
            fprintf(stderr,"test1\n");
                u->isblock = ResultSet_getIntByName(r, "block_id");
                fprintf(stderr,"test2\n");
            insertUser(u);
            u->life++;
            uu = u;
        }

		if(sn!=NULL && (oldsn==NULL || strcmp(sn,oldsn)!=0)){
    			sprintf(ssql,"update users set serial=\"%s\" where id=%d", sn,uu->id);
        		Connection_execute(con, ssql);
		}

		break;
        }
        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        END_TRY;
    if (0 != bOK)
    {
                Connection_close(con);
		return NULL;
    }
		
		bOK=0;
                Connection_close(con);
	
    return uu;
}


int doPay(int uid,int pay)
{
    char *err;
	
	int num_fields;
	int i;
	
    int bOK=-1;
    sprintf(ssql,"SELECT * FROM users where id =%d",uid);
	WS(ssql);
	ResultSet_T r;
        Connection_T con = ConnectionPool_getConnection(pool);
       TRY
        {
        r = Connection_executeQuery(con, ssql);
	char* oldsn=NULL;
	int payFrom=0;
	int payTo=0;
        while (ResultSet_next(r))
        {
	    payFrom = ResultSet_getIntByName(r,"payFrom");
	    payTo = ResultSet_getIntByName(r,"payTo");
		oldsn=ResultSet_getStringByName(r, "serial");
		
		break;	
        }
	if(oldsn!=NULL){
			int now = time(NULL);
			if(payTo>now) payTo=payTo-now+pay*31*24*60*60;
			else
				payTo=now+pay*31*24*60*60;
			payFrom = now;
    			sprintf(ssql,"update users set payFrom=%d,payTo=%d where id=%d", payFrom,payTo,uid);
			bOK=0;
        		Connection_execute(con, ssql);
	}

        }
        CATCH(SQLException)
        {
                fprintf(stderr,"SQLException -- %s\n", Exception_frame.message);
        }
        END_TRY;
                Connection_close(con);
    return bOK;
}



