package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_livestock")
public class Livestock {
    // 牲畜ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "livestock_id")
    private Long livestockId;

    // 农场ID
    @Column(name = "farm_id")
    private Long farmId;

    // 健康状态
    @Column(name = "health_status")
    private String healthStatus;

    // 入场时间
    @Column(name = "in_time")
    private LocalDateTime inTime;

    // 牲畜编码
    @Column(name = "livestock_code")
    private String livestockCode;

    // 出场时间
    @Column(name = "out_time")
    private LocalDateTime outTime;

    // 状态
    @Column(name = "status")
    private Integer status;

    // 类型
    @Column(name = "type")
    private String type;

    // 体重
    @Column(name = "weight")
    private Double weight;

    // 牲畜名称
    @Column(name = "livestock_name")
    private String livestockName;

    // 牲畜类型
    @Column(name = "livestock_type")
    private String livestockType;

    // 关联的农场
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farm_id", insertable = false, updatable = false)
    private Farm farm;

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

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    @Override
    public String toString() {
        return "Livestock{" +
                "livestockId=" + livestockId +
                ", farmId=" + farmId +
                ", healthStatus='" + healthStatus + '\'' +
                ", inTime=" + inTime +
                ", livestockCode='" + livestockCode + '\'' +
                ", outTime=" + outTime +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", weight=" + weight +
                ", livestockName='" + livestockName + '\'' +
                ", livestockType='" + livestockType + '\'' +
                '}';
    }
}