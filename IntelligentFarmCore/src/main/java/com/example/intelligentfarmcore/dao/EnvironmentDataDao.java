package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.EnvironmentData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvironmentDataDao extends JpaRepository<EnvironmentData, Long> {
    // 根据设备ID查询环境数据
    List<EnvironmentData> findByDeviceId(Long deviceId);

    // 根据设备ID和时间范围查询环境数据
    List<EnvironmentData> findByDeviceIdAndTimeBetween(Long deviceId, Long startTime, Long endTime);

    // 根据农场ID查询环境数据（通过设备ID关联）
    List<EnvironmentData> findByDeviceIdIn(List<Long> deviceIds);
}
