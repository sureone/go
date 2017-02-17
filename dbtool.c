#include "log.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <errno.h>
#include <assert.h>
#include "sqlite3.h"
#include "settings.h"
#include "user.h"
#include <my_global.h>
#include <my_sys.h>
#include <mysql.h>

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
	"127.0.0.1",
	"goapp",
	"androidgood123",
	0,
	NULL,
	"goapp",
	0,    
    NULL
};


typedef struct
{
    buffer *sqlite_db_name;
    sqlite3 *sql;
    sqlite3_stmt *stmt_create_user;
} db_config2;

db_config2 g_db_config2=
{
    NULL,
    NULL,
    NULL
};



int init_sqlite(char* sqlite_db_name)
{

    char *err;
    g_db_config2.sqlite_db_name = buffer_init();
    buffer_copy_string(g_db_config2.sqlite_db_name, (const char *)(sqlite_db_name));

    if (SQLITE_OK != sqlite3_open(g_db_config2.sqlite_db_name->ptr, &(g_db_config2.sql)))
    {
        return -1;
    }
	printf("open %s ok\n",sqlite_db_name);
	return 0;
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
		exit (1);
	}

	if(0!=mysql_query(g_db_config.conn,
                                  "CREATE TABLE users ("
                                  "  id INTEGER NOT NULL,"
                                  "  email VARCHAR(200) NOT NULL COLLATE utf8_bin,"
                                  "  password VARCHAR(30),"
                                  "  wins INTEGER,"
                                  "  loses INTEGER,"
                                  "  total INTEGER,"
								  "  rank INTEGER,"
								  "  score INTEGER,"
								  "	 ip VARCHAR(200),"
								  "	 serial VARCHAR(100),"
								  "  lastdt DATETIME,"
                                  " PRIMARY KEY(id),"
                                  "  UNIQUE (email))"))                                  
    {
		print_error();
    }
	if(0!=mysql_query(g_db_config.conn,
                                  "DROP TABLE admins"))
    {
		print_error();
    }	
	if(0!=mysql_query(g_db_config.conn,
                                  "CREATE TABLE admins ("
                                  "  id INTEGER NOT NULL,"
								  "  permission INTEGER,"
                                  "UNIQUE (id))"))
    {

        print_error();
    }
	if(0!=mysql_query(g_db_config.conn,
                                  "DROP TABLE max_id"))
    {
		print_error();
    }	
	if(0!=mysql_query(g_db_config.conn,
                                  "CREATE TABLE max_id ("
                                  "  id INTEGER NOT NULL)"))
    {
		print_error();
    }
}

