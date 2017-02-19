package com.sureone;

public class MyMsg {
    public byte[] buf=null;
    public int len=0;
    public int offset=0;
    public MyMsg(byte[] b,int l, int o) {
        buf=b;
        len=l;
        offset=o;
    }
    public MyMsg(int xx,int yy) {
        x=xx;
        y=yy;
    }
    public int x=-1;
    public int y=-1;
}
