#include "mpu6050.h"
#include "max30102_iic.h"
#include "math.h"
#include "SysTick.h"
// 陀螺仪校准值
float gyro_bias[3] = {0.0f, 0.0f, 0.0f};
// 加速度计校准值
float accel_bias[3] = {0.0f, 0.0f, 0.0f};
/**
 * @brief  初始化MPU6050传感器
 * @retval 0: 成功, 1: 失败
 */
u8 MPU6050_Init(void)
{
    u8 who_am_i;

    // 初始化I?C总线（如果还未初始化）
    IIC_Init();

    // 读取WHO_AM_I寄存器，确认设备是否存在
    IIC_Read_One_Byte((MPU6050_ADDR << 1) & 0xFE, WHO_AM_I, &who_am_i);
    // 某些MPU6050模块的WHO_AM_I可能是0x68或0x70，都认为是有效的
    if (who_am_i != 0x68 && who_am_i != 0x70) {
        return 1; // 设备不存在
    }

    // 复位设备
    IIC_Write_One_Byte(MPU6050_ADDR << 1, PWR_MGMT_1, 0x80);
    delay_ms(100);

    // 唤醒设备，选择时钟源为X轴陀螺仪
    IIC_Write_One_Byte(MPU6050_ADDR << 1, PWR_MGMT_1, 0x01);

    // 设置采样率
    IIC_Write_One_Byte(MPU6050_ADDR << 1, SMPLRT_DIV, 0x07);

    // 设置配置寄存器
    IIC_Write_One_Byte(MPU6050_ADDR << 1, CONFIG, 0x00);

    // 设置陀螺仪量程为±2000°/s
    IIC_Write_One_Byte(MPU6050_ADDR << 1, GYRO_CONFIG, 0x18);

    // 设置加速度计量程为±16g
    IIC_Write_One_Byte(MPU6050_ADDR << 1, ACCEL_CONFIG, 0x18);

    // 校准陀螺仪和加速度计
    MPU6050_Data mpu6050_data;

    for (int i = 0; i < 200; i++) {
        MPU6050_ReadData(&mpu6050_data);
        accel_bias[0] += mpu6050_data.accel_x / 2048.0;
        accel_bias[1] += mpu6050_data.accel_y / 2048.0;
        // z轴需要减去1g（重力加速度）
        accel_bias[2] += (mpu6050_data.accel_z / 2048.0)-1;
        // 陀螺仪转换系数：±2000°/s量程时，LSB/(°/s) = 16.4
        gyro_bias[0] += mpu6050_data.gyro_x / 16.4;
        gyro_bias[1] += mpu6050_data.gyro_y / 16.4;
        gyro_bias[2] += mpu6050_data.gyro_z / 16.4;
        delay_ms(10);
    }
    gyro_bias[0] /= 200.0f;
    gyro_bias[1] /= 200.0f;
    gyro_bias[2] /= 200.0f;
    accel_bias[0] /= 200.0f;
    accel_bias[1] /= 200.0f;
    accel_bias[2] /= 200.0f;
    return 0;
}

/**
 * @brief  读取MPU6050传感器数据
 * @param  data: 存储传感器数据的结构体指针
 * @retval 0: 成功, 1: 失败
 */
