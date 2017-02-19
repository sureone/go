package com.sureone.igs;

public interface IgsListener {
    public void onConnected();
    public void onConnectTimeOut();
    public void onDisconnected();
    public void onLineReceived(String strLine);
    public void onLoginOK();
}
