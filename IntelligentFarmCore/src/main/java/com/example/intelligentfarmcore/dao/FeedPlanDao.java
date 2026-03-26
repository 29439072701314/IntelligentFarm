package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.FeedPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FeedPlanDao extends JpaRepository<FeedPlan, Long> {
    // 根据日期查询计划
    List<FeedPlan> findByDate(LocalDate date);

    // 根据日期范围查询计划
    List<FeedPlan> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // 根据配方ID查询计划
    List<FeedPlan> findByFormulaId(Long formulaId);

    // 根据配方ID和状态查询计划
    List<FeedPlan> findByFormulaIdAndStatusNot(Long formulaId, String status);
}
