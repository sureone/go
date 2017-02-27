#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>


#include <errno.h>
#include <assert.h>

#ifdef  __cplusplus
extern "C" {
#endif
#include "buffer.h"
#ifdef  __cplusplus
}
#endif
#include "logicgo.h"
#ifdef DMALLOC
#include "dmalloc.h"
#endif

xListNode::xListNode()
{
}
xListNode::~xListNode()
{
}
xListNode::xListNode(void* pData)
{
    this->m_pLast=0;
    this->m_pData=pData;
    this->m_pNext=0;
}


xList::~xList()
{
}
xList::xList()
{
    this->m_pHead=0;
    this->m_pTail=0;
    m_nNodeCnt=0;
}
void xList::removeAll()
{

    xListNode* newNode =m_pHead;

    while(newNode!=0)
    {
        xListNode* pTemp = newNode;

        newNode=newNode->m_pNext;
        delete pTemp;
    }

    m_nNodeCnt=0;

    if(this->m_nNodeCnt==0)
    {
        this->m_pHead=0;
        this->m_pTail=0;
    }

}
void xList::remove(void* pData)
{

    xListNode* newNode =m_pHead;

    while(newNode!=0)
    {
        if(newNode->m_pData==pData)
        {
            m_nNodeCnt--;
            break;
        }
        newNode=newNode->m_pNext;
    }

    if(newNode!=0)
    {
        xListNode* pPrev = newNode->m_pLast;
        if(pPrev!=0)
        {
            pPrev->m_pNext = newNode->m_pNext;
            if(newNode->m_pNext!=0)
            {
                newNode->m_pNext->m_pLast=pPrev;
            }
        }
        else
        {
            this->m_pHead = newNode->m_pNext;
            this->m_pHead->m_pLast=0;
        }
    }
    if(newNode==m_pTail)
    {
        m_pTail=newNode->m_pLast;
    }
    delete newNode;
    if(this->m_nNodeCnt==0)
    {
        this->m_pHead=0;
        this->m_pTail=0;
    }

}
void xList::push(void* pData)
{

    xListNode* newNode = new xListNode(pData);
    if(this->m_pHead==0)
    {
        this->m_pHead=newNode;
    }
    if(this->m_pTail!=0)
    {
        this->m_pTail->m_pNext=newNode;
        newNode->m_pLast=this->m_pTail;
    }
    this->m_pTail=newNode;
    m_nNodeCnt++;
}
void* xList::pop()
{
    void* pData = 0;
    xListNode* pNode = 0;
    if(this->m_pHead!=0)
    {
        pNode = m_pHead;
        pData=m_pHead->m_pData;
        if(pNode->m_pNext!=0)
        {
            pNode->m_pNext->m_pLast=0;
        }
        m_pHead = pNode->m_pNext;
        delete pNode;
        m_nNodeCnt--;
    }
    if(m_pHead==0)
    {
        m_pTail=0;
    }
    return pData;

}

int xList::getSize()
{
    return m_nNodeCnt;
}

void** xList::getArray()
{
    void** array = (void**)X_MALLOC(sizeof(void*)*getSize());

    xListNode* newNode =m_pHead;
    int i=0;
    while(newNode!=0)
    {
        array[i++]=newNode->m_pData;
        newNode=newNode->m_pNext;
    }


    return array;
}

CQiZi::CQiZi()
{
    value=0;
};
CQiZi::~CQiZi()
{
};
void CQiZi::SetSide(WEIQI_SIDE_T side)
{
    if(side==WEIQI_SIDE_BLACK)
    {
        value|=SIDE_BLACK_MASK;
    }
    if(side==WEIQI_SIDE_WHITE)
    {
        value|=SIDE_WHITE_MASK;
    }
    if(side==WEIQI_SIDE_NULL)
    {
        value&=~(SIDE_WHITE_MASK|SIDE_BLACK_MASK);
    }
}
void CQiZi::clear()
{
    value=0;
}
void CQiZi::SetValue(unsigned int v)
{
    value = v;
}
void CQiZi::SetFlag(unsigned int flag_mask)
{
    value|=flag_mask;
}
void CQiZi::ClrFlag(unsigned int flag_mask)
{
    value&=~flag_mask;
}
BOOL  CQiZi::IsFlagSet(unsigned int flag_mask)
{
    return ((value&flag_mask)>0);
}
WEIQI_SIDE_T CQiZi::GetSide()
{
    if(IsFlagSet(SIDE_BLACK_MASK))
    {
        return WEIQI_SIDE_BLACK;
    }
    else if(IsFlagSet(SIDE_WHITE_MASK))
    {

        return WEIQI_SIDE_WHITE;
    }
    else
    {
        return WEIQI_SIDE_NULL;
    }
}
void CQiZi::SetDragon(unsigned int dragon_index)
{
    value &= (~GROUP_INDEX_MASK);
    value |= (dragon_index & GROUP_INDEX_MASK);
}

CChessQi::CChessQi() {};
CChessQi::CChessQi(unsigned int sz)
{
    size=sz;

    unsigned int num_int=(sz*sz/32)+1;
    data=(unsigned int* )X_MALLOC(num_int* sizeof(unsigned int));
    memset(data,0,num_int* sizeof(unsigned int));
}
CChessQi::~CChessQi()
{
    X_FREE(data);
}

BOOL CChessQi::IsInDragon(unsigned int x,unsigned int y)
{
    unsigned int index=y*size+x;
    unsigned int no_int=index/32;
    unsigned int no_bit=index%32;
    unsigned int value=*(data+no_int);
    if((value&(1<<no_bit))>0) return TRUE;
    return FALSE;
}

