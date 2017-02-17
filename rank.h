#ifndef _RANK_H
#define _RANK_H
typedef struct{
	char* label;
	int rank;
	int score;
}rank_map_t;
void set_rank(USER_T* u);
void cal_rank(USER_T* win,USER_T* los);
rank_map_t* getRankMap(int rank);
#endif


