package com.sureone;
import android.util.Log;

import java.util.Map;

public class xHelper {
    public static int getRankNo(String rank) {
        char[] cs = rank.toCharArray();
        int i=0;
        char l='d';
        int j=0;
        char c;
        if(rank.equals("NR")) return 60;
        while(j<cs.length) {
            c=cs[j];
            if(c>='0' && c<='9') {
                if(j==0)
                    i=c-'0';
                else if(j==1)
                    i=i*10+(c-'0');
            } else if(c=='d' || c=='k') {
                l=c;
                break;
            }
            j++;
        }
        if(l=='d') return 10-i;
        if(l=='k') {
            return i+9;
        }
        return 0;
    }

    public static int getInt(byte[] b,int l,x_Integer o,char t) {
        int count = 0;
        int start = o.v;
        while(o.v<l) {
            if(b[o.v]==t) {
                o.v++;
                String s = new String(b,start,count);
                return Integer.parseInt(s);
            }
            count++;
            o.v++;
        }
        return 0;
    }

    public static String getJson(byte[] b,int l,x_Integer o) {
        int count = 0;
        int start = o.v;
        while(o.v<l) {
            if(b[o.v]=='\r') {


                o.v++;

                if(o.v+2<l && b[o.v]=='\n' && b[o.v+1]=='\r' && b[o.v+2]=='\n'){
                    o.v+=3;
                    String s = new String(b,start,count);
                    return s;
                }else{
                    o.v--;
                }
            }
            count++;
            o.v++;


        }
        return null;
    }
    public static String getStr(byte[] b,int l,x_Integer o,char t) {
        int count = 0;
        int start = o.v;
        String s=null;
        while(o.v<l) {
            if(b[o.v]==t) {
                o.v++;
                s = new String(b,start,count);
                return s;
            }
            count++;
            o.v++;
        }
        return s;
    }
	public static String getStr(String s,char t1,char t2) {
		byte[] b = s.getBytes();
		int l = b.length;
		int start=-1;
		int count=0;
		int i = 0;
        while(i<l) {			
			if(start == -1 && b[i]==t1){
				count=0;
				start=i;				
			}else if(t1!=t2 && b[i]==t1){
				count=0;
				start=i;
			}
			if(start>=0) count++;
			
			if(start>=0 && b[i]==t2){
			    String ss = new String(b,start+1,count-2);
                return ss;         
			}
			i++;
        }
        return null;
    }
    public static String toHex(byte[] raw) {
        StringBuffer hexString = new StringBuffer();
        String s;
        for (int i=0; i<raw.length; i++) {
            s=Integer.toHexString(0xFF & raw[i]);
            if(s.length()==1) {
                s="0"+s;
            }
            hexString.append(s);
        }
        return hexString.toString();
    }
    public static byte hexToByte(char raw) {
        switch (raw) {
        case '0':
            return 0;
        case '1':
            return 1;
        case '2':
            return 2;
        case '3':
            return 3;
        case '4':
            return 4;
        case '5':
            return 5;
        case '6':
            return 6;
        case '7':
            return 7;
        case '8':
            return 8;
        case '9':
            return 9;
        case 'A':
            return 10;
        case 'B':
            return 11;
        case 'C':
            return 12;
        case 'D':
            return 13;
        case 'E':
            return 14;
        case 'F':
            return 15;
        default:
            return 16;
        }
    }
	
	public static void log(String s){
		Log.e("goapp",s);
	}
	
	public static void log(String s1,String s2){
		Log.e(s1,s2);
	}

    public static String s(Map map,String key){
        Object obj = map.get(key);
        if(obj==null) return null;
        if(obj instanceof String){
            if(((String) obj).trim().equals("")) return null;
            return (String)obj;
        }

        return null;
    }
    public static Long l(Map map,String key){
        Object obj = map.get(key);
        if(obj==null) return null;
        if(obj instanceof Long){
            return (Long)obj;
        }
        if(obj instanceof String){
            return Long.valueOf((String)obj);
        }
        if(obj instanceof Integer){
            return (Long)((Integer) obj).longValue();
        }
        return null;
    }

    public static Integer i(Map map,String key){
        Object obj = map.get(key);
        if(obj==null) return null;
        if(obj instanceof Integer){
            return (Integer)obj;
        }
        if(obj instanceof String){
            return Integer.valueOf((String)obj);
        }
        return null;
    }
}
