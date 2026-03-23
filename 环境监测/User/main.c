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
#include "max30102.h"
#include "mpu6050.h"
#include "timer.h"

// 数据类型定义
typedef unsigned char u8;
typedef unsigned short u16;
typedef unsigned int u32;

u8 temp;                // DHT11数据，温度
u8 humi;                // DHT11数据，湿度
float gasConcentration; // MQ2数据，气体浓度

// 心率和血氧数据
int32_t n_heart_rate = 0; // 心率值
int8_t ch_hr_valid   = 0; // 心率数据是否有效
int32_t n_spo2       = 0; // 血氧值
int8_t ch_spo2_valid = 0; // 血氧数据是否有效

// MPU6050数据
MPU6050_Data mpu6050_data; // 原始数据
float accel_g[3];          // 加速度数据（单位：g）
float gyro_dps[3];         // 陀螺仪数据（单位：°/s）

// 模式切换变量
u8 mode = 1; // 0: 采集心率血氧模式，1: 采集MPU6050模式

// 跌倒检测状态机
#define STATE_NORMAL  0 // 正常状态
#define STATE_FALLING 1 // 失重状态
#define STATE_IMPACT  2 // 撞击状态
#define STATE_STILL   3 // 静止状态

u8 fall_state         = STATE_NORMAL; // 当前跌倒检测状态
u8 fall_down_flag     = 0;            // 是否跌倒标志位
float square          = 0.0f;         // 加速度模的平方
u32 fall_detect_count = 0;            // 跌倒检测计数器

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
    u32 temp_sum           = 0;
    u32 humi_sum           = 0;
    float gas_sum          = 0.0;
    int32_t heart_rate_sum = 0;
    int32_t spo2_sum       = 0;
    u8 max30102_count      = 0;

    for (u8 i = 0; i < times; i++) {
        // 读取DHT11温湿度数据
        DHT11_Read_Data(&temp, &humi);
        temp_sum += temp;
        humi_sum += humi;

        // 读取MQ2气体浓度数据
        MQ2_GetData_PPM(&gasConcentration);
        gas_sum += gasConcentration;

        // 根据模式决定是否采集MAX30102心率和血氧数据
        if (mode == 0) {
            // 模式0：采集心率血氧数据
            MAX30102_App_Process();

            // 检查心率数据是否有效
            if (MAX30102_Get_HR_Valid() == 1 && MAX30102_Get_HeartRate() < 120 && MAX30102_Get_SpO2_Valid() == 1) {
                heart_rate_sum += MAX30102_Get_HeartRate();
                spo2_sum += MAX30102_Get_SpO2();
                max30102_count++;
            }
        } else {
            // 模式1：不采集心率血氧数据
            ch_hr_valid   = 0;
            ch_spo2_valid = 0;
            n_heart_rate  = 0;
            n_spo2        = 0;
        }
    }
    // 计算平均值并存储
    temp             = temp_sum / times;
    humi             = humi_sum / times;
    gasConcentration = gas_sum / times;

    // 只有当有有效数据时才计算平均值
    if (max30102_count > 0) {
        n_heart_rate  = heart_rate_sum / max30102_count;
        n_spo2        = spo2_sum / max30102_count;
        ch_hr_valid   = 1;
        ch_spo2_valid = 1;
    } else {
        n_heart_rate  = 0;
        ch_hr_valid   = 0;
        n_spo2        = 0;
        ch_spo2_valid = 0;
    }

    // 打印平均值结果
    printf("Average values:\r\n");
    printf("温度: %d\r\n", temp);
    printf("湿度: %d\r\n", humi);
    printf("气体浓度: %.2f PPM\r\n", gasConcentration);
    printf("心率: %ld (valid: %d)\r\n", n_heart_rate, ch_hr_valid);
    printf("血氧: %ld (valid: %d)\r\n", n_spo2, ch_spo2_valid);
}

