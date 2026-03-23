#include "timer.h"

void Timer2_Init(uint16_t arr, uint16_t psc)
{
    RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM2, ENABLE); //时钟使能

    TIM_TimeBaseInitTypeDef TIM_TimeBaseStructure;
    TIM_TimeBaseStructure.TIM_Period        = arr;                //设置自动重载技术周期值
    TIM_TimeBaseStructure.TIM_Prescaler     = psc;                //设置分频系数
    TIM_TimeBaseStructure.TIM_ClockDivision = TIM_CKD_DIV1;       //设置时钟分频因子
    TIM_TimeBaseStructure.TIM_CounterMode   = TIM_CounterMode_Up; //设置向上计数方式
    TIM_TimeBaseInit(TIM2, &TIM_TimeBaseStructure);               //初始化

    TIM_ClearFlag(TIM2, TIM_FLAG_Update);      //清除中断，避免系统启动中断后会立即中断
    TIM_ITConfig(TIM2, TIM_IT_Update, ENABLE); //使能定时器TIM2的更新中断
    TIM_Cmd(TIM2, ENABLE);                     //使能TIM2定时器
}

void Timer3_Init(uint16_t arr, uint16_t psc)
{
    RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM3, ENABLE); //时钟使能

    TIM_TimeBaseInitTypeDef TIM_TimeBaseStructure;
    TIM_TimeBaseStructure.TIM_Period        = arr;                //设置自动重载技术周期值
    TIM_TimeBaseStructure.TIM_Prescaler     = psc;                //设置分频系数
    TIM_TimeBaseStructure.TIM_ClockDivision = TIM_CKD_DIV1;       //设置时钟分频因子
    TIM_TimeBaseStructure.TIM_CounterMode   = TIM_CounterMode_Up; //设置向上计数方式
    TIM_TimeBaseInit(TIM3, &TIM_TimeBaseStructure);               //初始化

    TIM_ClearFlag(TIM3, TIM_FLAG_Update);      //清除中断，避免系统启动中断后会立即中断
    TIM_ITConfig(TIM3, TIM_IT_Update, ENABLE); //使能定时器TIM2的更新中断
    TIM_Cmd(TIM3, ENABLE);                    //使能TIM3定时器
}

void Time2_NVIC_Config()
{
    NVIC_InitTypeDef NVIC_InitStructuer;

    NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);
    NVIC_InitStructuer.NVIC_IRQChannel                   = TIM2_IRQn;
    NVIC_InitStructuer.NVIC_IRQChannelPreemptionPriority = 0;
    NVIC_InitStructuer.NVIC_IRQChannelSubPriority        = 1;
    NVIC_InitStructuer.NVIC_IRQChannelCmd                = ENABLE;
    NVIC_Init(&NVIC_InitStructuer);
}
void Time3_NVIC_Config()
{
    NVIC_InitTypeDef NVIC_InitStructuer;

    NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);
    NVIC_InitStructuer.NVIC_IRQChannel                   = TIM3_IRQn;
    NVIC_InitStructuer.NVIC_IRQChannelPreemptionPriority = 0;
    NVIC_InitStructuer.NVIC_IRQChannelSubPriority        = 1;
    NVIC_InitStructuer.NVIC_IRQChannelCmd                = ENABLE;
    NVIC_Init(&NVIC_InitStructuer);
}
