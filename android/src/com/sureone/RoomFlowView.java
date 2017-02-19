package com.sureone;
import android.os.Message;
import android.os.Handler;
import android.view.*;
import android.content.Intent;
import com.sureone.R;
import android.app.Dialog;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import android.widget.LinearLayout;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.PageIndicator;
import android.widget.ImageView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.TitlePageIndicator;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.util.TypedValue;

public class RoomFlowView extends FragmentActivity{

    RoomFragmentAdapter mAdapter;
    RoomViewPager mPager;
    PageIndicator mIndicator;

    GoApp mApp=null;
	GoController mController;
	
	AdTimer mAdTimer=null;
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp =  GoApp.getInstance();
        mController = mApp.getGoController();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = LayoutInflater.from(this);        
        setContentView(R.layout.room_flow_view);
		
        mAdapter = new RoomTitleFragmentAdapter(getSupportFragmentManager());

        mPager = (RoomViewPager)this.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
		
		mAdapter.setViewPager(mPager);

        mIndicator = (TitlePageIndicator)this.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);		
		

		mIndicator.setOnPageChangeListener(mAdapter);
		
		mAdTimer = new AdTimer();
		mAdTimer.setInterval(30000);

        mPager.setCurrentItem(0);
		//new Thread(mAdTimer, "Ad Refresh Timer").start();

    }
		MyHandler mHandler = new MyHandler();
		void showAd(int i){
			//LinearLayout adView = (LinearLayout)findViewById(R.id.adView);
		//	LinearLayout ymView = (LinearLayout)findViewById(R.id.ymView);
			if(i==0){
			//	adView.setVisibility(View.VISIBLE);
		//		ymView.setVisibility(View.GONE);
			}else if(i==1){
			//	adView.setVisibility(View.VISIBLE);
		//		ymView.setVisibility(View.GONE);
			}
		}

    private class AdTimer implements Runnable {
        private boolean mStopped=false;
        private long mInterval;
				private int ad=0;
        public synchronized void stop() {
            mStopped = true;
        }
        public synchronized void setInterval(int v) {
            mInterval = v;
        }
        private boolean mPause=false;
        public synchronized void pause() {
            mPause=true;
        }
        public synchronized void resume() {
            mPause=false;
        }
        public void run() {
            while (!mStopped) {
                if(!mStopped && mPause==false) {
                    Message message = new Message();
                    message.what = MyHandler.MSG_SHOW_AD;
										message.arg1=ad;
                    mHandler.sendMessage(message);
										if(ad==0) ad=1; else ad=0;
                }

                try {
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    continue;
                }

            }
        }
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {


        return mAdapter.mCurFragment.onContextItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        mAdapter.mCurFragment.onCreateContextMenu(menu,v,menuInfo);

    }




    class MyHandler extends Handler {
        public static final int MSG_SHOW_AD = 0x1001;


        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_SHOW_AD: {
                //showAd(msg.arg1);
                break;
            }
				}
			}
		}

    @Override
    public void onPause() {

		super.onPause();
	}

    @Override
    public void onStop() {

		super.onStop();
	}

	@Override
	public void onResume(){
	
		int position = mPager.getCurrentItem();
		xHelper.log("current page @ parent's onResume="+position);
		super.onResume();

	}

	@Override
	protected Dialog onCreateDialog(int id) {

			return null;
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            xHelper.log("goapp","back key pressed");
            //android.os.Process.killProcess(android.os.Process.myPid());
            Intent intent = new Intent(this,com.sureone.OptionsView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            mController.close();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
