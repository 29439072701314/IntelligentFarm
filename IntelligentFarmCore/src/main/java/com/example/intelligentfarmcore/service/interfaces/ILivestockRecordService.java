package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.entity.LivestockRecord;

import java.util.List;

public interface ILivestockRecordService {
    // 添加入出库记录
    LivestockRecord addRecord(LivestockRecord record);

    // 根据ID查询记录
    LivestockRecord getRecordById(Long id);

    // 根据农场ID查询记录
    List<LivestockRecord> getRecordsByFarmId(Long farmId);

    // 根据牲畜ID查询记录
    List<LivestockRecord> getRecordsByLivestockId(Long livestockId);

    // 获取所有记录
    List<LivestockRecord> getAllRecords();
}