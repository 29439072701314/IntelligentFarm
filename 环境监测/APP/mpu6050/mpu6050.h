#ifndef __MPU6050_H
#define __MPU6050_H
#include "system.h"

// MPU6050设备地址
#define MPU6050_ADDR 0x68 // AD0接地时的地址

// 寄存器地址
#define SMPLRT_DIV   0x19
#define CONFIG       0x1A
#define GYRO_CONFIG  0x1B
#define ACCEL_CONFIG 0x1C
#define ACCEL_XOUT_H 0x3B
#define ACCEL_XOUT_L 0x3C
#define ACCEL_YOUT_H 0x3D
#define ACCEL_YOUT_L 0x3E
#define ACCEL_ZOUT_H 0x3F
#define ACCEL_ZOUT_L 0x40
#define TEMP_OUT_H   0x41
#define TEMP_OUT_L   0x42
#define GYRO_XOUT_H  0x43
#define GYRO_XOUT_L  0x44
#define GYRO_YOUT_H  0x45
#define GYRO_YOUT_L  0x46
#define GYRO_ZOUT_H  0x47
#define GYRO_ZOUT_L  0x48
#define PWR_MGMT_1   0x6B
#define WHO_AM_I     0x75

// 数据结构
typedef struct {
    int16_t accel_x;
    int16_t accel_y;
    int16_t accel_z;
    int16_t gyro_x;
    int16_t gyro_y;
    int16_t gyro_z;
    float temperature;
} MPU6050_Data;

// 函数声明
u8 MPU6050_Init(void);
u8 MPU6050_ReadData(MPU6050_Data *data);
void MPU6050_ProcessData(MPU6050_Data *data, float *accel_g, float *gyro_dps);
void MPU6050_PrintData(MPU6050_Data *data);
#endif