package com.example.intelligentfarmcore.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_livestock_weight_record")
public class LivestockWeightRecord {
    // 记录ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 牲畜ID
    @Column(name = "livestock_id")
    private Long livestockId;

    // 体重
    @Column(name = "weight")
    private Double weight;

    // 记录时间
    @Column(name = "record_time")
    private LocalDateTime recordTime;

    // 关联的牲畜
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livestock_id", insertable = false, updatable = false)
    private Livestock livestock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLivestockId() {
        return livestockId;
    }

    public void setLivestockId(Long livestockId) {
        this.livestockId = livestockId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    public Livestock getLivestock() {
        return livestock;
    }

    public void setLivestock(Livestock livestock) {
        this.livestock = livestock;
    }

    @Override
    public String toString() {
        return "LivestockWeightRecord{" +
                "id=" + id +
                ", livestockId=" + livestockId +
                ", weight=" + weight +
                ", recordTime=" + recordTime +
                '}';
    }
}
