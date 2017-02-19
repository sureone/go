package com.sureone.match;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.sureone.GoController;
import com.sureone.GoModel;
import com.sureone.R;
import com.sureone.base.BaseActivity;
import us.xdroid.util.MapHelper;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 9/20/13
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewMatchView extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmatchview);
        setMyTitle(R.string.newSelfMatch);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }



    @Override
    protected void processHandlerMessage(Message msg){
        switch (msg.what) {

            case GoController.WEB_REGISTER_MATCH: {
                if(msg.arg2!=0){
                    showAlertDialog(R.string.registerfail);
                }else{
                    onRegisterMatchResult((Map) msg.obj);
                }
                break;
            }

        }
    }



    void onRegisterMatchResult(Map result){

        Long resultCode = MapHelper.l(result,"CODE");

        if(resultCode.equals(200L)==false){
            showAlertDialog(R.string.match_register_fail);
        }else{
            dismissAlertDialog();
            onBackPressed();
        }
    }


    public void onNewTheMatch(View v){

        String title = ((EditText)findViewById(R.id.etTitle)).getText().toString();
        String desc = ((EditText)findViewById(R.id.etDesc)).getText().toString();

        int rankMin,rankMax;
        rankMax=rankMin=0;
        int idx = ((Spinner)findViewById(R.id.rankMin)).getSelectedItemPosition();
        if(idx>0){
            String option = ((Spinner)findViewById(R.id.rankMin)).getSelectedItem().toString();
            rankMin= GoModel.getRankValue(option);

            option = ((Spinner)findViewById(R.id.rankMax)).getSelectedItem().toString();
            rankMax= GoModel.getRankValue(option);
        }

        if(title.trim().length()==0 || desc.trim().length()==0) {

            return;
        }

        showAlertDialog(R.string.waitingforregister);



        //if(xHelper.getRankNo(mController.getMyUser().rank)<=19){
        //    mController.registerGroup(mGroupName);
        //}


        mController.WebRegisterMatch(title,desc,rankMin,rankMax);

    }

    public void onCancelCreate(View v){
        onBackPressed();
    }
}
