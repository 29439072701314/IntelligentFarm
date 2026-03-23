package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;

@Table(name = "tb_environment_data")
@Entity
public class EnvironmentData {
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

    // 温度
    @Column(name = "temperature")
    private Integer temperature;

    // 湿度
    @Column(name = "humidity")
    private Integer humidity;

    // 气体浓度
    @Column(name = "gas_concentration")
    private Integer gasConcentration;

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

    public Device toDevice() {
        return new Device(this.deviceName, this.time, this.temperature, this.humidity, this.gasConcentration);
    }
}