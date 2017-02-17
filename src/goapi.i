%module goapi
%{
#define SWIG_FILE_WITH_INIT
struct Vector{
        double x,y,z;
};
struct buffer{
        char *ptr;

        size_t used;
        size_t size;
};
%}
struct  buffer{
        char *ptr;

        size_t used;
        size_t size;
};
struct Vector{
        double x,y,z;
};
void *logicgo_init(); 
void logicgo_free(void* p); 
void setSize(void* p,int size); 
void startGamePy(void* p); 
int stepTo(void* game,int x,int y); 
int setDead(void* game,int x,int y,int side); 
int undoDead(void* game,int side,struct buffer* b); 
        void setGameUser(void* game,int bid,int wid); 
    int getCurTurn(void* game); 
    int getStepNum(void* game); 
    int getLastStep(void* game,int *x,int *y); 
    int getKilled(void* game,struct buffer* b); 
    int dumpGo(void* game,struct buffer* b); 
    int dumpToHex(void* game,struct buffer* b); 
    int goPass(void* game,int side); 
    int getDianMuResult(void* game,int* black,int* white,struct buffer* b);
        int getDianMuResult2(void* game,int* black,int* white,int* bb, int* ww,struct buffer* b);
    int getGoGameResult(void* game,int* black,int* white);
        int getRecord(void* game,struct buffer* b);
    time_t getGameStartTime(void* game);

