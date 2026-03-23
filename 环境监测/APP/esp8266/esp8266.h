#ifndef __ESP8266_H__
#define __ESP8266_H__

#include "stm32f10x.h"
#include "systick.h"
#include "usart2.h"
#include <string.h>
#include <stdio.h>

#define REV_OK      0 // 接收完成
#define REV_HALFWAY 1 // 接收未完成
#define REV_NONE    2 // 未收到任何数据

// 传感器数据结构体
typedef struct {
    char *name;
    float value;
} SensorData;

extern unsigned char ESP_buf[2048];
extern unsigned short ESP_cnt, ESP_cntPre;

void ESP_Init(void);

void ESP_Clear(void);

u8 ESP_WaitRecive(void);

void ESP_PrintReceive(void);
_Bool ESP_CheckReceiveInfo(char *info);
_Bool ESP_SendCmdAndRecEcho(char *cmd, char *echo, u16 timeout);

void ESP_UploadSensorData(SensorData *data);
#endif
