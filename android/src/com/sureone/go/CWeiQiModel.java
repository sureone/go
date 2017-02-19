package com.sureone.go;

import java.util.LinkedList;

public class CWeiQiModel {
    int game_size;
    CQiZi[] vector_qizi=null;
    CDragon[] vector_dragon=null;
    public CWeiQiModel(int sz) {
        game_size=sz;
        int vector_size=sz*sz;

        vector_qizi=new CQiZi[vector_size];
        vector_dragon=new CDragon[vector_size];
        for(int i=0; i<vector_size; i++) {
            vector_qizi[i]=new CQiZi();
            vector_dragon[i]=null;

        }
    }

    public int GetQiZiIndex(int x,int y) {
        return (y*game_size+x);
    }


    public CQiZi GetQiZi(int x,int y) {
        CQiZi qz= vector_qizi[GetQiZiIndex(x,y)];
        return qz;
    }
    public void SetSide(int x,int y,int side) {
        GetQiZi(x,y).SetSide(side);
    }

    public void Clear(int x, int y) {
        GetQiZi(x,y).clear();
    }

    int GetSide(int x,int y) {
        CQiZi pQiZi = GetQiZi(x,y);
        return pQiZi.GetSide();
    }

    public boolean IsNull( int x, int y) {
        if(GetSide(x,y)==0) return true;
        return false;
    }

    public boolean IsDragoned( int x,  int y) {
        if(GetQiZi(x,y).GetDragonIndex()!=0) return true;
        return false;
    }
    public void SetDragon( int x, int y, int index) {
        GetQiZi(x,y).SetDragon(index);
    }

    public  CDragon GetItsDragon( int x, int y) {
        int dragon_index = GetQiZi(x,y).GetDragonIndex();
        if(dragon_index==0) return null;
        return vector_dragon[dragon_index-1];
    }
    public CDragon GetLeftDragon(int x,int y) {
        CDragon near = null;
        XY xy = new XY();
        if(Left(x,y,xy)==true) {

            near = GetItsDragon(xy.x,xy.y);
        }
        return near;
    }
    public  CDragon GetRightDragon(int x,int y) {
        CDragon near = null;
        XY xy = new XY();
        if(Right(x,y,xy)==true) {

            near = GetItsDragon(xy.x,xy.y);
        }
        return near;
    }
    public  CDragon GetUpDragon(int x,int y) {
        CDragon near = null;
        XY xy = new XY();
        if(Up(x,y,xy)==true) {
            near = GetItsDragon(xy.x,xy.y);
        }
        return near;
    }
    public  CDragon GetDownDragon(int x,int y) {
        CDragon near = null;
        XY xy = new XY();
        if(Down(x,y,xy)==true) {
            near = GetItsDragon(xy.x,xy.y);
        }
        return near;
    }

    public boolean Up(int x,int y, XY xy) {
        xy.x=x;
        xy.y=y-1;
        if(y==0) return false;
        return true;
    }

    public boolean Down(int x,int y, XY xy) {
        xy.x=x;
        xy.y=y+1;
        if(xy.y==game_size) return false;
        return true;
    }

    public boolean Left(int x,int y, XY xy) {
        xy.x=x-1;
        xy.y=y;
        if(x==0) return false;
        return true;
    }

    public boolean Right(int x,int y,XY xy) {
        xy.x=x+1;
        xy.y=y;
        if(xy.x==game_size) return false;
        return true;
    }

    public int GetItsQi(int x,int y) {
        XY xy=new XY();
        int qi=0;

        if(Up(x,y,xy)==true) {
            if(xy.x!=-1 && IsNull(xy.x,xy.y)==true) qi|=1;
        }

        if(Down(x,y,xy)==true) {
            if(xy.x!=-1 && IsNull(xy.x,xy.y)==true) qi|=4;
        }

        if(Left(x,y,xy)==true) {
            if(xy.x!=-1 && IsNull(xy.x,xy.y)==true) qi|=8;
        }

        if(Right(x,y,xy)==true) {
            if(xy.x!=-1 && IsNull(xy.x,xy.y)==true) qi|=2;
        }

        return qi;

    }

