package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.FeedStockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedStockRecordDao extends JpaRepository<FeedStockRecord, Long> {
    // 根据饲料配方ID查询入库记录
    List<FeedStockRecord> findByFormulaId(Long formulaId);
}