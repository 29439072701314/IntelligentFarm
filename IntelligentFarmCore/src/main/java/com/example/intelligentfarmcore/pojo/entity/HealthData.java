package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "tb_health_data")
@Entity
public class HealthData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 设备ID
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public HealthDevice toHealthDevice() {
        return new HealthDevice(this.deviceName, this.time, this.heartRate, this.spo2, this.isFallDown);
    }
}
