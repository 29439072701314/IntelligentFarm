package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedFormulaDao extends JpaRepository<FeedFormula, Long> {
    // 根据名称模糊查询
    List<FeedFormula> findByNameContaining(String name);
}
