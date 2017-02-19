package com.sureone.igs;
import com.sureone.xHelper;
import java.io.*;
import android.os.Bundle;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.sureone.MyMsg;
public class IgsController {


    public final static int MSG_LOGIN_OK=1;
    public final static int MSG_LOGIN_FAIL=2;
    public final static int MSG_LIST_GAME_DONE=3;
    public final static int MSG_CONNECT_OK=4;
    public final static int MSG_IGS_STEP=5;
    public final static int MSG_OBSERVE_OK=6;
    public final static int MSG_STATUS_OK=7;
    public final static int MSG_GAME_END=8;
    public final static int MSG_LIST_PLAYER_DONE=9;
    public final static int MSG_GAME_START=10;
    public final static int MSG_GET_INVITE=11;
    public final static int MSG_GAME_INFO=12;
    public final static int MSG_IGS_SAY=13;
    public final static int MSG_IGS_RESIGN=14;
    public final static int MSG_IGS_STATS=15;
    public final static int MSG_IGS_CONN_BROKEN=16;
    public final static int MSG_SILENT_LOGIN_OK=17;
    public final static int MSG_IGS_SCORE_BEGIN=18;
    public final static int MSG_IGS_PASS=19;
    public final static int MSG_SCORE_INFO=20;
    public final static int MSG_IGS_REMOVE_DEAD=21;
    public final static int MSG_IGS_UNDO_SCORE=22;
    public final static int MSG_IGS_RANK=23;
    public final static int MSG_IGS_STORED=24;
    public final static int MSG_IGS_OPP_LOST=25;
    public final static int MSG_IGS_OPP_RESTORE=26;
    public final static int MSG_IGS_RESTORED=27;
    public final static int MSG_IGS_LOOK_STORED=28;
    IgsThread mThread = null;
    IgsModel mModel = null;
    EvtListener mListener;
    IgsPollData mPollData = null;
    int mLastCommand = IgsMessage.UNKNOWN;
    int mState;
    public IgsController() {
        mModel = new IgsModel();
        mListener = new EvtListener();
        mPollData = new IgsPollData();
        mState = OFFLINE;
    }
    Handler mViewHandler = null;
    Handler getViewHandler() {
        return mViewHandler;
    }
    void setViewHandler(Handler h) {
        mViewHandler = h;
    }
    char getMyColor() {
        return mModel.getMyColor();
    }
    void setMyColor(char c) {
        LogLog("setMyColor="+c);
        mModel.setMyColor(c);
    }
    void sendTalk(String s) {
        sendCommand("say "+s);
    }
    void score() {
        setLastCommand(IgsMessage.SCORE);
        sendCommand("score");
    }
    public IgsPlayer getMe() {
        return mModel.mMe;
    }
    void done() {
        sendCommand("done");
    }
    void confrimDeads(String de) {
        sendCommand(de);
    }
    boolean pass() {
        if(isMyTurnToPutChess()) {
            setLastCommand(IgsMessage.PASS);
            sendCommand("pass");
            return true;
        }
        return false;
    }
    void LogLog(String s) {
        xHelper.log("igs","IgsController:"+s);
    }
    void LogLog(String tag,String s) {
        xHelper.log(tag,s);
    }
    public static final int OFFLINE = 0;
    public static final int CONNECTED = 1;
    public static final int IDLE = 2;
    public static final int PRE_OBSERVE =3;
    public static final int IN_OBSERVE = 4;
    public static final int IN_GAME = 5;
    public static final int IN_WAIT = 6;
    void reset() {
        changeState(OFFLINE);
        mSubState=SS_NONE;
        mPollData.reset();
    }

    int getLastCommand() {
        return mLastCommand;
    }
    void setMyName(String name) {
        mModel.setMyName(name);
    }
    String getMyName() {
        return mModel.getMyName();
    }
    String getMyRank() {
        return mModel.getMyRank();
    }

    IgsPlayer getAutoPairPlayer(int idx) {
        return mModel.getAutoPairPlayer(idx);
    }

    IgsGameItem getGameByIndex(int index) {
        IgsGameItem game = mModel.getGameByIndex(index);
        return game;
    }
    IgsPlayer getPlayerByIndex(int index) {
        IgsPlayer game = mModel.getPlayerByIndex(index);
        return game;
    }
    int getNumberOfGames() {
        return mModel.countGames();
    }
    int getNumberOfPlayers() {
        return mModel.countPlayers();
    }
    int mLastResponse=IgsMessage.UNKNOWN;
    void setLastResponse(int res) {
        mLastResponse=res;
    }
    int getLastResponse() {
        return mLastResponse;
    }
    void setLastCommand(int cmd) {
        int oldcmd = mLastCommand;
        mLastCommand = cmd;
        LogLog("setLastCommand="+cmd);
    }
    void changeState(int state) {
        int oldState = mState;
        mState = state;
        LogLog("Cur state="+mState);
    }
    int getState() {
        return mState;
    }
    public void close() {
        reset();
        if(mThread == null) return;
        if(mThread.isConnected()) {
            mThread.shutdown();
            mThread = null;
        }
    }
    public void shutdown() {
        if(mThread.isConnected()) {
            mThread.shutdown();
        }
    }
    boolean isConnected() {
        return mThread.isConnected();
    }
    public int reconnectIgs() {
        mThread.start();
        return 0;
    }
    public int connectIgs(String ip,int port,String userName,String passwd) {
        if(mThread == null) {
            mThread = new IgsThread();
            mThread.setListener(mListener);
        }
        setMyName(userName);
        LogLog("igs","connectIgs "+ip+" "+port+" "+userName);
        mThread.setUserName(userName);
        mThread.setPassword(passwd);
        mThread.setAddress(ip,port);
        mThread.start();
        return 0;
    }
    boolean pollFlag = false;
    void polling(boolean v) {
        pollFlag = v;
    }
    boolean isPolling() {
        return pollFlag;
    }

