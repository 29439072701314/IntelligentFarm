package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceDao extends JpaRepository<Device, Long> {
        boolean existsDeviceByDeviceName(String deviceName);

        // 分页查询设备列表
        @Query("SELECT d FROM Device d WHERE " +
                        "(:deviceName IS NULL OR d.deviceName LIKE %:deviceName%)")
        Page<Device> findByConditions(
                        @Param("deviceName") String deviceName,
                        Pageable pageable);

        // 根据农场ID分页查询设备
        @Query("SELECT d FROM Device d WHERE d.farmId = :farmId")
        Page<Device> findByFarmId(
                        @Param("farmId") Long farmId,
                        Pageable pageable);

        // 根据农场ID查询所有设备
        List<Device> findByFarmId(Long farmId);

        Device findDeviceByDeviceName(String deviceName);
}
