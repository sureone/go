package com.sureone;

public class xBuffer {
    public byte[] mbuffer;
    public int mSize;
    public x_Integer offset;
    public xBuffer() {
        mbuffer = null;
        mSize=0;
    }

    public xBuffer(byte[] b,int len) {
        mbuffer = b;
        mSize=len;
    }
    public xBuffer(int size) {
        mbuffer = new byte[size];
        mSize=size;
    }
    public void initBuff(byte[] b,int len) {
        mbuffer = new byte[len];
        mSize=len;
        System.arraycopy(mbuffer, 0, b, 0,len);
    }
    public void append(byte[] b,int len) {
        byte[] bb = mbuffer;
        mbuffer = new byte[mSize+len];
        System.arraycopy(mbuffer, 0, bb, 0,mSize);
        System.arraycopy(mbuffer, mSize, b, 0,len);
        mSize+=len;
    }
    public byte[] getBuffer() {
        return mbuffer;
    }


    public String toString() {
        String str=null;
        try {
            str = new String(mbuffer,"GB2312");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
