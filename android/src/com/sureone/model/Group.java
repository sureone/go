package com.sureone.model;

import com.sureone.User;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 8/17/13
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Group{
    public Long GROUP_ID;
    public String GROUP_NAME;
    public Long GROUP_SCORE;
    public Long MEMBERS_NUM;
    public Long GROUP_OWNER;
    public Long RANK_REQ;
    public String OWNER_NAME;
    public Long OWNER_RANK;
    public String GROUP_DESC;

    public ArrayList<User> members;

    public Long getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(Long GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public void setGROUP_NAME(String GROUP_NAME) {
        this.GROUP_NAME = GROUP_NAME;
    }

    public Long getGROUP_SCORE() {
        return GROUP_SCORE;
    }

    public void setGROUP_SCORE(Long GROUP_SCORE) {
        this.GROUP_SCORE = GROUP_SCORE;
    }

    public Long getMEMBERS_NUM() {
        return MEMBERS_NUM;
    }

    public void setMEMBERS_NUM(Long MEMBERS_NUM) {
        this.MEMBERS_NUM = MEMBERS_NUM;
    }

    public Long getGROUP_OWNER() {
        return GROUP_OWNER;
    }

    public void setGROUP_OWNER(Long GROUP_OWNER) {
        this.GROUP_OWNER = GROUP_OWNER;
    }

    public Long getRANK_REQ() {
        return RANK_REQ;
    }

    public void setRANK_REQ(Long RANK_REQ) {
        this.RANK_REQ = RANK_REQ;
    }

    public String getOWNER_NAME() {
        return OWNER_NAME;
    }

    public void setOWNER_NAME(String OWNER_NAME) {
        this.OWNER_NAME = OWNER_NAME;
    }

    public Long getOWNER_RANK() {
        return OWNER_RANK;
    }

    public void setOWNER_RANK(Long OWNER_RANK) {
        this.OWNER_RANK = OWNER_RANK;
    }

    public String getGROUP_DESC() {
        return GROUP_DESC;
    }

    public void setGROUP_DESC(String GROUP_DESC) {
        this.GROUP_DESC = GROUP_DESC;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
}

