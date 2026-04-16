package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;

@Table(name = "tb_weight_device")
@Entity
public class WeightDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 设备名称
    @Column(name = "device_name")
    private String deviceName;

    // 传感器数据采集时间
    @Column(name = "time")
    private Long time;

    // 重量
    @Column(name = "weight")
    private Integer weight;

    // 构造方法
    public WeightDevice() {
    }

    public WeightDevice(String deviceName, Long time, Integer weight) {
        this.deviceName = deviceName;
        this.time = time;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setDeviceData(WeightDevice newWeightDeviceData) {
        this.time = newWeightDeviceData.getTime();
        this.weight = newWeightDeviceData.getWeight();
    }
}
