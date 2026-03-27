package com.example.intelligentfarmcore.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.intelligentfarmcore.pojo.entity.EnvironmentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorMessageUtils {
    private static final Logger logger = LoggerFactory.getLogger(SensorMessageUtils.class);

    /**
     * 解析传感器消息并转换为EnvironmentSensor对象
     * 
     * @param payload 传感器消息的JSON字符串
     * @return 转换后的EnvironmentSensor对象，解析失败返回null
     */
    public static EnvironmentData parseSensorMessage(String payload) {
        try {
            // 解析JSON格式的payload
            JSONObject jsonObject = JSONObject.parseObject(payload);

            // 提取设备名称
            String deviceName = jsonObject.getString("deviceName");

            // 提取items部分
            JSONObject items = jsonObject.getJSONObject("items");

            // 提取时间戳（使用temperature的time，因为所有items的time都相同）
            long time = items.getJSONObject("temperature").getLong("time");

            // 提取温度
            int temperature = items.getJSONObject("temperature").getInteger("value");

            // 提取湿度
            int humidity = items.getJSONObject("humidity").getInteger("value");

            // 提取气体浓度
            int gasConcentration = items.getJSONObject("gasConcentration").getInteger("value");

            // 创建EnvironmentSensor对象
            EnvironmentData environmentData = new EnvironmentData();
            environmentData.setDeviceName(deviceName);
            environmentData.setTime(time);
            environmentData.setTemperature(temperature);
            environmentData.setHumidity(humidity);
            environmentData.setGasConcentration(gasConcentration);

            return environmentData;
        } catch (Exception e) {
            logger.error("解析消息为EnvironmentSensor对象时出错", e);
            return null;
        }
    }
}