    /*
     *
    Usage: status <status game number>

    'status' gives the board position for the supplied game number.
    Primarily for information for clients. The board is reflected about
    the diagonal.

    Format:
       <white name> <# of captured stones> <time left> <byoyomi stones> <T|F>
       <black name> <# of captured stones> <time left> <byoyomi stones> <T|F>
        ##: ################### (size of the board)

        The '##:' is the line number of the board. The rest of the numbers
        are as follows:
                0:  Black      4:  White Territory
                1:  White      5:  Black Territory
                2:  Empty      6:  Starpoint
                3:  Dame       7:  Counted

    See also:  games

    8 File
    1 5
     *
     */

    int mCurStatusGameId = -1;

    public boolean movesCur() {
        IgsGame game = getCurrentGame();
        LogLog("igs","moves "+game.getGameNo());
        if(game!=null) {
            setLastCommand(IgsMessage.MOVES);
            sendCommand("moves "+game.getGameNo());
            return true;
        }
        return false;
    }
    public boolean statusObserve() {
        IgsGame game = getCurrentGame();
        LogLog("igs","statusObserve "+game.getGameNo());
        if(game!=null) {
            status(game.getGameNo());
            return true;
        }
        return false;
    }
    public void status(int gameno) {
        mPollData.reset();
        mPollData.setPollType(IgsMessage.STATUS);
        mPollData.setParamInt(1,gameno);
        mPollData.setParamObj(1,getCurrentGame());
        setLastCommand(IgsMessage.STATUS);
        polling(true);
        sendCommand("status "+gameno);
    }
    boolean isMyTurnToPutChess() {
        if(mState!=IN_GAME) return false;
        return mModel.getCurColor()==getMyColor();
    }

    char getCurColor() {
        return mModel.getCurColor();
    }
    void setCurColor(char c) {
        LogLog("setCurColor="+c);
        mModel.setCurColor(c);
    }
    void removeDead(char x,int y) {
        String cmd =x+""+y;
        sendCommand(cmd.toUpperCase());
    }
    boolean putChess(char x,int y) {
        if(isMyTurnToPutChess()) {
            String cmd =x+""+y;
            setLastCommand(IgsMessage.STEP);
            sendCommand(cmd.toUpperCase());
            return true;
        }
        return false;
    }
    /*
    sendToIgs stats yamo
    9 Player:      yamo
    9 Game:        go (1)
    9 Language:    default
    9 Rating:      1k*   0
    9 Rated Games:    2154
    9 Rank:  1k* 28
    9 Wins:       1165
    9 Losses:     1047
    9 Idle Time:  (On server) 2m
    9 Address:  panda@.jp
    9 Country:  Japan
    9 Community:  -
    9 Reg date: Mon Jan  1 00:00:00 1900
    9 Info:  1/7-1/10
    9 Defaults (help defs):  time 1, size 19, byo-yomi time 10, byo-yomi stones 2
    1 5
    */

    void handlePollingStatsData() {
        ArrayList<String> pdata = mPollData.getPollLines();
        int sz = pdata.size();
        int i = 0;
        IgsPlayer player = (IgsPlayer)(mPollData.getParamObj(1));
        LogLog("handlePollingStatsData");
        while(i<sz) {
            String line = pdata.get(i);
            LogLog("parsing "+line);
            char[] cs = line.toCharArray();
            if(line.indexOf("Player:")!=-1) {
                player.name = IgsHelper.elementS(cs,2,' ',(char)0);
            } else if(line.indexOf("Country:")!=-1) {
                player.country = IgsHelper.elementS(cs,2,' ',(char)0);
            } else if(line.indexOf("Wins:")!=-1) {
                player.wins = IgsHelper.elementI(cs,2,' ',(char)0);
            } else if(line.indexOf("Losses:")!=-1) {
                player.loses = IgsHelper.elementI(cs,2,' ',(char)0);
            } else if(line.indexOf("Idle Time:")!=-1) {
                player.idle = IgsHelper.elementS(cs,5,' ',(char)0);
            } else if(line.indexOf("Rank:")!=-1) {
                player.rank = IgsHelper.elementS(cs,2,' ',(char)0);
            }
            i++;
        }
    }

    void rank(String s) {
        sendCommand("rank "+s);
    }

    void handlePollingStatusData() {
        ArrayList<String> pdata = mPollData.getPollLines();
        int sz = pdata.size();
        int i = 0;
        int gameno = mPollData.getParamInt(1);
        IgsGame game = (IgsGame)(mPollData.getParamObj(1));
        boolean bb = false;
        char cc = 'W';
        LogLog("igs","handlePollingStatusData");
        while(i<sz) {
            String line = pdata.get(i);
            if(line.indexOf(":")!=-1) {
                parseStatus(line,game);
            } else {
                char[] cs = line.toCharArray();
                game.setName(cc,IgsHelper.elementS(cs,1,' ',(char)0));
                game.setRank(cc,IgsHelper.elementS(cs,2,' ',(char)0));
                game.setCaptured(cc,IgsHelper.elementI(cs,3,' ',(char)0));
                game.setTimeLeft(cc,IgsHelper.elementI(cs,4,' ',(char)0));
                game.setSeconds(cc,IgsHelper.elementI(cs,5,' ',(char)0));
                if( cc=='W') {
                    cc='B';
                }
            }
            i++;
        }
        if(getPollType()==IgsMessage.LOOK_M) {
            sendMessage(MSG_IGS_LOOK_STORED);
        }
    }

