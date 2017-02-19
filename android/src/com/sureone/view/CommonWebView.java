package com.sureone.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sureone.R;
import com.sureone.base.BaseActivity;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 8/18/13
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonWebView extends BaseActivity{


    WebView mWebView;
    String mUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();


        setContentView(R.layout.commonwebview);

        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        mUrl = url;
        //mUrl = "http://dev.njhhsoft.com:9999/activeschool/apps/web/freeclassroom/list.html";

        setMyTitle(title);


        mWebView = (WebView)this.findViewById(R.id.webview);

        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);


        mWebView.loadUrl(mUrl);


        final Activity myActivity = this;
        mWebView.setWebViewClient(new MyWebViewClient());

    }


    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {



            if( url.startsWith("http:") || url.startsWith("https:") ) {
                return false;
            }

            if(url.startsWith("mailto:")){
                Uri sms_uri = Uri.parse(url);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"));
                //sms_intent.setType("message/rfc822");
                String subject = new String(sms_uri.getQuery().split("\\=")[1]);
                subject=subject.substring(0, subject.indexOf("&body"));
                subject.trim();
                sms_intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                sms_intent.putExtra(Intent.EXTRA_TEXT, sms_uri.getQuery().split("\\=")[2]);

                //startActivity(sms_intent.createChooser(sms_intent, "Choose an Email client :"));
                startActivity(sms_intent);
                return true;

            }else if(url.startsWith("sms:")){
                //String[] ss = Uri.parse(url).toString().split("\\=");
                Uri sms_uri = Uri.parse(url);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"));

                sms_intent.putExtra("sms_body", sms_uri.getQuery().split("\\=")[1]);
                startActivity(sms_intent);
                return true;
            }

            // Otherwise allow the OS to handle it
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //startActivity( intent );
            return false;
        }
        public void onPageFinished(WebView view, String url){
            //Make the bar disappear after URL is loaded, and changes string to Loading...

            // Return the app name after finish loading


        }
    }


}
