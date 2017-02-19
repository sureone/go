package com.sureone;

public interface ChessEventListener{
	public void onPutChess(int x,int y);
	public boolean haveLogic();
	public int getSide(int x,int y);
	public int getLastStepX();
	public int getLastStepY();
	public int getStepNo(int x,int y);
}