    /* status response
     *
    1 5
    22 SHINDO 6k* 0 596 22 T 6.5 0
    22 y37k42 6k* 1 509 11 T 6.5 0
    22  0: 2120000012222222212
    22  1: 0111101012222201222
    22  2: 2001001011221202111
    22  3: 0201021126222220001
    22  4: 0010002212221102220
    22  5: 1111220222221022202
    22  6: 2221222212222022022
    22  7: 2222210222222222022
    22  8: 2121210222222220112
    22  9: 2100020226222226012
    22 10: 2022222222222002012
    22 11: 2202222222222011012
    22 12: 2112020022222210112
    22 13: 2222201100222122022
    22 14: 2222011211022222222
    22 15: 2110201121101121222
    22 16: 2100200122001002122
    22 17: 2210221222220220022
    22 18: 2122222222222222222
    1 5
     *
     */
    void parseStatus(String line, IgsGame game) {
        if(line.indexOf(":")!=-1) {
            int x,y;
            x=y=-1;
            char[] cs = line.toCharArray();
            int len = cs.length;
            int i = 0;
            while(i<len) {
                char c = cs[i];
                if(c==':') {
                    int bi;
                    int count = 1;
                    bi=i-1;
                    if(isDigit(cs[bi-1])) {
                        bi--;
                        count++;
                    }
                    y = Integer.parseInt(new String(cs,bi,count));
                }
                if(isDigit(c)) {
                    if(y>=0) {
                        x++;
                        game.setMap(x,y,c);
                        if(x==18) break;
                    }
                }
                i++;
            }
        }
    }

    /* help observe
     *
    Usage:  observe <game number>
       The 'observe' command is used to observe a game, or games, in progress.
       To see a games listing, enter:   games
       Then choose a game you wish to observe, then enter:  observe <game number>
            Example:  observe 56

       After you start observing a game, you will be listed in the 'who' list
       with a number next to your name under 'Info', as observing game <number>.

       To stop observing a game, enter the same command again:
            observe <game number you were observing>
            Example:  If you were observing game 56, and wanted to stop
                      observing game 56, enter:   observe 56
            Or, you can use:  unobserve

     Some clients have "time control" to compensate for "net lag". These clients
     are able to time a players move starting from when a move is made, not when
     the signal reaches IGS. In such cases the time, or clock, appears to jump
     backward as each correction is made. Some people misinterpret the time
     correction as cheating.

    See also:  all games match team time trail unobserve watching who
    8 File
     *
     */

    public void unobserve() {
        setLastCommand(IgsMessage.UNKNOWN);
        changeState(IDLE);
        sendCommand("unobserve");

    }
    public void observeGame(int gameno) {
        mModel.startObserve(gameno);
        changeState(PRE_OBSERVE);
        game(gameno);
    }

    void observe() {
        IgsGame game = getCurrentGame();
        setLastCommand(IgsMessage.OBSERVE);
        sendCommand("observe "+game.getGameNo());
    }


    /* game 97
    7 [##]  white name [ rk ]      black name [ rk ] (Move size H Komi BY FR) (###)
    7 [97]       a1128 [ 4k*] vs.        YM03 [ 5k*] (147   19  2  0.5 10  I) (  0)
    */
    public void game(int gameno) {
        setLastCommand(IgsMessage.GAME);
        sendCommand("game "+gameno);
    }

    public IgsGame getCurrentGame() {
        return mModel.getCurrentGame();
    }

    /*
    15 Game 56 I: moti25 (1 484 2) vs s7623 (5 415 16)
    15 138(B): K9 L8 J8 K7 K8
    1 8
    15 Game 56 I: moti25 (5 483 1) vs s7623 (5 415 16)
    15 139(W): J10 H12 H10 H11 J11
    2
    1 8
    15 Game 56 I: moti25 (5 483 1) vs s7623 (5 393 15)
    15 140(B): O2
    2
    1 8
    15 Game 56 I: moti25 (5 479 0) vs s7623 (5 393 15)
    15 141(W): P3
    2
    1 8
    15 Game 56 I: moti25 (5 600 25) vs s7623 (5 389 14)
    15 142(B): Q13
    2
    1 8
     *
     */

    /*
    2
    15 Game 742 I: siyuango (0 4500 -1) vs motosure (0 4500 -1)
    9 Handicap and komi are disable.
    15 Game 742 I: siyuango (0 4500 -1) vs motosure (0 4500 -1)
    15   0(B): Handicap 4
    2 Game saved.
    1 6
    9 Match [742] with siyuango in 75 accepted.
    9 Please use say to talk to your opponent -- help say.
    1 6

    */
    void handleMove(String line) {
        parseMove(line);
    }
    public void parseMove(String line) {
        //Game 742 I: siyuango (0 4500 -1) vs motosure (0 4500 -1)
        if(mState == IN_WAIT && line.indexOf(getMyName())!=-1) {
            char[] cs = line.toCharArray();
            int gameno = IgsHelper.elementI(cs,2,' ',(char)0);
            mModel.startMatch(gameno);
            changeState(IN_GAME);
            setLastCommand(IgsMessage.UNKNOWN);
            IgsGame game = getCurrentGame();
            game(gameno);
            sendTalk("Hi!");
            setCurColor('B');
            sendMessage(MSG_GAME_START);
        } else if(mState == IN_GAME || mState == IN_OBSERVE || mState == PRE_OBSERVE) {
            IgsGame game = getCurrentGame();
            LogLog("parsing:"+line);
            char[] cs = line.toCharArray();
            if(line.indexOf("(B):")!=-1 ||
                    line.indexOf("(W):")!=-1) {
                if(mState==IN_WAIT) return;
                String color = IgsHelper.elementS(cs,0,'(',')');
                //15   2(B): Pass
                if(line.indexOf("Pass")!=-1) {
                    String cc = IgsHelper.elementS(cs,0,'(',')');
                    if(cc.charAt(0)=='B')
                        setCurColor('W');
                    else
                        setCurColor('B');
                    sendMessage(MSG_IGS_PASS);
                    //15   0(B): Handicap 4
                } else if (line.indexOf("Handicap")!=-1) {
                    int h = IgsHelper.elementI(cs,3,' ',(char)0);
                    LogLog("handicap="+h);
                    game.setHandicap(h);
                    MyMsg mm = new MyMsg(3,3);
                    Message message = new Message();
                    message.what = MSG_IGS_STEP;
                    message.obj=mm;
                    setCurColor('W');
                    sendMessage(message);
                    //15   0(B): Handicap 3
                } else {
                    //get move and cpatured
                    String s1 = line.substring(line.indexOf(":")+2);
                    //split move and captured
                    String[] s2 = s1.split(" ");
                    String xy = s2[0];
                    char x = xy.charAt(0);
                    int y = Integer.parseInt(xy.substring(1));
                    char cc = (color.charAt(0)=='B')?'0':'1';
                    if(cc=='0') setCurColor('W');
                    if(cc=='1') setCurColor('B');

                    LogLog("igs","move "+x+","+y+","+cc);
                    game.move(x,y,cc);
                    char x1=x;
                    int y1=y;
                    int i=1;
                    while(i<s2.length) {
                        xy = s2[i];
                        x = xy.charAt(0);
                        y = Integer.parseInt(xy.substring(1));
                        game.capture(x,y);
                        i++;
                    }
                    int ix=(int)(x1-'A');
                    if(x1>='J') ix--;
                    if(mState==IN_OBSERVE || mState==IN_GAME) {
                        MyMsg mm = new MyMsg(ix,y1-1);
                        Message message = new Message();
                        message.what = MSG_IGS_STEP;
                        message.obj=mm;
                        sendMessage(message);
                    }
                }
                //15 Game 742 I: siyuango (0 4500 -1) vs motosure (0 4500 -1)
            } else if(line.indexOf("Game")!=-1) {
                int gameNo = IgsHelper.elementI(cs,2,' ',(char)0);
                int wSeconds = IgsHelper.elementI(cs,0,' ',')');
                int bSeconds = IgsHelper.elementI(cs,1,' ',')');
                int wCaptured = IgsHelper.elementI(cs,0,'(',' ');
                int bCaptured = IgsHelper.elementI(cs,1,'(',' ');
                int wTimeLeft=IgsHelper.elementI(cs,6,' ',(char)0);
                int bTimeLeft=IgsHelper.elementI(cs,11,' ',(char)0);
                String wName = IgsHelper.elementS(cs,4,' ',(char)0);
                String bName = IgsHelper.elementS(cs,9,' ',(char)0);
                game.setGameNo(gameNo);
                game.setName('B',bName);
                game.setName('W',wName);
                game.setSeconds('B',bSeconds);
                game.setSeconds('W',wSeconds);
                game.setTimeLeft('B',bTimeLeft);
                game.setTimeLeft('W',wTimeLeft);
                game.setCaptured('B',bCaptured);
                game.setCaptured('W',wCaptured);
            }
        }
    }

