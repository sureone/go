package com.sureone;


public class Desk {
    public static final int DESK_NULL=0;
    public static final int DESK_NO_FULL=1;
    public static final int DESK_FULL=2;
    public static final int DESK_READY=3;
    public static final int DESK_PLAYING=4;

    public int id;
    public User black = null;
    public User white = null;
    public int status;
    public int step_time_out=3;
}
