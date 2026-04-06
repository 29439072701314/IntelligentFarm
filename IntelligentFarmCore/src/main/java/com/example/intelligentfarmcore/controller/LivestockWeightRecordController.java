package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.LivestockWeightRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.interfaces.ILivestockWeightRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/livestock/weight")
public class LivestockWeightRecordController {

    @Autowired
    private ILivestockWeightRecordService livestockWeightRecordService;

    // 添加体重记录
    @PostMapping
    public ResponseMessage<?> addRecord(@Validated @RequestBody LivestockWeightRecord record) {
        return livestockWeightRecordService.addRecord(record);
    }

    // 根据牲畜ID获取体重记录
    @GetMapping("/livestock/{id}")
    public ResponseMessage<?> getRecordsByLivestockId(@PathVariable("id") Long livestockId) {
        return livestockWeightRecordService.getRecordsByLivestockId(livestockId);
    }

    // 根据农场ID获取平均体重
    @GetMapping("/farm/{id}/average")
    public ResponseMessage<?> getAverageWeightByFarmId(@PathVariable("id") Long farmId) {
        return livestockWeightRecordService.getAverageWeightByFarmId(farmId);
    }

    // 初始化所有牲畜的体重记录
    @PostMapping("/initialize")
    public ResponseMessage<?> initializeWeightRecords() {
        return livestockWeightRecordService.initializeWeightRecords();
    }
}
