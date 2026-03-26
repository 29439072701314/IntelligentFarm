package com.example.intelligentfarmcore.pojo.dto;

import java.time.LocalDate;

public class DiseaseRecordDTO {
    private Long id;
    private String diseaseName;
    private String livestockCode;
    private LocalDate onsetDate;
    private String symptoms;
    private String treatment;
    private String status;

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
}
