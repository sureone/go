package com.sureone;
import android.media.MediaPlayer;
import android.widget.Toast;


import java.util.TimerTask;
import java.util.Timer;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.net.ServerSocket;
import java.net.Socket;
import com.sureone.igs.*;

public class MyService extends Service {
    private static final String TAG = "goapp";

    GoApp app = null;
    Context me;
    IgsController mIgsController=null;
    GoController mGoController=null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        xHelper.log(TAG, "MyService onCreate");
//		player = MediaPlayer.create(this, R.raw.braincandy);
//		player.setLooping(false); // Set looping

        // I read this is supposed to raise the priority of this Service to minimize it being killed
        me = this;
        app = GoApp.getInstance();
        app.setMyService(this);
        mGoController =  app.getGoController();
        mGoController.setDefHandler(mDefHandler);

    }


    Handler mDefHandler = new DefaultHandler();

    @Override
    public void onDestroy() {
//		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        xHelper.log(TAG, "onDestroy");

    }

    @Override
    public void onStart(Intent intent, int startid) {
//		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        xHelper.log(TAG, "onStart");

    }
    void playSound(int id) {
        MediaPlayer mPlayer = null;
        mPlayer=MediaPlayer.create(this, id);
        mPlayer.start();
    }
    class DefaultHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case GoController.MSG_NTFY_USER_JOIN:
                User u = (User)(msg.obj);
                onNotifyUserJoin(u);
                break;
            case GoController.MSG_NTFY_ALARM: {
                onAlarm(msg.arg1);
                break;
            }
            case GoController.MSG_NTFY_INVITE_REQ: {
                onInviteReq(msg.arg1);
                break;
            }
            case GoController.MSG_RSP_JOIN_INVITE_OK: {
                onJoinInviteOK();
                break;
            }
            case GoController.MSG_NTFY_BROADCAST: {
                onBroadcast(msg.obj);
                break;
            }
            case GoController.MSG_NTFY_MESSAGE: {
                onMessage((Bundle)(msg.obj));
                break;
			}
            case GoController.MSG_CONNECT_TIMEOUT: {
	        			//mGoController.silentReconnect();
                break;
			}
            }
        }
    }
	void onBroadcast(Object obj){
		if(obj!=null){
        	String text = (String)obj;
			showToast(text);
		}
	}
	void onAlarm(int did){
		playSound(R.raw.msg);
        String text = String.format(getResources().getString(R.string.alarm), did);
		showToast(text);
		
	}
	void onInviteReq(int uid){
        	xHelper.log(TAG, "onInviteReq");
		playSound(R.raw.msg);
            Intent intent = new Intent(getBaseContext(),com.sureone.InviteDialog.class);
			intent.putExtra("type",GoController.MSG_NTFY_INVITE_REQ);
            intent.putExtra("uid",uid);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.setAction(Intent.ACTION_VIEW);
        	getApplication().startActivity(intent);
	}
	void onMessage(Bundle msg){
        	xHelper.log(TAG, "onInviteReq");
		playSound(R.raw.global);
            Intent intent = new Intent(getBaseContext(),com.sureone.InviteDialog.class);
			intent.putExtra("type",GoController.MSG_NTFY_MESSAGE);
            intent.putExtra("uid",msg.getInt("uid"));
			intent.putExtra("content",msg.getString("content"));
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.setAction(Intent.ACTION_VIEW);
        	getApplication().startActivity(intent);
	}
	void onJoinInviteOK(){
        	xHelper.log(TAG, "onJoinInviteOK");
            Intent intent = new Intent(getBaseContext(),com.sureone.GoActivity.class);
        	intent.putExtra("view","normal");
            intent.putExtra("fullscreen",false);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.setAction(Intent.ACTION_VIEW);
        	getApplication().startActivity(intent);
	}
    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    NotificationManager mNotiMan=null;
    void showNotification(String str) {
        if(mNotiMan==null)
            mNotiMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new Notification(R.drawable.icon,str,System.currentTimeMillis());
        notification.flags=Notification.FLAG_AUTO_CANCEL;
        notification.defaults=Notification.DEFAULT_ALL;
        if(app.getSilentMode()==true) {
            notification.defaults &= ~(Notification.DEFAULT_SOUND);
            notification.defaults &= ~(Notification.DEFAULT_VIBRATE);
        }

        Intent intent = null;
        if(mGoController.getMyStatus()==User.USER_JOIN || mGoController.getMyStatus()==User.USER_READY)
            intent = new Intent(this,GoActivity.class);
        else
            intent = new Intent(this,RoomFlowView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pt=PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this,"",str,pt);
        mNotiMan.notify(192345, notification);
    }

    void onNotifyUserJoin(User u) {
        String str = "\""+u.name+"\""+this.getString(R.string.joingame);
        showNotification(str);
    }
}

