package com.example.intelligentfarmcore.pojo.dto;

import com.example.intelligentfarmcore.pojo.entity.Livestock;
import java.time.LocalDateTime;

public class LivestockDTO {
    private Long livestockId;
    private Long farmId;
    private String healthStatus;
    private LocalDateTime inTime;
    private String livestockCode;
    private LocalDateTime outTime;
    private Integer status;
    private String type;
    private Double weight;
    private String livestockName;
    private String livestockType;

    public LivestockDTO() {
    }

    public LivestockDTO(Livestock livestock) {
        this.livestockId = livestock.getLivestockId();
        this.farmId = livestock.getFarmId();
        this.healthStatus = livestock.getHealthStatus();
        this.inTime = livestock.getInTime();
        this.livestockCode = livestock.getLivestockCode();
        this.outTime = livestock.getOutTime();
        this.status = livestock.getStatus();
        this.type = livestock.getType();
        this.weight = livestock.getWeight();
        this.livestockName = livestock.getLivestockName();
        this.livestockType = livestock.getLivestockType();
    }

    public Long getLivestockId() {
        return livestockId;
    }

    public void setLivestockId(Long livestockId) {
        this.livestockId = livestockId;
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }

    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime;
    }

    public String getLivestockCode() {
        return livestockCode;
    }

    public void setLivestockCode(String livestockCode) {
        this.livestockCode = livestockCode;
    }

    public LocalDateTime getOutTime() {
        return outTime;
    }

    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getLivestockName() {
        return livestockName;
    }

    public void setLivestockName(String livestockName) {
        this.livestockName = livestockName;
    }

    public String getLivestockType() {
        return livestockType;
    }

    public void setLivestockType(String livestockType) {
        this.livestockType = livestockType;
    }
}