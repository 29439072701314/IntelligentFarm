package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.entity.WeightDevice;

public interface IWeightDeviceService {
    Long updateWeightDeviceData(WeightDevice weightDevice);
    WeightDevice getWeightDeviceByDeviceName(String deviceName);
}
