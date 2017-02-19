package com.sureone.igs;
/*
7 [##]  white name [ rk ]      black name [ rk ] (Move size H Komi BY FR) (###)
7 [484]   Samurai55 [ 2d*] vs.        stgl [ 1d*] (159   19  0  0.5 15  I) (  0)
7 [537]   hiroshigo [ 3d*] vs.  niwanotoki [ 2d*] (310   19  0 -5.5 10  I) (  1)
7 [128]     DaVinci [10k*] vs.      KT1421 [10k*] (310   19  0  6.5 10  I) (  0)
*/
public class IgsGameItem {
    int id;
    String wName;
    String bName;
    String wrk;
    String brk;
    int move;
    int size;
    int H;
    String Komi;
    int BY;
    String FR;
    int obs; // Number of observer
    public IgsGameItem() {
    }
};
