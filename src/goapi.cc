#include <sys/types.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>

#include <errno.h>
#include <assert.h>
#include <time.h>

#ifdef  __cplusplus
extern "C" {
#endif
#include "buffer.h"
#ifdef  __cplusplus
}
#endif
#include "logicgo.h"

#include "goapi.h"
#ifdef DMALLOC
#include "dmalloc.h"
#endif
#define dbgprint printf

void *logicgo_init()
{
    CLogicGo* go = new CLogicGo();
    dbgprint("%s called\n",__func__);
    return go;
}
void logicgo_free(void* p)
{
    CLogicGo* go = (CLogicGo*)p;
    delete go;
}
void setSize(void *p,int size)
{
    CLogicGo* go = (CLogicGo*)p;
    dbgprint("%s called\n",__func__);
    go->SetSize(size);
}
void startGamePy(void* game){
	startGame(game,0);
}
void startGame(void* game,time_t ts)
{
    CLogicGo* go = (CLogicGo*)game;
    go->start_ts = time(NULL);
    dbgprint("%s called\n",__func__);
    go->StartGame();
}
void setGameUser(void* game,int bid,int wid){
    CLogicGo* go = (CLogicGo*)game;
	go->setUser(bid,wid);
}
time_t getGameStartTime(void* game)
{
    CLogicGo* go = (CLogicGo*)game;
    if(go!=NULL)
        return go->start_ts;
    else
        return 0;
}
int dumpGo(void* game,buffer* b)
{
    CLogicGo* go = (CLogicGo*)game;
    go->dump(b);
}
int dumpDeads(void* game,buffer* b)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->dumpDeads(b);
}
int dumpToHex(void* game,buffer* b)
{
    CLogicGo* go = (CLogicGo*)game;
    go->dumpToHex(b);
}
int goPass(void* game,int sid)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->UserPass(sid);
}
int getLastStep(void* game,int* x,int* y)
{
    CLogicGo* go = (CLogicGo*)game;
    *x=go->mLastStep.x;
    *y=go->mLastStep.y;
    if(*x==22) return -1;
    return 0;
}
int setDead(void* game,int x,int y,int side)
{
    CLogicGo* go = (CLogicGo*)game;
    int ret = go->setDead((unsigned int)x,(unsigned int)y, (WEIQI_SIDE_T)side);
    return ret;
}

int undoDead(void* game,int side,buffer* b)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->undoDead((WEIQI_SIDE_T)side,b);
}

int stepTo(void* game,int x,int y)
{
    CLogicGo* go = (CLogicGo*)game;
    int ret = go->Move((unsigned int)x,(unsigned int)y, go->getCurrentTurn());
    if(ret>0)
    {
	go->mStepNum++;
        go->mLastStep.x=(unsigned int)x;
        go->mLastStep.y=(unsigned int)y;
    }
    return ret;
}

int getStepNum(void* game)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->mStepNum;
}
int getCurTurn(void* game)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->getCurrentTurn();
}

int getKilled(void* game,buffer* b)
{
    int len = 0;
    CLogicGo* go = (CLogicGo*)game;
    if(go->mKilled!=0 && go->mKilled->used>0)
        buffer_append_string(b,go->mKilled->ptr);
    return go->mKilledNumber;
}
int getRecord(void* game,buffer* b)
{
    int len = 0;
    CLogicGo* go = (CLogicGo*)game;
	buffer_append_string(b,"[");
	if(go->mStepsRecord!=0 && go->mStepsRecord->used>0)
	{
		buffer_append_long(b,go->mStepCount);		
		buffer_append_string(b,",");
		buffer_append_string(b,go->mStepsRecord->ptr);
	}else
		buffer_append_string(b,"0,");
	buffer_append_string(b,"]");
	return 0;
}
int getGoGameResult(void* game,int* black,int* white)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->getGoGameResult(black,white);

}
int getDianMuResult2(void* game,int* black,int* white,int* bb, int* ww,buffer* b)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->getDianMuResult(black,white,bb,ww,b);
}
int getDianMuResult(void* game,int* black,int* white,buffer* b)
{
    CLogicGo* go = (CLogicGo*)game;
    return go->getDianMuResult(black,white,b);
}

