#ifndef __MQ2_H_
#define __MQ2_H_

#include "stm32f10x.h"
#include "adc.h"

// 数据类型定义
typedef unsigned char u8;
typedef unsigned short u16;

// 函数声明
void MQ2_Init(void);
u16 MQ2_Get_ADC_Value(void);
float MQ2_Get_Voltage(void);
void MQ2_GetData_PPM(float *data);

#endif