    public void match(String name) {
        setLastCommand(IgsMessage.MATCH);
        changeState(IN_WAIT);
        sendCommand("match "+name+" W");
    }
    public void match(String name, String color,int size,int ttime,int minutes) {
        String cmd = "match "+name+" "+color+" "+size+ " "+ttime+" "+minutes;
        setLastCommand(IgsMessage.MATCH);
        changeState(IN_WAIT);
        mThread.sendData(cmd+"\n");
    }

    /*
    match Lanky48
    9 Requesting match in 75 min with Lanky48 as White.
    1 5
    2
    15 Game 197 I: Lanky48 (0 4500 -1) vs motosure (0 4500 -1)
    9 Handicap and komi are disable.
    15 Game 197 I: Lanky48 (0 4500 -1) vs motosure (0 4500 -1)
    15   0(B): Handicap 7
    2 Game saved.
    1 6
    9 Match [197] with Lanky48 in 75 accepted.
    9 Please use say to talk to your opponent -- help say.
    1 6
    51 Say in game 197

    19 *Lanky48*: Hi!
    1 6
    15 Game 197 I: Lanky48 (0 4498 -1) vs motosure (0 4500 -1)
    15   1(W): K17
    */

    void parseMatch(String line) {
    }
    public void sendCommand(String cmd) {
        LogLog("igs","sendToIgs "+cmd);
				if(mThread==null) return;
        mThread.sendData(cmd+"\n");
    }

    void statsMe() {
        stats(mModel.mMe);
    }
    void stats(IgsPlayer player) {
        mPollData.reset();
        mPollData.setPollType(IgsMessage.STATS);
        mPollData.setParamObj(1,player);
        setLastCommand(IgsMessage.STATS);
        polling(true);
        sendCommand("stats "+player.name);
    }

    IgsField mWhoInfo=null;
    IgsField mWhoName=null;
    IgsField mWhoIdle=null;
    IgsField mWhoRank=null;
    IgsField mWhoOffset=null;
    boolean isNextWhoValid=false;

