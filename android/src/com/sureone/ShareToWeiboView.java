package com.sureone;
import 	android.graphics.BitmapFactory;
import 	java.io.File;
import android.widget.Toast;
import java.util.ArrayList;
import android.view.Window;
import android.view.WindowManager;

import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.graphics.drawable.ColorDrawable;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ShareToWeiboView extends Activity {
    EvtListener evtLis=null;
    public MainHandler mHandler=null;
	GoController mGoController = null;
	ImageView ivBlog = null;
	String mWhich=null;
	String mPic=null;
    @Override
    public void onCreate(Bundle parent) {
        super.onCreate(parent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_form);
        GoApp app =  GoApp.getInstance();
		mGoController = app.getGoController();
		
        TextView tv_title = (TextView)findViewById(R.id.tv_title_text);
        
		
        evtLis = new EvtListener();
        mHandler=new MainHandler();
		Button btn = (Button)findViewById(R.id.btn_post);
		ImageView ivBlog = (ImageView)findViewById(R.id.ivBlog);
		btn.setOnClickListener(evtLis);
		btn = (Button)findViewById(R.id.btn_back);
		btn.setOnClickListener(evtLis);
		EditText et = (EditText)findViewById(R.id.etBlog);
		et.setText(mGoController.getNextWeiboMsg());
	
		if( mGoController.onBindCallBack(this,getIntent())==true){			
			mWhich=mGoController.getCurrentBind();
		}else
			mWhich = getIntent().getStringExtra("weibo");
			
		if (mWhich.equals("sina")){
			if(tv_title!=null) tv_title.setText(getString(R.string.shareToSina));
		}
		if (mWhich.equals("qq")){
			if(tv_title!=null) tv_title.setText(getString(R.string.shareToTencet));
		}
		mPic = "sgf_gen.png";
		if(mPic!=null){
                try {
						xHelper.log("goapp","show"+mPic);
                        java.io.FileInputStream in = openFileInput(mPic);
                        ivBlog.setImageBitmap(BitmapFactory.decodeStream(in));
                } catch(Exception e) {
                    e.printStackTrace();
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
    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    AlertDialog showInfoDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(id)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }
	
	static final int DIALOG_LOADING=4;
    Dialog mDialog=null;
    int mCurDialog;
    protected Dialog onCreateDialog(int id) {
        mCurDialog=id;
        Dialog dialog;
        switch(id) {
        case DIALOG_LOADING:
            dialog = showLoadingDialog();
            break;
        default:
            dialog = null;
        }
        mDialog=dialog;        
        return dialog;
    }
	
	Dialog showLoadingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.q_waiting);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        FrameLayout fl = (FrameLayout)dialog.findViewById(R.id.fl_pb_loading);
        fl.setVisibility(View.VISIBLE);
        return dialog;
    }
	

	public final static int MSG_SHARE_WEIBO_DONE=0x7001;
    class MainHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
			case MSG_SHARE_WEIBO_DONE:
				onSendWeiboDone();
				break;
            }
        }
    }
    class EvtListener implements OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_post:
				onSend();
                break;
            case R.id.btn_back:
							onBtnBack();
                break;
            }
        }

    }
void onBtnBack(){
	this.finish();
}
	void showProgress(){
        ProgressBar pb_loading = (ProgressBar)this.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);
	}
	void hideProgress(){
        ProgressBar pb_loading = (ProgressBar)this.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.GONE);
	}

	void onSend(){

		new Thread(new Runnable() {
			public void run() {
				EditText et = (EditText)(ShareToWeiboView.this.findViewById(R.id.etBlog));
				String fn=null;
				String content = et.getText().toString()+"\n";
				
				content+="@"+ShareToWeiboView.this.getString(R.string.atweibo)+GoApp.getInstance().getDownload();
				if(mPic!=null){
					fn = ShareToWeiboView.this.getFilesDir()+"/sgf_gen.png";
					if (mWhich.equals("sina")){
						xHelper.log("goapp","send to sina");
						mGoController.sendToSina(ShareToWeiboView.this,content,fn);
					}
					if (mWhich.equals("qq")){
						xHelper.log("goapp","send to qq");
						mGoController.sendToQQ(ShareToWeiboView.this,content,fn);
					}
				}else{
					if (mWhich.equals("sina")){
						mGoController.sendToSina(ShareToWeiboView.this,content);
					}
					if (mWhich.equals("qq")){
						mGoController.sendToQQ(ShareToWeiboView.this,content);
					}
				}
				mHandler.sendMessage(mHandler.obtainMessage(MSG_SHARE_WEIBO_DONE,
                                           0, 0, null));
			}
        }).start();
		showDialog(DIALOG_LOADING);		

	}
	void onSendWeiboDone(){
		removeDialog(DIALOG_LOADING);
		this.finish();
	}
}
