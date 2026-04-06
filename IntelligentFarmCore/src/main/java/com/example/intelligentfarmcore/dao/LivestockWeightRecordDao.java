package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.LivestockWeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivestockWeightRecordDao extends JpaRepository<LivestockWeightRecord, Long> {
    // 根据牲畜ID查询体重记录
    List<LivestockWeightRecord> findByLivestockIdOrderByRecordTimeAsc(Long livestockId);
}
