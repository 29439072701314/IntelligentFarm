package com.example.intelligentfarmcore.pojo.dto;

import com.example.intelligentfarmcore.pojo.entity.HealthDevice;

public class HealthDeviceSummaryDTO {
    // 设备ID
    private Long deviceId;
    // 设备名称
    private String deviceName;

    // 构造方法
    public HealthDeviceSummaryDTO() {
    }

    // 从实体转换为DTO的构造方法
    public HealthDeviceSummaryDTO(HealthDevice healthDevice) {
        this.deviceId = healthDevice.getDeviceId();
        this.deviceName = healthDevice.getDeviceName();
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
}
