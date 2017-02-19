package us.xdroid.util;
import 	android.widget.ScrollView;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.sureone.R;
public class WebViewActivity extends BaseActivity {
	private WebView webView;
	private Intent intent = null;
	public static WebViewActivity webInstance = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		ScrollView mainView = new ScrollView(this);
		webView  = new WebView(this);
		mainView.addView(webView); 
		setContentView(mainView);
		webInstance = this;
		mContext = getApplicationContext();
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode( WebSettings.LOAD_NO_CACHE );
        
        webView.setOnTouchListener(new OnTouchListener()
        {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				webView.requestFocus();
				return false;
			}
        });
        
		intent = this.getIntent();
		if(!intent.equals(null))
		{
			Bundle b=intent.getExtras();
		    if(b!=null&&b.containsKey("url"))
		    {  
		    	webView.loadUrl(b.getString("url"));
		    	webView.setWebChromeClient(new WebChromeClient() {            
		    		  public void onProgressChanged(WebView view, int progress)               
		    		  {                   
		    			  setTitle(getString(R.string.loadauth) + progress + "%");
		    			  setProgress(progress * 100);
		    		  }
		    	});
		    }
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	

    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {	
		if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 )
		{
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
