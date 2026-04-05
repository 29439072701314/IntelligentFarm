package com.example.intelligentfarmcore.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_feed_stock_record")
public class FeedStockRecord {
    // 记录ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 饲料配方ID
    @Column(name = "formula_id")
    private Long formulaId;

    // 入库数量
    @Column(name = "quantity")
    private Double quantity;

    // 入库时间
    @Column(name = "create_time")
    private LocalDateTime createTime;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 关联的饲料配方
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id", insertable = false, updatable = false)
    private FeedFormula feedFormula;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public FeedFormula getFeedFormula() {
        return feedFormula;
    }

    public void setFeedFormula(FeedFormula feedFormula) {
        this.feedFormula = feedFormula;
    }
}