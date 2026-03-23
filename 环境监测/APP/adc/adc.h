#ifndef __ADC_H_
#define __ADC_H_

#include "stm32f10x.h"
#include "stm32f10x_adc.h"

// 数据类型定义
typedef unsigned char u8;
typedef unsigned short u16;

void ADCx_Init();

u16 Get_ADC_Value(u8 ch, u8 times);

#endif