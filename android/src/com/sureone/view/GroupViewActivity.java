package com.sureone.view;

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
 * Date: 8/18/13
 * Time: 1:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupViewActivity extends BaseActivity{
    Long mGroupId;

    Group mGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupview);

        mGroupId = getIntent().getLongExtra("group_id",0L);

        mGroup = mController.getGroupById(mGroupId);

        setMyTitle(mGroup.getGROUP_NAME());


        TextView tv = (TextView) findViewById(R.id.groupDesc);
        if(mGroup.getGROUP_DESC()!=null)
        tv.setText(mGroup.getGROUP_DESC());

        View v1 = findViewById(R.id.btnJoin);

        if(mController.getMyUser().groupid==mGroupId.intValue()){
            v1.setVisibility(View.GONE);
        }

        View v2 = findViewById(R.id.btnLeave);


        if(mController.getMyUser().groupid==mGroupId.intValue()){
            v2.setVisibility(View.VISIBLE);
        }else{
            v2.setVisibility(View.GONE);
        }

        View v3=findViewById(R.id.btnUpdate);
        v3.setVisibility(View.GONE);
        if(mGroup.getGROUP_OWNER().intValue()==mController.getMyUser().id){
            v3.setVisibility(View.VISIBLE);
        }


        if(mController.isGroupOwner()){

            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);

        }



    }

    public void joinGroup(View view){

        xHelper.log("joinGroup clicked");

        mController.WebJoinGroup(mGroupId);

    }

    public void leaveGroup(View view){

        mController.WebLeaveGroup();

    }


    public String SERVER_PRE="http://42.121.129.37/xgo";
    public void showMembers(View view){

        SERVER_PRE= GoModel.getPrefString("NEW_HTTP_URL","http://42.121.129.37/xgo");
        String url=SERVER_PRE+"/index.php/group/members/"+mGroup.getGROUP_ID()
                +"/"+mController.getMyUser().id
                +"/"+ xUtil.getDeviceID(this);

        Intent intent =  new Intent(this, CommonWebView.class);

        intent.putExtra("url",url);
        intent.putExtra("title",getString(R.string.groupMembers));

        startActivity(intent);

    }

    public void showBoard(View view){

        SERVER_PRE= GoModel.getPrefString("NEW_HTTP_URL","http://42.121.129.37/xgo");
        String url=SERVER_PRE+"/index.php/group/board/"+mGroup.getGROUP_ID()
                +"/"+mController.getMyUser().id
                +"/"+ xUtil.getDeviceID(this);

        Intent intent =  new Intent(this, CommonWebView.class);

        intent.putExtra("title",getString(R.string.groupBoard));

        intent.putExtra("url",url);

        startActivity(intent);

    }


    public void editGroup(View view){

        Intent intent =  new Intent(this, GroupRegisterView.class);

        intent.putExtra("group_id",mGroupId);
        startActivity(intent);

    }


    protected void processHandlerMessage(Message msg){
        switch (msg.what){
            case GoController.WEB_LEAVE_GROUP:
                xHelper.log("LEAVE GROUP OK");
                mController.getMyUser().groupid=0;
                mController.getMyUser().groupName=null;
                this.finish();


                break;

            case GoController.WEB_JOIN_GROUP:
                xHelper.log("JOIN GROUP OK");
                mController.getMyUser().groupid=mGroupId.intValue();
                mController.getMyUser().groupName=mGroup.getGROUP_NAME();


                View v = findViewById(R.id.btnJoin);
                v.setVisibility(View.GONE);

                v = findViewById(R.id.btnLeave);

                v.setVisibility(View.VISIBLE);



                break;
        }

    }
}
