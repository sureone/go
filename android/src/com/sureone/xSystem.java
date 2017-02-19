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
public class xSystem {
    public static final int PAGE_NULL = -1;
    public static final int PAGE_MAIN = 0;
    public static final int PAGE_DESKLIST = 1;
    public static final int PAGE_GO = 2;
    public static final int PAGE_NUM = 4;

    public static final int BACK_COLOR= 0xff000000+221*0x10000+188*0x100+107;
    public static final int FRONT_COLOR=0xff000000+174*0x10000+148*0x100+84;
    public static xTcpThread mConn=null;
    public static int mDeskNumber = 0;
    public static int mUserNumber = 0;
    public static Desk[] mDesks=null;
    public static ArrayList<User> mUsers=new ArrayList<User>();
    public static Desk mMyDesk=null;
    public static User mMyUser=null;

    public static GoLogic mGoLogic=null;
    public static int[] mPages = new int[PAGE_NUM];
    public static int mCurrentPage=PAGE_NULL;
    public static String mVersion = "0.1.3";
    public static boolean mVersionChecked = false;
    public static String mLatestVersion = null;
    public static boolean isOnGame=false;
    public static String mCurSgf=null;
    public static int mGameSize=19;
    public static int mCurTurn=0;
    public static boolean mObserverMode=false;
    private static final String sgf_table_create =
        "CREATE TABLE IF NOT EXISTS sgfs (" +
        "ID INTEGER NOT NULL, " +
        "EV TEXT, " +
        "RE TEXT, " +
        "PB TEXT, " +
        "BR TEXT, " +
        "PW TEXT, " +
        "WR TEXT," +
        "DATA TEXT,"+
        "isnew INTEGER);";
    private static final String max_id_table_create =
        "CREATE TABLE IF NOT EXISTS max_id (" +
        "ID INTEGER NOT NULL)";

    public static SQLiteDatabase mDB = null;
    public static java.util.ArrayList<SgfHeader> mLocalSgfs = null;
    public static void initDB(Context context) {
        mLocalSgfs = new java.util.ArrayList<SgfHeader>();

        if(mDB==null) {
            try {
                mDB=context.openOrCreateDatabase("goapp", Context.MODE_WORLD_WRITEABLE, null);
                mDB.execSQL(sgf_table_create);
                mDB.execSQL(max_id_table_create);
                String sql = "SELECT * from max_id";
                Cursor c = xSystem.mDB.rawQuery(sql,null);
                String data = null;
                if(c!=null) {
                    int didx=c.getColumnIndex("ID");
                    if(c.moveToFirst()) {
                        do {
                            data = c.getString(didx);
                        } while (c.moveToNext());
                    }
                }
                if(data!=null) {
                    mMaxId=Integer.parseInt(data);
                } else {
                    sql = "INSERT INTO max_id (ID)"+
                          "VALUES("+mMaxId+")";
                    mDB.execSQL(sql);
                }
                c.close();
            } catch (SQLiteException se ) {
                xHelper.log("goapp", "Could not create or Open the database");
            }
        }
    }
    public static int getMaxId() {
        mMaxId++;
        String sql = "UPDATE max_id set ID="+mMaxId;
        if(xSystem.mDB==null) return -1;
        mDB.execSQL(sql);
        return mMaxId;
    }
    public static int mMaxId=80000000;
    public static String getLocalSgf(int id) {
        if(xSystem.mDB==null) return null;
        String sql = "SELECT * from sgfs WHERE ID="+id;
        Cursor c = xSystem.mDB.rawQuery(sql,null);
        String data = null;
        if(c!=null) {
            int didx=c.getColumnIndex("DATA");
            if(c.moveToFirst()) {
                do {
                    data = c.getString(didx);
                } while (c.moveToNext());
            }

        }
        c.close();
        return data;
    }

