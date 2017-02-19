package com.sureone.igs;
public class IgsPlayer {
    public String name=null;
    public String rank=null;
    public String idle=null;
    public String flags=null;
    public String country=null;
    public String lang=null;
    public String info=null;
    public int obs_id=-1;
    public int game_id=-1;
    public int wins=-1;
    public int loses=-1;
    public char color;
    public static int getRankNo(String rank) {
		if(rank==null) return 0;	
        char[] cs = rank.toCharArray();
        int i=0;
        char l='d';
        int j=0;
        char c;
        if(rank.equals("NR")) return 60;
        while(j<cs.length) {
            c=cs[j];
            if(c>='0' && c<='9') {
                if(j==0)
                    i=c-'0';
                else if(j==1)
                    i=i*10+(c-'0');
            } else if(c=='d' || c=='k') {
                l=c;
                break;
            }
            j++;
        }
        if(l=='d') return 10-i;
        if(l=='k') {
            return i+9;
        }
        return 0;
    }

    boolean isAcceptPlay() {
        if(flags.indexOf("X")!=-1) return false;
        return true;
    }
    boolean isLookingOn() {
        if(flags.indexOf("!")!=-1) return true;
        return false;
    }
};
