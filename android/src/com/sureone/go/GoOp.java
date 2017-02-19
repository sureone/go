package com.sureone.go;

public class GoOp {
    public static final int SET=0;
    public static final int KILL=1;
    public int x=-1;
    public int y=-1;
    public int sd=0;
    public int op=0;
    public GoOp(int t,int s,int x,int y) {
        this.x=x;
        this.y=y;
        sd=s;
        op=t;
    }
}
