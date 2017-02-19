package com.sureone.go;
import com.sureone.xHelper;
import android.util.Log;
import com.sureone.SgfParser;
import com.sureone.GoLogic;
import com.sureone.SgfTag;
import com.sureone.SgfHeader;
public class CLogicGo {
    public GoStepRecorder mStepRec=null;
    CWeiQiModel wqModel = null;
    boolean m_bServerMode = false;

    int mWhiteNum=0;
    int mBlackNum=0;
    int mWhiteDeadNum=0;
    int mBlackDeadNum=0;

    int game_size;
    public int m_nCurSide;
    int mKilledNumber=0;
    int mWhitePassSteps=0;
    int mBlackPassSteps=0;
    GoStep mCurStep=null;
    int mAnchor=-1;
    int mSgfStepCnt=0;
    GoLogic mGoLogic=null;

    boolean mAnchorSet=false;
    public CLogicGo() {

    }
    public void backToAnchor() {
        if(mAnchor!=-1) {
            while(mStepRec.size()>mAnchor) {
                this.back();
            }
            clearAnchor();
        }
    }
    public void setAnchor() {
        mAnchor = mSgfStepCnt;
    }

    public void clearAnchor() {
        mAnchor = -1;
    }

    public GoStep GetLastStep() {
        if(mStepRec.mSteps.size()>0)
            return this.mStepRec.mSteps.get(mStepRec.mSteps.size()-1);
        else
            return null;
    }
    public void SetSize(int size) {
        game_size=size;
        wqModel=new CWeiQiModel(size);

    }
    public void SetServerMode(boolean bServerMode) {
        m_bServerMode=bServerMode;
    }

    public int GetGameSize() {
        return game_size;
    }

    void OnNextTurn(int side) {
        if(side==1) {
            m_nCurSide=2;
        } else {
            m_nCurSide=1;
        }
    }
    int last_dj_x;
    int last_dj_y;
    public void StartGame() {
        last_dj_x=-1;
        last_dj_y=-1;
        OnNextTurn(1);
        if(mStepRec==null)
            mStepRec=new GoStepRecorder();
        else
            mStepRec.clear();
    }

    public void MoveNoThink(int x,int y,int side) {
        wqModel.SetSide(x,y,side);
    }
    public int getSide(int x,int y) {
        return wqModel.GetSide(x, y);
    }
    public void Kill(int x,int y) {
        int s = wqModel.Kill(x,y);
        mCurStep.AddOp(GoOp.KILL, s, x, y);
    }

    public void AddKilledDragon(CDragon g) {

        for( int x=0; x<game_size; x++) {
            for( int y=0; y<game_size; y++) {
                if(g.IsInDragon(x,y)) {
                    mKilledNumber++;
                    Kill(x,y);
                }
            }
        }
    }

