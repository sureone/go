#ifdef  __cplusplus
extern "C" {
#endif
void *logicgo_init();
void logicgo_free(void* p);
void setSize(void* p,int size);
void startGame(void* p,time_t ts);
void startGamePy(void* p);
int stepTo(void* game,int x,int y);
int setDead(void* game,int x,int y,int side);
int undoDead(void* game,int side,buffer* b);
void setGameUser(void* game,int bid,int wid);
int getCurTurn(void* game);
    int getStepNum(void* game);
    int getLastStep(void* game,int *x,int *y);
    int getKilled(void* game,buffer* b);
    int dumpGo(void* game,buffer* b);
    int dumpDeads(void* game,buffer* b);
    int dumpToHex(void* game,buffer* b);
    int goPass(void* game,int side);
    int getDianMuResult(void* game,int* black,int* white,buffer* b);
	int getDianMuResult2(void* game,int* black,int* white,int* bb, int* ww,buffer* b);
    int getGoGameResult(void* game,int* black,int* white);
	int getRecord(void* game,buffer* b);
    time_t getGameStartTime(void* game);
#ifdef  __cplusplus
}
#endif
