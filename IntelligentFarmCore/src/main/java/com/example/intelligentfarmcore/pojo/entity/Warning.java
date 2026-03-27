package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "tb_warning")
@Entity
public class Warning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type; // 告警类型：environment, livestock, feed

    @Column(name = "source")
    private String source; // 告警来源：设备ID、牲畜编号、配方ID

    @Column(name = "detail")
    private String detail; // 告警详情
    
    @Column(name = "details")
    private String details; // 告警详情

    @Column(name = "level")
    private String level; // 告警级别：high, medium, low

    @Column(name = "status")
    private String status; // 告警状态：unhandled, handled, resolved

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "handled_at")
    private LocalDateTime handledAt;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    @Override
    public String toString() {
        return "Warning{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", detail='" + detail + '\'' +
                ", details='" + details + '\'' +
                ", level='" + level + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", handledAt=" + handledAt +
                '}';
    }
}