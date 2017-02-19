
package com.sureone.igs;
import java.io.UnsupportedEncodingException;
import com.sureone.xHelper;
import com.sureone.R;
import com.sureone.GoApp;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
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

public class IgsFriendListView extends Activity {
    AppsAdapter mAppsAdapter;
    ListView mGrid;
    IgsController mController=null;
    Button mBtnPost= null;
    EditText mTxtPost = null;
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

    GoApp app = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app =  GoApp.getInstance();
        mController = app.getIgsController();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.igsfriendlistview);
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
        //	reloadPlayerList();
    }

    void reloadPlayerList() {
        this.showWaitingDialog(R.string.waitingforgetsgf);
        mController.listPlayers("");
    }
    void onLoadPlayerListDone() {
        mAppsAdapter.notifyDataSetChanged();
        this.closeWaitingDialog();
    }
    void onObserveOK() {
        this.closeWaitingDialog();
        Intent goview= new Intent(this,com.sureone.igs.IgsGoView.class);
        startActivity(goview);
    }
    void observeGame() {
    }
    ContextMenuListener mContextMenuListener=null;
    private MyHandler mHandler;
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case IgsController.MSG_LIST_PLAYER_DONE:
                onLoadPlayerListDone();
                break;
            }
        }
    }
    AlertDialog showWaitingDialog(int id) {
        if(mWaitingDialog!=null) return mWaitingDialog;
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
        mWaitingDialog=alert;
        return alert;
    }

    AlertDialog mWaitingDialog=null;
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
            mController.sendCommand(s);
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
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtContent.setTextSize(18);
            IgsPlayer player = mController.getPlayerByIndex(position);
            holder.player = player;
            holder.txtContent.setText("["+player.rank+"]"+ player.name);
            if(player.obs_id!=-1) {
                holder.txtStatus.setText(getString(R.string.observinggame)+":"+player.obs_id);
            } else if(player.game_id!=-1) {
                holder.txtStatus.setText(getString(R.string.playinggame)+":"+player.game_id);
            } else {
                holder.txtStatus.setText(getString(R.string.idle)+":"+player.idle);
            }
            return convertView;

        }



        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ViewHolder holder = (ViewHolder) v.getTag();
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ViewHolder holder = (ViewHolder) view.getTag();
            return true;
        }

        public final int getCount() {
            int cnt = 0;//mController.getNumberOfPlayers();
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
