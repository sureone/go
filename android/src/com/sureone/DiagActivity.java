
package com.sureone;
import java.io.UnsupportedEncodingException;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class DiagActivity extends Activity {
    xTcpThread mConn=null;
    TextView mTxtDump = null;
    Button mBtnDump=null;
    Button mBtnStat=null;
    Button mBtnReboot=null;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        GoApp app = GoApp.getInstance();
        mGoController.setHandler(mHandler);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        GoApp app =  GoApp.getInstance();
        mGoController.setHandler(mHandler);
        super.onResume();
        xHelper.log("goapp","BoardActivity onResume");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        GoApp app =  GoApp.getInstance();
        mGoController.setHandler(mHandler);
        super.onStart();
        xHelper.log("goapp","BoardActivity onStart");
    }

    GoController mGoController = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoApp app =  GoApp.getInstance();
        mGoController = app.getGoController();
        final Window win = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.diagview);
        mTxtDump=(TextView)findViewById(R.id.txtDump);
        mBtnDump=(Button)findViewById(R.id.btnDiag);
        mBtnStat=(Button)findViewById(R.id.btnStat);
        mBtnReboot=(Button)findViewById(R.id.btnReboot);

        mConn=mGoController.getConnection();
        mHandler = new DiagHandler();
        evtLis = new EvtListener();
        mBtnDump.setOnClickListener(evtLis);
        mBtnStat.setOnClickListener(evtLis);
        mBtnReboot.setOnClickListener(evtLis);
		Button btn = (Button)findViewById(R.id.btnAddAdmin);
		btn.setOnClickListener(evtLis);
        btn = (Button)findViewById(R.id.btnPay);
        btn.setOnClickListener(evtLis);
        btn = (Button)findViewById(R.id.btnQueryPay);
        btn.setOnClickListener(evtLis);
        mGoController.setHandler(mHandler);
        mTxtDump.setTextColor(android.graphics.Color.BLACK);
        mTxtDump.setText("Press below button to refresh");
    }
    EvtListener evtLis=null;
    private DiagHandler mHandler;
    class DiagHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case GoController.MSG_RSP_DIAG:
                xHelper.log("goapp","get MSG_RSP_DIAG");
                mTxtDump.setText((String)(msg.obj));
                break;
            }
        }
    }

    class EvtListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btnDiag:
                onDump();
				break;
            case R.id.btnReboot:
                onReboot();
				break;
            case R.id.btnStat:
                onStat();
				break;
			case R.id.btnAddAdmin:
				onAddAdmin();
				break;
                case R.id.btnPay:
                    onPay();
                    break;
                case R.id.btnQueryPay:
                    onQueryPay();
                    break;
            }

        }
    }

	void onAddAdmin(){
		EditText et = (EditText)findViewById(R.id.etIpt);
		
		String s = et.getText().toString();
		int uid = Integer.parseInt(s);		
		s= "request:addadmin\r\nid:"+uid+"\r\n"+"permission:0\r\n\r\n";
		mConn.sendData(s);
	}

    void onPay(){
        EditText et = (EditText)findViewById(R.id.etIpt);

        String s = et.getText().toString();
        int uid = Integer.parseInt(s);

        et = (EditText)findViewById(R.id.etPay);

         s = et.getText().toString();
        int month = Integer.parseInt(s);


        s= "request:pay\r\nuid:"+uid+"\r\n"+"month:"+month+"\r\n\r\n";
        mConn.sendData(s);
    }
    void onQueryPay(){
        EditText et = (EditText)findViewById(R.id.etIpt);

        String s = et.getText().toString();
        int uid = Integer.parseInt(s);
        s= "request:query\r\nuid:"+uid+"\r\n\r\n";
        mConn.sendData(s);
    }
    void onDump() {
        String str;
        str = "request:diag\r\n"+ "\r\n";
        mConn.sendData(str);
    }
    void onStat() {
        String str;
        str = "request:stat\r\n"+ "\r\n";
        mConn.sendData(str);
    }
    void onReboot() {
        String str;
        str = "request:reboot\r\n"+ "\r\n";
        mConn.sendData(str);
    }
}
