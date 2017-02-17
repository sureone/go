#include <stdio.h>

#include <assert.h>
#include <zdb.h>

/*
 This example demonstrate most of the functionality of libzdb and can be compiled with a C, OBJ-C(++) or a C++ compiler.
 Compile: [gcc -std=c99|g++|clang|clang++] -o select select.c -L/<libzdb>/lib -lzdb -I/<libzdb>/include/zdb
 */

int main(void) {
        URL_T url = URL_new("mysql://ali.3636360.com/goapp?user=goapp&password=androidgood123");
        ConnectionPool_T pool = ConnectionPool_new(url);
        ConnectionPool_start(pool);
        Connection_T con = ConnectionPool_getConnection(pool);
        TRY
        {
                ResultSet_T r = Connection_executeQuery(con, "SELECT email FROM users where email =\"root\" and password=\"androidgood\"");
                while (ResultSet_next(r))
                        printf("%s\n", ResultSet_getString(r, 1));
        }
        CATCH(SQLException)
        {
                printf("SQLException -- %s\n", Exception_frame.message);
        }
        FINALLY
        {
                Connection_close(con);
        }
        END_TRY;
        return 0;
}