void CChessQi::AddQi(unsigned int x,unsigned int y)
{
    unsigned int index=y*size+x;
    unsigned int no_int=index/32;
    unsigned int no_bit=index%32;
    unsigned int value=*(data+no_int);
    value |= (1<<no_bit);
    *(data+no_int)=value;

}

BOOL CChessQi::IsQi(unsigned int x,unsigned int y)
{
    unsigned int index=y*size+x;
    unsigned int no_int=index/32;
    unsigned int no_bit=index%32;
    unsigned int value=*(data+no_int);
    if((value&(1<<no_bit))!=0) return TRUE;
    return FALSE;
}
unsigned int CChessQi::ShuQi()
{
    unsigned int qi=0;
    for(unsigned int x=0; x<size; x++)
    {
        for(unsigned int y=0; y<size; y++)
        {
            if(IsQi(x,y)==TRUE) qi++;

        }
    }
    return qi;
}
void CChessQi::Dump()
{
#ifdef DISABLE_DUMP_DRAGON
    return;
#endif
    char str[128];

    for(unsigned int x=0; x<size; x++)
    {
        for(unsigned int y=0; y<size; y++)
        {
            if(IsQi(x,y)==TRUE)
            {
                sprintf(str,"(%d,%d)",x,y);
                TRACE_DEBUG(str);
            }
        }
    }



}

CDragon::CDragon()
{
    side=0;
}
CDragon::CDragon(WEIQI_SIDE_T s ,unsigned int* pData,unsigned int sz)
{
    size=sz;
    side=s;
    unsigned int num_int=(sz*sz/32)+1;
    data=(unsigned int* )X_MALLOC(num_int* sizeof(unsigned int));
    memcpy(data,pData,num_int* sizeof(unsigned int));
    dragon_size=0;
    data_len=num_int;
}
CDragon::CDragon(WEIQI_SIDE_T s,unsigned int sz)
{
    size=sz;
    side=s;
    unsigned int num_int=(sz*sz/32)+1;
    data=(unsigned int* )X_MALLOC(num_int* sizeof(unsigned int));
    memset(data,0,num_int* sizeof(unsigned int));
    dragon_size=0;
    data_len=num_int;
}
CDragon::~CDragon()
{
    X_FREE(data);
}
void CDragon::SetSide(WEIQI_SIDE_T sd)
{
    side=sd;
}
void CDragon::GetJieZi(unsigned int& x,unsigned int& y)
{
    x=x1;
    y=y1;


}
BOOL CDragon::IsInDragon(unsigned int x,unsigned int y)
{
    unsigned int index=y*size+x;
    unsigned int no_int=index/32;
    unsigned int no_bit=index%32;
    unsigned int value=*(data+no_int);
    if((value&(1<<no_bit))>0) return TRUE;
    return FALSE;
}
void CDragon::ClearDragon()
{
    unsigned int num_int=(size*size/32)+1;
    memset(data,0,num_int* sizeof(unsigned int));
    dragon_size=0;
}
WEIQI_SIDE_T CDragon::GetSide()
{
    return side;
}
void CDragon::AddIntoDragon(unsigned int x,unsigned int y)
{
    x1=x;
    y1=y;
    dragon_size++;
    unsigned int index=y*size+x;
    unsigned int no_int=index/32;
    unsigned int no_bit=index%32;
    unsigned int value=*(data+no_int);
    value |= (1<<no_bit);
    *(data+no_int)=value;
}
unsigned int CDragon::GetSize()
{
    return dragon_size;
}
unsigned int CDragon::GetIndex()
{
    return m_nIndex;
}
void CDragon::SetIndex(unsigned int i)
{
    m_nIndex=i;
}


CWeiQiModel::CWeiQiModel() {};
CWeiQiModel::CWeiQiModel(unsigned int sz)
{
    game_size=sz;
    unsigned int vector_size=sz*sz;

    vector_qizi=(CQiZi**)(X_MALLOC(vector_size*sizeof(CQiZi*)));
    vector_dragon=(CDragon**)(X_MALLOC(vector_size*sizeof(CDragon*)));
    for(unsigned int i=0; i<vector_size; i++)
    {
        vector_qizi[i]=new CQiZi();
        vector_dragon[i]=NULL;

    }
    mmap=(unsigned char*)(X_MALLOC((sz*sz*2)/8+1));
    memset(mmap,0,((sz*sz*2)/8+1));
};

CWeiQiModel::~CWeiQiModel()
{
    unsigned int vector_size=game_size*game_size;
    for(unsigned int i=0; i<vector_size; i++)
    {
        if(vector_dragon[i]!=NULL)
            delete (vector_dragon[i]);
        delete (vector_qizi[i]);
    }

    free(vector_dragon);
    free(vector_qizi);
	free(mmap);


}

unsigned int CWeiQiModel::GetQiZiIndex(unsigned int x,unsigned int y)
{
    return (y*game_size+x);
}


CQiZi* CWeiQiModel::GetQiZi(unsigned int x,unsigned int y)
{
    CQiZi* qz= vector_qizi[GetQiZiIndex(x,y)];
    return qz;
}
void CWeiQiModel::SetSide(unsigned int x,unsigned int y,WEIQI_SIDE_T side)
{
    int byte = (y*game_size+x)*2/8;
    int bit = ((y*game_size+x)*2)%8;
    unsigned char s = 0;
    GetQiZi(x,y)->SetSide(side);
    if(WEIQI_SIDE_BLACK==side)
    {
        s=1;
    }
    else if(WEIQI_SIDE_WHITE==side)
    {
        s=2;
    }
    (mmap[byte])&=(~(3<<bit));
    (mmap[byte])|=(s<<bit);
    /*
    how to get the side
    unsigned char side=mmap[byte];
    side &=(3<<bit);
    side = side>>bit;
    */

}

