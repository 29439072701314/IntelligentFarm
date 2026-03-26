package com.example.intelligentfarmcore.pojo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_feed_plan")
public class FeedPlan {
    // 计划ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 日期
    @Column(name = "date")
    private LocalDate date;

    // 日期(别名，兼容数据库字段)
    @Column(name = "plan_date")
    private LocalDate planDate;

    // 时间
    @Column(name = "time")
    private LocalTime time;

    // 时间(别名，兼容数据库字段)
    @Column(name = "plan_time")
    private LocalTime planTime;

    // 区域
    @Column(name = "area")
    private String area;

    // 配方ID
    @Column(name = "formula_id")
    private Long formulaId;

    // 数量
    @Column(name = "quantity")
    private Double quantity;

    // 数量(别名，兼容数据库字段)
    @Column(name = "amount")
    private Double amount;

    // 状态
    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalTime getPlanTime() {
        return planTime;
    }

    public void setPlanTime(LocalTime planTime) {
        this.planTime = planTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Long formulaId) {
        this.formulaId = formulaId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