    public static void updateSgfNew(SgfHeader h) {
        String sql = "UPDATE sgfs set isnew=0 WHERE ID="+ h.ID;
        if(xSystem.mDB==null) return;
        mDB.execSQL(sql);
    }
    public static void delSgf(int pos) {
        int idx = xSystem.mLocalSgfs.size()-pos-1;

        SgfHeader h = xSystem.mLocalSgfs.get(idx);

        String sql = "DELETE FROM sgfs WHERE ID="+ h.ID;
        if(xSystem.mDB==null) return;
        mDB.execSQL(sql);
        mLocalSgfs.remove(idx);
    }
    public static int queryDB() {
        mLocalSgfs.clear();
        if(xSystem.mDB==null) return 0;
        Cursor c = xSystem.mDB.rawQuery("select * from sgfs",null);
        if(c!=null) {
            int idIdx=c.getColumnIndex("ID");
            int evIdx=c.getColumnIndex("EV");
            int reIdx=c.getColumnIndex("RE");
            int pbIdx=c.getColumnIndex("PB");
            int brIdx=c.getColumnIndex("BR");
            int pwIdx=c.getColumnIndex("PW");
            int wrIdx=c.getColumnIndex("WR");
            int isnewIdx=c.getColumnIndex("isnew");
            if(c.moveToFirst()) {
                do {
                    SgfHeader h = new SgfHeader();

                    h.ID = c.getInt(idIdx);
                    h.EV = c.getString(evIdx);
                    h.RE = c.getString(reIdx);
                    h.PB = c.getString(pbIdx);
                    h.BR = c.getString(brIdx);
                    h.PW = c.getString(pwIdx);
                    h.WR = c.getString(wrIdx);
                    h.isnew = c.getInt(isnewIdx);
                    mLocalSgfs.add(h);
                } while (c.moveToNext());
            }

        }
        c.close();
        return mLocalSgfs.size();
    }

