package us.xdroid.util;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.security.MessageDigest;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

import android.net.Uri;

public class xUtil {
    public static void log(String s) {
        Log.e("goapp",s);
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

    public static int downloadFile(String uurl,String local) {
        log("downloading:"+uurl+" to:"+local);
        try {
            URL url = new URL(uurl);
            OutputStream output=null;
            InputStream input = new BufferedInputStream(url.openStream());
            output = new FileOutputStream(local);
            byte data[] = new byte[1024];
            int total = 0;
            int count =0;
            while ((count =input.read(data)) != -1) {
                total+=count;
                output.write(data, 0, count);
            }
            log(total+"bytes downloaded");

            output.flush();
            output.close();
            input.close();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getDeviceID(Context context){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

}

