package com.sureone;


import android.util.DisplayMetrics;
//import net.youmi.android.AdManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.os.Environment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.Locale;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import android.net.Uri;
import 	android.os.AsyncTask;
import android.util.Log;
//import com.tapjoy.TapjoyConnect;
//import com.tapjoy.TapjoyLog;
/*addr:192.168.56.101*/
public class main extends Activity implements View.OnCreateContextMenuListener {

    private static final int STOPSPLASH = 0;
    //time in milliseconds
    private static final long SPLASHTIME = 3000;

    ImageView mLogo=null;
    //handler for splash screen
    private MyHandler splashHandler=null;
    class MyHandler extends Handler {
        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case STOPSPLASH:
                //remove SplashScreen from view
                //mLogo.setVisibility(View.GONE);

                //fadeoutLogo();
				
                if(isDataOK()==true) {
                    xHelper.log("goapp","check update...");																				
                    if(updateNew(app.getDefaultBoot())==false) startView();
                } else {
                    startView();
		}
                break;
            }
            super.handleMessage(msg);
        }

    };
		
    public void fadeoutLogo() {
        //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        //Now Set your animation


    }
    public void startView() {

        User u = mGoController.getMyUser();
        Intent intent=null;
        if(mGoController.isInDesk()) {
            intent = new Intent(this,com.sureone.GoActivity.class);
            if(mDM.heightPixels<=480)
                intent.putExtra("fullscreen","true");
            else
                intent.putExtra("fullscreen","false");
            xHelper.log("goapp","showGoView");
        } else if(mGoController.isInRoom()) {
            intent = new Intent(this,com.sureone.RoomFlowView.class);
        } else {
            intent = new Intent(this,com.sureone.OptionsView.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    String versionName=null;
    int versionCode=0;
    String pkName="com.sureone";
    private ProgressBar mProgress;
    int totalSize=0;
    TextView loadTxt=null;
    DisplayMetrics mDM=null;
    GoController mGoController=null;
    void showProgress() {
        ProgressBar pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);
    }
    void hideProgress() {
        ProgressBar pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.GONE);
    }
		GoApp app = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = GoApp.getInstance();
		app.onCreate(this);
		startService(new Intent(this, MyService.class));

		
        mGoController = app.getGoController();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashview);
		//TapjoyConnect.requestTapjoyConnect(this, "18d3e757-976f-4f26-ba55-0e54e1c8eefc", "kcHbedSwijPuEhCrycfR");
		//TapjoyxHelper.lognableLogging(true);
		
		//com.waps.AppConnect.getInstance(this);		
		//AdManager.init(this,"576c9b0a16279c55", "9839cdaf5975e912", 60, false);
		//net.youmi.android.appoffers.YoumiOffersManager.init(this,"576c9b0a16279c55", "9839cdaf5975e912");
		Configure.chess_scheme = app.getStone();
        mDM=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(mDM);
        splashHandler= new MyHandler();
        TextView verTxt = (TextView)findViewById(R.id.txtVersion);
        loadTxt = (TextView)findViewById(R.id.txtLoading);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setVisibility(View.GONE);
        loadTxt.setVisibility(View.GONE);
        
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            pkName = this.getPackageName();
        } catch(Exception e) {
            e.printStackTrace();
        }
        verTxt.setText(getString(R.string.appnamecn) + " "+versionName);
        setTitle(getString(R.string.appnamecn) + " "+versionName);
	showProgress();
	//startView();
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);

    }


    int mDownloadMode=0; //0 = config ,1 = APK, 2 = AD
    StringBuffer mConfig=null;
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;
    boolean updateNew(String link) {

	xHelper.log("goapp","from:"+link);	
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        //if(mExternalStorageWriteable==true) {
            File folder = new File(Environment.getExternalStorageDirectory () + "/goapp");
            boolean success =true;
            if(!folder.exists()) {
                success = folder.mkdir();
            }
            if (success){
                File verFile = new File(Environment.getExternalStorageDirectory () + "/goapp","goapp.apk");
                if (verFile.exists()) {
                    Log.w("goApp", "File exists!");
                    try {
                        Runtime.getRuntime().exec("rm " + verFile.getPath());
                    } catch( Exception ex ) {
                        xHelper.log("goApp", "Failed to delete");
                        return false;
                    }
                }
            }
            //loadTxt.setVisibility(View.VISIBLE);
            //loadTxt.setText(getString(R.string.checkUpdate));
            xHelper.log("goapp","to download update.conf...");
            mConfig = new StringBuffer();
            DownloadFileInfo dfi = new DownloadFileInfo();
            mDownloadMode=0;
            dfi.remote=link;
            dfi.local=getFilesDir()+"/update.conf";
            DownloadTask task=new DownloadTask();
            task.execute(dfi);
            return true;

    }
    void onDownloadHtmlFinish() {
    }
    void onDownloadApkFinish() {
        xHelper.log("goapp","onDownloadApkFinish");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
            Uri.fromFile(new File (Environment.getExternalStorageDirectory() + "/goapp/" + "goapp.apk")),
            "application/vnd.android.package-archive");
        startActivity(intent);
    }
    String mNotifyHtmlFile=null;
    void downloadNewVersion(String url) {
	hideProgress();
        loadTxt.setText(getString(R.string.download));
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setProgress(0);
        DownloadFileInfo dfi = new DownloadFileInfo();
        mDownloadMode=1;
        if(pkName.equals("com.sureone.go")) url += ".go";
        dfi.remote=url;
        dfi.local=Environment.getExternalStorageDirectory() + "/goapp/goapp.apk";
        DownloadTask task=new DownloadTask();
        task.execute(dfi);
    }
    String mNewUrl=null;
    String mNewInfo=null;
	String mHttpUrl=null;


    String mNewHttpUrl=null;





    void onDownloadConfigFinish() {
        xHelper.log("goapp","onDownloadConfigFinish:"+mConfig);
        byte[] buf = mConfig.toString().getBytes();
        int len = buf.length;
        int offset = 0;
        x_Integer o = new x_Integer(offset);
        int ver = xHelper.getInt(buf, len, o, ',');
        totalSize = xHelper.getInt(buf, len, o, ',');
        mNewUrl = xHelper.getStr(buf,len,o,',');
        mNewInfo = xHelper.getStr(buf,len,o,',');
	String ip = xHelper.getStr(buf,len,o,',');
	app.setIp(ip);
        mHttpUrl = xHelper.getStr(buf,len,o,',');
        mNewHttpUrl = xHelper.getStr(buf,len,o,',');

        GoModel.setPrefString("NEW_HTTP_URL",mNewHttpUrl);
        xHelper.log("goapp","latest="+ver+",me="+versionCode+",ip="+ip);
		us.xdroid.util.HttpConnectionManager.SERVER_URL=mHttpUrl;
				app.setDownload(mNewUrl);
        // if(ver>versionCode && mExternalStorageWriteable==true) {
        if(ver>versionCode && mExternalStorageWriteable==true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String s = getString(R.string.newverfound)+"\n"+mNewInfo;
            builder.setMessage(s)
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    downloadNewVersion(mNewUrl);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startView();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            startView();
        }
    }
    class DownloadTask extends AsyncTask<DownloadFileInfo, Integer, String> {

        private DownloadFileInfo info;
        public static final int ON_FINISH=1;
				public boolean bSuccess=true;
        protected String doInBackground(DownloadFileInfo... dbInfo) {

            int count;
            info = dbInfo[0];
						bSuccess=true;	
            try {

                xHelper.log("goapp","main:download app from:"+dbInfo[0].remote);
                URL url = new URL(dbInfo[0].remote);
                OutputStream output=null;
                InputStream input = new BufferedInputStream(url.openStream());
                output = new FileOutputStream(dbInfo[0].local);

                byte data[] = new byte[1024];
                int total = 0;

                while ((count = input.read(data)) != -1) {

                    if(mDownloadMode==0)
                        mConfig.append(new String(data,0,count));
										else
                    	output.write(data, 0, count);

                    total += count;

                    publishProgress(total);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {	
								bSuccess=false;							
								onNetworkError();
                e.printStackTrace();
            }

            return null;
        }
        int progress=1;
        protected void onProgressUpdate(Integer... total) {
            float perc = (total[0]/(float)totalSize)*(float)100.0;
            int d=(int)perc;
            if(d>100) return;
            mProgress.setProgress((int)perc);
            loadTxt.setText(d+"%");
            xHelper.log("goapp","downloading...."+perc);
        }

        protected void onPostExecute(String s) {
            // dismissDialog(DIALOG_PROGRESS);
            xHelper.log("err", "finish!");
						if(bSuccess==false) return;
            if(mDownloadMode==0) {
                onDownloadConfigFinish();
            } else if(mDownloadMode==1) {
                onDownloadApkFinish();
            } else if(mDownloadMode==2) {
                onDownloadHtmlFinish();
            }
        }
    }

		int iretry = 0;
		void onNetworkError(){
			if(mDownloadMode==0 && iretry==0) {
					xHelper.log("goapp","try second bootstrap");
					iretry++;
					updateNew(app.getSecondBoot());
			}else if(mDownloadMode==0 && iretry==1) {
					iretry++;
					xHelper.log("goapp","try third bootstrap");
					updateNew(app.getThirdBoot());
			}else{
					xHelper.log("goapp","bootstrap failed after 3 retries");
				startView();
			}
		}

    boolean isDataOK() {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        //mobile
        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();

        //wifi
        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
        boolean dataOK=false;
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            dataOK=true;
            //mobile
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            //wifi
            dataOK=true;
            xHelper.log("goapp","is wifi");
        }
        return dataOK;
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        xHelper.log("goapp","onCreateOptionsMenu");
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        xHelper.log("goapp","onPrepareOptionsMenu");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        xHelper.log("goapp","onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart() {
        xHelper.log("goapp","main onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        xHelper.log("goapp","main onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        xHelper.log("goapp","main onStop");
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}


