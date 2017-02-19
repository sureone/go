package com.sureone;
public class DianMuResult {
        public int bmu;
        public int wmu;
        public int winner;
        public byte[] mus=new byte[19*19];
        public void clean() {
            if(bmu==0 && wmu==0) return;
            bmu=0;
            wmu=0;
            for(int i=0; i<19*19; i++) {
                mus[i]=0;
            }
        }
        public int getSide(int x,int y) {
            int idx=y*19+x;
            return mus[idx];
        }
    }