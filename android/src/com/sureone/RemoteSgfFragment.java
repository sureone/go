package com.sureone;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
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
        mHandler = new SgfHandler();
        evtLis = new BtnEvtListener();
        mBtnSearch = (Button)mView.findViewById(R.id.btnSearch);
        mBtnSearch.setOnClickListener(evtLis);
        mSearchTxt = (EditText)mView.findViewById(R.id.txtSearch);

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
		showRemote();
	}

	
	public void onHide(){
		if(mConn!=null){
			mConn.shutdown();
		}
		mConn=null;
	}
	
    void onBtnSearch() {
        String s = mSearchTxt.getText().toString().trim();
        if(s.length()>0) {
            mSgfData.clear();
            mStartIdx=0;
            searchSgf(s);
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


    void getRemoteSgf(int id) {
        if(mGoController.getLocalSgf(id)==null) {
            showWaitingDialog(R.string.savesgftolocal);
            String str = "request:get\r\n"+
                         "type:sgf\r\n"+
                         "id:"+id+"\r\n\r\n";
        	if(mConn!=null) mConn.sendData(str);
        }
    }
    void LoadMoreSgf() {
		if(mPager.getCurrentItem()!=1) return;
        mSearchKey=mSearchTxt.getText().toString().trim();
        if(mSearchKey!=null && mSearchKey.length()>0) {
            searchSgf(mSearchKey);
        } else {
            LoadSgf();
        }
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


            String s="";
            SgfHeader h = null;
            if(mViewMode==1) {
                String ss=mSgfData.get(position);
                // holder.icon.setImageResource(R.drawable.deskicon);
                SgfParser parser = new SgfParser(ss);
                if(parser.parseHeader()==true) {
                    h = parser.mH;
                }
            } 
            s=h.EV;
            String sbr=h.BR;
            String swr=h.WR;
            if(sbr.compareTo("NA")==0) sbr="";
            if(swr.compareTo("NA")==0) swr="";
            if(isDebug==true) s+="("+h.ID+")";
            s+="\n"+h.PB+sbr+" VS "+h.PW+swr+"\n";
            String[] ss = new String[2];
            ss[0]=h.RE.substring(0, 1);
            ss[1]=h.RE.substring(2);
            if(ss.length==2) {
                if(ss[1].compareTo("R")==0) {
                    if(ss[0].compareTo("B")==0)
                        s+=mContainer.getString(R.string.REBR);
                    if(ss[0].compareTo("W")==0)
                        s+=mContainer.getString(R.string.REWR);
                } else if(ss[0].compareTo("B")==0 || ss[0].compareTo("W")==0) {
                    if(ss[0].compareTo("B")==0)
                        s+=mContainer.getString(R.string.black)+
                           ss[1]+mContainer.getString(R.string.mu)+mContainer.getString(R.string.win);
                    if(ss[0].compareTo("W")==0)
                        s+=mContainer.getString(R.string.white)+
                           ss[1]+mContainer.getString(R.string.mu)+mContainer.getString(R.string.win);
                } else {
                    s+=h.RE;
                }
            }

            holder.sgfTxt.setTextColor(android.graphics.Color.BLACK);
            holder.sgfTxt.setText(s);

            holder.id = h.ID;
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
                getRemoteSgf(holder.id);
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
                if(mSgfData==null ) return 0;
                return mSgfData.size();
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
                    LoadMoreSgf();
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
            tcpevtLis = new EvtListener();
        }
        if(mConn==null) {
            showWaitingDialog(R.string.waitingforgetsgf);
            mConn=new xTcpThread();
            //mConn.setAddress("192.168.0.115", 82);
            //mConn.setAddress("192.168.56.101", 82);
            mConn.setAddress("184.82.230.120", 82);
            //mConn.setAddress("xdroid.us", 82);
            //mConn.setAddress("10.186.2.195", 82);
            mConn.setTcpEventListener(tcpevtLis);

            //put the connection start on a seperate thread to execution from block the UI.
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    mConn.start();
                }
            }, 500);
        }
    }
    SgfHandler mHandler = null;
    class SgfHandler extends Handler {
        private static final int MSG_LS_SGF = 0;
        private static final int MSG_GET_SGF = 1;
        private static final int MSG_LOAD_SGF = 2;
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_LS_SGF: {
                OnLsSgfResponse(msg.arg1);
                break;
            }
            case MSG_GET_SGF: {
                OnGetSgfResponse();
                break;
            }
            case MSG_LOAD_SGF: {
                LoadSgf();
                break;
            }

            }
        }
    }
    void onNotify(byte[] buf,int len,int offset) {

    }
    static final int RSP_DONE=1;
    static final int RSP_WAIT=2;
    static final class SgfDownload {
        int id=0;
        int len=0;
        int cur=0;
        byte[] data=null;
    }
    SgfDownload mLastSgfDl=null;
    byte[] last_rsp_data=null;
    int onResponse(byte[] buf,int len,int offset) {
        int i = 0;
        int start = offset;
        int count = 0;
        while(offset<len) {
            if(buf[offset]==',') {
                offset++;
                break;
            }
            count++;
            offset++;
        }

        String type= new String(buf,start,count);
        if(type.compareTo("ls")==0) {
            x_Integer o = new x_Integer(offset);
            int cnt = xHelper.getInt(buf, len, o, ',');
            Message message = new Message();
            message.arg1=cnt;
            while(cnt>0) {
                String str = xHelper.getStr(buf, len, o, ';');
                if(str!=null)
                    mSgfData.add(str);
                cnt--;
            }

            message.what = SgfHandler.MSG_LS_SGF;
            mHandler.sendMessage(message);
        } else if(type.compareTo("get")==0) {
            x_Integer o = new x_Integer(offset);
            String t = xHelper.getStr(buf, len, o, ',');
            if(t.compareTo("sgf")==0) {
                SgfDownload sgfdl=new SgfDownload();
                sgfdl.id = xHelper.getInt(buf, len, o, ',');
                sgfdl.len = xHelper.getInt(buf, len, o, ',');
                sgfdl.cur+=len-o.v;
                sgfdl.data=new byte[sgfdl.len];
                if(sgfdl.cur>sgfdl.len)
                    sgfdl.cur=sgfdl.len;
                xHelper.log("goapp","onResponse get expect:"+sgfdl.len+",current:"+sgfdl.cur);
                System.arraycopy(buf, o.v, sgfdl.data, 0,sgfdl.cur);
                if(sgfdl.cur>=sgfdl.len) {
                    String data = new String(sgfdl.data);
                    mGoController.AddSgf(sgfdl.id, data);
                    saveSgfToFs(sgfdl.id,data);
                    sgfdl=null;
                    Message message = new Message();
                    message.what = SgfHandler.MSG_GET_SGF;
                    mHandler.sendMessage(message);
                }
                mLastSgfDl=sgfdl;
            }

        }
        last_rsp_data=null;
        return RSP_DONE;
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
    void OnLsSgfResponse(int cnt) {
        if(cnt>0)
            mAppsAdapter.notifyDataSetChanged();
        closeWaitingDialog();
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
    void LoadSgf() {
        mStartIdx=mSgfData.size();
        showWaitingDialog(R.string.waitingforgetsgf);
        String str = "request:ls\r\n"+
                     "num:"+mOnceNum+"\r\n"+
                     "start:"+mStartIdx+"\r\n\r\n";
        if(mConn!=null)
            mConn.sendData(str);
    }

    String mSearchKey=null;
    void searchSgf(String key) {

        mSearchKey=key;
        showWaitingDialog(R.string.waitingforgetsgf);
        String str = "request:ls\r\n"+
                     "num:"+mOnceNum+"\r\n"+
                     "key:"+key+"\r\n"+
                     "start:"+mStartIdx+"\r\n\r\n";
        if(mConn!=null)
        	mConn.sendData(str);
    }
    EvtListener tcpevtLis = null;
    class EvtListener implements xTcpEventListener {
        public void onConnected(xTcpThread thread) {
            Message message = new Message();
            message.what = SgfHandler.MSG_LOAD_SGF;
	    if(mHandler!=null) mHandler.sendMessage(message);
        }
        public void onConnectTimeOut(xTcpThread thread) {
			mConn= null;
        }
        public void onDisconnected(xTcpThread thread) {
			mConn= null;
        }
        public void onSendFailed(String s) {

        }
        public void onDataReceived(xTcpThread thread,byte[] buff,int clen) {
            int offset = 0;
            int bFound=0;
            int start=0;
            int len = clen;
            byte[] buf = buff;
            if(mLastSgfDl!=null) {
                int delta = mLastSgfDl.len-mLastSgfDl.cur;
                if(clen<delta) {
                    delta=clen;
                }
                System.arraycopy(buff, 0, mLastSgfDl.data, mLastSgfDl.cur, delta);
                mLastSgfDl.cur+=delta;
                offset=delta;
                if(mLastSgfDl.cur>=mLastSgfDl.len) {
                    String data = new String(mLastSgfDl.data);
                    mGoController.AddSgf(mLastSgfDl.id,data);
                    saveSgfToFs(mLastSgfDl.id,data);
                    mLastSgfDl=null;
                    Message message = new Message();
                    message.what = SgfHandler.MSG_GET_SGF;
                    mHandler.sendMessage(message);
                }
            }
            while(offset<len) {
                if(buf[offset]==':') {
                    String type= new String(buf,start,offset-start);
                    offset++;
                    if(type.compareTo("response")==0) {
                        onResponse(buf,len,offset);
                    }
                    if(type.compareTo("notify")==0) {
                        onNotify(buf,len,offset);
                    }
                    bFound++;
                    break;
                }
                offset++;
            }

        }

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
