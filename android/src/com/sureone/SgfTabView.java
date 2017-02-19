package com.sureone;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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
public class SgfTabView extends Activity {
    private ImageButton mBtnLocalSGF;
    private ImageButton mBtnRemoteSGF;
    Button mBtnSearch = null;
    EditText mSearchTxt = null;

    Activity mContainer = null;
    RelativeLayout mContentGroup=null;
    LinearLayout mSearchGroup=null;
    boolean isDebug = false;
    @Override
    public void onCreate(Bundle parent) {
        // TODO Auto-generated method stub
        super.onCreate(parent);
        GoApp app =  GoApp.getInstance();
        mGoController = app.getGoController();
        mContainer = this;
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.sgftabview);
        mHandler = new SgfHandler();
        //super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setTitle(getString(R.string.menusgf));

        mSearchGroup=(LinearLayout)findViewById(R.id.searchBox);
        mSearchGroup.setVisibility(View.GONE);

        mBtnLocalSGF=(ImageButton) findViewById(R.id.BtnLocalSgf);
        mBtnRemoteSGF=(ImageButton) findViewById(R.id.BtnRemoteSgf);
        isDebug = getResources().getBoolean(R.bool.debug);

        if(Locale.getDefault().equals(Locale.CHINESE) ||
                Locale.getDefault().equals(Locale.CHINA)) {
            mBtnLocalSGF.setImageResource(R.drawable.localblack);

        } else {
            mBtnLocalSGF.setImageResource(R.drawable.localblacken);

        }

        if(Locale.getDefault().equals(Locale.CHINESE) ||
                Locale.getDefault().equals(Locale.CHINA)) {
            mBtnRemoteSGF.setImageResource(R.drawable.downloadblack);
        } else {
            mBtnRemoteSGF.setImageResource(R.drawable.downloadblacken);
        }
        evtLis = new BtnEvtListener();
        mBtnLocalSGF.setOnClickListener(evtLis);
        mBtnRemoteSGF.setOnClickListener(evtLis);
        mBtnSearch = (Button)findViewById(R.id.btnSearch);
        mBtnSearch.setOnClickListener(evtLis);
        mSearchTxt = (EditText)findViewById(R.id.txtSearch);



