package com.sureone;

/*
import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.Oauth2AccessToken;
import com.weibo.net.WeiboException;
*/

import android.widget.Toast;

import android.graphics.Color;
import android.view.ViewGroup;
import java.util.Locale;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.content.Context;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.Window;
import android.widget.LinearLayout;
import android.view.WindowManager;
//import cn.domob.android.ads.DomobAdManager;
import com.sureone.igs.IgsController;
//import com.tapjoy.TapjoyConnect;
import com.viewpagerindicator.NormalTitle;
public class OptionsView extends Activity {
    private ImageButton mBtnEnterPlay;
    private ImageButton mBtnEnterSGF;
    private ImageButton mBtnEnterBoard;
    private ImageButton mBtnEnterSettings;
    private ImageButton mBtnEnterIgs;


    public xTcpThread mConn;

    public static final String PREFS_NAME = "settings";
    /** Called when the activity is first created. */
    boolean mWaitingToDeskList = false;

    RelativeLayout mButtonGroup;


    public static final int MENU_PLAY = 0;

    public static final int MENU_SGF = 1;
    public static final int MENU_BOARD = 2;
    public static final int MENU_SETTINGS = 3;
    //public static final int MENU_IGS = 4;
    //public static final int MENU_ADAPP = 5;
    // public static final int MENU_MATCH = 3;
    public static final int MENU_MAX=4;
    class MyMenuItem {
        MyMenuItem(int lid,int did,int mid,int hid) {
            labelId=lid;
			hint=hid;
            drawId=did;
            menuId=mid;
        }
		int hint;
        int labelId;
        int drawId;
        int menuId;
    };

    MyMenuItem[] mMenuItems = null;


    public int mShowMode = 0; //0 means enterIn, 1 means backIn
    GridView mGrid;
    AppsAdapter mAppsAdapter=null;
    GoController mGoController=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoApp app =  GoApp.getInstance();
        mGoController = app.getGoController();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mMenuItems = new MyMenuItem[MENU_MAX];
        mMenuItems[MENU_PLAY]=new MyMenuItem(R.string.menuplay,R.drawable.menuplay,MENU_PLAY,R.string.menuplayhint);
        //mMenuItems[MENU_IGS]=new MyMenuItem(R.string.menuigs,R.drawable.menuigs,MENU_IGS,R.string.menuigshint);
        mMenuItems[MENU_SGF]=new MyMenuItem(R.string.menusgf,R.drawable.menusgf,MENU_SGF,R.string.menusgfhint);
        mMenuItems[MENU_BOARD]=new MyMenuItem(R.string.menuboard,R.drawable.menuboard,MENU_BOARD,R.string.menuboardhint);
        // mMenuItems[MENU_MATCH]=new MyMenuItem(R.string.menumatch,R.drawable.menuboard,MENU_MATCH,R.string.menumatchhint);
        mMenuItems[MENU_SETTINGS]=new MyMenuItem(R.string.menusettings,R.drawable.menusettings,MENU_SETTINGS,R.string.menusettingshint);
        //mMenuItems[MENU_ADAPP]=new MyMenuItem(R.string.menuadapp,R.drawable.menuadapp,MENU_ADAPP,R.string.menuadapphint);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.optionsview);
		
		NormalTitle nt = (NormalTitle) findViewById(R.id.ntTitle);
        
        if(nt!=null) nt.setTitle(getString(R.string.mainmenu));
        mGrid = (GridView) findViewById(R.id.menuGrid);
        mWaitingToDeskList=false;
        mAppsAdapter = new AppsAdapter((Activity)this);
        mGrid.setAdapter(mAppsAdapter);
        mGrid.setOnItemClickListener(mAppsAdapter);

		
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String email = settings.getString("email", "");
        String passwd = settings.getString("pin", "");

		// com.uucun.adsdk.UUAppConnect.getInstance(this).initSdk();

