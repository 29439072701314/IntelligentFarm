#include "stm32f10x.h"
#include "stm32f10x_gpio.h"
#include "stm32f10x_rcc.h"
#include "led.h"
#include "EXTI_key.h"
#include "SysTick.h"
#include "dht11.h"
#include "adc.h"
#include "usart.h"
#include "usart2.h"
#include "mq2.h"
#include "math.h"
#include "esp8266.h"
#include "timer.h"
#include "motor.h"
#include "hx711.h"

// 数据类型定义
typedef unsigned char u8;
typedef unsigned short u16;
typedef unsigned int u32;

u8 temp;                // DHT11数据，温度
u8 humi;                // DHT11数据，湿度
float gasConcentration; // MQ2数据，气体浓度
u32 weight;             // 重量数据(g)
u8 isWeighting = 0;     // 是否正在称重
void dealy_s(u16 s)
{
    for (u16 i = 0; i < s; i++) {
        delay_ms(1000);
    }
}

/** @brief 处理DHT11数据读取 */
void DHT11_Data_Process()
{
    if (DHT11_Read_Data(&temp, &humi) == 0) {
        printf("temp: %d, humi: %d\r\n", temp, humi);
        delay_ms(10);
    }
}

/** @brief 处理MQ-2数据读取 */
void MQ2_Data_Process()
{
    MQ2_GetData_PPM(&gasConcentration);
    printf("gasConcentration: %.2f PPM\r\n", gasConcentration);
}

// 获取所有传感器数据
void getData(u8 times)
{
    // 累积变量
    u32 temp_sum  = 0;
    u32 humi_sum  = 0;
    float gas_sum = 0.0;

    for (u8 i = 0; i < times; i++) {
        // 读取DHT11温湿度数据
        DHT11_Read_Data(&temp, &humi);
        temp_sum += temp;
        humi_sum += humi;

        // 读取MQ2气体浓度数据
        MQ2_GetData_PPM(&gasConcentration);
        gas_sum += gasConcentration;
    }
    // 计算平均值并存储
    temp             = temp_sum / times;
    humi             = humi_sum / times;
    gasConcentration = gas_sum / times;

    // 打印平均值结果
    printf("Average values:\r\n");
    printf("温度: %d\r\n", temp);
    printf("湿度: %d\r\n", humi);
    printf("气体浓度: %.2f PPM\r\n", gasConcentration);
}

/** @brief 主函数 */
int main(void)
{
    SysTick_Init(72);
    USART1_Init(115200);
    Usart2_Init(115200);
    // 初始化定时器2，配置为20ms中断一次
    // 时钟频率72MHz，预分频系数7200-1=7199，自动重载值200-1=199
    // 计算公式：72MHz / (7200 * 200) = 50Hz，即20ms中断一次
    Timer2_Init(199, 7199);
    Time2_NVIC_Config();

    // 初始化定时器3，配置为0.2秒中断一次
    // 时钟频率72MHz，预分频系数7200-1=7199，自动重载值2000-1=1999
    // 计算公式：72MHz / (7200 * 2000) = 5Hz，即0.2秒中断一次
    Timer3_Init(1999, 7199);
    Time3_NVIC_Config();

    EXTI_Key_Init();
    LED_Init();
    MOTOR_Init();
    // MOTOR_Control(1);
    delay_ms(1000);
    if (DHT11_Init() != 0) {
        printf("DHT11_Init failed\r\n");
    }
    GPIO_Configuration_HX711();
    ADCx_Init();
    ESP_Init(); // 初始化ESP8266模块
    MQ2_Init(); // 初始化MQ2传感器

    while (1) {
        // 采集数据
        getData(50);
        SensorData sensorData[] = {
            {"temperature", temp},
            {"humidity", humi},
            {"gasConcentration", (int)gasConcentration},
            {NULL, 0},
            {NULL, 0}};
        if (isWeighting) {
            printf("称重中\r\n");
            weight = Weighing();
            printf("称重结束，重量: %dkg\r\n", weight / 1000);
            isWeighting         = 0;
            sensorData[3].name  = "weight";
            sensorData[3].value = weight / 1000;
        }
        // 上传传感器数据
        ESP_UploadSensorData(sensorData);
    }
}

/**
 * @brief  EXTI0中断服务函数 - 处理PA0引脚的外部中断事件
 * @note   该函数用于KEY_UP按键触发中断处理
 */
void EXTI0_IRQHandler(void)
{
    // 检查EXTI0线路是否发生中断触发，并清除EXTI_Line0中断标志位
    // EXTI_GetITStatus()函数返回SET(非0)表示有中断，RESET(0)表示无中断
    if (EXTI_GetITStatus(EXTI_Line0) != RESET) {
        delay_ms(10);
        // 必须调用此函数清除中断标志，否则会持续触发中断
        EXTI_ClearITPendingBit(EXTI_Line0);
    }
}

/**
 * @brief  TIM2中断服务函数
 */
void TIM2_IRQHandler(void)
{
    if (TIM_GetITStatus(TIM2, TIM_IT_Update) != RESET) {
        // 清除中断标志位
        TIM_ClearITPendingBit(TIM2, TIM_IT_Update);
    }
}

/**
 * @brief  TIM3中断服务函数 - 每1秒检查一次云端消息
 */
void TIM3_IRQHandler(void)
{
    if (TIM_GetITStatus(TIM3, TIM_IT_Update) != RESET) {
        // 清除中断标志位
        LED_Toggle(LED_Green);
        if (ESP_WaitRecive() == REV_OK) // 判断是否接收到完整数据
        {
            if (strstr((const char *)ESP_buf, "motor=1") != NULL) {
                MOTOR_Control(1);
            } else if (strstr((const char *)ESP_buf, "motor=2") != NULL) {
                MOTOR_Control(2);
            } else if (strstr((const char *)ESP_buf, "motor=0") != NULL) {
                MOTOR_Control(0);
            } else if (strstr((const char *)ESP_buf, "weight=true") != NULL) {
                printf("称重指令\r\n");
                isWeighting = 1;
            }
            ESP_Clear(); // 清空接收缓存
        }
        TIM_ClearITPendingBit(TIM3, TIM_IT_Update);
    }
}
