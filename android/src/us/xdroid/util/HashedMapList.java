package us.xdroid.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 11/3/13
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class HashedMapList{
    LinkedList<Map> list;
    Map<Object,Map> hashedMap;
    public HashedMapList(){
        list = new LinkedList<Map>();
        hashedMap=new HashMap<Object, Map>();
    }

    public void addLast(Map map,String key){

        if(hashedMap.containsKey(map.get(key))==false){

            list.addLast(map);
            hashedMap.put(map.get(key),map);

        }

    }

    public void addFirst(Map map,String key){

        if(hashedMap.containsKey(map.get(key))==false){

            list.addFirst(map);

        }

    }

    public int size(){
        return list.size();
    }

    public Map get(int idx){

        return list.get(idx);
    }

}