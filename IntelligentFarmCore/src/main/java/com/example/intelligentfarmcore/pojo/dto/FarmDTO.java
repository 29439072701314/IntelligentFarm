package com.example.intelligentfarmcore.pojo.dto;

import com.example.intelligentfarmcore.pojo.entity.Farm;

public class FarmDTO {
    private Long farmId;
    private String farmName;
    private String address;
    private Long userId;
    private Integer livestockCount;
    private Integer deviceCount;

    public FarmDTO() {
    }

    public FarmDTO(Farm farm) {
        this.farmId = farm.getFarmId();
        this.farmName = farm.getFarmName();
        this.address = farm.getAddress();
        this.userId = farm.getUserId();
        this.livestockCount = farm.getLivestockList() != null ? farm.getLivestockList().size() : 0;
        this.deviceCount = farm.getDeviceList() != null ? farm.getDeviceList().size() : 0;
    }

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

    public Integer getLivestockCount() {
        return livestockCount;
    }

    public void setLivestockCount(Integer livestockCount) {
        this.livestockCount = livestockCount;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }
}