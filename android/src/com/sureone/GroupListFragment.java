package com.sureone;
import android.util.DisplayMetrics;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.view.*;
import android.widget.ProgressBar;
import android.support.v4.app.Fragment;
import android.graphics.drawable.*;
import android.graphics.drawable.shapes.RectShape;
import android.widget.Toast;
import com.sureone.GoApp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.app.Dialog;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import com.sureone.model.Group;
import com.sureone.view.GroupViewActivity;
import us.xdroid.util.xUtil;
public class GroupListFragment extends BaseFragment {
    AppsAdapter mAppsAdapter;
    ListView mGrid;
    GoController mController=null;

    boolean isHide=true;

    View mView=null;
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static GroupListFragment newInstance(String content) {
        GroupListFragment fragment = new GroupListFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        return fragment;
    }

    private String mContent = "???";

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
        View view = inflater.inflate(R.layout.grouplistview, null);
        mView = view;
        init(mContext,view);


        ImageView imageView = (ImageView)view.findViewById(R.id.btnMore);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onMoreClick();
            }
        });

        return view;
    }

    public void onMoreClick(){
        this.getActivity().openOptionsMenu();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    Context mContext = null;
	
	/*
	Called when a fragment is first attached to its activity. onCreate(Bundle) will be called after this.
	*/

    @Override
    public void onAttach (Activity activity){
        super.onAttach(activity);
        mContext = activity;

        setHasOptionsMenu(true);


    }



    ViewPager mPager;
    public void setViewPager(ViewPager pager){
        mPager=pager;
    }

    @Override
    public void onStop() {
        if (mPager.getCurrentItem()==0){
            xHelper.log("GroupList onHide");
            isHide=true;
            mAppsAdapter.notifyDataSetChanged();
            if(mHandler==mController.mHandler)
                mController.setHandler(null);
        }
        super.onStop();
    }

    @Override
    public void onResume(){
        xHelper.log("GroupList onResume");
        super.onResume();
        if (mPager.getCurrentItem()==0){
            xHelper.log("PlayersList onShow");
            isHide=false;
            //mAppsAdapter.notifyDataSetChanged();
            mController.setHandler(mHandler);
            loadListData();
        }
    }

    @Override
    public void onStart(){
        xHelper.log("PlayersList onStart");
        super.onStart();
    }



    @Override
    public void onPause() {
        if (mPager.getCurrentItem()==1){
            xHelper.log("PlayersList onHide");
            isHide=true;
            //mAppsAdapter.notifyDataSetChanged();
            if(mHandler==mController.mHandler)
                mController.setHandler(null);
        }
        super.onPause();
    }

    public void onHide() {

        xHelper.log("PlayersList onHide");
        isHide=true;
        //mAppsAdapter.notifyDataSetChanged();
        if(mHandler==mController.mHandler)
            mController.setHandler(null);

    }

    public void onShow() {


        xHelper.log("PlayersList onShow");
        isHide=false;
        //mAppsAdapter.notifyDataSetChanged();
        mController.setHandler(mHandler);
        loadListData();

    }




    GoApp mApp=null;

    public void init(Context context,View view) {
        mApp =  GoApp.getInstance();
        mController = mApp.getGoController();
        mGrid = (ListView) mView.findViewById(R.id.grouplist);
        mAppsAdapter = new AppsAdapter((Activity)mContext);
        mGrid.setAdapter(mAppsAdapter);
        mGrid.setOnItemClickListener(mAppsAdapter);
        mGrid.setOnItemLongClickListener(mAppsAdapter);
        mGrid.setOnScrollListener(mAppsAdapter);
        mGrid.setCacheColorHint(0);
        mHandler = new MyHandler();
        evtLis = new EvtListener();
        ((Activity)mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    void LogLog(String s){

        xHelper.log("goapp","PlayerListView: "+s);
    }
    static final int LOAD_LIST_DELAY=1;
    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = LOAD_LIST_DELAY;
            mHandler.sendMessage(msg);
        }
    }

    void loadListData(){
//        mHandler.postDelayed(new MyRunnable(), 300);
        mController.WebListGroup();
    }


    void onLoadGroupListDone() {
        mAppsAdapter.notifyDataSetChanged();
        int cnt = mController.getNumberOfGroups();
    }
    void acceptInvite(Bundle msg) {
        mController.acceptInvite(msg);
    }
    void declineInvite(Bundle msg) {
        mController.declineInvite(msg);
    }
    Bundle mInviteMsg=null;
    void showInfoDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity)mContext);
        builder.setMessage(str)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    void onAlarm(int did){
        String text = String.format(mContext.getResources().getString(R.string.alarm), did);
        showToast(text);
    }
    void onSilentConnectOK() {
        xHelper.log("goapp","PlayerListView:onSilentConnectOK");
        String email = mApp.getEmail();
        String pin = mApp.getPin();
        mController.login(email,pin);
    }


    private MyHandler mHandler;
    class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case GoController.MSG_RSP_LIST_GROUP:

                    onLoadGroupListDone();
                    break;


                case LOAD_LIST_DELAY:
                    mController.WebListGroup();


                    break;
                case GoController.WEB_LIST_GROUP:
                    xHelper.log("WEB LIST GROUP OK");
                    Map map = (Map) msg.obj;
                    xHelper.log(map.toString());
                    mController.parseGroupsData(map);
                    onLoadGroupListDone();



                    break;
                case GoController.MSG_NTFY_ALARM: {
                    onAlarm(msg.arg1);
                    break;
                }

                case GoController.MSG_CONNECT_OK: {
                    onSilentConnectOK();
                    break;
                }

            }
        }
    }
    ProgressDialog showWaitingDialog(int id) {
        ProgressDialog alert = new ProgressDialog((Activity)mContext);
        mWaitingDialog=alert;
        alert.setMessage(mContext.getString(id));
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
            xUtil.log("onclick="+v.getId());
            switch (v.getId()) {

            }
        }
    }

    void showToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    EvtListener evtLis=null;
    Group mSelGroup=null;
    View mSelItemView = null;



    void onClickGroup(int pos) {
        Group group = mController.getGroupByIndex(pos);
        if(group==null) return;
        mSelGroup = group;


        Intent intent =  new Intent(this.getActivity(), GroupViewActivity.class);

        intent.putExtra("group_id",mSelGroup.getGROUP_ID());


        startActivity(intent);


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu (Menu menu){
        menu.clear();
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.groupoptions,menu);
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection

        switch (item.getItemId()) {
            case R.id.createGroup:
                startActivity(new Intent(this.getActivity(), GroupRegisterView.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
                convertView = mInflater.inflate(R.layout.groupitem, null);
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.txtGroupName=(TextView) convertView.findViewById(R.id.txtGroupName);
                holder.txtStatus=(TextView) convertView.findViewById(R.id.txtStatus);

                holder.icon = (LoaderImageView) convertView.findViewById(R.id.ivIcon);
                holder.lvIcon=(LinearLayout)convertView.findViewById(R.id.lvIcon);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Group group = mController.getGroupByIndex(position);
            if(group==null) return convertView;
            String s = group.getGROUP_NAME();
            holder.txtGroupName.setText(s);
            s= mContext.getString(R.string.members_num)+": "+group.getMEMBERS_NUM()+" "+
                    mContext.getString(R.string.jifeng)+": "+ group.getGROUP_SCORE() +"\n"
                    +mContext.getString(R.string.creator)+": "+ group.getOWNER_NAME()+"("+GoModel.rankLabel(group.getOWNER_RANK().intValue())+")";

            holder.txtStatus.setText(s);

            holder.icon.setLocalResource(R.drawable.list_grouphead_normal);



            return convertView;

        }



        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ViewHolder holder = (ViewHolder) v.getTag();

            onClickGroup(position);
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ViewHolder holder = (ViewHolder) view.getTag();
            return true;
        }

        public final int getCount() {
            if(isHide==true) return 0;
            int cnt = mController.getNumberOfGroups();
            return cnt;
        }

        public final Object getItem(int position) {
            return null;
        }

        public final long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            TextView txtGroupName;
            TextView txtStatus;

            LoaderImageView icon;
            LinearLayout lvIcon;
            int id;
        }


        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub
        }
    }
}
