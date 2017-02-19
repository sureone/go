package com.sureone;

import java.util.ArrayList;
import android.view.Window;
import android.view.WindowManager;

import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.HorizontalScrollView;
import android.view.LayoutInflater;
import us.xdroid.util.RemoteResource;
import android.util.TypedValue;
import us.xdroid.util.xUtil;

public class EntryView extends Activity {
    private Button mBtnLogin;
    private Button mBtnReg;
    private EditText mPinText;
    private EditText mEmailText;
    ImageView mLogo=null;
    public xTcpThread mConn;
    EvtListener evtLis=null;
    Intent mDeskListActivity = null;
    public MainHandler mHandler=null;
	
    /** Called when the activity is first created. */
    boolean mWaitingToDeskList = false;


    //String mIp = "192.168.0.101";
    //String mIp = "10.186.0.152";
    String mIp = null;//"184.82.230.120";
    //String mIp="xdroid.us";
    //String mIp = "192.168.0.105";
    int mPort = 81;

    RelativeLayout mContentGroup=null;
    GoController mGoController=null;
    GoApp app = null;
    @Override
    public void onCreate(Bundle parent) {
        super.onCreate(parent);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        app =  GoApp.getInstance();
        mGoController = app.getGoController();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loginview);

        mWaitingToDeskList=false;
        mContentGroup = (RelativeLayout)findViewById(R.id.loginView);

		com.viewpagerindicator.NormalTitle nt = (com.viewpagerindicator.NormalTitle) findViewById(R.id.ntTitle);        
        if(nt!=null) nt.setTitle(getString(R.string.loginview));
		
        xTcpThread conn = mGoController.getConnection();
        evtLis = new EvtListener();

        //conn.setAddress("10.193.78.116", 81);
        //conn.setAddress("192.168.56.101", 81);


        //mLogo=(ImageView) findViewById(R.id.logoImage);
        //mLogo.setImageResource(R.drawable.icon);

        mBtnLogin = (Button)findViewById(R.id.BtnLogin);
        mBtnLogin.setOnClickListener(evtLis);
        mBtnReg = (Button)findViewById(R.id.BtnRegister);
        mBtnReg.setOnClickListener(evtLis);
		

        if (false){
    		LoaderImageView iv = (LoaderImageView)findViewById(R.id.ivIcon);		
    		mSavedIconId = app.getHeadIcon();		
    		RemoteResource rr = null;			
    		int iconid = mSavedIconId;
    		if(mSavedIconId==0){		
    			iconid=1;			
    		}
    		mGoController.getResourceById(iconid,0);
    		iv.setResourceId(iconid);				
            iv.setOnClickListener(evtLis);
        }

        mPinText = (EditText) findViewById(R.id.txtPassword);
        mEmailText = (EditText) findViewById(R.id.txtEmail);
        mEmailText.setTextColor(android.graphics.Color.BLACK);
        mPinText.setTextColor(android.graphics.Color.BLACK);
        mHandler=new MainHandler();
        mEmailText.setText(app.getEmail());
        mPinText.setText(app.getPin());

        //initGameSizeSpin();

