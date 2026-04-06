package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_livestock_record")
public class LivestockRecord {
    // 记录ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    // 农场ID
    @Column(name = "farm_id")
    private Long farmId;

    // 牲畜ID
    @Column(name = "livestock_id")
    private Long livestockId;

    // 操作类型：1-入库，2-出库
    @Column(name = "operation_type")
    private Integer operationType;

    // 操作时间
    @Column(name = "operation_time")
    private LocalDateTime operationTime;

    // 操作人
    @Column(name = "operator")
    private String operator;

    // 备注
    @Column(name = "remark")
    private String remark;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public Long getLivestockId() {
        return livestockId;
    }

    public void setLivestockId(Long livestockId) {
        this.livestockId = livestockId;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "LivestockRecord{" +
                "recordId=" + recordId +
                ", farmId=" + farmId +
                ", livestockId=" + livestockId +
                ", operationType=" + operationType +
                ", operationTime=" + operationTime +
                ", operator='" + operator + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}