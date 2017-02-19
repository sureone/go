package com.sureone.igs;
import android.widget.Toast;
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
import com.sureone.*;




public class IgsGoView extends Activity {
    public static final int ID_BOARD=1;
    //1 = play , 2=observe
    public static int mViewMode=1;

    EditText mTxtPost = null;
    public static final int LINE_COLOR=
        xSystem.FRONT_COLOR;
    public static final int BOARD_COLOR=xSystem.BACK_COLOR;
    IgsController mController = null;
    public TextView mBlackTxt=null;
    public TextView mWhiteTxt=null;
    public TextView mCenterTxt=null;
    public TextView mInfoTxt=null;
    private GoHandler mHandler = new GoHandler();
    Activity mContainer=null;
    BoardViewGo mBoardView=null;
    DisplayMetrics mDM=null;
    int mBoardSize = 19;
    public boolean bShowBigView = true;
    public int mStepTimeOut = 60*3;
    Button mBtnMenu=null;
    Button mBtnPost=null;
    Button mBtnBackTalk=null;
    Button mBtnBackTalk2=null;
    Button mBtnPass=null;
    Button mBtnDone=null;
    Button mBtnScore=null;
    Button mBtnGiveUp=null;
    Button mBtnGiveUp2=null;
    Button mBtnFullBoard=null;
    ButtonEventListener btnEvtListener = new ButtonEventListener();
    LinearLayout mScreen;
    LinearLayout mTalkArea=null;
    /** Called when the Activity is first created. */

    class LastStep {
        LastStep() {
            x=-1;
            y=-1;
        }
        int x;
        int y;
    };
    class DianMuResult {
        int bmu;
        int wmu;
        int winner;
        byte[] mus=new byte[mBoardSize*mBoardSize];
        void clean() {
            bmu=0;
            wmu=0;
            for(int i=0; i<mBoardSize*mBoardSize; i++) {
                mus[i]=0;
            }
        }
        int getSide(int x,int y) {
            int idx=y*mBoardSize+x;
            return mus[idx];
        }
    }
    String mMsg1="";
    String mMsg2="";
    DianMuResult mDianMuResult = null;
    LastStep mLastStep = null;
    GoApp app = null;
    RelativeLayout mContentGroup =null;
    TableLayout mGroupGameButtons =null;
    TableLayout mBigViewButtons =null;
    TableLayout mScoreButtons =null;

    // com.google.ads.AdView adView=null;
    GoLogic mGoLogic = null;
    GoApp mApp=null;
    @Override
    public void onCreate(Bundle parent) {
        super.onCreate(parent);
        LogLog("IgsGoView onCreate");
        mViewMode = getIntent().getIntExtra("viewMode",1);
        mApp =  GoApp.getInstance();
        mController = mApp.getIgsController();
        mController.setViewHandler(mHandler);
        mGame = mController.getCurrentGame();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.igsgoview);
        mContainer=this;
        app =  GoApp.getInstance();
        mLastStep = new LastStep();
        if(mGame!=null) mGoLogic = mGame.getGoLogic();
        if(mGoLogic!=null) {
            mLastStep.x=mGoLogic.mLastX;
            mLastStep.y=mGoLogic.mLastY;
        }
        setTitle(getString(R.string.gameno)+" "+mGame.getGameNo());
        mDianMuResult = new DianMuResult();
        mTimer = new Timer(1000);
        new Thread(mTimer, "Igs Go View Timer").start();
        mContentGroup = (RelativeLayout)this.findViewById(R.id.mainlayout);
        mGroupGameButtons = (TableLayout)this.findViewById(R.id.onGameButtons);
        mScoreButtons = (TableLayout)this.findViewById(R.id.scoreButtons);
        mGroupGameButtons.setVisibility(View.GONE);
        mScoreButtons.setVisibility(View.GONE);
        mBigViewButtons = (TableLayout)this.findViewById(R.id.bigviewButtons);
        mBigViewButtons.setVisibility(View.GONE);
        //mScreen.setBackgroundColor(BOARD_COLOR);
        //audio control settings
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mTalkArea = (LinearLayout) this.findViewById(R.id.talkArea);
        mTalkArea.setVisibility(View.GONE);
        mShowView=null;
        mTxtPost = (EditText)this.findViewById(R.id.txtTalk);
        mBlackTxt = (TextView) this.findViewById(R.id.txtBlackGo);
        mWhiteTxt = (TextView) this.findViewById(R.id.txtWhiteGo);
        mCenterTxt = (TextView) this.findViewById(R.id.txtCenterGo);
        mInfoTxt = (TextView)this.findViewById(R.id.txtInfoGo);
        mInfoTxt.setTextColor(Color.BLACK);

        mBtnPost = (Button)this.findViewById(R.id.btnSendTalk);
        mBtnPost.setOnClickListener(btnEvtListener);
        mBtnMenu = (Button)this.findViewById(R.id.btnMenu);
        mBtnMenu.setOnClickListener(btnEvtListener);

        mBtnBackTalk = (Button)this.findViewById(R.id.btnBackTalk);
        mBtnBackTalk.setOnClickListener(btnEvtListener);

        mBtnPass = (Button)this.findViewById(R.id.btnPass);
        mBtnPass.setOnClickListener(btnEvtListener);

