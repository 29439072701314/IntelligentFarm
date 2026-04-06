package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.entity.LivestockWeightRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;

import java.util.List;

public interface ILivestockWeightRecordService {
    // 添加体重记录
    ResponseMessage<?> addRecord(LivestockWeightRecord record);

    // 根据牲畜ID获取体重记录列表
    ResponseMessage<List<LivestockWeightRecord>> getRecordsByLivestockId(Long livestockId);

    // 获取农场所有牲畜的平均体重记录
    ResponseMessage<?> getAverageWeightByFarmId(Long farmId);

    // 初始化所有牲畜的体重记录
    ResponseMessage<?> initializeWeightRecords();
}
