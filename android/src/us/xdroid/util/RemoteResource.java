/**
 *
 */
package us.xdroid.util;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * We do tasks, get cash, and buy awards.
 *
 * The "award" table stores all available awards that user can buy. This table is
 * downloaded from server with "GETAWARDLIST" cmd
 */
@DatabaseTable(tableName = "resource")
public class RemoteResource {

    private static final String LOG_TAG = "RemoteResourceManager";

    @DatabaseField(canBeNull = false)
    private String type;

	@DatabaseField(id = true) 
    private int id;

    @DatabaseField(canBeNull = false)
    private String url;
	
	@DatabaseField(canBeNull = true)
    private String path;
	
	@DatabaseField(canBeNull = true)
    private int category;	
	
	
	public static final int TYPE_HEAD_IMG = 0;


    public RemoteResource() {
        // OrmLite only		
		path=null;
    }

	public String getUrl() {
        return url;
    }    
    public void setUrl(String url) {
        this.url = url;
    }
	
	public String getPath() {
        return path;
    }    
    public void setPath(String pa) {
        this.path = pa;
    }	
	
	
    public void setType(String tp) {
        this.type = tp;
    }
    public String getType() {
        return type;
    }	

	public void setId(int i){
		this.id=i;
	}
    public int getId() {
        return id;
    }
	
	public void setCategory(int i){
		category=i;
	}
	public int getCategory(){
		return category;
	}

    /* RESOURCE can only be created from input JSON stream !!!
     *
     * RESOURCE={
     		id:1
    	type:drawable
    	url:...
            }
     */
    public static RemoteResource createFromJson(JSONObject jo) {
        try {
            RemoteResource a = new RemoteResource();
            Iterator<String> it = jo.keys();
            while (it.hasNext()) {
                String field = it.next();
                if (field.equals("id"))
                    a.id = jo.getInt(field);
                else if (field.equals("type"))
                    a.type = jo.getString(field);
                else if (field.equals("url"))
                    a.url = jo.getString(field);
                else
                    Log.e(LOG_TAG, "Ignored unknown Award field " + field);
            }
            return a;
        } catch (JSONException e1) {
            Log.e(LOG_TAG, "JSON exception " + e1.getMessage());
        } catch (NumberFormatException e2) {
            Log.e(LOG_TAG, "Number format exception");
        }
        return null;
    }
}
