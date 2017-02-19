package us.xdroid.util;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 6/29/13
 * Time: 8:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class MapHelper {
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
            return Long.valueOf((Integer)obj);
        }
        return null;
    }

    public static Integer i(Map map,Integer key){
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


    public static Object recursionFind(Map map,String key,Class theClass){
        for (Object o : map.keySet()) {

            Object value = map.get(o);

            if(o.equals(key) && value.getClass().equals(theClass)){

                return value;

            }

            if(value.getClass().equals(ArrayList.class) ){

                List list = (List)map.get(o);

                for (Object o1 : list) {

                    if(o1.getClass().equals(LinkedHashMap.class)){
                        Object result = recursionFind((Map)o1,key,theClass);
                        if(result!=null){
                            return result;
                        }
                    }
                }
            }else if(value.getClass().equals(LinkedHashMap.class)){
                Object result = recursionFind((Map)value,key,theClass);
                if(result!=null){
                    return result;
                }
            }


        }

        return null;

    }


}
