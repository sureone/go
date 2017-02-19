
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class InviteDialog extends Activity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        GoApp app =  GoApp.getInstance();
        mGoController.setHandler(mHandler);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        GoApp app =  GoApp.getInstance();
        mGoController.setHandler(mHandler);
        super.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        GoApp app = GoApp.getInstance();
        mGoController.setHandler(mHandler);
        super.onStart();
    }
		
    GoController mGoController = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoApp app =  GoApp.getInstance();
        mGoController = app.getGoController();
        final Window win = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invitedialog);
        mHandler = new DiagHandler();
        evtLis = new EvtListener();
        Button btn=(Button)findViewById(R.id.btn_dialog_left);
        btn.setOnClickListener(evtLis);
        btn=(Button)findViewById(R.id.btn_dialog_right);
        btn.setOnClickListener(evtLis);
        mGoController.setHandler(mHandler);
				mType=getIntent().getIntExtra("type",0);
				if(mType==GoController.MSG_NTFY_INVITE_REQ){
					showInvite();
				}
				if(mType==GoController.MSG_NTFY_MESSAGE){
					showMessage();
				}
				if(mType==GoController.PLAYER_LIST_MENU){
					showPlayerListMenu();
				}
    }
	int mType=0;


	void showButtons(int left,int right){
				LinearLayout ll = (LinearLayout)findViewById(R.id.ll_buttons);
				ll.setVisibility(View.VISIBLE);
				Button btn= (Button)findViewById(R.id.btn_dialog_left);
				btn.setText(getString(left));
				btn= (Button)findViewById(R.id.btn_dialog_right);
				btn.setText(getString(right));
	}

	void showEdit(int hint){
				LinearLayout ll = (LinearLayout)findViewById(R.id.ll_et);
				ll.setVisibility(View.VISIBLE);
				EditText et = (EditText)findViewById(R.id.et_message);
				if(hint!=0){
					et.setHint(hint);
				}
	}

	void setTitle(String s){
		TextView tv = (TextView)findViewById(R.id.title);		
		tv.setText(s);
	}
	void setInfo(String s){
		TextView tv = (TextView)findViewById(R.id.txtInfo);
		tv.setVisibility(View.VISIBLE);
		tv.setText(s);
	}

	void showMessage(){
			setTitle(getString(R.string.message));
			showButtons(R.string.close,R.string.reply);
			showEdit(0);
      mUid = getIntent().getIntExtra("uid",0);
			if(mUid!=0){
				String s = getIntent().getStringExtra("content");
				User u = mGoController.getUser(mUid);
				setInfo(u.name+"\n"+s);																
			}
	}

	void showPlayerListMenu(){
      mUid = getIntent().getIntExtra("uid",0);
			setTitle(getString(R.string.options));			
			if(mUid!=0){
				
				LinearLayout ll = (LinearLayout)findViewById(R.id.ll_menus);
				ll.setVisibility(View.VISIBLE);
				ll = (LinearLayout)findViewById(R.id.ll_invite);
				ll.setOnClickListener(evtLis);
				ll = (LinearLayout)findViewById(R.id.ll_message);
				ll.setOnClickListener(evtLis);

			}
	}
	void showInvite(){
      mUid = getIntent().getIntExtra("uid",0);
			setTitle(getString(R.string.invite));
			showButtons(R.string.decline,R.string.accept);
			if(mUid!=0){
					User u = mGoController.getUser(mUid);
					String s= u.name+"\n"+getString(R.string.rank)+" "+u.rank+" "+
						getString(R.string.win)+" "+u.wins + " "+
						getString(R.string.lost)+" "+u.loses+"\n";
					setInfo(s);
			}
	}

	int mUid=-1;
    EvtListener evtLis=null;
    private DiagHandler mHandler;
    class DiagHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case GoController.MSG_RSP_DIAG:
                xHelper.log("goapp","get MSG_RSP_DIAG");
                break;
            }
        }
    }

    class EvtListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btn_dialog_right:
						onBtnRight();
						break;
		            case R.id.btn_dialog_left:
						onBtnLeft();
						break;
						case R.id.ll_invite:
							onSelInvite();break;
						case R.id.ll_message:
							onSelMessage();break;
            }

        }
    }
		void onSelMessage(){
			showButtons(R.string.close,R.string.send);
			showEdit(0);
		}
		void onSelInvite(){
			if(mUid!=0) mGoController.new_invite(mUid);	
			InviteDialog.this.finish();
		}
	String getEditText(){
			EditText et = (EditText)findViewById(R.id.et_message);
			String s = et.getText().toString();
			if(s!=null && s.length()>0) return s;
			else return null;
	}
	void onBtnRight(){
		if(mType==GoController.MSG_NTFY_INVITE_REQ) if(mUid!=0)mGoController.accept_invite(mUid);
		if(mUid!=0 && mType==GoController.MSG_NTFY_MESSAGE){
			String s = getEditText();
			if(s!=null) mGoController.messageHim(mUid,s);
		}

		if(mUid!=0 && mType==GoController.PLAYER_LIST_MENU){
			String s = getEditText();
			if(s!=null) mGoController.messageHim(mUid,s);
		}
		InviteDialog.this.finish();
	}
	void onBtnLeft(){
		if(mType==GoController.MSG_NTFY_INVITE_REQ) if(mUid!=0)mGoController.reject_invite(mUid);
		finish();
	}
}
