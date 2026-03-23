#include "esp8266.h"
#include "led.h"
/** 初始化指令 */
char ESP_InitCmd[8][400] = {
    "AT+RST\r\n",
    "AT+CWMODE=1\r\n",
    "AT+CIPSNTPCFG=1,8,\"ntp1.aliyun.com\"\r\n",
    "AT+CWJAP=\"MEIZU 18s\",\"1234567890\"\r\n",
    "AT+MQTTUSERCFG=0,1,\"NULL\",\"Environmental&k1x0t4xETH9\",\"664790da4fa6fe59074ef414ca4333d1ddca9b3fe042b17f9b80b14654e42e16\",0,0,\"\"\r\n",
    "AT+MQTTCLIENTID=0,\"k1x0t4xETH9.Environmental|securemode=2\\,signmethod=hmacsha256\\,timestamp=1765969085677|\"\r\n",
    "AT+MQTTCONN=0,\"iot-06z00hax6b0i8g0.mqtt.iothub.aliyuncs.com\",1883,1\r\n",
    // "AT+MQTTSUB=0,\"/sys/k1x0t4xETH9/Environmental/thing/service/property/set\",1\r\n",
    // "AT+MQTTSUB=0,\"/sys/k1x0t4xETH9/Environmental/thing/service/property/get\",1\r\n",
    "AT+MQTTSUB=0,\"/k1x0t4xETH9/Environmental/user/get\",1\r\n",
    // "AT+MQTTSUB=0,\"/sys/k1x0t4xETH9/Environmental/thing/event/property/post_reply\",1\r\n"
};
unsigned char ESP_buf[2048];                // ESP8266接收缓存
unsigned short ESP_cnt = 0, ESP_cntPre = 0; // ESP8266接收缓存计数器
/**
 * @brief  清空ESP8266接收缓存
 * @note   该函数用于清空ESP8266接收缓存，将缓存内容全部设为0
 * @retval None
 */
void ESP_Clear(void)
{
    memset(ESP_buf, 0, sizeof(ESP_buf));
    ESP_cnt    = 0;
    ESP_cntPre = 0;
}

/**
 * @brief  等待ESP8266接收完成
 * @note   该函数用于等待ESP8266接收完成，判断是否接收到完整数据
 * @retval REV_NONE  未接收到任何数据
 * @retval REV_OK    接收完成
 * @retval REV_HALFWAY  接收未完成
 */
u8 ESP_WaitRecive(void)
{
    // printf("ESP_cnt = %d, ESP_cntPre = %d\r\n", ESP_cnt, ESP_cntPre);
    // 如果接收计数器为0，表示还没有接收到任何数据
    if (ESP_cnt == 0)
        return REV_NONE; // 返回未接收到任何数据状态

    // 如果当前接收计数器等于上一次记录的计数器值，表示数据接收完成且稳定
    if (ESP_cnt == ESP_cntPre) {
        ESP_cnt = 0;   // 清零接收计数器，准备下一次接收
        return REV_OK; // 返回接收完成状态
    }

    // 如果接收计数器不等于上一次记录的值，表示数据正在接收中
    ESP_cntPre = ESP_cnt; // 更新记录值为当前计数器值
    return REV_HALFWAY;   // 返回接收未完成状态
}

/**
 * @brief  向ESP8266发送AT指令并等待回显
 * @note   该函数用于向ESP8266发送AT指令，并等待指令回显，判断指令是否执行成功
 * @param  cmd: 要发送的AT指令字符串
 * @param  echo: 指令回显关键字符串，用于判断指令是否执行成功
 * @param  timeout: 超时时间，单位为毫秒（u16限制为65535ms）
 * @retval 0  指令执行成功
 * @retval 1  指令执行失败或超时
 */
