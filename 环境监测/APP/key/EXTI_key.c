#include "EXTI_key.h"
#include "misc.h"


void EXTI_Key_Init(void){
	GPIO_InitTypeDef GPIO_TypeStructure;
	EXTI_InitTypeDef EXTI_initStructure;
	NVIC_InitTypeDef NVIC_initStructure;
	
	//按键时钟
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA,ENABLE);//KEY_UP
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOE,ENABLE);//K1,K2,K3
	
	
	//1.GPIO初始化			KEY_UP
	GPIO_TypeStructure.GPIO_Pin = GPIO_Pin_0;
	GPIO_TypeStructure.GPIO_Mode = GPIO_Mode_IPD;	//下拉输入
	GPIO_Init(GPIOA,&GPIO_TypeStructure);
	GPIO_TypeStructure.GPIO_Pin = GPIO_Pin_2|GPIO_Pin_3|GPIO_Pin_4;	//K1,K2,K3
	GPIO_TypeStructure.GPIO_Mode = GPIO_Mode_IPU;	//上拉输入
	GPIO_Init(GPIOE,&GPIO_TypeStructure);
	
	
	//2.复用端口时钟使能
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO,ENABLE);
	
	
	//3.I/O口与中断线映射
	GPIO_EXTILineConfig(GPIO_PortSourceGPIOA,GPIO_PinSource0);
	GPIO_EXTILineConfig(GPIO_PortSourceGPIOE,GPIO_PinSource2);
	GPIO_EXTILineConfig(GPIO_PortSourceGPIOE,GPIO_PinSource3);
	GPIO_EXTILineConfig(GPIO_PortSourceGPIOE,GPIO_PinSource4);
	
	
	//4.EXTI初始化
	EXTI_initStructure.EXTI_Line = EXTI_Line0;						//KEY_UP
	EXTI_initStructure.EXTI_Mode = EXTI_Mode_Interrupt;
	EXTI_initStructure.EXTI_Trigger = EXTI_Trigger_Rising;//上升沿有效
	EXTI_initStructure.EXTI_LineCmd = ENABLE;
	EXTI_Init(&EXTI_initStructure);
	
	EXTI_initStructure.EXTI_Line = EXTI_Line2|EXTI_Line3|EXTI_Line4;//K1,K2,K3
	EXTI_initStructure.EXTI_Trigger = EXTI_Trigger_Falling;					//下降沿有效
	EXTI_Init(&EXTI_initStructure);
	
	
	//5.NVIC初始化
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);//优先级分组
	
	NVIC_initStructure.NVIC_IRQChannel = EXTI0_IRQn;//中断通道
	NVIC_initStructure.NVIC_IRQChannelSubPriority = 2;
	NVIC_initStructure.NVIC_IRQChannelPreemptionPriority=3;
	NVIC_initStructure.NVIC_IRQChannelCmd = ENABLE;
	NVIC_Init(&NVIC_initStructure);
	
	NVIC_initStructure.NVIC_IRQChannel = EXTI2_IRQn;//中断通道
	NVIC_Init(&NVIC_initStructure);
	
	NVIC_initStructure.NVIC_IRQChannel = EXTI3_IRQn;//中断通道
	NVIC_Init(&NVIC_initStructure);
	
	NVIC_initStructure.NVIC_IRQChannel = EXTI4_IRQn;//中断通道
	NVIC_Init(&NVIC_initStructure);
	
}

