#include "log.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <errno.h>
#include <assert.h>
#include "user.h"
#include "rank.h"
#ifdef DMALLOC
#include "dmalloc.h"
#endif
rank_map_t rank_map[]={
	{"9d",1,100},
	{"8d",2,90},
	{"7d",3,80},
	{"6d",4,70},
	{"5d",5,60},
	{"4d",6,50},
	{"3d",7,40},
	{"2d",8,30},
	{"1d",9,20},
	{"1k",10,12},
	{"2k",11,12},
	{"3k",12,12},
	{"4k",13,12},
	{"5k",14,12},
	{"6k",15,12},
	{"7k",16,12},
	{"8k",17,12},
	{"9k",18,12},
	{"10k",19,12},
	{"11k",20,12},
	{"12k",21,12},
	{"13k",22,12},
	{"14k",23,12},
	{"15k",24,12},
	{"16k",25,12},
	{"17k",26,12},
	NULL,
};

rank_map_t* getRankMap(int rank){
	if(rank>0 && rank<=26){
		return &(rank_map[rank-1]);
	}
	return NULL;
}

void set_rank(USER_T* u){
	rank_map_t* rmap = getRankMap(u->rank);
	if(rmap!=NULL){
		if(u->score>=rmap->score && u->rank>1){
			u->rank--;
			u->score=0;
		} else if(u->score<0) u->score=-1;
		if(u->score<0 && u->rank<26){ 
			u->rank++;
			rmap = getRankMap(u->rank);
			u->score=rmap->score-1;
		}
	}	
}

void cal_rank(USER_T* win,USER_T* los){
	int v=0;
	if(win==NULL || los==NULL) return;
		if(win->rank>los->rank){
			v=(win->rank-los->rank);
			win->score+=v*2;
			los->score-=2;
		}
		if(win->rank<los->rank){
			if(los->rank-win->rank<=5){
				win->score++;
				los->score--;
			}
		}
		if(win->rank==los->rank){
			win->score++;
			los->score--;
		}
	set_rank(win);
	set_rank(los);
}
