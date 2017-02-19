package com.sureone;
// import com.google.ads.*;
import android.content.Intent;

import java.io.FileOutputStream;
import java.io.File;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import com.sureone.go.CLogicGo;
import com.sureone.go.GoOp;
import com.sureone.go.GoStep;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SgfActivity extends Activity implements ChessEventListener{
    public static final int ID_BOARD=1;
    ImageButton mBtnNext=null;
    ImageButton mBtnLast=null;
    ImageButton mBtnPlay=null;
    ImageButton mBtnStop=null;
	
    TextView txtComment= null;

    GoPanelView mBoardView=null;

    RelativeLayout mContentGroup=null;
    int mBoardSize = 19;
    public int mCurTurn = 0;
    public boolean bShowBigView = true;
	GoController mGoController = null;
    public void onCreate(Bundle parent) {
        super.onCreate(parent);
        GoApp app = GoApp.getInstance();
		mGoController = app.getGoController();
		if( mGoController.onBindCallBack(this,getIntent())==true){
			showWeiboShareView(mGoController.getCurrentBind());
			return;
		}
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sgfview);

        final Window win = this.getWindow();

        mContentGroup=(RelativeLayout)this.findViewById(R.id.sgfView);
        this.txtComment = (TextView)this.findViewById(R.id.txtComment);
        txtComment.setTextColor(Color.BLACK);
        String sgf = getIntent().getStringExtra("curSgf");
		//xHelper.log("goapp",sgf);
        this.loadSgf(sgf);
        createBoard();
        mBtnPlay.setImageResource(R.drawable.sgfplay);
		LinearLayout container =(LinearLayout)findViewById(R.id.adArea);
		//new com.waps.AdView(this,container).DisplayAd();
		//new com.waps.MiniAdView(this, container).DisplayAd(60); //默认10秒切换一次广告

    }


    String exportPicture() {
        Bitmap memBmp = mBoardView.forceDraw(this,19,480);
		String fn = this.getFilesDir()+"/sgf_gen.png";
        File newFile=new File(fn);
        try {
            memBmp.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(newFile));
        } catch (Exception e) {
            xHelper.log("Error--------->", e.toString());
			return null;
        }
		return fn;
    }

    private static final int CMD_SINA  = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        item = menu.add(0, CMD_SINA, 0, R.string.share);
        item.setIcon(R.drawable.sina48x48);
        //item.setIcon(R.drawable.clear_history);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }
    void showSinaBindView() {
        //accessInfo = InfoHelper.getAccessInfo(mContext);
		if(mGoController.isBindWeibo(this,"sina")==false)
			mGoController.bindSinaWeibo(this,"weibo4android://SgfActivity");
		else
			showWeiboShareView("sina");
			
	}
	void showWeiboShareView(String what){
		String fn = "sgf_gen.png";
        Intent intent=null;
        intent = new Intent(this,com.sureone.ShareToWeiboView.class);
		intent.putExtra("weibo",what);
		intent.putExtra("image",fn);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case CMD_SINA:
            if(mBoardView!=null) {
                if(mTimer!=null)
                    mTimer.stop();
                exportPicture();
				showSinaBindView();
                //Intent intent = new Intent(this,com.sureone.SgfWeibo.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //startActivity(intent);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    class LastStep {
        LastStep() {
            x=-1;
            y=-1;
        }
        int x;
        int y;
    };
    DisplayMetrics mDM=null;
    LastStep mLastStep = null;
    RelativeLayout mBoardLayout=null;

    int mPlayMode = 0; //0 = stop , 1 = play
    RelativeLayout mPanel=null;
    void createBoard() {
        //mBoardView = new GoPanelView(this);

        mDM=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDM);
        GoApp app =   GoApp.getInstance();
/*
        AdView	adView = (AdView)(this.findViewById(R.id.adView));
            if(app.getAdMode()==false) {
				if(mDM.heightPixels<854)
            		((RelativeLayout)this.findViewById(R.id.adArea)).setVisibility(View.GONE);
				else{
					adView.setVisibility(View.GONE);
            		//adView.loadAd(new AdRequest());
				}
            } else {
					adView.setVisibility(View.GONE);
                //adView.loadAd(new AdRequest());
            }
*/
        int w =mDM.widthPixels;
        if(w>mDM.heightPixels) w=mDM.heightPixels;
        InputEventListener listener = new InputEventListener();
        mBoardView = (GoPanelView)this.findViewById(R.id.boardView);
        mBoardView.setWidth(w);     
        //xHelper.log("goapp","listen touch event");
        mBoardView.setFocusable(true);
        mBoardView.setOnTouchListener(listener);
				mBoardView.setChessListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mBtnNext = (ImageButton)this.findViewById(R.id.btnNext);
        mBtnLast = (ImageButton)this.findViewById(R.id.btnLast);
        mBtnPlay = (ImageButton)this.findViewById(R.id.btnPlay);

        btnEvtLis = new ButtonEventListener();
        mBtnNext.setOnClickListener(btnEvtLis);
        mBtnLast.setOnClickListener(btnEvtLis);
        mBtnPlay.setOnClickListener(btnEvtLis);
        /*
        BitmapDrawable TileMe =
        	new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.tile));


        TileMe.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
        TileMe.setTileModeY(android.graphics.Shader.TileMode.REPEAT);

        ImageView Item =(ImageView) this.findViewById(R.id.tileBg);
        Item.setBackgroundDrawable(TileMe);
        */


    }

    void backToAnchor() {
        mGoLogic.backToAnchor();
    }

		public void onPutChess(int x,int y){

		}

		public int getStepNo(int x,int y){
			return mGoLogic.getStepNo(x,y);
		}

		public boolean haveLogic(){
			return !(mGoLogic==null);
		}
	
	
		public int getSide(int x,int y){
				return mGoLogic.getSide(x,y);
		}

	public int getLastStepX(){

  GoStep lastStep = mGoLogic.GetLastStep();
  if(lastStep!=null) {
     GoOp o = lastStep.getSetOp();
			return o.x;
	}
	return -1;
}
	public int getLastStepY(){
  GoStep lastStep = mGoLogic.GetLastStep();
  if(lastStep!=null) {
     GoOp o = lastStep.getSetOp();
			return o.y;
	}
	return -1;

}
    boolean onBtnNext() {
        if(mTimer!=null) mTimer.pause();
        backToAnchor();

        boolean b = mGoLogic.next();
        mBoardView.invalidateEx(true);
        mPlayMode=0;
        mBtnPlay.setImageResource(R.drawable.sgfplay);
        return b;
    }
    void onBtnLast() {
        if(mTimer!=null) mTimer.pause();
        mPlayMode=0;
        mBtnPlay.setImageResource(R.drawable.sgfplay);
        backToAnchor();
        mGoLogic.last();
        mBoardView.invalidateEx(true);

    }
    Timer mTimer = null;

    void onBtnPlay() {
        if(mPlayMode==0) {
            backToAnchor();
            if(mTimer==null) {
                mTimer = new Timer(mInterval);
                new Thread(mTimer, "Sgf Play Timer").start();
            }
            mTimer.play();
            mPlayMode=1;
            mBtnPlay.setImageResource(R.drawable.sgfstop);
        } else {
            mTimer.play();
            mPlayMode=0;
            mBtnPlay.setImageResource(R.drawable.sgfplay);
        }
    }
    private MyHandler mHandler = new MyHandler();
    class MyHandler extends Handler {
        private static final int MSG_AUTO_PLAY = 0;
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_AUTO_PLAY: {
                boolean b = mGoLogic.next();

                SgfTag t = mGoLogic.mCurTag;
                if(t.t==SgfTag.TAG_C) {
                    txtComment.setText(t.v);
                }
                mBoardView.invalidateEx(true);
                if(b==false && mTimer!=null)
                    mTimer.stop();
                break;
            }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mTimer!=null)
                mTimer.stop();
            //mViewSwitch.backToOptionsView();
            //return true;
            //this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public int mInterval=1000;
    protected class Timer implements Runnable {
        public boolean mStopped;

        public boolean mPlay=false;
        public Timer(long interval) {
            mStopped = false;
        }

        public void stop() {
            mStopped = true;
        }

        public void restart() {
            mStopped = false;
        }

        public void pause() {
            mPlay=false;
        }
        public void play() {
            mPlay=!mPlay;
        }


        public void run() {
            while (!mStopped) {
                if(mPlay) {
                    Message message = new Message();
                    message.what = MyHandler.MSG_AUTO_PLAY;
                    mHandler.sendMessage(message);
                }

                try {
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    continue;
                }

            }
        }
    }

    ButtonEventListener btnEvtLis=null;
    class ButtonEventListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btnNext:
                onBtnNext();
                break;
            case R.id.btnLast:
                onBtnLast();
                break;
            case R.id.btnPlay:
                onBtnPlay();
                break;
            }
        }

    }

    String mResult="";
    CLogicGo mGoLogic = null;
	String mTitle="";
    void loadSgf(String str) {

        mGoLogic = new CLogicGo();
        mGoLogic.loadSgf(str);
        SgfParser parser = mGoLogic.mParser;
        SgfHeader h = parser.mH;
        String s="";
		
		com.viewpagerindicator.GoGameTitle mGameTitle = (com.viewpagerindicator.GoGameTitle)findViewById(R.id.gtTitle);
		
		mGameTitle.setBlackName(h.PB);
		mGameTitle.setBlackInfo(h.BR);
		mGameTitle.setWhiteName(h.PW);
		mGameTitle.setWhiteInfo(h.WR);
		
        //txtComment.setText(h.DT+"\n"+h.EV);
		
        
        String[] ss = new String[2];
        ss[0]=h.RE.substring(0, 1);
        ss[1]=h.RE.substring(2);
        if(ss.length==2) {
            if(ss[1].compareTo("R")==0) {
                if(ss[0].compareTo("B")==0)
                    s+=this.getString(R.string.REBR);
                if(ss[0].compareTo("W")==0)
                    s+=this.getString(R.string.REWR);
            } else if(ss[0].compareTo("B")==0 || ss[0].compareTo("W")==0) {
                if(ss[0].compareTo("B")==0)
                    s+=this.getString(R.string.black)+
                       ss[1]+this.getString(R.string.mu)+this.getString(R.string.win);
                if(ss[0].compareTo("W")==0)
                    s+=this.getString(R.string.white)+
                       ss[1]+this.getString(R.string.mu)+this.getString(R.string.win);
            } else {
                s+=h.RE;
            }
        }
        mResult=h.EV;        
		mGameTitle.setCenter1(s);
		mGameTitle.setCenter2(mResult);
        mGoLogic.begin();
    }
    float mScreenX;
    float mScreenY;

    float mLastMoveX;
    float mLastMoveY;
    boolean mMoveStart=false;
    boolean mMoving=false;
    boolean bMotionDown=false;
    boolean bMotionMove=false;
    boolean bMotionUp=false;
    float mLastFingerX;
    float mLastFingerY;

    boolean mStopByFingerMove=false;
    void onFingerMove(float x,float y) {
        if(mPlayMode==0 && mStopByFingerMove==false) return;
        float h =mDM.heightPixels;
        float offset = h/100;
        float dy=y-mLastFingerY;
        if(dy>=offset || dy<=-offset) {
            if(mStopByFingerMove==false)
                onBtnPlay();
            mStopByFingerMove=true;

            mLastFingerY=y;
            mLastFingerX=x;
            if(dy>0) mInterval+=200;
            else
                mInterval-=200;
            if(mInterval<=0) mInterval=200;
            float f = (float)mInterval/(float)1000;
			com.viewpagerindicator.GoGameTitle mGameTitle = (com.viewpagerindicator.GoGameTitle)findViewById(R.id.gtTitle);
			
            mGameTitle.setCenter2(this.getString(R.string.speed)+f);
        }

    }
    void onFingerUp() {
        if(mStopByFingerMove==true) {
            onBtnPlay();
            mStopByFingerMove=false;
			com.viewpagerindicator.GoGameTitle mGameTitle = (com.viewpagerindicator.GoGameTitle)findViewById(R.id.gtTitle);
			
            mGameTitle.setCenter2(mResult);
        }
    }
    class InputEventListener implements android.view.View.OnTouchListener,
        android.view.View.OnClickListener,android.view.View.OnLongClickListener {
        InputEventListener() {
            //xHelper.log("goapp","InputEventListener created");
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            //xHelper.log("goapp","InputEventListener created");
            boolean eventstatus = true;
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = event.getX();
                float y = event.getY();
                mLastFingerX=mLastMoveX;
                mLastFingerY=mLastMoveY;
                bMotionDown=true;
                bMotionMove=false;
                bMotionUp=false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //xHelper.log("GoActivity","Touch finger Moving");
                bMotionMove=true;
                if(bMotionDown==true) {
                    onFingerMove(event.getX(),event.getY());
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                bMotionUp=true;
                onFingerUp();
                bMotionMove=false;
                bMotionDown=false;
                //xHelper.log("GoActivity","Touch finger up");

                break;
            }
            default: {

            }
            }
            return eventstatus;
        }
        @Override
        public boolean onLongClick(View v) {
            // TODO Auto-generated method stub


            return false;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //the click and touch up both happened during scroll view

        }


    }


}