void CWeiQiModel::Clear(unsigned int x,unsigned int y)
{
    GetQiZi(x,y)->clear();
    int byte = (y*game_size+x)*2/8;
    int bit = ((y*game_size+x)*2)%8;
    (mmap[byte])&=(~(3<<bit));

}

WEIQI_SIDE_T CWeiQiModel::GetSide(unsigned int x, unsigned int y)
{

    CQiZi* pQiZi = GetQiZi(x,y);
    return pQiZi->GetSide();
}

BOOL CWeiQiModel::IsNull(unsigned int x,unsigned int y)
{
    if(GetSide(x,y)==WEIQI_SIDE_NULL) return TRUE;
    return FALSE;
}


BOOL CWeiQiModel::IsDragoned(unsigned int x, unsigned int y)
{
    if(GetQiZi(x,y)->GetDragonIndex()!=0) return TRUE;
    return FALSE;
}
void CWeiQiModel::SetDragon(unsigned int x,unsigned int y,unsigned index)
{
    GetQiZi(x,y)->SetDragon(index);
}

CDragon* CWeiQiModel::getItsNullDragon(unsigned int x,unsigned int y)
{
    unsigned int dragon_index = GetQiZi(x,y)->getNullDragonIndex();
    unsigned int max_dragon=game_size*game_size;
    if(dragon_index==0 || dragon_index>max_dragon){
		 return NULL;
	}
    return vector_dragon[dragon_index-1];
}
CDragon* CWeiQiModel::GetItsDragon(unsigned int x,unsigned int y)
{
    unsigned int dragon_index = GetQiZi(x,y)->GetDragonIndex();
    unsigned int max_dragon=game_size*game_size;
    if(dragon_index==0 || dragon_index>max_dragon){
		 return NULL;
	}
    return vector_dragon[dragon_index-1];
}
CDragon* CWeiQiModel::GetLeftDragon(unsigned int x,unsigned int y)
{
    CDragon* near = NULL;
    unsigned int xx,yy;
    if(Left(x,y,xx,yy)==TRUE)
    {
        near = GetItsDragon(xx,yy);
    }
    return near;
}
CDragon* CWeiQiModel::GetRightDragon(unsigned int x,unsigned int y)
{
    CDragon* near = NULL;
    unsigned int xx,yy;
    if(Right(x,y,xx,yy)==TRUE)
    {
        near = GetItsDragon(xx,yy);
    }
    return near;
}
CDragon* CWeiQiModel::GetUpDragon(unsigned int x,unsigned int y)
{
    CDragon* near = NULL;
    unsigned int xx,yy;
    if(Up(x,y,xx,yy)==TRUE)
    {
        near = GetItsDragon(xx,yy);
    }
    return near;
}
CDragon* CWeiQiModel::GetDownDragon(unsigned int x,unsigned int y)
{
    CDragon* near = NULL;
    unsigned int xx,yy;
    if(Down(x,y,xx,yy)==TRUE)
    {
        near = GetItsDragon(xx,yy);
    }
    return near;
}

BOOL CWeiQiModel::Up(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy)
{
    xx=x;
    yy=y-1;
    if(y==0) return FALSE;
    return TRUE;
}

BOOL CWeiQiModel::Down(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy)
{
    xx=x;
    yy=y+1;
    if(yy==game_size) return FALSE;
    return TRUE;
}

BOOL CWeiQiModel::Left(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy)
{
    xx=x-1;
    yy=y;
    if(x==0) return FALSE;
    return TRUE;
}

BOOL CWeiQiModel::Right(unsigned int x,unsigned int y, unsigned int&  xx,unsigned int&  yy)
{
    xx=x+1;
    yy=y;
    if(xx==game_size) return FALSE;
    return TRUE;
}
unsigned int CWeiQiModel::GetItsQi(unsigned int x,unsigned int y)
{
    unsigned int xx;
    unsigned int yy;
    unsigned int qi=0;
    xx=yy=-1;
    if(Up(x,y,xx,yy)==TRUE)
    {
        if(xx!=-1 && IsNull(xx,yy)==TRUE) qi|=1;
    }
    xx=yy=-1;
    if(Down(x,y,xx,yy)==TRUE)
    {
        if(xx!=-1 && IsNull(xx,yy)==TRUE) qi|=4;
    }
    xx=yy=-1;
    if(Left(x,y,xx,yy)==TRUE)
    {
        if(xx!=-1 && IsNull(xx,yy)==TRUE) qi|=8;
    }
    xx=yy=-1;
    if(Right(x,y,xx,yy)==TRUE)
    {
        if(xx!=-1 && IsNull(xx,yy)==TRUE) qi|=2;
    }

    return qi;

}


