package com.sureone.igs;

import java.util.ArrayList;
import android.view.Window;
import android.view.WindowManager;
import com.sureone.*;
import android.net.Uri;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class IgsLoginView extends Activity {
    private Button mBtnLogin;
    private Button mBtnReg;
    private EditText mPinText;
    private EditText mEmailText;
    IgsController mController = null;
    EvtListener evtLis=null;
    public MyHandler mHandler=null;
    /** Called when the activity is first created. */
    boolean mWaitingToGameList = false;
    GoApp app = null;
    @Override
    public void onCreate(Bundle parent) {
        super.onCreate(parent);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.igsloginview);
        app = GoApp.getInstance();
        mWaitingToGameList=false;
        mController = app.getIgsController();
        evtLis = new EvtListener();
        mBtnLogin = (Button)findViewById(R.id.BtnLogin);
        mBtnLogin.setOnClickListener(evtLis);
        mBtnReg = (Button)findViewById(R.id.BtnRegister);
        mBtnReg.setOnClickListener(evtLis);
        mPinText = (EditText) findViewById(R.id.txtPassword);
        mEmailText = (EditText) findViewById(R.id.txtEmail);
        mEmailText.setTextColor(android.graphics.Color.BLACK);
        mPinText.setTextColor(android.graphics.Color.BLACK);
        mHandler=new MyHandler();
        mController.setViewHandler(mHandler);
        //mEmailText.setText(app.getIgsName());
        //mPinText.setText(app.getIgsPin());
	
        com.viewpagerindicator.NormalTitle nt = (com.viewpagerindicator.NormalTitle) findViewById(R.id.ntTitle);        
        if(nt!=null) nt.setTitle(getString(R.string.loginview));		
        
    }

    @Override
    public void onStart() {
        mController.setViewHandler(mHandler);
        super.onStart();
    }
    @Override
    public void onResume() {
        mController.setViewHandler(mHandler);
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mController.close();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void writeAccountAndPasswd() {
        String pin = mPinText.getText().toString();
        String name = mEmailText.getText().toString();
        app.setIgsName(name);
        app.setIgsPin(pin);
    }

    int connectReason=1;//login;
    String mIgsName=null;
    String mIgsPin=null;
    void onLogin() {
        mIgsPin = mPinText.getText().toString();
        mIgsName = mEmailText.getText().toString();
        if(mIgsPin.length()==0 || mIgsName.length()==0) {
            showFailureDialog(R.string.acc_pin_null);
            return;
        }
        if(mWaitingDialog==null) {
            mWaitingDialog=this.showFailureDialog(R.string.waitingforlogin);
        }
        mController.close();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mController.connectIgs(app.getIgsServer(),app.getIgsPort(),mIgsName,mIgsPin);
            }
        },500);
    }
    void onRegister() {
        String url = getString(R.string.igsurl);
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        this.startActivity(intent);
    }


    void loadGameList() {
        mController.listGames();
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

    void LogLog(String s) {
        xHelper.log("igs","IgsLoginView: "+s);
    }
    AlertDialog mWaitingDialog=null;
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {

            LogLog("recevied msg="+msg.what);
            switch (msg.what) {
            case IgsController.MSG_IGS_STATS: {
                onStatsOK();
                break;
            }
            case IgsController.MSG_LOGIN_OK:
            case IgsController.MSG_SILENT_LOGIN_OK: {
                onLoginOK();
                break;
            }
            case IgsController.MSG_LOGIN_FAIL: {
                onLoginFail();
                break;
            }
            }
        }
    }
    void closeWaitingDialog() {
        if(mWaitingDialog!=null) {
            mWaitingDialog.dismiss();
            mWaitingDialog=null;
        }

    }
    void onConnectOK() {
    }
    void onStatsOK() {
        Intent intent = new Intent(this,com.sureone.igs.IgsMyInfoView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    void onLoginOK() {
        mController.setMyName(mIgsName);
        writeAccountAndPasswd();
        closeWaitingDialog();
        mController.statsMe();
    }
    void onLoginFail() {
        closeWaitingDialog();
        showFailureDialog(R.string.loginfail);
    }
    class EvtListener implements OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.BtnLogin:
                onLogin();
                break;
            case R.id.BtnRegister:
                onRegister();
                break;
            }
        }

    }
}
