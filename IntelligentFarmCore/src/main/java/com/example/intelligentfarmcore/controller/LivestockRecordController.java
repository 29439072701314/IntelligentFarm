package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.LivestockRecord;
import com.example.intelligentfarmcore.service.interfaces.ILivestockRecordService;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livestock/record")
public class LivestockRecordController {

    @Autowired
    private ILivestockRecordService livestockRecordService;

    // 添加入出库记录
    @PostMapping("/add")
    public ResponseMessage<LivestockRecord> addRecord(@RequestBody LivestockRecord record) {
        try {
            LivestockRecord result = livestockRecordService.addRecord(record);
            return ResponseMessage.success(result);
        } catch (Exception e) {
            return ResponseMessage.error("添加记录失败: " + e.getMessage());
        }
    }

    // 根据ID查询记录
    @GetMapping("/get/{id}")
    public ResponseMessage<LivestockRecord> getRecordById(@PathVariable Long id) {
        try {
            LivestockRecord record = livestockRecordService.getRecordById(id);
            return ResponseMessage.success(record);
        } catch (Exception e) {
            return ResponseMessage.error("查询记录失败: " + e.getMessage());
        }
    }

    // 根据农场ID查询记录
    @GetMapping("/getByFarm/{farmId}")
    public ResponseMessage<List<LivestockRecord>> getRecordsByFarmId(@PathVariable Long farmId) {
        try {
            List<LivestockRecord> records = livestockRecordService.getRecordsByFarmId(farmId);
            return ResponseMessage.success(records);
        } catch (Exception e) {
            return ResponseMessage.error("查询记录失败: " + e.getMessage());
        }
    }

    // 根据牲畜ID查询记录
    @GetMapping("/getByLivestock/{livestockId}")
    public ResponseMessage<List<LivestockRecord>> getRecordsByLivestockId(@PathVariable Long livestockId) {
        try {
            List<LivestockRecord> records = livestockRecordService.getRecordsByLivestockId(livestockId);
            return ResponseMessage.success(records);
        } catch (Exception e) {
            return ResponseMessage.error("查询记录失败: " + e.getMessage());
        }
    }

    // 获取所有记录
    @GetMapping("/getAll")
    public ResponseMessage<List<LivestockRecord>> getAllRecords() {
        try {
            List<LivestockRecord> records = livestockRecordService.getAllRecords();
            return ResponseMessage.success(records);
        } catch (Exception e) {
            return ResponseMessage.error("查询记录失败: " + e.getMessage());
        }
    }
}