#ifndef __MAX30102_H
#define __MAX30102_H
#include "system.h"

#define MAX30102_INT PBin(9)

#define I2C_WR	0		/* 写控制bit */
#define I2C_RD	1		/* 读控制bit */

#define max30102_WR_address 0xAE

#define I2C_WRITE_ADDR 0xAE
#define I2C_READ_ADDR 0xAF

//register addresses
#define REG_INTR_STATUS_1 0x00
#define REG_INTR_STATUS_2 0x01
#define REG_INTR_ENABLE_1 0x02
#define REG_INTR_ENABLE_2 0x03
#define REG_FIFO_WR_PTR 0x04
#define REG_OVF_COUNTER 0x05
#define REG_FIFO_RD_PTR 0x06
#define REG_FIFO_DATA 0x07
#define REG_FIFO_CONFIG 0x08
#define REG_MODE_CONFIG 0x09
#define REG_SPO2_CONFIG 0x0A
#define REG_LED1_PA 0x0C
#define REG_LED2_PA 0x0D
#define REG_PILOT_PA 0x10
#define REG_MULTI_LED_CTRL1 0x11
#define REG_MULTI_LED_CTRL2 0x12
#define REG_TEMP_INTR 0x1F
#define REG_TEMP_FRAC 0x20
#define REG_TEMP_CONFIG 0x21
#define REG_PROX_INT_THRESH 0x30
#define REG_REV_ID 0xFE
#define REG_PART_ID 0xFF

#define MAX_BRIGHTNESS 255

//MAX30102数据结构体
typedef struct
{
	uint32_t aun_ir_buffer[500];  //IR LED sensor data
	int32_t n_ir_buffer_length;   //data length
	uint32_t aun_red_buffer[500]; //Red LED sensor data
	int32_t n_sp02;               //SPO2 value
	int8_t ch_spo2_valid;         //indicator to show if the SP02 calculation is valid
	int32_t n_heart_rate;         //heart rate value
	int8_t ch_hr_valid;           //indicator to show if the heart rate calculation is valid
	uint8_t temp_max30102[6];
}MAX30102_Data_t;

//MAX30102基础驱动函数
void max30102_init(void);  
void max30102_reset(void);
u8 max30102_Bus_Write(u8 Register_Address, u8 Word_Data);
u8 max30102_Bus_Read(u8 Register_Address);
void max30102_FIFO_ReadWords(u8 Register_Address,u16  Word_Data[][2],u8 count);
void max30102_FIFO_ReadBytes(u8 Register_Address,u8* Data);

void maxim_max30102_write_reg(uint8_t uch_addr, uint8_t uch_data);
void maxim_max30102_read_reg(uint8_t uch_addr, uint8_t *puch_data);
void maxim_max30102_read_fifo(uint32_t *pun_red_led, uint32_t *pun_ir_led);

//MAX30102应用层函数
void MAX30102_App_Init(void);
void MAX30102_App_Process(void);

//获取MAX30102数据
int32_t MAX30102_Get_HeartRate(void);
int32_t MAX30102_Get_SpO2(void);
int8_t MAX30102_Get_HR_Valid(void);
int8_t MAX30102_Get_SpO2_Valid(void);

#endif
