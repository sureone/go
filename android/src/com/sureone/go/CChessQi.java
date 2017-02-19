package com.sureone.go;

public class CChessQi {
    int size;
    byte[] data=null;
    public CChessQi() {

    }

    public CChessQi(int sz) {
        size=sz;
        int num_int=(sz*sz/8)+1;
        data = new byte[num_int];
        for(int i=0; i<num_int; i++)
            data[i]=0;
    }
    public boolean IsInDragon( int x, int y) {
        int index=y*size+x;
        int no_int=index/8;
        int no_bit=index%8;
        int value=data[no_int];
        if((value&(1<<no_bit))>0) return true;
        return false;
    }

    public void AddQi( int x, int y) {
        int index=y*size+x;
        int no_int=index/8;
        int no_bit=index%8;
        byte value=data[no_int];
        value |= (1<<no_bit);
        data[no_int]=value;

    }

    public boolean IsQi( int x, int y) {
        int index=y*size+x;
        int no_int=index/8;
        int no_bit=index%8;
        byte value=data[no_int];
        if((value&(1<<no_bit))!=0) return true;
        return false;
    }
    public int ShuQi() {
        int qi=0;
        for(int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                if(IsQi(x,y)==true) qi++;

            }
        }
        return qi;
    }
}
