package com.sureone.go;

import java.util.ArrayList;

public class GoStep {
    ArrayList<GoOp> mOps=null;
    public GoStep() {
        mOps=new ArrayList<GoOp>();
    }

    public void AddOp(int t,int s,int x,int y) {
        GoOp o = new GoOp(t,s,x,y);
        mOps.add(o);
    }

    public GoOp getSetOp() {
        for(int i=0; i<mOps.size(); i++) {
            GoOp o=mOps.get(i);
            if(o.op==GoOp.SET)
                return o;
        }
        return null;
    }
}
