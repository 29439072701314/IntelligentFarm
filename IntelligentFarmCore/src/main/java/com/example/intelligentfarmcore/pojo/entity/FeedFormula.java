package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_feed_formula")
public class FeedFormula {
    // 配方ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 配方名称
    @Column(name = "name")
    private String name;

    // 适用阶段
    @Column(name = "stage")
    private String stage;

    // 日建议量
    @Column(name = "daily_recommendation")
    private Double dailyRecommendation;

    // 日建议量(别名，兼容数据库字段)
    @Column(name = "daily_amount")
    private Double dailyAmount;

    // 库存
    @Column(name = "stock")
    private Double stock;

    // 阈值
    @Column(name = "threshold")
    private Double threshold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Double getDailyRecommendation() {
        return dailyRecommendation;
    }

    public void setDailyRecommendation(Double dailyRecommendation) {
        this.dailyRecommendation = dailyRecommendation;
    }

    public Double getDailyAmount() {
        return dailyAmount;
    }

    public void setDailyAmount(Double dailyAmount) {
        this.dailyAmount = dailyAmount;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }
}