        mGrid = (ListView) findViewById(R.id.sgfList);
        fillLocalRgfList();




    }
    void onBtnSearch() {
        String s = mSearchTxt.getText().toString().trim();
        if(s.length()>0) {
            mSgfData.clear();
            mStartIdx=0;
            searchSgf(s);
        }
    }

    public void fillLocalRgfList() {
        showLocal();
        if(mAppsAdapter==null) {
            mAppsAdapter = new AppsAdapter(this);
            mGrid.setAdapter(mAppsAdapter);
            mGrid.setOnItemClickListener(mAppsAdapter);
            mGrid.setOnScrollListener(mAppsAdapter);
            registerForContextMenu(mGrid);
            //make listview transparent when scroll it.
            mGrid.setCacheColorHint(0);
        }
    }


    public void delSgf(int pos) {
        if(pos>=0) {
            mGoController.delSgf(pos);
        }
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int menuItemIndex = item.getItemId();
            if(menuItemIndex==0) { //delete
                xHelper.log("goapp","onMenuItemSelected:"+info.position);
                delSgf(info.position);
                if(mAppsAdapter!=null)
                    mAppsAdapter.notifyDataSetChanged();
            }
        } catch (ClassCastException e) {
            
            return false;
        }
        return true;

    }
    int mSelectedPos=-1;

    public GoController mGoController = null;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.sgfList && mViewMode==0) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(mContainer.getString(R.string.SgfMenu));
            /*
            	String[] menuItems = getResources().mContainer.getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
              menu.add(Menu.NONE, i, i, menuItems[i]);
            }
            	*/
            menu.add(Menu.NONE, 0, 0, mContainer.getString(R.string.delete));
        }
    }

    ListView mGrid = null;
    AppsAdapter mAppsAdapter = null;

    String mCurSgf=null;
    void onEnterSGF(int id) {
        //gotoSgfActivity
        Intent intent = new Intent(this,com.sureone.SgfActivity.class);
        intent.putExtra("curSgf",mCurSgf);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);


    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //mViewSwitch.backToOptionsView();
            //return true;
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    void getRemoteSgf(int id) {
        if(mGoController.getLocalSgf(id)==null) {
            showWaitingDialog(R.string.savesgftolocal);
            String str = "request:get\r\n"+
                         "type:sgf\r\n"+
                         "id:"+id+"\r\n\r\n";
            mConn.sendData(str);
        }
    }
    void LoadMoreSgf() {
        mStartIdx=mSgfData.size();
        mSearchKey=mSearchTxt.getText().toString().trim();
        if(mSearchKey!=null && mSearchKey.length()>0) {
            searchSgf(mSearchKey);
        } else {
            LoadSgf();
        }
    }
    int mViewMode=0;
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
            } else {
                int idx = mGoController.getLocalSgfNum()-position-1;
                h = mGoController.getLocalSgfByIndex(idx);
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
            if(mViewMode==0) {
                if(h.isnew==1) {
                    holder.icon.setImageResource(R.drawable.isnew);
                } else {
                    holder.icon.setImageResource(R.drawable.isold);
                }
            } else {
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
            int idx = mGoController.getLocalSgfNum()-position-1;
            SgfHeader h = mGoController.getLocalSgfByIndex(idx);

            h.isnew=0;

            if(h.isnew==1) {
                holder.icon.setImageResource(R.drawable.isnew);
            } else {
                holder.icon.setImageResource(R.drawable.isold);
            }
            mGoController.updateSgfNew(h);
            String vv =mGoController.getLocalSgf(h.ID);
            if(vv.substring(0,3).compareTo("sgf")==0)
                mCurSgf=readSgfFromFs(h.ID);
            else
                mCurSgf=vv;

            onEnterSGF(h.ID);
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
            if(mViewMode==1) {
                if(mSgfData==null ) return 0;
                return mSgfData.size();
            }
            return mGoController.getLocalSgfNum();
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

    void showLocal() {
        mSearchGroup.setVisibility(View.GONE);

        if(mConn!=null) {
            xHelper.log("goapp","shutdown the connecton with sgf server");
            mConn.shutdown();
            mConn=null;
        }
        mViewMode = 0;
        mGoController.queryDB();



        if(Locale.getDefault().equals(Locale.CHINESE) ||
                Locale.getDefault().equals(Locale.CHINA)) {
            mBtnLocalSGF.setImageResource(R.drawable.localpressed);

        } else {
            mBtnLocalSGF.setImageResource(R.drawable.localpresseden);

        }
        if(Locale.getDefault().equals(Locale.CHINESE) ||
                Locale.getDefault().equals(Locale.CHINA)) {
            mBtnRemoteSGF.setImageResource(R.drawable.downloadblack);

        } else {
            mBtnRemoteSGF.setImageResource(R.drawable.downloadblacken);

        }
        if(mAppsAdapter!=null)
            mAppsAdapter.notifyDataSetChanged();
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
        mConn.sendData(str);
    }
    EvtListener tcpevtLis = null;
    class EvtListener implements xTcpEventListener {
        public void onConnected(xTcpThread thread) {
            LoadSgf();

        }
        public void onConnectTimeOut(xTcpThread thread) {

        }
        public void onDisconnected(xTcpThread thread) {

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

    void hideSearchBoxAnimate() {
        //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        //Now Set your animation
        Animation fadeInAnimation=null;
        fadeInAnimation =  AnimationHelper.outToBottomAnimation(200);
        fadeInAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mSearchGroup.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                mSearchGroup.setVisibility(View.VISIBLE);
            }
        });
        mSearchGroup.startAnimation(fadeInAnimation );
    }
    void showSearchBoxAnimate() {
        //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        //Now Set your animation
        Animation fadeInAnimation=null;
        fadeInAnimation =  AnimationHelper.inFromBottomAnimation(200);
        fadeInAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mSearchGroup.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                mSearchGroup.setVisibility(View.VISIBLE);
            }
        });
        mSearchGroup.startAnimation(fadeInAnimation );
    }
    void showRemote() {
        //unregisterForContextMenu(mGrid);

        mSearchGroup.setVisibility(View.VISIBLE);
        mViewMode = 1;

        if(Locale.getDefault().equals(Locale.CHINESE) ||
                Locale.getDefault().equals(Locale.CHINA)) {
            mBtnLocalSGF.setImageResource(R.drawable.localblack);

        } else {
            mBtnLocalSGF.setImageResource(R.drawable.localblacken);

        }
        if(Locale.getDefault().equals(Locale.CHINESE) ||
                Locale.getDefault().equals(Locale.CHINA)) {
            mBtnRemoteSGF.setImageResource(R.drawable.downloadpressed);

        } else {
            mBtnRemoteSGF.setImageResource(R.drawable.downloadpresseden);

        }
        if(mAppsAdapter!=null)
            mAppsAdapter.notifyDataSetChanged();

        connectToServer();
    }
    BtnEvtListener evtLis=null;
    int lastView=R.id.BtnLocalSgf;
    class BtnEvtListener implements OnClickListener {

        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.BtnLocalSgf:
                showLocal();
                lastView=R.id.BtnLocalSgf;
                break;
            case R.id.BtnRemoteSgf:
                //onEnterSGF();
                showRemote();
                lastView=R.id.BtnRemoteSgf;
                break;
            case R.id.btnSearch:
                onBtnSearch();
                break;
            }
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

}