        mConn = conn;
        mGoController.setHandler(mHandler);
				String[] ss = app.getIp().split(":");
				mIp=ss[0];
				//mIp="192.168.1.194";
				mPort = Integer.valueOf(ss[1]);
    }


    @Override
    public void onStart() {



        mGoController.setHandler(mHandler);
        super.onStart();
    }


    @Override
    public void onStop() {
        if(mHandler==mGoController.mHandler)
            mGoController.setHandler(null);
        super.onStop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mConn.isConnected()) {
                //To make always connected
                //mConn.shutdown();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    void writeAccountAndPasswd() {
        String pin = mPinText.getText().toString();
        String email = mEmailText.getText().toString();
        app.setEmail(email);
        app.setPin(pin);
        User u = mGoController.getMyUser();
        if(u!=null) {
			app.setUID(u.id);
            u.name=email;
            if(email.indexOf("@")!=-1) {
                u.name=email.substring(0,email.indexOf("@"));
            }
        }

    }

    int connectReason=1;//login;
    String mEmail=null;
    String mPin=null;
    void onLogin() {

        mPin = mPinText.getText().toString();
        mEmail = mEmailText.getText().toString();

        if(mPin.length()==0 || mEmail.length()==0) {
            showFailureDialog(R.string.acc_pin_null);
            return;
        }
        if(mWaitingDialog==null) {
            mWaitingDialog=this.showFailureDialog(R.string.waitingforlogin);
        }
        connectReason=1;

        mHandler.postDelayed(new Runnable() {
            public void run() {
                mGoController.connectServer(mIp, mPort);
            }
        },500);


    }
    void onRegister() {
        mPin = mPinText.getText().toString();
        mEmail = mEmailText.getText().toString();
        if(mPin.length()==0 || mEmail.length()==0) {
            showFailureDialog(R.string.acc_pin_null);
            return;
        }

        if(mWaitingDialog==null) {
            mWaitingDialog=this.showFailureDialog(R.string.waitingforlogin);
        }
        connectReason=2;
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mGoController.connectServer(mIp, mPort);
            }
        },500);

    }


    void getDeskListData() {
        String s = "request:list\r\n"
                   +"type:desk\r\n\r\n";
        mConn.sendData(s);
        mWaitingToDeskList=true;

    }



    AlertDialog showFailureDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(id)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mWaitingDialog=null;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    AlertDialog mWaitingDialog=null;

    class MainHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case GoController.MSG_LOGIN_OK: {
                onLoginOK();
                break;
            }
            case GoController.MSG_LOGIN_RESUME: {
                onLoginResume();
                break;
            }
            case GoController.MSG_LOGIN_FAIL: {
                onLoginFail();
                break;
            }
            case GoController.MSG_REGISTER_OK: {
                onRegisterOK();
                break;
            }
            case GoController.MSG_REGISTER_FAIL: {
                onRegisterFail();
                break;
            }
            case GoController.MSG_CONNECT_FAILED:
	        onConnectFailed();
		break;
            case GoController.MSG_CONNECT_OK:
            case GoController.MSG_SILENT_CONNECT_OK: {
                onConnectOK();
                break;
            }
            case GoController.MSG_RSP_LIST: {
                onResponseList();
                break;
            }
			
            case GoController.RSP_LOAD_HEAD_ICONS: {

				// onRspLoadHeadIcons(msg);
                break;
            }			
            case GoController.RSP_GET_REMOTE_RESOURCE: {

				// onGetRemoteResourceDone(msg);
                break;
            }				
            }
        }
    }
	
	void onGetRemoteResourceDone(android.os.Message msg){
	
		LoaderImageView iv = (LoaderImageView)findViewById(R.id.ivIcon);
		Bundle bundle = (Bundle)(msg.obj);
		if(bundle.getInt("resid")==iv.getResourceId()){
			iv.reloadResource();
		}

	}
	
	int mHeadCount=0;
	int mSelectedIcon=0;
	int mSavedIconId=0;
	boolean mListCreated=false;
	
	// void onRspLoadHeadIcons(android.os.Message msg){
	//         // A ViewGroup MUST be the only child of the HSV
	// 	int mHeadCount = msg.arg1;
	// 	HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.hsvIcon);
	// 	scrollView.setVisibility(View.VISIBLE);
	// 	LinearLayout iiv =(LinearLayout)findViewById(R.id.lvIcon);
	// 	iiv.setVisibility(View.GONE);
		
	// 	if(mListCreated==true) return;
 //        ViewGroup parent = (ViewGroup) scrollView.getChildAt(0);	
	// 	LayoutInflater inflater = LayoutInflater.from(this);  		
	// 	// Add all the children, but add them invisible so that the layouts are calculated, but you can't see the Views
	// 	for (int i = 0; i < mHeadCount; i++) {					
	// 		View item = inflater.inflate(R.layout.icon_item, null);
			
	// 		RemoteResource rr = mGoController.getHeadIconByIndex(i);
	// 		LoaderImageView iv = new LoaderImageView(this,rr.getId());
	// 		LinearLayout lv = (LinearLayout)item.findViewById(R.id.lvIcon);
	// 		//float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
	// 		//iv.setWidth(px);
	// 		//iv.setWidth(py);
			
	// 		lv.addView(iv);
			
	// 		parent.addView(item);
	// 		item.setId(rr.getId());
	// 		item.setOnClickListener(evtLis);
	// 	}			
	// 	mListCreated=true;
	// }
	
	

	// void onIconSelected(int id){
	// 	xHelper.log("icon selected="+id);
	// 	mSelectedIcon=id;
		
	// 	HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.hsvIcon);
	// 	scrollView.setVisibility(View.GONE);
	// 	LinearLayout iiv =(LinearLayout)findViewById(R.id.lvIcon);
	// 	LoaderImageView iv = (LoaderImageView)findViewById(R.id.ivIcon);
	// 	iv.setResourceId(id);
	// 	iiv.setVisibility(View.VISIBLE);		
	// }
	
    void closeWaitingDialog() {
        if(mWaitingDialog!=null) {
            xHelper.log("goapp","closeWaitingDialog");
            mWaitingDialog.dismiss();
            mWaitingDialog=null;
        }

    }
    void onConnectFailed(){
        closeWaitingDialog();
        showFailureDialog(R.string.connectfail);
    }
    void onConnectOK() {
        if(connectReason==1) {
            String data="request:login\r\n";
            data+="sn:"+ xUtil.getDeviceID(this)+"\r\n";
            data+="email:"+mEmail+"\r\n";
            data+="password:"+mPin+"\r\n\r\n";
            if(mConn.isConnected()==true) {
                mConn.sendData(data);
            }
        } else {
            String data="request:register\r\n";
            data+="sn:"+ xUtil.getDeviceID(this)+"\r\n";
            data+="email:"+mEmail+"\r\n";
            data+="password:"+mPin+"\r\n\r\n";
            if(mConn.isConnected()==true) {
                mConn.sendData(data);
            }
        }
    }
    void onLoginResume() {
        xHelper.log("goapp","login is ok");
        writeAccountAndPasswd();
        closeWaitingDialog();
        showDeskList("resume");
    }
    void onLoginOK() {
		
		if(mSelectedIcon!=0){
			mGoController.setHeadIcon(mSelectedIcon);
		}else{		
			mGoController.queryHeadIcon(app.getUID());		
		}
		

        xHelper.log("goapp","login is ok");
        writeAccountAndPasswd();

        closeWaitingDialog();
        showDeskList("ok");
    }
    void onLoginFail() {
        closeWaitingDialog();
        showFailureDialog(R.string.loginfail);
    }
    void onRegisterOK() {
		
		if(mSelectedIcon!=0){
			mGoController.setHeadIcon(mSelectedIcon);
		}else{
			//set default icon
			mGoController.setHeadIcon(1);
		}
		
        writeAccountAndPasswd();
        closeWaitingDialog();
        showDeskList("ok");
    }
    void onRegisterFail() {
        closeWaitingDialog();
        showFailureDialog(R.string.registerfail);
    }
    void onResponseList() {
        if(mWaitingToDeskList==true) {
            mWaitingToDeskList=false;
            closeWaitingDialog();
            showDeskList("ok");

        }
    }
    void showDeskList(String param) {
        //Intent intent = new Intent(this,com.sureone.TheHall.class);
        Intent	intent = new Intent(this,com.sureone.RoomFlowView.class);
        if(param!=null) intent.putExtra("login",param);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
	
	void showProfileSetting() {
        //Intent intent = new Intent(this,com.sureone.TheHall.class);
		/*
        Intent	intent = new Intent(this,com.sureone.ProfileSettingView.class);        
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
		*/
		mGoController.loadHeadIcons();
    }

    class EvtListener implements OnClickListener {
        public void onClick(View v) {
		
			xHelper.log("view clicked="+v.getId());
			
			if(v.getId()<10000){
				// onIconSelected(v.getId());
				return;
			}
			
            switch (v.getId()) {
            case R.id.BtnLogin:
                onLogin();
                break;
            case R.id.BtnRegister:
                onRegister();
                break;
			case R.id.ivIcon:
				showProfileSetting();
				break;				
            }
			
        }

    }
}
