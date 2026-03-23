#include "adc.h"
#include "SysTick.h"
//#include "SysTick.h"

/**
 * @brief ADC初始化函数
 */
void ADCx_Init()
{
    GPIO_InitTypeDef GPIO_InitStructure; //定义GPIO初始化结构体
    ADC_InitTypeDef ADC_InitStructure;

    RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB | RCC_APB2Periph_ADC1, ENABLE);
    RCC_ADCCLKConfig(RCC_PCLK2_Div6);                 //设置ADC时钟分频为6 72M/6=12MHz ADC最大不超过14MHz
    GPIO_InitStructure.GPIO_Pin   = GPIO_Pin_1;       //选择需要使用的IO口(PB1)
    GPIO_InitStructure.GPIO_Mode  = GPIO_Mode_AIN;    //设置为模拟输入模式
    GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz; //设置传输速度
    GPIO_Init(GPIOB, &GPIO_InitStructure);            /* 初始化GPIO */

    ADC_DeInit(ADC1);                                                     //将外设ADC1寄存器重置为缺省值
    ADC_InitStructure.ADC_Mode               = ADC_Mode_Independent;      //独立模式
    ADC_InitStructure.ADC_ScanConvMode       = DISABLE;                   //关闭扫描模式
    ADC_InitStructure.ADC_ContinuousConvMode = DISABLE;                   //关闭连续转换
    ADC_InitStructure.ADC_ExternalTrigConv   = ADC_ExternalTrigConv_None; //禁止外部触发，使用软件触发
    ADC_InitStructure.ADC_DataAlign          = ADC_DataAlign_Right;       //右对齐
    ADC_InitStructure.ADC_NbrOfChannel       = 1;                         // 1个转换在规则序列中 也就是只转换规则序列中的1个通道
    ADC_Init(ADC1, &ADC_InitStructure);                                   // ADC初始化
    ADC_Cmd(ADC1, ENABLE);                                                // 使能AD转换

    ADC_ResetCalibration(ADC1); //复位ADC校准寄存器
    while (ADC_GetResetCalibrationStatus(ADC1))
        ; //获取ADC复位校准寄存器的状态

    ADC_StartCalibration(ADC1); //开始指定ADC的校准状态
    while (ADC_GetCalibrationStatus(ADC1))
        ; //获取指定ADC的校准程序状态

    // ADC_SoftwareStartConvCmd(ADC1, ENABLE);//使能ADC软件开始转换命令
}

/**
 * @brief 获取ADC转换值
 * @param ch 通道号
 * @param times 转换次数
 * @return u16 转换值
 */
u16 Get_ADC_Value(u8 ch, u8 times)
{
    u32 temp_val = 0;
    u8 t;
    //设置ADC通道和一个转换周期时间
    // ADC1的ADC通道239.5个周期，保证精度
    ADC_RegularChannelConfig(ADC1, ch, 1, ADC_SampleTime_239Cycles5);
    for (t = 0; t < times; t++) {
        ADC_SoftwareStartConvCmd(ADC1, ENABLE); //使能ADC1软件开始转换命令
        while (!ADC_GetFlagStatus(ADC1, ADC_FLAG_EOC))
            ; //等待转换结束
        temp_val += ADC_GetConversionValue(ADC1);
        delay_ms(5);
    }
    return temp_val / times;
}