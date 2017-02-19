package com.sureone.match;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;
import com.sureone.GoController;
import com.sureone.R;
import com.sureone.base.BaseActivity;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 11/3/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchViewActivity extends BaseActivity{

    int matchIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_view);

        matchIndex=getIntent().getIntExtra("match-index", -1);
        if(matchIndex!=-1){
            Map match = getModel().getSelfMatchByIndex(matchIndex);

            setMyTitle((String)match.get("title"));


            TextView tv = (TextView) findViewById(R.id.tvDescription);
            tv.setText((String)match.get("description"));


        }




    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }



    @Override
    protected void processHandlerMessage(Message msg){
        switch (msg.what) {

            case GoController.WEB_REGISTER_MATCH: {
                break;
            }

        }
    }

}
