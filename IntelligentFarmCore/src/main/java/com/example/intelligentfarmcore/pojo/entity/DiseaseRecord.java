package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_disease_record")
public class DiseaseRecord {
    // 记录ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 疾病名称
    @Column(name = "disease_name")
    private String diseaseName;

    // 牲畜编号
    @Column(name = "livestock_code")
    private String livestockCode;

    // 发病日期
    @Column(name = "onset_date")
    private LocalDate onsetDate;

    // 症状
    @Column(name = "symptoms")
    private String symptoms;

    // 治疗措施
    @Column(name = "treatment")
    private String treatment;

    // 状态（治疗中/已康复）
    @Column(name = "status")
    private String status;

    // 软删除标记
    @Column(name = "deleted")
    private Boolean deleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getLivestockCode() {
        return livestockCode;
    }

    public void setLivestockCode(String livestockCode) {
        this.livestockCode = livestockCode;
    }

    public LocalDate getOnsetDate() {
        return onsetDate;
    }

    public void setOnsetDate(LocalDate onsetDate) {
        this.onsetDate = onsetDate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