u8 MPU6050_ReadData(MPU6050_Data *data)
{
    u8 buf[2];

    // 读取加速度计数据
    IIC_Read_One_Byte(MPU6050_ADDR << 1, ACCEL_XOUT_H, &buf[0]);
    IIC_Read_One_Byte(MPU6050_ADDR << 1, ACCEL_XOUT_L, &buf[1]);
    data->accel_x = (buf[0] << 8) | buf[1];

    IIC_Read_One_Byte(MPU6050_ADDR << 1, ACCEL_YOUT_H, &buf[0]);
    IIC_Read_One_Byte(MPU6050_ADDR << 1, ACCEL_YOUT_L, &buf[1]);
    data->accel_y = (buf[0] << 8) | buf[1];

    IIC_Read_One_Byte(MPU6050_ADDR << 1, ACCEL_ZOUT_H, &buf[0]);
    IIC_Read_One_Byte(MPU6050_ADDR << 1, ACCEL_ZOUT_L, &buf[1]);
    data->accel_z = (buf[0] << 8) | buf[1];

    // 读取温度数据
    IIC_Read_One_Byte(MPU6050_ADDR << 1, TEMP_OUT_H, &buf[0]);
    IIC_Read_One_Byte(MPU6050_ADDR << 1, TEMP_OUT_L, &buf[1]);
    int16_t temp_raw  = (buf[0] << 8) | buf[1];
    data->temperature = (temp_raw / 340.0) + 36.53;

    // 读取陀螺仪数据
    IIC_Read_One_Byte(MPU6050_ADDR << 1, GYRO_XOUT_H, &buf[0]);
    IIC_Read_One_Byte(MPU6050_ADDR << 1, GYRO_XOUT_L, &buf[1]);
    data->gyro_x = (buf[0] << 8) | buf[1];

    IIC_Read_One_Byte(MPU6050_ADDR << 1, GYRO_YOUT_H, &buf[0]);
    IIC_Read_One_Byte(MPU6050_ADDR << 1, GYRO_YOUT_L, &buf[1]);
    data->gyro_y = (buf[0] << 8) | buf[1];

    IIC_Read_One_Byte(MPU6050_ADDR << 1, GYRO_ZOUT_H, &buf[0]);
    IIC_Read_One_Byte(MPU6050_ADDR << 1, GYRO_ZOUT_L, &buf[1]);
    data->gyro_z = (buf[0] << 8) | buf[1];

    // 打印原始读取数据
    // printf("Raw data: ");
    // printf("0x%02X 0x%02X 0x%02X 0x%02X 0x%02X 0x%02X ",
    //        (data->accel_x >> 8) & 0xFF, data->accel_x & 0xFF,
    //        (data->accel_y >> 8) & 0xFF, data->accel_y & 0xFF,
    //        (data->accel_z >> 8) & 0xFF, data->accel_z & 0xFF);
    // printf("0x%02X 0x%02X ", (temp_raw >> 8) & 0xFF, temp_raw & 0xFF);
    // printf("0x%02X 0x%02X 0x%02X 0x%02X 0x%02X 0x%02X\r\n",
    //        (data->gyro_x >> 8) & 0xFF, data->gyro_x & 0xFF,
    //        (data->gyro_y >> 8) & 0xFF, data->gyro_y & 0xFF,
    //        (data->gyro_z >> 8) & 0xFF, data->gyro_z & 0xFF);

    return 0;
}

/**
 * @brief  处理MPU6050传感器数据，转换为物理单位
 * @param  data: 原始传感器数据
 * @param  accel_g: 转换后的加速度数据（单位：g）
 * @param  gyro_dps: 转换后的陀螺仪数据（单位：°/s）
 */
void MPU6050_ProcessData(MPU6050_Data *data, float *accel_g, float *gyro_dps)
{
    // 加速度计转换系数：±16g量程时，LSB/g = 2048
    accel_g[0] = data->accel_x / 2048.0 - accel_bias[0];
    accel_g[1] = data->accel_y / 2048.0 - accel_bias[1];
    accel_g[2] = data->accel_z / 2048.0 - accel_bias[2];
    // 陀螺仪转换系数：±2000°/s量程时，LSB/(°/s) = 16.4
    gyro_dps[0] = data->gyro_x / 16.4 - gyro_bias[0];
    gyro_dps[1] = data->gyro_y / 16.4 - gyro_bias[1];
    gyro_dps[2] = data->gyro_z / 16.4 - gyro_bias[2];
}

/**
 * @brief  打印MPU6050传感器原始数据
 * @param  data: 传感器数据结构体指针
 */
void MPU6050_PrintData(MPU6050_Data *data)
{
    // 打印加速度计原始数据
    printf("加速度: X=%d, Y=%d, Z=%d\r\n", data->accel_x, data->accel_y, data->accel_z);

    // 打印陀螺仪原始数据
    printf("陀螺仪:  X=%d, Y=%d, Z=%d\r\n", data->gyro_x, data->gyro_y, data->gyro_z);

    // 打印温度数据
    printf("温度:  %.2f C\r\n", data->temperature);
}