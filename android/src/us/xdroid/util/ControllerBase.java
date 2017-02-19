package us.xdroid.util;
import android.os.Process;
import android.os.Message;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.LinkedList;

public class ControllerBase{
    protected static HandlerThread mThread;
    protected Context mContext;
    protected BackgroundHandler mBackgroundHandler;
    protected Handler mUiHandler;
    protected static final Object sLock = new Object();
    public void setUIHandler(Handler h) {
        synchronized (sLock) {
            mUiHandler = h;
        }
    }

    public static class MyMessage{
        Message msg;
        Handler handler;
        public MyMessage(Message m ,Handler h){
            msg=m;
            handler =h;
        }
    }


    protected static LinkedList<MyMessage> msg_queue = new LinkedList<MyMessage>();

    public static void pushMessage(MyMessage msg){
        synchronized (sLock) {
            msg_queue.addLast(msg);
        }
    }
    public static MyMessage popMessage(){
        MyMessage msg;
        synchronized (sLock) {
            msg= msg_queue.getFirst();
            msg_queue.removeFirst();
        }
        return msg;
    }



	public void init(Context context){
		mContext = context;
        mThread = new HandlerThread("BasicController.Thread",
                                    Process.THREAD_PRIORITY_BACKGROUND);
        mThread.start();
        mBackgroundHandler = new BackgroundHandler(mThread.getLooper());
	}

	public Context getContext(){
		return mContext;
	}

	public void sendRequestNC(int reqid,int arg1,int arg2,Object obj){
		
        if (mBackgroundHandler!=null) mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(
                                       reqid,arg1,arg2,obj));
	}

    public class InterResponse{
        public Object obj1;
        public Object obj2;
        public int what;

        public InterResponse(Object o1,Object o2){
            obj1=o1;
            obj2=o2;
        }
    };

	public void sendRequest(int reqid,int arg1,int arg2,Object obj){
        /*
		com.sureone.xHelper.log("sendRequest");
        if (!mBackgroundHandler.hasMessages(reqid)) {
			com.sureone.xHelper.log("sendRequest");
            mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(
                                               reqid,arg1,arg2,obj));
        }
        */


        Message message=new Message();
        message.what=reqid;
        message.obj=obj;
        MyMessage msg = new MyMessage(message,mUiHandler);
        pushMessage(msg);
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                MyMessage message = popMessage();
                // TODO Auto-generated method stub

                int ret = ControllerBase.this.handleMessage(message.msg);
                if(ret==0){
                    message.handler.sendMessage(message.handler.obtainMessage(message.msg.what,ret,0,message.msg.obj));
                }else
                    message.handler.sendMessage(message.handler.obtainMessage(
                            message.msg.what,
                            ret,
                            404,
                            message.msg.obj));

            }
        });
        thread.start();


	}
	public void sendRequest(int reqid,Object obj){
		sendRequest(reqid,0,0,obj);
	}
	public void sendRequest(int reqid){
		sendRequest(reqid,0,0,null);
	}
	public void sendRequest(int reqid,int arg1){
		sendRequest(reqid,arg1,0,null);
	}
	public void sendRequestNC(int reqid,int arg1){
		sendRequestNC(reqid,arg1,0,null);
	}
	public void sendRequestNC(int reqid,Object obj){
		sendRequestNC(reqid,0,0,obj);
	}
	public void sendRequestNC(int reqid){
		sendRequestNC(reqid,0,0,null);
	}
    public void sendMessageToUI(int msgid,int arg1,int arg2,Object obj) {
		if(mUiHandler!=null) mUiHandler.sendMessage(mUiHandler.obtainMessage(msgid,arg1,arg2,obj));
    }
    class BackgroundHandler extends Handler {
        BackgroundHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
			ControllerBase.this.handleMessage(msg);
		}
	}
	public int handleMessage(Message msg){

        return 0;
	}
}


	

