
package com.sureone.igs;
import java.io.UnsupportedEncodingException;
import com.sureone.xHelper;
import android.view.WindowManager;
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

public class IgsGameListView extends Activity {
    AppsAdapter mAppsAdapter;
    ListView mGrid;
    IgsController mController=null;
    Button mBtnPost= null;
    EditText mTxtPost = null;
    void LogLog(String s) {
        xHelper.log("igs","IgsGameListView:"+s);
    }
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
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mApp =  GoApp.getInstance();
        mController = mApp.getIgsController();
        setContentView(R.layout.igsgamelistview);
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
        mBtnPost = (Button)findViewById(R.id.btnPost);
        mBtnPost.setOnClickListener(evtLis);
        mTxtPost = (EditText)findViewById(R.id.txtPost);
        mContextMenuListener = new ContextMenuListener();
        //reloadGameList();
        if(mController.isLoading()==true)
        	this.showWaitingDialog(R.string.waitingforgamelist);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    void reloadGameList() {
        this.showWaitingDialog(R.string.waitingforgamelist);
        mController.listGames();
        mAppsAdapter.notifyDataSetChanged();
    }
    void onLoadGameListDone() {
        mAppsAdapter.notifyDataSetChanged();
        int cnt = mController.getNumberOfGames();
        getParent().getWindow().setTitle(getString(R.string.totalgamenum)+": " +cnt);
        this.closeWaitingDialog();
    }
    void onObserveOK() {
        this.closeWaitingDialog();
        Intent goview= new Intent(this,com.sureone.igs.IgsGoView.class);
        startActivity(goview);
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
    void onGameStart() {
        Intent intent = new Intent(this,com.sureone.igs.IgsGoView.class);
        intent.putExtra("viewMode",1);
        startActivity(intent);
    }

    ContextMenuListener mContextMenuListener=null;
    private MyHandler mHandler;
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case IgsController.MSG_LIST_GAME_DONE:
                onLoadGameListDone();
                break;
            case IgsController.MSG_OBSERVE_OK:
                onObserveOK();
                break;

            case IgsController.MSG_GET_INVITE:
                showInviteDialog((Bundle)(msg.obj));
                break;
            case IgsController.MSG_GAME_START:
                onGameStart();
                break;
            case IgsController.MSG_STATUS_OK:
                break;
            }
        }
    }
    ProgressDialog showWaitingDialog(int id) {
        ProgressDialog alert = new ProgressDialog(this);
        mWaitingDialog=alert;
        alert.setMessage(getString(id));
        alert.setCancelable(false);
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
            case R.id.btnPost:
                onPost();
            }
        }
    }


    void onPost() {
        String s = mTxtPost.getText().toString().trim();
        if(s.length()>0) {
        }
    }
    void onObserveGame(IgsGameItem game) {
        if(game!=null) {
            this.showWaitingDialog(R.string.waitingforjoin);
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
                convertView = mInflater.inflate(R.layout.igsgameitem, null);
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.txtContent=(TextView) convertView.findViewById(R.id.txtContent);
                holder.txtGameNo=(TextView) convertView.findViewById(R.id.txtGameNo);
                holder.txtObservers=(TextView) convertView.findViewById(R.id.txtObservers);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtContent.setTextSize(18);
            IgsGameItem game = mController.getGameByIndex(position);
            holder.game = game;
            String s = "["+game.brk+"]"+game.bName+" "+getString(R.string.vs)+" "+
                       game.wName+"["+game.wrk+"]";
            holder.txtContent.setText(s);
            holder.txtGameNo.setText(getString(R.string.gameno)+" "+game.id);
            holder.txtObservers.setText(getString(R.string.observernum)+" "+game.obs);
            return convertView;

        }



        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ViewHolder holder = (ViewHolder) v.getTag();
            onObserveGame(holder.game);
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ViewHolder holder = (ViewHolder) view.getTag();
            return true;
        }

        public final int getCount() {
            int cnt = mController.getNumberOfGames();
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
            TextView txtGameNo;
            TextView txtObservers;
            IgsGameItem game=null;
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
