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
}