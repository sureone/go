package com.sureone.igs;

import java.util.Locale;

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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.Window;
import android.view.WindowManager;
import com.sureone.R;
import com.sureone.GoApp;
//import cn.domob.android.ads.DomobAdManager;

public class IgsPlayerDetailView extends Activity {
    private Button mBtnInvite;
    private Button mBtnAddFriend;
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
        setContentView(R.layout.igsplayerdetail);
        mWaitingToDeskList=false;

        mBtnInvite=(Button) findViewById(R.id.BtnInvite);
        mBtnAddFriend=(Button) findViewById(R.id.BtnAddFriend);
        txtName = (TextView)findViewById(R.id.txtName);
        txtInfo = (TextView)findViewById(R.id.txtInfo);

        evtLis = new EvtListener();
        mBtnInvite.setOnClickListener(evtLis);
        mBtnAddFriend.setOnClickListener(evtLis);

        int idx = getIntent().getIntExtra("index",0);
        mHandler = new MyHandler();
        mController.setViewHandler(mHandler);
        mPlayer = mController.getPlayerByIndex(idx);
        txtName.setText(mPlayer.name);
        String s= getString(R.string.rank)+":"+mPlayer.rank+"\n";
        txtInfo.setText(s);
        mController.stats(mPlayer);
    }


    void onStats() {
        txtName.setText(mPlayer.name);
        if(mPlayer.country!=null)
            txtName.setText("*"+mPlayer.name+"* "+getString(R.string.from)+" "+mPlayer.country);
        String s= getString(R.string.rank)+":"+mPlayer.rank+"\n"+
                  getString(R.string.wins)+":"+mPlayer.wins+"\n"+
                  getString(R.string.loses)+":"+mPlayer.loses+"\n"+
                  getString(R.string.idle)+":"+mPlayer.idle;
        txtInfo.setText(s);
    }

    void onGameStart() {
        Intent intent = new Intent(this,com.sureone.igs.IgsGoView.class);
        intent.putExtra("viewMode",1);
        startActivity(intent);
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case IgsController.MSG_GAME_START:
                onGameStart();
                break;
            case IgsController.MSG_IGS_STATS:
                onStats();
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
    void onBtnInvite() {
        mController.match(mPlayer.name);
    }
    void onBtnAddFriend() {
    }
    class EvtListener implements OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.BtnInvite:
                onBtnInvite();
                break;
            case R.id.BtnAddFriend:
                onBtnAddFriend();
                break;
            }
        }

    }
}