BOOL CWeiQiModel::IsNeighbourAllSameSide(unsigned int x,unsigned int y,WEIQI_SIDE_T& side,CChessMap* pDeadMap)
{
    unsigned int xx;
    unsigned int yy;

    BOOL isBlackSide = FALSE;
    BOOL isWhiteSide = FALSE;
    if(Up(x,y,xx,yy)==TRUE)
    {
        side=GetSide(xx,yy);
        if(side==WEIQI_SIDE_BLACK) isBlackSide=TRUE;
        if(side==WEIQI_SIDE_WHITE) isWhiteSide=TRUE;

        //if it is a dead chess
        if(pDeadMap->GetSide(xx,yy)!=0)
        {
            isBlackSide=FALSE;
            isWhiteSide=FALSE;
        }
    }
    if(Down(x,y,xx,yy)==TRUE)
    {
        side=GetSide(xx,yy);
        if(side==WEIQI_SIDE_BLACK) isBlackSide=TRUE;
        if(side==WEIQI_SIDE_WHITE) isWhiteSide=TRUE;
        if(pDeadMap->GetSide(xx,yy)!=0)
        {
            isBlackSide=FALSE;
            isWhiteSide=FALSE;
        }

    }
    if(Left(x,y,xx,yy)==TRUE)
    {
        side=GetSide(xx,yy);
        if(side==WEIQI_SIDE_BLACK) isBlackSide=TRUE;
        if(side==WEIQI_SIDE_WHITE) isWhiteSide=TRUE;
        if(pDeadMap->GetSide(xx,yy)!=0)
        {
            isBlackSide=FALSE;
            isWhiteSide=FALSE;
        }
    }
    if(Right(x,y,xx,yy)==TRUE)
    {
        side=GetSide(xx,yy);
        if(side==WEIQI_SIDE_BLACK) isBlackSide=TRUE;
        if(side==WEIQI_SIDE_WHITE) isWhiteSide=TRUE;
        if(pDeadMap->GetSide(xx,yy)!=0)
        {
            isBlackSide=FALSE;
            isWhiteSide=FALSE;
        }
    }

    if(isBlackSide == TRUE && isWhiteSide==TRUE)
    {
        return FALSE;
    }

    if(isBlackSide == FALSE && isWhiteSide==FALSE)
    {
        side=WEIQI_SIDE_NULL;
        return TRUE;
    }

    if(isBlackSide == TRUE)
    {
        side = WEIQI_SIDE_BLACK;
    }

    if(isWhiteSide == TRUE)
    {
        side = WEIQI_SIDE_WHITE;
    }


    return TRUE;
}

BOOL CWeiQiModel::CompareSide(unsigned int x,unsigned int y,unsigned int xx,unsigned int yy)
{
    WEIQI_SIDE_T s1=GetSide(x,y);
    WEIQI_SIDE_T s2=GetSide(xx,yy);
    if(s1==s2 && s1!=WEIQI_SIDE_NULL)
    {
        return TRUE;
    }
    return FALSE;
}
void CWeiQiModel::SetAllFlag(unsigned int mask)
{
    unsigned int sz = game_size*game_size;
    for(unsigned int i=0; i<sz; i++)
    {
        vector_qizi[i]->SetFlag(mask);
    }

}
void CWeiQiModel::SetFlag(unsigned int x,unsigned int y,unsigned int mask)
{
    GetQiZi(x,y)->SetFlag(mask);

}
BOOL CWeiQiModel::IsFlagSet(unsigned int x,unsigned int y,unsigned int mask)
{
    return (GetQiZi(x,y)->IsFlagSet(mask));
}


void CWeiQiModel::clearDragonIndex(CDragon* g1,BOOL isNULL)
{
    unsigned int size = game_size;
    for(unsigned int x=0; x<size; x++)
    {
        for(unsigned int y=0; y<size; y++)
        {
            if(g1->IsInDragon(x,y))
            {
                if(isNULL==true)
                    setNullDragon(x,y,0);
                else
                    SetDragon(x,y,0);
            }
        }
    }

}


void CWeiQiModel::MergeDragon(CDragon* g1,CDragon* g2,BOOL isNULL)
{
    unsigned int size = game_size;
    for(unsigned int x=0; x<size; x++)
    {
        for(unsigned int y=0; y<size; y++)
        {
            if(g2->IsInDragon(x,y))
            {
                g1->AddIntoDragon(x,y);
                if(isNULL==true)
                    setNullDragon(x,y,g1->GetIndex());
                else
                    SetDragon(x,y,g1->GetIndex());
            }
        }
    }

}

CDragon* CWeiQiModel::GroupDragons(CDragon** gragons,int num)
{
    CDragon* newDragon = this->newDragon();
    unsigned int size = game_size;
    for(unsigned int x=0; x<size; x++)
    {
        for(unsigned int y=0; y<size; y++)
        {
            for(int i=0; i<num; i++)
            {
                CDragon* g = gragons[i];
                if(g->IsInDragon(x,y))
                {
                    newDragon->AddIntoDragon(x,y);
                }
            }
        }
    }
    return newDragon;
}

void CWeiQiModel::AssignIndex(CDragon* g)
{
    unsigned int size = game_size;
    for(unsigned int x=0; x<size; x++)
    {
        for(unsigned int y=0; y<size; y++)
        {
            if(g->IsInDragon(x,y))
            {
                SetDragon(x,y,g->GetIndex());
            }
        }
    }
}

void CWeiQiModel::ClearNullDragons()
{
    ClearDragons();
}

