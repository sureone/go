package com.sureone;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "user")

public class User {

    public static final int USER_LOGIN = 1;
    public static final int USER_JOIN = 2;
    public static final int USER_READY= 3;
    public static final int USER_PLAYING=4;
    public static final int USER_OBSERVE=5;
	public void setHeadIcon(int icon){
		this.icon=icon;
	}
	public int getHeadIcon(){
		return this.icon;
	}
    public String name;
	
	@DatabaseField(id = true)     
    public int id;
	public int icon=0;
    public int wins;
    public int loses;
    public int totals;
    public int status;
    public int ref=0;
    public int did;
    public int sid;
    public boolean isOffline=false;
	public int score;
	public int maxScore;
	public String rank="";
	public int isadmin=0;
    public int groupid;
    //所在组的管理员
    public int groupOwner;
    public String groupName;

}
