//desks table
#include "user.h"
#define MAX_OBSERVER 20
typedef struct
{
    short id;
    USER_T* bu;
    USER_T* wu;
    char status;
    void* game;

    USER_T* observers[MAX_OBSERVER];
    char observer_num;		
    int step_time_out;
    
} DESK_T;

typedef enum
{
    DESK_NULL,
    DESK_NO_FULL,
    DESK_FULL,
    DESK_READY,
    DESK_PLAYING,
    DESK_DIANMU,
    DESK_WAITING_RESUME,
} desk_status_t;
int joinDesk(int did,int sid,USER_T* u);
int leaveObserver(USER_T *u);
int joinObserver(int did,USER_T* u);
int leaveDesk(USER_T* u);
USER_T* getSide(int did,int side);
USER_T* getPeerUser(USER_T* u);
USER_T* getPeerSide(int did, int side);
void setDeskStatus(int did,int status);
int getDeskStatus(int did);
void* getGame(int did);

int find_free_desk();
DESK_T* getDesk(int did);
void setDeskStepTimeOut(int did,int timeout);
int getDeskStepTimeOut(int did);
