#ifndef __HX711_H
#define __HX711_H

#include "stm32f10x.h"

#define HX711_GPIO GPIOB
#define HX711_SCK  GPIO_Pin_4
#define HX711_DOUT GPIO_Pin_3

void GPIO_Configuration_HX711(void);

u32 ReadCount(void);
u32 Weighing(void);

#endif