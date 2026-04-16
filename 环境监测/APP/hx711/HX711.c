
#include <stdio.h>
#include "stm32f10x_gpio.h"
#include "HX711.h"

// sbit ADDO=P2^0;
// sbit ADSK=P2^1;

#define ADSK_H GPIO_SetBits(HX711_GPIO, HX711_SCK)
#define ADSK_L GPIO_ResetBits(HX711_GPIO, HX711_SCK)

void static Delay(u32 del)
{
    while (del--)
        ;
}

void GPIO_Configuration_HX711(void)
{
    GPIO_InitTypeDef GPIO_InitStructure;

    GPIO_InitStructure.GPIO_Pin   = HX711_SCK;
    GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
    GPIO_InitStructure.GPIO_Mode  = GPIO_Mode_Out_PP;
    GPIO_Init(HX711_GPIO, &GPIO_InitStructure);

    GPIO_InitStructure.GPIO_Pin   = HX711_DOUT;
    GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
    GPIO_InitStructure.GPIO_Mode  = GPIO_Mode_IPU;
    GPIO_Init(HX711_GPIO, &GPIO_InitStructure);
}

//读711 AD数据子程序

//**********************************************************/
u32 ReadCount(void)
{
    u32 Count;
    u16 ADval;
    u8 i;
    ADSK_L;
    Count = 0;
    while (GPIO_ReadInputDataBit(HX711_GPIO, HX711_DOUT))
        ;
    for (i = 0; i < 24; i++) //读24个bit
    {
        ADSK_H;
        Delay(5);
        Count = Count << 1;
        ADSK_L;
        Delay(5);
        if (GPIO_ReadInputDataBit(HX711_GPIO, HX711_DOUT)) Count++;
    }
    ADSK_H; //发送第25个脉冲，表示下次转换使用A通道128db
    Delay(5);
    //	Count=Count^0x800000;
    ;
    ADSK_L;
    Delay(5);
    /*	if((Count & 0x800000) == 0x800000)
        {
            Count = ~(Count - 1);
        }  		*/
    //	ADval = (int)(Count >> 8);//取高十六位有效值
    ADval = (int)(Count >> 8);
    //	ADDO=1;
    return (ADval);
}

u32 Weighing(void)
{
    u32 X1;
    u8 t, t1, count = 0;
    u16 databuffer[30]; //过采样缓冲区
    u16 temp = 0;
    u32 X;
    do { //循环读数30次
        X = ReadCount();
        if (X > 100) //如果是在测量在有效区范围的值，标示此读数有效
        {
            databuffer[count] = X;
            count++;
        }
    } while (count < 30);
    if (count == 30) //每次度数一定要读到30次数据,否则丢弃
    {
        do //将数据X升序排列
        {
            t1 = 0;
            for (t = 0; t < count - 1; t++) {
                if (databuffer[t] > databuffer[t + 1]) //升序排列
                {
                    temp              = databuffer[t + 1];
                    databuffer[t + 1] = databuffer[t];
                    databuffer[t]     = temp;
                    t1                = 1;
                }
            }
        } while (t1);
    }
    /* 从排序过的数组里中间抽取连续的10组数据，进行取平均值,获得较高的精度 */
    X1 = 0;
    for (count = 10; count < 20; count++) {
        X1 = X1 + databuffer[count];
    }
    X1 = X1 / 10;
    return X1;
}
