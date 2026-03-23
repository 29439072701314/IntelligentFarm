#include "mq2.h"
#include "math.h"

/**
 * @brief 初始化MQ2传感器
 * @note  使用之前确保ADC已经初始化
 */
void MQ2_Init(void)
{
    // MQ2传感器不需要特殊的初始化，只需要ADC已经初始化即可
    // 在实际使用中可以添加传感器预热延迟
}

/**
 * @brief 获取MQ2传感器的ADC值
 * @return ADC原始值 (0-4095)
 */
u16 MQ2_Get_ADC_Value(void)
{
    // 使用ADC通道9 (PB1) 读取20次平均值
    return Get_ADC_Value(ADC_Channel_9, 20);
}

/**
 * @brief 获取MQ2传感器的电压值
 * @return 电压值 (0-3.3V)
 */
float MQ2_Get_Voltage(void)
{
    u16 adc_value = MQ2_Get_ADC_Value();
    return (float)adc_value * 3.3 / 4096.0;
}

void MQ2_GetData_PPM(float *data)
{
    float Vol = MQ2_Get_Voltage();
    float RS  = (5 - Vol) / (Vol * 0.5);
    float R0  = 6.64;
    float ppm = pow(21.72 * R0 / RS, 2.1101f);
    *data     = ppm;
}