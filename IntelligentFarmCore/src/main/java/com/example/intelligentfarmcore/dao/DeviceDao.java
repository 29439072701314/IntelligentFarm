package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceDao extends JpaRepository<Device, Long> {
        boolean existsDeviceByDeviceName(String deviceName);

        // 分页查询设备列表
        @Query("SELECT d FROM Device d WHERE " +
                        "(:deviceName IS NULL OR d.deviceName LIKE %:deviceName%)")
        Page<Device> findByConditions(
                        @Param("deviceName") String deviceName,
                        Pageable pageable);

        Device findDeviceByDeviceName(String deviceName);
}
