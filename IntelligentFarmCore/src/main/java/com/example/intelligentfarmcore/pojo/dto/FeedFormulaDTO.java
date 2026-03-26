package com.example.intelligentfarmcore.pojo.dto;

public class FeedFormulaDTO {
    private Long id;
    private String name;
    private String stage;
    private Double dailyRecommendation;
    private Double stock;
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
