package com.sureone;

public interface xTcpEventListener {
    public void onConnected(xTcpThread thread);
    public void onConnectTimeOut(xTcpThread thread);
    public void onDisconnected(xTcpThread thread);
    public void onDataReceived(xTcpThread thread,byte[] buf,int len);
		public void onSendFailed(String s);
//	public void onErrorHappened(xTcpThread thread);
}