void CWeiQiModel::CalNULLDragons(CDragon* &pBlackMu, CDragon* &pWhiteMu,int &isOK)
{
    xList list_null_dragon;
    unsigned int sz = game_size*game_size;
    for(unsigned int x=0; x<game_size; x++)
    {
        for(unsigned int y=0; y<game_size; y++)
        {
            BOOL bnull = IsNull(x,y);
            unsigned int xx,yy;
            xx=yy=-1;
            CDragon* g_left_null=NULL;
            CDragon* g_up_null=NULL;
			printf("(%d,%d)",x,y);
            if(Up(x,y,xx,yy)==TRUE)
            {
                //If there is null or a dead chess in the position
                if(xx!=-1 && (IsNull(xx,yy)==TRUE || isDead(xx,yy)==TRUE))
                {
                    g_up_null=getItsNullDragon(xx,yy);
                }
            }
            xx=yy=-1;
            if(Left(x,y,xx,yy)==TRUE)
            {
                //If there is null or a dead chess in the position
                if(xx!=-1 && (IsNull(xx,yy)==TRUE || isDead(xx,yy)==TRUE))
                {
                    g_left_null = getItsNullDragon(xx,yy);
                }
            }

            //The dead is Not null and Not be set dead
            if(bnull == FALSE && isDead(x,y)==FALSE)
            {
                //setup the side of nearby null dragon
                if(g_up_null!=NULL)
                    g_up_null->setNullSide(GetSide(x,y));
                if(g_left_null!=NULL)
                    g_left_null->setNullSide(GetSide(x,y));
            }
            else
            {
                CDragon* g=NULL;
                if(g_up_null!=NULL)
                {
                    g=g_up_null;
                }
                if(g_left_null!=NULL)
                {
                    g=g_left_null;
                }
                if(g_up_null!=NULL
                        && g_left_null!=NULL
                        && g_up_null != g_left_null)
                {
                    g_up_null->setNullSide(g_left_null->GetSide());
                    MergeDragon(g_up_null,g_left_null,TRUE);
                    g=g_up_null;
                    list_null_dragon.remove(g_left_null);
                    delDragon(g_left_null);
                    printf("m");
                }
                if(g==NULL)
                {
                    g= newDragon();
                    printf("n");
                    list_null_dragon.push(g);
                }
                g->AddIntoDragon(x,y);
                setNullDragon(x,y,g->GetIndex());
                if(Up(x,y,xx,yy)==TRUE)
                {
                    if(IsNull(xx,yy)==FALSE && isDead(xx,yy)==FALSE)
                    {
                        g->setNullSide(GetSide(xx,yy));
                    }
                }
                if(Left(x,y,xx,yy)==TRUE)
                {
                    if(IsNull(xx,yy)==FALSE && isDead(xx,yy)==FALSE)
                    {
                        g->setNullSide(GetSide(xx,yy));
                    }
                }
            }
        }
    }

    printf("null dragon num=%d\n",list_null_dragon.getSize());
    xListNode* pNode = list_null_dragon.m_pHead;
    while(pNode!=NULL)
    {

        CDragon* g = (CDragon*)(pNode->m_pData);
        printf("null dragon (%p)'s side=%d\n",g,g->GetSide());
        if(g->GetSide()!=3)
        {
            if(g->GetSide()==WEIQI_SIDE_BLACK) {
                if(pBlackMu==NULL)
                    pBlackMu = g;
                else
                {
                    MergeDragon(pBlackMu,g,TRUE);
                    delDragon(g);
                }
            }else if(g->GetSide()==WEIQI_SIDE_WHITE) {
               	if(pWhiteMu==NULL)
                   	pWhiteMu = g;
               	else
               	{
                   	MergeDragon(pWhiteMu,g,TRUE);
                  	delDragon(g);
               	}
			}else if(g->GetSide()==0){
                   delDragon(g);
			}
        }
        else
            delDragon(g);
        pNode=pNode->m_pNext;
    }
    list_null_dragon.removeAll();

    for(unsigned int x=0; x<game_size; x++)
    {
        for(unsigned int y=0; y<game_size; y++)
        {
            if(GetSide(x,y)==WEIQI_SIDE_NULL || isDead(x,y)==TRUE)
            {
                setNullDragon(x,y,0);
            }
        }
    }
}

void CWeiQiModel::GetDragonQi(CDragon* dragon,CChessQi& qi)
{

    for(unsigned int x=0; x<game_size; x++)
    {
        for(unsigned int y=0; y<game_size; y++)
        {

            unsigned int xx;
            unsigned int yy;
            if(dragon->IsInDragon(x,y)==FALSE) continue;

            if(Up(x,y,xx,yy)==TRUE)
            {
                if(IsNull(xx,yy)==TRUE) qi.AddQi(xx,yy);
            }
            if(Down(x,y,xx,yy)==TRUE)
            {
                if(IsNull(xx,yy)==TRUE) qi.AddQi(xx,yy);
            }
            if(Left(x,y,xx,yy)==TRUE)
            {
                if(IsNull(xx,yy)==TRUE) qi.AddQi(xx,yy);
            }
            if(Right(x,y,xx,yy)==TRUE)
            {
                if(IsNull(xx,yy)==TRUE) qi.AddQi(xx,yy);
            }

        }
    }
}

void CWeiQiModel::KillDragon(CDragon* g)
{
    for(unsigned int x=0; x<game_size; x++)
    {
        for(unsigned int y=0; y<game_size; y++)
        {
            if(g->IsInDragon(x,y)==TRUE)
            {
                Kill(x,y);
            }
        }
    }
}

void CWeiQiModel::Kill(unsigned int x,unsigned int y)
{
    GetQiZi(x,y)->clear();
    int byte = (y*game_size+x)*2/8;
    int bit = ((y*game_size+x)*2)%8;
    (mmap[byte])&=(~(3<<bit));
}
int CWeiQiModel::dumpDeads(buffer* b){
    unsigned int maxi=game_size*game_size;
    int num=0;
    for(unsigned int x=0; x<game_size; x++)
    {
        for(unsigned int y=0; y<game_size; y++)
        {
            CQiZi* qz = GetQiZi(x,y);
            if(qz->isDead()==TRUE)
            {
                num++;
                buffer_append_long(b,x);
                buffer_append_string(b,",");
                buffer_append_long(b,y);
                buffer_append_string(b,",");
            }
        }
    }
    return num;
}
int CWeiQiModel::clearDeads(WEIQI_SIDE_T side,buffer* b)
{
    unsigned int maxi=game_size*game_size;
    int num=0;
    for(unsigned int x=0; x<game_size; x++)
    {
        for(unsigned int y=0; y<game_size; y++)
        {
            CQiZi* qz = GetQiZi(x,y);
            if(qz->GetSide()==side && qz->isDead()==TRUE)
            {
                num++;
                qz->setDead(FALSE);
                buffer_append_long(b,x);
                buffer_append_string(b,",");
                buffer_append_long(b,y);
                buffer_append_string(b,",");
            }
        }
    }
    return num;
}


void CWeiQiModel::ClearDragons()
{
    unsigned int max_dragon=game_size*game_size;
    for(unsigned int i=0; i<max_dragon; i++)
    {
        vector_dragon[i]->ClearDragon();
    }
    for(unsigned int i=0; i<max_dragon; i++)
    {
        vector_qizi[i]->SetDragon(0);
    }
}


