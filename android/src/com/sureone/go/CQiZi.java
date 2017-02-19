package com.sureone.go;

public class CQiZi {
    public static final int SIDE_WHITE_MASK	=0x8000;
    public static final int  SIDE_BLACK_MASK=		0x4000;
    public static final int  GROUP_MASK		=		0x2000;
    public static final int  DIGUI_CHECK_MASK=	    0x1000;
    public static final int  GROUP_INDEX_MASK=	0x02FF;
    int value;
    public CQiZi() {
        value=0;
    }
    public void SetSide(int side) {
        if(side==1)	{
            value|=SIDE_BLACK_MASK;
        }
        if(side==2)	{
            value|=SIDE_WHITE_MASK;
        }
        if(side==0) {
            value&=~(SIDE_WHITE_MASK|SIDE_BLACK_MASK);
        }
    }
    public void clear() {
        value=0;
    }
    public void SetValue(int v) {
        value =v;
    }
    public void SetFlag( int flag_mask) {
        value|=flag_mask;
    }
    public void ClrFlag( int flag_mask) {
        value&=~flag_mask;
    }
    public boolean  IsFlagSet(int flag_mask) {
        return ((value&flag_mask)>0);
    }
    public int GetSide() {
        if(IsFlagSet(SIDE_BLACK_MASK)) {
            return 1;
        } else if(IsFlagSet(SIDE_WHITE_MASK)) {

            return 2;
        } else {
            return 0;
        }
    }
    public void SetDragon(int dragon_index) {
        value &= (~GROUP_INDEX_MASK);
        value |= (dragon_index & GROUP_INDEX_MASK);
    }
    public int GetDragonIndex() {
        return (value & GROUP_INDEX_MASK);
    }
}
