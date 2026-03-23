package com.example.intelligentfarmcore.pojo.dto;

/**
 * Device摘要数据传输对象，用于避免循环引用
 */
public class DeviceSummaryDTO {
    // 设备ID
    private Long deviceId;

    // 设备名称
    private String deviceName;

    // 构造方法
    public DeviceSummaryDTO() {
    }

    public DeviceSummaryDTO(Long deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    // 从实体转换为DTO的构造方法
    public DeviceSummaryDTO(com.example.intelligentfarmcore.pojo.entity.Device device) {
        this.deviceId = device.getDeviceId();
        this.deviceName = device.getDeviceName();
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