    /* output of "who"
    27  Info       Name       Idle   Rank |  Info       Name       Idle   Rank
    27  Q  --  268 Albatross3  1s     9k* |   X 22   -- powang      1s     8k*
    27     --   -- f591tn      9s    11k* |  Q  12   -- guest3047   5s     NR
    27  Q  --  697 TFK2187     0s     3d* |  Q  12   -- guest3048  10s     NR
    27  Q  --  697 pero66      0s     3d* |  Q  12   -- guest3049   8s     NR
    27  Q  --  502 gouiti      0s     5k* |     --   -- autochk01  18s     NR
    27  Q  --   -- guest3050  11s     NR  |  Q  --   -- usa131      5s    11k*
    27  Q  --   -- HYHTM       2s    12k* |  Q  --   -- hiiragisan  2s     6k*
    27  Q  --   -- guest3051   4s     NR  |  QX --   -- yoshi555    0s     4k*
    27  Q  --   -- tummy221    1s     8k* |                              Logon
    27                 ******** 2794 Players 781 Total Games ********
    1 5
    */
    void parseWhoInfo(String info,IgsPlayer player) {
        char[] cs = info.toCharArray();
        int len = cs.length;
        int i=0;
        String flags = null;
        int obs = -1;
        int game = -1;
        int j = 0;

        char[] v = new char[20];
        while(i<len) {
            char c = cs[i];
            if(c>='0' && c<='9') {
                j=0;
                while(i<len) {
                    c = cs[i];
                    if(c>='0' && c<='9') {
                        v[j]=c;
                        j++;
                    } else break;
                    i++;
                }
                v[j]=0;
                String sv = new String(v,0,j);
                int iv = Integer.parseInt(sv);
                if(obs==-1) obs = iv;
                else
                    game = iv;
                continue;
            } else if(c!=' ' && c!='-') {
                j=0;
                while(i<len) {
                    c = cs[i];
                    if(c>='0' && c<='9') break;
                    if(c==' ') break;
                    v[j]=c;
                    i++;
                    j++;
                }
                v[j]=0;
                flags = new String(v);
                continue;
            } else if(c=='-') {
                if(obs==-1) obs=0;
            }
            i++;
        }
        if(obs==0) obs=-1;
        player.flags=flags;
        player.obs_id=obs;
        player.game_id=game;
    }
    IgsPlayer[] parseWho(String sline) {
        //27 X 39   -- Marin       5m     2k* |     --   53 KT713      18s     2d*
        IgsPlayer[] players = null;
        //parser header
        if(sline.indexOf(" Info ")!=-1 && sline.indexOf(" Name ")!=-1) {
            String line=sline.substring(sline.indexOf("Info"));
            mWhoInfo = IgsHelper.calField(line,"Info","Name");
            mWhoName = IgsHelper.calField(line,"Name","Idle");
            mWhoIdle = IgsHelper.calField(line,"Idle","Rank");
            mWhoRank = IgsHelper.calField(line,"Rank","|");
            isNextWhoValid = (mWhoInfo!=null) && (mWhoName!=null)
                             && (mWhoIdle!=null) && (mWhoRank!=null);
            int o1 = sline.indexOf("Info");
            int o2 = sline.lastIndexOf("Info");
            mWhoOffset = new IgsField(o1,o2);
            //parse who data
        } else if(isNextWhoValid) {
            players = new IgsPlayer[2];
            players[0]=null;
            players[1]=null;
            //first col
            int o1 =mWhoOffset.beginIndex;
            int o2 =mWhoOffset.endIndex;
            IgsPlayer player = new IgsPlayer();
            String info = null;
            info = sline.substring(o1+mWhoInfo.beginIndex,o1+mWhoInfo.endIndex).trim();
            parseWhoInfo(info,player);
            player.name = sline.substring(o1+mWhoName.beginIndex,o1+mWhoName.endIndex).trim();
            player.idle = sline.substring(o1+mWhoIdle.beginIndex,o1+mWhoIdle.endIndex).trim();
            player.rank = sline.substring(o1+mWhoRank.beginIndex,o1+mWhoRank.endIndex).trim();
            String s="";
            if(player.name.trim().length()!=0) {
                players[0]=player;
                s=player.flags+","+player.obs_id+","+player.game_id+",";
                s+=player.name+",";
                s+=player.idle+",";
                s+=player.rank+",";
            }
            if(sline.indexOf("|")!=-1) {
                player = new IgsPlayer();
                info = sline.substring(o2+mWhoInfo.beginIndex,o2+mWhoInfo.endIndex).trim();
                parseWhoInfo(info,player);
                player.name = sline.substring(o2+mWhoName.beginIndex,o2+mWhoName.endIndex).trim();
                player.idle = sline.substring(o2+mWhoIdle.beginIndex,o2+mWhoIdle.endIndex).trim();
                player.rank = sline.substring(o2+mWhoRank.beginIndex,sline.length()-1).trim();
                if(player.name.trim().length()!=0) {
                    players[1]=player;
                    s+=" | ";
                    s+=player.flags+","+player.obs_id+","+player.game_id+",";
                    s+=player.name+",";
                    s+=player.idle+",";
                    s+=player.rank+",";
                }
            }

        }
        return players;
    }

    int getPollType() {
        return mPollData.getPollType();
    }

    void sendMessage(int id,Bundle msg) {
        Message message = new Message();
        message.what = id;
        message.obj=msg;
        sendMessage(message);
    }
    void sortGame() {
        mModel.sortGame();
    }

    void sortPlayer() {
        mModel.sortPlayerForAutoPair();
        //mModel.sortPlayer();
    }
    void sortPlayerForAutoPair() {
        mModel.sortPlayerForAutoPair();
    }
    void handlePrompt(String str) {
        if(isPolling()) {
            handlePollingData();
            polling(false);
        }
        if(bGamesUpdated==true) {
            sortGame();
	    bLoading=false;
            sendMessage(MSG_LIST_GAME_DONE);
            bGamesUpdated=false;
        }
        if(bPlayersUpdated==true) {
            sortPlayer();
	    bLoading=false;
            sendMessage(MSG_LIST_PLAYER_DONE);
            bPlayersUpdated=false;
        }
        if(bScoreBegin) {
            bScoreBegin=false;
            sendMessage(MSG_SCORE_INFO);
        }
        switch(getLastResponse()) {
        case IgsMessage.STORED:
            setLastCommand(IgsMessage.UNKNOWN);
            sendMessage(MSG_IGS_STORED);
            break;
        }
        switch(getLastCommand()) {
        case IgsMessage.STATS:
            setLastCommand(IgsMessage.UNKNOWN);
            sendMessage(MSG_IGS_STATS);
            break;
        case IgsMessage.GAME:
            setLastCommand(IgsMessage.UNKNOWN);
            if(mState==PRE_OBSERVE) {
                observe();
            }
            break;
        case IgsMessage.OBSERVE:
            setLastCommand(IgsMessage.UNKNOWN);
            if(mState==PRE_OBSERVE) {
                movesCur();
            }
            break;
        case IgsMessage.MOVES:
            setLastCommand(IgsMessage.UNKNOWN);
            if(mState==PRE_OBSERVE) {
                changeState(IN_OBSERVE);
                sendMessage(MSG_OBSERVE_OK);
            }
            break;
        }
        setLastResponse(IgsMessage.PROMPT);
    }

    void handlePollingData() {
        int cmd = getPollType();
        switch (cmd) {
        case IgsMessage.STATUS:
        case IgsMessage.LOOK_M:
            handlePollingStatusData();
            break;
        case IgsMessage.STATS:
            handlePollingStatsData();
            break;
        }

    }

