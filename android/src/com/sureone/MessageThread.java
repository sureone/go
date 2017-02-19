package com.sureone;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageThread {
    public int id;
	public int rid;
	public int uid;
	public int rcount;
    public String sn;
	public String uname;
	public String content;
	public String cdate;

    public static MessageThread createFromJson(JSONObject jo) {
        try {
            MessageThread a = new MessageThread();
            Iterator<String> it = jo.keys();
            while (it.hasNext()) {
                String field = it.next();
                if (field.equals("id"))
                    a.id = jo.getInt(field);
                else if (field.equals("rid"))
                    a.rid = jo.getInt(field);
                else if (field.equals("uid"))
                    a.uid = jo.getInt(field);
                else if (field.equals("rcount"))
                    a.rcount = jo.getInt(field);					
                else if (field.equals("uname"))
                    a.uname = jo.getString(field);
                else if (field.equals("content"))
                    a.content = jo.getString(field);
                else if (field.equals("cdate"))
                    a.cdate = jo.getString(field);
                else if (field.equals("sn"))
                    a.sn = jo.getString(field);
                else
                    xHelper.log("Ignored unknown Award field " + field);
            }
            return a;
        } catch (JSONException e1) {
            xHelper.log("JSON exception " + e1.getMessage());
        } catch (NumberFormatException e2) {
            xHelper.log("Number format exception");
        }
        return null;
    }	
	
}