    public static void saveSgfToSD(int id,String sgf) {
        String state = Environment.getExternalStorageState();
        /*
            if (Environment.MEDIA_MOUNTED.equals(state))
            {
                 //SDcard is available
            	   String fn = "/sdcard"
                   File f=new File("/sdcard/test.txt");
                   if (!f.exists())
                   {
                    //File does not exists
                    f.createNewFile();
                   }

                  //take your inputstream and write it to your file

                  OutputStream out=new FileOutputStream(f);
                  byte buf[]=new byte[1024];
                  int len;
                  while((len=inputStream.read(buf))>0)
                  out.write(buf,0,len);
                  out.close();
                  inputStream.close();
                  System.out.println("\nFile is created...................................");


            }
            */



    }
    public static void saveSgfToFs(Context ctx,int id,String data) {
        //Toast.makeText(this, getFilesDir().toString(), Toast.LENGTH_LONG).show();
        String fn = "sgf"+id+".sgf";

        File newFile=new File(ctx.getFilesDir(), fn);

        BufferedWriter output;
        try {
            output = new BufferedWriter(new FileWriter(newFile));
            output.write(data);
            output.flush();
            output.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public static int saveSgf(Context ctx) {

        if(mGoLogic!=null && mObserverMode==false) {
            String s = mGoLogic.genSgf(null);
            int id = getMaxId();
            if(id>0) {
                saveSgfToFs(ctx,id,s);
                AddSgf(id,s);
            }
            return id;
        }
        return -1;
    }
    public static void AddSgf(int id,String sgf) {

        SgfParser parser = new SgfParser(sgf);
        xHelper.log("goapp","save sgf:"+sgf);
        if(parser.parseHeader()==true) {
            SgfHeader h = parser.mH;
            String sql = "INSERT INTO sgfs (ID,EV,RE,PB,BR,PW,WR,DATA,isnew)"+
                         "VALUES("+id+","+
                         "\""+h.EV+"\","+
                         "\""+h.RE+"\","+
                         "\""+h.PB+"\","+
                         "\""+h.BR+"\","+
                         "\""+h.PW+"\","+
                         "\""+h.WR+"\","+
                         "\"sgf"+id+""+".sgf\",1)";
            if(mDB!= null) {
                mDB.execSQL(sql);
                xHelper.log("goapp",sql);
            }
        }
    }

    public static xTcpThread getConnection() {
        if ( mConn == null) {
            mConn = new xTcpThread();

        }
        return mConn;
    }

    public static void clean() {
        if(mGoLogic!=null) {
            mGoLogic.clear();
            xHelper.log("goapp","clean gologic");
        }
    }

    //desk_number;did,desk_status,side_number,sid,uid,name,win/total,;...
    public static int parseDeskListData(byte[] buf, int len, int offset) {
        int ret = 0;
        x_Integer o = new x_Integer(offset);
        mDeskNumber = xHelper.getInt(buf, len, o, ';');
        if(mDesks==null)
            mDesks = new Desk[mDeskNumber];
        xHelper.log("xSystem","desknum="+mDeskNumber);
        mUserNumber=0;
        for(int i = 0 ; i < mDeskNumber ; i++) {
            Desk d = mDesks[i];
            if( d == null)
                d = new Desk();
            mDesks[i]=d;
            int did = xHelper.getInt(buf,len,o,',');
            int desk_status = xHelper.getInt(buf, len, o, ',');
            int bFound=0;
            int wFound=0;
            if(desk_status != Desk.DESK_NULL) {
                int side_num = xHelper.getInt(buf, len, o, ',');
                int j = 0;
                while(j<side_num) {

                    int sid = xHelper.getInt(buf, len, o, ',');
                    int uid = xHelper.getInt(buf, len, o, ',');
                    int status = xHelper.getInt(buf, len, o, ',');
                    String name = xHelper.getStr(buf, len, o, ',');
                    int wins = xHelper.getInt(buf, len, o, '/');
                    int totals = xHelper.getInt(buf, len, o, ',');
                    User u = getUser(uid);
                    if( u == null) {
                        u = new User();
                        mUsers.add(u);

                    }
                    mUserNumber++;
                    u.id = uid;
                    u.name = name;
                    u.wins = wins;
                    u.totals = totals;
                    u.did = d.id;
                    u.sid = sid;

                    if(sid==1) {
                        bFound=1;
                        if(d.black!=u) {
                            u.ref++;
                            if(d.black!=null) {
                                d.black.ref--;
                                if(d.black.ref==0) {
                                    removeUser(d.black);
                                }
                            }
                        }
                        d.black=u;
                    }
                    if(sid==2) {
                        wFound=1;
                        if(d.white!=u) {
                            u.ref++;
                            if(d.white!=null) {
                                d.white.ref--;
                                if(d.white.ref==0) {
                                    removeUser(d.white);
                                }
                            }
                        }
                        d.white=u;
                    }
                    j++;
                }

            }
            if(bFound==0) {
                if(d.black!=null) {
                    d.black.ref--;
                    if(d.black.ref==0) {
                        removeUser(d.black);
                    }
                }
                d.black=null;
            }
            if(wFound==0) {
                if(d.white!=null) {
                    d.white.ref--;
                    if(d.white.ref==0) {
                        removeUser(d.white);
                    }
                }
                d.white=null;
            }
            if(buf[o.v]!=';') return -1;
            o.v++;

            d.id=did;
            d.status=desk_status;
        }
        return ret;
    }
    public static void removeUser(User u) {
        mUsers.remove(u);
    }
    public static User getUser(int uid) {
        User u = null;
        for(int i = 0; i < mUsers.size(); i ++) {
            User uu = mUsers.get(i);
            if(uu.id == uid) {
                u = uu;
                break;
            }
        }
        return u;
    }
    public static User getUser(int did,int sid) {
        User u = null;
        Desk d = getDesk(did-1);
        if(sid==1)
            u = d.black;
        else
            u = d.white;
        return u;
    }
    public static void setGameStatus(boolean b) {
        isOnGame=b;
    }
    public static boolean getGameStatus() {
        return isOnGame;
    }
    public static void addNewUser(User u,int did,int sid) {
        Desk d = getDesk(did-1);

        if(sid==1) {
            if(d.black!=u) {
                u.ref++;
                if(d.black!=null) {
                    d.black.ref--;
                    if(d.black.ref==0) {
                        removeUser(d.black);
                    }
                }
            }
            d.black=u;
        }
        if(sid==2) {
            if(d.white!=u) {
                u.ref++;
                if(d.white!=null) {
                    d.white.ref--;
                    if(d.white.ref==0) {
                        removeUser(d.white);
                    }
                }
            }
            d.white=u;
        }
        u.did = did;
        u.sid = sid;
        mUsers.add(u);
    }
    public static Desk getDesk(int idx) {
        return mDesks[idx];
    }

    public static void setMyDesk(int did) {
        mMyDesk = getDesk(did-1);
    }

    public static int userLeave(int uid) {
        User u = xSystem.getUser(uid);
        if ( u!=null && u.did == mMyDesk.id) {
            if(u.sid == 1) {
                mMyDesk.black = null;
            } else {
                mMyDesk.white = null;
            }
            u.sid=0;
            u.did=0;
            u.ref--;
            if(u.ref==0) {
                removeUser(u);
            }

        }
        return 0;
    }
    public static int setMyStatus(int status) {
        if(mMyUser!=null) {
            mMyUser.status = status;
        }
        return -1;
    }
    public static int getMyStatus() {
        if(mMyUser!=null) {
            return mMyUser.status;
        }
        return -1;
    }
    public static void iLeaveDesk() {
        mMyDesk=null;
        if(mMyUser!=null) {
            mMyUser.did=0;
            mMyUser.sid=0;
            mMyUser.status=User.USER_LOGIN;
        }
    }

    static String replaceKeyChar(String s) {
        StringBuffer sb = new StringBuffer();
        char[] cs = s.toCharArray();

        int i=0;
        while(i<cs.length) {
            switch (cs[i]) {
            case '[':
                sb.append("&B&");
                break;
            case ']':
                sb.append("&C&");
                break;
            default:
                sb.append(cs[i]);
                break;
            }
            i++;
        }
        return sb.toString();
    }
    public static void iamok() {
        mConn.sendData("request:iamok\r\n\r\n\r\n");
    }
    public static int sendTalk(String s) {
        if(s.length()>0) {
            String content = replaceKeyChar(s);
            String str;
            try {
                str = "request:post-talk\r\n"+
                      "content-length:"+content.toString().getBytes("UTF-8").length+
                      "\r\n\r\n";
                mConn.sendData(str+content+"\r\n");
                xHelper.log("goapp","send:"+str+content);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return 0;
    }
    public static int peerReady(int uid) {
        Desk d = mMyDesk;
        if(d.black!=null && d.black.id == uid) {
            d.black.status = User.USER_READY;
            return 0;
        }
        if(d.white!=null && d.white.id == uid) {
            d.white.status = User.USER_READY;
            return 0;
        }
        return -1;
    }
    public static int iamReady() {
        if(mMyUser.status==User.USER_JOIN) {
            String s ="request:ready\r\n\r\n";
            mConn.sendData(s);
            return 0;
        }
        return -1;
    }

    public static int giveUp() {
        String s ="request:giveup\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }

    public static int setDead(int x,int y) {
        String s ="request:setdead\r\n";
        s=s+"x:"+x+"\r\n";
        s=s+"y:"+y+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public static int putChess(int x,int y) {
        String s ="request:step\r\n";
        s=s+"x:"+x+"\r\n";
        s=s+"y:"+y+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }

    public static void requestDianMu() {
        String s ="request:dianmu_req\r\n\r\n";
        mConn.sendData(s);
    }
    public static void requestGiveUp() {
        String s ="request:giveup\r\n\r\n";
        mConn.sendData(s);
    }
    public static void requestPass() {
        String s ="request:pass\r\n\r\n";
        mConn.sendData(s);
    }
    public static void requestDone() {
        String s ="request:done\r\n\r\n";
        mConn.sendData(s);
    }
    public static void requestUndoDead() {
        String s ="request:undodead\r\n\r\n";
        mConn.sendData(s);
    }
    public static void requestUndoStep() {
        String s ="request:undostep\r\n\r\n";
        mConn.sendData(s);
    }
    public static void requestScore() {
        String s ="request:score\r\n\r\n";
        mConn.sendData(s);
    }

    public static void requestLeave() {
        String s ="request:leave\r\n\r\n";
        mConn.sendData(s);
    }
    public static void requestReady() {
        String s ="request:ready\r\n\r\n";
        mConn.sendData(s);
    }
    public static void rejectDianMuReq() {
        String s ="request:dianmu_rsp\r\n"+
                  "answer:no\r\n\r\n";
        mConn.sendData(s);
    }
    public static void acceptDianMuReq() {
        String s ="request:dianmu_rsp\r\n"+
                  "answer:yes\r\n\r\n";
        mConn.sendData(s);
    }

    public static void rejectDianMuResult() {
        String s ="request:dianmu_accept\r\n"+
                  "answer:no\r\n\r\n";
        mConn.sendData(s);
    }
    public static void acceptDianMuResult() {
        String s ="request:dianmu_accept\r\n"+
                  "answer:yes\r\n\r\n";
        mConn.sendData(s);
    }


    public static int joinObserve(int did) {
        String s ="request:observe\r\n"+
                  "did:"+did+"\r\n\r\n";
        mConn.sendData(s);
        xHelper.log("goapp",s);
        return 0;
    }
    public static int resume() {
        String s ="request:resume\r\n\r\n";
        mConn.sendData(s);
        xHelper.log("goapp",s);
        return 0;
    }
    public static int joinDesk(int did,int sid) {
        Desk d = getDesk(did-1);
        if(d.status<=Desk.DESK_NO_FULL) {
            if(sid==0) {
                if(d.black==null)
                    sid=1;
                else
                    sid=2;
            }
            if(sid==1 && d.black!=null)
                return -1;
            if(sid==2 && d.white!=null)
                return -1;
        } else {
            return -1;
        }
        String s ="request:join\r\n"+
                  "did:"+did+"\r\n"+
                  "sid:"+sid+"\r\n\r\n";
        mConn.sendData(s);
        xHelper.log("goapp",s);
        return 0;
    }

    public static int leave() {
        if(userLeave(mMyUser.id)==0) {
            String s ="request:leave\r\n\r\n";
            mConn.sendData(s);
            return 0;
        }
        return -1;
    }

    public static int logout() {
        User u = mMyUser;
        if(u!=null) {
            u.ref--;
            if(u.ref==0) {
                removeUser(u);
            }
        }
        String s ="request:logout\r\n\r\n";
        mConn.sendData(s);
        return 0;

    }

    public static void loadmap(int num,String mmap) {
        char[] ss = null;
        int[] bb = null;
        int b=0;
        int j=0;
        if(mGoLogic==null) {
            getGoLogic(19);
        }
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
                if(side!=0)
                    xHelper.log("goapp","byte="+side);
                side &=((3<<p2));
                side = (side>>p2);
                if(side!=0)
                    xHelper.log("goapp","set side=("+x+","+y+","+side+")");
                mGoLogic.setSide(side,x,y);
            }
        }
    }
    public static GoLogic getGoLogic(int size) {
        if(mGoLogic==null) {
            mGoLogic = new GoLogic();
            mGoLogic.setGameSize(size);
        }
        return mGoLogic;
    }

}
