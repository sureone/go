package com.sureone;
import java.io.FileOutputStream;
import java.io.File;
import android.widget.Toast;
// import com.google.ads.*;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.text.method.ScrollingMovementMethod;
import android.media.AudioManager;
import android.view.Window;
import android.view.WindowManager;
import java.io.UnsupportedEncodingException;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.Bitmap;
import java.lang.Math;
import android.widget.FrameLayout;


public class GoActivity extends Activity implements ChessEventListener {
    public static final int ID_BOARD=1;

    EditText mTxtPost = null;
    Intent mDeskListActivity = null;
    public xTcpThread mConn;
    public TextView mBlackTxt=null;
    public TextView mWhiteTxt=null;
    public TextView mCenterTxt=null;
    public TextView mInfoTxt=null;
    public TextView tv_whiteInfo=null;
    public TextView tv_blackInfo=null;
    private GoHandler mHandler = new GoHandler();
    Activity mContainer=null;

    GoPanelView mBoardView=null;
    DisplayMetrics mDM=null;
    int mBoardSize = 19;

    public boolean bShowBigView = true;
    public int mStepTimeOut = 60*3;

    Button mBtnReady=null;
    Button mBtnPost=null;
    Button mBtnPass=null;
    Button mBtnAlarm=null;
    Button mBtnBlackWin=null;
    Button mBtnWhiteWin=null;
    Button mBtnGiveUp=null;
    Button mBtnGiveUp2=null;
    Button mBtnFullBoard=null;
    Button mBtnUndoStep=null;
    Button mBtnUndoDead=null;
    Button mBtnDone=null;
    Button mBtnScore=null;
    Button mBtnContinueGo=null;
    ButtonEventListener btnEvtListener = new ButtonEventListener();
    LinearLayout mScreen;
    LinearLayout mTalkArea=null;
    /** Called when the Activity is first created. */
	
	com.viewpagerindicator.GoGameTitle mGameTitle = null;

    class LastStep {
        LastStep() {
            x=-1;
            y=-1;
        }
        int x;
        int y;
    };

    String mMsg1="";
    String mMsg2="";
    DianMuResult mDianMuResult = null;
    LastStep mLastStep = null;
    String viewMode="normal";
    GoApp app = null;
    RelativeLayout mContentGroup =null;
    TableLayout mGroupGameButtons =null;
    TableLayout mGroupAdminButtons =null;
    TableLayout mGroupDianMuButtons =null;
    TableLayout mFullBoardButtons =null;
    // AdView adView=null;	
    GoController mGoController=null;
	boolean bCallback=false;
	boolean bMute=false;
    @Override
    public void onCreate(Bundle parent) {
        super.onCreate(parent);
        app =  GoApp.getInstance();
        mGoController = app.getGoController();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.goview);
		
		
        mContainer=this;
        mLastStep = new LastStep();
        mDianMuResult = new DianMuResult();
        mTimer = new Timer(1000);
        new Thread(mTimer, "Desk List Refresh Timer").start();
        mContentGroup = (RelativeLayout)this.findViewById(R.id.mainlayout);
        // Lookup your LinearLayout assuming it��s been given
        // the attribute android:id="@+id/mainLayout"

        // Add the adView to it
        //mContentGroup.addView(adView);

        // Initiate a generic request to load it with an ad
		
		mGameTitle = (com.viewpagerindicator.GoGameTitle)this.findViewById(R.id.gtTitle);
		 
        mGroupGameButtons = (TableLayout)this.findViewById(R.id.onGameButtons);
        mGroupGameButtons.setVisibility(View.GONE);
        mGroupAdminButtons = (TableLayout)this.findViewById(R.id.adminButtons);
        mGroupAdminButtons.setVisibility(View.GONE);
        mGroupDianMuButtons = (TableLayout)this.findViewById(R.id.dianmuButtons);
        mGroupDianMuButtons.setVisibility(View.GONE);
        mFullBoardButtons = (TableLayout)this.findViewById(R.id.fullboardbuttons);
        mFullBoardButtons.setVisibility(View.GONE);
        //mScreen.setBackgroundColor(BOARD_COLOR);
        mReadyBtnFlag=0;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mConn = mGoController.getConnection();
        //let myService to handle the tcp data
        mGoController.setHandler(mHandler);
        mTalkArea = (LinearLayout) this.findViewById(R.id.talkArea);
        mTalkArea.setVisibility(View.GONE);
        mShowView=null;
        mTxtPost = (EditText)this.findViewById(R.id.txtTalk);

        mInfoTxt = (TextView)this.findViewById(R.id.txtInfoGo);

        mBtnPost = (Button)this.findViewById(R.id.btnSendTalk);
        mBtnPost.setOnClickListener(btnEvtListener);
        mBtnContinueGo = (Button)this.findViewById(R.id.btnContinueGo);
        mBtnContinueGo.setOnClickListener(btnEvtListener);
        mBtnReady = (Button)this.findViewById(R.id.btnStart);
        mBtnReady.setOnClickListener(btnEvtListener);
        mBtnReady.setText(this.getString(R.string.start));

        mBtnBlackWin = (Button)this.findViewById(R.id.btnBlackWin);
        mBtnBlackWin.setOnClickListener(btnEvtListener);
        mBtnWhiteWin = (Button)this.findViewById(R.id.btnWhiteWin);
        mBtnWhiteWin.setOnClickListener(btnEvtListener);

        mBtnPass = (Button)this.findViewById(R.id.btnPass);
        mBtnPass.setOnClickListener(btnEvtListener);
        mBtnAlarm = (Button)this.findViewById(R.id.btnAlarm);
        mBtnAlarm.setOnClickListener(btnEvtListener);
		
		Button btnMute = (Button)this.findViewById(R.id.btnMute);
        btnMute.setOnClickListener(btnEvtListener);

        mBtnGiveUp = (Button)this.findViewById(R.id.btnGiveUp);
        mBtnGiveUp.setOnClickListener(btnEvtListener);
        mBtnGiveUp2 = (Button)this.findViewById(R.id.btnGiveUp2);
        mBtnGiveUp2.setOnClickListener(btnEvtListener);
        /* todo undo step
                mBtnUndoStep = (Button)this.findViewById(R.id.btnUndoStep);
                mBtnUndoStep.setOnClickListener(btnEvtListener);
        */

        mBtnUndoDead = (Button)this.findViewById(R.id.btnUndoDead);
        mBtnUndoDead.setOnClickListener(btnEvtListener);
        mBtnScore = (Button)this.findViewById(R.id.btnScore);
        mBtnScore.setOnClickListener(btnEvtListener);
        mBtnFullBoard = (Button)this.findViewById(R.id.btnFullBoard);
        mBtnFullBoard.setOnClickListener(btnEvtListener);
        mBtnDone = (Button)this.findViewById(R.id.btnDone);
        mBtnDone.setOnClickListener(btnEvtListener);