    public boolean Move(int x,int y,int side) {
        boolean bNewDragon = false;
        int bNotAllowed = 0;
        int bDaJie=0;
        mCurStep = new GoStep();
        if(wqModel.IsNull(x,y)==true) {
            do {
                CDragon myDragon=null;
                CDragon[] otherDragons=new CDragon[4];
                CDragon[] myDragons=new CDragon[4];
                CDragon nearDragon;
                int others_num=0;
                int my_num=0;
                nearDragon = wqModel.GetLeftDragon(x,y);
                if(nearDragon!=null) {
                    if(nearDragon.GetSide()==side) {
                        myDragons[my_num] = nearDragon;
                        my_num++;
                    } else {
                        otherDragons[others_num] = nearDragon;
                        others_num++;
                    }
                }

                nearDragon = wqModel.GetRightDragon(x,y);
                if(nearDragon!=null) {
                    if(nearDragon.GetSide()==side) {
                        int bdup=0;
                        if(my_num>0) {
                            for(int k=0; k<my_num; k++) {
                                if(myDragons[k]==nearDragon) {
                                    bdup=1;
                                    break;
                                }
                            }
                        }
                        if (bdup == 0) {
                            myDragons[my_num] = nearDragon;
                            my_num++;
                        }

                    } else {
                        int bdup=0;
                        if(others_num>0) {
                            for(int k=0; k<others_num; k++) {
                                if(otherDragons[k]==nearDragon) {
                                    bdup=1;
                                    break;
                                }
                            }
                        }
                        if (bdup == 0) {
                            otherDragons[others_num] = nearDragon;
                            others_num++;
                        }
                    }
                }

                nearDragon = wqModel.GetDownDragon(x,y);
                if(nearDragon!=null) {
                    if(nearDragon.GetSide()==side) {
                        int bdup=0;
                        if(my_num>0) {
                            for(int k=0; k<my_num; k++) {
                                if(myDragons[k]==nearDragon) {
                                    bdup=1;
                                    break;
                                }
                            }
                        }
                        if (bdup == 0) {
                            myDragons[my_num] = nearDragon;
                            my_num++;
                        }
                    } else {
                        int bdup=0;
                        if(others_num>0) {
                            for(int k=0; k<others_num; k++) {
                                if(otherDragons[k]==nearDragon) {
                                    bdup=1;
                                    break;
                                }
                            }
                        }
                        if (bdup == 0) {
                            otherDragons[others_num] = nearDragon;
                            others_num++;
                        }
                    }
                }

                nearDragon = wqModel.GetUpDragon(x,y);
                if(nearDragon!=null) {
                    if(nearDragon.GetSide()==side) {
                        int bdup=0;
                        if(my_num>0) {
                            for(int k=0; k<my_num; k++) {
                                if(myDragons[k]==nearDragon) {
                                    bdup=1;
                                    break;
                                }
                            }
                        }
                        if (bdup == 0) {
                            myDragons[my_num] = nearDragon;
                            my_num++;
                        }
                    } else {
                        int bdup=0;
                        if(others_num>0) {
                            for(int k=0; k<others_num; k++) {
                                if(otherDragons[k]==nearDragon) {
                                    bdup=1;
                                    break;
                                }
                            }
                        }
                        if (bdup == 0) {
                            otherDragons[others_num] = nearDragon;
                            others_num++;
                        }
                    }
                }

                //temply add the step into model
                wqModel.SetSide(x,y,side);
                if(my_num==0) {
                    myDragon = wqModel.newDragon();
                    myDragon.AddIntoDragon(x,y);
                    wqModel.SetSide(x,y,side);
                    wqModel.GetQiZi(x,y).SetDragon(myDragon.GetIndex());
                    myDragon.SetSide(side);
                    bNewDragon=true;
                } else {
                    myDragon = wqModel.GroupDragons(myDragons,my_num);
                    myDragon.AddIntoDragon(x,y);
                    myDragon.SetSide(side);
                }

                int myqi=0;
                CChessQi qi1=wqModel.GetDragonQi(myDragon);

                myqi=qi1.ShuQi();

                others_num--;
                int bKill=0;
                while(others_num>=0) {
                    CDragon otherDragon = otherDragons[others_num];
                    others_num--;
                    if(otherDragon!=null) {
                        CDragon g = otherDragon;
                        CChessQi qi=wqModel.GetDragonQi(otherDragon);


                        if(qi.ShuQi()==0) {
                            if(g.GetSize()!=1) {
                                AddKilledDragon(g);
                                wqModel.delDragon(g);
                                bKill=1;
                            }
                            //Handle the DaJie
                            else {
                                XY xy = new XY();
                                g.GetJieZi(xy);
                                if(xy.x==last_dj_x && xy.y==last_dj_y) {
                                    bNotAllowed=1;
                                    break;
                                } else {
                                    bDaJie=1;
                                    last_dj_x=x;
                                    last_dj_y=y;
                                    AddKilledDragon(g);
                                    wqModel.delDragon(g);
                                    bKill=1;
                                }
                            }
                        }
                    }
                }
                if(bNotAllowed==1) {
                    wqModel.GetQiZi(x,y).SetDragon(0);
                    wqModel.SetSide(x,y,0);
                    wqModel.delDragon(myDragon);
                    break;
                }
                if(bKill==0 && myqi==0) {
                    wqModel.GetQiZi(x,y).SetDragon(0);
                    wqModel.SetSide(x,y,0);
                    wqModel.delDragon(myDragon);
                    break;
                }
                if(my_num>0) {
                    for(int i=0; i<my_num; i++) {
                        CDragon g = myDragons[i];
                        wqModel.delDragon(g);
                    }
                    wqModel.AssignIndex(myDragon);
                }
                if(side == 2) {
                    this.mWhiteNum++;
                } else {
                    this.mBlackNum++;
                }
                if(bDaJie==0) {
                    last_dj_x=-1;
                    last_dj_y=-1;
                }

                OnNextTurn(side);
                mCurStep.AddOp(GoOp.SET, side,x,y);
                mStepRec.push(mCurStep);
                return true;

            } while(false);
        }
        return false;
    };

