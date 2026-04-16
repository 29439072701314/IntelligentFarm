package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.WeightDevice;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.interfaces.IWeightDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/weightDevice")
public class WeightDeviceController {
    @Autowired
    private IWeightDeviceService weightDeviceService;

    // 根据设备名称获取重量传感器数据
    @GetMapping("/getByDeviceName")
    public ResponseMessage<WeightDevice> getWeightDeviceByDeviceName(@RequestParam String deviceName) {
        WeightDevice weightDevice = weightDeviceService.getWeightDeviceByDeviceName(deviceName);
        if (weightDevice != null) {
            return ResponseMessage.success(weightDevice);
        } else {
            return ResponseMessage.error("未找到设备数据");
        }
    }
}