void login(char* email,char* password)
{
    char ssql[512];
    char *err;
	
	MYSQL_RES *result;
	MYSQL_ROW row;
	int num_fields;
	int i;
	

    int bOK=-1;
    sprintf(ssql,"SELECT * FROM users where email =\"%s\" and password=\"%s\"",
            email,
            password);
    if (0 != mysql_query(g_db_config.conn,ssql))
    {
        print_error();
	
    }
	
	result = mysql_store_result(g_db_config.conn);

	num_fields = mysql_num_fields(result);

	while ((row = mysql_fetch_row(result)))
	{
	
     
            int id = atoi(row[0]);
            
            int wins = atoi(row[3]);
            int loses = atoi(row[4]);
            int total = atoi(row[5]);
            int rank = atoi(row[6]);
            int score = atoi(row[7]);

			printf("%d,%d,%d,%d,%d,%d\n",id,wins,loses,total,rank,score);

	}
	mysql_free_result(result);		

}
void import_max_id()
{
    char ssql[512];
    char *err;
    sqlite3_stmt *stmt = NULL;
    const char *next_stmt=NULL;

    int bOK=-1;
    sprintf(ssql,"SELECT * FROM max_id");

    if (SQLITE_OK != sqlite3_prepare(g_db_config2.sql, CONST_STR_LEN(ssql), &(stmt), &next_stmt))
    {
        printf("sqlite3 prepare failed");
	
    }
	int cnt = 0;
	int fail=0;
    while (SQLITE_ROW == sqlite3_step(stmt))
    {

		int id = sqlite3_column_int(stmt,0);
		
		
		memset(ssql,0,512);
		sprintf(ssql,"INSERT INTO max_id (id)"
					"VALUES(%d)",
					id);
		if(insertMySql(ssql)!=0)
		{
			fail++;
		}else{
			cnt++;
		}
    }
    sqlite3_finalize(stmt);
	printf("total %d imported successfully,%d failed\n",cnt,fail);
   
}
void import_admins()
{
    char ssql[512];
    char *err;
    sqlite3_stmt *stmt = NULL;
    const char *next_stmt=NULL;

    int bOK=-1;
    sprintf(ssql,"SELECT * FROM admins");

    if (SQLITE_OK != sqlite3_prepare(g_db_config2.sql, CONST_STR_LEN(ssql), &(stmt), &next_stmt))
    {
        printf("sqlite3 prepare failed");
	
    }
	int cnt = 0;
	int fail=0;
    while (SQLITE_ROW == sqlite3_step(stmt))
    {

		int id = sqlite3_column_int(stmt,0);
		int permission = sqlite3_column_int(stmt,1);
		
		
		memset(ssql,0,512);
		sprintf(ssql,"INSERT INTO admins (id,permission)"
					"VALUES(%d,%d)",
					id,permission);
		if(insertMySql(ssql)!=0)
		{
			fail++;
		}else{
			cnt++;
		}
    }
    sqlite3_finalize(stmt);
	printf("total %d imported successfully,%d failed\n",cnt,fail);
   
}


void import_users()
{
    char ssql[512];
    char *err;
    sqlite3_stmt *stmt = NULL;
    const char *next_stmt=NULL;

    int bOK=-1;
    sprintf(ssql,"SELECT * FROM users");

    if (SQLITE_OK != sqlite3_prepare(g_db_config2.sql, CONST_STR_LEN(ssql), &(stmt), &next_stmt))
    {
        printf("sqlite3 prepare failed");
	
    }
	int cnt = 0;
	int fail=0;
    while (SQLITE_ROW == sqlite3_step(stmt))
    {
		int id = sqlite3_column_int(stmt,0);
		char* email = sqlite3_column_text(stmt,1);
		char* password = sqlite3_column_text(stmt,2);
		int wins = sqlite3_column_int(stmt,3);
		int loses = sqlite3_column_int(stmt,4);
		int total = sqlite3_column_int(stmt,5);
		int rank = sqlite3_column_int(stmt,6);
		int score = sqlite3_column_int(stmt,7);
		
		
		memset(ssql,0,512);
		sprintf(ssql,"INSERT INTO users (id,email,password,wins,loses,total,rank,score)"
					"VALUES(%d,\"%s\",\"%s\",%d,%d,%d,%d,%d)",
					id,email,password,wins,loses,total,rank,score);
		if(insertMySql(ssql)!=0)
		{
			printf("VALUES(%d,\"%s\",\"%s\",%d,%d,%d,%d,%d)\n",
					id,email,password,wins,loses,total,rank,score);
			fail++;
		}else{
			cnt++;
		}
    }
    sqlite3_finalize(stmt);
	printf("total %d imported successfully,%d failed\n",cnt,fail);
   
}

int insertMySql(char* ssql){
	if(0!=mysql_query(g_db_config.conn,ssql))
    {
		print_error();
		return -1;
    }
	return 0;
	
	
}
void main(){
	init_mysql();
	login("root","androidgood");
}
void main1(){
	init_mysql();
	init_sqlite("sql.db");
	import_users();
	import_max_id();
	import_admins();
	if(g_db_config.conn!=NULL){
		mysql_close (g_db_config.conn);
		mysql_library_end ();
	}
	exit (0);	
}
