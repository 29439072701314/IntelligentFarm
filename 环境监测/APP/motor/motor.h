#ifndef _motor_H
#define _motor_H

#include "stm32f10x.h"

/* 电机时钟端口、引脚定义 */
#define MOTOR_PORT     GPIOA
#define MOTOR_PIN1     (GPIO_Pin_11)
#define MOTOR_PIN2     (GPIO_Pin_12)
#define MOTOR_PIN      (MOTOR_PIN1 | MOTOR_PIN2)
#define MOTOR_PORT_RCC RCC_APB2Periph_GPIOA

/**
 * @brief  初始化电机
 */
void MOTOR_Init(void);

/**
 * @brief  控制电机状态
 * @param  state: 控制状态，0为停止，1为正转，2为反转
 */
void MOTOR_Control(u8 state);

#endif