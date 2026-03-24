package com.example.intelligentfarmcore.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_device")
@JsonIgnoreProperties({ "environmentSensors" })
public class Device {
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

    // 温度
    @Column(name = "temperature")
    private Integer temperature;

    // 湿度
    @Column(name = "humidity")
    private Integer humidity;

    // 气体浓度
    @Column(name = "gas_concentration")
    private Integer gasConcentration;

    // 农场ID
    @Column(name = "farm_id")
    private Long farmId;

    @OneToOne(mappedBy = "device")
    private Room room;

    // 关联的农场
    @ManyToOne
    @JoinColumn(name = "farm_id", insertable = false, updatable = false)
    private Farm farm;

    // 构造方法
    public Device() {
    }

    public Device(String deviceName, Long time, Integer temperature, Integer humidity, Integer gasConcentration) {
        this.deviceName = deviceName;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.gasConcentration = gasConcentration;
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

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public void setDeviceData(Device newDeviceData) {
        this.time = newDeviceData.getTime();
        this.temperature = newDeviceData.getTemperature();
        this.humidity = newDeviceData.getHumidity();
        this.gasConcentration = newDeviceData.getGasConcentration();
    }
}
