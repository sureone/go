package com.sureone.go;

public class CDragon {
    int side;
    int size;
    byte[] data=null;
    int data_len;
    int dragon_size;
    int x1;
    int y1;
    int m_nIndex;
    public CDragon() {
        side=0;
    }
    public CDragon(int s,byte[] pData,int sz) {
        size=sz;
        side=s;
        int num_int=(sz*sz/8)+1;
        data=new byte[num_int];
        for(int i=0; i<num_int; i++) {
            data[i]=pData[i];
        }
        dragon_size=0;
        data_len=num_int;
    }
    public CDragon(int s, int sz) {
        size=sz;
        side=s;
        int num_int=(sz*sz/8)+1;
        data= new byte[num_int];
        for(int i=0; i<num_int; i++) {
            data[i]=0;
        }

        dragon_size=0;
        data_len=num_int;
    }

    public void SetSide(int sd) {
        side=sd;
    }
    public void GetJieZi(XY xy) {
        xy.x=x1;
        xy.y=y1;
    }
    public boolean IsInDragon(int x,int y) {
        int index=y*size+x;
        int no_int=index/8;
        int no_bit=index%8;
        int value=data[no_int];
        if((value&(1<<no_bit))>0) return true;
        return false;
    }
    public void ClearDragon() {
        int num_int=(size*size/8)+1;
        for(int i=0; i<num_int; i++) {
            data[i]=0;
        }
        dragon_size=0;
    }
    public int GetSide() {
        return side;
    }
    public void AddIntoDragon(int x,int y) {
        x1=x;
        y1=y;
        dragon_size++;
        int index=y*size+x;
        int no_int=index/8;
        int no_bit=index%8;
        byte value=data[no_int];
        value |= (1<<no_bit);
        data[no_int]=value;
    }

    public void removeFromDragon(int x,int y) {
        x1=x;
        y1=y;
        dragon_size--;
        int index=y*size+x;
        int no_int=index/8;
        int no_bit=index%8;
        byte value=data[no_int];
        value &= ~(1<<no_bit);
        data[no_int]=value;
    }
    public int GetSize() {
        return dragon_size;
    }
    public int GetIndex() {
        return m_nIndex;
    }
    public void SetIndex(int i) {
        m_nIndex=i;
    }

    public void setNullSide(int sd) {
        if((side!=0 && side!=sd) || (side==3)) side = 3;
        else
            side = sd;
    }
}
