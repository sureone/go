package com.sureone;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.List;
import java.util.LinkedList;
import org.json.JSONObject;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.view.ViewPager;
public class RemoteSgfFragment extends BaseFragment {


	View mView=null;
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static RemoteSgfFragment newInstance(String content) {
        RemoteSgfFragment fragment = new RemoteSgfFragment();

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
		View view = inflater.inflate(R.layout.remotesgfview, null);
		mView = view;
		init(mContext,view);

        return view;
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
		
	}

	ViewPager mPager;
	public void setViewPager(ViewPager pager){
		mPager=pager;
	}	
	
	@Override
    public void onStop() {
		onHide();
		super.onStop();
	}

	@Override
	public void onResume(){
		
		super.onResume();
		if (mPager.getCurrentItem()==1){
			onShow();
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	
	
    @Override
    public void onPause() {
		
		onHide();
		super.onPause();
	}
	
    Button mBtnSearch = null;
    EditText mSearchTxt = null;
    Activity mContainer = null;
    RelativeLayout mContentGroup=null;
    boolean isDebug = false;

    public void init(Context context,View view) {

		mContainer = (Activity)context;
        GoApp app =  GoApp.getInstance();
        mGoController = app.getGoController();
        mHandler = new MainHandler();
        evtLis = new BtnEvtListener();
        mBtnSearch = (Button)mView.findViewById(R.id.btnSearch);
        mBtnSearch.setOnClickListener(evtLis);
        mSearchTxt = (EditText)mView.findViewById(R.id.txtSearch);
                mMainMsgThreads = new java.util.LinkedList<MessageSgf>();


        mGrid = (ListView) mView.findViewById(R.id.sgfList);
        if(mAppsAdapter==null) {
            mAppsAdapter = new AppsAdapter(mContainer);
            mGrid.setAdapter(mAppsAdapter);
            mGrid.setOnItemClickListener(mAppsAdapter);
            mGrid.setOnScrollListener(mAppsAdapter);
            //make listview transparent when scroll it.
            mGrid.setCacheColorHint(0);
        }
    }

	
	public void onShow(){

        GoApp.getInstance().getGoController().setHandler(mHandler);
		showRemote();
	}

	
	public void onHide(){
		if(mConn!=null){
			mConn.shutdown();
		}
		mConn=null;
	}
	
    void onBtnSearch() {
        if(mSearchKey.length()>0) {
            mMaxDt=0;
            mMinDt=0;
            loadMoreOld();
        }
    }

    public GoController mGoController = null;
    ListView mGrid = null;
    AppsAdapter mAppsAdapter = null;
    String mCurSgf=null;

    String readSgfFromFs(int id) {
        //Get the text file

        String fn = "sgf"+id+".sgf";
        String k="";
        try {
            byte[] buf = new byte[20480];
            FileInputStream fis = mContainer.openFileInput(fn);
            int len = fis.read(buf);
            if(len>0) {
                k=new String(buf,"utf-8");
            }
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return k;
    }

     void onEnterSGF(String sgfData) {
        //gotoSgfActivity
        Intent intent = new Intent(mContainer,com.sureone.SgfActivity.class);
        intent.putExtra("curSgf",sgfData);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContainer.startActivity(intent);
    }

    void getRemoteSgf(int id,String url) {
        if(mGoController.getLocalSgf(id)==null) {
            showWaitingDialog(R.string.savesgftolocal);
            mGoController.getRemoteSgf(id,url);
        }else{
            onEnterSGF(readSgfFromFs(id));
        }
    }



    void onLoadRemoteSgfDone(Bundle obj){
        try{

            int id = obj.getInt("id");
             xHelper.log("goapp","sgf"+id+" downloaded");

             onEnterSGF(readSgfFromFs(id));


             
        }catch(Exception e){

            xHelper.log("goapp","sgf download error");
        }

        closeWaitingDialog();
       
    }


    int mViewMode=1;
    public class AppsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,OnScrollListener {
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
                convertView = mInflater.inflate(R.layout.localsgfgriditem, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.sgfTxt=(TextView) convertView.findViewById(R.id.txtSgf);

                holder.icon = (ImageView) convertView.findViewById(R.id.sgfItemImage);
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
            // Bind the data efficiently with the holder.
            MessageSgf mt=mMainMsgThreads.get(position);


            xHelper.log("goapp",mt.cdate);
            holder.sgfTxt.setTextColor(android.graphics.Color.BLACK);
            holder.sgfTxt.setText(mt.name+"\n"+mt.black+" 对 "+mt.white+"\n"+mt.result);

            holder.id = mt.id;
            if(mViewMode==1) {
                if(mGoController.getLocalSgf(holder.id)!=null) {
                    holder.icon.setImageResource(R.drawable.done);
                } else {
                    holder.icon.setImageResource(R.drawable.save);
                }
            }


            //holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);



            return convertView;

        }


        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ViewHolder holder = (ViewHolder) v.getTag();
            if(mViewMode==1) {
                MessageSgf mt=mMainMsgThreads.get(position);
                getRemoteSgf(mt.id,mt.sgf);
                xHelper.log("goapp","get remote sgf="+holder.id);
                return;
            }
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!view.isInTouchMode()) {
                return false;
            }

            ViewHolder holder = (ViewHolder) view.getTag();

            return true;
        }

        public final int getCount() {
            //return xGlobal.getSystemInfo().mGames.size();
              return mMainMsgThreads.size();
        }

        public final Object getItem(int position) {
            //return (Object)(xGlobal.getSystemInfo().mGames.get(position));
            return null;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            if(mViewMode==1) {
                boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount-1);
                if(loadMore==true && totalItemCount!=0 && mWaitingDialog==null) {
                    loadMoreOld();
                }
            }
        }

        public final long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            TextView sgfTxt;
            ImageView icon;
            int id;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

        }
    }

    xTcpThread mConn=null;
    java.util.ArrayList<String> mSgfData = null;
    int mStartIdx=0;
    int mOnceNum=10;
    void connectToServer() {
        if(mSgfData==null) {
            mSgfData = new java.util.ArrayList<String>();
         }
            showWaitingDialog(R.string.waitingforgetsgf);
            loadMoreOld();
        
    }


    class MainHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GoController.RSP_LOAD_SGFS_FROM:
                    onLoadDone((LinkedList<MessageSgf>)msg.obj,true);
                break;
                case GoController.RSP_LOAD_SGFS_TO:
                    onLoadDone((LinkedList<MessageSgf>)msg.obj,false);
                break;

                case GoController.RSP_GET_REMOTE_SGF:
                    onLoadRemoteSgfDone((Bundle)msg.obj);
                break;
            }
        }
    }
    MainHandler mHandler = null;
    
    void onNotify(byte[] buf,int len,int offset) {

    }

    int mMaxDt=0;
    int mMinDt=0;

    boolean isLoadingThreads;

    java.util.LinkedList<MessageSgf> mMainMsgThreads = null;

    void onLoadDone(LinkedList<MessageSgf> threads,boolean isfrom){    
  
        if(threads!=null){
            try {
                int i=0;

                if(threads.size()>0){


                    if(isfrom){
                        mMaxDt=threads.getLast().id;
                        if(mMinDt==0){
                            mMinDt=threads.getFirst().id;
                        }
                    }else{
                        mMinDt=threads.getLast().id;
                        if(mMaxDt==0){
                            mMaxDt=threads.getFirst().id;
                        }                       
                    }
                }
                for (MessageSgf mt : threads) {
                    int pos=0;
                    
                    for(MessageSgf mmt: mMainMsgThreads){
                        
                        if(mmt.id==mt.id){
                            mMainMsgThreads.remove(pos);
                            break;
                        }
                        pos++;
                    }
                    
                    if(isfrom){
                        mMainMsgThreads.addFirst(mt);                       
                    }else{
                        mMainMsgThreads.addLast(mt);
                    }


                    
                }
                isLoadingThreads=false;
            } catch (Exception e) {
                xHelper.log("goapp",e.toString());
                e.printStackTrace();
            }
        }
        closeWaitingDialog();

        mAppsAdapter.notifyDataSetChanged();
    }

    
    void saveSgfToFs(int id,String data) {
        //Toast.makeText(this, getFilesDir().toString(), Toast.LENGTH_LONG).show();
        String fn = "sgf"+id+".sgf";

        File newFile=new File(mContainer.getFilesDir(), fn);

        BufferedWriter output;
        try {
            output = new BufferedWriter(new FileWriter(newFile));
            output.write(data);
            output.flush();
            output.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    void OnGetSgfResponse() {
        mAppsAdapter.notifyDataSetChanged();
        closeWaitingDialog();
    }

    AlertDialog showWaitingDialog(int id) {
        if(mWaitingDialog!=null) return null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContainer);
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

    String skey = null;
    void loadMoreNew(){
        
        GoApp.getInstance().getGoController().loadSgfsFrom(mMaxDt,mSearchKey);
    }
    
    void loadMoreOld(){
        if(mMinDt==0)
            GoApp.getInstance().getGoController().loadSgfsTo(0,mSearchKey);
        else
            GoApp.getInstance().getGoController().loadSgfsFrom(mMinDt,mSearchKey);  
    }


    String mSearchKey=null;
    void searchSgf(String key) {

        mSearchKey=key;
        showWaitingDialog(R.string.waitingforgetsgf);
        mMaxDt=0;
        loadMoreNew();
    }
    

    void showRemote() {
        mViewMode = 1;
        if(mAppsAdapter!=null)
            mAppsAdapter.notifyDataSetChanged();
        connectToServer();
    }
    BtnEvtListener evtLis=null;
    int lastView=R.id.BtnLocalSgf;
    class BtnEvtListener implements OnClickListener {

        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btnSearch:
                onBtnSearch();
                break;
            }
        }
    }
}
