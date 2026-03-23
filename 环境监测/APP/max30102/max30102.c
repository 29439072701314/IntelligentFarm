#include "max30102.h"
#include "max30102_iic.h"
#include "SysTick.h"
#include "algorithm.h"

// MAX30102全局数据变量
static MAX30102_Data_t max30102_data;

u8 max30102_Bus_Write(u8 Register_Address, u8 Word_Data)
{

    /* 第1步：发起I2C总线启动信号 */
    IIC_Start();

    /* 第2步：发起通讯地址，高7bit是地址，bit0是读写控制位，0表示写，1表示读 */
    IIC_Send_Byte(max30102_WR_address | I2C_WR); /* 此处是写指令 */

    /* 第3步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第4步：发送字节地址 */
    IIC_Send_Byte(Register_Address);
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第5步：开始写入数据 */
    IIC_Send_Byte(Word_Data);

    /* 第6步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 发送I2C总线停止信号 */
    IIC_Stop();
    return 1; /* 执行成功 */

cmd_fail: /* 命令执行失败后，切记发送停止信号，避免影响I2C总线上其他设备 */
    /* 发送I2C总线停止信号 */
    IIC_Stop();
    return 0;
}

u8 max30102_Bus_Read(u8 Register_Address)
{
    u8 data;

    /* 第1步：发起I2C总线启动信号 */
    IIC_Start();

    /* 第2步：发起通讯地址，高7bit是地址，bit0是读写控制位，0表示写，1表示读 */
    IIC_Send_Byte(max30102_WR_address | I2C_WR); /* 此处是写指令 */

    /* 第3步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第4步：发送字节地址 */
    IIC_Send_Byte((uint8_t)Register_Address);
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第6步：重新启动I2C总线。下面开始读取数据 */
    IIC_Start();

    /* 第7步：发起通讯地址，高7bit是地址，bit0是读写控制位，0表示写，1表示读 */
    IIC_Send_Byte(max30102_WR_address | I2C_RD); /* 此处是读指令 */

    /* 第8步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第9步：读取数据 */
    {
        data = IIC_Read_Byte(0); /* 读1个字节 */

        IIC_NAck(); /* 最后1个字节读完后，CPU产生NACK信号(驱动SDA = 1) */
    }
    /* 发送I2C总线停止信号 */
    IIC_Stop();
    return data; /* 执行成功 返回data值 */

cmd_fail: /* 命令执行失败后，切记发送停止信号，避免影响I2C总线上其他设备 */
    /* 发送I2C总线停止信号 */
    IIC_Stop();
    return 0;
}

void max30102_FIFO_ReadWords(u8 Register_Address, u16 Word_Data[][2], u8 count)
{
    u8 i  = 0;
    u8 no = count;
    u8 data1, data2;
    /* 第1步：发起I2C总线启动信号 */
    IIC_Start();

    /* 第2步：发起通讯地址，高7bit是地址，bit0是读写控制位，0表示写，1表示读 */
    IIC_Send_Byte(max30102_WR_address | I2C_WR); /* 此处是写指令 */

    /* 第3步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第4步：发送字节地址 */
    IIC_Send_Byte((uint8_t)Register_Address);
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第6步：重新启动I2C总线。下面开始读取数据 */
    IIC_Start();

    /* 第7步：发起通讯地址，高7bit是地址，bit0是读写控制位，0表示写，1表示读 */
    IIC_Send_Byte(max30102_WR_address | I2C_RD); /* 此处是读指令 */

    /* 第8步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第9步：读取数据 */
    while (no) {
        data1 = IIC_Read_Byte(0);
        IIC_Ack();
        data2 = IIC_Read_Byte(0);
        IIC_Ack();
        Word_Data[i][0] = (((u16)data1 << 8) | data2); //

        data1 = IIC_Read_Byte(0);
        IIC_Ack();
        data2 = IIC_Read_Byte(0);
        if (1 == no)
            IIC_NAck(); /* 最后1个字节读完后，CPU产生NACK信号(驱动SDA = 1) */
        else
            IIC_Ack();
        Word_Data[i][1] = (((u16)data1 << 8) | data2);

        no--;
        i++;
    }
    /* 发送I2C总线停止信号 */
    IIC_Stop();

cmd_fail: /* 命令执行失败后，切记发送停止信号，避免影响I2C总线上其他设备 */
    /* 发送I2C总线停止信号 */
    IIC_Stop();
}

