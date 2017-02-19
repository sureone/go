package com.sureone;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import java.security.*;

public class xTcpThread implements Runnable {

    public static final int TCP_DATA_ARRIVED = 0x2000;
    public static final int TCP_CONN_OK = 0x2001;
    public static final int TCP_CONN_TIMEOUT = 0x2002;
    public static final int TCP_CONN_CLOSED = 0x2003;

    private static final int OK_TIMEOUT = 30000;
    public int MAX_TCP_BUFFER = 20480;

    private String mAddress;
    private int mPort;

    private boolean mDone=true;

    private Socket mSocket;

    private boolean mWaitForOK;
    private long mLastActive;


    private DataInputStream mReader;

    private Thread mThread;
    private xTcpEventListener mTcpEventListener = null;

    public boolean mConnected=false;

    private String mNonce=null;

    private Handler mHandler=null;

    public final static String A_MAGIC="ABCDEFG1234567!@#$%^&*()-=";
    public xTcpThread() {
    }

    public void updateNonce(String n) {
        mNonce=n;

    }
    public void setAddress(String ip,int port) {
        mAddress = ip;
        mPort = port;
    }

    public synchronized void setTcpEventListener(xTcpEventListener listener) {
        mTcpEventListener = listener;
    }

    public void setEventHandler(Handler handler) {
        mHandler = handler;
    }
    public void setSoTimeout(int timeout) {
        try {
            mSocket.setSoTimeout(timeout);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    String mLastData=null;

    public boolean isConnected() {
        return mConnected;
    }
    public void onError() {
        mConnected=false;
        xHelper.log("tcp error happened");
        if(mTcpEventListener!=null)
            mTcpEventListener.onDisconnected(this);
    }
    public synchronized void start() {
        try {
            if(mDone==true) {
                mDone=false;
                mThread = new Thread(this, "tcpThread");
                mThread.setDaemon(true);
                mThread.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
            onError();
        }
    }


    public synchronized void shutdown() {

        mDone = true;
        try {
            if(mSocket != null) {
                //Issue:http://code.google.com/p/android/issues/detail?id=7933
                mSocket.shutdownInput();
                mSocket.close();
                
            }
        } catch (IOException e) {
            onError();
            e.printStackTrace();
            // ignore
        }
    }

		int max_retry=100;
		int retry_times=0;
    public void run() {
        byte[] buf = new byte[MAX_TCP_BUFFER];
        while (!mDone) {
						
						if(mConnected == false){
								if(connectServer()<0){
										xHelper.log("tcp error on connect");
										if(retry_times>=max_retry) mDone=true;
										else{
											 try{Thread.sleep(1500);}catch(Exception e){e.printStackTrace();}
										}
										continue;
								}
						}
						retry_times=0;
						
						if(mDone==true) return;
            try {		
                int ret = mReader.read(buf);
                if(ret>0) {
                    //xHelper.log("goapp","received "+ret+ " bytes");

                    //String str = new String(buf,0,ret,"utf-8");
                    //xHelper.log("goapp","recevied : "+str);
                    //xHelper.log("xTcpThread",str);
                    if(mTcpEventListener!=null)
                        mTcpEventListener.onDataReceived(this,buf,ret);
                    else if(mHandler!=null) {
                        xBuffer xbuf= new xBuffer(buf,ret);
                        Message message = new Message();
                        message.obj=xbuf;
                        message.what =TCP_DATA_ARRIVED;
                        this.mHandler.sendMessage(message);
                    }


                } else {
                    if(mTcpEventListener!=null)
                        mTcpEventListener.onDisconnected(this);
                    else if(mHandler!=null) {
                        Message message = new Message();
                        message.what =TCP_CONN_TIMEOUT;
                        this.mHandler.sendMessage(message);
                    }

										xHelper.log("tcp error on read");
										mConnected=false; continue;
                    //break;
                }

            } catch (Exception e) {
                e.printStackTrace();                
                if(mTcpEventListener!=null)
                    mTcpEventListener.onDisconnected(this);
                else if(mHandler!=null) {
                    Message message = new Message();
                    message.what =TCP_CONN_TIMEOUT;
                    this.mHandler.sendMessage(message);
                }
								xHelper.log("tcp error on read");
								mConnected=false; continue;
                //break;
            }
        }

        mConnected=false;
        if (mReader != null) {
            try {
                mReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //xHelper.log("goapp","tcp thread exit");

    }


    public void reconnect() {

    }

    private void reconnectAndWait() {

    }

    private synchronized int connectServer() {

        if (mSocket != null) {

            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
                // ignore
            }
        }

        mSocket = new Socket();
        try {
            xHelper.log("connect to "+mAddress+":"+mPort);
            mSocket.connect(new InetSocketAddress(mAddress, mPort), 3000);
        } catch (Exception e) {
            e.printStackTrace();
            if(mTcpEventListener!=null)
                mTcpEventListener.onConnectTimeOut(this);
            else if(mHandler!=null) {
                Message message = new Message();
                message.what =TCP_CONN_TIMEOUT;
                this.mHandler.sendMessage(message);
            }
						//xHelper.log("tcp error on connect");
            return -1;
        }

        mConnected=true;


        try {
            if (mReader != null) {
                mReader.close();
            }
            mReader = new DataInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
						mConnected=false;
            //onError();
            return -1;
        }
        if(mTcpEventListener!=null)
            mTcpEventListener.onConnected(this);
        else if(mHandler!=null) {
            Message message = new Message();
            message.what =TCP_CONN_OK;
            this.mHandler.sendMessage(message);
        }
        return 0;

    }


    public int sendDataWithAuth(String s) {
        try {

            String sa=A_MAGIC;
            if(mNonce!=null) sa = mNonce+A_MAGIC;
            byte[] bytesOfMessage = sa.getBytes("UTF-8");
            int lenM = bytesOfMessage.length;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            String hex = xHelper.toHex(thedigest);
            //xHelper.log("goapp",hex);
            String ns="auth:"+hex+"\r\n"+s;
            sendData(ns);
        } catch(Exception e) {
            e.printStackTrace();
            onError();
            return -1;
        }
        return 0;
    }

    public int sendData(String s) {
        try {
            if(this.isConnected()==false) {
                //xHelper.log("goapp","tcp connection broken while sending Data");
                return -1;
            }
            mWaitForOK = true;
            mLastActive = SystemClock.elapsedRealtime();
            /*
            byte[] bytesOfMessage = s.getBytes("UTF-8");
            int lenM = bytesOfMessage.length;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            int lenD = thedigest.length;
            byte[] data = new byte[lenD+2+lenM];
            System.arraycopy(bytesOfMessage, 0, thedigest, 0, lenD);
            byte[] blen = new byte[2];
            blen[0] = (byte)((lenM) & 0xFF);
            blen[1] = (byte)((lenM>>16) & 0xFF);
            System.arraycopy(thedigest, 0, data, 0, lenD);
            System.arraycopy(blen, 0, data, lenD, 2);
            System.arraycopy(bytesOfMessage, 0, data, lenD+2, lenM);
            */
	    			xHelper.log("sending "+s);
						mLastData=s;
            mSocket.getOutputStream().write(s.getBytes("UTF-8"));
						mLastData=null;
        } catch(Exception e) {
            e.printStackTrace();
						xHelper.log("tcp error on write");            
        		if(mTcpEventListener!=null)
					mTcpEventListener.onSendFailed(s);
				//stop the thread
				
            return -1;
        }
        return 0;
    }

    private void sendData(xBuffer xbuf) throws IOException {
        mSocket.getOutputStream().write(xbuf.mbuffer);
        mWaitForOK = true;
        mLastActive = SystemClock.elapsedRealtime();
    }
}