			//LinearLayout container =(LinearLayout)findViewById(R.id.adArea);
			//com.uucun.adsdk.UUAppConnect.getInstance(this).showBanner(container , 20);
		//doSinaAuth();
		//checkSinaAuth();

		
		//LinearLayout container =(LinearLayout)findViewById(R.id.miniAdLinearLayout);
		//new com.waps.AdView(this,container).DisplayAd();
		
		
    }


	private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://OptionsView";
    private static final String FROM = "xweibo";
    // set app key and secret!!!!!!!
    private static final String CONSUMER_KEY = "3918458294";
    private static final String CONSUMER_SECRET = "10e678687fc4b2f508e565e8206136de";



    @Override
    public void onStart() {

        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {						            					
            GoApp app =  GoApp.getInstance();
			xTcpThread conn = mGoController.getConnection();
        	if(conn!=null) conn.shutdown();
	     		app.stopMyService();
			System.gc();
			this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());			
			//com.waps.AppConnect.getInstance(this).finalize();
			// com.uucun.adsdk.UUAppConnect.getInstance(this).exitSdk();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


	//final MyPointsManager mpm = new MyPointsManager();
	void onEnterAd(){
		//net.youmi.android.appoffers.YoumiOffersManager.showOffers(this,0,mpm);
		//TapjoyConnect.getTapjoyConnectInstance().showOffers();
		//com.uucun.adsdk.UUAppConnect.getInstance(this).showOffers();
		//com.waps.AppConnect.getInstance(this).showOffers(this);
		// com.uucun.adsdk.UUAppConnect.getInstance(this).showOffers();
	}
    void onEnterPlay() {
        Intent intent=null;
        if(mGoController.isOffline()==true) {
            intent = new Intent(this,com.sureone.EntryView.class);
        } else if (mGoController.isInRoom()==true) {
            intent = new Intent(this,com.sureone.RoomFlowView.class);
            intent.putExtra("login","ok");
        } else {
            intent = new Intent(this,com.sureone.GoActivity.class);
            intent.putExtra("view","normal");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);


    }

    void onEnterSgfTabView() {
        Intent intent = new Intent(this,com.sureone.SgfFlowView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        //
    }
    void onEnterBoard() {
        Intent intent = new Intent(this,com.sureone.WeiboView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);	

    }

    void onEnterMatch() {
        Intent intent = new Intent(this,com.sureone.match.MatchHomeView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
    void onEnterSettings() {
        Intent	intent = new Intent(this,com.sureone.SettingsActivity.class);
		    //Intent	intent = new Intent(this,com.sureone.InviteDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    AlertDialog showInfoDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(id)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    void onEnterIgs() {
        Intent	intent = new Intent(this,com.sureone.igs.IgsLoginView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    public class AppsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
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
                convertView = mInflater.inflate(R.layout.menugriditem, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.txtItem=(TextView) convertView.findViewById(R.id.txtItem);
                holder.txtHint=(TextView) convertView.findViewById(R.id.txtHint);
                holder.icon=(ImageView) convertView.findViewById(R.id.imageItem);
                convertView.setTag(holder);


            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.


                //xHelper.log("DeskListActivity:","position="+position);
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            MyMenuItem item = mMenuItems[position];

            //holder.icon.setImageResource(item.drawId);
            //holder.icon.setImageResource(R.drawable.desk);
            holder.txtHint.setText(getString(item.hint));
            holder.txtItem.setText(getString(item.labelId));
            holder.id=item.menuId;
            return convertView;
        }
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            MyMenuItem item = mMenuItems[position];
            switch(item.menuId) {
            case MENU_PLAY:
                onEnterPlay();
                break;
//            case MENU_IGS:
//                onEnterIgs();
//                break;
            case MENU_SGF:
                onEnterSgfTabView();
                break;
            case MENU_BOARD:
                onEnterBoard();
                break;
            case MENU_SETTINGS:
                onEnterSettings();
                break;
//            case MENU_ADAPP:
//				onEnterAd();
//                break;
                // case MENU_MATCH:
                //     onEnterMatch();
            }

        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!view.isInTouchMode()) {
                return false;
            }

            return true;
        }

        public final int getCount() {
            //return xGlobal.getSystemInfo().mGames.size();
            return mMenuItems.length;
        }

        public final Object getItem(int position) {
            //return (Object)(xGlobal.getSystemInfo().mGames.get(position));
            return null;
        }
        public final long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            TextView txtItem;
            TextView txtHint;
            ImageView icon;
            int id;
        }
    }
}