void max30102_FIFO_ReadBytes(u8 Register_Address, u8 *Data)
{
    max30102_Bus_Read(REG_INTR_STATUS_1);
    max30102_Bus_Read(REG_INTR_STATUS_2);

    /* 第1步：发起I2C总线启动信号 */
    IIC_Start();

    /* 第2步：发起通讯地址，高7bit是地址，bit0是读写控制位，0表示写，1表示读 */
    IIC_Send_Byte(max30102_WR_address | I2C_WR); /* 此处是写指令 */

    /* 第3步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第4步：发送字节地址 */
    IIC_Send_Byte((uint8_t)Register_Address);
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第6步：重新启动I2C总线。下面开始读取数据 */
    IIC_Start();

    /* 第7步：发起通讯地址，高7bit是地址，bit0是读写控制位，0表示写，1表示读 */
    IIC_Send_Byte(max30102_WR_address | I2C_RD); /* 此处是读指令 */

    /* 第8步：等待ACK */
    if (IIC_Wait_Ack() != 0) {
        goto cmd_fail; /* EEPROM器件无应答 */
    }

    /* 第9步：读取数据 */
    Data[0] = IIC_Read_Byte(1);
    Data[1] = IIC_Read_Byte(1);
    Data[2] = IIC_Read_Byte(1);
    Data[3] = IIC_Read_Byte(1);
    Data[4] = IIC_Read_Byte(1);
    Data[5] = IIC_Read_Byte(0);
    /* 最后1个字节读完后，CPU产生NACK信号(驱动SDA = 1) */
    /* 发送I2C总线停止信号 */
    IIC_Stop();

cmd_fail: /* 命令执行失败后，切记发送停止信号，避免影响I2C总线上其他设备 */
    /* 发送I2C总线停止信号 */
    IIC_Stop();
}

void max30102_init(void)
{
    GPIO_InitTypeDef GPIO_InitStructure;

    RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB, ENABLE);
    GPIO_InitStructure.GPIO_Pin  = GPIO_Pin_9;
    GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPU;
    GPIO_Init(GPIOB, &GPIO_InitStructure);

    IIC_Init();

    max30102_reset();

    max30102_Bus_Write(REG_INTR_ENABLE_1, 0xc0); // INTR setting
    max30102_Bus_Write(REG_INTR_ENABLE_2, 0x00);
    max30102_Bus_Write(REG_FIFO_WR_PTR, 0x00); // FIFO_WR_PTR[4:0]
    max30102_Bus_Write(REG_OVF_COUNTER, 0x00); // OVF_COUNTER[4:0]
    max30102_Bus_Write(REG_FIFO_RD_PTR, 0x00); // FIFO_RD_PTR[4:0]
    max30102_Bus_Write(REG_FIFO_CONFIG, 0x0f); // sample avg = 1, fifo rollover=false, fifo almost full = 17
    max30102_Bus_Write(REG_MODE_CONFIG, 0x03); // 0x02 for Red only, 0x03 for SpO2 mode 0x07 multimode LED
    max30102_Bus_Write(REG_SPO2_CONFIG, 0x27); // SPO2_ADC range = 4096nA, SPO2 sample rate (100 Hz), LED pulseWidth (400uS)
    max30102_Bus_Write(REG_LED1_PA, 0x24);     // Choose value for ~ 7mA for LED1
    max30102_Bus_Write(REG_LED2_PA, 0x24);     // Choose value for ~ 7mA for LED2
    max30102_Bus_Write(REG_PILOT_PA, 0x7f);    // Choose value for ~ 25mA for Pilot LED
}

