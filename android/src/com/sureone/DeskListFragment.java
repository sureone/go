package com.sureone;


import android.util.DisplayMetrics;
import android.widget.Toast;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;
import android.support.v4.view.ViewPager;
import com.sureone.view.DeskSetupActivity;

import java.util.List;


public class DeskListFragment extends BaseFragment {
    public static final String TAG="goapp";
    GridView mGrid;
    AppsAdapter mAppsAdapter;
    private DeskListHandler mHandler = null;
    int mRefreshTimeout = 60*1000;
    Timer mTimer = null;
    Intent mGoActivity = null;
    Intent mMainActivity = null;
    EvtListener evtLis=null;
    int mJoinedAndWaitList = 0;
    GoApp app = null;
    String loginResult = null;
    /** Called when the Activity is first created. */
	
	View mView=null;

    DisplayMetrics mDM=null;
    GoController mGoController=null;
	private static final String KEY_CONTENT = "TestFragment:Content";
	
    public static DeskListFragment newInstance(String content) {
        DeskListFragment fragment = new DeskListFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        return fragment;
    }
	
	private String mContent = "???";
	
	Context mContext = null;
	
	/*
	Called when a fragment is first attached to its activity. onCreate(Bundle) will be called after this.
	*/
	
	@Override
	public void onAttach (Activity activity){
		super.onAttach(activity);
		mContext = activity;	
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//LinearLayout layout = new LinearLayout(getActivity());
		View view = inflater.inflate(R.layout.desklistview, null);
		mView = view;
		init(mContext,view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
	
	

    public void init(Context context,View view) {        
	    app = GoApp.getInstance();
	    mHandler = new DeskListHandler();
		xHelper.log("desk handler="+mHandler);
        mGoController = app.getGoController();
        mDM=new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(mDM);	
        mGrid = (GridView) mView.findViewById(R.id.deskGrid);
        evtLis = new EvtListener();
		LinearLayout miniLayout =(LinearLayout)view.findViewById(R.id.miniAdLinearLayout); 
		//new com.waps.MiniAdView(context, miniLayout).DisplayAd(60); //默认10秒切换一次广告

        mAppsAdapter = new AppsAdapter((Activity)mContext);
        mGrid.setAdapter(mAppsAdapter);
        mGrid.setOnItemClickListener(mAppsAdapter);
        mGrid.setOnItemLongClickListener(mAppsAdapter);

        //new Thread(mTimer, "Desk List Refresh Timer").start();
        String value = ((Activity)mContext).getIntent().getStringExtra("login");
        loginResult = value;


	//do this in Tab switch
    }
	

	ViewPager mPager;
	public void setViewPager(ViewPager pager){
		mPager=pager;
	}
	
    @Override
    public void onStop() {
        if(mHandler==mGoController.mHandler && mPager.getCurrentItem()==0)
            mGoController.setHandler(null);
		super.onStop();
		xHelper.log("DeskListActivity:","onStop is called");
	}

	@Override
	public void onResume(){
		super.onResume();
		if(mPager.getCurrentItem()==0){
			mGoController.setHandler(mHandler);
			getDeskListData();
		}
        xHelper.log("DeskListActivity:","onResume is called");
	}
	
	@Override
	public void onStart(){
		super.onStart();
		xHelper.log("DeskListActivity:","onStart is called");
	}
	
	
	
    @Override
    public void onPause(){
		
		xHelper.log("DeskListActivity:","onPause is called");
        if(mHandler==mGoController.mHandler && mPager.getCurrentItem()==0)
            mGoController.setHandler(null);
		super.onPause();
	}
	
	public void onHide(){
		xHelper.log("desk onHide");
		mGoController.setHandler(null);
	}
	public void onShow(){
		xHelper.log("desk onShow");
		mGoController.setHandler(mHandler);
		getDeskListData();		
	}
	

    class EvtListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.txtListTitle:
                break;

            }
        }

    }



    void toGoActivityObserve() {
		xHelper.log("toGoActivityObserve");
        Intent intent = new Intent(mContext,com.sureone.GoActivity.class);
        intent.putExtra("view","observe");
        intent.putExtra("fullscreen","false");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
            ((Activity)mContext).finish();
    }
    void toGoActivity() {
        Intent intent = new Intent(mContext,com.sureone.GoActivity.class);
        intent.putExtra("view","normal");
        intent.putExtra("fullscreen","false");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
            ((Activity)mContext).finish();
    }

    void toDeskSetupActivity(){

        Intent intent = new Intent(mContext,DeskSetupActivity.class);
        intent.putExtra("view","normal");
        intent.putExtra("fullscreen","false");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);

    }
    void toGoActivityStart() {
        Intent intent = new Intent(mContext,com.sureone.GoActivity.class);
        intent.putExtra("view","start");

        intent.putExtra("fullscreen","false");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
            ((Activity)mContext).finish();
    }
    void toGoActivityResume() {
        Intent intent = new Intent(mContext,com.sureone.GoActivity.class);
        intent.putExtra("view","resume");
        if(mDM.heightPixels<=480)
            intent.putExtra("fullscreen","true");
        else
            intent.putExtra("fullscreen","false");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
            ((Activity)mContext).finish();
    }

    void onSilentConnectOK() {
        xHelper.log("goapp","DeskListActivity:onSilentConnectOK");
        String email = app.getEmail();
        String pin = app.getPin();
        mGoController.login(email,pin);
    }

    void onGameStart() {
        xHelper.log("goapp","DeskListActivity:toGoActivityStart");
        toGoActivityStart();
    }
    void onConnectBroken() {
        xHelper.log("goapp","DesklistActivity:onConnectBroken");
        mGoController.silentReconnect();
    }
    void onJoinOK() {
        closeWaitingDialog();
        toDeskSetupActivity();
        //toGoActivity();
    }
    void onResponseList() {
        //update grid view
        refreshList();
		if(loginResult==null) return;
        if(loginResult.compareTo("resume")==0) {
            mGoController.resume();
            mWaitingDialog=showWaitingDialog(R.string.waitingforjoin);
        }
        mGoController.statRoom();
    }
    void showToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    void getDeskListData() {
		mGoController.loadDeskList();
    }
    private class Timer implements Runnable {
        private boolean mStopped;
        private long mInterval;
        public Timer(long interval) {
            mInterval = interval;
            mStopped = false;
        }
        public void stop() {
            mStopped = true;
        }
        public void run() {
            while (!mStopped) {
                getDeskListData();
                try {
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    continue;
                }
            }
        }
    }

	void onAlarm(int did){
        String text = String.format(mContext.getResources().getString(R.string.alarm), did);
		showToast(text);
	}



    class DeskListHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            xHelper.log(TAG,"Receive the msg ="+msg.what+" from GoController");
            switch (msg.what) {
            case GoController.MSG_NTFY_ALARM: {
                onAlarm(msg.arg1);
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
            case GoController.MSG_CONNECT_OK: {
                onSilentConnectOK();
                break;
            }

            case GoController.MSG_RSP_LIST: {
                onResponseList();
                break;
            }
            case GoController.MSG_NTFY_USER_JOIN:
                mAppsAdapter.notifyDataSetChanged();
                break;
            case GoController.MSG_NTFY_SET_STEP_TIME:
                xHelper.log("goapp","receive the step time update!");
                mAppsAdapter.notifyDataSetChanged();
                break;

                case GoController.MSG_NTFY_USER_LEAVE:
                mAppsAdapter.notifyDataSetChanged();
                break;
                case GoController.MSG_NTFY_STAT_ROOM:
                    mAppsAdapter.notifyDataSetChanged();
                    break;
            case GoController.MSG_RSP_JOIN_OK: {
                xHelper.log("goapp","JOINED OK");
                onJoinOK();
                mJoinedAndWaitList=1;
                break;
            }

            case GoController.MSG_RSP_RESUME_OK: {
                xHelper.log("goapp","RESUME OK");
                closeWaitingDialog();
                toGoActivityResume();
                break;
            }
            case GoController.MSG_RSP_OBSERVE_OK: 
			case GoController.MSG_NTFY_OBSERVE_START:
			{
                xHelper.log("goapp","OBSERVE OK");
                closeWaitingDialog();
                toGoActivityObserve();
                break;
            }
            case GoController.MSG_RSP_JOIN_FAIL: {
                closeWaitingDialog();
                break;
            }
            case GoController.MSG_RSP_OBSERVE_FAIL: {
                closeWaitingDialog();
                break;
            }

            }
        }
    }
    void onTcpDisconnected() {
        Intent intent = new Intent(mContext,com.sureone.EntryView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
    }
    public void refreshList() {
        mAppsAdapter.notifyDataSetChanged();
        //setTitle(mContext.getString(R.string.playernum)+": "+mGoController.getUserNum());
    }
    AlertDialog showWaitingDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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

    AlertDialog mWaitingDialog=null;
    void closeWaitingDialog() {
        if(mWaitingDialog!=null) {
            xHelper.log("goapp","closeWaitingDialog");
            mWaitingDialog.dismiss();
            mWaitingDialog=null;
        }

    }
    public class AppsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
        private LayoutInflater mInflater;
        public AppsAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
        }

        public View getView(int position, View convertView, ViewGroup parent) {


            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.deskgriditem, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.deskId=(TextView) convertView.findViewById(R.id.txtDeskId);
                holder.bname=(TextView) convertView.findViewById(R.id.txtBlack);
                holder.join=(TextView) convertView.findViewById(R.id.txtJoin);
                holder.binfo=(TextView) convertView.findViewById(R.id.txtBlackInfo);
                holder.wname=(TextView) convertView.findViewById(R.id.txtWhite);
                holder.winfo=(TextView) convertView.findViewById(R.id.txtWhiteInfo);
                holder.icon = (ImageView) convertView.findViewById(R.id.deskItemImage);
                holder.time = (TextView) convertView.findViewById(R.id.txtStepTime);
                //holder.wicon = (ImageView) convertView.findViewById(R.id.WhiteItemImage);
                //xHelper.log("DeskListActivity:","new position="+position);
                convertView.setTag(holder);


            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.


                //xHelper.log("DeskListActivity:","position="+position);
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.

            Desk desk = mGoController.getDesk(position);
            holder.icon.setImageResource(R.drawable.desk);
            if(desk.black!=null) {
				holder.bname.setVisibility(View.VISIBLE);
				holder.binfo.setVisibility(View.VISIBLE);
				holder.bname.setText(desk.black.name);
				desk.black.loses=desk.black.totals-desk.black.wins;
				holder.binfo.setText(desk.black.rank+" "+
					mContext.getString(R.string.win)+desk.black.wins+" "+
					mContext.getString(R.string.lost)+(desk.black.loses));
            }else{
				holder.bname.setVisibility(View.GONE);
				holder.binfo.setVisibility(View.GONE);
			}

            if(desk.white!=null) {
				holder.wname.setVisibility(View.VISIBLE);
				holder.winfo.setVisibility(View.VISIBLE);
				holder.wname.setText(desk.white.name);
				desk.white.loses=desk.white.totals-desk.white.wins;
				holder.winfo.setText(desk.white.rank+" "+
					mContext.getString(R.string.win)+desk.white.wins+" "+
					mContext.getString(R.string.lost)+(desk.white.loses));
            }else{
				holder.wname.setVisibility(View.GONE);
				holder.winfo.setVisibility(View.GONE);
			}


            if(desk.black==null && desk.white==null) {
                holder.deskId.setVisibility(View.VISIBLE);
                holder.deskId.setText(""+(position+1));
                holder.join.setVisibility(View.VISIBLE);
                holder.time.setVisibility(View.GONE);
            } else {
                holder.join.setVisibility(View.GONE);
                holder.deskId.setVisibility(View.GONE);
                holder.time.setVisibility(View.VISIBLE);
                holder.time.setText(""+(desk.step_time_out/60)+getString(R.string.minutes));

            }

            //holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);
            return convertView;
        }


        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Desk desk = mGoController.getDesk(position);
            xHelper.log("DeskListActivity:","User select desk:"+desk.id);
            if(desk.black!=null && desk.white!=null) {
                mGoController.joinObserve(desk.id);
                mWaitingDialog=showWaitingDialog(R.string.waitingforjoin);
            } else if(mGoController.joinDesk(desk.id,0)==-1)
                xHelper.log("DeskListActivity:","Can not select this desk");
            else
                mWaitingDialog=showWaitingDialog(R.string.waitingforjoin);

        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!view.isInTouchMode()) {
                return false;
            }

            return true;
        }

        public final int getCount() {
            return mGoController.getDeskNum();
        }

        public final Object getItem(int position) {
            //return (Object)(xGlobal.getSystemInfo().mGames.get(position));
            return null;
        }


        public final long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            TextView deskId;
            TextView bname;
            TextView join;
            TextView binfo;
            TextView wname;
            TextView winfo;
            ImageView icon;
            TextView time;
            Desk desk;
        }
    }
}
