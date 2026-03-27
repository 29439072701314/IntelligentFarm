package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.dto.DeviceDTO;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private IDeviceService deviceService;

    // 获取设备列表
    @PostMapping("/list")
    public ResponseMessage<PageRes<DeviceDTO>> getDeviceList(@RequestBody PageReq pageReq) {
        return deviceService.getDeviceList(pageReq);
    }

    // 获取所有设备
    @GetMapping("/getAllDevice")
    public ResponseMessage<List<DeviceDTO>> getAllDevice() {
        return deviceService.getAllDevice();
    }

    // 绑定设备到农场
    @PostMapping("/bindFarm")
    public ResponseMessage<String> bindDeviceToFarm(@RequestBody Map<String, Object> params) {
        Long deviceId = Long.parseLong(params.get("deviceId").toString());
        Long farmId = Long.parseLong(params.get("farmId").toString());
        return deviceService.bindDeviceToFarm(deviceId, farmId);
    }

    // 解绑设备与农场
    @PostMapping("/unbindFarm")
    public ResponseMessage<String> unbindDeviceFromFarm(@RequestBody Map<String, Object> params) {
        Long deviceId = Long.parseLong(params.get("deviceId").toString());
        return deviceService.unbindDeviceFromFarm(deviceId);
    }

    // 根据农场ID获取设备列表
    @GetMapping("/getByFarmId")
    public ResponseMessage<List<DeviceDTO>> getDevicesByFarmId(@RequestParam Long farmId) {
        return deviceService.getDevicesByFarmId(farmId);
    }
}
