package com.sureone.igs;
import com.sureone.xHelper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.security.*;
import android.util.Log;
public class IgsThread implements Runnable {

    public static final int TCP_DATA_ARRIVED = 0x2000;
    public static final int TCP_CONN_OK = 0x2001;
    public static final int TCP_CONN_TIMEOUT = 0x2002;
    public static final int TCP_CONN_CLOSED = 0x2003;
    private static final int OK_TIMEOUT = 30000;
    public int MAX_TCP_BUFFER = 4096;
    private String mAddress;
    private int mPort;
    private boolean mDone;
    private Socket mSocket;
    private boolean mWaitForOK;
    private long mLastActive;
    private BufferedReader mReader;
    private Thread mThread;
    private IgsListener mListener = null;
    public boolean mConnected=false;

    protected IgsThread() {
    }
    public void setAddress(String ip,int port) {
        mAddress = ip;
        mPort = port;
    }
    void LogLog(String s) {
        xHelper.log("igs","IgsThread:"+s);
    }

    public synchronized void setListener(IgsListener listener) {
        mListener = listener;
    }
    public void setSoTimeout(int timeout) {
        try {
            mSocket.setSoTimeout(timeout);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mConnected;
    }
    public void onError() {
        mConnected=false;
        xHelper.log("goapp","tcp error happened");
        if(mListener!=null)
            mListener.onDisconnected();
    }
    public synchronized void start() {
        try {
            LogLog("start the thread and connect to server...");
            connectServer();
            mDone=false;
            mThread = new Thread(this, "tcpThread");
            mThread.setDaemon(true);
            mThread.start();
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
                LogLog("close the socket");
                state = STATE_DOWN;
            }
        } catch (IOException e) {
            e.printStackTrace();
            onError();
            // ignore
        }
    }

    boolean arrayContains(byte[] src,byte[] dest,int len) {
        int i=0;
        int j=0;
        while(i<len) {
            if(src[i]==dest[0]) {
                j=0;
                int ii=i;
                while(j<dest.length) {
                    i++;
                    j++;
                    if(i==len || j==dest.length) {
                        return false;
                    }
                    if(src[i]!=dest[j]) {
                        break;
                    }
                }
                i=ii;
                if(j==dest.length) return true;
            }
            i++;
        }
        return false;
    }
    public void run() {
        byte[] buf = new byte[MAX_TCP_BUFFER];
        String curline=null;
        while (!mDone) {
            try {
                if(state == STATE_DOWN || state == STATE_LOGON || state == STATE_AUTH) {
                    DataInputStream in = new DataInputStream(mSocket.getInputStream());
                    int ret = in.read(buf,0,MAX_TCP_BUFFER-2);
                    String str=new String(buf,0,ret);
                    //System.out.println(str);
                    //Need input user name first
                    LogLog(str);
                    if(state == STATE_DOWN) {
                        byte[] bs= {(byte)'L',(byte)'o',(byte)'g',(byte)'i',(byte)'n',(byte)':'};
                        //if(arrayContains(buf,bs,ret)){
                        if(str.indexOf("Login:")!=-1) {
                            sendData(mUserName+"\n");
                            state = STATE_LOGON;
                        }
                    } else if(state == STATE_LOGON) {
                        byte[] bs1= {
                            (byte)'g',(byte)'u',(byte)'e',(byte)'s',(byte)'t',(byte)' ',
                            (byte)'a',(byte)'c',(byte)'c',(byte)'o',(byte)'u',(byte)'n',
                            (byte)'t',
                        };
                        byte[] bs2= {
                            (byte)'P',(byte)'a',(byte)'s',(byte)'s',(byte)'w',(byte)'o',
                            (byte)'r',(byte)'d',(byte)':',
                        };
                        byte[] bs3= {
                            (byte)'1',(byte)' ',(byte)'1',
                        };
                        //if(arrayContains(buf,bs1,ret)){
                        if(str.indexOf("guest account")!=-1) {
                            //mListener.onInvalidNameOrPassword();
                            //}else if(arrayContains(buf,bs2,ret) || arrayContains(buf,bs3,ret)){
                        } else if(str.indexOf("Password:")!=-1 || str.indexOf("1 1")!=-1) {
                            sendData(mPasswd+"\n");
                            state = STATE_AUTH;
                        }
                    } else if(state == STATE_AUTH) {
                        byte[] bs1= {
                            (byte)'I',(byte)'n',(byte)'v',(byte)'a',(byte)'l',(byte)'i',
                            (byte)'d',(byte)' ',(byte)'p',(byte)'a',(byte)'s',(byte)'s',
                            (byte)'w',(byte)'o',(byte)'r',(byte)'d',
                        };
                        byte[] bs2= {
                            (byte)'1',(byte)' ',(byte)'5',
                        };
                        byte[] bs3= {
                            (byte)'#',(byte)'>',
                        };
                        //if(arrayContains(buf,bs1,ret)){
                        if(str.indexOf("Invalid password")!=-1) {
                            //mListener.onInvalidNameOrPassword();
                            //}else if(arrayContains(buf,bs2,ret) || arrayContains(buf,bs3,ret)){
                        } else if(str.indexOf("1 5")!=-1 || str.indexOf("#>")!=-1) {
                            state = STATE_SESSION;
                            mListener.onLoginOK();
                        }
                    }
                } else {
                    curline=null;
                    curline = mReader.readLine();
                    if(curline!=null) {
                        if(mListener!=null)
                            mListener.onLineReceived(curline);
                    } else {
                        LogLog("socket read error happened");
                        onError();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError();
                break;
            }
        }

        mConnected=false;
        state = STATE_DOWN;
        if (mReader != null) {
            try {
                mReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogLog("tcp thread exit");

    }

    public void reconnect() {

    }

    private void reconnectAndWait() {
    }

    private final int STATE_DOWN = 0;
    private final int STATE_LOGON = 1;
    private final int STATE_AUTH = 2;
    private final int STATE_SESSION = 3;
    private int state = STATE_DOWN;

    private String mUserName = null;
    public void setUserName(String userName) {
        mUserName = userName;
    }
    public String mPasswd = null;
    public void setPassword(String password) {
        mPasswd=password;
    }
    private synchronized void connectServer() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(mAddress, mPort), 3000);
        } catch (Exception e) {
            e.printStackTrace();
            if(mListener!=null)
                mListener.onConnectTimeOut();
            return;
        }

        mConnected=true;

        try {
            if (mReader != null) {
                mReader.close();
            }
            InputStreamReader converter = new InputStreamReader(mSocket.getInputStream());
            mReader = new BufferedReader(converter);
        } catch (IOException e) {
            e.printStackTrace();
            onError();
            return;
        }
        if(mListener!=null)
            mListener.onConnected();

    }


    public int sendData(String s) {
        LogLog("sendData:"+s);
        try {
            if(this.isConnected()==false) {
                LogLog("tcp connection broken while sending Data");
                return -1;
            }
            mWaitForOK = true;
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
            mSocket.getOutputStream().write(s.getBytes("UTF-8"));
        } catch(Exception e) {
            e.printStackTrace();
            onError();
            return -1;
        }
        return 0;
    }





}
