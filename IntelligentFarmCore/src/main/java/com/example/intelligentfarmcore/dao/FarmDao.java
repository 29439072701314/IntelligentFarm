package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmDao extends JpaRepository<Farm, Long> {
    // 根据用户ID查询农场
    List<Farm> findByUserId(Long userId);

    // 根据名称模糊查询
    @Query("SELECT f FROM Farm f WHERE f.farmName LIKE %:name%")
    List<Farm> findByFarmNameContaining(@Param("name") String name);

    // 检查农场是否有关联的牲畜
    @Query("SELECT COUNT(l) FROM Livestock l WHERE l.farmId = :farmId")
    long countLivestockByFarmId(@Param("farmId") Long farmId);

    // 检查农场是否有关联的设备
    @Query("SELECT COUNT(d) FROM Device d WHERE d.farmId = :farmId")
    long countDevicesByFarmId(@Param("farmId") Long farmId);
}