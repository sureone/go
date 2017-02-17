#ifndef _TIMER_CHECK_H
#define _TIMER_CHECK_H
#include "user.h"
struct timer_check
{
    time_t start;
    long count;
};
static inline int isTimeout(struct timer_check* tc,time_t cur_ts)
{
    int ret = 0;
    if(tc==NULL) return 1;
    if(tc->start>0 && cur_ts-tc->start > tc->count)
        ret = 1;
    return ret;
}
static inline int isInTimeout(struct timer_check* tc)
{
    int ret = 0;
    if(tc==NULL) return 1;
    if(tc->start>0 &&  tc->count>0)
        ret = 1;
    return ret;
}
static inline void setTimeout(struct timer_check* tc,
                              time_t cur_ts,int count)
{
    if(tc!=NULL)
    {
        tc->start = cur_ts;
        tc->count = count;
    }
}
static inline void resetTimeout(struct timer_check* tc)
{
    if(tc!=NULL)
    {
        tc->start = 0;
        tc->count = 0;
    }
}
#endif
