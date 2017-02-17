#include "server.h"
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
#ifdef DMALLOC
#include "dmalloc.h"
#endif

DESK_T** d_table = NULL;
int d_number=0;
int s_number=2;
DESK_T* getDesk(int did)
{
    if(did<=0 || did>d_number) return NULL;
    return d_table[did-1];
}

int getDeskStepTimeOut(int did){
	DESK_T* desk = getDesk(did);
	if(desk!=0){
		return desk->step_time_out;
	}
	return 180;
}
void setDeskStepTimeOut(int did,int timeout){
	DESK_T* desk = getDesk(did);
	if(desk!=0){
		desk->step_time_out=timeout;
	}
}

void init_desks(int count)
{
    d_number = count;
    d_table = (DESK_T**)malloc(sizeof(DESK_T*)*count);
    for ( int i = 0 ; i < count ; i++)
    {
        d_table[i] = (DESK_T*)malloc(sizeof(DESK_T));
        d_table[i]->id = i+1;
        d_table[i]->status = DESK_NULL;
        d_table[i]->bu = NULL;
        d_table[i]->wu = NULL;
        d_table[i]->game = NULL;

        memset(d_table[i]->observers,0,sizeof(d_table[i]->observers));
        d_table[i]->observer_num=0;
        d_table[i]->step_time_out=180;

    }
}

int find_free_desk()
{
		int did=-1;
    for ( int i = 0 ; i < d_number ; i++)
    {
				if(d_table[i]->status==DESK_NULL){
				 did=i+1;
				 break;
				}
    }
		return did;
}

void setGame(int did,void* game)
{    
    d_table[did-1]->game = game;
}


void* getGame(int did)
{
    if(did<=0 || did>d_number) return NULL;
    return d_table[did-1]->game;
}


int getDeskNumber()
{
    return d_number;
}
int getTotalSides()
{
    return s_number;
}
int getSideNumber(int did)
{
    if(d_table[did-1]->status>=DESK_FULL) return 2;
    if(d_table[did-1]->status==DESK_NO_FULL) return 1;
    if(d_table[did-1]->status==DESK_NULL) return 0;
}

int getDeskStatus(int did)
{
    if(did<=0 || did>d_number) return -1;
    return d_table[did-1]->status;
}



void setDeskStatus(int did,int status)
{
    if(did<=0 || did>d_number) return;
    d_table[did-1]->status = status;
}

USER_T* getPeerSide(int did, int side)
{
    USER_T* uid = NULL;
    if(side == 1)
    {
        uid = d_table[did-1]->wu;
    }
    if(side == 2)
    {
        uid = d_table[did-1]->bu;
    }
    return uid;
}
USER_T* getPeerUser(USER_T* u)
{
    if(u==NULL) return NULL;
    if(u->did<=0)
    {
        return NULL;
    }
    return getPeerSide(u->did,u->sid);
}
USER_T* getSide(int did,int side)
{
    USER_T* uid = NULL;
    if(side == 1)
    {
        uid = d_table[did-1]->bu;
    }
    if(side == 2)
    {
        uid = d_table[did-1]->wu;
    }
    return uid;
}

int leaveDesk(USER_T* u)
{
    int ret = -1;
    DESK_T* desk = getDesk(u->did);
    if(u==NULL || desk==NULL) return -1;
    int sid = u->sid;
    int did = u->did;
    int f=0;
    if(u->sid==1)
    {
        desk->bu=NULL;
        u->life--;
    }
    else if(u->sid==2)
    {
        desk->wu=NULL;
        u->life--;
    }

    if(sid==1 || sid==2)
    {
        int status = desk->status;
        if(status == DESK_NO_FULL)
            desk->status = DESK_NULL;
        else if(status >= DESK_FULL)
            desk->status = DESK_NO_FULL;
        if(f==0)
        {
            u->sid=0;
            u->did=0;
        }
    }
    return 0;
}

int joinObserver(int did,USER_T* u)
{
    DESK_T* dsk = NULL;
    if(u==NULL) return -1;
    dsk = getDesk(did);
    if(dsk==NULL) return -1;
    for(int i=0 ; i < MAX_OBSERVER ; i++)
    {
        if(dsk->observers[i]==NULL)
        {
            dsk->observers[i]=u;
            u->did=did;
            u->sid=i+3;
            dsk->observer_num++;
            u->life++;
            return u->sid;
        }
    }
    return -1;
}
int leaveObserver(USER_T *u)
{
    DESK_T* dsk = NULL;
    if(u==NULL) return -1;
    if(u->did<=0 || u->did>d_number) return -1;
    dsk=getDesk(u->did);
    if(dsk==NULL) return -1;
    for(int i=0 ; i < MAX_OBSERVER ; i++)
    {
        if(dsk->observers[i]==u)
        {
            dsk->observers[i]=NULL;
            dsk->observer_num--;
            u->did=0;
            u->sid=0;
            u->life--;
            break;
        }
    }
}

int joinDesk(int did,int sid,USER_T* u)
{
    int ret = -1;
    DESK_T *desk = NULL;
    if(u==NULL) return -1;
    desk=getDesk(did);
    if(desk==NULL) return -1;

    if(sid==1 && desk->bu==NULL)
    {
        desk->bu = u;
        u->sid=1;
        u->life++;
        ret = 0;
    }
    else if(sid==2 && desk->wu==NULL)
    {
        desk->wu = u;
        u->sid=2;
        u->life++;
        ret = 0;
    }
    if(ret == 0)
    {
        int status = desk->status;
        u->did=did;
        printf("%s, %d\n",__func__,status);
        if(status == DESK_NO_FULL)
            desk->status = DESK_FULL;
        else if(status == DESK_NULL)
            desk->status = DESK_NO_FULL;
    }
    return ret;
}
