package com.sureone.igs;
import android.util.Log;
import com.sureone.xHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class IgsModel {
    String mUserName=null;
    String mPassword=null;
    ArrayList<IgsPlayer> mPlayers = null;
    ArrayList<IgsGameItem> mGames = null;
    IgsGame mCurGame = null; //Observer Game or Playing Game
    IgsPlayer mMe = null;
    char mCurColor;
    void LogLog(String s) {
        xHelper.log("igs","IgsModel:"+s);
    }
    public char getCurColor() {
        return mCurColor;
    }
    public void setCurColor(char c) {
        mCurColor = c;
    }
    public void setMyName(String name) {
        mMe.name = name;
    }
    public String getMyName() {
        return mMe.name;
    }
    public String getMyRank() {
        return mMe.rank;
    }
    public void setMyRank(String rank) {
        mMe.rank=rank;
    }
    public IgsPlayer getAutoPairPlayer(int idx) {
        String name=null;
        int from = IgsPlayer.getRankNo(getMyRank());
        from--;
        int to = from+2;
        int i = 0;
        int si=-1;
        LogLog("autopair with "+from+" to "+to);
        while(i<mPlayers.size()) {
            IgsPlayer p = mPlayers.get(i);
            int rno=IgsPlayer.getRankNo(p.rank);
            if(rno>=from && rno<=to) {
                si++;
                if(si==idx) return p;
            } else if(rno>to) {
                break;
            }
            i++;
        }
        return null;
    }
    public IgsPlayer getMe() {
        return mMe;
    }
    public void sortPlayerForAutoPair() {
        Collections.sort(mPlayers,new Comparator<IgsPlayer>() {
            public int compare(IgsPlayer a, IgsPlayer b) {
                return IgsPlayer.getRankNo(a.rank)-IgsPlayer.getRankNo(b.rank);
            }
        });
    }
    public void sortGame() {
        Collections.sort(mGames,new Comparator<IgsGameItem>() {
            public int compare(IgsGameItem a, IgsGameItem b) {
                return IgsPlayer.getRankNo(a.brk)-IgsPlayer.getRankNo(b.brk);
            }
        });
    }
    public void sortPlayer() {
        Collections.sort(mPlayers,new Comparator<IgsPlayer>() {
            public int compare(IgsPlayer a, IgsPlayer b) {
                return a.name.compareTo(b.name);
            }
        });
    }
    IgsModel() {
        mPlayers = new ArrayList();
        mGames = new ArrayList();
        mMe = new IgsPlayer();
    }
    public IgsGame startObserve(int gameno) {
        mCurGame = new IgsGame();
        mCurGame.setGameNo(gameno);
        mCurGame.setSize(19);
        return mCurGame;
    }
    public IgsGame startMatch(int gameno) {
        mCurGame = new IgsGame();
        mCurGame.setGameNo(gameno);
        mCurGame.setSize(19);
        return mCurGame;
    }

    char getMyColor() {
        return mMe.color;
    }
    void setMyColor(char c) {
        mMe.color = c;
    }
    public IgsGame getCurrentGame() {
        return mCurGame;
    }

    public IgsGameItem getGameByIndex(int index) {
        IgsGameItem game = null;
        if(index>=mGames.size()) return null;
        game = mGames.get(index);
        return game;
    }
    public IgsPlayer getPlayerByIndex(int index) {
        IgsPlayer game = null;
        if(index>=mPlayers.size()) return null;
        game = mPlayers.get(index);
        return game;
    }
    public void addPlayer(IgsPlayer player) {
        mPlayers.add(player);
    }
    public void addGame(IgsGameItem game) {
        mGames.add(game);
    }
    public int countGames() {
        return mGames.size();
    }
    public int countPlayerss() {
        return mPlayers.size();
    }
    public int countPlayers() {
        return mPlayers.size();
    }
    void clearGames() {
        mGames.clear();
    }
    void clearPlayers() {
        mPlayers.clear();
    }
}
