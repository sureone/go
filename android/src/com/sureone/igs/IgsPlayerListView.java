
package com.sureone.igs;
import java.io.UnsupportedEncodingException;
import android.view.Window;
import com.sureone.xHelper;
import android.view.WindowManager;
import android.widget.Toast;
import com.sureone.R;
import com.sureone.GoApp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class IgsPlayerListView extends Activity {
    AppsAdapter mAppsAdapter;
    ListView mGrid;
    IgsController mController=null;
    Button mBtnRank1= null;
    Button mBtnRank2= null;
    Button mBtnRank3= null;
    Button mBtnRank4= null;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        mController.setViewHandler(mHandler);
        super.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    GoApp mApp=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp =  GoApp.getInstance();
        mController = mApp.getIgsController();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.igsplayerlistview);
        mGrid = (ListView) findViewById(R.id.gameList);
        mAppsAdapter = new AppsAdapter(this);
        mGrid.setAdapter(mAppsAdapter);
        mGrid.setOnItemClickListener(mAppsAdapter);
        mGrid.setOnItemLongClickListener(mAppsAdapter);
        mGrid.setOnScrollListener(mAppsAdapter);
        mGrid.setCacheColorHint(0);
        mHandler = new MyHandler();
        evtLis = new EvtListener();
        mController.setViewHandler(mHandler);
        mBtnRank1 = (Button)findViewById(R.id.btnRank1);
        mBtnRank1.setOnClickListener(evtLis);
        mBtnRank2 = (Button)findViewById(R.id.btnRank2);
        mBtnRank2.setOnClickListener(evtLis);
        mBtnRank3 = (Button)findViewById(R.id.btnRank3);
        mBtnRank3.setOnClickListener(evtLis);
        mBtnRank4 = (Button)findViewById(R.id.btnRank4);
        mBtnRank4.setOnClickListener(evtLis);
        mContextMenuListener = new ContextMenuListener();
	/*
        mHandler.postDelayed(new Runnable() {
            public void run() {
                reloadPlayerList();
            }
        },500);
	*/
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(bOnAutoPair==false && mController.isLoading()==true) this.showWaitingDialog(R.string.waitingforplayerlist);
    }

    void LogLog(String s) {
        xHelper.log("igs","IgsPlayerListView: "+s);
    }
    int oldBtn = -1;
    int curBtn = R.id.btnRank1;
    int btnColor=0;

    void startDetail(int index) {
        Intent intent= new Intent(this,com.sureone.igs.IgsPlayerDetailView.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }
    void loadOnBtn(int id) {
        Button btn;
        if(oldBtn!=-1) {
            btn = (Button)findViewById(oldBtn);
            btn.setTextColor(btnColor);
            LogLog("change old btn to black"+oldBtn);
        }
        curBtn = id;
        oldBtn=curBtn;
        btn = (Button)findViewById(id);
        btnColor = btn.getTextColors().getDefaultColor();
        btn.setTextColor(Color.RED);
        if(bOnAutoPair==false) this.showWaitingDialog(R.string.waitingforplayerlist);
        LogLog("change new btn to new"+id);

        switch(curBtn) {
        case R.id.btnRank1:
            mController.listPlayers("1d-9d o");
            break;
        case R.id.btnRank2:
            mController.listPlayers("1k-10k o");
            break;
        case R.id.btnRank3:
            mController.listPlayers("11k-30k o");
            break;
        }
    }
    void reloadPlayerList() {
        loadOnBtn(curBtn);
        mAppsAdapter.notifyDataSetChanged();
    }
    void onLoadPlayerListDone() {
        mAppsAdapter.notifyDataSetChanged();
        int cnt = mController.getNumberOfPlayers();
        getParent().getWindow().setTitle(getString(R.string.playernum)+": " +cnt);

        if(bOnAutoPair==true) {
            bOnAutoPair=false;
            startAutoPair();
        } else
            this.closeWaitingDialog();
    }
    void onObserveOK() {
        this.closeWaitingDialog();
        Intent goview= new Intent(this,com.sureone.igs.IgsGoView.class);
        startActivity(goview);
    }
    void onGameStart() {
        stopPair();
        Intent intent = new Intent(this,com.sureone.igs.IgsGoView.class);
        intent.putExtra("viewMode",1);
        startActivity(intent);
    }
    void observeGame() {
    }
    void acceptInvite(Bundle msg) {
        mController.acceptInvite(msg);
    }
    void declineInvite(Bundle msg) {
        mController.declineInvite(msg);
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
    void onConnectBroken() {
        LogLog("connect is broken, do silent reconnect");
        mController.silentReconnect();
    }
    void onSilentLoginOK() {
        LogLog("Silent Login OK");
        reloadPlayerList();
    }
    ContextMenuListener mContextMenuListener=null;
    static final int MSG_PAIR_NEXT=2001;
    private MyHandler mHandler;
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case IgsController.MSG_LIST_PLAYER_DONE:
                onLoadPlayerListDone();
                break;
            case IgsController.MSG_GAME_START:
                if(mAutoPairTimer!=null) mAutoPairTimer.pause();
                onGameStart();
                break;
            case IgsController.MSG_IGS_CONN_BROKEN:
                onConnectBroken();
                break;
            case IgsController.MSG_GET_INVITE:
                stopPair();
                showInviteDialog((Bundle)(msg.obj));
                break;
            case IgsController.MSG_SILENT_LOGIN_OK:
                onSilentLoginOK();
                break;
            case MSG_PAIR_NEXT:
                onPairNext();
                break;
            }
        }
    }
    ProgressDialog showWaitingDialog(int id) {
        ProgressDialog alert = new ProgressDialog(this);
        mWaitingDialog=alert;
        alert.setMessage(getString(id));
        alert.setCancelable(true);
        alert.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        alert.show();
        return alert;
    }
    ProgressDialog mWaitingDialog=null;
    void closeWaitingDialog() {
        if(mWaitingDialog!=null) {
            xHelper.log("goapp","closeWaitingDialog");
            mWaitingDialog.dismiss();
            mWaitingDialog=null;
        }
    }
    class EvtListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btnRank1:
            case R.id.btnRank2:
            case R.id.btnRank3:
                loadOnBtn(v.getId());
                break;
            case R.id.btnRank4:
                doAutoPair();
                break;
            }
        }
    }

    boolean bOnAutoPair=false;
    AutoPairTimer mAutoPairTimer=null;
    void startAutoPair() {
        if(mAutoPairTimer == null) {
            mAutoPairTimer = new AutoPairTimer();
            mAutoPairTimer.setInterval(10*1000);
            mAutoPairTimer.resume();
            new Thread(mAutoPairTimer, "Auto Pair Timer").start();
        } else {
            mAutoPairTimer.resume();
        }
    }

    int pairIndex=0;
    void onPairNext() {
        pairNext();
    }

    void stopPair() {
        if(mAutoPairTimer!=null)	mAutoPairTimer.pause();
        int cnt = mController.getNumberOfPlayers();
        getParent().getWindow().setTitle(getString(R.string.playernum)+": " +cnt);
    }
    void pairNext() {
        IgsPlayer p = mController.getAutoPairPlayer(pairIndex);
        if(p!=null) {
            LogLog("auto pair with "+p.name+":"+p.rank);
            if(p.name.equals(mController.getMyName())==false) {
                mController.match(p.name);
                getParent().getWindow().setTitle("Inviting "+p.name+" ["+p.rank+"] ...");
            }
        } else {
            stopPair();
        }
        pairIndex++;
    }
    void showAutoPairDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s)
        .setCancelable(true)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                autoPair();
            }
        });
        AlertDialog mAutoPairDialog = builder.create();
        mAutoPairDialog.show();
    }
    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    void doAutoPair() {
        showToast(getString(R.string.autopairinfo));
        autoPair();
    }

    void autoPair() {
        LogLog("autoPair: my rank="+mController.getMyRank());
        int rr = IgsPlayer.getRankNo(mController.getMyRank());
        if(rr!=0) bOnAutoPair=true;
        if(rr>=1 && rr<=9)
            loadOnBtn(R.id.btnRank1);
        else if(rr>=10 && rr<=19)
            loadOnBtn(R.id.btnRank2);
        else if(rr>=20 && rr<=29)
            loadOnBtn(R.id.btnRank3);
    }

    private class AutoPairTimer implements Runnable {
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
                if(!mStopped && mPause==false) {
                    Message message = new Message();
                    message.what = MSG_PAIR_NEXT;
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


    void onObserveGame(IgsGameItem game) {
        if(game!=null) {
            this.showWaitingDialog(R.string.waitingforgetsgf);
            mController.observeGame(game.id);
        }
        /*
        Intent mGameView=null;
        if(mGameView==null)
        	mGameView = new Intent(this,com.sureone.igs.IgsGameView.class);
        startActivity(mGameView);
        */
    }



    EvtListener evtLis=null;
    class ContextMenuListener implements View.OnCreateContextMenuListener {

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenuInfo menuInfo) {
            //onDeleteThread(mSelectedThreadId);
        }

    }
    public class AppsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,OnScrollListener {
        private LayoutInflater mInflater;
        public AppsAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
        }



        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.igsplayeritem, null);
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.txtContent=(TextView) convertView.findViewById(R.id.txtContent);
                holder.txtStatus=(TextView) convertView.findViewById(R.id.txtStatus);
                holder.txtFlag=(TextView) convertView.findViewById(R.id.txtFlag);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtContent.setTextSize(18);
            IgsPlayer player = mController.getPlayerByIndex(position);
            if(player==null) return convertView;
            holder.player = player;
            String s = "["+player.rank+"] "+ player.name;
            holder.txtContent.setText(s);
            if(player.obs_id!=-1) {
                holder.txtStatus.setText(getString(R.string.observinggame)+": "+player.obs_id);
            } else if(player.game_id!=-1) {
                holder.txtStatus.setText(getString(R.string.playinggame)+": "+player.game_id);
            } else {
                holder.txtStatus.setText(getString(R.string.idle)+": "+player.idle);
            }
            holder.txtFlag.setText("");
            /*
            if(player.flags!=null){
            	if(player.isAcceptPlay()==false)
            		holder.txtFlag.setText("X");
            	if(player.isLookingOn()==true)
            		holder.txtFlag.setText("!");
            }
            */
            return convertView;

        }



        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ViewHolder holder = (ViewHolder) v.getTag();
            startDetail(position);
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ViewHolder holder = (ViewHolder) view.getTag();
            return true;
        }

        public final int getCount() {
            int cnt = mController.getNumberOfPlayers();
            return cnt;
        }

        public final Object getItem(int position) {
            return null;
        }

        public final long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            TextView txtContent;
            TextView txtStatus;
            TextView txtFlag;
            IgsPlayer player=null;
            int id;
        }


        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            /*
            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount-1;
            xHelper.log("goapp","onScroll");
            if(loadMore==true && totalItemCount!=0 && mWaitingDialog==null && mReallyEnd==false){
            	LoadMoreMainThreads();
            	xHelper.log("goapp","scroll to end");
            }
            */
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub
        }
    }
}
