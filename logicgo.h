#define SIDE_WHITE_MASK		0x80000000
#define SIDE_BLACK_MASK		0x40000000
#define GROUP_MASK			0x20000000
#define DIGUI_CHECK_MASK	0x10000000
#define IS_DEAD_MASK		0x08000000
#define N_GROUP_IDX_MASK	0x003FF000
#define GROUP_INDEX_MASK	0x000003FF

#define TRUE 1
#define FALSE 0
#define DISABLE_DUMP_DRAGON
#define TRACE_DEBUG(X)
#define X_MALLOC malloc
#define X_FREE free

typedef unsigned char uint8;
typedef unsigned char BOOL;

class xListNode
{
public:
    xListNode();
    xListNode(void* pData);
    ~xListNode();
    xListNode* m_pNext;
    xListNode* m_pLast;
    void* m_pData;
public:
};
class xList
{
public:
    xList();
    ~xList();
    xListNode* m_pHead;
    xListNode* m_pTail;
    int getSize();
    int m_nNodeCnt;
public:
    void removeAll();
    void push(void* pData);
    void* pop();
    void remove(void* pData);
    void** getArray();
};

enum
{
    WEIQI_SIDE_NULL=0,
    WEIQI_SIDE_BLACK=1,
    WEIQI_SIDE_WHITE=2,
};

typedef int WEIQI_SIDE_T;

struct CXY
{
    uint8 x;
    uint8 y;
};



//x,y count from 0
class CChessMap
{
public:
    uint8** mMap;
    uint8 mSize;
    CChessMap(int size)
    {
        mSize=size;
        mMap = (uint8**)X_MALLOC(size*size);
        memset(mMap,0,size*size);
    }
    ~CChessMap()
    {
        X_FREE(mMap);
    }
    inline uint8 GetSide(uint8 x,uint8 y)
    {
        uint8 side = mMap[x][y];
        return side;;
    }

    inline void Set(uint8 x,uint8 y,uint8 side)
    {
        mMap[x][y]=side;
    }

    void Clean(uint8 side)
    {
        if(side==0)
        {
            memset(mMap,0,mSize*mSize);
            return;
        }
        for(int x=0 ; x<mSize; x++)
        {
            for( int y=0 ; y<mSize; y++)
            {

                if(mMap[x][y]==side)
                {
                    mMap[x][y]=0;
                }
            }
        }

    }
};
class  CQiZi
{
public:
    unsigned int value;
    CQiZi();
    ~CQiZi();
    inline void SetSide(WEIQI_SIDE_T side);
    inline void clear();
    inline void SetValue(unsigned int v);
    inline void SetFlag(unsigned int flag_mask);
    inline void ClrFlag(unsigned int flag_mask);
    inline void setDead(BOOL is)
    {
        if(is==TRUE) SetFlag(IS_DEAD_MASK);
        else ClrFlag(IS_DEAD_MASK);
    }
    inline BOOL isDead()
    {
        return IsFlagSet(IS_DEAD_MASK);
    }
    inline BOOL  IsFlagSet(unsigned int flag_mask);
    inline WEIQI_SIDE_T GetSide();
    inline void SetDragon( unsigned int dragon_index);
    inline unsigned int GetDragonIndex()
    {
        return (value & GROUP_INDEX_MASK);
    }
    inline unsigned int getNullDragonIndex()
    {
        return ((value & N_GROUP_IDX_MASK)>>12);
    }
    inline void setNullDragon( unsigned int dragon_index)
    {
        value &= (~N_GROUP_IDX_MASK);
        value |= ((dragon_index<<12) & N_GROUP_IDX_MASK);
    }
};
class CWeiQiModel;
class  CChessQi
{
public:
    unsigned int*  data;
    unsigned int size;
    CChessQi();
    CChessQi(unsigned int sz);
    ~CChessQi();

    inline BOOL IsInDragon(unsigned int x,unsigned int y);

    void AddQi(unsigned int x,unsigned int y);

