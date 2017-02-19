package com.sureone.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.sureone.*;
import com.sureone.base.BaseActivity;
import com.sureone.model.Group;
import us.xdroid.util.xUtil;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 9/14/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeskSetupActivity extends BaseActivity{
    Long mGroupId;

    Group mGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desksetupview);



        setMyTitle(R.string.matchsetup);





    }


    @Override
    public void onBackPressed(){
        mController.requestLeave();
        super.onBackPressed();

    }


    int mStepTimeOut=3;

    public void onTimeRadioClicked(View view){

        if(view.getId()==R.id.radio_1min){
            mStepTimeOut=1;
        }

        if(view.getId()==R.id.radio_2min){
            mStepTimeOut=2;
        }

        if(view.getId()==R.id.radio_3min){
            mStepTimeOut=3;
        }

    }





    void toGoActivity() {
        Intent intent = new Intent(this,com.sureone.GoActivity.class);
        intent.putExtra("view","normal");
        intent.putExtra("fullscreen","false");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
        ((Activity)this).finish();
    }

    public void enterDesk(View v){

        //toGoActivity();
        mController.setStepTime(mStepTimeOut);

    }


    public void showBoard(View view){


        Intent intent =  new Intent(this, CommonWebView.class);

        intent.putExtra("title",getString(R.string.groupBoard));

        startActivity(intent);

    }



    protected void processHandlerMessage(Message msg){
        switch (msg.what){
            case GoController.WEB_LEAVE_GROUP:


                break;
            case GoController.MSG_NTFY_SET_STEP_TIME:
                toGoActivity();
                break;
        }

    }
}
