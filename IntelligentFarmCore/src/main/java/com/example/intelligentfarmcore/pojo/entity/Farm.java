package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_farm")
public class Farm {
    // 农场ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farm_id")
    private Long farmId;

    // 农场名称
    @Column(name = "farm_name")
    private String farmName;

    // 农场地址
    @Column(name = "address")
    private String address;

    // 用户ID
    @Column(name = "user_id")
    private Long userId;

    // 关联的牲畜
    @OneToMany(mappedBy = "farm")
    private List<Livestock> livestockList;

    // 关联的设备
    @OneToMany(mappedBy = "farm")
    private List<Device> deviceList;

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Livestock> getLivestockList() {
        return livestockList;
    }

    public void setLivestockList(List<Livestock> livestockList) {
        this.livestockList = livestockList;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public String toString() {
        return "Farm{" +
                "farmId=" + farmId +
                ", farmName='" + farmName + '\'' +
                ", address='" + address + '\'' +
                ", userId=" + userId +
                '}';
    }
}