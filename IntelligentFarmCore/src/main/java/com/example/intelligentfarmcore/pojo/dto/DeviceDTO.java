package com.example.intelligentfarmcore.pojo.dto;

/**
 * Device数据传输对象，用于避免循环引用
 */
public class DeviceDTO {
    // 设备ID
    private Long deviceId;

    // 设备名称
    private String deviceName;

    // 传感器数据采集时间
    private Long time;

    // 温度
    private Integer temperature;

    // 湿度
    private Integer humidity;

    // 气体浓度
    private Integer gasConcentration;

    // 关联的农场
    private FarmSummaryDTO farm;

    // 构造方法
    public DeviceDTO() {
    }

    // Getter和Setter方法
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getGasConcentration() {
        return gasConcentration;
    }

    public void setGasConcentration(Integer gasConcentration) {
        this.gasConcentration = gasConcentration;
    }

    public FarmSummaryDTO getFarm() {
        return farm;
    }

    public void setFarm(FarmSummaryDTO farm) {
        this.farm = farm;
    }
}
