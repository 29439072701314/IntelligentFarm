package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.dto.DeviceDTO;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController // 接口方法返回对象 ，默认返回JSON格式
@RequestMapping("/device") // localhost:8080/room/**
public class DeviceController {
    @Autowired
    IDeviceService deviceService;

    @PostMapping("/list")
    public ResponseMessage<PageRes<DeviceDTO>> getDeviceList(@RequestBody PageReq pageReq) {
        return deviceService.getDeviceList(pageReq);
    }

    @GetMapping("/getAllDevice")
    public ResponseMessage<List<DeviceDTO>> getAllDevice() {
        return deviceService.getAllDevice();
    }

}