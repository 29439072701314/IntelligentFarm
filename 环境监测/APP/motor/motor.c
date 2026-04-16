#include "motor.h"

/**
 * @brief  初始化电机
 */
void MOTOR_Init(void)
{
    GPIO_InitTypeDef GPIO_InitStructure;

    // 使能GPIOA时钟
    RCC_APB2PeriphClockCmd(MOTOR_PORT_RCC, ENABLE);

    // 配置GPIOA11和GPIOA12为推挽输出
    GPIO_InitStructure.GPIO_Pin   = MOTOR_PIN;
    GPIO_InitStructure.GPIO_Mode  = GPIO_Mode_Out_PP;
    GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
    GPIO_Init(MOTOR_PORT, &GPIO_InitStructure);

    // 初始化为低电平（关闭状态）
    GPIO_ResetBits(MOTOR_PORT, MOTOR_PIN);
}

/**
 * @brief  控制电机状态
 * @param  state: 控制状态，0为停止，1为正转，2为反转
 */
void MOTOR_Control(u8 state)
{
    switch (state) {
        case 0: // 停止
            GPIO_ResetBits(MOTOR_PORT, MOTOR_PIN);
            break;
        case 1: // 正转（A11低，A12高）
            GPIO_ResetBits(MOTOR_PORT, MOTOR_PIN1);
            GPIO_SetBits(MOTOR_PORT, MOTOR_PIN2);
            break;
        case 2: // 反转（A11高，A12低）
            GPIO_SetBits(MOTOR_PORT, MOTOR_PIN1);
            GPIO_ResetBits(MOTOR_PORT, MOTOR_PIN2);
            break;
        default:
            break;
    }
}