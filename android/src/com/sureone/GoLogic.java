package com.sureone;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.util.Log;
public class GoLogic {
    int[] mXY;
    int mGameSize;
    int mWhiteKilled=0;
    int mBlackKilled=0;
    String mSgfs="";
    SgfHeader mH;
    int mSteps=0;
    public int mLastX=-1;
    public int mLastY=-1;

    public GoLogic() {
        mXY=null;
        mGameSize=0;
        mH=new SgfHeader();
        mH.RE="NA";
        mH.WR="NA";
        mH.BR="NA";
        mH.PB="NA";
        mH.PW="NA";
        mH.EV="Online Game";
    }

    public void setGameSize(int sz) {
        mGameSize=sz;
        mXY=new int[sz*sz];
        for(int i=0 ; i< sz*sz ; i++) {
            mXY[i]=0;
        }
    }
    int[] mSavedXY=null;
    public void saveBoard() {
        int sz = mXY.length;
        mSavedXY=new int[sz];
        for(int i=0 ; i< sz ; i++) {
            mSavedXY[i]=mXY[i];
        }
    }
    public void restoreBoard() {
        int sz = mXY.length;
        if(mSavedXY==null) return;
        for(int i=0 ; i< sz ; i++) {
            mXY[i]=mSavedXY[i];
        }
    }

    void setSgfHeader(String key,String v) {
		xHelper.log(key+"="+v);
        if(key.compareTo("PB")==0)
            mH.PB=v;
        else if(key.compareTo("PW")==0)
            mH.PW=v;
        else if(key.compareTo("EV")==0)
            mH.EV=v;
        else if(key.compareTo("DT")==0)
            mH.DT=v;
        else if(key.compareTo("RE")==0)
            mH.RE=v;
        else if(key.compareTo("SZ")==0)
            mH.SZ=v;
        else if(key.compareTo("BR")==0)
            mH.BR=v;
        else if(key.compareTo("WR")==0)
            mH.WR=v;
    }
	
	String getSgfHeader(String key) {
        if(key.compareTo("PB")==0)
            return mH.PB;
        else if(key.compareTo("PW")==0)
            return mH.PW;
        else if(key.compareTo("EV")==0)
            return mH.EV;
        else if(key.compareTo("DT")==0)
            return mH.DT;
        else if(key.compareTo("RE")==0)
            return mH.RE;
        else if(key.compareTo("SZ")==0)
            return mH.SZ;
        else if(key.compareTo("BR")==0)
            return mH.BR;
        else if(key.compareTo("WR")==0)
            return mH.WR;
		return "";
    }
    int getGameSize() {
        return mGameSize;
    }


    public int getSide(int x,int y) {
        int index = x+y*mGameSize;
        int v = mXY[index];
        int side = v&3;
        return side;
    }

    public int getStepNo(int x,int y) {
        int index = x+y*mGameSize;
        int v = mXY[index];
        int side = v>>2;
		//xHelper.log("goapp","getStepNo=("+x+","+y+":"+side+")");
        return side;
    }

    public void setStepNo(int no,int x,int y) {
        int index = x+y*mGameSize;
        int v = mXY[index];
		//xHelper.log("goapp","setStepNo=("+x+","+y+":"+no+")");
        //clear the No.
        v&=3;
        no=no<<2;
        v|=no;
        mXY[index]=v;
    }
    public void setSide(int side,int x,int y) {
        if(getSide(x,y)==side) {
            return;
        }
        if(side!=0) {
            mLastX=x;
            mLastY=y;
        }
        int index = x+y*mGameSize;
        int v = mXY[index];
        v&=~(3);
        v|=side;
        mXY[index] = v;
    }
    void kill(int side,int x,int y) {
        if(side==1) {
            this.mBlackKilled++;
        } else
            this.mWhiteKilled++;
        setSide(0,x,y);

    }


    void kill(int x,int y) {

        setSide(0,x,y);

    }
    public void setHandicap(int h) {
        switch (h) {
        case 1:
            setSide(1,15,15);
            break;
        case 2:
            setSide(1,15,15);
            setSide(1,3,3);
            break;
        case 3:
            setSide(1,15,15);
            setSide(1,3,3);
            setSide(1,15,3);
            break;
        case 4:
            setSide(1,15,15);
            setSide(1,3,3);
            setSide(1,15,3);
            setSide(1,3,15);
            break;
        case 5:
            setSide(1,15,15);
            setSide(1,3,3);
            setSide(1,15,3);
            setSide(1,3,15);
            setSide(1,9,9);
            break;
        case 6:
            setSide(1,15,15);
            setSide(1,3,3);
            setSide(1,15,3);
            setSide(1,3,15);
            setSide(1,3,9);
            setSide(1,15,9);
            break;
        case 7:
            setSide(1,15,15);
            setSide(1,3,3);
            setSide(1,15,3);
            setSide(1,3,15);
            setSide(1,3,9);
            setSide(1,15,9);
            setSide(1,9,9);
            break;
        case 8:
            setSide(1,3,3);
            setSide(1,3,15);
            setSide(1,3,9);
            setSide(1,15,9);
            setSide(1,15,3);
            setSide(1,9,9);
            setSide(1,9,15);
            setSide(1,9,3);
            break;
        case 9:
            setSide(1,15,15);
            setSide(1,3,3);
            setSide(1,3,15);
            setSide(1,3,9);
            setSide(1,15,9);
            setSide(1,15,3);
            setSide(1,9,9);
            setSide(1,9,15);
            setSide(1,9,3);
            break;
        }
    }
    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

		
    public String genSgf(String sr) {
		if(sr==null) return null;
		x_Integer o = new x_Integer(0);
		//7,B:FG ;W:GG ;B:GH ;W:FH ;B:HG ;W:HH ;B:GF GG ;
		byte[] buf = sr.getBytes();
		int len = buf.length;
        int steps = xHelper.getInt(buf, len, o, ',');
		String ss="";
		for(int i=0;i<steps;i++){			
			String step = xHelper.getStr(buf,len,o,';');
			String cmd = xHelper.getStr(step,':',' ');
			if(cmd.equals("00")) continue;
			int x,y;
			x=cmd.charAt(0)-'a';
			y=cmd.charAt(1)-'a';
			setStepNo(i+1,x,y);
			if(step.charAt(0)=='B'){				
				ss+=";B["+cmd+"]";
			}else if(step.charAt(0)=='W'){
				ss+=";W["+cmd+"]";
			}
		}
        int bmu = xHelper.getInt(buf, len, o, ',');
        int wmu = xHelper.getInt(buf, len, o, ',');
        int uid = xHelper.getInt(buf, len, o, ',');
		String rank=xHelper.getStr(buf,len,o,',');		
        String s="(US[http://www.xdroid.us]SZ[19]EV["+mH.EV+"]"+
                 "DT["+getDate()+"]"+
                 "PB["+mH.PB+"]"+
                 "BR["+mH.BR+"]"+
                 "PW["+mH.PW+"]"+
                 "WR["+mH.WR+"]"+
                 "RE["+mH.RE+"]"+ss+")";
        return s;
    }

    public void clear() {
        mWhiteKilled=0;
        mBlackKilled=0;
        mSteps=0;
        mSgfs="";
        mH.RE="NA";
        mH.WR="NA";
        mH.BR="NA";
        mH.PB="NA";
        mH.PW="NA";
        mH.EV="Online Game";
        for(int i=0 ; i< mGameSize*mGameSize ; i++) {
            mXY[i]=0;
        }
    }
}