CDragon*  CWeiQiModel::GetDragon(unsigned int dragon_index)
{
    return vector_dragon[dragon_index];
}

int CLogicGo::dump(buffer* b)
{
    for(unsigned int y=0; y<game_size; y++)
    {
        for(unsigned int x=0; x<game_size; x++)
        {
            if(wqModel->GetSide(x,y)==WEIQI_SIDE_NULL)
                buffer_append_string(b," .");
            else if(wqModel->GetSide(x,y)==WEIQI_SIDE_WHITE)
                buffer_append_string(b," w");
            else if(wqModel->GetSide(x,y)==WEIQI_SIDE_BLACK)
                buffer_append_string(b," b");
        }
        buffer_append_string(b,"\r\n");
    }
}
int CLogicGo::dumpToHex(buffer* b)
{
    unsigned char* mmap = wqModel->mmap;
    int num = (game_size*game_size*2)/8+1;
    char c[3];
    int t;
    int i;

    buffer_append_long(b,num);
    buffer_append_string(b,",");
    for(i=0; i<num; i++)
    {
        c[2]=0;
        if(mmap[i]<=0xF)
        {
            c[0]='0';
            if(mmap[i]<=9) c[1]='0'+mmap[i];
            else c[1]='A'+(mmap[i]-10);
        }
        else
        {
            t=(mmap[i])&0x0F;
            if(t<=9) c[1]='0'+t;
            else c[1]='A'+(t-10);
            t=((mmap[i])&0xF0)>>4;
            if(t<=9) c[0]='0'+t;
            else c[0]='A'+(t-10);
        }
        buffer_append_string(b,c);
    }
}
int CLogicGo::getGoGameResult(int* bmu,int* wmu)
{
    *bmu=mBlackMu;
    *wmu=mWhiteMu;
    return 0;
}
int CLogicGo::getDianMuResult(int* bmu,int* wmu,buffer* b){
	int bStone=0;
	int wStone=0;
	return getDianMuResult(bmu,wmu,&bStone,&wStone,b);
}
int CLogicGo::getDianMuResult(int* bmu,int* wmu,int* bstone,int* wstone,buffer* b)
{
    CDragon *bl,*wh,*g;
    int bb,ww;
    int isOK=0;
    bl=wh=NULL;
    bb=ww=0;
    wqModel->CalNULLDragons(bl,wh,isOK);
    g=bl;
    *bmu=*wmu=0;
    if(g!=NULL)
    {
        *bmu=g->GetSize();
        buffer_append_long(b,1);
        buffer_append_string(b,",");
        buffer_append_long(b,*bmu);
        buffer_append_string(b,",");
        for(unsigned int x=0; x<game_size; x++)
        {
            for(unsigned int y=0; y<game_size; y++)
            {
                if(g->IsInDragon(x,y))
                {
                    buffer_append_long(b,x);
                    buffer_append_string(b,",");
                    buffer_append_long(b,y);
                    buffer_append_string(b,",");
                }
            }
        }
        wqModel->delDragon(g);
    }

    g=wh;
    if(g!=NULL)
    {
        *wmu=g->GetSize();
        buffer_append_long(b,2);
        buffer_append_string(b,",");
        buffer_append_long(b,*wmu);
        buffer_append_string(b,",");
        for(unsigned int x=0; x<game_size; x++)
        {
            for(unsigned int y=0; y<game_size; y++)
            {
                if(g->IsInDragon(x,y))
                {
                    buffer_append_long(b,x);
                    buffer_append_string(b,",");
                    buffer_append_long(b,y);
                    buffer_append_string(b,",");
                }
            }
        }
        wqModel->delDragon(g);
    }

    for(unsigned int y=0; y<game_size; y++)
    {
        for(unsigned int x=0; x<game_size; x++)
        {
            if(wqModel->isDead(x,y)==TRUE) continue;
            if(wqModel->GetSide(x,y)==WEIQI_SIDE_WHITE)
                ww++;
            else if(wqModel->GetSide(x,y)==WEIQI_SIDE_BLACK)
                bb++;
        }
    }
    mWhiteKongNum=*wmu;
    mBlackKongNum=*bmu;
    mBlackMu=
        mBlackKongNum
        +mWhiteDeadNum
        -mBlackTieMu;
    mWhiteMu=
        mWhiteKongNum+
        +mBlackDeadNum;

    mBlackMu=mBlackKongNum+bb;
    mWhiteMu=mWhiteKongNum+ww;

    buffer_append_long(b,mBlackMu);
    buffer_append_string(b,",");
    buffer_append_long(b,mWhiteMu);
    buffer_append_string(b,",");
	*bstone=mBlackMu;
	*wstone=mWhiteMu;
    return isOK;

}
CLogicGo::CLogicGo()
{
    mWinner=WEIQI_SIDE_NULL;
    m_bServerMode=TRUE;
    wqModel=NULL;
    mKilled=NULL;
    mWhiteNum=0;
    mBlackNum=0;
    mWhiteDeadNum=0;
    mBlackDeadNum=0;
    mBlackKongNum=0;
    mWhiteKongNum=0;
    mWhitePassSteps=0;
    mBlackPassSteps=0;
    mBlackTieMu=3;
    mBlackMu=0;
    mWhiteMu=0;
    mKilledNumber=0;
    mLastStep.x=22;
    mLastStep.y=22;
	mStepNum=0;
	mStepsRecord=buffer_init();
	mStepKilled=NULL;
};
CLogicGo::~CLogicGo()
{
	buffer_free(mStepsRecord);
    if(wqModel!=NULL)
    {
        delete wqModel;
    }
}

