package com.sureone;

import java.util.ArrayList;
import android.view.Window;
import android.view.WindowManager;

import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.HorizontalScrollView;
import android.view.LayoutInflater;
import com.sureone.base.BaseActivity;
import com.sureone.model.Group;
import us.xdroid.util.RemoteResource;
import android.util.TypedValue;
import us.xdroid.util.xUtil;

public class GroupRegisterView extends BaseActivity {

    private Button mBtnReg;
    private EditText mEtGroup;


    Long mGroupId;
    Group mGroup;

    @Override
    public void onCreate(Bundle parent) {
        super.onCreate(parent);

        setContentView(R.layout.groupregisterview);

        setMyTitle(R.string.registergroup);


        mGroupId = getIntent().getLongExtra("group_id",0L);

        View v = findViewById(R.id.BtnRegister);
        View v2 = findViewById(R.id.btnSave);

        if(mGroupId!=0){
            mGroup = mController.getGroupById(mGroupId);
            v.setVisibility(View.GONE);
            v2.setVisibility(View.VISIBLE);


            ((EditText)findViewById(R.id.groupname)).setText(mGroup.getGROUP_NAME());
            ((EditText)findViewById(R.id.groupdesc)).setText(mGroup.getGROUP_DESC());


            String[] mTestArray =  getResources().getStringArray(R.array.ranks);

            String label = GoModel.rankLabel(mGroup.getRANK_REQ().intValue());
            int idx=0;

            for (String s : mTestArray) {

                if(s.equals(label)){

                      break;
                }
                idx++;
            }

            ((Spinner)findViewById(R.id.rankReq)).setSelection(idx);


        } else{
            v2.setVisibility(View.GONE);
            v.setVisibility(View.VISIBLE);

        }






    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }



    String mGroupName=null;

    public void onRegister(View view) {
        mGroupName = ((EditText)findViewById(R.id.groupname)).getText().toString();
        String desc = ((EditText)findViewById(R.id.groupdesc)).getText().toString();
        String rank = ((Spinner)findViewById(R.id.rankReq)).getSelectedItem().toString();
        if(mGroupName.length()==0) {

            return;
        }

        if(mWaitingDialog==null) {
            mWaitingDialog=this.showFailureDialog(R.string.waitingforlogin);
        }


        //if(xHelper.getRankNo(mController.getMyUser().rank)<=19){
        //    mController.registerGroup(mGroupName);
        //}


        mController.WebRegisterGroup(mGroupName,desc,rank);



    }


    public void onSave(View view) {
        mGroupName = ((EditText)findViewById(R.id.groupname)).getText().toString();
        String desc = ((EditText)findViewById(R.id.groupdesc)).getText().toString();
        String rank = ((Spinner)findViewById(R.id.rankReq)).getSelectedItem().toString();
        if(mGroupName.length()==0) {

            return;
        }

        if(mWaitingDialog==null) {
            mWaitingDialog=this.showFailureDialog(R.string.waitingforlogin);
        }


        //if(xHelper.getRankNo(mController.getMyUser().rank)<=19){
        //    mController.registerGroup(mGroupName);
        //}


        mController.WebUpdateGroup(mGroupId,mGroupName,desc,rank);



    }








    @Override
    protected void processHandlerMessage(Message msg){
        switch (msg.what) {

            case GoController.MSG_REGISTER_GROUP_OK: {
                onRegisterOK();
                break;
            }
            case GoController.MSG_REGISTER_GROUP_FAIL: {
                onRegisterFail();
                break;
            }


            case GoController.WEB_UPDATE_GROUP: {
                closeWaitingDialog();
                break;
            }

            case GoController.WEB_GROUP_REGISTER: {
                closeWaitingDialog();
                break;
            }




        }
    }




    void closeWaitingDialog() {
        if(mWaitingDialog!=null) {
            xHelper.log("goapp","closeWaitingDialog");
            mWaitingDialog.dismiss();
            mWaitingDialog=null;
        }

    }

    void onRegisterOK() {
        closeWaitingDialog();
    }
    void onRegisterFail() {
        closeWaitingDialog();
        showFailureDialog(R.string.registerfail);
    }

}
