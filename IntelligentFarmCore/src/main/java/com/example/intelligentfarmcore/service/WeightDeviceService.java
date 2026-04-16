package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.WeightDeviceDao;
import com.example.intelligentfarmcore.pojo.entity.WeightDevice;
import com.example.intelligentfarmcore.service.interfaces.IWeightDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeightDeviceService implements IWeightDeviceService {
    @Autowired
    private WeightDeviceDao weightDeviceDao;

    @Override
    public Long updateWeightDeviceData(WeightDevice weightDevice) {
        // 检查设备是否已存在
        boolean exists = weightDeviceDao.existsWeightDeviceByDeviceName(weightDevice.getDeviceName());
        Long deviceId = null;
        if (exists) {
            // 更新设备数据
            WeightDevice existingWeightDevice = weightDeviceDao.findWeightDeviceByDeviceName(weightDevice.getDeviceName());
            existingWeightDevice.setDeviceData(weightDevice);
            deviceId = weightDeviceDao.save(existingWeightDevice).getId();
        } else {
            // 添加设备
            deviceId = weightDeviceDao.save(weightDevice).getId();
        }
        return deviceId;
    }

    @Override
    public WeightDevice getWeightDeviceByDeviceName(String deviceName) {
        return weightDeviceDao.findWeightDeviceByDeviceName(deviceName);
    }
}
