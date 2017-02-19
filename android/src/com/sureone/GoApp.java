package com.sureone;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sureone.igs.IgsController;
import android.content.Context;

// import org.acra.*;
// import org.acra.annotation.*;
/*
@ReportsCrashes(formKey = "",
								formUri = "http://bugfix.sinaapp.com/add.php",
								mode = ReportingInteractionMode.TOAST,
                forceCloseDialogAfterToast = false, // optional, default false
                resToastText = R.string.crash_toast_text) 
				*/
public class GoApp{
    private static final String TAG = "goapp";
    public static final String PREFS_NAME = "settings";
    MyService mService = null;
    IgsController mIgsController=null;
    GoController mGoController=null;
    SharedPreferences settings=null;
    xTcpThread mBoardConn=null;	
	
	MessageThread mCurrentThread=null;

	static GoApp mApp = null;
	
	public static GoApp getInstance(){
		if(mApp==null){
			mApp =new GoApp();
		}
		return mApp;
	}
	
	Context mContext;

    public void onCreate(Context context) {
		
		mContext = context;
		settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        
    }
	
	Context getApplication(){
		return mContext.getApplicationContext();
	}

    void stopMyService(){
        mContext.stopService(new Intent(mContext,MyService.class));
    }
    public IgsController getIgsController() {
        if(mIgsController==null) mIgsController = new IgsController();
        return mIgsController;
    }
	
	public Context getContext(){
		return mContext;
	}
    public GoController getGoController() {
        if(mGoController==null) {
            mGoController = new GoController();
            mGoController.setApp(mContext);
	    mGoController.initDB(mContext);
        }
        return mGoController;
    }

    public boolean getTouchMode() {
        return settings.getBoolean("touchMode",false);
    }
    public void setTouchMode(boolean v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("touchMode", v);
        editor.commit();
    }
	
    public boolean getIconHide() {
        return settings.getBoolean("iconHide",false);
    }
    public void setIconHide(boolean v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("iconHide", v);
        editor.commit();
    }	
    public boolean getAdMode() {
        return settings.getBoolean("adMode",true);
    }
    public void setAdMode(boolean v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("adMode", v);
        editor.commit();
    }
    public boolean getSilentMode() {
        return settings.getBoolean("silentMode", false);
    }
    public void setSilentMode(boolean v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", v);
        editor.commit();
    }
    public int getUID() {
        return settings.getInt("uid", 0);
    }
    public void setUID(int v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("uid", v);
        editor.commit();
    }
	
    public int getHeadIcon(){
        return settings.getInt("headicon", 0);
    }
    public void setHeadIcon(int v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("headicon", v);
        editor.commit();
    }	
	
	public int getStone() {
        return settings.getInt("stone", 5);
    }
    public void setStone(int v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("stone", v);
        editor.commit();
    }
	
    public int getSgfStepTime() {
        return settings.getInt("sgfStepTime", 1);
    }
    public void setSgfStepTime(int v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sgfStepTime", v);
        editor.commit();
    }
    public String getIgsName() {
        return settings.getString("IgsName", "yymoto");
    }
    public String getIgsPin() {
        return settings.getString("IgsPin", "asdfjkl;");
    }
    public String getIgsServer() {
        return settings.getString("IgsServer", "igs.joyjoy.net");
    }
    public int getIgsPort() {
        return settings.getInt("IgsPort", 6969);
    }

    public String getEmail() {
        return settings.getString("email", "");
    }
    public String getPin() {
        return settings.getString("pin", "");
    }
    public void setPin(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pin", p);
        editor.commit();
    }
    public String getQQAccessToken() {
        return settings.getString("qqat", null);
    }
    public void setQQAccessToken(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("qqat", p);
        editor.commit();
    }
    public String getQQAccessSecret() {
        return settings.getString("qqas", null);
    }
    public void setQQAccessSecret(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("qqas", p);
        editor.commit();
    }

    public String getSinaAccessToken() {
        return settings.getString("sinaat", null);
    }
    public void setSinaAccessToken(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("sinaat", p);
        editor.commit();
    }
    public String getSinaAccessSecret() {
        return settings.getString("sinaas", null);
    }
    public void setSinaAccessSecret(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("sinaas", p);
        editor.commit();
    }
    public String getHost() {
        return settings.getString("hot", "xdroid.us:81");
    }

    public void setHost(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("host", p);
        editor.commit();
    }

    public String getIp() {
        //return "ali.3636360.com:91";
        //return "192.168.1.102:9091";
        //return "42.121.129.37:9091";
        //return "192.168.1.105:9091";
        return settings.getString("ip", "pk.wcare.cn:19010");
    }
    public void setIp(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ip", p);
        editor.commit();
    }

	public String getDownload() {
        return settings.getString("download", "http://easygui.googlecode.com/files/goapp.apk");
    }
    public void setDownload(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("download", p);
        editor.commit();
    }
    public String getThirdBoot() {
        return "https://nt.wcare.cn/go/web/boot";
    }
    public String getSecondBoot() {
        return "http://wcare.cn/go/web/boot";
    }
    public String getDefaultBoot() {
        return "http://pk.wcare.cn:19022/go/web/boot";
    }


    public void setIgsName(String e) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IgsName", e);
        editor.commit();
    }
    public void setIgsPin(String p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IgsPin", p);
        editor.commit();
    }
    public void setEmail(String e) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", e);
        editor.commit();
    }

    public void setMyService(MyService s) {
        mService = s;
    }

    public MyService getMyService() {
        return mService;
    }


}