void CLogicGo::SetSize(int size)
{
    printf("%s enter\n",__func__);
    game_size=size;
    wqModel=new CWeiQiModel(size);
}

void CLogicGo::SetServerMode(BOOL bServerMode)
{
    m_bServerMode=bServerMode;
}

void CLogicGo::SetCallBack(CGoCallBackInterface* pCallBack)
{
    m_pCallBack=pCallBack;
}

void CLogicGo::OnNextTurn(int side)
{
    if(side==WEIQI_SIDE_BLACK)
    {
        this->m_nCurSide=WEIQI_SIDE_WHITE;
    }
    else
    {
        this->m_nCurSide=WEIQI_SIDE_BLACK;
    }
}

//Dragon only killed during server mode true.
//dragon will killed after receive the dragon killed commond during client mode.
void CLogicGo::OnKillDragon(CDragon* pDragon)
{
    bNoKill=FALSE;
    if(m_bServerMode==TRUE)
    {
        wqModel->KillDragon(pDragon);
        if(m_pCallBack!=NULL)
        {
            m_pCallBack->OnDragonKilled(pDragon);
        }

        if(pDragon->GetSide()==WEIQI_SIDE_WHITE)
        {
            this->mWhiteDeadNum-=pDragon->GetSize();
        }
        else
        {
            this->mBlackDeadNum-=pDragon->GetSize();
        }
        pDragon->ClearDragon();
    }
}

unsigned int CLogicGo::GetGameSize()
{
    return game_size;
}

void CLogicGo::StartGame()
{
    last_dj_x=-1;
    last_dj_y=-1;
    mStepCount=0; 
    OnNextTurn(WEIQI_SIDE_WHITE);
}

void CLogicGo::MoveNoThink(unsigned int x,unsigned int y,WEIQI_SIDE_T side)
{
    wqModel->SetSide(x,y,side);
}

void CLogicGo::Kill(unsigned int x,unsigned int y)
{
    wqModel->Kill(x,y);
}

void CLogicGo::AddKilledDragon(CDragon* g)
{
	if(mStepKilled==NULL){
		mStepKilled=buffer_init();
	}
    if(mKilled==NULL)
    {
        mKilledNumber=0;
        int len = g->GetSize();
        mKilled=buffer_init();
        len*=8;
        len+=2;
        buffer_prepare_copy(mKilled,len);
        buffer_append_long(mKilled,g->GetSide());
        buffer_append_string(mKilled,",");
    }
	char s[4];	

    for(unsigned int x=0; x<game_size; x++)
    {
        for(unsigned int y=0; y<game_size; y++)
        {
            if(g->IsInDragon(x,y))
            {
                mKilledNumber++;
                buffer_append_long(mKilled,x);
                buffer_append_string(mKilled,",");
                buffer_append_long(mKilled,y);
                buffer_append_string(mKilled,",");
				
				memset(s,0,sizeof(s));
				sprintf(s,"%c%c ",'A'+x,'A'+y);
				buffer_append_string(mStepKilled,s);
				
                Kill(x,y);				
            }
        }
    }
}

void CLogicGo::recordStep(WEIQI_SIDE_T side,unsigned int x,unsigned int y){
	char s[6];	
	memset(s,0,sizeof(s));
	mStepCount++;
	if(side == WEIQI_SIDE_WHITE){
		sprintf(s,"W:%c%c ",'a'+x,'a'+y);
	}else
		sprintf(s,"B:%c%c ",'a'+x,'a'+y);
	buffer_append_string(mStepsRecord,(char*)s);
	if(mStepKilled!=NULL){
		buffer_append_string(mStepsRecord,mStepKilled->ptr);
		buffer_free(mStepKilled);
		mStepKilled=NULL;
	}
	buffer_append_string(mStepsRecord,";");
}

