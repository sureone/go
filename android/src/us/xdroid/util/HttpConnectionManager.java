/*
 * Copyright (C) 2008 Romain Guy
 */
package us.xdroid.util;
import us.xdroid.util.xUtil;

import org.apache.http.params.HttpParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.HttpResponse;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.NoHttpResponseException;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

import java.net.SocketTimeoutException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * The HTTP Manager handles HTTP interactions with remote server, and maintains
 * HTTP session cookies
 */
public class HttpConnectionManager {
    /*
     * FIXME - am I thread safe?
     */
    private static final DefaultHttpClient sClient;
    private static final String LOG_TAG = "HttpConnectionManager";

    public static String SERVER_URL = "http://weiqi188.sinaapp.com/index.php/appentry";

    // [version: 0][name: saeut][value:
    // 219.142.125.161.1326768416547744][domain: xwtech.sinaapp.com][path:
    // /][expiry: Thu Nov 25 10:49:49 �������α�׼ʱ��+0800 2021]
    // [version: 0][name: PHPSESSID][value:
    // 98937a352a6d6a32a01c026e9b3185c4][domain: xwtech.sinaapp.com][path:
    // /][expiry: null]
    private static List<Cookie> cookies;
    private static boolean isLoggedIn = false;

		public static void setUrl(String url){
			SERVER_URL = url;
		}
    static {
        final HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setHttpElementCharset(params, "UTF-8");

        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        HttpClientParams.setRedirecting(params, false);

        HttpProtocolParams.setUserAgent(params, "GOAPP/1.1");

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory
                                           .getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory
                                           .getSocketFactory(), 443));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(
            params, schemeRegistry);
        sClient = new DefaultHttpClient(manager, params);
        CookieStore cookieStore = new BasicCookieStore();
        sClient.setCookieStore(cookieStore);

        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                xUtil.log("HTTP Retry requested");
                if (executionCount >= 10) {
                    Log.e(LOG_TAG, "executionCount>=10, returning false.");
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    Log.d(LOG_TAG, "NoHttpResponseException, returning true");
                    return true;
                }

                if (exception instanceof SocketTimeoutException) {
                    Log.d(LOG_TAG, "SocketTimeoutException, returning true");
                    return true;
                }

                HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    // Retry if the request is considered idempotent
                    Log.e(LOG_TAG, "Idempotent=true, returning true...");
                    return true;
                }
                Log.e(LOG_TAG, "Returning false");
                return false;
            }
        };
        sClient.setHttpRequestRetryHandler(myRetryHandler);
    }

    private HttpConnectionManager() {
        // no instantiation
    }

    //
    // public static HttpResponse execute(HttpHead head) throws IOException {
    // return sClient.execute(head);
    // }
    //
    // public static HttpResponse execute(HttpHost host, HttpGet get) throws
    // IOException {
    // return sClient.execute(host, get);
    // }
    //
    public static HttpResponse execute(HttpGet get) throws IOException {
        return sClient.execute(get);
    }


    public static int doPostEntity(String postEntity, StringBuffer responseEntityBuffer) throws IOException {
        xUtil.log("POST ENTITY: " + postEntity);

        HttpPost post = getPostInstance();
        StringEntity se = new StringEntity(postEntity, HTTP.UTF_8);
        post.setEntity(se);

        HttpResponse response = sClient.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        xUtil.log("RESPONSE CODE: " + statusCode);

//        String responseString = entityToString(response.getEntity());
        String responseString = entityToString(response);
        if (responseString != null) {
            xUtil.log("RESPONSE ENTITY: " + responseString);
            if (responseEntityBuffer != null)
                responseEntityBuffer.replace(0, responseEntityBuffer.length(), responseString);
        }
        return statusCode;
    }

    private static HttpPost getPostInstance() {
        HttpPost post = new HttpPost();
        try {
            post.setURI(new URI(SERVER_URL));
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json;charset=utf-8");
            post.setHeader("Host", "weiqi188.sinaapp.com");

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            post = null;
        }

        return post;
    }

    //private static String entityToString(HttpEntity entity) throws IOException {
    private static String entityToString(HttpResponse response) throws IOException {
//        String s = null;
//        if (entity != null) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
//
//            try {
//                entity.writeTo(baos);
//                s = baos.toString("UTF-8");
//            }
//            finally {
//                baos.close();
//            }
//        }
//        return s;

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    public static DefaultHttpClient getHttpClient() {
        return sClient;
    }

    public static byte[] downloadBitmap(String url) {
        xUtil.log("downloadBitmap++, url=" + url);

        try {
            HttpGet request = new HttpGet(url.toString());
            HttpResponse response = execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toByteArray(entity);
            } else
                Log.d(LOG_TAG, "Download failed, HTTP response code "
                      + statusCode + " - " + statusLine.getReasonPhrase());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Network IO error");
        }
        return null;
    }
}