    void addPollLine(String line) {
        mPollData.addPollLine(line);
    }
    void sendMessage(int msgid) {
        LogLog("igs","Controller: send message="+msgid);
        Message msg = new Message();
        msg.what = msgid;
        sendMessage(msg);
    }

    void sendMessage(Message msg) {
        if(mViewHandler==null) {
        } else
            mViewHandler.sendMessage(msg);
    }
    String mLastToggle="singlegame";

    void setupToggle() {
        sendCommand("toggle quiet on");
        sendCommand("toggle open on");
        sendCommand("toggle looking on");
        sendCommand("toggle client on");
        sendCommand("toggle chatter on");
        sendCommand("toggle kibitz on");
        sendCommand("toggle singlegame on");
    }
    int inSilentReconnect = 0;
    int mMaxSilentRetries = 20;
    boolean bBrokenEventInjected = false;
    public void silentReconnect() {
        //Toast.makeText(this, getString(R.string.silentreconn), Toast.LENGTH_LONG).show();
        LogLog("do Silent Reconnect "+inSilentReconnect);
        inSilentReconnect++;
        //startSilentTimer(mSilentTimeout);
        reconnectIgs();
    }
    public void onSilentError() {
        LogLog("Silent Reconnect failed");
    }
    class EvtListener implements IgsListener {
        public void onConnected() {
            sendMessage(MSG_CONNECT_OK);
        }
        public void onConnectTimeOut() {
            LogLog("onConnectTimeout");
            if(inSilentReconnect<mMaxSilentRetries && mViewHandler!=null) {
                mViewHandler.postDelayed(new Runnable() {
                    public void run() {
                        silentReconnect();
                    }
                },1000);
            } else {
                onSilentError();
            }

        }
        public void onDisconnected() {
            changeState(OFFLINE);
            LogLog("onDisconnected");
            if(bBrokenEventInjected==true) return;
            bBrokenEventInjected=true;
            sendMessage(MSG_IGS_CONN_BROKEN);
        }
        public void onLoginOK() {
            LogLog("igs","Login OK!");
            changeState(CONNECTED);
            setupToggle();
        }

        public void onLineReceived(String str) {
            char[] line = str.toCharArray();
            if(str.length()<=2) return;
            //LogLog(str);
            Integer cmd = IgsHelper.elementI(line,0,' ',(char)0);
            boolean bSkipHandle = false;
            if(isPolling()==true) {
                if(cmd == getPollType() || (cmd==9 && getPollType()==IgsMessage.STATS)
                        || cmd==22 && getPollType()==IgsMessage.LOOK_M) {
                    addPollLine(str);
                    bSkipHandle = true;
                }
            }
            if(bSkipHandle) return;
            switch(cmd) {
            case 1:
                LogLog("onLineReceived:"+str);
                handlePrompt(str);
                break;
            case 7:
                handleGames(str);
                break;
            case 9:
                LogLog("onLineReceived:"+str);
                handleInfo(str);
                break;
            case 15:
                handleMove(str);
                break;
            case 19:
                handleSay(str);
                break;
            case 18:
                LogLog("onLineReceived:"+str);
                handleStored(str);
                break;
            case 27:
                handleWho(str);
                break;
            }
            dumpToFile(str);
        }
    }

    /*
    22 siyuango 14k* 0 4488 25 F 0.5 4
    22 motosure NR  0 4499 25 F 0.5 4
    22  0: 2222222222222222222
    22  1: 2222222222222222222
    22  2: 2222222222222222222
    22  3: 2220222226222220222
    22  4: 2222222222222222222
    22  5: 2222222222222222222
    22  6: 2222222222222222222
    22  7: 2222222222212222222
    22  8: 2222222222222222222
    22  9: 2226222226222226220
    22 10: 2222222222222222222
    22 11: 2222222222222222222
    22 12: 2222222222222222222
    22 13: 2222222222222222222
    22 14: 2222222222222222222
    22 15: 2220222226222220222
    22 16: 2222222222222222222
    22 17: 2222222222222222222
    22 18: 2222222222222222222
    13 The date on the game is Wed Dec 14 15:31:14 2011
    1 5
    */


    void handleMatchResponse(String line) {
        //failed
        if(line.indexOf("declines your request")!=-1) {
            setLastCommand(IgsMessage.UNKNOWN);
            changeState(IDLE);
        }
    }

    void handleSay(String line) {
        Bundle msg = new Bundle();
        msg.putString("say",line);
        sendMessage(MSG_IGS_SAY,msg);
    }
    void resign() {
        sendCommand("resign");
    }

    void handleResign(String name) {
        Bundle msg = new Bundle();
        msg.putString("name",name);
        sendMessage(MSG_IGS_RESIGN,msg);
    }
    /*
    18 siyuango-motosure
    18 Found 1 stored games.
    1 5
    */


    String mStoredGame=null;
    String getStoredGame() {
        return mStoredGame;
    }
    void setStoredGame(String s) {
        mStoredGame=s;
    }

    void lookStored() {
        mPollData.reset();
        mPollData.setPollType(IgsMessage.LOOK_M);
        mPollData.setParamObj(1,getCurrentGame());
        setLastCommand(IgsMessage.LOOK_M);
        //prepare the game for restore
        mModel.startMatch(0);
        sendCommand("look "+getStoredGame());
        polling(true);
    }

    void restoreGame() {
        mSubState=SS_RESTORE;
        setLastCommand(IgsMessage.LOAD);
        sendCommand("load "+getStoredGame());
    }
    //18 siyuango-motosure
    void handleStored(String line) {
        setLastResponse(IgsMessage.STORED);
        if(line.indexOf(getMyName())!=-1) {
            mStoredGame=IgsHelper.elementS(line,1,' ');
        }
    }
    void checkStored() {
        setStoredGame(null);
        setLastCommand(IgsMessage.STORED);
        sendCommand("stored");
    }
    int mSoTimeout = 60*10*1000;

