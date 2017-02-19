package us.xdroid.util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import us.xdroid.util.RemoteResource;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class ResourceManager {
    private static final String LOG_TAG = "ResourceManager";

    private static HandlerThread mThread;

    private Context mContext;
    private DatabaseHelper dbHelper;
    private static final Object sLock = new Object();
    Map<Integer,RemoteResource> mResourceList;
	
	static ResourceManager me=null;
	
	
	public static ResourceManager getInstance(){
		return me;
	}


    public ResourceManager(Context ctx) {
        mContext = ctx;
        dbHelper = DatabaseHelper.getHelper(mContext);
		me = this;
    }

    public int addResource(RemoteResource rr) {        
        try {
            if(mResourceList==null)
                mResourceList= new HashMap<Integer,RemoteResource>();
            Integer key = new Integer(rr.getId());
			Dao<RemoteResource, Integer> dao = dbHelper.getResourceDao();
			//check if there already a record?
			RemoteResource oo = dao.queryForId(rr.getId());            
            if(oo==null) {                
                dao.create(rr);
				oo=rr;
            }			
			mResourceList.put(key,oo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }
	
    public int loadResourceFromRemote() {
        xUtil.log("loadResourceFromRemote");
        int num=0;
        Iterator<Map.Entry<Integer, RemoteResource>> entries = mResourceList.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, RemoteResource> entry = entries.next();
            RemoteResource rr = (RemoteResource)entry.getValue();
            if(rr.getPath()==null) {
                num+=downloadResource(rr);
            }	else {
                xUtil.log("loadResourceFromRemote:alread in local");
            }
        }
        return num;
    }

    public RemoteResource getResourceById(int id) {
//        Integer key = new Integer(id);        
 //       RemoteResource obj = (RemoteResource)mResourceList.get(key);  
		RemoteResource oo = null;
		try { 
			Dao<RemoteResource, Integer> dao = dbHelper.getResourceDao();
			//check if there already a record?
			oo = dao.queryForId(id);  		
		}catch(Exception e){
			e.printStackTrace();
		}
		return oo;        
    }
	
	
	public RemoteResource getHeadIconByIndex(int idx) {
   
        RemoteResource obj = null;    
        int num=0;
        Iterator<Map.Entry<Integer, RemoteResource>> entries = mResourceList.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, RemoteResource> entry = entries.next();
            RemoteResource rr = (RemoteResource)entry.getValue();
			if(rr.getCategory()==RemoteResource.TYPE_HEAD_IMG){
				if(num==idx){
					obj = rr;
					break;
				}
				num++;
			}
        }
		return obj;        
    }
	

    public int downloadResource(RemoteResource rr) {
        String url = rr.getUrl();
        String fn = mContext.getFilesDir()+"/"+rr.getId()+"."+rr.getType();
        int ret = xUtil.downloadFile(url,fn);
        if(ret==1) {
            Dao<RemoteResource, Integer> dao = dbHelper.getResourceDao();
			rr.setPath(fn);		
            try {
                dao.update(rr);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public int loadResourceFromDB() {
        Log.d(LOG_TAG, "loadResource++");
        int num=0;
        if(mResourceList==null)
            mResourceList= new HashMap<Integer,RemoteResource>();
        else
            mResourceList.clear();
        // Get award list from DB
        Dao<RemoteResource, Integer> dao = dbHelper.getResourceDao();
        try {
            num = (int)dao.countOf();
            Log.d(LOG_TAG, "Number of reources in DB=" + num);
            if (num > 0) {
                CloseableIterator<RemoteResource> citer = dao.iterator();
                while (citer.hasNext()) {
                    RemoteResource rr = (RemoteResource)citer.next();
                    Integer key = new Integer(rr.getId());
                    mResourceList.put(key,rr);
                }
            } else {
                Log.d(LOG_TAG, "No resources in DB. Generate initial list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }
}

