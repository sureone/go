package com.sureone.com.sureone.web;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 8/4/13
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
// vim: set encoding=utf-8
/*
 * Copyright (C) 2008 Romain Guy
 */

import android.content.Context;
import android.util.Log;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import us.xdroid.util.xUtil;

import org.apache.http.NameValuePair;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;


/**
 * The HTTP Manager handles HTTP interactions with remote server, and maintains
 * HTTP session cookies
 */
public class WebChanner {
    /*
     * FIXME - am I thread safe?
     */
    private static final DefaultHttpClient sClient;
    private static final String LOG_TAG = "HttpConnectionManager";

    public static String SERVER_URL = "";

    private static List<Cookie> cookies;
    private static boolean isLoggedIn = false;

    public static class MyJsonResponse {
        public int ret = -1;
        public Map map = null;
        public Object obj=null;
        public List ary = null;

    }

    protected static void debug(String s){

        Log.d("WebChanner",s);
    }


    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static String readInStream(FileInputStream inStream)
    {
        try
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while((length = inStream.read(buffer)) != -1 )
            {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadCacheUrl(Context context,String url){

        try
        {
            FileInputStream in = context.openFileInput(MD5(url));
            return readInStream(in);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }


    public static <T> T fromJsonToObject(String json, Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return (T) mapper.readValue(json, valueType);
        } catch (JsonParseException e) {
        } catch (JsonMappingException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * java对象转换为json字符串
     * @param object Java对象
     * @return 返回字符串
     */
    public static String fromObjectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
        mapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT_TWO));
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
        } catch (JsonMappingException e) {
        } catch (IOException e) {
        }
        return null;
    }


    /** 格式化时间的string */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT_TWO = "yyyy-MM-dd";





    public static void cacheUrl(Context context,String uurl,String content) {

        String fn = xUtil.MD5(uurl);
        if( content == null )	content = "";

        try
        {
            FileOutputStream fos = context.openFileOutput(fn, Context.MODE_PRIVATE);
            fos.write( content.getBytes() );

            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public static MyJsonResponse doPostJson(Context context,String url, Map obj, boolean iscache) {

        MyJsonResponse resp = new MyJsonResponse();
        do {
            int status;
            StringBuffer jsonResponse = new StringBuffer(1024);
            try {
                status = WebChanner.doPostEntity(url,
                        fromObjectToJson(obj), jsonResponse);
            } catch (Exception e) {
                debug("network error");
                if(iscache){
                    debug("load cache for " + url);
                    String content = loadCacheUrl(context, url + obj.toString());

                    if(content!=null){
                        try{
                            Thread.sleep(200);
                        }catch(Exception ee){
                            ee.printStackTrace();
                        }
                        resp.ret=0;
                        resp.map = fromJsonToObject(content, Map.class);
                        return resp;
                    }
                }
                resp.ret = -1;
                break;
            }

            if (status == HttpStatus.SC_OK) {
                debug("HTTP POST OK");
                if(iscache){
                    debug("cache "+url);
                    cacheUrl(context, url + obj.toString(), jsonResponse.toString());
                }
                resp.map = fromJsonToObject(jsonResponse.toString(), Map.class);
                resp.ret=0;
                return resp;
            } else {
                debug("Failed, status code=" + status);
                if(iscache){
                    String content = loadCacheUrl(context,url+obj.toString());
                    if(content!=null){
                        try{
                            Thread.sleep(200);
                        }catch(Exception ee){
                            ee.printStackTrace();
                        }
                        resp.ret=0;
                        resp.map = fromJsonToObject(content, Map.class);
                        return resp;
                    }
                }
                resp.ret = -1;
                break;
            }
        } while (false);

        return resp;
    }


    public static void setUrl(String url) {
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

        // HttpProtocolParams.setUserAgent(params, "GOAPP/1.1");

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
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
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

                HttpRequest request = (HttpRequest) context
                        .getAttribute(ExecutionContext.HTTP_REQUEST);
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

    private WebChanner() {
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

    public static int doPostEntity(String url,String postEntity,
                                   StringBuffer responseEntityBuffer) throws IOException {
        xUtil.log("POST ENTITY: " + postEntity);

        HttpPost post = getPostInstance(url);
        StringEntity se = new StringEntity(postEntity, HTTP.UTF_8);
        post.setEntity(se);


        HttpResponse response = sClient.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        xUtil.log("RESPONSE CODE: " + statusCode);

        // String responseString = entityToString(response.getEntity());
        String responseString = entityToString(response);
        if (responseString != null) {
            xUtil.log("RESPONSE ENTITY: " + responseString);
            if (responseEntityBuffer != null)
                responseEntityBuffer.replace(0, responseEntityBuffer.length(),
                        responseString);
        }
        return statusCode;
    }

    public static String mEncodeKey = "xxx";

    public static int doPostEntityHH(String url, String postEntity,
                                     StringBuffer responseEntityBuffer) throws IOException {
        xUtil.log("POST URL: " + url + ", ENTITY: " + postEntity);

        HttpPost post = new HttpPost();
        try {
            post.setURI(new URI(url));
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type",
                    "application/x-www-form-urlencoded;charset=utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ��NameValuePair�������Post��
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // ��������
        params.add(new BasicNameValuePair("encodeKey", mEncodeKey));
        params.add(new BasicNameValuePair("param", postEntity));
        HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
        // ��httpRequest
        post.setEntity(httpentity);

        HttpResponse response = sClient.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        xUtil.log("RESPONSE CODE: " + statusCode);

        // String responseString = entityToString(response.getEntity());
        String responseString = entityToString(response);
        if (responseString != null) {
            xUtil.log("RESPONSE ENTITY: " + responseString);
            if (responseEntityBuffer != null)
                responseEntityBuffer.replace(0, responseEntityBuffer.length(),
                        responseString);
        }
        return statusCode;
    }

    private static HttpPost getPostInstance(String url) {
        HttpPost post = new HttpPost();
        try {
            post.setURI(new URI(url));
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json;charset=utf-8");
            // post.setHeader("Host", "weiqi188.sinaapp.com");

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            post = null;
        }

        return post;
    }

    // private static String entityToString(HttpEntity entity) throws
    // IOException {
    private static String entityToString(HttpResponse response)
            throws IOException {
        // String s = null;
        // if (entity != null) {
        // ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        //
        // try {
        // entity.writeTo(baos);
        // s = baos.toString("UTF-8");
        // }
        // finally {
        // baos.close();
        // }
        // }
        // return s;

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent(), "UTF-8"));
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

