package com.example.intelligentfarmcore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.intelligentfarmcore.pojo.entity.HealthDevice;

public interface HealthDeviceDao extends JpaRepository<HealthDevice, Long> {
    boolean existsHealthDeviceByDeviceName(String deviceName);

    HealthDevice findHealthDeviceByDeviceName(String deviceName);
}