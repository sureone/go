package com.sureone;
import com.sureone.com.sureone.web.WebChanner;

import com.sureone.model.Group;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import us.xdroid.util.*;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;
import android.content.res.Resources;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
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
import android.os.Handler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.util.EntityUtils;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoController extends us.xdroid.util.ControllerBase{

    //Event to UI
    public final static int MSG_CONNECT_OK = 0;
    public final static int MSG_LOGIN_OK = 1;
    public final static int MSG_LOGIN_FAIL = 2;
    public final static int MSG_REGISTER_OK = 3;
    public final static int MSG_REGISTER_FAIL = 4;
    public final static int MSG_RSP_LIST = 5;
    public final static int MSG_NTFY_USER_JOIN = 6;
    public final static int MSG_NTFY_USER_LEAVE = 7;
    public final static int MSG_NTFY_USER_READY = 8;
    public final static int MSG_NTFY_GAME_START = 9;
    public final static int MSG_NTFY_ARRIVE_DIANMU_REQ = 10;
    public final static int MSG_NTFY_ARRIVE_SCORE = 11;
    public final static int MSG_NTFY_TIMEOUT_STEP = 12;
    public final static int MSG_NTFY_TIMEOUT_JOIN = 13;
    public final static int MSG_NTFY_TIMEOUT_DIANMU = 14;
    public final static int MSG_NTFY_GAME_END = 15;
    public final static int MSG_NTFY_NEXT_SIDE = 16;
    public final static int MSG_NTFY_STEP = 17;
    public final static int MSG_RSP_USER_READY = 18;
    public final static int MSG_RSP_STEP = 19;
    public final static int MSG_RSP_USER_PASS = 20;
    public final static int MSG_RSP_JOIN_OK = 21;
    public final static int MSG_RSP_JOIN_FAIL = 22;
    public final static int MSG_RSP_USER_LOGOUT = 23;
    public final static int MSG_NTFY_USER_TALK = 24;
    public final static int MSG_RSP_DIAG = 25;
    public final static int MSG_RSP_OBSERVE_OK = 26;
    public final static int MSG_RSP_OBSERVE_FAIL = 27;
    public final static int MSG_NTFY_OBSERVE_START = 28;
    public final static int MSG_LOGIN_RESUME = 29;
    public final static int MSG_RSP_RESUME_OK = 30;
    public final static int MSG_RSP_RESUME_FAIL = 31;
    public final static int MSG_CONNECT_BROKEN = 32;
    public final static int MSG_SILENT_CONNECT_OK = 33;
    public final static int MSG_NTFY_ARRIVE_START_DIANMU = 34;
    public final static int MSG_NTFY_SET_DEAD = 35;
    public final static int MSG_NTFY_UNDO_DEAD = 36;
    public final static int MSG_NTFY_CONTINUE_GO = 37;
    public final static int MSG_NTFY_USER_PASS = 38;
    public final static int MSG_NTFY_INVITE = 39;
    public final static int MSG_RSP_LIST_USER = 40;
    public final static int MSG_NTFY_ALARM = 41;
    public final static int MSG_NTFY_BROADCAST = 42;
    public final static int MSG_NTFY_INVITE_REQ = 43;
    public final static int MSG_NTFY_INVITE_DONE = 44;
    public final static int MSG_RSP_JOIN_INVITE_OK = 45;
    public final static int MSG_RSP_JOIN_INVITE_FAIL = 46;
    public final static int MSG_NTFY_REJECT_INVITE = 47;
    public final static int MSG_NTFY_MESSAGE = 48;
    public final static int MSG_RSP_INVITE_ERROR = 48;
	public final static int PLAYER_LIST_MENU = 49;
	public final static int MSG_CONNECT_TIMEOUT = 50;
	public final static int MSG_CONNECT_FAILED = 51;
	public final static int MSG_SEND_FAILED = 52;
	public final static int MSG_NTFY_USER_DONE=53;
	public final static int MSG_SEND_WEIBO_DONE=54;
	public final static int REQ_LOAD_HEAD_ICONS=55;
	public final static int RSP_LOAD_HEAD_ICONS=56;
	public final static int REQ_SET_HEAD_ICON=57;
	public final static int RSP_SET_HEAD_ICON=58;
	
	public final static int REQ_GET_HEAD_ICON=59;
	public final static int RSP_GET_HEAD_ICON=60;	
	
	public final static int REQ_GET_REMOTE_RESOURCE=61;
	public final static int RSP_GET_REMOTE_RESOURCE=62;	

	public final static int MSG_NTFY_HEART_BEAT=63;
	
	public final static int REQ_LOAD_THREADS_FROM=64;
	public final static int RSP_LOAD_THREADS_FROM=65;
	
	public final static int REQ_LOAD_THREADS_TO=66;
	public final static int RSP_LOAD_THREADS_TO=67;
	
	public final static int REQ_POST_THREAD=68;
	public final static int RSP_POST_THREAD=69;

	public final static int REQ_POST_REPLY=70;
	public final static int RSP_POST_REPLY=71;

	public final static int REQ_RM_REPLY=72;
	public final static int RSP_RM_REPLY=73;	

	public final static int REQ_RM_THREAD=74;
	public final static int RSP_RM_THREAD=75;


    public final static int REQ_BLOCK_USER=76;
    public final static int RSP_BLOCK_USER=77;
    public final static int MSG_RSP_LIST_GROUP=78;


    public final static int MSG_REGISTER_GROUP_OK = 79;
    public final static int MSG_REGISTER_GROUP_FAIL = 80;

    public final static int WEB_GROUP_REGISTER = 81;
    public final static int WEB_LIST_GROUP = 82;
    public final static int WEB_JOIN_GROUP = 83;
    public final static int WEB_LEAVE_GROUP = 84;
    public final static int WEB_UPDATE_GROUP = 85;


    public final static int MSG_NTFY_SET_STEP_TIME=86;

    public final static int MSG_NTFY_STAT_ROOM=87;

    public final static int WEB_UPLOAD_FILE=88;

    public final static int WEB_REGISTER_MATCH=89;
    public final static int WEB_LIST_SELF_MATCH=90;

    public xTcpThread mConn=null;
    public static boolean mObserverMode=false;
    DbModel mDbModel=null;
    GoGame mGoGame=null;
    GoModel mGoModel=null;
	ResourceManager mResMan=null;

    GoApp mApp = null;
    int mBoardSize=19;
    public GoController() {
        mGoModel=new GoModel();

    }

    public GoModel getModel(){
        return mGoModel;
    }
    void setApp(Context context) {
        mApp = GoApp.getInstance();
		if(mResMan==null){
			mResMan = new ResourceManager(mApp.getContext());
		}
		init(mApp.getContext());
		
    }
	// WeiboHelper weiboHelper = new WeiboHelper();
    public void bindSinaWeibo(Context context,String callBackUrl){
		// weiboHelper.bindSinaWeibo(context,callBackUrl);
	}
	public boolean onBindCallBack(Context context,Intent intent){
		// return weiboHelper.onBindCallBack(context,intent);
        return false;
	}
    public void bindTencentWeibo(Context context,String callBackUrl){
		// weiboHelper.bindTencentWeibo(context,callBackUrl);
	}
    public int sendToSina(Context context,String msg){
		int ret = 0;
		// weiboHelper.sendToSina(context,msg);
		return ret;
	}
    public int sendToSina(Context context,String msg,String fn){
		int ret = 0;
		// weiboHelper.sendToSina(context,msg,fn);
		return ret;
	}

   public void sendToQQ(Context context,String msg) {
		// weiboHelper.sendToQQ(context,msg);
	}

   public void sendToQQ(Context context,String msg,String fn){
		// weiboHelper.sendToQQ(context,msg,fn);
	}

	public boolean isBindWeibo(Context context,String which){
		// return weiboHelper.isBindWeibo(context,which);
        return false;
	}
	public String getCurrentBind (){
		// return weiboHelper.getCurrentBind();
        return null;
	}
	int getNumberOfPlayers(){
		int num=0;
       	synchronized (sLock) {
			num = mGoModel.getUserNumber();
		}
		return num;
	}

    int getNumberOfGroups(){
        int num=0;
        synchronized (sLock) {
            num = mGoModel.getGroupNumber();
        }
        return num;
    }

    public int getNumberOfMatches(){
        int num=0;
        synchronized (sLock) {
            num = mGoModel.getSelfMatchNumber();
        }
        return num;
    }
	User getPlayerByIndex(int idx){
		User u = null;
       	synchronized (sLock) {
			u= mGoModel.getPlayerByIndex(idx);
		}
		return u;
	}



    public  Group getGroupByIndex(int idx){
        Group u = null;
        synchronized (sLock) {
            u= mGoModel.getGroupByIndex(idx);
        }
        return u;
    }


    public Map getSelfMatchByIndex(int idx){
        Map map = null;
        synchronized (sLock) {
            map= mGoModel.getSelfMatchByIndex(idx);
        }
        return map;
    }



    public Group getGroupById(Long idx){
        Group u = null;
        synchronized (sLock) {
            u= mGoModel.getGroupById(idx);
        }
        return u;
    }
    int getDeskNum() {
		int num=0;
       	synchronized (sLock) {
       		num =mGoModel.getDeskNum();
		}
        xHelper.log("Desk Num="+num);
        return num;
    }
    int getUserNum() {
        return mGoModel.getUserNum();
    }
    User getUser(int uid) {
        return mGoModel.getUser(uid);
    }
	void setRank(int uid,String rank){
		User u = getUser(uid);
		if(u!=null) u.rank=rank;
	}

    public void initDB(Context ctx) {
        mGoModel.init(ctx);
        if(mDbModel==null) mDbModel=new DbModel();
        mDbModel.initDB(ctx);
    }

    public Desk getMyDesk() {
        return mGoModel.getMyDesk();
    }
    public User getMyUser() {
        return mGoModel.getMyUser();
    }

    public Boolean isGroupOwner() {
        User u = mGoModel.getMyUser();
        if(u.id==u.groupOwner){
            return true;
        }
        return false;
    }
    public void setMyDesk(Desk d) {
        mGoModel.setMyDesk(d);
    }
    public void setMyUser(User d) {
        mGoModel.setMyUser(d);
    }
    public int getLocalSgfNum() {
        return mDbModel.getLocalSgfNum();
    }
    public SgfHeader getLocalSgfByIndex(int idx) {
        return mDbModel.getLocalSgfByIndex(idx);
    }
    public String getLocalSgf(int id) {
        return mDbModel.getLocalSgf(id);
    }

    public int saveSgf(Context ctx) {
        //if(mGoGame!=null && mObserverMode==false) {
        if(mGoGame!=null) {
            String s = mGoGame.genSgf();
			if(s==null) return -1;
            int id = mDbModel.getMaxId();
            if(id>0) {
                mDbModel.saveSgfToFs(ctx,id,s);
                mDbModel.AddSgf(id,s);
            }
            return id;
        }
        return -1;
    }

    public xTcpThread getConnection() {
        if (mConn == null) {
            mConn = new xTcpThread();
        }
        return mConn;
    }

    public void newGame(int size) {
        if(mGoGame==null) mGoGame= new GoGame(size);
        mGoGame.clean();
    }


    public void setMyDesk(int did,int sid) {
        mGoModel.setMyDesk(did,sid);
    }

    public int userLeave(int uid) {
        return mGoModel.userLeave(uid);
    }
    public int setMyStatus(int status) {
        return mGoModel.setMyStatus(status);
    }
    public int getMyStatus() {
        return mGoModel.getMyStatus();
    }
    public void iLeaveDesk() {
        mGoModel.iLeaveDesk();
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
    public void iamok() {
        mConn.sendData("request:iamok\r\n\r\n\r\n");
    }
	public void requestBroadcast(String s){
        if(s.length()>0) {
            String content = replaceKeyChar(s);
			String str="request:broadcast\r\ncontent:"+s+"\r\n\r\n";
            mConn.sendData(str);
		}
	}
	public int messageHim(int uid,String s){
            String content = replaceKeyChar(s);
			String str="request:message\r\nuid:"+uid+"\r\ncontent:"+s+"\r\n\r\n";	
            mConn.sendData(str);
		return 0;
	}
    public int sendTalk(String s) {
        if(s.length()>0) {
            String content = replaceKeyChar(s);
            String str;
            try {
                str = "request:post-talk\r\n"+
                      "content-length:"+content.toString().getBytes("UTF-8").length+
                      "\r\n\r\n";
                mConn.sendData(str+content+"\r\n");
                xHelper.log("send:"+str+content);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return 0;
    }
    public int setPeerReady(int uid) {
        mGoModel.setPeerReady(uid);
        return 0;
    }
    public int iamReady() {
        if(getMyStatus()==User.USER_JOIN) {
            String s ="request:ready\r\n\r\n";
            mConn.sendData(s);
            return 0;
        }
        return -1;
    }

    public int giveUp() {
        String s ="request:giveup\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public int continueGo() {
        if(getStatus()==IN_GAME) {
            if(mSubStatus==IN_DIANMU) {
                String s ="request:continuego\r\n\r\n";
                mConn.sendData(s);
            }
        }
        return 0;
    }

    public int setDead(int x,int y) {
        String s ="request:setdead\r\n";
        s=s+"x:"+x+"\r\n";
        s=s+"y:"+y+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
	
	public RemoteResource getHeadIconByIndex(int idx){
		return mResMan.getHeadIconByIndex(idx);
	}
	
	
	public RemoteResource getResourceById(int id,int uid){
		RemoteResource rr = null;
		rr = mResMan.getResourceById(id);
		
		if(rr==null){
		//load from remote
		
			xHelper.log("getResourceById");
			JSONObject obj = new JSONObject();
			try {

				obj.put("ACTION", new String("getResource"));
				obj.put("resid", id);
				obj.put("uid", uid);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				return null;
			}
			sendRequest(REQ_GET_REMOTE_RESOURCE,obj);				
			
		}
		return rr;
	}
	
	
	
	public void setHeadIcon(int id){
		mApp.setHeadIcon(id);
		User u = getMyUser();
		u.setHeadIcon(id);
		xHelper.log("setHeadIcon");
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("setHeadIcon"));
			obj.put("uid", mApp.getUID());
			obj.put("resid", id);
			
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequest(REQ_SET_HEAD_ICON,obj);				
	}
	
	public void queryHeadIcon(int uid){
		User u = getUser(uid);
		if(u!=null) u.setHeadIcon(-1);
		
		xHelper.log("queryHeadIcon");
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("getHeadIcon"));
			obj.put("uid", uid);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_GET_HEAD_ICON,obj);				
	}	
	
	public void loadHeadIcons(){	
		xHelper.log("loadHeadIcons");
		sendRequest(REQ_LOAD_HEAD_ICONS);
	}
	
	public void loadThreadsFrom(String dt,int rid){
		xHelper.log("loadThreadsFrom "+dt);
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("loadThreadsFrom"));
			obj.put("dt", dt);
			obj.put("rid", rid);
			obj.put("max", 100);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_LOAD_THREADS_FROM,obj);				
	}
	
	public void loadThreadsTo(String dt,int rid){
		xHelper.log("loadThreadsTo "+dt);
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("loadThreadsTo"));
			obj.put("dt", dt);
			obj.put("rid", rid);
			obj.put("max", 100);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_LOAD_THREADS_TO,obj);				
	}	
	
	public void postThread(String content){
	
		String author =  GoApp.getInstance().getEmail();
		int c = author.indexOf('@');
		if(c!=-1){
			author=author.substring(0,c);
		}
		if(author==null || author.trim().length()==0) {
			author = GoApp.getInstance().getContext().getString(R.string.noname);
		}
	
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("postThread"));
			obj.put("content", content);
			obj.put("uid", mApp.getUID());
            obj.put("sn:",xUtil.getDeviceID(mContext));
			obj.put("uname", author);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_POST_THREAD,obj);			
	}
	
	public void postReply(int tid,String content){
		String author =  GoApp.getInstance().getEmail();
		int c = author.indexOf('@');
		if(c!=-1){
			author=author.substring(0,c);
		}
		if(author==null || author.trim().length()==0) {
			author = GoApp.getInstance().getContext().getString(R.string.noname);
		}	
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("postReply"));
			obj.put("content", content);
			obj.put("uid", mApp.getUID());
            obj.put("sn:",xUtil.getDeviceID(mContext));
			obj.put("rid",tid);
			obj.put("uname", author);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_POST_REPLY,obj);			
	}
	
	public void removeThread(int tid,int rmuid){
		xHelper.log("removeThread "+tid);
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("removeThread"));
			obj.put("uid", mApp.getUID());
            obj.put("rmuid",rmuid);
			obj.put("tid", tid);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_RM_THREAD,obj);				
	}



    public void blockUser(String sn){
        xHelper.log("blockUser "+sn);
        JSONObject obj = new JSONObject();
        try {

            obj.put("ACTION", new String("blockUser"));
            obj.put("uid", mApp.getUID());
            obj.put("sn",sn);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_BLOCK_USER,obj);
    }




    public void removeReply(int tid,int rmuid){
		xHelper.log("removeReply "+tid);
        JSONObject obj = new JSONObject();
        try {

			obj.put("ACTION", new String("removeReply"));
            obj.put("uid", mApp.getUID());
            obj.put("rmuid",rmuid);

            obj.put("tid", tid);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            return;
        }
        sendRequestNC(REQ_RM_REPLY,obj);				
	}		
	
	@Override
    public int handleMessage(Message msg) {
        int result=0;
        switch (msg.what) {
        case REQ_LOAD_HEAD_ICONS: {
			int ret = doLoadHeadIcons();            
			sendMessageToUI(RSP_LOAD_HEAD_ICONS,ret,0,null);
			break;		
		}
        case REQ_SET_HEAD_ICON: {
			int ret = doSetHeadIcon((JSONObject)msg.obj);            
			sendMessageToUI(RSP_SET_HEAD_ICON,ret,0,null);
			break;				
		}	
		case REQ_GET_HEAD_ICON: {
			Bundle bundle = doGetHeadIcon((JSONObject)msg.obj);            
			sendMessageToUI(RSP_GET_HEAD_ICON,0,0,bundle);				
			break;				
		}	
		case REQ_GET_REMOTE_RESOURCE: {
			Bundle bundle = doGetRemoteResource((JSONObject)msg.obj);            
			sendMessageToUI(RSP_GET_REMOTE_RESOURCE,0,0,bundle);				
			break;				
		}			
		
		case REQ_LOAD_THREADS_FROM: {
			List<MessageThread> threads = doLoadThreads((JSONObject)msg.obj);			
			sendMessageToUI(RSP_LOAD_THREADS_FROM,0,0,threads);
			break;				
		}	

		case REQ_LOAD_THREADS_TO: {
			List<MessageThread> threads = doLoadThreads((JSONObject)msg.obj);            
			sendMessageToUI(RSP_LOAD_THREADS_TO,0,0,threads);
			break;				
		}	
		
		case REQ_POST_THREAD: {
			int ret = doPostThread((JSONObject)msg.obj);            
			sendMessageToUI(RSP_POST_THREAD,ret,0,null);				
			break;				
		}	

		case REQ_POST_REPLY: {
			int ret = doPostThread((JSONObject)msg.obj);            
			sendMessageToUI(RSP_POST_REPLY,ret,0,null);				
			break;				
		}			
		case REQ_RM_THREAD: {
			int ret = doRemoveThread((JSONObject)msg.obj);            
			sendMessageToUI(RSP_RM_THREAD,ret,0,null);				
			break;				
		}


            case REQ_BLOCK_USER: {
                int ret = doBlockUser((JSONObject)msg.obj);
                sendMessageToUI(RSP_BLOCK_USER,ret,0,null);
                break;
            }
            case REQ_RM_REPLY: {
			int ret = doRemoveReply((JSONObject)msg.obj);            
			sendMessageToUI(RSP_RM_REPLY,ret,0,null);				
			break;				
		}

            case WEB_UPLOAD_FILE:{
                Map map = (Map)msg.obj;

                //WebChanner.doSendFile((String)(map.get("filename")));
            }

            default:{
                WebChanner.MyJsonResponse ret = WebChanner.doPostJson(mContext,getNEW_SERVER_URL(),(Map)msg.obj,false);
                msg.obj=ret.map;
                //sendMessageToUI(msg.what,0,0,ret.map);
                result= ret.ret;
                break;
            }
		
		}

        return result;
	}

    public String getNEW_SERVER_URL(){
        return GoModel.getPrefString("NEW_HTTP_URL","http://42.121.129.37/xgo")+"/index.php/appentry";
        //return NEW_SERVER_URL;
    }

    public String NEW_SERVER_URL="http://42.121.129.37/xgo/index.php/appentry";
	
	
	private List<MessageThread> doLoadThreads(JSONObject obj) {
		xHelper.log("doLoadThreads");
			
        do {

            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return null;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
				LinkedList<MessageThread> list = new LinkedList<MessageThread>();
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());										
                    JSONArray ary = jo.getJSONArray("threads");
					xHelper.log("threads="+ary.length());
					String action = obj.getString("ACTION");
                    for (int i = 0; i < ary.length(); i++) {					
                        JSONObject joo = ary.getJSONObject(i);						
						MessageThread mt = MessageThread.createFromJson(joo);
						if(action.equals("loadThreadsTo"))
							list.addLast(mt);
						else
							list.addFirst(mt);
						//list.add(mt);
					}		
					return list;
                } catch (JSONException e1) {
                    e1.printStackTrace();                    
					return null;
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return null;
            }
        } while (false);

    }		
	
    private int doPostThread(JSONObject obj) {
		xHelper.log("doPostThread");
        do {
            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return -1;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
                    //JSONObject jo = new JSONObject(test);

                } catch (JSONException e1) {
                    e1.printStackTrace();                    
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return -1;
            }
        } while (false);

        return 0;
    }	
	
    private int doRemoveThread(JSONObject obj) {
		xHelper.log("doRemoveThread");
        do {
            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return -1;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
                    //JSONObject jo = new JSONObject(test);

                } catch (JSONException e1) {
                    e1.printStackTrace();                    
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return -1;
            }
        } while (false);

        return 0;
    }


    private int doBlockUser(JSONObject obj) {
        xHelper.log("doRemoveThread");
        do {
            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
                return -1;
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
                    //JSONObject jo = new JSONObject(test);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else {
                xHelper.log("Failed, status code=" + status);
                return -1;
            }
        } while (false);

        return 0;
    }
	
    private int doRemoveReply(JSONObject obj) {
		xHelper.log("doRemoveReply");
        do {
            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return -1;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
                    //JSONObject jo = new JSONObject(test);

                } catch (JSONException e1) {
                    e1.printStackTrace();                    
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return -1;
            }
        } while (false);

        return 0;
    }		
	
    private int doSetHeadIcon(JSONObject obj) {
		xHelper.log("doSetHeadIcon");
        do {
            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return -1;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
                    //JSONObject jo = new JSONObject(test);

                } catch (JSONException e1) {
                    e1.printStackTrace();                    
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return -1;
            }
        } while (false);

        return 0;
    }
	
    private Bundle doGetHeadIcon(JSONObject obj) {
		xHelper.log("doGetHeadIcon");
		int cnt=0;
		Bundle bundle = null;
        do {

            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return null;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
                    JSONArray ary = jo.getJSONArray("headicon");
					
					if(ary==null) return null;
					RemoteResource rr=null;
					bundle = new Bundle();
					int uid=0;
                    for (int i = 0; i < ary.length(); i++) {					
                        JSONObject joo = ary.getJSONObject(i);
						rr = new RemoteResource();
						Iterator<String> it = joo.keys();
						
						while (it.hasNext()) {
							String field = it.next();
							if (field.equals("uid"))
								uid=joo.getInt(field);
							if (field.equals("resid"))
								rr.setId(joo.getInt(field));						
							else if (field.equals("type"))
								rr.setType(joo.getString(field));
							else if (field.equals("url"))
								rr.setUrl(joo.getString(field));
						}
					}
					
					bundle.putInt("uid",uid);
					bundle.putInt("resid",rr.getId());
					
					rr.setCategory(RemoteResource.TYPE_HEAD_IMG);
					mResMan.addResource(rr);					
					User u = getUser(uid);
					if(u!=null){
						u.setHeadIcon(rr.getId());
						xHelper.log("apply head icon to user="+u.id+","+u.getHeadIcon());
						
						if(u.id==mApp.getUID()){
							mApp.setHeadIcon(rr.getId());
						}
					}					

					
                } catch (JSONException e1) {
                    e1.printStackTrace();                    
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return null;
            }
        } while (false);

        return bundle;
    }	
	
    private Bundle doGetRemoteResource(JSONObject obj) {
		xHelper.log("doGetRemoteResource");
		int cnt=0;
		Bundle bundle = null;
        do {

            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return null;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
					
					
                    JSONArray ary = jo.getJSONArray("resource");
					RemoteResource rr=null;
					bundle = new Bundle();
					int uid = jo.getInt("uid");
					if(ary==null) return null;
                    for (int i = 0; i < ary.length(); i++) {					
                        JSONObject joo = ary.getJSONObject(i);
						rr = new RemoteResource();
						Iterator<String> it = joo.keys();
						while (it.hasNext()) {
							String field = it.next();							
							if (field.equals("id"))
								rr.setId(joo.getInt(field));	
							else if (field.equals("category"))
								rr.setCategory(joo.getInt(field));							
							else if (field.equals("type"))
								rr.setType(joo.getString(field));
							else if (field.equals("url"))
								rr.setUrl(joo.getString(field));
						}
					}		
					bundle.putInt("uid",uid);
					bundle.putInt("resid",rr.getId());
					cnt = rr.getId();
					mResMan.addResource(rr);	
					
                } catch (JSONException e1) {
                    e1.printStackTrace();                    
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return null;
            }
        } while (false);

        return bundle;
    }		
		
	
    private int doLoadHeadIcons() {
		xHelper.log("doLoadHeadIcons");
		int cnt=0;
        do {
            JSONObject obj = new JSONObject();
            try {
                obj.put("ACTION", new String("loadHeadIcons"));
				obj.put("uid",mApp.getUID());
            } catch (JSONException e1) {
                xHelper.log("error packing JSON object");
                return -1;
            }

            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = HttpConnectionManager.doPostEntity(obj.toString(), jsonResponse);
            } catch (Exception e) {
                xHelper.log("network error");
				return -1;                
            }

            if (status == HttpStatus.SC_OK) {
                xHelper.log("HTTP POST OK");
                try {
                    //String test = "{ACTION:GETAWARDLIST,awardlist:[{id:5,name:fsafd,price:200,iconid:1},{id:6,name:浣犲ソ鍚?price:300,iconid:2}]}";
                    JSONObject jo = new JSONObject(jsonResponse.toString());
                    //JSONObject jo = new JSONObject(test);
                    JSONArray ary = jo.getJSONArray("icons");
					
					if(ary==null) return 0;
                    for (int i = 0; i < ary.length(); i++) {					
                        JSONObject joo = ary.getJSONObject(i);
                        RemoteResource rr = RemoteResource.createFromJson(joo);			
						rr.setCategory(RemoteResource.TYPE_HEAD_IMG);
						mResMan.addResource(rr);
						cnt++;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();                    
                }
            } else {
                xHelper.log("Failed, status code=" + status);
				return -1;
            }
        } while (false);

        return cnt;
    }
	
	
	public String mMsgWeibo="";

	public void setNextWeiboMsg(String s){
		mMsgWeibo=s;
	}
	public String getNextWeiboMsg(){
		return mMsgWeibo;
	}
    String mLastReqData=null;
    void doRetry() {
        if( mLastReqData!=null) {
            mConn.sendData(mLastReqData);
        }
    }
	public int join_invite(){
        String s ="request:join_invite\r\n\r\n";
        mLastReqData=s;
        mConn.sendData(s);
		return 0;
	}
	public int new_invite(int uid){
        String s ="request:invite\r\n";
        s=s+"uid:"+uid+"\r\n\r\n";
        mLastReqData=s;
        mConn.sendData(s);
		return 0;
	}
	public int accept_invite(int uid){
        String s ="request:accept_invite\r\n";
        s=s+"uid:"+uid+"\r\n\r\n";
        mLastReqData=s;
        mConn.sendData(s);
		return 0;
	}
	public int reject_invite(int uid){
        String s ="request:reject_invite\r\n";
        s=s+"uid:"+uid+"\r\n\r\n";
        mLastReqData=s;
        mConn.sendData(s);
		return 0;
	}
    public int putChess(int x,int y) {
        if(getStatus()==IN_GAME) {
            if(mSubStatus==UNKNOWN) {
                String s ="request:step\r\n";
                s=s+"x:"+x+"\r\n";
                s=s+"y:"+y+"\r\n\r\n";
                mLastReqData=s;
                mConn.sendData(s);
            } else if(mSubStatus==IN_DIANMU) {
                String s ="request:setdead\r\n";
                s=s+"x:"+x+"\r\n";
                s=s+"y:"+y+"\r\n\r\n";
                mLastReqData=s;
                mConn.sendData(s);
            }
        }
        return 0;
    }

    public void requestGiveUp() {
        String s ="request:giveup\r\n\r\n";
        mConn.sendData(s);
    }
    public void requestPass() {
        String s ="request:pass\r\n\r\n";
        mConn.sendData(s);
    }
    public void requestAlarm() {
        String s ="request:alarm\r\n\r\n";
        mConn.sendData(s);
    }
    public void judgeWin(int did,int win) {
        String s ="request:judge\r\n"
				+"did:"+did+"\r\n"
				+"win:"+win
				+"\r\n\r\n";
        mConn.sendData(s);
	}
    public void requestDone() {
        String s ="request:done\r\n\r\n";
        mConn.sendData(s);
    }
    public void requestUndoDead() {
        String s ="request:undodead\r\n\r\n";
        mConn.sendData(s);
    }
    public void requestUndoStep() {
        String s ="request:undostep\r\n\r\n";
        mConn.sendData(s);
    }
    public void requestScore() {
        String s ="request:score\r\n\r\n";
        mConn.sendData(s);
    }

    public void requestLeave() {
        changeStatus(IN_ROOM);
        String s ="request:leave\r\n\r\n";
        mConn.sendData(s);
    }
    public void requestReady() {
        String s ="request:ready\r\n\r\n";
        mConn.sendData(s);
    }

    public int loadPlayerList() {
        String s ="request:users\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }

    public int loadGroupList() {
        String s ="request:listgroup\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public int loadGroupUser(int gid) {
        String s ="request:listgroupuser\r\n"+
                "gid:"+gid+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public int joinGroup(int gid) {
        String s ="request:joingroup\r\n"+
                "gid:"+gid+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public int leaveGroup(int gid) {
        String s ="request:leavegroup\r\n"+
                "gid:"+gid+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public int loadDeskList() {
        String s ="request:list\r\n"
		  +"type:desk\r\n\r\n";
	if(mConn==null) return 0;
        mConn.sendData(s);
        return 0;
    }
    public int declineInvite(Bundle msg) {
		int id=0;
        String s ="request:declineInvite\r\n"+
                  "id:"+id+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public int acceptInvite(Bundle msg) {
		int id=0;
        String s ="request:acceptInvite\r\n"+
                  "id:"+id+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public int joinObserve(int did) {
        String s ="request:observe\r\n"+
                  "did:"+did+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }

    public int setStepTime(int minutes) {
        String s ="request:set_step_time\r\n"+
                "minutes:"+minutes+"\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }

    public int statRoom() {
        String s ="request:stat-room\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }


    public int resume() {
        String s ="request:resume\r\n\r\n";
        mConn.sendData(s);
        return 0;
    }
    public Desk getDesk(int idx) {
		Desk desk = null;
       	synchronized (sLock) {
        	desk = mGoModel.getDesk(idx);
		}
		return desk;
    }
    public int joinDesk(int did,int sid) {
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
        return 0;
    }

    public int leave() {
        User u = getMyUser();
        if(userLeave(u.id)==0) {
            String s ="request:leave\r\n\r\n";
            mConn.sendData(s);
            return 0;
        }
        return -1;
    }

    public int logout() {
        User u = getMyUser();
        if(u!=null) {
            u.ref--;
            if(u.ref==0) {
                mGoModel.removeUser(u);
            }
        }
        String s ="request:logout\r\n\r\n";
        mConn.sendData(s);
        return 0;

    }
	
	public void saveGameRecord(String record){
		if(mGoGame!=null){
			mGoGame.saveRecord(record);
		}
	}
	
    public void loadmap(int num,String mmap) {
        mGoGame.loadmap(num,mmap);
    }
    public GoLogic getGoLogic(int size) {
        if(mGoGame!=null) return mGoGame.mGoLogic;
        return null;
    }

    Handler mHandler = null;
    Handler mDefHandler = null;

    public Handler getHandler(){
        return mHandler;
    }
    public void setHandler(Handler handler) {
		xHelper.log("controller setHandler="+handler);
        mHandler = handler;
		setUIHandler(handler);
    }

    void setDefHandler(Handler handler) {
        mDefHandler=handler;
    }

    void sendMessageToService(Message msg) {
	
		xHelper.log("controller handler="+mDefHandler);
		if(mDefHandler!=null){
        	mDefHandler.sendMessage(msg);
		}
    }
    void sendMessage(Message msg) {
        if(mHandler==null) {
            mDefHandler.sendMessage(msg);
        } else
            mHandler.sendMessage(msg);
    }

    String mLastIp=null;
    int mLastPort=0;
    int inSilentReconnect = 0;
    int mMaxSilentRetries = 50;
    int mSilentTimeout = 20*1000;
    boolean bBrokenEventInjected = false;

	String mSecondIp="184.82.230.120";
	boolean bTrySecond=true;
    public void silentReconnect() {
        xHelper.log("do Silent Reconnect "+inSilentReconnect);
        inSilentReconnect++;
        //connectServer(mLastIp,mLastPort);
    }

    public void onSilentError() {
		inSilentReconnect=0;
		//try to connect "xdroid.us"
		if(bTrySecond) bTrySecond=false;
		else
			return;
		mLastIp=mSecondIp;
        xHelper.log("Silent Reconnect failed");
    }

    public void login(String email,String pin) {
        xTcpThread conn = getConnection();
        conn.sendData("request:login\r\n"+
                      "sn:"+ xUtil.getDeviceID(mContext)+"\r\n"+
                      "email:"+email+"\r\n"+
                      "password:"+pin+"\r\n\r\n");
    }

    public void registerGroup(String groupName) {
        xTcpThread conn = getConnection();
        conn.sendData("request:registergroup\r\n"+
                "sn:"+ xUtil.getDeviceID(mContext)+"\r\n"+
                "name:"+groupName+"\r\n\r\n");
    }


    public void WebRegisterGroup(String groupName,String groupDesc,String rankReq)
    {

        groupName.trim();
        HashMap param = new HashMap();
        param.put("ACTION","REGISTER_GROUP");
        param.put("name",groupName);
        param.put("desc",groupDesc);
        param.put("rankreq",GoModel.getRankValue(rankReq));
        param.put("uid",getMyUser().id);
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_GROUP_REGISTER,param);
    }


    public void webListSelfMatch()
    {

        if(mApp.getUID()==0) return;

        HashMap param = new HashMap();
        param.put("ACTION","LIST_SELF_MATCH");
        param.put("uid",mApp.getUID());
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_LIST_SELF_MATCH,param);
    }




    public void WebRegisterMatch(String matchTitle,String matchDescription,int minLevel,int maxLevel)
    {
        if(mApp.getUID()==0) return;

        HashMap param = new HashMap();
        param.put("ACTION","REGISTER_MATCH");
        param.put("title",matchTitle);
        param.put("desc",matchDescription);
        param.put("min-level",minLevel);
        param.put("max-level",maxLevel);
        param.put("uid", mApp.getUID());
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_REGISTER_MATCH,param);
    }






    public void WebUpdateGroup(Long groupId,String groupName,String groupDesc,String rankReq)
    {

        groupName.trim();
        HashMap param = new HashMap();
        param.put("ACTION","UPDATE_GROUP");
        param.put("name",groupName);
        param.put("desc",groupDesc);
        param.put("groupid",groupId);
        param.put("rankreq",GoModel.getRankValue(rankReq));
        param.put("uid",getMyUser().id);
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_UPDATE_GROUP,param);
    }


    public void uploadFile(String filename){

        HashMap param = new HashMap();
        param.put("ACTION","UPLOAD_FILE");
        param.put("filename",filename);
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_UPLOAD_FILE,param);
    }


    public void WebJoinGroup(Long groupId)
    {

        HashMap param = new HashMap();
        param.put("ACTION","JOIN_GROUP");
        param.put("group_id",groupId);

        param.put("uid",getMyUser().id);
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_JOIN_GROUP,param);
    }


    public void WebLeaveGroup()
    {

        HashMap param = new HashMap();
        param.put("ACTION","LEAVE_GROUP");
        param.put("group_id",getMyUser().groupid);

        param.put("uid",getMyUser().id);
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_LEAVE_GROUP,param);
    }



    public void WebListGroup()
    {


        HashMap param = new HashMap();
        param.put("ACTION","LIST_GROUP");

        param.put("uid",getMyUser().id);
        param.put("sn",xUtil.getDeviceID(mContext));

        sendRequest(WEB_LIST_GROUP,param);
    }


    public void close() {
        xTcpThread conn = getConnection();
        if(conn!=null) conn.shutdown();
        setMyUser(null);
        setMyDesk(null);
        changeStatus(OFFLINE);
    }

    EvtListener mEvtListener = new EvtListener();
    public void connectServer(String ip,int port) {
        xTcpThread conn = getConnection();
        if(conn.mConnected==false || mLastIp.compareTo(ip)!=0 || mLastPort!=port) {
            mLastIp = ip;
            mLastPort = port;
            if(conn.mConnected==true) {
                conn.shutdown();
            }
            xHelper.log("Connect to "+ip+":"+port);
            conn.setTcpEventListener(mEvtListener);
            conn.setAddress(ip,port);
            conn.start();
        } else if(conn.mConnected==true) {
            Message message = new Message();
            message.what = MSG_CONNECT_OK;
            sendMessage(message);
        }
    }
    void handleStatRsp(byte[] buf,int len,int offset) {
            String ss= new String(buf,0,len);
            Message message = new Message();
            message.what = MSG_RSP_DIAG;
            message.obj = ss;
            sendMessage(message);
    }
    void handleInviteRsp(byte[] buf,int len,int offset) {
        	x_Integer o = new x_Integer(offset);
        	String result = xHelper.getStr(buf, len, o, ',');
			if(result.compareTo("error")==0){
            	Message message = new Message();
            	message.what = MSG_RSP_INVITE_ERROR;
            	sendMessage(message);
			}
    }
    void handleDiagRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int l = xHelper.getInt(buf, len, o, ',');
        if(l>0) {
            String ss= new String(buf,o.v,l);
            Message message = new Message();
            message.what = MSG_RSP_DIAG;
            message.obj = ss;
            sendMessage(message);
        }
    }
    User addUser(int uid) {
        return mGoModel.addUser(uid);
    }

    void handleLoginRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        String result = xHelper.getStr(buf, len, o, ',');
        if (result.compareTo("ok")==0) {
            int uid = xHelper.getInt(buf, len, o, ',');
            String loginT = xHelper.getStr(buf, len, o, ',');
			User u=null;
            if (loginT.compareTo("normal")==0) {
                u = addUser(uid);
                u.ref++;
                u.id = uid;
                setMyUser(u);
                setMyStatus(User.USER_LOGIN);
                Message message = new Message();
                message.what = MSG_LOGIN_OK;
                sendMessage(message);
            } else if(loginT.compareTo("resume")==0) {
                u = addUser(uid);
                u.ref++;
                u.id = uid;
                setMyUser(u);
                setMyStatus(User.USER_LOGIN);
                Message message = new Message();
                message.what = MSG_LOGIN_RESUME;
                sendMessage(message);
            }
            String rank = xHelper.getStr(buf, len, o, ',');
			u.rank=rank;
            String admin = xHelper.getStr(buf, len, o, ',');
			if(admin.compareTo("A")==0) u.isadmin=1;
			else u.isadmin=0;
			int wins = xHelper.getInt(buf,len,o,',');
			int loses = xHelper.getInt(buf,len,o,',');
            u.groupid= xHelper.getInt(buf,len,o,',');

            u.groupName = xHelper.getStr(buf,len,o,',');
            if(u.groupid==0){
                u.groupName=null;

            }
            u.groupOwner= xHelper.getInt(buf,len,o,',');
            u.wins=wins;
			u.loses=loses;
			u.totals=wins+loses;
            changeStatus(IN_ROOM);
        } else {
            Message message = new Message();
            message.what = MSG_LOGIN_FAIL;
            sendMessage(message);
        }
    }
    void handleRegisterRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        String result = xHelper.getStr(buf, len, o, ',');
        if (result.compareTo("ok")==0) {
            int uid = xHelper.getInt(buf, len, o, ',');
            User u = addUser(uid);
            setMyUser(u);
            setMyStatus(User.USER_LOGIN);
            Message message = new Message();
            message.what = MSG_REGISTER_OK;
            sendMessage(message);
        } else {
            Message message = new Message();
            message.what = MSG_REGISTER_FAIL;
            sendMessage(message);
        }

    }


    void handleRegisterGroupRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        String result = xHelper.getStr(buf, len, o, ',');
        if (result.compareTo("ok")==0) {

            Message message = new Message();
            message.what = MSG_REGISTER_GROUP_OK;
            sendMessage(message);
        } else {
            Message message = new Message();
            message.what = MSG_REGISTER_GROUP_FAIL;
            sendMessage(message);
        }

    }

    void setCurTurn(int t) {
        mGoGame.setCurTurn(t);
    }
    int getCurTurn() {
        return mGoGame.getCurTurn();
    }

    public static final int UNKNOWN=0;
    public static final int OFFLINE=1;
    public static final int ONLINE=2;
    public static final int IN_ROOM=3;
    public static final int IN_DESK=4;
    public static final int IN_GAME=5;
    public static final int IN_OBSERVE=6;
    public static final int IN_DIANMU=7;
    int mStatus=OFFLINE;
    int mSubStatus=UNKNOWN;
    int mLastStatus=OFFLINE;
    boolean isInRoom() {
        if(mStatus==IN_ROOM) return true;
        return false;
    }
    boolean isOffline() {
        if(mStatus==OFFLINE) return true;
        return false;
    }
    void changeStatus(int status) {
        mLastStatus = mStatus;
        mStatus = status;
    }
    int getStatus() {
        return mStatus;
    }

    String mGameStatus="play";
    void handleResumeRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if (err == 200) {
            int uid = xHelper.getInt(buf, len, o, ',');
            int did = xHelper.getInt(buf, len, o, ',');
            int sid = xHelper.getInt(buf, len, o, ',');
            int next = xHelper.getInt(buf, len, o, ',');
            setMyDesk(did,sid);
            int num = xHelper.getInt(buf, len, o, ',');
            String mmap=null;
            if(num>0) {
                mmap = xHelper.getStr(buf, len, o, ',');
            }

            if(mGoGame==null) {
                newGame(mBoardSize);
            }
						
            xHelper.log("resume,did="+did+"num="+num+"\n"+mmap);
            loadmap(num,mmap);
						int lx=xHelper.getInt(buf, len, o, ',');
						int ly=xHelper.getInt(buf, len, o, ',');


                        mGameStatus=xHelper.getStr(buf,len,o,',');

                        if(mGameStatus.equals("dianmu")){


                            mSubStatus=IN_DIANMU;

                            //清除死子
                            int dm_deads_num = xHelper.getInt(buf,len,o,',');
                            for(int i=0;i<dm_deads_num;i++){
                                int x = xHelper.getInt(buf, len, o, ',');
                                int y = xHelper.getInt(buf, len, o, ',');
                                kill(x,y);
                            }
                        }


                        int timeout=xHelper.getInt(buf,len,o,',');
                        this.mStepTimeout=timeout;
						setLastStep(lx,ly);
            setCurTurn(next);
            setMyStatus(User.USER_PLAYING);
            changeStatus(IN_GAME);
            Message message = new Message();
            message.what = MSG_RSP_RESUME_OK;
            sendMessage(message);
        } else {
            Message message = new Message();
            message.what = MSG_RSP_RESUME_FAIL;
            sendMessage(message);
        }
    }
    void handleObserveRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if (err == 200) {
            int did = xHelper.getInt(buf, len, o, ',');
            xHelper.log("Observe Desk "+did+" OK!");
            setMyDesk(did,3);
            //clean the go logic data
            newGame(mBoardSize);
            setMyStatus(User.USER_OBSERVE);
            changeStatus(IN_OBSERVE);
            Message message = new Message();
            message.what = MSG_RSP_OBSERVE_OK;
            sendMessage(message);

        } else {
            Message message = new Message();
            message.what = MSG_RSP_OBSERVE_FAIL;
            sendMessage(message);
        }
    }
    void handleJoinInviteRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if (err == 200) {
            int did = xHelper.getInt(buf, len, o, ',');
            int sid = xHelper.getInt(buf, len, o, ',');
            xHelper.log("Join Desk "+did+" OK!");
            setMyDesk(did,sid);
            setMyStatus(User.USER_JOIN);
            newGame(19);
            changeStatus(IN_DESK);
            Message message = new Message();
            message.what = MSG_RSP_JOIN_INVITE_OK;
            sendMessageToService(message);
        } else {
            Message message = new Message();
            message.what = MSG_RSP_JOIN_INVITE_FAIL;
            sendMessageToService(message);
        }
    }
    void handleJoinRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if (err == 200) {
            int did = xHelper.getInt(buf, len, o, ',');
            int sid = xHelper.getInt(buf, len, o, ',');
            xHelper.log("Join Desk "+did+" OK!");
            setMyDesk(did,sid);
            setMyStatus(User.USER_JOIN);
            newGame(19);
            changeStatus(IN_DESK);
            Message message = new Message();
            message.what = MSG_RSP_JOIN_OK;
            sendMessage(message);
        } else {
            Message message = new Message();
            message.what = MSG_RSP_JOIN_FAIL;
            sendMessage(message);
        }
    }
    void parseUsersData(Bundle msg){
    	int len = msg.getInt("length");
    	int offset = msg.getInt("offset");
    	byte[] buf = msg.getByteArray("buffer");
	mGoModel.parseUsersData(buf, len, offset);
    }




    void parseGroupsData(Map map){

        mGoModel.parseGroupsData(map);
    }



    void handleUsersRsp(byte[] buf,int len,int offset) {
        xHelper.log("handleListRsp");
        Bundle msg = new Bundle();
	byte[] cbuf = new byte[len];
        System.arraycopy(buf,0,cbuf,0,len);
        msg.putInt("length",len);
        msg.putInt("offset",offset);
	msg.putByteArray("buffer",cbuf);
        //if(mGoModel.parseUsersData(buf, len, offset)==0) {
                Message message = new Message();
                message.what = MSG_RSP_LIST_USER;
		message.obj=msg;
                sendMessage(message);
        //}
    }


    void handleGroupsRsp(byte[] buf,int len,int offset) {
        xHelper.log("handleGroupsRsp");
        Bundle msg = new Bundle();
        byte[] cbuf = new byte[len];
        System.arraycopy(buf,0,cbuf,0,len);
        msg.putInt("length",len);
        msg.putInt("offset",offset);
        msg.putByteArray("buffer",cbuf);
        //if(mGoModel.parseUsersData(buf, len, offset)==0) {
        Message message = new Message();
        message.what = MSG_RSP_LIST_GROUP;
        message.obj=msg;
        sendMessage(message);
        //}
    }

    protected static final Object sLock = new Object();
    void handleListRsp(byte[] buf,int len,int offset) {
        xHelper.log("handleListRsp");
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if (err == 200) {
		int ret=0;
        	synchronized (sLock) {
            	ret = mGoModel.parseDeskListData(buf, len, o.v);
			}
			if(ret==0){
                Message message = new Message();
                message.what = MSG_RSP_LIST;
                sendMessage(message);
            }
        }
    }
    void handleLogoutRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if (err == 200) {
            //update grid view
            Message message = new Message();
            message.what = MSG_RSP_USER_LOGOUT;
            sendMessage(message);
        }
    }
    void handleReadyRsp(byte[] buf,int len,int offset) {
        //notify:ready,uid,status,name,wins/totals
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if(err==200) {
            xHelper.log("I am ready");
            setMyStatus(User.USER_READY);
            Message message = new Message();
            message.what = MSG_RSP_USER_READY;
            sendMessage(message);
        }
    }
    void handleLeaveRsp(byte[] buf,int len,int offset) {
        //notify:ready,uid,status,name,wins/totals
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if(err==200) {
            xHelper.log("leave the desk");
        }
    }
    void kill(int sid,int x,int y) {
        mGoGame.kill(sid,x,y);
    }

    void kill(int x,int y) {
        mGoGame.kill(x,y);
    }
    void setLastStep(int x,int y) {
				
        mGoGame.setLastStep(x,y);
    }
    int getLastStepX() {
				if(mGoGame==null) return 1;
        return mGoGame.getLastStepX();
    }
    int getLastStepY() {
				if(mGoGame==null) return 1;
        return mGoGame.getLastStepY();
    }

    void setStep(int sid,int x,int y) {
        mGoGame.setStep(sid,x,y);
    }
    void setUndoStep(int sid,int x,int y) {
        mGoGame.setStep(sid,x,y);
    }
    public void AddSgf(int id,String sgf) {
        mDbModel.AddSgf(id,sgf);
    }
    public int queryDB() {
        if(mDbModel==null) return 0;
        return mDbModel.queryDB();
    }
    public void updateSgfNew(SgfHeader h) {
        mDbModel.updateSgfNew(h);
    }
    public void delSgf(int pos) {
        mDbModel.delSgf(pos);
    }
    public boolean isInDesk() {
        if(getMyDesk()!=null) return true;
        return false;
    }
    public boolean isInGame() {
        if(getStatus()==IN_GAME) return true;
        return false;
    }
    public boolean isDianMu() {
        if(mSubStatus==IN_DIANMU && isInGame()) return true;
        return false;
    }
    int getMySide() {
        return mGoModel.getMySide();
    }
    void handleStepRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int err = xHelper.getInt(buf, len, o, ',');
        if(err==200) {
            int x = xHelper.getInt(buf, len, o, ',');
            int y = xHelper.getInt(buf, len, o, ',');
            int deadlen = xHelper.getInt(buf, len, o, ',');
            if(deadlen>0) {
                int deadsid = xHelper.getInt(buf, len, o, ',');
                for ( int j = 0 ; j < deadlen ; j++) {
                    int xx = xHelper.getInt(buf, len, o, ',');
                    int yy = xHelper.getInt(buf, len, o, ',');
                    kill(deadsid,xx,yy);
                }
            }
            setLastStep(x,y);
            setStep(getMySide(),x,y);
            MyMsg mm = new MyMsg(x,y);
            Message message = new Message();
            message.what = MSG_RSP_STEP;
            message.obj=mm;
            sendMessage(message);
        }
    }

    void handlePassRsp(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        String result = xHelper.getStr(buf, len, o, ',');
        if(result.compareTo("yes")==0) {
            Message message = new Message();
            message.what = MSG_RSP_USER_PASS;
            sendMessage(message);
        }
    }
    void handleScoreRsp(byte[] buf,int len,int offset) {
        MyMsg mm = new MyMsg(buf,len,offset);
        Message message = new Message();
        message.what = MSG_NTFY_ARRIVE_SCORE;
        message.obj=mm;
        sendMessage(message);
        //parse the dianmu result and show a dialog to let user confirm whether agree the dianmu result
    }
    void onResponse(byte[] buf,int len,int offset) {
        int i = 0;
        int start = offset;
        int count = 0;
        while(offset<len) {
            if(buf[offset]==',') {
                offset++;
                break;
            }
            count++;
            offset++;
        }
        String type= new String(buf,start,count);
        if(type.compareTo("diag")==0) {
            handleDiagRsp(buf,len,offset);
        } else if(type.compareTo("stat")==0) {
            handleStatRsp(buf,len,offset);
        } else if(type.compareTo("invite")==0) {
            handleInviteRsp(buf,len,offset);
        } else if(type.compareTo("login")==0) {
            handleLoginRsp(buf,len,offset);
        } else if(type.compareTo("register")==0) {
            handleRegisterRsp(buf,len,offset);
        } else if(type.compareTo("registergroup")==0) {
            handleRegisterGroupRsp(buf,len,offset);

        } else if(type.compareTo("resume")==0) {
            handleResumeRsp(buf,len,offset);
        } else if(type.compareTo("observe")==0) {
            handleObserveRsp(buf,len,offset);
        } else if(type.compareTo("join")==0) {
            handleJoinRsp(buf,len,offset);
        } else if(type.compareTo("join_invite")==0) {
            handleJoinInviteRsp(buf,len,offset);
        } else if(type.compareTo("list")==0) {
            handleListRsp(buf,len,offset);
        } else if(type.compareTo("users")==0) {
            handleUsersRsp(buf,len,offset);
        } else if(type.compareTo("groups")==0) {
            handleGroupsRsp(buf, len, offset);
        } else if(type.compareTo("logout")==0) {
            handleLogoutRsp(buf, len, offset);
        } else if(type.compareTo("ready")==0) {
            handleReadyRsp(buf, len, offset);
        } else if(type.compareTo("leave")==0) {
            handleLeaveRsp(buf, len, offset);
        } else if(type.compareTo("step")==0) {
            mLastReqData=null;
            handleStepRsp(buf, len, offset);
        } else if(type.compareTo("score")==0) {
            handleScoreRsp(buf, len, offset);
        } else if(type.compareTo("pass")==0) {
            handlePassRsp(buf,len,offset);
        } else if(type.compareTo("stat-room")==0) {
            handleStatRoomRsp(buf, len, offset);
        }
    }
	

    void handleRejectInviteNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int uid = xHelper.getInt(buf, len, o, ',');
        Message message = new Message();
        message.what = MSG_NTFY_REJECT_INVITE;
		message.arg1=uid;
        sendMessage(message);
	}
    void handleInviteNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int uid = xHelper.getInt(buf, len, o, ',');
		String rank = xHelper.getStr(buf,len,o,',');
        int wins = xHelper.getInt(buf, len, o, ',');
        int loses = xHelper.getInt(buf, len, o, ',');
		String name = xHelper.getStr(buf,len,o,',');
        User u1 = mGoModel.getUser(uid);
		if(u1==null){
        	u1 = mGoModel.addUser(uid);
		}
		u1.name=name;	
		u1.rank=rank;
		u1.wins=wins;
		u1.loses=loses;
        Message message = new Message();
        message.what = MSG_NTFY_INVITE_REQ;
		message.arg1=uid;
        sendMessageToService(message);
	}
    void handleInviteDoneNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int uid = xHelper.getInt(buf, len, o, ',');
		String rank = xHelper.getStr(buf,len,o,',');
        int wins = xHelper.getInt(buf, len, o, ',');
        int loses = xHelper.getInt(buf, len, o, ',');
		String name = xHelper.getStr(buf,len,o,',');
        User u1 = mGoModel.getUser(uid);
		if(u1==null){
        	u1 = mGoModel.addUser(uid);
		}
		u1.name=name;	
		u1.rank=rank;
		u1.wins=wins;
		u1.loses=loses;
        uid = xHelper.getInt(buf, len, o, ',');
		rank = xHelper.getStr(buf,len,o,',');
        wins = xHelper.getInt(buf, len, o, ',');
        loses = xHelper.getInt(buf, len, o, ',');
		name = xHelper.getStr(buf,len,o,',');
        User u2 = mGoModel.getUser(uid);
		if(u2==null){
        	u2 = mGoModel.addUser(uid);
		}
		u2.name=name;	
		u2.rank=rank;
		u2.wins=wins;
		u2.loses=loses;
        Bundle msg = new Bundle();
		msg.putInt("u1",u1.id);
		msg.putInt("u2",u2.id);
		join_invite();
		/*
        Message message = new Message();
        message.what = MSG_NTFY_INVITE_DONE;
		message.obj=msg;
        sendMessageToService(message);
		*/
    }


    void handleObserveStartNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        String mmap=null;
        int did = xHelper.getInt(buf, len, o, ',');
        int num = xHelper.getInt(buf, len, o, ',');
        if(num>0) {
            mmap = xHelper.getStr(buf, len, o, ',');
        }
        xHelper.log("observe_start,did="+did+"num="+num+"\n"+mmap);
        loadmap(num,mmap);
				int lx=xHelper.getInt(buf, len, o, ',');
				int ly=xHelper.getInt(buf, len, o, ',');
				setLastStep(lx,ly);
        Message message = new Message();
				MyMsg mm = new MyMsg(lx,ly);
				message.obj=mm;
        message.what = MSG_NTFY_OBSERVE_START;
        sendMessage(message);
		
		GoLogic logic = this.getGoLogic(mBoardSize);
		Desk d = this.getMyDesk();
		if(d!=null) {
			if(d.black!=null) {
				logic.setSgfHeader("PB",d.black.name);
				logic.setSgfHeader("BR",d.black.rank);
			}
			if(d.white!=null) {
				logic.setSgfHeader("PW",d.white.name);
				logic.setSgfHeader("WR",d.white.rank);
			}
		}
    }

    void handleJoinNoti(byte[] buf,int len,int offset) {
        //notify:join,did,sid,uid,status,name,wins/totals
        x_Integer o = new x_Integer(offset);
        int did = xHelper.getInt(buf, len, o, ',');
        int sid = xHelper.getInt(buf, len, o, ',');
        int uid = xHelper.getInt(buf, len, o, ',');
        int status = xHelper.getInt(buf, len, o, ',');
        String rank = xHelper.getStr(buf, len, o, '|');
        String name = xHelper.getStr(buf, len, o, ',');
        int wins = xHelper.getInt(buf, len, o, '/');
        int totals = xHelper.getInt(buf, len, o, ',');
        User u = mGoModel.addUser(uid,did,sid);
        u.name = name;
		u.rank = rank;
        u.status = status;
        u.wins = wins;
        u.totals = totals;
		
		
        Message message = new Message();
        message.what = MSG_NTFY_USER_JOIN;
        message.arg1=uid;
        message.obj=u;
        sendMessage(message);

    }
    void handleMessageNoti(byte[] buf,int len,int offset) {
        //notify:join,did,sid,uid,status,name,wins/totals
        x_Integer o = new x_Integer(offset);
		int uid = xHelper.getInt(buf,len,o,'[');
        String bc = xHelper.getStr(buf, len, o, ']');
        Message message = new Message();
        message.what = MSG_NTFY_MESSAGE;
        Bundle msg = new Bundle();
		msg.putInt("uid",uid);
		msg.putString("content",bc);
		message.obj=msg;
        sendMessageToService(message);
	}
    void handleBroadcastNoti(byte[] buf,int len,int offset) {
        //notify:join,did,sid,uid,status,name,wins/totals
        x_Integer o = new x_Integer(offset);
        String bc = xHelper.getStr(buf, len, o, ']');
        Message message = new Message();
        message.what = MSG_NTFY_BROADCAST;
		message.obj=bc;
        sendMessageToService(message);
	}
    void handleAlarmNoti(byte[] buf,int len,int offset) {
        //notify:join,did,sid,uid,status,name,wins/totals
        x_Integer o = new x_Integer(offset);
        int did = xHelper.getInt(buf, len, o, ',');
        Message message = new Message();
        message.what = MSG_NTFY_ALARM;
        message.arg1 = did;
        sendMessage(message);
    }
    void handlePassNoti(byte[] buf,int len,int offset) {
        xHelper.log("handlePassNoti");
        x_Integer o = new x_Integer(offset);
        int sid = xHelper.getInt(buf, len, o, ',');
        Message message = new Message();
        message.what = MSG_NTFY_USER_PASS;
        message.arg1 = sid;
        sendMessage(message);
    }
    void handleLeaveNoti(byte[] buf,int len,int offset) {
        xHelper.log("handleLeaveNoti");
        x_Integer o = new x_Integer(offset);
        int uid = xHelper.getInt(buf, len, o, ',');
        userLeave(uid);
        Message message = new Message();
        message.what = MSG_NTFY_USER_LEAVE;
        message.arg1 = uid;
        sendMessage(message);
    }
    void handleReadyNoti(byte[] buf,int len,int offset) {
        //notify:ready,uid,status,name,wins/totals
        x_Integer o = new x_Integer(offset);
        int uid = xHelper.getInt(buf, len, o, ',');
        User u = mGoModel.getUser(uid);
        u.status=User.USER_READY;
        Message message = new Message();
        message.what = MSG_NTFY_USER_READY;
        sendMessage(message);
    }
    void handleStartNoti(byte[] buf,int len,int offset) {
        xHelper.log("game started");
        x_Integer o = new x_Integer(offset);
        int did = xHelper.getInt(buf, len, o, ',');
        int sid = xHelper.getInt(buf, len, o, ',');
        mStepTimeout=xHelper.getInt(buf,len,o,',');
        newGame(mBoardSize);
        setCurTurn(sid);
        changeStatus(IN_GAME);
        mSubStatus=UNKNOWN;
        setMyStatus(User.USER_PLAYING);
        Message message = new Message();
        message.what = MSG_NTFY_GAME_START;
        sendMessage(message);
    }
    void handleStartDianMuNoti(byte[] buf,int len,int offset) {
        //parse the dianmu result and show a dialog to let user confirm whether agree the dianmu result
        mSubStatus=IN_DIANMU;
        MyMsg mm = new MyMsg(buf,len,offset);
        Message message = new Message();
        message.what = MSG_NTFY_ARRIVE_START_DIANMU;
        message.obj=mm;
        sendMessage(message);

    }
    void handleTimeoutNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        String reason = xHelper.getStr(buf, len, o, ',');
        int side = xHelper.getInt(buf,len,o,',');
        Message message = new Message();
        message.arg1 = side;
        if(reason.compareTo("step")==0) {
            message.what = MSG_NTFY_TIMEOUT_STEP;
        } else if(reason.compareTo("join")==0) {
            message.what = MSG_NTFY_TIMEOUT_JOIN;
        } else if(reason.compareTo("dianmu")==0) {
            message.what = MSG_NTFY_TIMEOUT_DIANMU;
        }
        sendMessage(message);
    }
	
	/*notify:game_end,1,0,0,22998,17k,14370,17k,[7,FG ;GG ;GH ;FH ;HG ;HH ;GF GG ;] */
    void handleGameEndNoti(byte[] buf,int len,int offset) {
        xHelper.log("game_end received");
        x_Integer o = new x_Integer(offset);
        int win = xHelper.getInt(buf, len, o, ',');
        int bmu = xHelper.getInt(buf, len, o, ',');
        int wmu = xHelper.getInt(buf, len, o, ',');
        int uid = xHelper.getInt(buf, len, o, ',');
		String rank=xHelper.getStr(buf,len,o,',');
		setRank(uid,rank);
        uid = xHelper.getInt(buf, len, o, ',');
		rank=xHelper.getStr(buf,len,o,',');
		setRank(uid,rank);
        Bundle msg = new Bundle();
        msg.putInt("win",win);
        msg.putInt("bmu",bmu);
        msg.putInt("wmu",wmu);
		
		//skip '['
		o.v++;
		String record = xHelper.getStr(buf,len,o,']');
		saveGameRecord(record);
		
        changeStatus(IN_DESK);
		if(getMyStatus()!=User.USER_OBSERVE){
        setMyStatus(User.USER_JOIN);
		}
        Message message = new Message();
        message.what = MSG_NTFY_GAME_END;
        message.obj=msg;
        sendMessage(message);
    }
    void handleNextNoti(byte[] buf,int len,int offset) {
        //get next side
        x_Integer o = new x_Integer(offset);
        int next = xHelper.getInt(buf, len, o, ',');
        mStepTimeout=xHelper.getInt(buf,len,o,',');
        setCurTurn(next);
        Message message = new Message();
        message.what = MSG_NTFY_NEXT_SIDE;
        sendMessage(message);
    }
    void handleContinueGoNoti(byte[] buf,int len,int offset) {
        //get next side
        x_Integer o = new x_Integer(offset);
        int sid = xHelper.getInt(buf, len, o, ',');
        int num = xHelper.getInt(buf, len, o, ',');
        if(num>0) {
            for ( int i = 0 ; i < num ; i++) {
                int xx = xHelper.getInt(buf, len, o, ',');
                int yy = xHelper.getInt(buf, len, o, ',');
                setUndoStep(1,xx,yy);
            }
        }
        num = xHelper.getInt(buf, len, o, ',');
        if(num>0) {
            for ( int i = 0 ; i < num ; i++) {
                int xx = xHelper.getInt(buf, len, o, ',');
                int yy = xHelper.getInt(buf, len, o, ',');
                setUndoStep(2,xx,yy);
            }
        }
        mSubStatus=UNKNOWN;
        Message message = new Message();
        message.what = MSG_NTFY_CONTINUE_GO;
        sendMessage(message);
    }
    void handleUndoDeadNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int sid = xHelper.getInt(buf, len, o, ',');
        int num = xHelper.getInt(buf, len, o, ',');
        if(num>0) {
            for ( int i = 0 ; i < num ; i++) {
                int xx = xHelper.getInt(buf, len, o, ',');
                int yy = xHelper.getInt(buf, len, o, ',');
                setUndoStep(sid,xx,yy);
            }
        }
        Message message = new Message();
        message.what = MSG_NTFY_UNDO_DEAD;
        sendMessage(message);
    }
    void handleSetDeadNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int sid = xHelper.getInt(buf, len, o, ',');
        int x = xHelper.getInt(buf, len, o, ',');
        int y = xHelper.getInt(buf, len, o, ',');
        kill(sid,x,y);
        MyMsg mm = new MyMsg(x,y);
        Message message = new Message();
        message.what = MSG_NTFY_SET_DEAD;
        message.obj=mm;
        sendMessage(message);
    }

    void handleDoneNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int sid = xHelper.getInt(buf, len, o, ',');
        Message message = new Message();
        message.what = MSG_NTFY_USER_DONE;
        message.arg1=sid;
        sendMessage(message);
    }
    void handleStepNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int sid = xHelper.getInt(buf, len, o, ',');
        int x = xHelper.getInt(buf, len, o, ',');
        int y = xHelper.getInt(buf, len, o, ',');
        int deadlen = xHelper.getInt(buf, len, o, ',');
        if(deadlen>0) {
            int deadsid = xHelper.getInt(buf, len, o, ',');
            for ( int i = 0 ; i < deadlen ; i++) {
                int xx = xHelper.getInt(buf, len, o, ',');
                int yy = xHelper.getInt(buf, len, o, ',');
                kill(deadsid,xx,yy);
            }
        }
        setLastStep(x,y);
        setStep(sid,x,y);
        MyMsg mm = new MyMsg(x,y);
        Message message = new Message();
        message.what = MSG_NTFY_STEP;
        message.obj=mm;
        sendMessage(message);
    }


    void handleSetStepTimeNoti(byte[] buf,int len,int offset) {
        x_Integer o = new x_Integer(offset);
        int did = xHelper.getInt(buf, len, o, ',');
        int min = xHelper.getInt(buf, len, o, ',');

        Desk desk = getDesk(did - 1);
        desk.step_time_out=min;
        Message message = new Message();
        message.what = MSG_NTFY_SET_STEP_TIME;
        sendMessage(message);
    }

    public static <T> T fromJsonToObject(String json, Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return (T) mapper.readValue(json, valueType);
        } catch (JsonParseException e) {
        } catch (JsonMappingException e) {
        } catch (IOException e) {
        }
        return null;
    }


    void handleStatRoomRsp(byte[] buf,int len,int offset) {

        x_Integer o = new x_Integer(offset);
        String jsons = xHelper.getJson(buf,len,o);


        Map map = fromJsonToObject(jsons,Map.class);


        List<Map> desks = (List<Map>)map.get("desks");

//        Desk desk = getDesk(did-1);
//        desk.step_time_out=min;

        for (Map desk : desks) {

            int did = xHelper.l(desk,GoConstants.JSON_KEY.DID).intValue();

            Desk desk2 = getDesk(did-1);
           desk2.step_time_out=xHelper.l(desk,GoConstants.JSON_KEY.STEP_TIME).intValue();
        }
        Message message = new Message();
        message.what = MSG_NTFY_STAT_ROOM;
        sendMessage(message);
    }








    int mResumeTimeout=60*3;
    int getResumeTimeout(){
        return mResumeTimeout;
    }

    int mStepTimeout=60*3;
    int getStepTimeout(){
        return mStepTimeout;
    }
	
	void sendHeartBeatNoti() {
                
        Message message = new Message();
        message.what = MSG_NTFY_HEART_BEAT;        
        sendMessage(message);
    }
    void showToast(String s) {
        Toast.makeText(mApp.getContext(), s, Toast.LENGTH_LONG).show();
    }
    void onNotify(byte[] buf,int len,int offset) {
	
        int start = offset;
        int count = 0;
        while(offset<len) {
            if(buf[offset]==',') {
                offset++;
                break;
            }
            count++;
            offset++;
        }
        String type= new String(buf,start,count);
        //notify,areyouok,
        if(type.compareTo("areyouok")==0) {
            xHelper.log("areyouok?");
			iamok();
			
            
        } else if(type.compareTo("observe_start")==0) {
            handleObserveStartNoti(buf,len,offset);
        } else if(type.compareTo("join")==0) {
            xHelper.log("handleJoinNoti");
            handleJoinNoti(buf,len,offset);
        } else if(type.compareTo("alarm")==0) {
            xHelper.log("handleAlarmNoti");
            handleAlarmNoti(buf,len,offset);
        } else if(type.compareTo("broadcast")==0) {
            xHelper.log("handleBraodcastNoti");
            handleBroadcastNoti(buf,len,offset);
        } else if(type.compareTo("message")==0) {
            xHelper.log("handleMessageNoti");
            handleMessageNoti(buf,len,offset);
        } else if(type.compareTo("invite")==0) {
            xHelper.log("handleInviteNoti");
            handleInviteNoti(buf,len,offset);
        } else if(type.compareTo("reject_invite")==0) {
            handleRejectInviteNoti(buf,len,offset);
        } else if(type.compareTo("invite_done")==0) {
            xHelper.log("handleBraodcastNoti");
            handleInviteDoneNoti(buf,len,offset);
        } else if(type.compareTo("leave")==0) {
            handleLeaveNoti(buf,len,offset);
        } else if(type.compareTo("pass")==0) {
            handlePassNoti(buf,len,offset);
        } else if(type.compareTo("ready")==0) {
            handleReadyNoti(buf,len,offset);
        } else if(type.compareTo("talk")==0) {
            //notify:ready,uid,status,name,wins/totals
            MyMsg mm = new MyMsg(buf,len,offset);
            Message message = new Message();
            message.what = MSG_NTFY_USER_TALK;
            message.obj=mm;
            sendMessage(message);
        } else if(type.compareTo("start")==0) {
            handleStartNoti(buf,len,offset);
        } else if(type.compareTo("start_dianmu")==0) {
            handleStartDianMuNoti(buf,len,offset);
        } else if(type.compareTo("timeout")==0) {
            handleTimeoutNoti(buf,len,offset);
        } else if(type.compareTo("game_end")==0) {
            handleGameEndNoti(buf,len,offset);
        } else if(type.compareTo("next")==0) {
            handleNextNoti(buf,len,offset);
        } else if(type.compareTo("setdead")==0) {
            mLastReqData=null;
            handleSetDeadNoti(buf,len,offset);
        } else if(type.compareTo("undodead")==0) {
            handleUndoDeadNoti(buf,len,offset);
        } else if(type.compareTo("done")==0) {
            handleDoneNoti(buf,len,offset);
        } else if(type.compareTo("continuego")==0) {
            handleContinueGoNoti(buf,len,offset);
        } else if(type.compareTo("step")==0) {
            handleStepNoti(buf,len,offset);
        } else if(type.compareTo("set_step_time")==0) {
            handleSetStepTimeNoti(buf, len, offset);
        }



    }

    int mSoTimeout = 60*1000;
    class EvtListener implements xTcpEventListener {
        public void onConnected(xTcpThread thread) {
            xHelper.log("connect with server OK\n");
            thread.setSoTimeout(mSoTimeout);
            Message message = new Message();
            changeStatus(ONLINE);
            bBrokenEventInjected=false;
            message.what = MSG_CONNECT_OK;
            sendMessage(message);
        }
        public void onConnectTimeOut(xTcpThread thread) {
            xHelper.log("onConnectTimeout");
            if(inSilentReconnect<mMaxSilentRetries) {
                Message message = new Message();
                message.what = MSG_CONNECT_TIMEOUT;
                sendMessageToService(message);
            } else {
	        onSilentError();
                Message message = new Message();
                message.what = MSG_CONNECT_FAILED;
                sendMessage(message);
                Message message2 = new Message();
                message2.what = MSG_CONNECT_FAILED;
                sendMessageToService(message2);
            }
        }
        public void onDisconnected(xTcpThread thread) {
            changeStatus(OFFLINE);
            xHelper.log("onDisconnected");
            if(bBrokenEventInjected==true) return;
            bBrokenEventInjected=true;
            Message message = new Message();
            message.what = MSG_CONNECT_BROKEN;
            sendMessage(message);
        }
        public void onSendFailed(String s) {
            Message message = new Message();
			message.obj=s;
            message.what = MSG_SEND_FAILED;
            sendMessage(message);
			if(mLastIp!=null) connectServer(mLastIp,mLastPort);
        }
        byte[] mLastBuff=null;
        public void onDataReceived(xTcpThread thread,byte[] bbuf,int len) {
            int offset = 0;
            int bFound=0;
            int start=0;
            int startOffset=0;
            xHelper.log("received:"+new String(bbuf,0,len));
			sendHeartBeatNoti();
            if(mLastBuff==null) {
                byte[] newb = new byte[len];
                System.arraycopy(bbuf,0,newb,0,len);
                mLastBuff=newb;
            } else {
                xHelper.log("combine the package");
                byte[] newb = new byte[mLastBuff.length+len];
                System.arraycopy(mLastBuff,0,newb,0,mLastBuff.length);
                System.arraycopy(bbuf,0,newb,mLastBuff.length,len);
                len+=mLastBuff.length;
                mLastBuff=newb;
                xHelper.log("received:"+new String(newb,0,len));
            }
            String type = null;
            byte[] buf = mLastBuff;
            while(offset<len) {
                if(buf[offset]==':' && type==null) {
                    type= new String(buf,start,offset-start);

                    offset++;
                    startOffset=offset;
                    continue;
                }
                if( (offset+3<len) && buf[offset]=='\r' &&
                        buf[++offset]=='\n' &&
                        buf[++offset]=='\r' &&
                        buf[++offset]=='\n') {
                    bFound++;
                    if(type!=null) {
                        xHelper.log(type);
                        if(type.compareTo("response")==0) {
                            onResponse(buf,len,startOffset);
                        }
                        if(type.compareTo("notify")==0) {
                            onNotify(buf,len,startOffset);
                        }
                        type=null;
                    }
                    startOffset=0;
                    start = ++offset;
                    continue;
                }
                offset++;
            }
            if(bFound==0) {
                mLastBuff=buf;
            } else {
                mLastBuff=null;
            }
        }
    }
}