_Bool ESP_SendCmdAndRecEcho(char *cmd, char *echo, u16 timeout)
{
    unsigned char repeatCnt = timeout / 10;
    u8 waitTime;
    char timeStr[20];
    ESP_Clear(); // 清空接收缓存
    // 向ESP8266发送AT指令
    Usart_SendString(USART2, (unsigned char *)cmd, strlen((const char *)cmd));

    while (repeatCnt--) {
        if (ESP_WaitRecive() == REV_OK) // 判断是否接收到完整数据
        {
            if (strstr((const char *)ESP_buf, echo) != NULL) // 判断是否接收到指令回显关键字
            {
                UsartPrintf(USART1, (char *)ESP_buf); // 打印接收到的数据
                waitTime = timeout - repeatCnt * 10;  // 计算指令执行时间
                sprintf(timeStr, "%d", waitTime);
                strcat(timeStr, "ms\r\n");
                UsartPrintf(USART1, timeStr);

                ESP_Clear(); // 清空接收缓存

                return 0;
            }
        }
        delay_ms(10); // 延时10ms，等待下一次接收
    }
    return 1;
}
/**
 * @brief  打印ESP8266接收到的数据
 * @note   该函数用于打印ESP8266接收的数据，并打印到USART1
 * @retval None
 */
void ESP_PrintReceive(void)
{
    if (ESP_WaitRecive() == REV_OK) // 判断是否接收到完整数据
    {
        UsartPrintf(USART1, (char *)ESP_buf); // 打印接收到的数据
        ESP_Clear();                          // 清空接收缓存
    }
}

/**
 * @brief  检查ESP8266接收到的数据是否包含特定信息
 * @note   该函数用于检查ESP8266接收的数据是否包含特定信息
 * @param  info: 需要检查的信息字符串
 * @retval 1: 包含指定信息
 * @retval 0: 不包含指定信息
 */
_Bool ESP_CheckReceiveInfo(char *info)
{
    _Bool result = 0;
    if (ESP_WaitRecive() == REV_OK) // 判断是否接收到完整数据
    {
        if (strstr((const char *)ESP_buf, info) != NULL) {
            result = 1;
        }
        ESP_Clear(); // 清空接收缓存
    }
    return result;
}
void ESP_Init(void)
{
    ESP_Clear(); // 清空接收缓存

    // UsartPrintf(USART1, "开始初始化\r\n\r\n"); // 向USART1打印提示信息

    for (int i = 0; i < strlen(ESP_InitCmd[i]); i++) {
        while (ESP_SendCmdAndRecEcho(ESP_InitCmd[i], "OK", 5000)) {
            printf("%s(指令失败，等待重试)\r\n", ESP_InitCmd[i]);
            LED_Blink(LED_Red, 10, 100);
        }
        delay_ms(1000);
    }
}

void USART2_IRQHandler(void)
{
    u8 r;
    if (USART_GetITStatus(USART2, USART_IT_RXNE) != RESET) //接收中断
    {
        if (ESP_cnt >= sizeof(ESP_buf)) ESP_cnt = 0; // 防止缓存溢出
        r = USART_ReceiveData(USART2);               //(USART2->DR);	//读取接收到的数据
        // 记录接收到的数据到缓存
        ESP_buf[ESP_cnt++] = r;
    }
}
/**
 * @brief  上传传感器数据
 * @note   该函数用于向ESP8266发送AT指令，上传传感器数据
 * @param  data: 传感器数据结构体数组，包含传感器名称和数值(最后一个元素的name为NULL)
 * @retval None
 */
void ESP_UploadSensorData(SensorData *data)
{
    char cmd[400];
    char jsonParams[300] = "{\\\"params\\\":{";
    // 构建params部分的JSON字符串
    int i = 0;
    while (data[i].name != NULL) {
        char paramEntry[50];
        if (i > 0) {
            strcat(jsonParams, ",");
        }
        // 根据数值类型决定是否保留小数点
        if (data[i].value == (int)data[i].value) {
            sprintf(paramEntry, "\\\"%s\\\":%d\\", data[i].name, (int)data[i].value);
        } else {
            sprintf(paramEntry, "\\\"%s\\\":%.2f\\", data[i].name, data[i].value);
        }
        strcat(jsonParams, paramEntry);
        i++;
    }

    strcat(jsonParams, "}\\,\\\"version\\\":\\\"1.0.0\\\"}");

    // 构建完整的AT指令
    sprintf(cmd, "AT+MQTTPUB=0,\"/sys/k1x0t4xETH9/Environmental/thing/event/property/post\",\"%s\",0,0\r\n", jsonParams);
    // 发送AT指令(不回显)
    Usart_SendString(USART2, (unsigned char *)cmd, strlen((const char *)cmd));
}