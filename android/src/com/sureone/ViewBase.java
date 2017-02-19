package com.sureone;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.util.Log;
import android.view.View;
import android.content.Context;
import us.xdroid.util.xUtil;
import android.app.Dialog;
public class ViewBase {
    protected Context mContext=null;
    View mView=null;
    void init(Context context,View view) {
        mContext=context;
        mView=view;
    }
    void showView() {
    }
	public View getView(){
		return mView;
	}
    void onHide() {
		xUtil.log("ViewBase:onHide");
	}
    void onShow() {
		xUtil.log("ViewBase:onShow");

	}
    void onPause() {
	}
    void onStop() {
	}
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

		protected Dialog onCreateDialog(int id) {
				return null;
		}
    void onResume() {
		xUtil.log("ViewBase:onResume");
        showView();
    }
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return true;
	}
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
	}
}
