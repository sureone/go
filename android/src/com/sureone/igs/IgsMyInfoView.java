package com.sureone.igs;

import java.util.Locale;
import android.content.res.Resources;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;

import android.widget.AdapterView.OnItemSelectedListener;


import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.Window;
import android.view.WindowManager;
import com.sureone.R;
import com.sureone.GoApp;
//import cn.domob.android.ads.DomobAdManager;

public class IgsMyInfoView extends Activity {
    private Button mBtnNext;
    private Button mBtnRestore;

    private MyHandler mHandler=null;


    TextView txtName=null;
    TextView txtInfo=null;
    public IgsController mController=null;
    EvtListener evtLis=null;
    boolean mWaitingToDeskList = false;
    IgsPlayer mPlayer = null;
    GoApp mApp=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mApp =  GoApp.getInstance();
        mController = mApp.getIgsController();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.igsmyinfo);
        mWaitingToDeskList=false;

        mBtnNext=(Button) findViewById(R.id.BtnNext);
        mBtnRestore=(Button) findViewById(R.id.BtnRestore);
        txtName = (TextView)findViewById(R.id.txtName);
        txtInfo = (TextView)findViewById(R.id.txtInfo);

        evtLis = new EvtListener();
        mBtnNext.setOnClickListener(evtLis);
        mBtnRestore.setOnClickListener(evtLis);
        mBtnRestore.setVisibility(View.GONE);

        mHandler = new MyHandler();
        mController.setViewHandler(mHandler);
        mPlayer = mController.getMe();
        //setTitle(getString(R.string.welcomigs));
        TextView tv_title = (TextView)findViewById(R.id.tv_title_text);
        if(tv_title!=null) tv_title.setText(getString(R.string.welcomigs));	
		
// Application of the Array to the Spinner

        showMe();
        mController.checkStored();
    }
    String sRankSet="";
    void changeRank(String s) {
        sRankSet=s;
        mController.rank(s);
    }
    Spinner mSpinRank=null;
    String ranks[] = {
        "17k","16k","15k","14k","13k","12k","11k","10k",
        "9k","8k","7k","6k","5k","4k","3k","2k","1k",
        "1d","2d","3d","4d","5d","6d"
    };


    void showMe() {
        txtName.setText(mPlayer.name);
        if(mPlayer.country!=null)
            txtName.setText("*"+mPlayer.name+"* "+getString(R.string.from)+" "+mPlayer.country);
        String s=
            getString(R.string.wins)+":"+mPlayer.wins+"\n"+
            getString(R.string.loses)+":"+mPlayer.loses+"\n"+
            getString(R.string.idle)+":"+mPlayer.idle;
        txtInfo.setText(s);
        setRankUI();

    }
    void setRankUI() {
		TextView txt = (TextView)findViewById(R.id.txtRank);
        if(mPlayer.rank==null) return;
		txt.setText(getString(R.string.rank)+":"+mPlayer.rank);

    }
    AlertDialog showInfoDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    void onRank() {
        String r = mController.getMyRank();
        String s = getString(R.string.yourankset)+r;
        setRankUI();
        //showInfoDialog(s);
    }
    void onStored() {
        String s = mController.getStoredGame();
        if(s!=null) {
            mController.lookStored();
            mBtnRestore.setVisibility(View.VISIBLE);
            Resources res = getResources();
            String text = String.format(res.getString(R.string.restoreInfo), s);
            showInfoDialog(text);
        }
    }
    void onLookStored() {
        String s = mController.getStoredGame();
        if(s!=null) {
            mController.restoreGame();
        }
    }
    void onRestored() {
        Intent intent = new Intent(this,com.sureone.igs.IgsGoView.class);
        intent.putExtra("viewMode",1);
        startActivity(intent);
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case IgsController.MSG_IGS_RANK:
                onRank();
                break;
            case IgsController.MSG_IGS_STORED:
                onStored();
                break;
            case IgsController.MSG_IGS_RESTORED:
                onRestored();
                break;
            case IgsController.MSG_IGS_LOOK_STORED:
                onLookStored();
                break;
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }
    void onBtnNext() {
        Intent intent = new Intent(this,com.sureone.igs.IgsBrowser.class);
        startActivity(intent);
    }
    class EvtListener implements OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.BtnNext:
                onBtnNext();
                break;
            }
        }

    }
}
