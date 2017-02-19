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
public class DbModel {
    private final String sgf_table_create =
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

    private final String max_id_table_create =
        "CREATE TABLE IF NOT EXISTS max_id (" +
        "ID INTEGER NOT NULL)";

    public DbModel() {
    }
    public SQLiteDatabase mDB = null;
    public java.util.ArrayList<SgfHeader> mLocalSgfs = null;
    public int getLocalSgfNum() {
        return mLocalSgfs.size();
    }
    public SgfHeader getLocalSgfByIndex(int idx) {
        return mLocalSgfs.get(idx);
    }
    public void initDB(Context context) {
        mLocalSgfs = new java.util.ArrayList<SgfHeader>();
        if(mDB==null) {
            try {
                mDB=context.openOrCreateDatabase("goapp", Context.MODE_WORLD_WRITEABLE, null);
                mDB.execSQL(sgf_table_create);
                mDB.execSQL(max_id_table_create);
                String sql = "SELECT * from max_id";
                Cursor c = mDB.rawQuery(sql,null);
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
    public int getMaxId() {
        mMaxId++;
        String sql = "UPDATE max_id set ID="+mMaxId;
        if(mDB==null) return -1;
        mDB.execSQL(sql);
        return mMaxId;
    }
    public int mMaxId=80000000;
    public String getLocalSgf(int id) {
        if(mDB==null) return null;
        String sql = "SELECT * from sgfs WHERE ID="+id;
        Cursor c = mDB.rawQuery(sql,null);
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

    public void updateSgfNew(SgfHeader h) {
        String sql = "UPDATE sgfs set isnew=0 WHERE ID="+ h.ID;
        if(mDB==null) return;
        mDB.execSQL(sql);
    }
    public void delSgf(int pos) {
        int idx = mLocalSgfs.size()-pos-1;
        SgfHeader h = mLocalSgfs.get(idx);
        String sql = "DELETE FROM sgfs WHERE ID="+ h.ID;
        if(mDB==null) return;
        mDB.execSQL(sql);
        mLocalSgfs.remove(idx);
    }
    public int queryDB() {
        mLocalSgfs.clear();
        if(mDB==null) return 0;
        Cursor c = mDB.rawQuery("select * from sgfs",null);
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

    public void saveSgfToSD(int id,String sgf) {
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

    public void saveSgfToFs(Context ctx,int id,String data) {
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
    public void AddSgf(int id,String sgf) {

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
}
