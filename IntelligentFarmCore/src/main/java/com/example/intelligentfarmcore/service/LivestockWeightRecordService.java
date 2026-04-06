package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.LivestockWeightRecordDao;
import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.entity.LivestockWeightRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.interfaces.ILivestockWeightRecordService;
import com.example.intelligentfarmcore.service.interfaces.ILivestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LivestockWeightRecordService implements ILivestockWeightRecordService {

    @Autowired
    private LivestockWeightRecordDao livestockWeightRecordDao;

    @Autowired
    private ILivestockService livestockService;

    @Autowired
    private com.example.intelligentfarmcore.dao.LivestockDao livestockDao;

    @Override
    public ResponseMessage<?> addRecord(LivestockWeightRecord record) {
        try {
            // 设置记录时间
            record.setRecordTime(LocalDateTime.now());
            // 保存记录
            LivestockWeightRecord savedRecord = livestockWeightRecordDao.save(record);
            return ResponseMessage.success(savedRecord, "添加成功");
        } catch (Exception e) {
            return ResponseMessage.error("添加失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseMessage<List<LivestockWeightRecord>> getRecordsByLivestockId(Long livestockId) {
        try {
            List<LivestockWeightRecord> records = livestockWeightRecordDao.findByLivestockIdOrderByRecordTimeAsc(livestockId);
            return ResponseMessage.success(records);
        } catch (Exception e) {
            return ResponseMessage.error("获取记录失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseMessage<?> getAverageWeightByFarmId(Long farmId) {
        try {
            // 获取农场所有牲畜
            List<Livestock> livestockList = livestockService.getLivestockByFarmId(farmId);
            
            // 按日期分组计算平均体重
            Map<String, List<Double>> weightByDate = new HashMap<>();
            
            for (Livestock livestock : livestockList) {
                List<LivestockWeightRecord> records = livestockWeightRecordDao.findByLivestockIdOrderByRecordTimeAsc(livestock.getLivestockId());
                for (LivestockWeightRecord record : records) {
                    String date = record.getRecordTime().toLocalDate().toString();
                    weightByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(record.getWeight());
                }
            }
            
            // 计算每天的平均体重
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map.Entry<String, List<Double>> entry : weightByDate.entrySet()) {
                double avgWeight = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
                Map<String, Object> item = new HashMap<>();
                item.put("date", entry.getKey());
                item.put("averageWeight", avgWeight);
                result.add(item);
            }
            
            // 按日期排序
            result.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));
            
            return ResponseMessage.success(result);
        } catch (Exception e) {
            return ResponseMessage.error("获取平均体重失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseMessage<?> initializeWeightRecords() {
        try {
            // 获取所有牲畜
            List<Livestock> allLivestock = livestockDao.findAll();
            
            // 过滤出有体重值的牲畜
            List<Livestock> livestockWithWeight = allLivestock.stream()
                    .filter(livestock -> livestock.getWeight() != null)
                    .collect(java.util.stream.Collectors.toList());
            
            int createdCount = 0;
            for (Livestock livestock : livestockWithWeight) {
                // 检查是否已有体重记录
                List<LivestockWeightRecord> existingRecords = livestockWeightRecordDao.findByLivestockIdOrderByRecordTimeAsc(livestock.getLivestockId());
                if (existingRecords.isEmpty()) {
                    // 创建初始体重记录
                    LivestockWeightRecord record = new LivestockWeightRecord();
                    record.setLivestockId(livestock.getLivestockId());
                    record.setWeight(livestock.getWeight());
                    record.setRecordTime(LocalDateTime.now());
                    livestockWeightRecordDao.save(record);
                    createdCount++;
                }
            }
            
            return ResponseMessage.success(null, "初始化完成，创建了 " + createdCount + " 条体重记录");
        } catch (Exception e) {
            return ResponseMessage.error("初始化体重记录失败: " + e.getMessage());
        }
    }
}