void max30102_reset(void)
{
    max30102_Bus_Write(REG_MODE_CONFIG, 0x40);
    max30102_Bus_Write(REG_MODE_CONFIG, 0x40);
}

void maxim_max30102_write_reg(uint8_t uch_addr, uint8_t uch_data)
{
    IIC_Write_One_Byte(I2C_WRITE_ADDR, uch_addr, uch_data);
}

void maxim_max30102_read_reg(uint8_t uch_addr, uint8_t *puch_data)
{
    IIC_Read_One_Byte(I2C_WRITE_ADDR, uch_addr, puch_data);
}

void maxim_max30102_read_fifo(uint32_t *pun_red_led, uint32_t *pun_ir_led)
{
    uint32_t un_temp;
    unsigned char uch_temp;
    char ach_i2c_data[6];
    *pun_red_led = 0;
    *pun_ir_led  = 0;

    // read and clear status register
    maxim_max30102_read_reg(REG_INTR_STATUS_1, &uch_temp);
    maxim_max30102_read_reg(REG_INTR_STATUS_2, &uch_temp);

    IIC_ReadBytes(I2C_WRITE_ADDR, REG_FIFO_DATA, (u8 *)ach_i2c_data, 6);

    un_temp = (unsigned char)ach_i2c_data[0];
    un_temp <<= 16;
    *pun_red_led += un_temp;
    un_temp = (unsigned char)ach_i2c_data[1];
    un_temp <<= 8;
    *pun_red_led += un_temp;
    un_temp = (unsigned char)ach_i2c_data[2];
    *pun_red_led += un_temp;

    un_temp = (unsigned char)ach_i2c_data[3];
    un_temp <<= 16;
    *pun_ir_led += un_temp;
    un_temp = (unsigned char)ach_i2c_data[4];
    un_temp <<= 8;
    *pun_ir_led += un_temp;
    un_temp = (unsigned char)ach_i2c_data[5];
    *pun_ir_led += un_temp;
    *pun_red_led &= 0x03FFFF; // Mask MSB [23:18]
    *pun_ir_led &= 0x03FFFF;  // Mask MSB [23:18]
}

// MAX30102应用层函数实现

// MAX30102应用层初始化
void MAX30102_App_Init(void)
{
    uint32_t un_min, un_max;
    int i;

    max30102_init();
    printf("\r\n MAX30102 init \r\n");

    un_min = 0x3FFFF;
    un_max = 0;

    max30102_data.n_ir_buffer_length = 500; // buffer length of 100 stores 5 seconds of samples running at 100sps
    // read first 500 samples, and determine signal range
    for (i = 0; i < max30102_data.n_ir_buffer_length; i++) {
        while (MAX30102_INT == 1)
            ; // wait until interrupt pin asserts
        max30102_FIFO_ReadBytes(REG_FIFO_DATA, max30102_data.temp_max30102);
        max30102_data.aun_red_buffer[i] = (long)((long)((long)max30102_data.temp_max30102[0] & 0x03) << 16) | (long)max30102_data.temp_max30102[1] << 8 | (long)max30102_data.temp_max30102[2];
        max30102_data.aun_ir_buffer[i]  = (long)((long)((long)max30102_data.temp_max30102[3] & 0x03) << 16) | (long)max30102_data.temp_max30102[4] << 8 | (long)max30102_data.temp_max30102[5];

        if (un_min > max30102_data.aun_red_buffer[i])
            un_min = max30102_data.aun_red_buffer[i];
        if (un_max < max30102_data.aun_red_buffer[i])
            un_max = max30102_data.aun_red_buffer[i];
    }
    // calculate heart rate and SpO2 after first 500 samples (first 5 seconds of samples)
    maxim_heart_rate_and_oxygen_saturation(max30102_data.aun_ir_buffer, max30102_data.n_ir_buffer_length, max30102_data.aun_red_buffer, &max30102_data.n_sp02, &max30102_data.ch_spo2_valid, &max30102_data.n_heart_rate, &max30102_data.ch_hr_valid);
    printf("MAX30102 init done\r\n");
}

