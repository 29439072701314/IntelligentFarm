package com.example.intelligentfarmcore.pojo.dto;

import com.example.intelligentfarmcore.pojo.entity.Warning;

import java.time.LocalDateTime;

public class WarningDTO {
    private Long id;
    private String type;
    private String source;
    private String details;
    private String level;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;

    public WarningDTO() {
    }

    public WarningDTO(Warning warning) {
        this.id = warning.getId();
        this.type = warning.getType();
        this.source = warning.getSource();
        this.details = warning.getDetails();
        this.level = warning.getLevel();
        this.status = warning.getStatus();
        this.createdAt = warning.getCreatedAt();
        this.handledAt = warning.getHandledAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getHandledAt() {
        return handledAt;
    }

    public void setHandledAt(LocalDateTime handledAt) {
        this.handledAt = handledAt;
    }
}