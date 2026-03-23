#include "led.h"
#include "SysTick.h"

void LED_Init()
{
    GPIO_InitTypeDef GPIO_InitStructure; //定义结构体变量

    RCC_APB2PeriphClockCmd(LED_PORT_RCC, ENABLE);

    GPIO_InitStructure.GPIO_Pin   = LED_PIN;          //选择你要设置的IO口
    GPIO_InitStructure.GPIO_Mode  = GPIO_Mode_Out_PP; //设置推挽输出模式
    GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz; //设置传输速率
    GPIO_Init(LED_PORT, &GPIO_InitStructure);         /* 初始化GPIO */

    GPIO_SetBits(LED_PORT, LED_PIN); //将LED端口拉高，熄灭所有LED
}

void LED_Control(u16 led, u8 state)
{
    if (state == 0) {
        GPIO_SetBits(LED_PORT, led); // 熄灭LED
    } else {
        GPIO_ResetBits(LED_PORT, led); // 点亮LED
    }
}

void LED_Toggle(u16 led)
{
    GPIO_WriteBit(LED_PORT, led, (BitAction)!GPIO_ReadOutputDataBit(LED_PORT, led));
}

void LED_Blink(u16 led, u8 times, u16 interval)
{
    for (u8 i = 0; i < times; i++) {
        LED_Toggle(led);
        delay_ms(interval);
    }
}