// MAX30102应用层数据处理
void MAX30102_App_Process(void)
{
    int i;
    uint32_t un_min, un_max;
    uint32_t un_prev_data;
    float f_temp;
    int32_t n_brightness;
    uint32_t timeout;

    i      = 0;
    un_min = 0x3FFFF;
    un_max = 0;

    // dumping the first 100 sets of samples in the memory and shift the last 400 sets of samples to the top
    for (i = 100; i < 500; i++) {
        max30102_data.aun_red_buffer[i - 100] = max30102_data.aun_red_buffer[i];
        max30102_data.aun_ir_buffer[i - 100]  = max30102_data.aun_ir_buffer[i];

        // update the signal min and max
        if (un_min > max30102_data.aun_red_buffer[i])
            un_min = max30102_data.aun_red_buffer[i];
        if (un_max < max30102_data.aun_red_buffer[i])
            un_max = max30102_data.aun_red_buffer[i];
    }

    // take 100 sets of samples before calculating the heart rate.
    for (i = 400; i < 500; i++) {
        un_prev_data = max30102_data.aun_red_buffer[i - 1];

        // 添加超时机制，防止无限等待
        timeout = 100000; // 设置一个超时值
        while (MAX30102_INT == 1) {
            timeout--;
            if (timeout == 0) {
                // 超时，设置数据为无效
                max30102_data.ch_hr_valid   = 0;
                max30102_data.ch_spo2_valid = 0;
                max30102_data.n_heart_rate  = 0;
                max30102_data.n_sp02        = 0;
                printf("MAX30102 read timeout\r\n");
                return;
            }
        }

        max30102_FIFO_ReadBytes(REG_FIFO_DATA, max30102_data.temp_max30102);
        max30102_data.aun_red_buffer[i] = (long)((long)((long)max30102_data.temp_max30102[0] & 0x03) << 16) | (long)max30102_data.temp_max30102[1] << 8 | (long)max30102_data.temp_max30102[2];
        max30102_data.aun_ir_buffer[i]  = (long)((long)((long)max30102_data.temp_max30102[3] & 0x03) << 16) | (long)max30102_data.temp_max30102[4] << 8 | (long)max30102_data.temp_max30102[5];

        if (max30102_data.aun_red_buffer[i] > un_prev_data) {
            f_temp = max30102_data.aun_red_buffer[i] - un_prev_data;
            f_temp /= (un_max - un_min);
            f_temp *= MAX_BRIGHTNESS;
            n_brightness -= (int)f_temp;
            if (n_brightness < 0)
                n_brightness = 0;
        } else {
            f_temp = un_prev_data - max30102_data.aun_red_buffer[i];
            f_temp /= (un_max - un_min);
            f_temp *= MAX_BRIGHTNESS;
            n_brightness += (int)f_temp;
            if (n_brightness > MAX_BRIGHTNESS)
                n_brightness = MAX_BRIGHTNESS;
        }
    }
    maxim_heart_rate_and_oxygen_saturation(max30102_data.aun_ir_buffer, max30102_data.n_ir_buffer_length, max30102_data.aun_red_buffer, &max30102_data.n_sp02, &max30102_data.ch_spo2_valid, &max30102_data.n_heart_rate, &max30102_data.ch_hr_valid);
}

//获取心率数据
int32_t MAX30102_Get_HeartRate(void)
{
    return max30102_data.n_heart_rate;
}

//获取血氧数据
int32_t MAX30102_Get_SpO2(void)
{
    return max30102_data.n_sp02;
}

//获取心率数据有效性
int8_t MAX30102_Get_HR_Valid(void)
{
    return max30102_data.ch_hr_valid;
}

//获取血氧数据有效性
int8_t MAX30102_Get_SpO2_Valid(void)
{
    return max30102_data.ch_spo2_valid;
}
