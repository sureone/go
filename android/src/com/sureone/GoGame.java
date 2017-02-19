package com.sureone;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.UnsupportedEncodingException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
public class GoGame {
    GoLogic mGoLogic=null;
	String mRecord=null;
    int mGameSize=19;
    int mCurTurn=0;
    int mLastX=-1;
    int mLastY=-1;

    public GoGame(int size) {
        mGameSize = size;
        mGoLogic = new GoLogic();
        mGoLogic.setGameSize(size);
    }
    public void setCurTurn(int t) {
        mCurTurn=t;
    }

    public int getCurTurn() {
        return mCurTurn;
    }
    public int getLastStepX() {
        return mLastX;
    }
    public int getLastStepY() {
        return mLastY;
    }
    public void setLastStep(int x,int y) {
        mLastX=x;
        mLastY=y;
    }
    public void kill(int sid,int x,int y) {
        mGoLogic.kill(sid,x,y);
    }

    public void kill(int x,int y) {
        mGoLogic.kill(x,y);
    }
    public void setStep(int sid,int x,int y) {
        mGoLogic.setSide(sid,x,y);
    }
    public void clean() {
        mLastX=-1;
        mLastY=-1;
        mCurTurn=0;
		mRecord=null;
        if(mGoLogic!=null) {			
            mGoLogic.clear();
            xHelper.log("goapp","clean gologic");
        }
    }
    public String genSgf() {
		if(mRecord==null) return null;
        return mGoLogic.genSgf(mRecord);
    }
	public void saveRecord(String s){
		mRecord = s;
	}
    public void loadmap(int num,String mmap) {
        char[] ss = null;
        int[] bb = null;
        int b=0;
        int j=0;
        if(num==0) return;
        ss = mmap.toCharArray();
        bb = new int[num];
        int size=mGoLogic.getGameSize();
        for(int i=0; i<num; i++) {
            b=xHelper.hexToByte(ss[j]);
            j++;
            b=(b<<4);
            b|=xHelper.hexToByte(ss[j]);
            bb[i]=b;
            j++;
        }
        for(int y=0; y<size; y++) {
            for(int x=0; x<size; x++) {
                int p1=((y*size+x)*2)/8;
                int p2=((y*size+x)*2)%8;
                int side=bb[p1];
                side &=((3<<p2));
                side = (side>>p2);
                mGoLogic.setSide(side,x,y);
            }
        }
    }

}
