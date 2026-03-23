#ifndef _led_H
#define _led_H

#include "stm32f10x.h"

/*  LED时钟端口、引脚定义 */
#define LED_PORT     GPIOC
#define LED_Green    (GPIO_Pin_13)
#define LED_Red      (GPIO_Pin_14)
#define LED_PIN      (LED_Green | LED_Red)
#define LED_PORT_RCC RCC_APB2Periph_GPIOC

/**
 * @brief  初始化LED
 */
void LED_Init(void);
/**
 * @brief  控制LED状态
 * @param  state: 控制状态，0为熄灭，1为点亮
 * @param  led: 要控制的LED引脚，LED_Green或LED_Red
 */
void LED_Control(u16 led, u8 state);

/**
 * @brief  切换LED状态
 */
void LED_Toggle(u16 led);

/**
 * @brief  闪烁LED
 * @param  times: 闪烁次数
 * @param  interval: 间隔时间（ms）
 */
void LED_Blink(u16 led, u8 times, u16 interval);

#endif