        mBtnGiveUp = (Button)this.findViewById(R.id.btnGiveUp);
        mBtnGiveUp.setOnClickListener(btnEvtListener);
        mBtnFullBoard = (Button)this.findViewById(R.id.btnFullBoard);
        mBtnFullBoard.setOnClickListener(btnEvtListener);
        mBtnScore = (Button)this.findViewById(R.id.btnScore);
        mBtnScore.setOnClickListener(btnEvtListener);
        mBtnDone = (Button)this.findViewById(R.id.btnDone);
        mBtnDone.setOnClickListener(btnEvtListener);
        mBtnBackTalk2 = (Button)this.findViewById(R.id.btnBackTalk2);
        mBtnBackTalk2.setOnClickListener(btnEvtListener);
        mBtnGiveUp2 = (Button)this.findViewById(R.id.btnGiveUp2);
        mBtnGiveUp2.setOnClickListener(btnEvtListener);
        mWhiteTxt.setGravity(Gravity.RIGHT);

        mInfoTxt.setText("");
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
        showButtons(-1);
        xHelper.log("igs", "IgsGoView onCreate");
        updateUser();

    }
    void showButtons(int idButtonPressed) {
        if(mShowView==null) {
            mTalkArea.setVisibility(View.VISIBLE);
            mShowView=mTalkArea;
            mCurButtonsView=mShowView;
        } else if(mShowView == mTalkArea && idButtonPressed == -1) {
            mCurButtonsView=mShowView;
        } else if(mShowView == mTalkArea && idButtonPressed == R.id.btnMenu) {
            mHideView = mCurButtonsView;
            mShowView = mGroupGameButtons;
            switchButtonsAnimate();
            mCurButtonsView=mShowView;
        } else if(mShowView == mGroupGameButtons && idButtonPressed == R.id.btnBackTalk) {
            mHideView = mCurButtonsView;
            mShowView = mTalkArea;
            switchButtonsAnimate();
            mCurButtonsView=mShowView;
        }
    }
    View mCurButtonsView=null;
    RelativeLayout mBoardLayout=null;

    void createBoard() {
        mBoardView = new BoardViewGo(this);
        RelativeLayout parent = (RelativeLayout)this.findViewById(R.id.panelGo);
        mBoardLayout = parent;
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        mBoardView.setId(ID_BOARD);
        parent.addView(mBoardView,lp1);
        mDM=new DisplayMetrics();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindowManager().getDefaultDisplay().getMetrics(mDM);
   //      adView = (com.google.ads.AdView)(this.findViewById(R.id.adView));
   //      if(app.getAdMode()==false) {
   //          //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			// if(mDM.heightPixels<854)
   //          	((RelativeLayout)this.findViewById(R.id.adArea)).setVisibility(View.GONE);
			// else{
   //          	((RelativeLayout)this.findViewById(R.id.adArea)).setVisibility(View.GONE);
			// 	adView.setVisibility(View.GONE);
   //          	//adView.loadAd(new com.google.ads.AdRequest());
			// }
   //      } else {
			// adView.setVisibility(View.GONE);
   //         	((RelativeLayout)this.findViewById(R.id.adArea)).setVisibility(View.GONE);
   //          //adView.loadAd(new com.google.ads.AdRequest());
   //      }

        mBoardView.setMinimumWidth(mDM.widthPixels);
        mBoardView.setMinimumHeight(mDM.widthPixels);
    }

    @Override
    public void onPause() {
        xHelper.log("goapp", "go onPause");
        super.onPause();
    }
    public void leaveActivity() {
        if(mController.getViewHandler()==mHandler)
            mController.setViewHandler(null);
        //back to DeskList
        /*
        Intent intent = new Intent(this,com.sureone.DeskListActivity.class);
        intent.putExtra("login","ok");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        */
        mContainer.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //user will leave the last desk only which is full
            int state = mController.getState();
            if(state == IgsController.IN_OBSERVE) {
                mController.unobserve();
            } else if(state == IgsController.IN_GAME) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirmleave)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mController.resign();
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
            }
            leaveActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        mController.setViewHandler(mHandler);
        updateUser();
        LogLog("IgsGoView onResume");
        super.onResume();
    }
    @Override
    public void onStop() {
        mTimer.stop();
        if(mController.getViewHandler()==mHandler)
            mController.setViewHandler(null);
        LogLog("IgsGoView onStop");
        super.onStop();
    }
    @Override
    public void onStart() {
        mController.setViewHandler(mHandler);
        LogLog("IgsGoView onStart");
        super.onStart();
    }
    void showInfo(String str) {
        mInfoTxt.setMovementMethod(new ScrollingMovementMethod());
        String s = mInfoTxt.getText().toString();
        s=str+"\n"+s;
        mInfoTxt.setTextColor(android.graphics.Color.BLACK);
        mInfoTxt.setText(s);
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
    AlertDialog showResignDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                leaveActivity();
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
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }
    AlertDialog mWaitingDialog=null;
    IgsGame mGame = null;
    void updateUser() {
        if(mGame!=null) {
            String info;
            mBlackTxt.setTextColor(Color.BLACK);
            info = mGame.getName('B')+" ["+ mGame.getRank('B')+"]";
            mBlackTxt.setText(info);
            mWhiteTxt.setTextColor(Color.BLACK);
            info = "["+ mGame.getRank('W')+"] "+mGame.getName('W');
            mWhiteTxt.setText(info);
        }
    }
    String mDeads="";
    void onBtnDone() {
        mController.done();
    }
    void onBtnScore() {
        mController.score();
    }

    void onBtnPass() {
        mController.pass();
    }
    void onBtnMenu() {
        showButtons(R.id.btnMenu);
    }
    void onBtnBackTalk() {
        mHideView = mShowView;
        mShowView = mTalkArea;
        switchButtonsAnimate();
        mCurButtonsView=mShowView;
    }
    void onUserTalk(MyMsg mm) {
        //showInfo(s);
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
        case 'i':
            return 8;
        case 'j':
            return 9;
        case 'k':
            return 10;
        case 'l':
            return 11;
        case 'm':
            return 12;
        case 'n':
            return 13;
        case 'o':
            return 14;
        case 'p':
            return 15;
        case 'q':
            return 16;
        case 'r':
            return 17;
        case 's':
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
        if(s.compareTo(".save")==0) {
            saveSgf();
        } else if(s.compareTo(".off")==0) {
            mController.shutdown();
        } else if(s.compareTo(".panic")==0) {
            s=null;
            s.trim();
        } else if(s.charAt(0)=='.') {
            char[] ss = s.toCharArray();
            if(ss[1]>='a' &&ss[1]<='t') {
                char x = ss[1];
                int count=0;
                if(ss.length>=3 && ss[2]>='1' && ss[2]<='9') count++;
                if(ss.length>=4 && ss[3]>='0' && ss[3]<='9') count++;
                if(count>0) {
                    String v = new String(ss,2,count);
                    int y = Integer.parseInt(v);
                    if(y>=1 && y<=19 && isMyTurnToPutChess()==true)
                        mController.putChess(x,y);
                    if(mCursorTimer!=null) mCursorTimer.pause();
                }

            }

        } else {
            mController.sendTalk(s);
            showInfo("*"+mController.getMyName()+"*:"+s);
        }
    }
    CursorTimer mCursorTimer = null;
    void saveSgf() {
    }

    class ButtonEventListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btnMenu:
                onBtnMenu();
                break;
            case R.id.btnSendTalk:
                onSendTalk();
                break;
            case R.id.btnBackTalk:
            case R.id.btnBackTalk2:
                onBtnBackTalk();
                break;
            case R.id.btnScore:
                onBtnScore();
                break;
            case R.id.btnDone:
                onBtnDone();
                break;
            case R.id.btnGiveUp:
            case R.id.btnGiveUp2:
                onBtnGiveUp();
                break;
            case R.id.btnPass:
                onBtnPass();
                break;

            case R.id.btnFullBoard:
                mBoardView.switchBoardMode(true);
                break;
            }

        }

    }

    void updateTimer() {

    }

    void showStepTimer() {
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
            }
        }
    }

    boolean isMyTurnToPutChess() {
        if(bInScore) return true;
        boolean b=mController.isMyTurnToPutChess();
        LogLog("isMyTurnToPutChess="+b);
        return b;
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

    void onBtnAgreeResult() {
    }
    void onBtnRejectResult() {
    }
    void onDianMuResult(MyMsg mm) {
    }
    void onBtnGiveUp() {
        mController.resign();
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
    }
    void LogLog(String s) {
        xHelper.log("igs",s);
    }
    void showScoreInfo(String s) {
        showInfoDialog(s);
    }
    void onConnectBroken() {
        LogLog("connect is broken, do silent reconnect");
        showToast(this.getString(R.string.silentreconn));
        mController.silentReconnect();
    }
    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    void onOppRestore() {
        showToast(getString(R.string.oppRestore));
    }
    void onOppLost() {
        showToast(getString(R.string.oppLost));
    }
    void onGetStored() {
        String s = mController.getStoredGame();
        if(s!=null)
            mController.restoreGame();
    }

    /*Restore game case:
    1. From loginview
    loginok->checkstored->found stored->look stored->prepare game->parse status->fill game->
    */


    void onSilentLoginOK() {
        LogLog("Silent Login OK");
        showToast(this.getString(R.string.silentconnok));
        //try to restore the game
        if(mViewMode==1) {
            mController.checkStored();
        }
    }
    class GoHandler extends Handler {
        public static final int MSG_UPDATE_USER = 0x1001;
        public static final int MSG_UPDATE_GAME = 0x1002;
        public static final int MSG_UPDATE_TIMER = 0x1003;
        public static final int MSG_UPDATE_CURSOR = 0x1004;

        @Override
        public void handleMessage(android.os.Message msg) {
            LogLog("IgsGoView received message="+msg.what);
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
                break;
            }
            case MSG_UPDATE_TIMER: {
                updateTimer();
                break;
            }

            case IgsController.MSG_SCORE_INFO: {
                showScoreInfo(mController.mScoreStr);
                break;
            }
            case IgsController.MSG_IGS_CONN_BROKEN:
                onConnectBroken();
                break;
            case IgsController.MSG_IGS_OPP_LOST:
                onOppLost();
                break;
            case IgsController.MSG_IGS_OPP_RESTORE:
                onOppRestore();
                break;
            case IgsController.MSG_IGS_STORED:
                onGetStored();
                break;
            case IgsController.MSG_SILENT_LOGIN_OK:
                onSilentLoginOK();
                break;
            case IgsController.MSG_IGS_PASS: {
                onPass();
                break;
            }
            case IgsController.MSG_IGS_UNDO_SCORE: {
                onUndoScore();
                break;
            }
            case IgsController.MSG_IGS_STEP: {
                MyMsg mm = (MyMsg)(msg.obj);
                onStep(mm);
                break;
            }
            case IgsController.MSG_IGS_RESIGN: {
                Bundle mm = (Bundle)(msg.obj);
                onResign(mm);
                break;
            }
            case IgsController.MSG_IGS_SAY: {
                Bundle mm = (Bundle)(msg.obj);
                onSay(mm);
                break;
            }
            case IgsController.MSG_GAME_INFO: {
                updateUser();
                break;
            }
            case IgsController.MSG_IGS_SCORE_BEGIN: {
                scoreBegin();

                break;
            }
            case IgsController.MSG_GET_INVITE:
                showInviteDialog((Bundle)(msg.obj));
                break;
            case IgsController.MSG_GAME_START:
                onGameStart();
                break;
            case IgsController.MSG_IGS_REMOVE_DEAD:
                onRemoveDead();
                break;
            }

        }
    }
    void onGameStart() {
        mGame = mController.getCurrentGame();
        setTitle(getString(R.string.gameno)+" "+mGame.getGameNo());
        mViewMode=1;
    }


    void onUnDoScore() {
    }
    void acceptInvite(Bundle msg) {
        if(mGame!=null) {
            mController.unobserve();
        }
        mController.acceptInvite(msg);
    }
    void declineInvite(Bundle msg) {
        mController.declineInvite(msg);
    }

    boolean bInScore=false;
    void scoreBegin() {
        bInScore=true;
        showInfoDialog(getString(R.string.scoreinfo));
        mHideView = mShowView;
        mShowView = mScoreButtons;
        switchButtonsAnimate();
        mCurButtonsView=mShowView;
    }
    Bundle mInviteMsg=null;
    void showInviteDialog(Bundle msg) {
        mInviteMsg = msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String s = getString(R.string.getinvite)+"\n"+
                   msg.getString("name")+" ";
        LogLog("showInviteDialog:"+s);
        builder.setMessage(s)
        .setCancelable(true)
        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                acceptInvite(mInviteMsg);
            }
        })
        .setNegativeButton(getString(R.string.decline), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                declineInvite(mInviteMsg);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    void onResign(Bundle msg) {
        String name = msg.getString("name");
        name = "*"+name + "* "+getString(R.string.resignedgame);
        showResignDialog(name);
    }
    void onSay(Bundle msg) {
        String s = msg.getString("say");
        showInfo(s);
    }
    MediaPlayer mPlayer = null;
    int passNum=0;
    void onPass() {
        char c = mController.getCurColor();
        passNum++;
        if(passNum==3) return;
        if(c==mController.getMyColor()) {
            showInfo(getString(R.string.passinfo));
        }
    }
    void onRemoveDead() {
        mBoardView.invalidateEx(true);
    }
    void onUndoScore() {
        mBoardView.invalidateEx(true);
    }
    void onStep(MyMsg mm) {
        passNum=0;
        mCursorX=-1;
        mCursorY=-1;
        mLastStep.x=mm.x;
        mLastStep.y=mm.y;
        mBoardView.hideFocus();
        mBoardView.invalidateEx(true);
        AudioManager audioManager = (AudioManager)(this.getSystemService(Context.AUDIO_SERVICE));
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL && app.getSilentMode()==false) {
            if(mPlayer == null) {
                mPlayer=MediaPlayer.create(this, R.raw.putchess);
            }
            //float vol=(float)(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            //mPlayer.setVolume(vol,vol);
            mPlayer.start();
        }
    }
    void onUserPass() {

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
        //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        //Now Set your animation
        Animation fadeInAnimation=null;
        fadeInAnimation =  AnimationHelper.outToBottomAnimation(200);
        fadeInAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mHideView.setVisibility(View.GONE);
                if(mShowView!=null)
                    showButtonsAnimate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
        if(mHideView!=null)
            mHideView.startAnimation(fadeInAnimation );
    }

    public void hideButtonsAnimate() {
        //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        //Now Set your animation
        Animation fadeInAnimation=null;
        fadeInAnimation =  AnimationHelper.outToBottomAnimation(200);
        fadeInAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mHideView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
        if(mHideView!=null)
            mHideView.startAnimation(fadeInAnimation );
    }
    private static final int CMD_PASS  = 1;
    private static final int CMD_DIANMU  = 2;
    private static final int CMD_GIVEUP = 3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        item = menu.add(0, CMD_PASS, 0, R.string.pass);
        //item.setIcon(R.drawable.clear_history);
        item = menu.add(0, CMD_DIANMU, 0, R.string.dianmu);
        item = menu.add(0, CMD_GIVEUP, 0, R.string.giveup);
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

    void showNotification(String str) {

        NotificationManager mNotiMan=null;
        mNotiMan=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new Notification(R.drawable.icon,str,System.currentTimeMillis());
        notification.defaults=Notification.DEFAULT_ALL;
        if(app.getSilentMode()==true) notification.defaults &= ~(Notification.DEFAULT_SOUND);
        notification.flags=Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, main.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pt=PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this,"",str,pt);
        mNotiMan.notify(192345, notification);

    }

    void onGameEnd(MyMsg mm) {
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
                    mBoardView.reCalFocus();
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
                    if(bShowBigView==true) {
                        if(mBoardView.mFullBoard==true) {
                            if(isMyTurnToPutChess()==true) {
                                if(mBoardView.mFullBoard==true) {
                                    mBoardView.switchBoardMode(false);
                                }
                            }
                        } else if(mBoardView.mFullBoard==false) {
                            if(isMyTurnToPutChess()==true) {
                                mBoardView.putChess();
                                mBoardView.switchBoardMode(true);
                            }
                        }
                    } else {
                        if(isMyTurnToPutChess()==true )
                            mBoardView.putChess();
                    }
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
        Bitmap mBig=null;
        Bitmap mSmall=null;
        boolean visible=false;
    }

    class ChessBMP {
        Bitmap mBig=null;
        Bitmap mSmall=null;
        Bitmap mMu=null;
    }

    class BoardViewGo extends ImageView {

        private android.graphics.Bitmap mMemBmp=null;
        private android.graphics.Bitmap mBigBoardBmp;
        private android.graphics.Bitmap mSmallBoardBmp;
        private FocusWidget mFocusWidget = null;

        private ChessBMP mBlackBmp = null;
        private ChessBMP mWhiteBmp = null;
        final int BOARD_OFFSET_320 = 16;
        final int BOARD_STEP_320 = 16;
        public int mTopX=BOARD_OFFSET_320;
        public int mTopY=BOARD_OFFSET_320;
        public int mStepW=BOARD_STEP_320;



        public int mScaleRate = 2;
        private boolean IsQiPanDrawed=false;
        private float mScare=1;
        private int mCanvasWidth;
        private int mCanvasHeight;
        private boolean mFullBoard = true;


        public int mBigViewX=0;
        public int mBigViewY=0;

        public int mFocusX=0;
        public int mFocusY=0;

        public boolean mDianMuMode=false;

        public BoardViewGo(Context context) {
            super(context);
            InputEventListener listener = new InputEventListener();
            this.setOnLongClickListener(listener);
            this.setOnClickListener(listener);
            this.setOnKeyListener(listener);
            this.setOnTouchListener(listener);



        }

        @Override
        protected void onDraw(Canvas canvas) {
            paint(canvas);
        }
        int mBlinkCursor=0;
        void updateCursor() {
            mBlinkCursor++;
            if(mBlinkCursor==4) mBlinkCursor=0;
            invalidateEx(false);
        }

        void ptol(float x,float y) {
            mCursorX=getLX(x,mTopX,mStepW);
            mCursorY=getLY(y,mTopY,mStepW);
        }
        void drawCursor(Canvas canvas) {
            if(mCursorX==-1) return;
            float px,py;
            int lx,ly;
            lx=mCursorX;
            ly=mCursorY;
            px = getPX(lx,mTopX,mStepW);
            py = getPY(ly,mTopY,mStepW);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            float w = mStepW/(float)1.5;
            w=(w/4)*(mBlinkCursor+1);
            float x1=px-w+1;
            float x2=px+w;
            float y1=py+(float)0.5;
            float y2=y1;
            canvas.drawLine(x1, y1, x2, y2, paint);
            x1=px+(float)0.5;
            x2=x1;
            y1=py-w+1;
            y2=py+w;
            canvas.drawLine(x1, y1, x2, y2, paint);
        }




        android.graphics.Bitmap CreateMemBmp(int w,int h) {

            Bitmap memBmp = android.graphics.Bitmap.createBitmap(w, h, android.graphics.Bitmap.Config.ARGB_4444);
            return memBmp;
        }

        void putChess() {
            int x,y;
            x=mFocusX;
            y=18-mFocusY;
            char c = xToC(x);
            if(bInScore==false) mController.putChess(c,y+1);
            else mController.removeDead(c,y+1);
            mCursorX=-1;
            mCursorY=-1;

        }

        Bitmap drawChesses() {
            Bitmap memBmp = null;
            if(mGame!=null) mGoLogic = mGame.getGoLogic();
            if(mGoLogic==null) {
                LogLog("!!! mGoLogic is null during drawChesses");
                return null;
            }

            if(mFullBoard==true) {
                if(xBoardBmp.mSmallChessesBmp==null) {
                    //   xBoardBmp.mSmallChessesBmp = CreateMemBmp(mCanvasWidth,mCanvasHeight);
                }
                //memBmp = xBoardBmp.mSmallChessesBmp;
                memBmp = CreateMemBmp(mCanvasWidth,mCanvasHeight);;
            } else {
                if(xBoardBmp.mBigChessesBmp==null) {
                    //xBoardBmp.mBigChessesBmp = CreateMemBmp(mCanvasWidth*mScaleRate,mCanvasHeight*mScaleRate);
                }
                memBmp = CreateMemBmp(mCanvasWidth*mScaleRate,mCanvasHeight*mScaleRate);
            }
            Canvas canvasMem = new Canvas();
            canvasMem.setBitmap(memBmp);
            /*
            Paint eraseP = new Paint(Color.TRANSPARENT);
            eraseP.setStyle(Paint.Style.FILL_AND_STROKE);

            canvasMem.drawPaint(eraseP);
            */
            for (int x=0 ; x<mBoardSize ; x++) {
                for(int y=0 ; y<mBoardSize; y++) {
                    float px,py;
                    Bitmap bmp=null;
                    if(mGoLogic.getSide(x, y)==1) {
                        if(mFullBoard==true) {
                            bmp=mBlackBmp.mSmall;
                        } else {
                            bmp=mBlackBmp.mSmall;
                        }

                    } else if(mGoLogic.getSide(x, y)==2) {
                        if(mFullBoard==true) {
                            bmp=mWhiteBmp.mSmall;
                        } else {
                            bmp=mWhiteBmp.mSmall;
                        }
                    } else if(mDianMuMode==true) {
                        if(mDianMuResult.getSide(x, y)==1) {
                            bmp=mBlackBmp.mMu;
                        } else if(mDianMuResult.getSide(x, y)==2) {
                            bmp=mWhiteBmp.mMu;
                        }
                    }
                    if(bmp==null) continue;

                    if(mFullBoard==true) {
                        px = getPX(x,mTopX,mStepW);

                        py = getPY(18-y,mTopY,mStepW);
                        Rect src = new Rect();
                        Rect dst = new Rect();
                        dst.top = (int)(py-(mStepW)/2);
                        dst.left = (int)(px-(mStepW)/2);
                        dst.right = (int)(px+mStepW/2+1);
                        dst.bottom = (int)(py+mStepW/2+1);

                        src.top=0;
                        src.left=0;
                        src.right=bmp.getWidth();
                        src.bottom=bmp.getHeight();
                        /*
                        	   canvasMem.drawBitmap(bmp.mSmall ,
                         			   px-(mStepW)/2,
                         			   py-(mStepW)/2,null) ;
                         			   */
                        canvasMem.drawBitmap(bmp, src, dst,null);
                        if(mDianMuMode==true) {

                        }
                        if(x==mLastStep.x && y==mLastStep.y) {
                            Paint paint = new Paint();
                            paint.setColor(Color.RED);
                            canvasMem.drawRect(px-3, py-3, px+4, py+4, paint);
                        }
                    } else {
                        px = getPX(x,mTopX*mScaleRate,mStepW*mScaleRate);
                        py = getPY(18-y,mTopY*mScaleRate,mStepW*mScaleRate);
                        /*
                        canvasMem.drawBitmap(bmp.mBig ,
                        	   px-(mStepW*mScaleRate)/2,
                        	   py-(mStepW*mScaleRate)/2,null) ;
                        	   */
                        Rect src = new Rect();
                        Rect dst = new Rect();
                        dst.top = (int)(py-(mStepW*mScaleRate)/2);
                        dst.left = (int)(px-(mStepW*mScaleRate)/2);
                        dst.right = (int)(px+(mStepW*mScaleRate)/2+1);
                        dst.bottom = (int)(py+(mStepW*mScaleRate)/2+1);

                        src.top=0;
                        src.left=0;
                        src.right=bmp.getWidth();
                        src.bottom=bmp.getHeight();
                        canvasMem.drawBitmap(bmp, src, dst,null);
                        if(x==mLastStep.x && y==mLastStep.y) {
                            Paint paint = new Paint();
                            paint.setColor(Color.RED);
                            canvasMem.drawRect(px-6, py-6, px+7, py+7, paint);
                        }
                    }
                }
            }
            return memBmp;
        }

        Bitmap mChessMem=null;

        void paint(Canvas canvas) {
            mCanvasWidth=canvas.getWidth();
            mCanvasHeight=canvas.getHeight();
            mStepW=mCanvasWidth/(xSystem.mGameSize+1);
            mTopX=mStepW;
            mTopY=mStepW;
            drawBoardMem(mBoardSize,mTopX,mTopY,mStepW);

            if(mBlackBmp==null) {
                mBlackBmp = new ChessBMP();
                mWhiteBmp = new ChessBMP();
                mBlackBmp.mBig=BitmapFactory.decodeResource(
                                   getResources(), R.drawable.b32) ;
                mBlackBmp.mSmall=BitmapFactory.decodeResource(
                                     getResources(), R.drawable.b16) ;

                mWhiteBmp.mBig=BitmapFactory.decodeResource(
                                   getResources(), R.drawable.w32) ;
                mWhiteBmp.mSmall=BitmapFactory.decodeResource(
                                     getResources(), R.drawable.w16) ;
                mBlackBmp.mMu=BitmapFactory.decodeResource(
                                  getResources(), R.drawable.bmu16) ;
                mWhiteBmp.mMu=BitmapFactory.decodeResource(
                                  getResources(), R.drawable.wmu16) ;
            }



            if(mFocusWidget==null) {
                mFocusWidget = new FocusWidget();
                mFocusWidget.mBig = BitmapFactory.decodeResource(getResources(), R.drawable.focus32) ;
                mFocusWidget.mSmall = BitmapFactory.decodeResource(getResources(), R.drawable.focus18) ;
            }

            if(mDianMuMode==true) {
                mFullBoard=true;
            }
            Bitmap boardBmp=mBigBoardBmp;
            if(mDirty==true || mChessMem==null)
                mChessMem = drawChesses();


            if(mFullBoard==false) {
                boardBmp=mBigBoardBmp;
                xHelper.log("xGoActivity","BigViewX="+mBigViewX+"BigViewY="+mBigViewY);
                //canvas.drawBitmap(boardBmp ,mBigViewX,mBigViewY,  null) ;

                Rect src = new Rect();
                Rect dst = new Rect();
                src.top = mBigViewY;
                src.left = mBigViewX;
                src.right = mBigViewX+mCanvasWidth;
                src.bottom = mBigViewY+mCanvasWidth;

                dst.top=0;
                dst.left=0;
                dst.right=mCanvasWidth;
                dst.bottom=mCanvasWidth;
                canvas.drawBitmap(boardBmp, src, dst, null);
                if(mChessMem!=null) canvas.drawBitmap(mChessMem, src, dst, null);

                if(mFocusWidget.visible==true) {
                    float px = getPX(mFocusX,mTopX*mScaleRate,mStepW*mScaleRate);
                    float py = getPY(mFocusY,mTopY*mScaleRate,mStepW*mScaleRate);


                    Rect src1 = new Rect();
                    Rect dst1 = new Rect();
                    dst1.top = (int)(py-(mStepW*mScaleRate)/2-mBigViewY);
                    dst1.left = (int)(px-(mStepW*mScaleRate)/2-mBigViewX);
                    dst1.right = (int)(px+(mStepW*mScaleRate)/2+1-mBigViewX);
                    dst1.bottom = (int)(py+(mStepW*mScaleRate)/2+1-mBigViewY);

                    src1.top=0;
                    src1.left=0;
                    src1.right=mFocusWidget.mSmall.getWidth();
                    src1.bottom=mFocusWidget.mSmall.getHeight();

                    canvas.drawBitmap(mFocusWidget.mSmall, src1, dst1,null);

                }
            } else {
                boardBmp=mSmallBoardBmp;

                canvas.drawBitmap(boardBmp ,0,0,  null) ;
                if(mChessMem!=null) canvas.drawBitmap(mChessMem, 0, 0, null);
                if(mFocusWidget.visible==true) {
                    float px = getPX(mFocusX,mTopX,mStepW);
                    float py = getPY(mFocusY,mTopY,mStepW);
                    Rect src1 = new Rect();
                    Rect dst1 = new Rect();
                    dst1.top = (int)(py-(mStepW)/2-2);
                    dst1.left = (int)(px-(mStepW)/2-2);
                    dst1.right = (int)(px+mStepW/2+3);
                    dst1.bottom = (int)(py+mStepW/2+3);

                    src1.top=0;
                    src1.left=0;
                    src1.right=mFocusWidget.mSmall.getWidth();
                    src1.bottom=mFocusWidget.mSmall.getHeight();
                    canvas.drawBitmap(mFocusWidget.mSmall, src1, dst1,null);
                }
            }
            if(isMyTurnToPutChess()==true) {
                if(bCursorMode==true) drawCursor(canvas);
            }

        }

        void scrollView(float dx,float dy) {
            mBigViewX-=dx;
            mBigViewY-=dy;

            if(mBigViewX<=0) mBigViewX=0;
            if(mBigViewY<=0) mBigViewY=0;

            if(mBigViewX+mCanvasWidth>=mCanvasWidth*mScaleRate)
                mBigViewX=mCanvasWidth*(mScaleRate-1);
            if(mBigViewY+mCanvasWidth>=mCanvasWidth*mScaleRate)
                mBigViewY=mCanvasWidth*(mScaleRate-1);

            this.invalidateEx(true);
        }
        boolean mDirty = false;
        void invalidateEx(boolean dirty) {
            mDirty=dirty;
            this.invalidate();
        }
        void showFocus() {
            if(mFocusWidget!=null) mFocusWidget.visible = true;
            this.invalidateEx(true);
        }
        void hideFocus() {
            if(mFocusWidget!=null) mFocusWidget.visible = false;
            this.invalidateEx(true);
        }


        void reCalFocus() {
            if(mFullBoard==true) {
                int lx = getLX(mScreenX,mTopX,mStepW);
                int ly = getLY(mScreenY,mTopY,mStepW);
                mFocusX=lx;
                mFocusY=ly;
            } else {
                int lx = getLX(mScreenX+mBigViewX,mTopX*mScaleRate,mStepW*mScaleRate);
                int ly = getLY(mScreenY+mBigViewY,mTopY*mScaleRate,mStepW*mScaleRate);
                mFocusX=lx;
                mFocusY=ly;
            }
        }

        void switchBoardMode(boolean mode) {
            if(this.mFullBoard!=mode) {
                this.mFullBoard = mode;

                if(mode==false) {
                    xHelper.log("GoActivity","lx="+mFocusX+"ly="+mFocusY);

                    float px_old = getPX(mFocusX,mTopX,mStepW);
                    float py_old = getPY(mFocusY,mTopY,mStepW);

                    float px = getPX(mFocusX,mTopX*mScaleRate,mStepW*mScaleRate);
                    float py = getPY(mFocusY,mTopY*mScaleRate,mStepW*mScaleRate);

                    float x=px-px_old;
                    float y=py-py_old;
                    if(x<=0) x=0;
                    if(y<=0) y=0;
                    mBigViewX=(int)x;
                    mBigViewY=(int)y;

                    // draw focus
                    if(isMyTurnToPutChess()==true) showFocus();
                    else hideFocus();

                    mHideView=mCurButtonsView;
                    mShowView=mBigViewButtons;
                    switchButtonsAnimate();

                } else {
                    mHideView=mBigViewButtons;
                    mShowView=mCurButtonsView;
                    switchButtonsAnimate();
                }
                mBoardView.invalidateEx(true);


            }


        }

        Bitmap scaleBmp(Bitmap src,int rate) {
            if(rate!=1) {
                int bmpW = src.getWidth();
                int bmpH = src.getHeight();
                Matrix matrix=new Matrix();
                matrix.postScale(rate, rate);
                Bitmap resizeBmp=Bitmap.createBitmap(src, 0, 0, bmpW, bmpH, matrix, true);
                return resizeBmp;
            }
            return src;

        }

        void drawBoardMem(int size,int lx,int ly,float step) {
            if(xBoardBmp.IsQiPanDrawed==true &&
                    xBoardBmp.mBoardSize==mBoardSize) {

                mBigBoardBmp=xBoardBmp.mBigBoardBmp;
                mSmallBoardBmp=xBoardBmp.mSmallBoardBmp;
            } else {
                int w=mCanvasWidth;
                xBoardBmp.mSmallBoardBmp=Bitmap.createBitmap(w, w, android.graphics.Bitmap.Config.ARGB_4444);
                w*=mScaleRate;

                xBoardBmp.mBigBoardBmp=Bitmap.createBitmap(w, w, android.graphics.Bitmap.Config.ARGB_4444);
                mBigBoardBmp=xBoardBmp.mBigBoardBmp;
                mSmallBoardBmp=xBoardBmp.mSmallBoardBmp;
                drawQiPanEx(mSmallBoardBmp,mBoardSize,mTopX,mTopY,mStepW);
                drawQiPanEx(mBigBoardBmp,mBoardSize,mTopX*mScaleRate,mTopY*mScaleRate,mStepW*mScaleRate);
            }
        }
        void drawQiPanEx(Bitmap memBmp,int size,int lx,int ly,float step) {

            xBoardBmp.IsQiPanDrawed = true;
            xBoardBmp.mBoardSize=size;
            Canvas canvas = new Canvas();
            canvas.setBitmap(memBmp);

            Paint paint = new Paint();
            paint.setColor(LINE_COLOR);
            paint.setColor(Color.BLACK);
            paint.setTextSize(12);
            paint.setAntiAlias(true);

            Paint paintDot = new Paint();
            paintDot.setColor(Color.BLACK);

            int width = (int)(step*(size-1));
            int startX,startY,stopX,stopY;

            ly-=2;

            startX=lx;
            startY=ly;
            stopX=lx+width;
            stopY=ly;
            Bitmap h_outline= BitmapFactory.decodeResource(getResources(), R.drawable.houtline) ;
            Bitmap v_outline= BitmapFactory.decodeResource(getResources(), R.drawable.voutline) ;

            Bitmap h_bmp= BitmapFactory.decodeResource(getResources(), R.drawable.h_bl) ;
            Bitmap v_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.v_bl) ;
            Bitmap dot_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.dot) ;
            //draw horizon line
            for(int i=0 ; i<size; i++) {
                //if(i==0 || i==size-1)
                //	canvas.drawLine(startX, startY, stopX, stopY, paintDot);
                //else
                //canvas.drawLine(startX, startY, stopX, stopY, paint);
                int x1=startX;
                int x2=stopX;
                while(x1<x2) {
                    if(i==0 || i==size-1)
                        canvas.drawBitmap(h_outline, x1, startY+1, null);
                    else
                        canvas.drawBitmap(h_bmp, x1, startY+2, null);
                    x1++;
                }
                String tag = new Integer(19-i).toString();
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(tag,startX-4,startY+6,paint);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(tag,stopX+4,startY+6,paint);
                startY+=step;
                stopY+=step;

            }


            //draw ver line
            startX=lx-2;
            startY=ly+2;
            stopX=lx;
            stopY=ly+width+2;
            for(int i=0 ; i<size; i++) {
                //if(i==0 || i==size-1)
                //	canvas.drawLine(startX, startY, stopX, stopY, paintDot);
                //else
                //canvas.drawLine(startX, startY, stopX, stopY, paint);
                int y1=startY;
                int y2=stopY;
                if(i==0 || i==size-1)
                    y2=stopY+2;
                if(i==size-1)
                    startX++;
                while(y1<y2) {
                    if(i==0 || i==size-1) {
                        canvas.drawBitmap(v_outline, startX+1, y1-1, null);
                    } else
                        canvas.drawBitmap(v_bmp, startX+2, y1, null);
                    y1++;
                }
                int xi = i;
                if(xi>7) xi++;
                String tag = new String(Character.toChars(65+xi));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(tag,startX+2,startY-6,paint);
                canvas.drawText(tag,startX+2,stopY+16,paint);
                startX+=step;
                stopX+=step;

            }
            //draw dots

            if(xSystem.mGameSize==19) {
                startX=(int)(lx+3*step);
                startY=(int)(ly+3*step);
                float offset=(float)2;

                float offsety=(float)0;
                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);

                startX=(int)(lx+3*step);
                startY=(int)(ly+9*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);

                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);

                startX=(int)(lx+9*step);
                startY=(int)(ly+3*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);
                startX=(int)(lx+9*step);
                startY=(int)(ly+9*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);
                startX=(int)(lx+3*step);
                startY=(int)(ly+15*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);
                startX=(int)(lx+9*step);
                startY=(int)(ly+15*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);
                startX=(int)(lx+15*step);
                startY=(int)(ly+3*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);
                startX=(int)(lx+15*step);
                startY=(int)(ly+9*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);
                startX=(int)(lx+15*step);
                startY=(int)(ly+15*step);

                //canvas.drawRect(startX-offset, startY-offset, startX+offset+1,startY+offset+1, paintDot);
                canvas.drawBitmap(dot_bmp, startX-offset, startY-offsety, null);
            }
        }

        int getLX(float x,int topx,int stepW) {
            x=x-topx;
            int index=(int)(x/stepW);
            int offset=(int)(x % stepW);

            if(offset>(stepW/2)) {
                index++;
            }

            if(index==mBoardSize) {
                index--;
            }
            if(index>mBoardSize) {
                return -1;
            }
            return index;
        }
        int getLY(float y,int topy,int stepW) {
            y=y-topy;
            int index=(int)(y/stepW);
            int offset=(int)(y % stepW);

            if(offset>(stepW/2)) {
                index++;
            }

            if(index==mBoardSize) {
                index--;
            }
            if(index>mBoardSize) {
                return -1;
            }
            return index;
        }

        float getPX(int x,int topx,int stepw) {
            float px;
            px=(x)*stepw+topx;
            return px;
        }
        float getPY(int y,int topy,int stepw) {
            float py;
            py=(y)*stepw+topy;
            return py;
        }
    }

}
