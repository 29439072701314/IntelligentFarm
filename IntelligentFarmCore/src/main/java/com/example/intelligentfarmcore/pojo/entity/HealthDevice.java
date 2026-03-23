package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_health_device")
public class HealthDevice {
    // 设备ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    // 设备名称
    @Column(name = "device_name")
    private String deviceName;

    // 传感器数据采集时间
    @Column(name = "time")
    private Long time;

    // 心率
    @Column(name = "heart_rate")
    private Integer heartRate;

    // 血氧
    @Column(name = "spo2")
    private Integer spo2;

    // 是否跌倒
    @Column(name = "is_fall_down")
    private Boolean isFallDown;

    @OneToOne(mappedBy = "healthDevice")
    private User user;

    public HealthDevice() {
    }

    public HealthDevice(String deviceName, Long time, Integer heartRate, Integer spo2, Boolean isFallDown) {
        this.deviceName = deviceName;
        this.time = time;
        this.heartRate = heartRate;
        this.spo2 = spo2;
        this.isFallDown = isFallDown;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

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

    public void setHealthDeviceData(HealthDevice healthDevice) {
        // 心率和血氧可能为空（为空时不更新）
        if (healthDevice.getHeartRate() != 0) {
            this.heartRate = healthDevice.getHeartRate();
        }
        if (healthDevice.getSpo2() != 0) {
            this.spo2 = healthDevice.getSpo2();
        }
        this.isFallDown = healthDevice.getIsFallDown();
    }
}
