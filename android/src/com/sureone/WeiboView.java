
package com.sureone;
import java.io.UnsupportedEncodingException;
import android.net.Uri;
import android.view.MenuInflater;
import java.util.List;
import java.util.LinkedList;
import android.view.MenuItem;
import android.view.Menu;
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
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class WeiboView extends Activity {
    AppsAdapter mAppsAdapter;
    ListView mGrid;
    java.util.LinkedList<MessageThread> mMainMsgThreads = null;
    int mStartIdx=0;
    int mOnceNum=5;
    String mLoadFrom = null;
    Button mBtnPost= null;
    EditText mTxtPost = null;
    TextView mListTitle = null;
	
	int mMaxTid=0;
	int mMinTid=0;
	
	int mMaxDt=0;
	int mMinDt=0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //mTimer.stop();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onPause() {
        //mTimer.stop();
        super.onPause();
    }
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        xHelper.log("goapp","BoardActivity onRestart");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        /*
        if(mTimer.mStopped == true){
        	mTimer.mStopped = false;
        	new Thread(mTimer, "Weibo Refresh Timer").start();
        }
        */
        super.onResume();
		GoApp.getInstance().getGoController().setHandler(mHandler);
		if(mMaxDt==0)
			loadMoreOld();
		else
			loadMoreNew();
        xHelper.log("goapp","BoardActivity onResume");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        xHelper.log("goapp","BoardActivity onStart");
    }
    @Override
    public void onStop() {
        //mTimer.stop();
        super.onStop();
    }

	MainHandler mHandler = new MainHandler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainMsgThreads = new java.util.LinkedList<MessageThread>();
        final Window win = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weiboview);

        mGrid = (ListView) findViewById(R.id.threadList);
        mAppsAdapter = new AppsAdapter(this);		
        com.viewpagerindicator.NormalTitle nt = (com.viewpagerindicator.NormalTitle) findViewById(R.id.ntTitle);        
        if(nt!=null) nt.setTitle(getString(R.string.boardtitle));
		
        mGrid.setAdapter(mAppsAdapter);
        mGrid.setOnItemClickListener(mAppsAdapter);

        //mGrid.setOnItemLongClickListener(mAppsAdapter);
        mGrid.setOnScrollListener(mAppsAdapter);
        //mGrid.setCacheColorHint(xSystem.BACK_COLOR);
        mGrid.setCacheColorHint(0);
        GoApp app =  GoApp.getInstance();
		
        //weibo = app.getWeibo();
        //mTimer = new Timer(2000);
        //new Thread(mTimer, "Weibo Refresh Timer").start();
        evtLis = new EvtListener();

        mBtnPost = (Button)findViewById(R.id.btnPost);
        mBtnPost.setOnClickListener(evtLis);
        mTxtPost = (EditText)findViewById(R.id.txtPost);
        RelativeLayout backLayout = (RelativeLayout)findViewById(R.id.backLayout);
        //backLayout.setBackgroundColor(xSystem.BACK_COLOR);
        //mTxtPost.clearFocus();
 
		this.registerForContextMenu(mGrid);



    }


	String getTimeDiff(String dt){
				
				
				java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date cdate=null;
				try{
				cdate=sf.parse(dt);
				}catch(Exception e){
					e.printStackTrace();
				}
				String diff=null;
				java.util.Date now = new java.util.Date(System.currentTimeMillis());
				long ms = now.getTime()-cdate.getTime();
				long s = ms/1000;
				long m = ms/(1000*60);
				long h = ms/(1000*60*60);
				long d = ms/(1000*60*60*24);
				long mo = ms/(1000*60*60*24*30);
				long y = ms/(1000*60*60*24*30*365);
				
				String ss=sf.format(now);
				// xHelper.log("test=="+ss);
				// xHelper.log("test=="+sf.format(cdate));
				if(y>0){
					diff=y+this.getString(R.string.yearago);
				}else if(mo>0){
					diff=mo+this.getString(R.string.monago);
				}else if(d>0){
					diff=d+this.getString(R.string.dayago);
				}else if(h>0){
					diff=h+this.getString(R.string.hourago);
				}else if(m>0){
					diff=m+this.getString(R.string.minago);
				}else if(s>0){
					diff=this.getString(R.string.justnow);
				}else if(ms>=0){
					diff=this.getString(R.string.justnow);
				}else{
					diff=this.getString(R.string.justnow);
				}
				return diff;
	}
    void showProgress() {
        ProgressBar pb_reward_loading = (ProgressBar)findViewById(R.id.pb_loading);
        pb_reward_loading.setVisibility(View.VISIBLE);
    }
    void hideProgress() {
        ProgressBar pb_reward_loading = (ProgressBar)findViewById(R.id.pb_loading);
        pb_reward_loading.setVisibility(View.GONE);
    }

	
    class MainHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
				case GoController.RSP_LOAD_THREADS_FROM:
					onLoadDone((LinkedList<MessageThread>)msg.obj,true);
				break;
				case GoController.RSP_LOAD_THREADS_TO:
					onLoadDone((LinkedList<MessageThread>)msg.obj,false);
				break;
				case GoController.RSP_POST_THREAD:				
				case GoController.RSP_POST_REPLY:
					onPostDone(msg.arg1);
					break;
				case GoController.RSP_RM_THREAD:{
					
					int pos=0;
					for(MessageThread mmt: mMainMsgThreads){
						
						if(mmt.id==mSelectedThreadId){
							mMainMsgThreads.remove(pos);
							break;
						}
						pos++;
					}	
					mAppsAdapter.notifyDataSetChanged();					
				}
				break;
			}
		}
	}
	String sids="";
	void onLoadDone(LinkedList<MessageThread> threads,boolean isfrom){		
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
				for (MessageThread mt : threads) {
					int pos=0;
					for(MessageThread mmt: mMainMsgThreads){
						
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
					
					

					xHelper.log(mt.uname+"@"+mt.cdate+":"+mt.content);
				}
				isLoadingThreads=false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		hideProgress();
		mAppsAdapter.notifyDataSetChanged();
	}

    boolean isLoadingThreads=false;

	String mToPost=null;
    void post(String txt) {
        
		if(txt==null ||txt.length()<=0) return;			
		GoApp.getInstance().getGoController().postThread(txt);
    }
	void onPostDone(int id){
		hideProgress();
        try {
            //Hide the soft keyboard
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mTxtPost.getWindowToken(), 0);
            mTxtPost.setText("");
            loadMoreNew();
        } catch(Exception e1) {
            e1.printStackTrace();
        }
	}
	
	void loadMoreNew(){
		GoApp.getInstance().getGoController().loadThreadsFrom(mMaxDt,0);
	}
	
	void loadMoreOld(){
		if(mMinDt==0)
			GoApp.getInstance().getGoController().loadThreadsTo(0,0);
		else
			GoApp.getInstance().getGoController().loadThreadsTo(mMinDt,0);			
	}


    class EvtListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btnPost:
                String s = mTxtPost.getText().toString().trim();
                post(s);
            }
        }
    }


    Intent mThreadView=null;
    public int mSelectedThreadId=0;
    void onEnterThread() {
        //gotoSgfActivity
        if(mThreadView==null)
            mThreadView = new Intent(this,com.sureone.WeiboThread.class);
        startActivity(mThreadView);
    }
    EvtListener evtLis=null;
	
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int menuItemIndex = item.getItemId();
            if(menuItemIndex==0) { //delete          
				
				MessageThread mt=mMainMsgThreads.get(info.position);
				mSelectedThreadId=mt.id;                       
				   
				onDeleteThread(mSelectedThreadId,mt.uid);
            }

            if(menuItemIndex==1) { //blockuser

                MessageThread mt=mMainMsgThreads.get(info.position);


                onBlockUser(mt.sn);
            }
        } catch (ClassCastException e) {
            
            return false;
        }
        return true;

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.threadList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(getString(R.string.SgfMenu));
            menu.add(Menu.NONE, 0, 0, getString(R.string.delete));
            menu.add(Menu.NONE, 1, 1, "封号");
        }
    }
	

    void onDeleteThread(int id,int uid) {
		GoApp.getInstance().getGoController().removeThread(id,uid);
    }

    void onBlockUser(String sn) {
        GoApp.getInstance().getGoController().blockUser(sn);
    }
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
                convertView = mInflater.inflate(R.layout.weiboitem, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.txtContent=(TextView) convertView.findViewById(R.id.txtContent);
                holder.txtAuthor=(TextView)convertView.findViewById(R.id.txtAuthor);
                holder.txtReplyAndView=(TextView)convertView.findViewById(R.id.txtReplyAndView);
                //holder.icon = (ImageView) convertView.findViewById(R.id.rsgfItemImage);
                //holder.wicon = (ImageView) convertView.findViewById(R.id.WhiteItemImage);
                //xHelper.log("DeskListActivity:","new position="+position);
                convertView.setTag(holder);

                //convertView.setOnCreateContextMenuListener(mContextMenuListener);

            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.


                //xHelper.log("DeskListActivity:","position="+position);
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            MessageThread mt=mMainMsgThreads.get(position);


            holder.txtContent.setText(mt.content);
            holder.txtAuthor.setText(mt.uname+
				WeiboView.this.getString(R.string.postat)+getTimeDiff(mt.cdate));
            holder.txtReplyAndView.setText( getString(R.string.reply)+" "+mt.rcount);
            //holder.txtReplyAndView.setVisibility(View.GONE);

            return convertView;

        }


        public void onItemClick(AdapterView parent, View v, int position, long id) {

            ViewHolder holder = (ViewHolder) v.getTag();
            MessageThread mt=mMainMsgThreads.get(position);
            GoApp.getInstance().mCurrentThread=mt;
            onEnterThread();
            xHelper.log("goapp","get remote sgf="+holder.id);
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!view.isInTouchMode()) {
                return false;
            }
            ViewHolder holder = (ViewHolder) view.getTag();

            MessageThread mt=mMainMsgThreads.get(position);



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



        public final long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            TextView txtContent;
            TextView txtDate;
            TextView txtAuthor;
            TextView txtReplyAndView;
            int id;
        }

        boolean mReallyEnd=false;

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            boolean loadMore = /* maybe add a padding */
                firstVisibleItem + visibleItemCount >= totalItemCount-1;
            // xHelper.log("goapp","onScroll");
            if(loadMore==true && totalItemCount!=0 && mReallyEnd==false) {
                //refresh();
                // xHelper.log("goapp","scroll to end");
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub
            // xHelper.log("goapp","onScrollStateChanged");

        }
    }
}