    public boolean CompareSide(int x,int y,int xx,int yy) {
        int s1=GetSide(x,y);
        int s2=GetSide(xx,yy);
        if(s1==s2 && s1!=0) {
            return true;
        }
        return false;
    }
    public void SetAllFlag(int mask) {
        int sz = game_size*game_size;
        for(int i=0; i<sz; i++) {
            vector_qizi[i].SetFlag(mask);
        }

    }

    public void SetFlag(int x,int y,int mask) {
        GetQiZi(x,y).SetFlag(mask);

    }
    public boolean IsFlagSet(int x,int y,int mask) {
        return (GetQiZi(x,y).IsFlagSet(mask));
    }



    void MergeDragon(CDragon g1,CDragon g2) {

        int size = game_size;
        for(int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                if(g2.IsInDragon(x,y)) {
                    g1.AddIntoDragon(x,y);
                    SetDragon(x,y,g1.GetIndex());
                }
            }
        }

    }

    CDragon GroupDragons(CDragon[] gragons,int num) {
        CDragon newDragon = this.newDragon();
        int size = game_size;
        for(int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                for(int i=0; i<num; i++) {
                    CDragon g = gragons[i];
                    if(g.IsInDragon(x,y)) {
                        newDragon.AddIntoDragon(x,y);
                    }

                }
            }
        }
        return newDragon;
    }
    public void AssignIndex(CDragon g) {
        int size = game_size;
        for(int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                if(g.IsInDragon(x,y)) {
                    SetDragon(x,y,g.GetIndex());
                }
            }
        }

    }
    public void RecalDragons() {
        this.ClearDragons();
        int sz = game_size*game_size;
        for(int x=0; x<game_size; x++) {
            for(int y=0; y<game_size; y++) {
                if(IsNull(x,y)==true) continue;
                XY xy = new XY();
                CDragon g=null;
                if(Up(x,y,xy)==true) {
                    if(CompareSide(x,y,xy.x,xy.y)==true) {
                        g=GetItsDragon(xy.x,xy.y);
                    }
                }

                if(Left(x,y,xy)==true) {
                    if(CompareSide(x,y,xy.x,xy.y)==true) {
                        if(g!=null) {
                            CDragon g_other = GetItsDragon(xy.x,xy.y);
                            if(g_other!=g && g_other!=null) {
                                MergeDragon(g,g_other);
                                delDragon(g_other);
                            }
                        } else {
                            g=GetItsDragon(xy.x,xy.y);
                        }
                    }
                }

                if(g==null) {
                    g=this.newDragon();
                }
                g.SetSide(GetSide(x,y));
                g.AddIntoDragon(x,y);
                GetQiZi(x,y).SetDragon(g.GetIndex());
            }
        }
    }
    public int CalnullDragons(CDragonMu mu) {
        int isOK=1;
        LinkedList<CDragon> list_null_dragon=new LinkedList<CDragon>();
        int sz = game_size*game_size;
        for(int x=0; x<game_size; x++) {
            for(int y=0; y<game_size; y++) {
                //If there is not null and not a dead chess at the postion , then continue
                boolean bnull = IsNull(x,y);

                XY xy = new XY();
                CDragon g_left_null=null;
                CDragon g_up_null=null;
                if(Up(x,y,xy)==true) {
                    //If there is null or a dead chess in the position
                    if(xy.x!=-1 && IsNull(xy.x,xy.y)==true) {
                        g_up_null=GetItsDragon(xy.x,xy.y);
                    }
                }

                if(Left(x,y,xy)==true) {
                    //If there is null or a dead chess in the position
                    if(xy.x!=-1 && IsNull(xy.x,xy.y)==true) {
                        g_left_null = GetItsDragon(xy.x,xy.y);
                    }
                }

                if(bnull == false) {
                    //setup the side of nearby null dragon
                    if(g_up_null!=null)
                        g_up_null.setNullSide(GetSide(x,y));
                    if(g_left_null!=null)
                        g_left_null.setNullSide(GetSide(x,y));
                } else {
                    CDragon g=null;
                    if(g_up_null!=null) {
                        g=g_up_null;
                    }
                    if(g_left_null!=null) {
                        g=g_left_null;
                    }
                    if(g_up_null!=null
                            && g_left_null!=null
                            && g_up_null != g_left_null) {
                        g_up_null.setNullSide(g_left_null.GetSide());
                        MergeDragon(g_up_null,g_left_null);
                        g=g_up_null;
                        list_null_dragon.remove(g_left_null);
                        delDragon(g_left_null);
                        //printf("merge and remove dragon at %d,%d,%p\n",x,y,g_left_null);
                    }
                    if(g==null) {
                        g= newDragon();
                        //printf("add a new null dragon for %d,%d,%p\n",x,y,g);
                        list_null_dragon.add(g);
                    }
                    g.AddIntoDragon(x,y);
                    SetDragon(x,y,g.GetIndex());
                    if(Up(x,y,xy)==true) {
                        if(IsNull(xy.x,xy.y)==false) {
                            g.setNullSide(GetSide(xy.x,xy.y));
                        }
                    }
                    if(Left(x,y,xy)==true) {
                        if(IsNull(xy.x,xy.y)==false) {
                            g.setNullSide(GetSide(xy.x,xy.y));
                        }
                    }
                }
            }
        }

        //printf("null dragon num=%d\n",list_null_dragon.getSize());

        for(int n=0; n<list_null_dragon.size(); n++) {

            CDragon g = list_null_dragon.get(n);
            //printf("null dragon (%p)'s side=%d\n",g,g.GetSide());
            if(g.GetSide()!=3) {
                if(g.GetSide()==1) {
                    if(mu.pBlackMu==null)
                        mu.pBlackMu = g;
                    else {
                        MergeDragon(mu.pBlackMu,g);
                        delDragon(g);
                    }
                }
                if(g.GetSide()==2) {
                    if(mu.pWhiteMu==null)
                        mu.pWhiteMu = g;
                    else {
                        MergeDragon(mu.pWhiteMu,g);
                        delDragon(g);
                    }
                }
            } else
                delDragon(g);
        }
        list_null_dragon.clear();

        for(int x=0; x<game_size; x++) {
            for(int y=0; y<game_size; y++) {
                if(GetSide(x,y)==0) {
                    SetDragon(x,y,0);
                }
            }
        }

        return isOK;
    }

    public CChessQi GetDragonQi(CDragon dragon) {
        CChessQi qi = new CChessQi(game_size);
        for(int x=0; x<game_size; x++) {
            for(int y=0; y<game_size; y++) {

                XY xy = new XY();
                if(dragon.IsInDragon(x,y)==false) continue;

                if(Up(x,y,xy)==true) {
                    if(IsNull(xy.x,xy.y)==true) qi.AddQi(xy.x,xy.y);
                }
                if(Down(x,y,xy)==true) {
                    if(IsNull(xy.x,xy.y)==true) qi.AddQi(xy.x,xy.y);
                }
                if(Left(x,y,xy)==true) {
                    if(IsNull(xy.x,xy.y)==true) qi.AddQi(xy.x,xy.y);
                }
                if(Right(x,y,xy)==true) {
                    if(IsNull(xy.x,xy.y)==true) qi.AddQi(xy.x,xy.y);
                }

            }
        }
        return qi;
    }

    public int Kill(int x,int y) {
        int s = GetQiZi(x,y).GetSide();
        GetQiZi(x,y).clear();
        return s;
    }

    public void ClearDragons() {
        int max_dragon=game_size*game_size;
        for(int i=0; i<max_dragon; i++) {
            CDragon g = vector_dragon[i];
            if(g!=null) {
                vector_dragon[i]=null;
            }
        }
        for(int i=0; i<max_dragon; i++) {
            vector_qizi[i].SetDragon(0);
        }
    }

    public CDragon  GetDragon(int dragon_index) {
        return vector_dragon[dragon_index];
    }

    public CDragon  GetDragon(int x,int y) {
        int idx = GetQiZi(x,y).GetDragonIndex();
        return vector_dragon[idx];
    }

    public CDragon newDragon() {
        CDragon ng = new CDragon(0,game_size);
        int max_dragon=game_size*game_size;
        for(int i=0; i<max_dragon; i++) {
            CDragon g=vector_dragon[i];
            if(g==null) {
                vector_dragon[i]=ng;
                ng.m_nIndex=i+1;
                break;
            }
        }
        return ng;
    }

    public void delDragon(CDragon g) {
        vector_dragon[g.m_nIndex-1]=null;

    }

}
