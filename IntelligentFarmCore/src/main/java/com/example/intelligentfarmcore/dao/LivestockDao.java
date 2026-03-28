package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.Livestock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivestockDao extends JpaRepository<Livestock, Long> {
    // 根据农场ID查询牲畜
    List<Livestock> findByFarmId(Long farmId);

    // 根据名称模糊查询
    List<Livestock> findByLivestockNameContaining(String name);

    // 根据农场ID和名称模糊查询
    List<Livestock> findByFarmIdAndLivestockNameContaining(Long farmId, String name);
    
    // 根据牲畜编号查询
    List<Livestock> findByLivestockCode(String livestockCode);
    
    // 根据牲畜编号模糊查询
    List<Livestock> findByLivestockCodeContaining(String livestockCode);
    
    // 根据牲畜类型模糊查询
    List<Livestock> findByLivestockTypeContaining(String livestockType);
    
    // 根据健康状态查询
    List<Livestock> findByHealthStatus(String healthStatus);
    
    // 根据农场ID和多条件查询
    List<Livestock> findByFarmIdAndLivestockCodeContainingAndLivestockTypeContainingAndHealthStatus(
        Long farmId, String livestockCode, String livestockType, String healthStatus
    );
    
    // 多条件查询
    List<Livestock> findByLivestockCodeContainingAndLivestockTypeContainingAndHealthStatus(
        String livestockCode, String livestockType, String healthStatus
    );
    
    // 根据体重查询
    List<Livestock> findByWeight(Double weight);
    
    // 根据体重范围查询
    List<Livestock> findByWeightBetween(Double minWeight, Double maxWeight);
    
    // 根据入场时间范围查询
    List<Livestock> findByInTimeBetween(java.time.LocalDateTime startInTime, java.time.LocalDateTime endInTime);
}