/** @brief 主函数 */
int main(void)
{
    LED_Init();
    LED_Control(LED_Green, mode);
    SysTick_Init(72);
    USART1_Init(115200);
    Usart2_Init(115200);
    delay_ms(1000);
    if (DHT11_Init() != 0) {
        printf("DHT11_Init failed\r\n");
    }
    ADCx_Init();
    ESP_Init();          // 初始化ESP8266模块
    MQ2_Init();          // 初始化MQ2传感器
    MAX30102_App_Init(); // 初始化MAX30102传感器
    if (MPU6050_Init() != 0) {
        printf("MPU6050_Init failed\r\n");
    }
    EXTI_Key_Init();

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

    while (1) {
        // 根据模式决定采集采集次数
        getData(mode == 0 ? 5 : 40);
        // 上传传感器数据（包含心率和血氧）
        SensorData sensorData[] = {
            {"temperature", temp},
            {"humidity", humi},
            {"gasConcentration", (int)gasConcentration},
            {"isFallDown", fall_down_flag},
            {(ch_hr_valid == 1) ? "heartRate" : NULL, (ch_hr_valid == 1) ? n_heart_rate : 0},
            {(ch_spo2_valid == 1) ? "spo2" : NULL, (ch_spo2_valid == 1) ? n_spo2 : 0},
            {NULL, 0}};
        TIM_Cmd(TIM2, DISABLE);
        ESP_UploadSensorData(sensorData);
        if (mode == 1) TIM_Cmd(TIM2, ENABLE);
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
        // 切换模式
        mode = !mode;
        // 切换LED状态(模式0:LED熄灭,模式1:LED亮)
        LED_Control(LED_Green, mode);
        // 打印当前模式
        if (mode == 0) {
            printf("切换到采集心率血氧模式\r\n");
            TIM_Cmd(TIM2, DISABLE);
            MAX30102_App_Init();
        } else {
            printf("切换到采集MPU6050模式\r\n");
            MPU6050_Init();
        }
        delay_ms(10);
        // 必须调用此函数清除中断标志，否则会持续触发中断
        EXTI_ClearITPendingBit(EXTI_Line0);
    }
}

/**
 * @brief  TIM2中断服务函数 - 每20毫秒采集一次MPU6050数据
 */
void TIM2_IRQHandler(void)
{
    if (TIM_GetITStatus(TIM2, TIM_IT_Update) != RESET) {
        // 根据模式决定是否采集MPU6050数据
        if (mode == 1) {
            MPU6050_ReadData(&mpu6050_data);
            MPU6050_ProcessData(&mpu6050_data, accel_g, gyro_dps);
            // 计算加速度模
            // 加速度模的平方等于加速度的x、y、z分量的平方和
            square = accel_g[0] * accel_g[0] + accel_g[1] * accel_g[1] + accel_g[2] * accel_g[2];
            printf("合加速度模: %.2f\r\n", sqrt(square));
            // 跌倒检测状态机
            switch (fall_state) {
                case STATE_NORMAL:
                    // 检测失重状态：合加速度 < 0.7g
                    if (square < 0.49f) { // 0.7的平方是0.49
                        fall_state        = STATE_FALLING;
                        fall_detect_count = 0;
                        printf("检测到失重状态\r\n");
                    }
                    break;
                case STATE_FALLING:
                    // 检测撞击状态：合加速度 > 2g
                    if (square > 4.0f) { // 2的平方是4.0
                        fall_state        = STATE_IMPACT;
                        fall_detect_count = 0;
                        printf("检测到撞击状态\r\n");
                    } else if (fall_detect_count > 25) { // 超过0.5秒仍未检测到撞击，回到正常状态
                        fall_state = STATE_NORMAL;
                        printf("回到正常状态\r\n");
                    }
                    break;
                case STATE_IMPACT:
                    // 检测静止状态：合加速度 ≈ 1g（±0.2g）
                    if (square >= 0.64f && square <= 1.44f) { // 0.8的平方是0.64，1.2的平方是1.44
                        fall_state        = STATE_STILL;
                        fall_detect_count = 0;
                        printf("检测到静止状态\r\n");
                    } else if (fall_detect_count > 25) { // 超过0.5秒仍未检测到静止，回到正常状态
                        fall_state = STATE_NORMAL;
                        printf("回到正常状态\r\n");
                    }
                    break;
                case STATE_STILL:
                    // 静止状态持续一段时间后，判断为跌倒
                    if (fall_detect_count > 50) { // 持续1秒以上
                        printf("===== 检测到跌倒！ =====\r\n");
                        // 跌倒警报的处理
                        fall_down_flag = 1;
                        // 点亮红色LED
                        LED_Control(LED_Red, fall_down_flag);
                        // 重置跌倒检测状态
                        fall_state = STATE_NORMAL;
                    } else if (square < 0.64f || square > 1.44f) { // 不再静止，回到正常状态
                        fall_state = STATE_NORMAL;
                        printf("回到正常状态\r\n");
                    }
                    break;
            }

            // 增加跌倒检测计数器
            fall_detect_count++;
        }
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
        // 检查云端消息
        if (ESP_CheckReceiveInfo("isFallDown=0")) {
            printf("云端已处理跌倒事件\r\n");
            fall_down_flag = 0;
            // 熄灭红色LED
            LED_Control(LED_Red, fall_down_flag);
        }
        // 清除中断标志位
        TIM_ClearITPendingBit(TIM3, TIM_IT_Update);
    }
}
