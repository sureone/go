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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
public class LocalSgfFragment extends BaseFragment {

	View mView = null;
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static LocalSgfFragment newInstance(String content) {
        LocalSgfFragment fragment = new LocalSgfFragment();

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
		View view = inflater.inflate(R.layout.localsgfview, null);
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

	
	@Override
    public void onStop() {
		onHide();
		super.onStop();
	}

	@Override
	public void onResume(){
		
		super.onResume();
		onShow();
	}
	
	@Override
	public void onStart(){
		onShow();
		super.onStart();
	}
	
	
	
    @Override
    public void onPause() {
		
		onHide();
		super.onPause();
	}
	
    public void onHide() {
	}

    public void onShow() {
		fillLocalSgfList();
	}
	
	ViewPager mPager;
	public void setViewPager(ViewPager pager){
		mPager=pager;
	}	
	
    Activity mContainer = null;
    RelativeLayout mContentGroup=null;
    boolean isDebug = false;
    

	
	public void init(Context context,View view){

		mContainer = (Activity)context;
        GoApp app =  GoApp.getInstance();
        mGoController = app.getGoController();
        mGrid = (ListView) mView.findViewById(R.id.sgfList);
    }

	
    public void fillLocalSgfList() {
                xHelper.log("goapp","fillLocalSgfList");
        showLocal();
        if(mAppsAdapter==null) {
            mAppsAdapter = new AppsAdapter(mContainer);
            mGrid.setAdapter(mAppsAdapter);
            mGrid.setOnItemClickListener(mAppsAdapter);
            mGrid.setOnScrollListener(mAppsAdapter);
            mContainer.registerForContextMenu(mGrid);
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
    public boolean onContextItemSelected(MenuItem item) {
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
        Intent intent = new Intent(mContainer,com.sureone.SgfActivity.class);
        intent.putExtra("curSgf",mCurSgf);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContainer.startActivity(intent);
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
                convertView.setTag(holder);

            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.

            String s="";
            SgfHeader h = null;
            int idx = mGoController.getLocalSgfNum()-position-1;
            h = mGoController.getLocalSgfByIndex(idx);
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
            }

            return convertView;

        }


        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ViewHolder holder = (ViewHolder) v.getTag();
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

        if(mConn!=null) {
            xHelper.log("goapp","shutdown the connecton with sgf server");
            mConn.shutdown();
            mConn=null;
        }
        mViewMode = 0;
        mGoController.queryDB();
        if(mAppsAdapter!=null)
            mAppsAdapter.notifyDataSetChanged();
    }

    xTcpThread mConn=null;
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
}
