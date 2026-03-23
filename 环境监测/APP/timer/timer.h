#ifndef __TIMER_H
#define __TIMER_H
#include "stm32f10x_tim.h"
#include "stm32f10x.h"
void Timer2_Init(uint16_t arr,uint16_t psc);
void Time2_NVIC_Config(void);
void Timer3_Init(uint16_t arr,uint16_t psc);
void Time3_NVIC_Config(void);

#endif

