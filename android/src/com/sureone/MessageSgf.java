package com.sureone;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageSgf {
    public int id;
	public String name;
    public String black;
	public String sgf;
	public String white;

    public String result;
	public String cdate;

    public static MessageSgf createFromJson(JSONObject jo) {
        try {
            MessageSgf a = new MessageSgf();
            Iterator<String> it = jo.keys();
            while (it.hasNext()) {
                String field = it.next();
                if (field.equals("id"))
                    a.id = jo.getInt(field);
                else if (field.equals("name"))
                    a.name = jo.getString(field);
                else if (field.equals("black"))
                    a.black = jo.getString(field);
                else if (field.equals("white"))
                    a.white = jo.getString(field);
                else if (field.equals("result"))
                    a.result = jo.getString(field);
                else if (field.equals("cdate"))
                    a.cdate = jo.getString(field);
                else if (field.equals("sgf"))
                    a.sgf = jo.getString(field);
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