    /*
    12-10 10:55:08.735  2047  2118 E igs     : IgsController:9 You can check your score with the score command, type 'done' when finished.
    12-10 10:55:08.735  2047  2118 E igs     : Controller: send message=18
    12-10 10:55:08.735  2047  2118 E igs     : IgsController:parsing:15 Game 573 I: siyuango (0 4492 -1) vs motosure (0 4480 -1)
    12-10 10:55:08.735  2047  2118 E igs     : IgsController:parsing:15  18(B): Pass
    12-10 10:55:08.735  2047  2118 E igs     : IgsController:setCurColor=W
    12-10 10:55:08.735  2047  2118 E igs     : Controller: send message=19
    12-10 10:55:08.742  2047  2047 E igs     : IgsGoView received message=18
    12-10 10:55:08.742  2047  2047 E igs     : IgsGoView received message=19
    12-10 10:55:08.844  2047  2047 E igs     : isMyTurnToPutChess=false
    12-10 10:55:09.102  2047  2047 E igs     : isMyTurnToPutChess=false
    12-10 10:55:09.273  2047  2047 E igs     : isMyTurnToPutChess=false
    12-10 10:55:12.320  2047  2118 E igs     : IgsController:9 Removing @ N12
    12-10 10:55:24.911  2047  2118 E igs     : IgsController:9 Removing @ L9
    12-10 10:55:31.313  2047  2118 E igs     : IgsController:9 Removing @ N7
    12-10 10:55:34.125  2047  2118 E igs     : IgsController:9 Board is restored to what it was when you started scoring
    12-10 10:55:34.125  2047  2118 E igs     : IgsController:9 Please type 'done again.
    */


    String mScoreStr="";
    boolean bScoreBegin=false;


    String mRemoveDeads="";
    int mSubState = SS_NONE;
    public final static int SS_NONE=0;
    public final static int SS_SCORE=1;
    public final static int SS_RESTORE=2;
    void handleInfo(String line) {
        //9 Your rank is set to 6d
        if(line.indexOf("Your rank is set to")!=-1) {
            char[] cs = line.toCharArray();
            String s=IgsHelper.elementS(cs,6,' ',(char)0);
            mModel.setMyRank(s);
            sendMessage(MSG_IGS_RANK);
        }
        switch (mState) {
        case IDLE:
            /*
            9 motosure has restarted your game.
            15 Game 275 I: siyuango (0 4472 -1) vs motosure (0 4486 -1)
            9 Handicap and komi are disable.
            15  11(W): O6
            1 6
            */
            if(line.indexOf(getMyName())!=-1 && mSubState==SS_RESTORE) {
                mSubState=SS_NONE;
                changeState(IN_GAME);
                sendMessage(MSG_IGS_RESTORED);
            }
        case IN_WAIT:
            if(getLastCommand()==IgsMessage.MATCH) {
                handleMatchResponse(line);
            }
            if(line.indexOf("Adding game to observation list")!=-1) {
                if(getLastCommand()==IgsMessage.OBSERVE) {
                    //observe ok;
                }
            }
            break;
            //9 {Game 415: akamaru vs kazu50 : Black resigns.}
        case IN_OBSERVE:
            if(line.startsWith("9 {Game")) {
                if(line.indexOf("Black resigns")!=-1) {
                } else if(line.indexOf("White resigns")!=-1) {
                }
            }
            break;
        case IN_GAME:
            if(line.indexOf("has resigned the game.")!=-1) {
                char[] cs = line.toCharArray();
                String name = IgsHelper.elementS(cs,1,' ',(char)0);
                handleResign(name);
            }
            if(line.indexOf("You can check your score with the score command")!=-1) {
                mSubState = SS_SCORE;
                IgsGame game = getCurrentGame();
                game.saveBoard();
                sendMessage(MSG_IGS_SCORE_BEGIN);
            }
            /*
            9 The current score is:
            9 White: -5.5, Black 357.0, Dame 0
            9 The final score will be:
            9 White: -5.5, Black 357.0, Dame 0
            */
            if(bScoreBegin) {
                mScoreStr+=line;
            }
            if(bScoreBegin==false && line.indexOf("The current score is:")!=-1) {
                mScoreStr+=line;
                bScoreBegin=true;
            }


            //9 Removing @ N10
            if(line.indexOf("9 Removing @")!=-1) {
                char[] cs = line.toCharArray();
                String xy=IgsHelper.elementS(cs,3,' ',(char)0);
                char x = xy.charAt(0);
                int y = Integer.parseInt(xy.substring(1));
                IgsGame game = getCurrentGame();
                game.capture(x,y);
                mRemoveDeads+=xy+" ";
                sendMessage(MSG_IGS_REMOVE_DEAD);
            }
            /*
            1 7
            9 Board is restored to what it was when you started scoring
            9 Please type 'done again.
            1 7
            */
            if( mSubState==SS_SCORE && line.indexOf("Board is restored to")!=-1) {
                IgsGame game = getCurrentGame();
                game.restoreBoard();
                sendMessage(MSG_IGS_UNDO_SCORE);


            }
            /*
            12-14 11:41:35.823  1973  2662 E igs     : IgsController:onLineReceived:9 Your opponent has lost his/her connection.
            12-14 11:41:35.823  1973  2662 E igs     : IgsController:onLineReceived:1 6
            12-14 11:41:35.823  1973  2662 E igs     : IgsController:onLineReceived:9 Game has been adjourned.
            12-14 11:41:35.823  1973  2662 E igs     : IgsController:onLineReceived:9 Game saved.
            12-14 11:41:35.823  1973  2662 E igs     : IgsController:onLineReceived:1 5
            */
            if(line.indexOf("Your opponent has lost his/her connection")!=-1) {
                mSubState=SS_RESTORE;
                sendMessage(MSG_IGS_OPP_LOST);
            }

            if(mSubState==SS_RESTORE && line.indexOf("has restarted your game.")!=-1) {
                mSubState=SS_NONE;
                sendMessage(MSG_IGS_OPP_RESTORE);
            }


            break;
        case CONNECTED:
            if(line.indexOf(mLastToggle)!=-1) {
                mThread.setSoTimeout(mSoTimeout);
                bBrokenEventInjected=false;
                if(inSilentReconnect > 0) {
                    inSilentReconnect = 0;
                    sendMessage(MSG_SILENT_LOGIN_OK);
                } else {
                    sendMessage(MSG_LOGIN_OK);
                }
                changeState(IDLE);
            }
            break;
        }
        /*
        9 Match[19x19] in 75 minutes requested with motosure as Black.
        9 Use <match motosure W 19 75 0> or <decline motosure> to respond.
        2
        1 5
        */
        if(line.indexOf("> or <decline")!=-1 && line.indexOf("Use <match")!=-1) {
            char[] cs = line.toCharArray();
            Bundle msg = new Bundle();
            msg.putString("name",IgsHelper.elementS(cs,3,' ',(char)0));
            msg.putString("color",IgsHelper.elementS(cs,4,' ',(char)0));
            msg.putInt("size",IgsHelper.elementI(cs,5,' ',(char)0));
            msg.putInt("time",IgsHelper.elementI(cs,6,' ',(char)0));
            msg.putInt("seconds",IgsHelper.elementI(cs,0,' ','>'));
            if(msg.getInt("size")==19) sendMessage(MSG_GET_INVITE,msg);
        }
    }


