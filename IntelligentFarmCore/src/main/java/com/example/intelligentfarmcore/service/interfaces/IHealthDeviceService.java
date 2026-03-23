package com.example.intelligentfarmcore.service.interfaces;

import java.util.List;

import com.example.intelligentfarmcore.pojo.dto.HealthDeviceDTO;
import com.example.intelligentfarmcore.pojo.entity.HealthDevice;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;

public interface IHealthDeviceService {
    HealthDevice updateHealthDevice(HealthDevice healthDevice);

    /**
     * 获取所有健康设备
     * 
     * @return 所有健康设备列表
     */
    ResponseMessage<List<HealthDeviceDTO>> getAllHealthDevice();
}