        mGameTitle.setCenter1("");
        mInfoTxt.setText("");
        viewMode = getIntent().getStringExtra("view");
        if(viewMode!=null && viewMode.compareTo("resume")==0) {
            mLastStep.x=mGoController.getLastStepX();
            mLastStep.y=mGoController.getLastStepY();
            resumeGame();

        }
        if(viewMode.compareTo("observe")==0) {
            mLastStep.x=mGoController.getLastStepX();
            mLastStep.y=mGoController.getLastStepY();
        }
        mTxtPost.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    onSendTalk();
                    return true;
                }
                return false;
            }
        });

        bCursorMode=app.getTouchMode();


        createBoard();

        if(viewMode!=null && viewMode.compareTo("resume")==0) {
            mBoardView.mDianMuMode=false;

            if(mGoController.isDianMu()){
                mBoardView.mDianMuMode=true;
                mHideView = mShowView;
                mShowView = mGroupDianMuButtons;
                switchButtonsAnimate();
            }
            viewMode="normal";
        }
        if(viewMode!=null && viewMode.compareTo("start")==0) {
            onGameStart();
        }
        showTalkArea();
        mFullBoardButtons.setVisibility(View.GONE);
        xHelper.log("goapp", "go onCreate");
        updateUser();
        /*
        	RelativeLayout touchlayout = (RelativeLayout)findViewById(R.id.touchlayout);
        TouchInputEventListener evtl = new TouchInputEventListener();
        touchlayout.setOnTouchListener(evtl);
        */
    }

    RelativeLayout mBoardLayout=null;
    void createBoard() {

        mDM=new DisplayMetrics();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindowManager().getDefaultDisplay().getMetrics(mDM);
        LogLog("w,h="+mDM.heightPixels+","+mDM.widthPixels);


        int w =mDM.widthPixels;
        if(w>mDM.heightPixels) w=mDM.heightPixels;
        mBoardView = (GoPanelView)this.findViewById(R.id.boardView);
        mBoardView.setWidth(w);
        mBoardView.setChessListener(this);
        InputEventListener listener = new InputEventListener();
        mBoardView.setOnLongClickListener(listener);
        mBoardView.setOnClickListener(listener);
        mBoardView.setOnKeyListener(listener);
        mBoardView.setOnTouchListener(listener);
        mBoardView.mDianMuResult=mDianMuResult;
		mBoardView.setCursorMode(bCursorMode);

		
        if(viewMode.compareTo("observe")==0) {
			//showAd();
			
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    //showAd();
                }
            },60000);
			
        }
    }

    String mPlayerStr="";

    boolean bShowAd=false;
    void showAd() {
        //RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.titleBar);
		
		//LinearLayout container =(LinearLayout)findViewById(R.id.adArea);
		//new com.waps.AdView(this,container).DisplayAd();
		//new com.waps.MiniAdView(this, container).DisplayAd(60); //Ĭ��10���л�һ�ι��
		//com.waps.AppConnect.getInstance(this).setAdForeColor(Color.BLACK); 
        //adView = (AdView)(this.findViewById(R.id.adView));
		//adView.setVisibility(View.VISIBLE);
        //adView.loadAd(new AdRequest());
        
        //bShowAd=true;
        //titleBar.setVisibility(View.GONE);
        //showInfo(null);
		
		

    }

    void freeMem() {

        if(mBoardView!=null) mBoardView.freeMem();
    }
    @Override
    public void onPause() {
        xHelper.log("GoActivity", "go onPause");
        freeMem();
        super.onPause();
    }
    public void leaveActivity() {
        if(mGoController.mHandler==mHandler)
            mGoController.setHandler(null);
        //back to DeskList
        Intent intent = new Intent(this,com.sureone.RoomFlowView.class);
        intent.putExtra("login","ok");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        mContainer.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //user will leave the last desk only which is full
            if(mGoController.isInGame()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirmleave)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mGoController.iLeaveDesk();
                        mGoController.requestLeave();
                        leaveActivity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            } else if(mGoController.isInDesk()) {
                mGoController.iLeaveDesk();
                mGoController.requestLeave();
            }
            leaveActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        mGoController.setHandler(mHandler);
		if(mGoController.isInGame()==true){
			mGoController.resume();
		}
		
        updateUser();
        xHelper.log("goapp", "go onResume");
        super.onResume();
    }
    @Override
    public void onStop() {
        xHelper.log("GoActivity", "go onStop");
        freeMem();
        mTimer.stop();
        if(mGoController.mHandler==mHandler)
            mGoController.setHandler(null);
        super.onStop();
    }
    @Override
    public void onStart() {
        xHelper.log("goapp","go onStart");
        super.onStart();
    }
    String mTalkHis="";
    void showInfo(String str) {
        mInfoTxt.setMovementMethod(new ScrollingMovementMethod());
        String s=null;
        if(str!=null)	mTalkHis=str+"\n"+mTalkHis;
        if(bShowAd==true && mPlayerStr!=null) {
            s=mPlayerStr+"\n"+mTalkHis;
        } else
            s=mTalkHis;
        mInfoTxt.setText(s);
    }
    void showDebugInfo(String str) {
        boolean isDebug = getResources().getBoolean(R.bool.debug);
        if(isDebug==true) {
            mInfoTxt.setMovementMethod(new ScrollingMovementMethod());
            String s = mInfoTxt.getText().toString();
            s=str+"\n"+s;
            mInfoTxt.setText(s);
        }
    }

    AlertDialog showWaitingDialog(int id) {
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
    AlertDialog showInfoDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str)
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

    static final int DIALOG_RESULT=0;
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch(id) {
        case DIALOG_RESULT:
            // do the work to define the pause Dialog
            dialog=showResultDialog();
            break;
        default:
            dialog = null;
        }
        //if(dialog!=null) dialog.setCancelable(false);
        return dialog;
    }
    String mGameResult=null;
    Dialog showResultDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.q_dialog);
        //dialog.setTitle("Custom Dialog");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.tv_msg_content);
        text.setText(mGameResult);

        Button btn = (Button) dialog.findViewById(R.id.btn_dialog_left);
        btn.setVisibility(View.GONE);
        btn = (Button) dialog.findViewById(R.id.btn_dialog_right);
		btn.setText(this.getString(R.string.close));
        btn.setVisibility(View.VISIBLE);
		btn.setOnClickListener(btnEvtListener);
		//If I am not a observer, show the menu for share to weibo
		if(viewMode.compareTo("observe")!=0){

			// btn = (Button) dialog.findViewById(R.id.btn_sina);
			// btn.setVisibility(View.GONE);
			// //btn.setOnClickListener(btnEvtListener);
			// btn = (Button) dialog.findViewById(R.id.btn_qq);
			// btn.setVisibility(View.VISIBLE);
			// btn.setOnClickListener(btnEvtListener);
		}
        //btnLeft.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_buy));
        
        //ImageView image = (ImageView) dialog.findViewById(R.id.image);
        //image.setImageResource(R.drawable.android);
        return dialog;
    }
    void onBtnDialogRight() {
        removeDialog(DIALOG_RESULT);
    }
	void onBtnSina() {
		
				exportPicture();
				showSinaBindView();
        removeDialog(DIALOG_RESULT);
    }
	void onBtnQQ() {
		
				exportPicture();
				showQQBindView();
        removeDialog(DIALOG_RESULT);
    }
	
	

	void showQQBindView() {
        //accessInfo = InfoHelper.getAccessInfo(mContext);
		// if(mGoController.isBindWeibo(this,"qq")==false)
		// 	mGoController.bindTencentWeibo(this,"weibo4android://ShareToWeiboView");
		// else
		// 	showWeiboShareView("qq");
			
	}
	
	void showSinaBindView() {

			
	}
	
	void showWeiboShareView(String what){
		// String fn = "sgf_gen.png";
  //       Intent intent=null;
  //       intent = new Intent(this,com.sureone.ShareToWeiboView.class);
		// intent.putExtra("weibo",what);
		// intent.putExtra("image",fn);
  //       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
  //       startActivity(intent);
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

    AlertDialog mWaitingDialog=null;
    void updateUser() {
        mPlayerStr=null;
        Desk d = mGoController.getMyDesk();
        if(d!=null) {
            LogLog("updateUser="+d.black+","+d.white);
            String info;
            if(d.black!=null) {
                info = d.black.name;
                mPlayerStr=info+"|"+d.black.rank;
                
				mGameTitle.setBlackName(info);
                float per = 0f;
                if(d.black.totals!=0) per=100f*(float)d.black.wins/((float)d.black.totals);
                //tv_blackInfo.setText(getString(R.string.rank)+""+d.black.rank+" "+ getString(R.string.rate)+(int)per);
                mGameTitle.setBlackInfo(d.black.rank+" | "+(int)per+"%");							

            } else {
                mGameTitle.setBlackInfo("");
                mGameTitle.setBlackName("");
            }
            //mBlackTxt.setBackgroundColor(Color.BLACK);
            if(d.white!=null) {
                info=d.white.name;
                mGameTitle.setWhiteName(info);
                mPlayerStr+=" -oo- "+info+"|"+d.white.rank;
                float per = 0f;
                if(d.white.totals!=0) per=100f*(float)d.white.wins/((float)d.white.totals);
                
                //tv_whiteInfo.setText(getString(R.string.rate)+(int)per+" "+ getString(R.string.rank)+""+d.white.rank);
                mGameTitle.setWhiteInfo((int)per+"% | "+d.white.rank);
            } else {
                mGameTitle.setWhiteInfo("");
                mGameTitle.setWhiteName("");
            }
        } else {
            LogLog("myDesk is null");
        }
    }

    void onBtnDone() {
        mGoController.requestDone();
    }
    void onBtnScore() {
        mGoController.requestScore();
    }
    void onBtnUndoDead() {
        mGoController.requestUndoDead();
    }
    void onBtnUndoStep() {
        mGoController.requestUndoStep();
    }
    void onBtnPass() {
        mGoController.requestPass();
    }
    void onBtnAlarm() {
        mGoController.requestAlarm();
    }
	
	void onBtnMute() {
		onMute();
    }
	
	void onMute(){
		bMute=!bMute;
		Button btnMute = (Button)this.findViewById(R.id.btnMute);	
		if(bMute){			
			btnMute.setText(getString(R.string.unmute));
		}else
			btnMute.setText(getString(R.string.mute));
	}

    void showMenuButtons() {
        mHideView = null;
        if(mGoController.isInGame()) {
            if(mGoController.isDianMu()) mShowView = mGroupDianMuButtons;
            else mShowView = mGroupGameButtons;
            switchButtonsAnimate();
        } else {
            User u = mGoController.getMyUser();
            if(u!=null) {
                if(u.isadmin==1) {
                    mShowView = mGroupAdminButtons;
                    switchButtonsAnimate();
                }
            }
        }
    }

    void onBtnReady() {
	
        if(mReadyBtnFlag==0)
            mGoController.requestReady();
        else {
            if(mShowView==null)
                showMenuButtons();
            else
                hideMenuButtons();
        }

    }
    void hideMenuButtons() {
        showTalkArea();
    }
    void onBtnJudgeWin(int sid) {
        Desk d = mGoController.getMyDesk();
        mGoController.judgeWin(d.id,sid);
    }
    final static class TalkMsg {
        int from;
        String content;
    }
    int parseTalkMessage(byte[] buf,int len,x_Integer o,TalkMsg tm) {
        String tag = xHelper.getStr(buf, len, o, '[');
        String v = xHelper.getStr(buf, len, o, ']');
        xHelper.log("goapp",tag+":"+v);
        if(tag.compareTo("CONTENT")==0) {
            tm.content=v;
        } else if(tag.compareTo("FROM")==0) {
            tm.from=Integer.parseInt(v);
        }
        return 0;
    }
    void onUserTalk(MyMsg mm) {
        byte[] buf = mm.buf;
        int len = mm.len;
        int offset = mm.offset;
        x_Integer o = new x_Integer(offset);
        TalkMsg tm = new TalkMsg();
        parseTalkMessage(buf,len,o,tm);
        parseTalkMessage(buf,len,o,tm);
        String s="";
        Desk d = mGoController.getMyDesk();
        if(d!=null) {
            if(d.black!=null && tm.from==1) {
                s+=d.black.name+":"+tm.content;
            }
            if(d.white!=null && tm.from==2) {
                s+=d.white.name+":"+tm.content;
            }
            if(tm.from>=3 && bMute==false) {
                s=tm.content;
            }
            showInfo(s);
        }
    }
    Intent mDiagAct = null;
    void onEnterDiag() {
        //gotoDiagActivity
        if(mDiagAct==null)
            mDiagAct= new Intent(this,com.sureone.DiagActivity.class);
        mDiagAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(mDiagAct);
    }

    char xToC(int x) {
        switch (x) {
        case 0:
            return 'a';
        case 1:
            return 'b';
        case 2:
            return 'c';
        case 3:
            return 'd';
        case 4:
            return 'e';
        case 5:
            return 'f';
        case 6:
            return 'g';
        case 7:
            return 'h';
        case 8:
            return 'j';
        case 9:
            return 'k';
        case 10:
            return 'l';
        case 11:
            return 'm';
        case 12:
            return 'n';
        case 13:
            return 'o';
        case 14:
            return 'p';
        case 15:
            return 'q';
        case 16:
            return 'r';
        case 17:
            return 's';
        case 18:
            return 't';
        default :
            return 'x';
        }
    }
    int getX(char c) {
        switch (c) {
        case 'a':
            return 0;
        case 'b':
            return 1;
        case 'c':
            return 2;
        case 'd':
            return 3;
        case 'e':
            return 4;
        case 'f':
            return 5;
        case 'g':
            return 6;
        case 'h':
            return 7;
        case 'j':
            return 8;
        case 'k':
            return 9;
        case 'l':
            return 10;
        case 'm':
            return 11;
        case 'n':
            return 12;
        case 'o':
            return 13;
        case 'p':
            return 14;
        case 'q':
            return 15;
        case 'r':
            return 16;
        case 's':
            return 17;
        case 't':
            return 18;
        default :
            return -1;
        }
    }
    void onSendTalk() {
        String s = mTxtPost.getText().toString().trim();
        //Hide the soft keyboard
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mTxtPost.getWindowToken(), 0);
        mTxtPost.setText("");
        if(s==null) return ;
        if(s.length()==0) return;
        if(s.compareTo(".diag")==0) {
            onEnterDiag();
        } else if(s.compareTo(".save")==0) {
            saveSgf();
        } else if(s.startsWith(".bb ")==true) {
            User u = mGoController.getMyUser();
            if(u!=null) {
                if(u.isadmin==1) {
                    mGoController.requestBroadcast(s.substring(4));
                }
            }
        } else if(s.compareTo(".off")==0) {
            mConn.shutdown();
        } else if(s.charAt(0)=='.') {
            char[] ss = s.toCharArray();
            if(ss[1]>='a' &&ss[1]<='t') {
                int x=getX(ss[1]);
                int count=0;
                if(ss.length>=3 && ss[2]>='1' && ss[2]<='9') count++;
                if(ss.length>=4 && ss[3]>='0' && ss[3]<='9') count++;
                if(count>0) {
                    String v = new String(ss,2,count);
                    int y = Integer.parseInt(v)-1;
                    if(x>=0 && x<19 && y>=0 && y<19 && isMyTurnToPutChess()==true)
                        mGoController.putChess(x,y);
                    if(mCursorTimer!=null) mCursorTimer.pause();
                }
            }

        } else
            mGoController.sendTalk(s);
    }
    CursorTimer mCursorTimer = null;
    public int getStepNo(int x,int y) {
        GoLogic logic = mGoController.getGoLogic(mBoardSize);
        return logic.getStepNo(x,y);
    }

    public void onPutChess(int x,int y) {
        mGoController.putChess(x,18-y);
        mCursorX=-1;
        mCursorY=-1;
		mBoardView.mCursorX=-1;
		mBoardView.mCursorY=-1;
		mBoardView.invalidateEx(false);
    }
    void saveSgf() {
        if(mGoController.saveSgf(this)>=0) {
            Toast.makeText(this, getString(R.string.sgfsaved), Toast.LENGTH_LONG).show();
        }
    }
    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    class ButtonEventListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btnStart:
                onBtnReady();
                break;
            case R.id.btnContinueGo:
                mGoController.continueGo();
                break;
            case R.id.btnSendTalk:
                onSendTalk();
                break;
            case R.id.btnBlackWin:
                onBtnJudgeWin(1);
                break;
            case R.id.btnWhiteWin:
                onBtnJudgeWin(2);
                break;
            case R.id.btnUndoDead:
                onBtnUndoDead();
                break;
            case R.id.btnGiveUp:
            case R.id.btnGiveUp2:
                onBtnGiveUp();
                break;
            case R.id.btnPass:
                onBtnPass();
                break;
            case R.id.btnAlarm:
                onBtnAlarm();
                break;
            case R.id.btnMute:
                onBtnMute();
                break;				
            case R.id.btnDone:
                onBtnDone();
                break;

            case R.id.btnScore:
                onBtnScore();
                break;

                /* Todo undo step
                            case R.id.btnUndoStep:
                                onBtnUndoStep();
                                break;
                */


            case R.id.btnFullBoard:
                switchBoard(true);
                break;
            case R.id.btn_dialog_right:
                onBtnDialogRight();
                break;
			// case R.id.btn_sina:
   //              onBtnSina();
   //              break;
			// case R.id.btn_qq:
   //              onBtnQQ();
   //              break;
            }

        }

    }


    void switchBoard(boolean b) {
        if(mBoardView.mFullBoard==b) return;
        mBoardView.switchBoardMode(b);
        if(b==true) {
            View v = mShowView;
            mShowView=mHideView;
            mHideView=v;
            switchButtonsAnimate();
        } else {
            mHideView=mShowView;
            mShowView=mFullBoardButtons;
            switchButtonsAnimate();
        }
    }

    void updateTimer() {

        if(mTimer.mBlack>-1 && mGameTitle!=null) {

            mGameTitle.setCenter1(""+mTimer.mBlack);
        }
        if(mTimer.mWhite>-1 && mGameTitle!=null) {
            //mCenterTxt.setText("" + mTimer.mWhite);
            mGameTitle.setCenter1(""+mTimer.mWhite);
        }
    }

    void showStepTimer(int timeout) {
        if(mGoController.getCurTurn()==1) {
            mTimer.mBlack=timeout;
            mTimer.mWhite=-1;
        } else {
            mTimer.mWhite=timeout;
            mTimer.mBlack=-1;
        }
    }

    Timer mTimer = null;

    private class CursorTimer implements Runnable {
        private boolean mStopped=false;
        private long mInterval;
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
                try {
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    continue;
                }
                if(!mStopped && mPause==false) {
                    Message message = new Message();
                    message.what = GoHandler.MSG_UPDATE_CURSOR;
                    mHandler.sendMessage(message);
                }
            }
        }
    }

    private class Timer implements Runnable {
        private boolean mStopped;
        private long mInterval;
        int mBlack=-1;
        int mWhite=-1;
        public Timer(long interval) {
            mInterval = interval;
            mStopped = false;
        }
        public synchronized void stop() {
            mStopped = true;
        }
        public void run() {
            while (!mStopped) {
                try {
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    continue;
                }
                if(mBlack>=-1 && !mStopped) {
                    Message message = new Message();
                    message.what = GoHandler.MSG_UPDATE_TIMER;
                    mHandler.sendMessage(message);
                    mBlack--;
                }
                if(mWhite>=-1 && !mStopped) {
                    Message message = new Message();
                    message.what = GoHandler.MSG_UPDATE_TIMER;
                    mHandler.sendMessage(message);
                    mWhite--;
                }

            }
        }
    }

    boolean isMyTurnToPutChess() {
        return true;
        /*
        if(==null) return false;
        if(xSystem.mCurTurn==xSystem.mMyUser.sid)
        	return true;
        return false;
        */
    }

    boolean confirmLeaved=false;
    void showLeaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmleave)
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirmLeaved=true;
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirmLeaved=false;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void parseDianMuResult(MyMsg mm) {
        byte[] buf = mm.buf;
        int len = mm.len;
        int offset = mm.offset;
        x_Integer o = new x_Integer(offset);
        mDianMuResult.clean();
        String isok = xHelper.getStr(buf, len, o, ',');
        int bmu = xHelper.getInt(buf,len,o,',');
        int wmu = xHelper.getInt(buf,len,o,',');
        xHelper.log("goapp","arrive the dianmu_result");
        mDianMuResult.bmu=bmu;
        mDianMuResult.wmu=wmu;

        if(bmu>0) {
            int side = xHelper.getInt(buf, len, o, ',');
            int num =  xHelper.getInt(buf, len, o, ',');
            while(num>0) {
                int x =  xHelper.getInt(buf, len, o, ',');
                int y =  xHelper.getInt(buf, len, o, ',');
                mDianMuResult.mus[y*mBoardSize+x]=(byte)side;
                num--;
            }
        }

        if(wmu>0) {
            int side = xHelper.getInt(buf, len, o, ',');
            int num =  xHelper.getInt(buf, len, o, ',');
            while(num>0) {
                int x =  xHelper.getInt(buf, len, o, ',');
                int y =  xHelper.getInt(buf, len, o, ',');
                mDianMuResult.mus[y*mBoardSize+x]=(byte)side;
                num--;
            }
        }
        //stone number
        bmu = xHelper.getInt(buf,len,o,',');
        wmu = xHelper.getInt(buf,len,o,',');
        mDianMuResult.bmu=bmu;
        mDianMuResult.wmu=wmu;

    }
    void onScore(MyMsg mm) {
        parseDianMuResult(mm);
        String str = this.getString(R.string.black)+" "+mDianMuResult.bmu+","+
                     this.getString(R.string.white)+" "+mDianMuResult.wmu+
                     "\n"+this.getString(R.string.blackTie);
        showInfo(str);
        mBoardView.invalidateEx(true);
    }
    void onStartDianMu(MyMsg mm) {
        /*
        String str = this.getString(R.string.black)+" "+mDianMuResult.bmu+","+
                     this.getString(R.string.white)+" "+mDianMuResult.wmu;
        showInfo(str);
        */
        mBoardView.mDianMuMode=true;
        mHideView = mShowView;
        mShowView = mGroupDianMuButtons;
        switchButtonsAnimate();
        showInfoDialog(R.string.dianmustep);
    }
    void onBtnGiveUp() {
        mGoController.requestGiveUp();
    }
    void showInfoDialog(int infoId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(infoId)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void stopTimer() {
        mTimer.mBlack=-1;
        mTimer.mWhite=-1;
    }
    class GoHandler extends Handler {
        public static final int MSG_UPDATE_USER = 0x1001;
        public static final int MSG_UPDATE_GAME = 0x1002;
        public static final int MSG_UPDATE_TIMER = 0x1003;
        public static final int MSG_UPDATE_CURSOR = 0x1004;
		public static final int MSG_UPDATE_HEART_BEAT = 0x1005;
		
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_UPDATE_USER: {
                updateUser();
                break;
            }

            case MSG_UPDATE_GAME: {
                mBoardView.invalidateEx(true);
                break;
            }

            case MSG_UPDATE_CURSOR: {
                mBoardView.updateCursor();
                mBoardView.showFocus();
                break;
            }
            case MSG_UPDATE_TIMER: {
                updateTimer();
                break;
            }
			
			case MSG_UPDATE_HEART_BEAT:{
				onHeartBeat(false);
				break;
			}

            case GoController.MSG_NTFY_ARRIVE_START_DIANMU: {
                onStartDianMu((MyMsg)(msg.obj));
                break;
            }
            case GoController.MSG_NTFY_ARRIVE_SCORE: {
                onScore((MyMsg)(msg.obj));
                break;
            }

            case GoController.MSG_NTFY_GAME_START: {
                onGameStart();
                break;
            }
            case GoController.MSG_CONNECT_BROKEN: {
                onConnectBroken();
                break;
            }
            case GoController.MSG_CONNECT_TIMEOUT: {
                onConnectTimeout();
                break;
            }
            case GoController.MSG_CONNECT_OK: {
                onSilentConnectOK();
                break;
            }
            case GoController.MSG_RSP_OBSERVE_OK: {
                onObserveOK();
                break;
            }
            case GoController.MSG_LOGIN_OK: {
                onLoginOK();
                break;
            }
            case GoController.MSG_LOGIN_RESUME: {
                onLoginResume();
                break;
            }
            case GoController.MSG_NTFY_GAME_END: {
                onGameEnd((Bundle)(msg.obj));
                break;
            }
            case GoController.MSG_NTFY_ALARM: {
                onAlarm(msg.arg1);
                break;
            }
            case GoController.MSG_RSP_RESUME_OK: {
                xHelper.log("goapp","RESUME OK");
                onResumeOK();
                break;
            }

            case GoController.MSG_RSP_USER_PASS: {
                onRspPass();
                break;
            }
            case GoController.MSG_NTFY_TIMEOUT_STEP: {
                onTimeoutStep(msg.arg1);
                break;
            }
            case GoController.MSG_NTFY_TIMEOUT_JOIN: {
                onTimeoutJoin(msg.arg1);
                break;
            }
            case GoController.MSG_NTFY_TIMEOUT_DIANMU: {
                onTimeoutDianMu(msg.arg1);
                break;
            }
            case GoController.MSG_NTFY_USER_READY: {
                onNtfyUserReady();
                break;
            }
            case GoController.MSG_RSP_USER_READY: {
                onUserReady();
                break;
            }
			
			case GoController.MSG_NTFY_HEART_BEAT:{
				onHeartBeat(true);
				break;
			}
            case GoController.MSG_NTFY_USER_DONE: {
                onUserDone(msg.arg1);
                break;
            }
            case GoController.MSG_NTFY_USER_TALK: {
                onUserTalk((MyMsg)(msg.obj));
                break;
            }
            case GoController.MSG_NTFY_USER_LEAVE: {
                onUserLeave(msg.arg1);
                break;
            }
            case GoController.MSG_NTFY_USER_PASS: {
                onUserPass();
                break;
            }
            case GoController.MSG_NTFY_USER_JOIN: {
                onUserJoin(msg.arg1);
                break;
            }
            case GoController.MSG_NTFY_STEP:
            case GoController.MSG_RSP_STEP: {
                onStep((MyMsg)(msg.obj));
                break;
            }
            case GoController.MSG_NTFY_NEXT_SIDE:
                onNext();
                break;
            case  GoController.MSG_NTFY_OBSERVE_START:
                onObserveStart((MyMsg)(msg.obj));
                break;
            case GoController.MSG_NTFY_SET_DEAD:
                onSetDead((MyMsg)(msg.obj));
                break;
            case GoController.MSG_NTFY_UNDO_DEAD:
                onUndoDead();
                break;
            case GoController.MSG_NTFY_CONTINUE_GO:
                onContinueGo();
                break;
            }

        }
    }
    void onContinueGo() {
        mDianMuResult.clean();
        mBoardView.hideFocus();
        mBoardView.invalidateEx(true);
        showTalkArea();
    }
    void onUndoDead() {
        mDianMuResult.clean();
        mBoardView.hideFocus();
        mBoardView.invalidateEx(true);
    }
    void onSetDead(MyMsg mm) {
        mBoardView.hideFocus();
        mBoardView.invalidateEx(true);
    }
    void playSound(int id) {
        MediaPlayer mPlayer = null;
        AudioManager audioManager = (AudioManager)(this.getSystemService(Context.AUDIO_SERVICE));
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL && app.getSilentMode()==false) {
            if(mPlayer == null) {
                mPlayer=MediaPlayer.create(this, id);
            }
            //float vol=(float)(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            //mPlayer.setVolume(vol,vol);
            mPlayer.start();
        }

    }
	

	void onHeartBeat(boolean bLight){
		if(bLight){
			mGameTitle.setFooterColor(getResources().getColor(R.color.beat_heart_light));
			mHandler.postDelayed(new MyRunnable(), 400);
		}
		else
			mGameTitle.setFooterColor(getResources().getColor(R.color.tab_background_color));
		
	}
	
	
	static final int LOAD_LIST_DELAY=1;
	private class MyRunnable implements Runnable {
		@Override
		public void run() {
			Message msg = new Message();
			msg.what = GoHandler.MSG_UPDATE_HEART_BEAT;
			mHandler.sendMessage(msg);
		}
	}

    void onObserveStart(MyMsg mm) {
		GoLogic logic = mGoController.getGoLogic(mBoardSize);
	    Desk d = mGoController.getMyDesk();
        if(d!=null) {
            if(d.black!=null) {
                logic.setSgfHeader("PB",d.black.name);
				logic.setSgfHeader("BR",d.black.rank);
            }
            if(d.white!=null) {
                logic.setSgfHeader("PW",d.white.name);
				logic.setSgfHeader("WR",d.white.rank);
            }
        }
        logic.setSgfHeader("EV",this.getString(R.string.appnamecn));
        logic.setSgfHeader("RE",this.getString(R.string.rena));

        mBoardView.mDianMuMode=false;
        mLastStep.x=mm.x;
        mLastStep.y=mm.y;
        mBoardView.hideFocus();
        mBoardView.invalidateEx(true);
    }
    void onStep(MyMsg mm) {
        mCursorX=-1;
        mCursorY=-1;
		mBoardView.mCursorX=-1;
		mBoardView.mCursorY=-1;
        mLastStep.x=mm.x;
        mLastStep.y=mm.y;
        if(mCursorTimer!=null)
            mCursorTimer.pause();
        mBoardView.hideFocus();
        mBoardView.invalidateEx(true);
        playSound(R.raw.putchess);

    }

    void onNext(){
        showStepTimer(mGoController.getStepTimeout());
    }
    void onUserPass() {
        showInfoDialog(R.string.passinfo);
    }

    void onUserLeave(int uid) {
        xHelper.log("goapp","user is leave uid="+uid);
        updateUser();
    }
    void onNtfyUserReady() {
        xHelper.log("goapp","onUserReady is called");
        Desk d = mGoController.getMyDesk();
        String s="";
        if(d!=null) {
            if(d.black!=null && d.black.status==User.USER_READY) {
                s+=d.black.name+this.getString(R.string.isready);
            }
            if(d.white!=null && d.white.status==User.USER_READY) {
                s+=d.white.name+this.getString(R.string.isready);
            }
        }
        showInfo(s);
        updateUser();
        /* change the start button to menu button after I am ready */

    }

    void onUserDone(int sid) {

        Desk d = mGoController.getMyDesk();
        String s="";
        if(d!=null) {
            if(sid==1 && d.black!=null ) {
                s+=d.black.name+this.getString(R.string.isdone);
            }
            if(sid==2 && d.white!=null) {
                s+=d.white.name+this.getString(R.string.isdone);
            }
        }
        showInfo(s);
    }

    void onRspPass() {
        showInfo(getString(R.string.ipass));
    }

    void onUserReady() {
        xHelper.log("goapp","onUserReady is called");
        Desk d = mGoController.getMyDesk();
        String s="";
        if(d!=null) {
            if(d.black!=null && d.black.status==User.USER_READY) {
                s+=d.black.name+this.getString(R.string.isready)+",";
            }
            if(d.white!=null && d.white.status==User.USER_READY) {
                s+=d.white.name+this.getString(R.string.isready);
            }
        }
        showInfo(s);
        updateUser();
        /* change the start button to menu button after I am ready */
        showTalkArea();

    }

    View mHideView=null;
    View mShowView=null;
    public void showButtonsAnimate() {
        //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        //Now Set your animation
        Animation fadeInAnimation=null;
        fadeInAnimation =  AnimationHelper.inFromBottomAnimation(200);
        fadeInAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                mShowView.setVisibility(View.VISIBLE);
            }
        });
        mShowView.startAnimation(fadeInAnimation );
    }
    public void switchButtonsAnimate() {
        if(mHideView!=null) mHideView.setVisibility(View.GONE);
        if(mShowView!=null) mShowView.setVisibility(View.VISIBLE);
        if(mShowView!=null) {
            mBtnReady.setText(this.getString(R.string.hide));
        } else {
            updateStartButton();
        }
    }

    int mReadyBtnFlag = 0; // 0 = start, 1 = menu, 2 = hide
    void updateStartButton() {
        mBtnReady.setText(this.getString(R.string.menu));
        mReadyBtnFlag=1;
        if(mGoController.getMyUser()!=null) {
            int myStatus = mGoController.getMyStatus();
            if(myStatus == User.USER_JOIN) {
                mReadyBtnFlag=0;
                mBtnReady.setText(this.getString(R.string.start));
            }
        }
    }

    private static final int CMD_BLACK_WIN  = 1;
    private static final int CMD_WHITE_WIN  = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        //item = menu.add(0, CMD_BLACK_WIN, 0, R.string.blackwin);
        //item.setIcon(R.drawable.clear_history);
        //item = menu.add(0, CMD_WHITE_WIN, 0, R.string.whitewin);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    void onUserJoin(int uid) {
        updateUser();
        User u = mGoController.getUser(uid);
        LogLog("user is joined="+u.name);
        showNotification(u.name+this.getString(R.string.joingame));
    }
    void LogLog(String s) {
        xHelper.log("goapp","GoActivity:"+s);
    }
    void onTimeoutStep(int side) {

    }

    void showNotification(String str) {
        NotificationManager mNotiMan=null;
        mNotiMan=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new Notification(R.drawable.icon,str,System.currentTimeMillis());
        notification.defaults=Notification.DEFAULT_ALL;
        if(app.getSilentMode()==true) {
            notification.defaults &= ~(Notification.DEFAULT_SOUND);
            notification.defaults &= ~(Notification.DEFAULT_VIBRATE);
        }
        notification.flags=Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, main.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pt=PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this,"",str,pt);
        mNotiMan.notify(192345, notification);
    }

    void showJoinTimeOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.jointimeout)
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //leaveTheDesk();
                mGoController.setMyStatus(User.USER_JOIN);

                Intent intent= new Intent(mContainer,com.sureone.RoomFlowView.class);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //user in join status have too long time, will be kick out of the desk
    void onTimeoutJoin(int side) {
        if(side==mGoController.getMySide())
            showJoinTimeOutDialog();

    }
    void onTimeoutDianMu(int side) {
       // showStepTimer();
    }

    public boolean haveLogic() {
        return !(mGoController.getGoLogic(mBoardSize)==null);
    }

    public int getLastStepX() {
        return mLastStep.x;
    }
    public int getLastStepY() {

        return mLastStep.y;
    }

    public int getSide(int x,int y) {
        GoLogic logic = mGoController.getGoLogic(mBoardSize);
        return logic.getSide(x,y);
    }
    void resumeGame() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        GoLogic logic = mGoController.getGoLogic(mBoardSize);




        showInfo(this.getString(R.string.gamestart));
        updateUser();
        Desk d = mGoController.getMyDesk();
        if(d!=null) {
            if(d.black!=null) {
                logic.setSgfHeader("PB",d.black.name);
				logic.setSgfHeader("BR",d.black.rank);
            }
            if(d.white!=null) {
                logic.setSgfHeader("PW",d.white.name);
				logic.setSgfHeader("WR",d.white.rank);
            }
        }
        logic.setSgfHeader("EV",this.getString(R.string.appnamecn));
        logic.setSgfHeader("RE",this.getString(R.string.rena));
        showStepTimer(mGoController.getResumeTimeout());
    }
    void onGameStart() {

        GoLogic logic = mGoController.getGoLogic(mBoardSize);

        logic.clear();

        mBoardView.mDianMuMode=false;
        //mBoardLayout.removeView(mBtnReady);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showInfo(this.getString(R.string.gamestart));
        updateUser();
        Desk d = mGoController.getMyDesk();
        if(d!=null) {
            if(d.black!=null) {
                logic.setSgfHeader("PB",d.black.name);
				logic.setSgfHeader("BR",d.black.rank);
            }
            if(d.white!=null) {
                logic.setSgfHeader("PW",d.white.name);
				logic.setSgfHeader("WR",d.white.rank);
            }
        }
        logic.setSgfHeader("EV",this.getString(R.string.appnamecn));
        logic.setSgfHeader("RE",this.getString(R.string.rena));
        mBoardView.invalidateEx(true);
        playSound(R.raw.ringer);
        showStepTimer(mGoController.getStepTimeout());
    }

    void onLoginOK() {
        //back to desk list
        Desk d = mGoController.getMyDesk();
        if(viewMode.compareTo("observe")==0) {
            if(d!=null) mGoController.joinObserve(d.id);
        } else {
            leaveActivity();
        }

    }
    void onObserveOK() {
	        GoLogic logic = mGoController.getGoLogic(mBoardSize);
	        Desk d = mGoController.getMyDesk();
        if(d!=null) {
            if(d.black!=null) {
                logic.setSgfHeader("PB",d.black.name);
				logic.setSgfHeader("BR",d.black.rank);
            }
            if(d.white!=null) {
                logic.setSgfHeader("PW",d.white.name);
				logic.setSgfHeader("WR",d.white.rank);
            }
        }
        logic.setSgfHeader("EV",this.getString(R.string.appnamecn));
        logic.setSgfHeader("RE",this.getString(R.string.rena));
        xHelper.log("goapp","GoActivity:onObserveOK");
    }
    void onResumeOK() {
        //back to desk list
        GoLogic logic = mGoController.getGoLogic(mBoardSize);

        mLastStep.x=logic.mLastX;
        mLastStep.y=logic.mLastY;
        mBoardView.invalidateEx(true);
        //Here we resend the go step if it have
        mGoController.doRetry();
        xHelper.log("goapp","GoActivity:onResumeOK");
    }
    void onLoginResume() {
        mGoController.resume();
    }
    void onSilentConnectOK() {
        xHelper.log("goapp","GoActivity:onSilentConnectOK");
        //showToast(this.getString(R.string.silentconnok));
        showDebugInfo("TCP reconnect ok.");
        String email = app.getEmail();
        String pin = app.getPin();
        mGoController.login(email,pin);
    }
    void onConnectBroken() {
        int myStatus = mGoController.getMyStatus();
        xHelper.log("goapp","GoActivity:connect broken");
        showDebugInfo("TCP error happened,do reconnect...");
        //showToast(this.getString(R.string.silentreconn));
        mGoController.silentReconnect();
    }
    void onConnectTimeout() {

        showDebugInfo("TCP connect timeout.");
        //showToast(this.getString(R.string.silentreconn));

    }
    void onAlarm(int did) {
        String text = String.format(getResources().getString(R.string.alarm), did);
        showToast(text);
    }
    void onGameEnd(Bundle mm) {
        GoLogic logic = mGoController.getGoLogic(mBoardSize);
        xHelper.log("goapp","onGameEnd called:");
        int winner = mm.getInt("win");
        int bmu = mm.getInt("bmu");
        int wmu = mm.getInt("wmu");
        mDianMuResult.bmu=bmu;
        mDianMuResult.wmu=wmu;
        mDianMuResult.winner = winner;
        stopTimer();
        mBoardView.invalidateEx(true);
		
        String str= logic.getSgfHeader("PB")+"|"+logic.getSgfHeader("BR")+ " "+getString(R.string.vs)+" "+logic.getSgfHeader("PW")+"|"+logic.getSgfHeader("WR");
		String sstr=str;
        if(mDianMuResult.winner==1) {			
            str += "\n"+this.getString(R.string.black)+" "+this.getString(R.string.win)+","
                  +"\n"+this.getString(R.string.black)+":"+mDianMuResult.bmu+"\n"+
                  this.getString(R.string.white)+":"+mDianMuResult.wmu+
                  "\n"+this.getString(R.string.blackTie);
			sstr+= " "+this.getString(R.string.black)+" "+this.getString(R.string.win)+","
                  +" "+this.getString(R.string.black)+":"+mDianMuResult.bmu+" "+
                  this.getString(R.string.white)+":"+mDianMuResult.wmu+
                  " "+this.getString(R.string.blackTie);
            logic.setSgfHeader("RE","B+R");
        } else if(mDianMuResult.winner==2) {			
            str += "\n"+this.getString(R.string.white)+" "+this.getString(R.string.win)+","
                  +"\n"+this.getString(R.string.black)+":"+mDianMuResult.bmu+"\n"+
                  this.getString(R.string.white)+":"+mDianMuResult.wmu+
                  "\n"+this.getString(R.string.blackTie);
			sstr += " "+this.getString(R.string.white)+" "+this.getString(R.string.win)+","
                  +" "+this.getString(R.string.black)+":"+mDianMuResult.bmu+" "+
                  this.getString(R.string.white)+":"+mDianMuResult.wmu+
                  " "+this.getString(R.string.blackTie);
            logic.setSgfHeader("RE","W+R");
        } else {			
            str += "\n"+this.getString(R.string.ping)+","
                  +"\n"+this.getString(R.string.black)+":"+mDianMuResult.bmu+"\n"+
                  this.getString(R.string.white)+":"+mDianMuResult.wmu;
			sstr += " "+this.getString(R.string.ping)+","
                  +" "+this.getString(R.string.black)+":"+mDianMuResult.bmu+" "+
                  this.getString(R.string.white)+":"+mDianMuResult.wmu;
            logic.setSgfHeader("RE","B:"+mDianMuResult.bmu+"W:"+mDianMuResult.wmu);
        }
        xHelper.log("goapp","onGameEnd called:"+str);
        saveSgf();
        showInfo(str);
        Desk d = mGoController.getMyDesk();
        String s="";
        if(d!=null) {
            if(d.black!=null) {
                d.black.status=User.USER_JOIN;
            }
            if(d.white!=null) {
                d.white.status=User.USER_JOIN;;
            }
        }
        mGameTitle.setCenter1("");
        showTalkArea();
        mGameResult=str;          
		mGoController.setNextWeiboMsg(sstr);		
		showDialog(DIALOG_RESULT);
        mBoardView.mDianMuMode=true;
        mBoardView.invalidateEx(true);
		mDianMuResult.clean();
		
		
		
        //mBoardLayout.addView(mBtnReady);
    }


    void showTalkArea() {
        updateStartButton();
        mTalkArea.setVisibility(View.VISIBLE);
        if(mShowView!=null) {
            mShowView.setVisibility(View.GONE);
        }
        mShowView = null;
        mHideView = null;
    }


    float mScreenX;
    float mScreenY;

    float mLastMoveX;
    float mLastMoveY;
    boolean mMoveStart=false;
    boolean mMoving=false;
    boolean bCursorMode=false;

    boolean bFingerDown=false;
    boolean bFingerMove=false;
    boolean bFingerUp=false;

    int mCursorX=-1;
    int mCursorY=-1;
    float mLastX=(float)0.0;
    float mLastY=(float)0.0;

    class TouchInputEventListener implements android.view.View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            boolean eventstatus = true;
            boolean bMyTouch=false;
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = event.getX();
                float y = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float x = event.getX();
                float y = event.getY();
                break;
            }
            default: {
            }
            }
            return eventstatus;
        }
    }
    class InputEventListener implements android.view.View.OnKeyListener,android.view.View.OnTouchListener,
        android.view.View.OnClickListener,android.view.View.OnLongClickListener {


        boolean bMyTouch=false;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            boolean eventstatus = true;
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = event.getX();
                float y = event.getY();
                mScreenX=x;
                mScreenY=y;
                bMyTouch=true;
                if(bCursorMode==false) {
                    mBoardView.reCalFocus(x,y);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float x = event.getX();
                float y = event.getY();
                if(bMyTouch) {
                    bMyTouch=false;
                }
                xHelper.log("GoActivity","Touch finger up");
                if(bCursorMode==true) {
                    mScreenX=x;
                    mScreenY=y;
                    if(isMyTurnToPutChess()==true) {
                        mBoardView.ptol(mScreenX,mScreenY);
                        mCursorX=mBoardView.mCursorX;
                        mCursorY=mBoardView.mCursorY;
                        if(mCursorTimer == null) {
                            mCursorTimer = new CursorTimer();
                            mCursorTimer.setInterval(250);
                            new Thread(mCursorTimer, "Cursor Refresh Timer").start();
                        }
                        mBoardView.invalidateEx(false);
                        mCursorTimer.resume();
                        String s = "."+xToC(mCursorX)+(19-mCursorY);
                        mTxtPost.setText(s);
                    }
                } else {

                    if(viewMode.compareTo("normal")==0 && mBoardView.mDianMuMode==false) {
                        if(mCursorTimer == null) {
                            mCursorTimer = new CursorTimer();

                            new Thread(mCursorTimer, "Cursor Refresh Timer").start();
                        }
                        mCursorTimer.setInterval(500);
                        mCursorTimer.resume();
                        mBoardView.showFocus();
                    }


                    if(bShowBigView==true) {
                        if(mBoardView.mFullBoard==true) {
                            xHelper.log("GoActivity","Touch finger click");
                             switchBoard(false);

                        } else {

                            mBoardView.putChess();
                            switchBoard(true);

                        }
                    } else {
                        if(isMyTurnToPutChess()==true )
                            mBoardView.putChess();
                    }

                    GoActivity.this.mBoardView.mCurrentSide = GoActivity.this.mGoController.getMySide();
                    mBoardView.invalidateEx(false);
                }
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

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub

            Log.w("sureoneApp","== keyCode: "+keyCode);
            return false;
        }
    }

    class FocusWidget {
        Bitmap mSmall=null;
        boolean visible=false;
    }

    class ChessBMP {
        Bitmap mSmall=null;
        Bitmap mShadow=null;
        Bitmap mMu=null;
    }
	

}