    public boolean UserPass(int side) {
        if(m_nCurSide==side) {
            if(1==side) {
                mBlackPassSteps++;
                OnNextTurn(1);
            } else {
                mWhitePassSteps++;
                OnNextTurn(2);
            }
            return true;
        }
        return false;

    }


    void back() {
        GoStep mStep=mStepRec.pop();
        if(mStep==null) return;
        for(int i=0; i<mStep.mOps.size(); i++) {
            GoOp op = mStep.mOps.get(i);
            if(op.op==GoOp.KILL) {
                wqModel.SetSide(op.x, op.y, op.sd);
            } else if(op.op==GoOp.SET) {
                wqModel.SetSide(op.x, op.y, 0);
            }
        }
        wqModel.RecalDragons();
    }

    public SgfParser mParser=null;
    public void loadSgf(String str) {
        SgfParser parser = new SgfParser(str);
        parser.parseHeader();
        int sz=Integer.parseInt(parser.mH.SZ);
        this.SetSize(sz);
        if(mGoLogic==null)
            mGoLogic = new GoLogic();
        else
            mGoLogic.clear();
        mGoLogic.setGameSize(sz);
        mParser = parser;


    }

    public boolean begin() {
        this.StartGame();
        return mParser.begin();
    }

    void setSide(int side,char cx,char cy) {
        int x = cx-'a';
        int y = cy-'a';
        this.Move(x, y, side);
    }
    public SgfTag mCurTag = null;
    int mStepNo = 0;

    public int getStepNo(int x,int y) {
        int no= mGoLogic.getStepNo(x,y);
        xHelper.log("goapp","getStepNo("+x+","+y+")="+no);
        return no;
    }
    public boolean next() {
        SgfTag t=new SgfTag();
        boolean b = mParser.next(t);
        if(b && t!=null) {
            if(t.t==SgfTag.TAG_B) {
                char[] c = t.v.toCharArray();
                mStepNo++;
                setSide(1,c[0],c[1]);
                mCurTag=t;
                mSgfStepCnt++;
                int x = c[0]-'a';
                int y = c[1]-'a';
                mGoLogic.setStepNo(mStepNo,x,y);
                xHelper.log("goapp","setStepNo("+x+","+y+")="+mStepNo);

            } else if(t.t==SgfTag.TAG_W) {
                char[] c = t.v.toCharArray();
                mStepNo++;
                setSide(2,c[0],c[1]);
                mCurTag=t;
                mSgfStepCnt++;
                int x = c[0]-'a';
                int y = c[1]-'a';
                mGoLogic.setStepNo(mStepNo,x,y);
                xHelper.log("goapp","setStepNo("+x+","+y+")="+mStepNo);
            } else if(t.t==SgfTag.TAG_END) {
                return false;
            } else if(t.t==SgfTag.TAG_C) {
                mCurTag=t;
                mSgfStepCnt++;
            }

        }
        return b;
    }
    public boolean last() {
        SgfTag t=new SgfTag();
        if(mCurTag!=null) {
            if(mCurTag.t==SgfTag.TAG_B) {
                char[] c = mCurTag.v.toCharArray();

                back();
                mSgfStepCnt--;
                //set(0,c[0],c[1]);
                mStepNo--;

            } else if(mCurTag.t==SgfTag.TAG_W) {
                char[] c = mCurTag.v.toCharArray();
                back();
                mSgfStepCnt--;
                mStepNo--;
                //set(0,c[0],c[1]);
            }
        }

        boolean b = mParser.prev(t);
        mCurTag=null;
        if(b==true && t.t!=SgfTag.TAG_START) {
            mCurTag=t;
        }
        return b;
    }

}
