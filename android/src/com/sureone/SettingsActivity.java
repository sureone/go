
package com.sureone;
import java.io.UnsupportedEncodingException;
import 	android.widget.RadioButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.AbsListView.OnScrollListener;
import android.view.Window;
import android.view.WindowManager;

public class SettingsActivity extends Activity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        }
		
		RadioGroup rg = (RadioGroup) findViewById(R.id.stoneStyle);
		int idx=5;
		int id = rg.getCheckedRadioButtonId();
		if(id==R.id.st0) idx=0;
		if(id==R.id.st1) idx=1;
		if(id==R.id.st2) idx=2;
		if(id==R.id.st3) idx=3;
		if(id==R.id.st4) idx=4;
		if(id==R.id.st5) idx=5;
		Configure.chess_scheme=idx;
		GoApp app =  GoApp.getInstance();
		app.setStone(idx);
        return super.onKeyDown(keyCode, event);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settingsview);
        com.viewpagerindicator.NormalTitle nt = (com.viewpagerindicator.NormalTitle) findViewById(R.id.ntTitle);        
        if(nt!=null) nt.setTitle(getString(R.string.menusettings));
        final GoApp app =  GoApp.getInstance();
        final CheckBox checkbox = (CheckBox) findViewById(R.id.rb_silent);
        checkbox.setChecked(app.getSilentMode());
        checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    app.setSilentMode(true);
                } else {
                    app.setSilentMode(false);
                }
            }
        });
        final CheckBox checkboxTouch = (CheckBox) findViewById(R.id.rb_cursor);
        checkboxTouch.setChecked(app.getTouchMode());
        checkboxTouch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    app.setTouchMode(true);
                } else {
                    app.setTouchMode(false);
                }
            }
        });
		
		final CheckBox checkboxIcon = (CheckBox) findViewById(R.id.rb_icon);
        checkboxIcon.setChecked(app.getIconHide());
        checkboxIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    app.setIconHide(true);
                } else {
                    app.setIconHide(false);
                }
            }
        });

		int idx = app.getStone();
		
		RadioButton rd = (RadioButton) findViewById(R.id.st0);
		if(idx==0) rd = (RadioButton) findViewById(R.id.st0);
		if(idx==1) rd = (RadioButton) findViewById(R.id.st1);
		if(idx==2) rd = (RadioButton) findViewById(R.id.st2);
		if(idx==3) rd = (RadioButton) findViewById(R.id.st3);
		if(idx==4) rd = (RadioButton) findViewById(R.id.st4);
		if(idx==5) rd = (RadioButton) findViewById(R.id.st5);
		rd.setChecked(true);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }
}