    inline BOOL IsQi(unsigned int x,unsigned int y);
    unsigned int ShuQi();
    void Dump();
};
class CHelper
{
public:
    static inline BOOL Up(int game_size,int x,int y, int&  xx,int&  yy)
    {
        xx=x;
        yy=y-1;
        if(y==0) return FALSE;
        return TRUE;
    }
    static inline BOOL Down(int game_size,int x,int y, int&  xx,int&  yy)
    {
        xx=x;
        yy=y+1;
        if(yy==game_size) return FALSE;
        return TRUE;
    }
    static inline BOOL Left(int game_size,int x,int y, int&  xx,int&  yy)
    {
        xx=x-1;
        yy=y;
        if(x==0) return FALSE;
        return TRUE;
    }
    static inline BOOL Right(int game_size,int x,int y, int&  xx,int&  yy)
    {
        xx=x+1;
        yy=y;
        if(xx==game_size) return FALSE;
        return TRUE;
    }
};
class  CDragon
{
public:
    WEIQI_SIDE_T side;
    unsigned int* data;
    unsigned char size;
    unsigned short dragon_size;
    unsigned char x1;
    unsigned char y1;
    unsigned short m_nIndex;
    unsigned int data_len;
    BOOL bSharedNull;

    CDragon();
    CDragon(WEIQI_SIDE_T s ,unsigned int* pData,unsigned int sz);
    CDragon(WEIQI_SIDE_T s,unsigned int sz);
    ~CDragon();
    void SetSide(WEIQI_SIDE_T sd);
    //´ò½Ù
    void GetJieZi(unsigned int& x,unsigned int& y);
    inline BOOL IsInDragon(unsigned int x,unsigned int y);
    inline void ClearDragon();
    inline WEIQI_SIDE_T GetSide();
    inline void AddIntoDragon(unsigned int x,unsigned int y);
    unsigned int GetSize();
    unsigned int GetIndex();
    void SetIndex(unsigned int i);
    void setNullSide(WEIQI_SIDE_T sd)
    {
        if((side!=0 && side!=sd) || (side==3)) side = 3;
        else
            side = sd;
    }
};

class  CWeiQiModel
{
public:
    unsigned int game_size;
    CQiZi** vector_qizi;
    CDragon** vector_dragon;
    unsigned char* mmap;

    CWeiQiModel();
    CWeiQiModel(unsigned int sz);
    ~CWeiQiModel();

    inline unsigned int GetQiZiIndex(unsigned int x,unsigned int y);

    inline CQiZi* GetQiZi(unsigned int x,unsigned int y);
    inline void setDead(unsigned int x,unsigned int y,BOOL is)
    {
        GetQiZi(x,y)->setDead(is);
    }
    inline BOOL isDead(unsigned int x,unsigned int y)
    {
        return GetQiZi(x,y)->isDead();
    }
    int clearDeads(WEIQI_SIDE_T side,buffer* b);
    int dumpDeads(buffer* b);
    inline void SetSide(unsigned int x,unsigned int y,WEIQI_SIDE_T side);

    inline void Clear(unsigned int x,unsigned int y);

    inline WEIQI_SIDE_T GetSide(unsigned int x, unsigned int y);
    inline BOOL IsNull(unsigned int x,unsigned int y);

    inline BOOL IsDragoned(unsigned int x, unsigned int y);
    inline void SetDragon(unsigned int x,unsigned int y,unsigned index);
    inline void setNullDragon(unsigned int x,unsigned int y,unsigned index)
    {
        GetQiZi(x,y)->setNullDragon(index);
    }
    inline CDragon* GetItsDragon(unsigned int x,unsigned int y);
    inline CDragon* getItsNullDragon(unsigned int x,unsigned int y);
    inline CDragon* GetLeftDragon(unsigned int x,unsigned int y);
    inline CDragon* GetRightDragon(unsigned int x,unsigned int y);
    inline CDragon* GetDownDragon(unsigned int x,unsigned int y);
    inline CDragon* GetUpDragon(unsigned int x,unsigned int y);
    void setNullSide(unsigned int x,unsigned int y,WEIQI_SIDE_T side);
    WEIQI_SIDE_T getNullSide(unsigned int x,unsigned int y);

    inline BOOL Up(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy);
    inline BOOL Down(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy);
    inline BOOL Left(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy);
    inline BOOL Right(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy);
    CDragon* GroupDragons(CDragon** gragons,int num);
    void AssignIndex(CDragon* g);

    inline unsigned int GetItsQi(unsigned int x,unsigned int y);


    BOOL IsNeighbourAllSameSide(unsigned int x,unsigned int y,WEIQI_SIDE_T& side,CChessMap* pDeadMap=NULL)
    ;

    inline BOOL CompareSide(unsigned int x,unsigned int y,unsigned int xx,unsigned int yy);
    inline void SetAllFlag(unsigned int mask);
    inline void SetFlag(unsigned int x,unsigned int y,unsigned int mask);
    inline BOOL IsFlagSet(unsigned int x,unsigned int y,unsigned int mask);