    void acceptInvite(Bundle msg) {
        match(msg.getString("name"),msg.getString("color"),msg.getInt("size"),
              msg.getInt("time"),msg.getInt("seconds"));
    }
    void declineInvite(Bundle msg) {
    }

    boolean bLoading=false;
    boolean isLoading(){
    	return bLoading;
    }
    void listGames() {
    	bLoading=true;
        setLastCommand(IgsMessage.GAMES);
        sendCommand("games");
        mModel.clearGames();
    }

    /*
    7 [##]  white name [ rk ]      black name [ rk ] (Move size H Komi BY FR) (###)
    7 [484]   Samurai55 [ 2d*] vs.        stgl [ 1d*] (159   19  0  0.5 15  I) (  0)
    7 [537]   hiroshigo [ 3d*] vs.  niwanotoki [ 2d*] (310   19  0 -5.5 10  I) (  1)
    7 [128]     DaVinci [10k*] vs.      KT1421 [10k*] (310   19  0  6.5 10  I) (  0)
    */
    IgsGameItem parseGames(String line) {
        IgsGameItem igame = null;
        igame = new IgsGameItem();
        char[] cs = line.toCharArray();
        igame.id = IgsHelper.elementI(cs,0,'[',']');
        igame.wName = IgsHelper.elementS(cs,1,' ',' ');
        igame.wrk = IgsHelper.elementS(cs,1,'[',']');
        String nline = line.substring(line.indexOf("vs.")+3);
        cs = nline.toCharArray();
        igame.bName = IgsHelper.elementS(cs,0,' ',(char)0);
        igame.brk = IgsHelper.elementS(cs,0,'[',']');
        igame.obs = IgsHelper.elementI(cs,1,'(',')');
        nline = IgsHelper.elementS(cs,0,'(',')');
        cs = nline.toCharArray();
        igame.move = IgsHelper.elementI(cs,0,' ',(char)0);
        igame.size = IgsHelper.elementI(cs,1,' ',(char)0);
        igame.H = IgsHelper.elementI(cs,2,' ',(char)0);
        igame.Komi = IgsHelper.elementS(cs,3,' ',(char)0);
        igame.BY = IgsHelper.elementI(cs,4,' ',(char)0);
        igame.FR = IgsHelper.elementS(cs,5,' ',(char)0);
        return igame;
    }

    void dumpGameItem(IgsGameItem igame) {
        System.out.println(igame.id+","+
                           igame.wName+","+
                           igame.wrk+","+
                           igame.bName+","+
                           igame.brk+","+
                           igame.move+","+
                           igame.size+","+
                           igame.H+","+
                           igame.Komi+","+
                           igame.BY+","+
                           igame.FR+","+
                           igame.obs);
    }

    boolean bGamesUpdated=false;
    void handleGames(String line) {
        if(line.indexOf("[##]")!=-1 &&
                line.indexOf("(###)")!=-1) {
            //games header
        } else {
            IgsGameItem game = parseGames(line);
            if(game!=null) {
                //dumpGameItem(game);
                if((mState==PRE_OBSERVE||mState==IN_GAME)) {
                    LogLog("refresh the game info druing observe or in game");
                    IgsGame igame = getCurrentGame();
                    if(game.id==igame.getGameNo()) {
                        igame.setName('W',game.wName);
                        igame.setName('B',game.bName);
                        igame.setHandicap(game.H);
                        igame.setRank('W',game.wrk);
                        igame.setRank('B',game.brk);
                        if(igame.getName('W').equals(getMyName())) {
                            setMyColor('W');
                        } else
                            setMyColor('B');
                        if(mState==IN_GAME) {
                            sendMessage(MSG_GAME_INFO);
                        }
                    }
                } else {
                    bGamesUpdated=true;
                    mModel.addGame(game);
                }
            }

        }
    }
    void listPlayers(String param) {
    	bLoading=true;
        setLastCommand(IgsMessage.WHO);
        sendCommand("who "+param);
        mModel.clearPlayers();
    }
    boolean bPlayersUpdated=false;
    void handleWho(String line) {
        if(line.indexOf("Total Games ********")!=-1) {
            //end of who response
            System.out.println("Command WHO done.");
        } else {
            IgsPlayer[] players = parseWho(line);
            if(players!=null) {
                bPlayersUpdated=true;
                if(players[0]!=null) {
                    mModel.addPlayer(players[0]);
                }
                if(players[1]!=null) {
                    mModel.addPlayer(players[1]);
                }
            }

        }
    }

    void dumpToFile(String s) {
        /*
        try{
        BufferedWriter out = new BufferedWriter(new FileWriter("dump.txt",true));
        //someText.replaceAll("\n", System.getProperty("line.separator"));
        out.write(s+"\n");
        out.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
        */
    }

    boolean isDigit(char c) {
        if(c>='0' && c<='9') {
            return true;
        }
        return false;
    }

}
