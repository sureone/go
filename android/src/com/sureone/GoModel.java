package com.sureone;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.UnsupportedEncodingException;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View.OnClickListener;
import com.sureone.model.Group;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import us.xdroid.util.HashedMapList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GoModel {
    public int mCurTurn=0;
    public int mDeskNumber = 0;
    public int mUserNumber = 0;
    public Desk[] mDesks=null;
    public ArrayList<User> mUsers=null;
    public Desk mMyDesk=null;
    public User mMyUser=null;

    Context mContext;

    public static final String PREFS_NAME = "settings";
    static SharedPreferences settings = null;

    public void init(Context context){
        mContext=context;
        settings = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public static void setPrefInt(String k, int v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(k, v);
        editor.commit();
    }

    public static int getPrefInt(String k, int def) {
        return settings.getInt(k, def);
    }

    public static String getPrefString(String k, String def) {
        return settings.getString(k, def);
    }

    public static void setPrefString(String k, String v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(k, v);
        editor.commit();
    }

    public GoModel() {
        mUsers = new ArrayList<User>();

        mRankMap.put(26,"17k");
        mRankMap.put(25,"16k");
        mRankMap.put(24,"15k");
        mRankMap.put(23,"14k");
        mRankMap.put(22,"13k");
        mRankMap.put(21,"12k");
        mRankMap.put(20,"11k");
        mRankMap.put(19,"10k");
        mRankMap.put(18,"9k");
        mRankMap.put(17,"8k");
        mRankMap.put(16,"7k");
        mRankMap.put(15,"6k");
        mRankMap.put(14,"5k");
        mRankMap.put(13,"3k");
        mRankMap.put(12,"3k");
        mRankMap.put(11,"2k");
        mRankMap.put(10,"1k");
        mRankMap.put(9,"1d");
        mRankMap.put(8,"2d");
        mRankMap.put(7,"3d");
        mRankMap.put(6,"4d");
        mRankMap.put(5,"5d");
        mRankMap.put(4,"6d");
        mRankMap.put(3,"7d");
        mRankMap.put(2,"8d");
        mRankMap.put(1,"9d");


    }



    int getGroupNumber(){
        return mGroups.size();
	}


    int getSelfMatchNumber(){
        return mSelfMatches.size();
    }

    int getUserNumber(){
        return mUserNumber;
    }
/*
response:users,\r\n
13881,lisb911,3,6,1,1,0,6,17k,\r\n
5819,hh,9,4,2,1,4,6,17k,\r\n
0,EOF,
*/


    public int parseUsersData(byte[] buf, int len, int offset) {
        int ret = 0;
		mUserNumber=0;
		for(int i=0;i<mUsers.size();i++){
			User u = mUsers.get(i);
			u.isOffline=true;
		}
		//skip \r\n
		offset+=2;
        x_Integer o = new x_Integer(offset);
		do{
			int uid=xHelper.getInt(buf, len, o, ',');
			if(uid==0) break;
            User u = getUser(uid);
            if( u == null) {
                u = new User();
                mUsers.add(u);
            }
	    		mUserNumber++;
	    		u.isOffline=false;
			u.id=uid;
			u.name=xHelper.getStr(buf,len,o,',');
			u.wins=xHelper.getInt(buf,len,o,',');
			u.loses=xHelper.getInt(buf,len,o,',');
			u.sid=xHelper.getInt(buf,len,o,',');
			u.did=xHelper.getInt(buf,len,o,',');
			u.score=xHelper.getInt(buf,len,o,',');
			u.maxScore=xHelper.getInt(buf,len,o,',');
			u.rank=xHelper.getStr(buf,len,o,',');
			o.v+=2;
		}while(true);
        Collections.sort(mUsers,new Comparator<User>() {
            public int compare(User a, User b) {
                int ret = xHelper.getRankNo(a.rank)-xHelper.getRankNo(b.rank);
		if(ret==0) ret = b.score-a.score;
		return ret;
            }
        });
		return 0;
	}


    public static HashMap mRankMap=new HashMap();


    public static String rankLabel(int i){

        return (String) mRankMap.get(i);

    }


    public static int getRankValue(String s){

        for (Object o : mRankMap.keySet()) {
            if(mRankMap.get(o).equals(s)){
                return (Integer)o;
            }
        }
        return 0;
    }


    public ArrayList<Group> mGroups = new ArrayList<Group>();



    public HashedMapList mSelfMatches = new HashedMapList();


    public int addSelfMatches(Map map){


        if(map.get("matches")!=null){

            List<Map> matches = (List<Map>)map.get("matches");

            for (Map match : matches) {



                mSelfMatches.addLast(match,"id");

            }

        }
        return mSelfMatches.size();
    }

    public int parseGroupsData(Map map){

        //final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        //final Group pojo = mapper.convertValue(map, MyPojo.class);


        if(map.get("GROUPS")!=null){
            mGroups.clear();
            List<Map> maps = (List<Map>) map.get("GROUPS");
            for (Map map1 : maps) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                Group pojo = mapper.convertValue(map1,Group.class);
                mGroups.add(pojo);

            }

        }
        return mGroups.size();


    }




    //desk_number;did,desk_status,side_number,sid,uid,name,win/total,;...
    public int parseDeskListData(byte[] buf, int len, int offset) {
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
					
                    String rank = xHelper.getStr(buf, len, o, '|');
                    String name = xHelper.getStr(buf, len, o, ',');
                    int wins = xHelper.getInt(buf, len, o, '/');
                    int totals = xHelper.getInt(buf, len, o, ',');
                    int groupid=xHelper.getInt(buf,len,o,',');
                    String groupname=xHelper.getStr(buf,len,o,',');
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
					u.rank = rank;
                    u.groupid=groupid;
                    if(groupid!=0)
                        u.groupName=groupname;

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
    public void removeUser(User u) {
        mUsers.remove(u);
    }
	public int getPlayerNum(){
		return mUsers.size();
	}
	public User getPlayerByIndex(int idx){
		if(idx>=mUsers.size()) return null;
		int j=0;
		User u=null;
		for(int i=0;i<mUsers.size();i++){
			u = mUsers.get(i);
			if(u.isOffline==false)
			{
				if(j==idx) return u;
				j++;
			}
		}
		return u;
	}


    public Group getGroupByIndex(int idx){


        if(idx>=mGroups.size()) return null;
        int j=0;
        Group u=null;
        for(int i=0;i<mGroups.size();i++){
            u = mGroups.get(i);

                if(j==idx) return u;
                j++;

        }
        return u;
    }

    public Map getSelfMatchByIndex(int idx){


        if(idx>=mSelfMatches.size()) return null;
        int j=0;
        Map u=null;
        for(int i=0;i<mSelfMatches.size();i++){
            u = mSelfMatches.get(i);

            if(j==idx) return u;
            j++;

        }
        return u;
    }

    public Group getGroupById(Long idx){

        int j=0;
        Group u=null;
        for(int i=0;i<mGroups.size();i++){
            u = mGroups.get(i);

            if(u.getGROUP_ID().equals(idx)) return u;

        }
        return u;
    }
    public User addUser(int uid,int did,int sid) {
        User u = addUser(uid);
        Desk d = getDesk(did-1);
        u.id=uid;
        u.did=did;
        u.sid=sid;
        if(sid==1) {
            d.black = u;
        } else {
            d.white = u;
        }
        u.ref++;
        return u;
    }

    public User addUser(int uid) {
        User u = getUser(uid);
        if(u==null) {
            u = new User();
            u.id=uid;
            u.ref++;
            mUsers.add(u);
        }
        return u;
    }
    public void setMyUser(User u) {
        mMyUser = u;
        if(u!=null) mMyUser.ref++;
    }
    public int getDeskNum() {
        return mDeskNumber;
    }
    public int getUserNum() {
        return mUserNumber;
    }

    public User getMyPeer() {
        if(mMyDesk!=null && mMyUser!=null) {
            if(mMyUser.sid==1) {
                return mMyDesk.white;
            } else
                return mMyDesk.black;
        }
        return null;
    }

    public User getUser(int uid) {
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
    public User getUser(int did,int sid) {
        User u = null;
        Desk d = getDesk(did-1);
        if(sid==1)
            u = d.black;
        else
            u = d.white;
        return u;
    }
    public void addNewUser(User u,int did,int sid) {
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
    public Desk getDesk(int idx) {
        return mDesks[idx];
    }

    public Desk getMyDesk() {
        return mMyDesk;
    }
    public User getMyUser() {
        return mMyUser;
    }
    public void setMyDesk(Desk d) {
        mMyDesk = d;
    }
    public void setMyDesk(int did,int sid) {
        Desk d= getDesk(did-1);
        //=3 means a observer
        mMyUser.sid=sid;
        mMyUser.did=did;
        if(mMyUser!=null && sid!=3) {
            if(sid==1) d.black = mMyUser;
            else d.white = mMyUser;
            mMyUser.ref++;
        }
        setMyDesk(d);
    }

    public int userLeave(int uid) {
        User u = getUser(uid);
        if (u!=null) {
            int did = u.did;
            Desk d = null;
            if (did!=0) d=getDesk(did-1);
            if(d!=null) {
                if(u.sid == 1) d.black = null;
                else d.white = null;
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
    public int getMySide() {
        if(mMyDesk!=null)
            return mMyUser.sid;
        return -1;
    }
    public void setPeerReady(int uid) {
        User u = getMyPeer();
        if(u!=null) {
            u.status = User.USER_READY;
        }
    }
    public int setMyStatus(int status) {
        if(mMyUser!=null) {
            mMyUser.status = status;
        }
        return -1;
    }
    public int getMyStatus() {
        if(mMyUser!=null) {
            return mMyUser.status;
        }
        return -1;
    }
    public void iLeaveDesk() {
        mMyDesk=null;
        if(mMyUser!=null) {
            mMyUser.did=0;
            mMyUser.sid=0;
            mMyUser.status=User.USER_LOGIN;
        }
    }
}
