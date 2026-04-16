package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.WeightDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightDeviceDao extends JpaRepository<WeightDevice, Long> {
    boolean existsWeightDeviceByDeviceName(String deviceName);
    WeightDevice findWeightDeviceByDeviceName(String deviceName);
}
