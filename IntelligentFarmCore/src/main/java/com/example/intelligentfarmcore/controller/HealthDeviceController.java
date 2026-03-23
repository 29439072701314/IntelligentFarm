package com.example.intelligentfarmcore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.intelligentfarmcore.pojo.dto.HealthDeviceDTO;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.interfaces.IHealthDeviceService;

@RestController
@RequestMapping("/healthDevice")
public class HealthDeviceController {
    @Autowired
    IHealthDeviceService healthDeviceService;

    @GetMapping("/getAllHealthDevice")
    public ResponseMessage<List<HealthDeviceDTO>> getAllHealthDevice() {
        return healthDeviceService.getAllHealthDevice();
    }
}
