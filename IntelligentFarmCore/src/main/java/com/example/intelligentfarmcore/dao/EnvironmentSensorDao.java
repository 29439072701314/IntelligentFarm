package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.EnvironmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvironmentSensorDao extends JpaRepository<EnvironmentData, Long> {
        // 查询数据列表（没有分页）
        @Query("SELECT d FROM EnvironmentData d WHERE " +
                        "(:deviceId IS NULL OR d.deviceId = :deviceId) " +
                        "AND d.time >= :minTime " +
                        "AND d.time <= :maxTime " +
                        "ORDER BY d.time ASC")
        List<EnvironmentData> findByConditions(
                        @Param("deviceId") Long deviceId,
                        @Param("minTime") long minTime,
                        @Param("maxTime") long maxTime);
}