package com.sureone;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
public class SplashScreen extends Activity {
    private static final int STOPSPLASH = 0;
    //time in milliseconds
    private static final long SPLASHTIME = 10000;
    ImageView mLogo=null;
    Intent mOptionView = null;
    //handler for splash screen
    private Handler splashHandler = new Handler() {
        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case STOPSPLASH:
                //remove SplashScreen from view

                startActivity(mOptionView);
                break;
            }
            super.handleMessage(msg);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        mLogo=(ImageView) findViewById(R.id.logoImage);
        //mLogo.setImageResource(R.drawable.siyuan_2);
        if(mOptionView==null)
            mOptionView = new Intent(this,com.sureone.main.class);
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }
}
