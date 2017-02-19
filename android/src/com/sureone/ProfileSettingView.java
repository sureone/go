package com.sureone;

import java.util.ArrayList;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.view.LayoutInflater;
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
import java.util.List;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;
import android.widget.TableRow;
import android.widget.TableLayout;

public class ProfileSettingView extends Activity {

    EvtListener evtLis=null;
    Intent mDeskListActivity = null;
    public MainHandler mHandler=null;

    GoController mGoController=null;
    GoApp app = null;
	HorizontalScrollView scrollView=null;
	LayoutInflater inflater=null;
    @Override
    public void onCreate(Bundle parent) {
        super.onCreate(parent);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        app =  GoApp.getInstance();
        mGoController = app.getGoController();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profilesettingview);
		
		inflater = LayoutInflater.from(this);        


        evtLis = new EvtListener();
		mHandler = new MainHandler();
        mGoController.setHandler(mHandler);
		
		mGoController.loadHeadIcons();

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
        return super.onKeyDown(keyCode, event);
    }
	
	
	void onRspLoadHeadIcons(android.os.Message msg){
	        // A ViewGroup MUST be the only child of the HSV
		int cnt = msg.arg1;
		scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        ViewGroup parent = (ViewGroup) scrollView.getChildAt(0);						
		// Add all the children, but add them invisible so that the layouts are calculated, but you can't see the Views
		for (int i = 0; i < cnt; i++) {
					
			View item = inflater.inflate(R.layout.icon_item, null);
			LoaderImageView iv = new LoaderImageView(this,mGoController.getHeadIconByIndex(i).getId());
			LinearLayout lv = (LinearLayout)item.findViewById(R.id.lvIcon);
			lv.addView(iv);			
			parent.addView(item);
		}				
	}

	void onRspLoadHeadIconsDummy(android.os.Message msg){
	        // A ViewGroup MUST be the only child of the HSV
		int cnt = msg.arg1;
			
		/*	
		scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);

        ViewGroup parent = (ViewGroup) scrollView.getChildAt(0);
		
		
			
		// Add all the children, but add them invisible so that the layouts are calculated, but you can't see the Views
		for (int i = 0; i < cnt; i++) {
			View item = inflater.inflate(R.layout.icon_item, null);
			ImageView iv = (ImageView)item.findViewById(R.id.ivIcon);
			iv.setImageResource(R.drawable.zhugeliang);
			parent.addView(item);
		}			
		*/
		
/* Find Tablelayout defined in main.xml */
	 /*
	  TableLayout tl = (TableLayout)findViewById(R.id.myTableLayout);
		   
		   tl.setShrinkAllColumns(true);
		int i=0;
		while(i<10) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
			for(int j=0;j<4 && i<cnt;j++){
				View item = inflater.inflate(R.layout.icon_item, null);
				ImageView iv = (ImageView)item.findViewById(R.id.ivIcon);
				iv.setAdjustViewBounds(true);
				iv.setImageResource(R.drawable.zhugeliang);
				tr.addView(item);
				i++;
			}

	  tl.addView(tr,new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));	
		}	
*/		



	}


    class MainHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case GoController.RSP_LOAD_HEAD_ICONS: {

				onRspLoadHeadIcons(msg);
                break;
            }
            }
        }
    }


    class EvtListener implements OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
			}
		}

    }
}
