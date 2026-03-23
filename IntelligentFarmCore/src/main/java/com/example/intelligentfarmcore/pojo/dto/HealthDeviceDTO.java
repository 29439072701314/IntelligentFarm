package com.example.intelligentfarmcore.pojo.dto;

public class HealthDeviceDTO {
    // 设备ID
    private Long deviceId;

    // 设备名称
    private String deviceName;

    // 传感器数据采集时间
    private Long time;

    // 心率
    private Integer heartRate;

    // 血氧
    private Integer spo2;

    // 是否跌倒
    private Boolean isFallDown;

    // 关联的用户基本信息（避免循环引用）
    private UserSummaryDTO user;

    // 构造方法
    public HealthDeviceDTO() {
    }

    // 从实体类构造

    public HealthDeviceDTO(Long deviceId, String deviceName, Long time, Integer heartRate, Integer spo2,
            Boolean isFallDown, UserSummaryDTO user) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.time = time;
        this.heartRate = heartRate;
        this.spo2 = spo2;
        this.isFallDown = isFallDown;
        this.user = user;
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

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getSpo2() {
        return spo2;
    }

    public void setSpo2(Integer spo2) {
        this.spo2 = spo2;
    }

    public Boolean getIsFallDown() {
        return isFallDown;
    }

    public void setIsFallDown(Boolean isFallDown) {
        this.isFallDown = isFallDown;
    }

    public UserSummaryDTO getUser() {
        return user;
    }

    public void setUser(UserSummaryDTO user) {
        this.user = user;
    }
}
