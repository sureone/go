package com.sureone;
import com.sureone.R;

import android.view.KeyEvent;
import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ViewFlipper;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.RelativeLayout;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.os.Handler;


public class RoomView extends Activity{
    private ViewFlipper mViewFlipper;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.roomview);
        initViews();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(mCurView!=null) {
            mCurView.onResume();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    void initViews() {
 //       mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        View view;
        LayoutInflater inflater =
            (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*
        view = inflater.inflate(R.layout.main_page, null);
        mViewFlipper.addView(view);
        initHomeView(view);
		*/
    }
    ViewBase mCurView=null;
	/*
    ViewBase mHomeView=null;
    void initHomeView(View view) {
        if(mHomeView==null) {
            mHomeView=new HomeView();
            mHomeView.init(this,view);
        }
    }
	*/
    class EvtListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            }
        }
    }

    void onBtnHome() {
        mViewFlipper.setDisplayedChild(0);
        //mHomeView.showView();
        //mCurView=mHomeView;
    }
}
