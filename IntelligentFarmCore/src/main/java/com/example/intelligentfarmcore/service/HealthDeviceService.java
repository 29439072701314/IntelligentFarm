package com.example.intelligentfarmcore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.intelligentfarmcore.dao.HealthDeviceDao;
import com.example.intelligentfarmcore.mapper.HealthDeviceMapper;
import com.example.intelligentfarmcore.pojo.dto.HealthDeviceDTO;
import com.example.intelligentfarmcore.pojo.entity.HealthDevice;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.interfaces.IHealthDeviceService;

@Service
public class HealthDeviceService implements IHealthDeviceService {
    @Autowired
    private HealthDeviceDao healthDeviceDao;
    @Autowired
    private HealthDeviceMapper healthDeviceMapper;

    @Override
    public HealthDevice updateHealthDevice(HealthDevice healthDevice) {
        // 检查设备是否已存在
        boolean exists = healthDeviceDao.existsHealthDeviceByDeviceName(healthDevice.getDeviceName());
        HealthDevice device = null;
        if (exists) {
            // 更新设备数据
            HealthDevice existingDevice = healthDeviceDao.findHealthDeviceByDeviceName(healthDevice.getDeviceName());
            existingDevice.setHealthDeviceData(healthDevice);
            device = healthDeviceDao.save(existingDevice);
        } else {
            // 添加设备
            device = healthDeviceDao.save(healthDevice);
        }
        return device;
    }

    @Override
    public ResponseMessage<List<HealthDeviceDTO>> getAllHealthDevice() {
        List<HealthDevice> healthDevices = healthDeviceDao.findAll();
        List<HealthDeviceDTO> healthDeviceDTOs = healthDeviceMapper.toDTOList(healthDevices);
        return ResponseMessage.success(healthDeviceDTOs);
    }
}