BOOL CLogicGo::setDead(unsigned int x,unsigned int y,WEIQI_SIDE_T side)
{
    CQiZi* qz = wqModel->GetQiZi(x,y);
    //Can not set opponent's stone as dead.
    if(qz->GetSide()!=side) return FALSE;
    qz->setDead(TRUE);
    return TRUE;
}
int CLogicGo::dumpDeads(buffer* b)
{
    return wqModel->dumpDeads(b);
}
int CLogicGo::undoDead(WEIQI_SIDE_T side,buffer* b)
{
    return wqModel->clearDeads(side,b);
}
BOOL CLogicGo::Move(unsigned int x,unsigned int y,WEIQI_SIDE_T side)
{
    int bNewDragon = 0;
    int bNotAllowed = 0;
    int bDaJie=0;
    if(mKilled!=NULL)
    {
        mKilledNumber = 0;
        buffer_free(mKilled);
        mKilled=NULL;
    }
    if(wqModel->IsNull(x,y)==TRUE)
    {
        do
        {
            CDragon* myDragon=NULL;
            CDragon* otherDragons[4];
            CDragon* myDragons[4];
            CDragon* nearDragon;
            int others_num=0;
            int my_num=0;
            nearDragon = wqModel->GetLeftDragon(x,y);
            if(nearDragon!=NULL)
            {
                if(nearDragon->GetSide()==side)
                {
                    myDragons[my_num] = nearDragon;
                    my_num++;
                }
                else
                {
                    otherDragons[others_num] = nearDragon;
                    others_num++;
                }
            }

            nearDragon = wqModel->GetRightDragon(x,y);
            if(nearDragon!=NULL)
            {
                if(nearDragon->GetSide()==side)
                {
                    int bdup=0;
                    if(my_num>0)
                    {
                        for(int k=0; k<my_num; k++)
                        {
                            if(myDragons[k]==nearDragon)
                            {
                                bdup=1;
                                break;
                            }
                        }
                    }
                    if (bdup == 0)
                    {
                        myDragons[my_num] = nearDragon;
                        my_num++;
                    }
                }
                else
                {
                    int bdup=0;
                    if(others_num>0)
                    {
                        for(int k=0; k<others_num; k++)
                        {
                            if(otherDragons[k]==nearDragon)
                            {
                                bdup=1;
                                break;
                            }
                        }
                    }
                    if (bdup == 0)
                    {
                        otherDragons[others_num] = nearDragon;
                        others_num++;
                    }
                }
            }

            nearDragon = wqModel->GetDownDragon(x,y);
            if(nearDragon!=NULL)
            {
                if(nearDragon->GetSide()==side)
                {
                    int bdup=0;
                    if(my_num>0)
                    {
                        for(int k=0; k<my_num; k++)
                        {
                            if(myDragons[k]==nearDragon)
                            {
                                bdup=1;
                                break;
                            }
                        }
                    }
                    if (bdup == 0)
                    {
                        myDragons[my_num] = nearDragon;
                        my_num++;
                    }
                }
                else
                {
                    int bdup=0;
                    if(others_num>0)
                    {
                        for(int k=0; k<others_num; k++)
                        {
                            if(otherDragons[k]==nearDragon)
                            {
                                bdup=1;
                                break;
                            }
                        }
                    }
                    if (bdup == 0)
                    {
                        otherDragons[others_num] = nearDragon;
                        others_num++;
                    }
                }
            }

            nearDragon = wqModel->GetUpDragon(x,y);
            if(nearDragon!=NULL)
            {
                if(nearDragon->GetSide()==side)
                {
                    int bdup=0;
                    if(my_num>0)
                    {
                        for(int k=0; k<my_num; k++)
                        {
                            if(myDragons[k]==nearDragon)
                            {
                                bdup=1;
                                break;
                            }
                        }
                    }
                    if (bdup == 0)
                    {
                        myDragons[my_num] = nearDragon;
                        my_num++;
                    }
                }
                else
                {
                    int bdup=0;
                    if(others_num>0)
                    {
                        for(int k=0; k<others_num; k++)
                        {
                            if(otherDragons[k]==nearDragon)
                            {
                                bdup=1;
                                break;
                            }
                        }
                    }
                    if (bdup == 0)
                    {
                        otherDragons[others_num] = nearDragon;
                        others_num++;
                    }
                }
            }

            //temply add the step into model
            wqModel->SetSide(x,y,side);
            if(my_num==0)
            {
                myDragon = wqModel->newDragon();
                myDragon->AddIntoDragon(x,y);
                wqModel->SetSide(x,y,side);
                wqModel->GetQiZi(x,y)->SetDragon(myDragon->GetIndex());
                myDragon->SetSide(side);
                bNewDragon=TRUE;
            }
            else
            {
                myDragon = wqModel->GroupDragons(myDragons,my_num);
                myDragon->AddIntoDragon(x,y);
                myDragon->SetSide(side);
            }

            int myqi=0;
            CChessQi qi1(game_size);
            wqModel->GetDragonQi(myDragon,qi1);
            myqi=qi1.ShuQi();

            others_num--;
            int bKill=0;
            while(others_num>=0)
            {
                CDragon* otherDragon = otherDragons[others_num];
                others_num--;
                if(otherDragon!=NULL)
                {
                    CChessQi qi(game_size);
                    CDragon* g = otherDragon;
                    wqModel->GetDragonQi(otherDragon,qi);
                    if(qi.ShuQi()==0)
                    {
                        if(g->GetSize()!=1)
                        {
                            AddKilledDragon(g);
                            wqModel->delDragon(g);
                            bKill=1;
                        }
                        else
                        {
                            //Handle the DaJie
                            g->GetJieZi(cur_dj_x,cur_dj_y);
                            if(cur_dj_x==last_dj_x && cur_dj_y==last_dj_y)
                            {
                                bNotAllowed=1;
                                break;
                            }
                            else
                            {
                                bDaJie=1;
                                last_dj_x=x;
                                last_dj_y=y;
                                AddKilledDragon(g);
                                wqModel->delDragon(g);
                                bKill=1;
                            }
                        }
                    }
                }
            }
            if(bNotAllowed==1)
            {
                wqModel->GetQiZi(x,y)->SetDragon(0);
                wqModel->SetSide(x,y,0);
                wqModel->delDragon(myDragon);
                break;
            }
            if(bKill==0 && myqi==0)
            {
                wqModel->GetQiZi(x,y)->SetDragon(0);
                wqModel->SetSide(x,y,0);
                wqModel->delDragon(myDragon);
                break;
            }
            if(my_num>0)
            {
                for(int i=0; i<my_num; i++)
                {
                    CDragon* g = myDragons[i];
                    wqModel->delDragon(g);
                }
                wqModel->AssignIndex(myDragon);
            }
            if(side == WEIQI_SIDE_WHITE)
            {
                this->mWhiteNum++;
            }
            else
            {
                this->mBlackNum++;
            }
            if(bDaJie==0)
            {
                last_dj_x=-1;
                last_dj_y=-1;
            }
			recordStep(side,x,y);
            OnNextTurn(side);
			
            return TRUE;
        }
        while(0);
    }
    return FALSE;
};




BOOL CLogicGo::UserPass(WEIQI_SIDE_T side)
{
    if(m_nCurSide==side)
    {
        if(WEIQI_SIDE_BLACK==side)
        {
            this->mBlackPassSteps++;
			buffer_append_string(mStepsRecord,"B:00 ;");
            OnNextTurn(WEIQI_SIDE_BLACK);
        }
        else
        {
            this->mWhitePassSteps++;
			buffer_append_string(mStepsRecord,"W:00 ;");
            OnNextTurn(WEIQI_SIDE_WHITE);
        }
        return TRUE;
    }
    return FALSE;

}
