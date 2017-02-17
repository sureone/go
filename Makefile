#OPTIM = -O2
INCLUDE = -I. -I.. -I/usr/include/mysql -I/usr/local/include/zdb -I/usr/local/include/ -I ../libaray/include

LPATH = -L/usr/local/lib -L../libaray/lib

#CFLAGS = -DHAVE_CONFIG_H -DHAVE_VERSION_H -DLIBRARY_DIR="\"/usr/local/lib\"" -DSBIN_DIR="\"/usr/local/sbin\"" $(INCLUDE)  -g -D_REENTRANT -D__EXTENSIONS__ -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE -D_LARGE_FILES  -g $(OPTIM) -Wall -W -Wshadow -pedantic -std=gnu99 

CFLAGS = -DHAVE_CONFIG_H -DHAVE_VERSION_H -DLIBRARY_DIR="\"/usr/local/lib\"" -DSBIN_DIR="\"/usr/local/sbin\"" $(INCLUDE) \
  -g -D_REENTRANT -D__EXTENSIONS__ -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE -D_LARGE_FILES  -g $(OPTIM) -w -pedantic -std=gnu99 

#CFLAGS = -DHAVE_CONFIG_H -DHAVE_VERSION_H $(INCLUDE) -g $(OPTIM) -std=gnu99

LDFLAG = -g $(LPATH) $(OPTIM) -Wall -W -Wshadow -pedantic -std=gnu99 -Wl,-ldl -lstdc++ \
 -lmysqlclient -lpthread -lzdb -lzmq
LDFLAG = -g $(LPATH) $(OPTIM) -Wall -W -Wshadow -pedantic -std=gnu99 -Wl,-ldl -lstdc++ -lpthread
# -ldmallocth

#LDFLAG = -g $(LPATH) $(OPTIM) -std=gnu99 -Wl,-ldl -lstdc++ -lmysqlclient -lpthread

#CFLAGS += $(shell pkg-config --cflags json-c)
#LDFLAG += $(shell pkg-config --libs json-c)

#LDFLAG += -lzmq

CPPFLAGS = -w -g $(INCLUDE) -D_REENTRANT -D__EXTENSIONS__ -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE -D_LARGE_FILES


# CFLAGS += -DDMALLOC
# CPPFLAGS += -DDMALLOC

IPFACE_SOURCE = server.c \
	config.c array.c buffer.c data_array.c data_stringc.c bitset.c chunk.c log.c stat_cache.c etag.c splaytree.c \
	fdevent.c fdevent_libev.c fdevent_poll.c fdevent_solaris_devpoll.c \
	fdevent_freebsd_kqueue.c fdevent_linux_sysepoll.c fdevent_select.c fdevent_solaris_port.c \
	joblist.c network.c network_write.c network_writev.c network_linux_sendfile.c connections.c connections-glue.c \
	threadqueue.c shelper.c \
	modgo.c log.c rank.c desk.c appgo.c goapi.cc logicgo.cc user_sqlite.c sqlite3.c

IPFACE_OBJS = server.o \
	config.o array.o buffer.o data_array.o data_string.o bitset.o chunk.o log.o stat_cache.o etag.o splaytree.o \
	fdevent.o fdevent_libev.o fdevent_poll.o fdevent_solaris_devpoll.o \
	fdevent_freebsd_kqueue.o fdevent_linux_sysepoll.o fdevent_select.o fdevent_solaris_port.o \
	joblist.o network.o network_write.o network_writev.o network_linux_sendfile.o connections.o connections-glue.o \
	threadqueue.o shelper.o \
	modgo.o rank.o desk.o appgo.o goapi.o logicgo.o user_sqlite.o sqlite3.o 
JSONC_OBJS = arraylist.o debug.o json_c_version.o json_object.o \
	json_object_iterator.o json_pointer.o json_tokener.o \
	json_util.o json_visit.o libjson.o linkhash.o printbuf.o random_seed.o
        


HEADERS = log.h
.SUFFIXES: .c .o .cc

MOD_GO_SOURCE = modgo.c user_mysql.c log.c buffer.c rank.c threadqueue.c shelper.c data_string.c array.c desk.c appgo.c goapi.cc logicgo.cc
MOD_GO_OBJS = modgo.o user_mysql.o log.o buffer.o rank.o threadqueue.o shelper.o data_string.o array.o desk.o appgo.o goapi.o logicgo.o


IPFACE_EXE = ipface.exe
MOD_GO_EXE = modgo.exe

all:$(IPFACE_EXE) client.exe sqlite.exe
$(IPFACE_EXE):$(IPFACE_OBJS) $(JSONC_OBJS)
	gcc -o $@ $(IPFACE_OBJS) $(JSONC_OBJS) $(LDFLAG)
logip.exe:zmqcc.o
	gcc -o $@ zmqcc.o $(LDFLAG)
sqlite.exe:shell.o sqlite3.o
	gcc -o $@ shell.o sqlite3.o $(LDFLAG)
# test.exe:test.o
# 	gcc -o $@ test.o $(LDFLAG)
zmqlog.exe:zmqlog.o threadqueue.o 
	gcc -o $@ zmqlog.o threadqueue.o $(LDFLAG)
logmodgo.exe:logmodgo.o
	gcc -o $@ logmodgo.o $(LDFLAG)
client.exe:client.o
	gcc -o $@ client.o $(LDFLAG)
zmqcc.exe:zmqcc.o
	gcc -o $@ zmqcc.o $(LDFLAG)
	
zmqss.exe:zmqss.o
	gcc -o $@ zmqss.o $(LDFLAG)
	
dbtool.exe:dbtool.o sqlite3.o buffer.o
	gcc -o $@ dbtool.o sqlite3.o buffer.o $(LDFLAG) -lm
$(MOD_GO_EXE):$(MOD_GO_OBJS)
	gcc -o $@ $(MOD_GO_OBJS) $(LDFLAG)	
.c.o:
	gcc $(CFLAGS) -c -o $@ $<

clean:
	rm -f *.o *.exe

rebuild:clean all
