package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.LivestockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivestockRecordDao extends JpaRepository<LivestockRecord, Long> {
    // 根据农场ID查询记录
    List<LivestockRecord> findByFarmId(Long farmId);

    // 根据牲畜ID查询记录
    List<LivestockRecord> findByLivestockId(Long livestockId);
}