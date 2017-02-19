package com.sureone.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
import com.sureone.GoApp;
import com.sureone.GoController;
import com.sureone.GoModel;
import com.sureone.R;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 8/4/13
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends Activity{

    protected GoApp mApp=null;
    protected GoController mController;

    private MyHandler mHandler = new MyHandler();

    public void onCreate(Bundle parent) {
        super.onCreate(parent);

        mApp =  GoApp.getInstance();
        mController = mApp.getGoController();
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        mController.setHandler(mHandler);

    }

    public GoController getController(){
        return mController;
    }

    public GoModel getModel(){
        return mController.getModel();
    }


    @Override
    public void onStart() {

        mController.setHandler(mHandler);
        super.onStart();
    }


    @Override
    public void onStop() {
        if(mHandler==mController.getHandler())
            mController.setHandler(null);
        super.onStop();
    }




    class MyHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
          processHandlerMessage(msg);
        }
    }


    protected void processHandlerMessage(Message msg){

    }

    protected void setMyTitle(int strId){
        com.viewpagerindicator.NormalTitle nt = (com.viewpagerindicator.NormalTitle) findViewById(R.id.ntTitle);
        if(nt!=null) nt.setTitle(getString(strId));
    }


    protected void setMyTitle(String s){
        com.viewpagerindicator.NormalTitle nt = (com.viewpagerindicator.NormalTitle) findViewById(R.id.ntTitle);
        if(nt!=null) nt.setTitle(s);
    }


    protected  void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }




    public AlertDialog showFailureDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(id)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mWaitingDialog=null;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }


    private AlertDialog alertDialog = null;

    public void dismissAlertDialog(){
        if(alertDialog!=null){
            alertDialog.dismiss();
            alertDialog=null;
        }

    }
    public void showAlertDialog(int id) {
        if(alertDialog!=null){
            alertDialog.dismiss();

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(id)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }


    public AlertDialog mWaitingDialog=null;

}
