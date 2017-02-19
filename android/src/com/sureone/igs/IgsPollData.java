package com.sureone.igs;
import java.util.ArrayList;
public class IgsPollData {
    public String mPollCommand;
    int mPollType=IgsMessage.UNKNOWN;
    int iparam1;
    int iparam2;
    String sparam1;
    String sparam2;
    Object oparam1;
    int getPollType() {
        return mPollType;
    }
    void setPollType(int v) {
        mPollType = v;
    }
    void setParamObj(int index,Object obj) {
        if(index==1) oparam1=obj;
    }
    Object getParamObj(int index) {
        if(index==1) return oparam1;
        return null;
    }
    void setParamInt(int index,int v) {
        if(index==1) iparam1 = v;
        if(index==2) iparam2 = v;
    }
    void setParamString(int index,String v) {
        if(index==1) sparam1 = v;
        if(index==2) sparam2 = v;
    }
    int getParamInt(int index) {
        if(index==1) return iparam1;
        if(index==2) return iparam2;
        return -1;
    }
    String getParamString(int index) {
        if(index==1) return sparam1;
        if(index==2) return sparam2;
        return null;
    }
    void reset() {
        mPollData.clear();
    }
    void addPollLine(String line) {
        mPollData.add(line);
    }
    ArrayList getPollLines() {
        return mPollData;
    }
    ArrayList<String> mPollData = new ArrayList<String> ();
}