    void MergeDragon(CDragon* g1,CDragon* g2,BOOL isNULL=FALSE);
    void clearDragonIndex(CDragon* g1,BOOL isNULL=FALSE);

    void ClearNullDragons();
    void CalNULLDragons(CDragon* &pBlackMu, CDragon* &pWhiteMu,int &isOK);

    void CalDragons();


    inline void GetDragonQi(CDragon* dragon,CChessQi& qi);

    void KillDragon(CDragon* g);

    void Kill(unsigned int x,unsigned int y);

    void ClearDragons();

    inline CDragon* GetDragon(unsigned int dragon_index);

    CDragon* newDragon()
    {
        CDragon* ng = new CDragon(WEIQI_SIDE_NULL,game_size);
        int max_dragon=game_size*game_size;
		int i=0;
        for(i=0; i<max_dragon; i++)
        {
            CDragon* g=vector_dragon[i];
            if(g==NULL)
            {
                vector_dragon[i]=ng;
                ng->m_nIndex=i+1;
                break;
            }
        }
		if(i==max_dragon) printf("Too more dragon allocated");
        return ng;
    }

    void delDragon(CDragon* g)
    {
        vector_dragon[g->m_nIndex-1]=NULL;
        delete g;
    }

};


class   CGoCallBackInterface
{
public:
    virtual void OnDragonKilled(CDragon* pDeadDragon)=0;
    virtual void OnNextTurn(int side)=0;
};

class   CLogicGo
{
public:
    WEIQI_SIDE_T mWinner;
    WEIQI_SIDE_T mCurSide;

    CWeiQiModel* wqModel;

    void* m_hMutex;
	

    unsigned int game_size;
    WEIQI_SIDE_T last_dajie;
    BOOL bDaJie;
    BOOL bNoKill;
    unsigned int last_dj_x;
    unsigned int last_dj_y;
    unsigned int cur_dj_x;
    unsigned int cur_dj_y;

    short mWhiteNum;
    short mBlackNum;
    short mWhiteDeadNum;
    short mBlackDeadNum;

    short mWhitePassSteps;
    short mBlackPassSteps;

  short mBlackKongNum;
    short mWhiteKongNum;

    short mBlackMu;
    short mWhiteMu;

    short mBlackTieMu;
    
    CGoCallBackInterface* m_pCallBack;
    BOOL m_bServerMode;
    WEIQI_SIDE_T m_nCurSide;

    time_t start_ts;
    time_t end_ts;
	int bid;
	int wid;
	int mStepNum;
    CXY mLastStep;

    CLogicGo();
    ~CLogicGo();
	inline void setUser(int b,int w){
		bid=b;
		wid=w;
	}
    void SetSize(int size);
    void SetServerMode(BOOL bServerMode);
    void SetCallBack(CGoCallBackInterface* pCallBack);
    void init(unsigned int size,CGoCallBackInterface* pCallBack=NULL,BOOL bServerMode=TRUE);

    void OnNextTurn(int side);
    int dump(buffer* b);
    int dumpToHex(buffer* b);

    void AddKilledDragon(CDragon* g);

    buffer* mKilled;
    int mKilledNumber;
	
	/* record the steps */
	buffer* mStepKilled;
	buffer* mStepsRecord;
	int mStepCount;
	void recordStep(WEIQI_SIDE_T side,unsigned int x,unsigned int y);

    int getCurrentTurn()
    {
        return m_nCurSide;
    }

    //Dragon only killed during server mode true.
    //dragon will killed after receive the dragon killed commond during client mode.
    void OnKillDragon(CDragon* pDragon);

    unsigned int GetGameSize();

    void StartGame();

    void MoveNoThink(unsigned int x,unsigned int y,WEIQI_SIDE_T side);

    BOOL Move(unsigned int x,unsigned int y,WEIQI_SIDE_T side);

    BOOL setDead(unsigned int x,unsigned int y,WEIQI_SIDE_T side);
    int undoDead(WEIQI_SIDE_T side,buffer* b);
    int dumpDeads(buffer* b);
    void Kill(unsigned int x,unsigned int y);


    int getDianMuResult(int* bmu,int* wmu,buffer* b);
	int getDianMuResult(int* bmu,int* wmu,int* bstone,int* wstone,buffer* b);
    int getGoGameResult(int* bmu,int* wmu);


    BOOL UserPass(WEIQI_SIDE_T side);

    void Lock()
    {
    }

    void Unlock()
    {
    }


};

