package com.sureone.igs;
import com.sureone.*;
/*
7 [##]  white name [ rk ]      black name [ rk ] (Move size H Komi BY FR) (###)
7 [484]   Samurai55 [ 2d*] vs.        stgl [ 1d*] (159   19  0  0.5 15  I) (  0)
7 [537]   hiroshigo [ 3d*] vs.  niwanotoki [ 2d*] (310   19  0 -5.5 10  I) (  1)
7 [128]     DaVinci [10k*] vs.      KT1421 [10k*] (310   19  0  6.5 10  I) (  0)
*/
public class IgsGame {
    int id;
    String wName;
    String bName;
    String wrk;
    String brk;
    int wCaptured=0;
    int bCaptured=0;
    int wTimeLeft;
    int bTimeLeft;
    int wSeconds;
    int bSeconds;
    int move;
    int size=19;
    int H;
    String Komi;
    int BY;
    String FR;
    int obs; // Number of observer
    GoLogic mGoLogic = null;

    public IgsGame() {
        mGoLogic = new GoLogic();
    }
    void clear() {
        mGoLogic.clear();
    }

    GoLogic getGoLogic() {
        return mGoLogic;
    }
    void setSize(int sz) {
        mGoLogic.setGameSize(sz);
        size = sz;
    }
    void setSeconds(char c,int v) {
        if(c=='B') bSeconds = v;
        if(c=='W') wSeconds = v;
    }
    void setTimeLeft(char c,int v) {
        if(c=='B') bTimeLeft = v;
        if(c=='W') wTimeLeft = v;
    }
    void setCaptured(char c,int v) {
        if(c=='B') bCaptured = v;
        if(c=='W') wCaptured = v;
    }
    void setName(char c,String name) {
        if(c=='B') bName = name;
        if(c=='W') wName = name;
    }
    String getName(char c) {
        if(c=='B') return bName;
        if(c=='W') return wName;
        return null;
    }
    String getRank(char c) {
        if(c=='B') return brk;
        if(c=='W') return wrk;
        return null;
    }
    void setRank(char c,String v) {
        if(c=='B') brk = v;
        if(c=='W') wrk = v;
    }
    public void saveBoard() {
        mGoLogic.saveBoard();
    }
    public void restoreBoard() {
        mGoLogic.restoreBoard();
    }
    public void setMap(int x,int y,char c) {
        if(mGoLogic==null) return;
        if(c=='0' || c=='1') {
            mGoLogic.setSide(((int)(c-'0')+1),x,y);
        } else if(c=='2') {
            mGoLogic.setSide(0,x,y);
        }
    }
    public void move(char x,int y,char c) {
        if(x>='A' && x<='T' && y>=1 && y<=19) {
            int ix = (int)(x-'A');
            if(x>='J') ix--;
            y--;
            setMap(ix,y,c);
        }
    }
    public void capture(char x,int y) {
        if(x>='A' && x<='T' && y>=1 && y<=19) {
            int ix = (int)(x-'A');
            if(x>='J') ix--;
            y--;
            setMap(ix,y,'2');
        }
    }
    public void setHandicap(int h) {
        H = h;
        if(mGoLogic!=null) {
            mGoLogic.setHandicap(h);
        }
    }
    public void setGameNo(int gno) {
        id = gno;
    }
    public int getGameNo() {
        return id;
    }

};
