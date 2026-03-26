package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.DiseaseRecordDao;
import com.example.intelligentfarmcore.dao.LivestockDao;
import com.example.intelligentfarmcore.mapper.DiseaseRecordMapper;
import com.example.intelligentfarmcore.pojo.dto.DiseaseRecordDTO;
import com.example.intelligentfarmcore.pojo.entity.DiseaseRecord;
import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IDiseaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiseaseRecordService implements IDiseaseRecordService {

    @Autowired
    private DiseaseRecordDao diseaseRecordDao;
    @Autowired
    private LivestockDao livestockDao;

    @Override
    public ResponseMessage<PageRes<DiseaseRecordDTO>> getDiseaseList(PageReq pageReq) {
        List<DiseaseRecord> diseaseRecords;
        
        // 如果有农场ID，根据农场ID查询
        if (pageReq.getCondition() != null && pageReq.getCondition().containsKey("farmId")) {
            Object farmIdObj = pageReq.getCondition().get("farmId");
            if (farmIdObj != null) {
                Long farmId = Long.valueOf(farmIdObj.toString());
                
                // 获取该农场下的所有牲畜
                List<Livestock> livestockList = livestockDao.findByFarmId(farmId);
                if (livestockList.isEmpty()) {
                    // 农场下没有牲畜，返回空列表
                    PageRes<DiseaseRecordDTO> pageRes = new PageRes<>(List.of(), 0);
                    return ResponseMessage.success(pageRes);
                }
                
                // 获取这些牲畜的编号列表
                List<String> livestockCodes = livestockList.stream()
                        .map(Livestock::getLivestockCode)
                        .filter(code -> code != null && !code.isEmpty())
                        .collect(Collectors.toList());
                
                if (livestockCodes.isEmpty()) {
                    // 没有有效的牲畜编号，返回空列表
                    PageRes<DiseaseRecordDTO> pageRes = new PageRes<>(List.of(), 0);
                    return ResponseMessage.success(pageRes);
                }
                
                // 根据牲畜编号查询疾病记录
                diseaseRecords = diseaseRecordDao.findByLivestockCodeIn(livestockCodes);
            } else {
                // 农场ID为null，查询所有
                diseaseRecords = diseaseRecordDao.findAll();
            }
        } else {
            // 否则查询所有
            diseaseRecords = diseaseRecordDao.findAll();
        }
        
        // 过滤掉已删除的记录
        List<DiseaseRecord> activeRecords = diseaseRecords.stream()
                .filter(record -> !Boolean.TRUE.equals(record.getDeleted()))
                .collect(Collectors.toList());
        
        // 应用其他查询条件
        if (pageReq.getCondition() != null) {
            // 状态查询
            if (pageReq.getCondition().containsKey("status")) {
                String status = (String) pageReq.getCondition().get("status");
                if (status != null && !status.isEmpty()) {
                    activeRecords = activeRecords.stream()
                            .filter(record -> status.equals(record.getStatus()))
                            .collect(Collectors.toList());
                }
            }
            
            // 疾病名称模糊查询
            if (pageReq.getCondition().containsKey("diseaseName")) {
                String diseaseName = (String) pageReq.getCondition().get("diseaseName");
                if (diseaseName != null && !diseaseName.isEmpty()) {
                    activeRecords = activeRecords.stream()
                            .filter(record -> record.getDiseaseName() != null && record.getDiseaseName().contains(diseaseName))
                            .collect(Collectors.toList());
                }
            }
            
            // 牲畜编号模糊查询
            if (pageReq.getCondition().containsKey("livestockCode")) {
                String livestockCode = (String) pageReq.getCondition().get("livestockCode");
                if (livestockCode != null && !livestockCode.isEmpty()) {
                    activeRecords = activeRecords.stream()
                            .filter(record -> record.getLivestockCode() != null && record.getLivestockCode().contains(livestockCode))
                            .collect(Collectors.toList());
                }
            }
            
            // 日期范围查询
            if (pageReq.getCondition().containsKey("startDate") && pageReq.getCondition().containsKey("endDate")) {
                String startDateStr = (String) pageReq.getCondition().get("startDate");
                String endDateStr = (String) pageReq.getCondition().get("endDate");
                if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
                    try {
                        java.time.LocalDate startDate = java.time.LocalDate.parse(startDateStr);
                        java.time.LocalDate endDate = java.time.LocalDate.parse(endDateStr);
                        activeRecords = activeRecords.stream()
                                .filter(record -> record.getOnsetDate() != null && 
                                        !record.getOnsetDate().isBefore(startDate) && 
                                        !record.getOnsetDate().isAfter(endDate))
                                .collect(Collectors.toList());
                    } catch (Exception e) {
                        // 日期解析失败，忽略日期条件
                        e.printStackTrace();
                    }
                }
            }
        }
        
        // 手动分页
        int start = (pageReq.getPageNumber() - 1) * pageReq.getPageSize();
        int end = Math.min(start + pageReq.getPageSize(), activeRecords.size());
        List<DiseaseRecord> pageRecords = start < end ? activeRecords.subList(start, end) : List.of();
        
        List<DiseaseRecordDTO> diseaseRecordDTOs = DiseaseRecordMapper.INSTANCE.toDiseaseRecordDTOList(pageRecords);
        PageRes<DiseaseRecordDTO> pageRes = new PageRes<>(diseaseRecordDTOs, activeRecords.size());
        return ResponseMessage.success(pageRes);
    }

    @Override
    public ResponseMessage<Map<String, Long>> getStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        statistics.put("total", diseaseRecordDao.countByDeletedFalse());
        statistics.put("treating", diseaseRecordDao.countByStatusAndDeletedFalse("治疗中"));
        statistics.put("recovered", diseaseRecordDao.countByStatusAndDeletedFalse("已康复"));
        return ResponseMessage.success(statistics);
    }
    
    // 根据农场ID获取统计数据
    public ResponseMessage<Map<String, Long>> getStatisticsByFarmId(Long farmId) {
        Map<String, Long> statistics = new HashMap<>();
        
        // 获取该农场下的所有牲畜
        List<Livestock> livestockList = livestockDao.findByFarmId(farmId);
        if (livestockList.isEmpty()) {
            // 农场下没有牲畜，返回零统计
            statistics.put("total", 0L);
            statistics.put("treating", 0L);
            statistics.put("recovered", 0L);
            return ResponseMessage.success(statistics);
        }
        
        // 获取这些牲畜的编号列表
        List<String> livestockCodes = livestockList.stream()
                .map(Livestock::getLivestockCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toList());
        
        if (livestockCodes.isEmpty()) {
            // 没有有效的牲畜编号，返回零统计
            statistics.put("total", 0L);
            statistics.put("treating", 0L);
            statistics.put("recovered", 0L);
            return ResponseMessage.success(statistics);
        }
        
        // 查询这些牲畜的疾病记录
        List<DiseaseRecord> diseaseRecords = diseaseRecordDao.findByLivestockCodeIn(livestockCodes);
        
        // 过滤掉已删除的记录
        List<DiseaseRecord> activeRecords = diseaseRecords.stream()
                .filter(record -> !Boolean.TRUE.equals(record.getDeleted()))
                .collect(Collectors.toList());
        
        // 统计数据
        long total = activeRecords.size();
        long treating = activeRecords.stream().filter(record -> "治疗中".equals(record.getStatus())).count();
        long recovered = activeRecords.stream().filter(record -> "已康复".equals(record.getStatus())).count();
        
        statistics.put("total", total);
        statistics.put("treating", treating);
        statistics.put("recovered", recovered);
        return ResponseMessage.success(statistics);
    }

    @Override
    public ResponseMessage<DiseaseRecord> addDiseaseRecord(DiseaseRecord diseaseRecord) {
        // 设置默认状态为治疗中
        diseaseRecord.setStatus("治疗中");
        diseaseRecord.setDeleted(false);
        DiseaseRecord savedRecord = diseaseRecordDao.save(diseaseRecord);
        return ResponseMessage.success(savedRecord, "新增疾病记录成功");
    }

    @Override
    public ResponseMessage<DiseaseRecord> editDiseaseRecord(DiseaseRecord diseaseRecord) {
        // 检查记录是否存在
        DiseaseRecord existingRecord = diseaseRecordDao.findById(diseaseRecord.getId()).orElse(null);
        if (existingRecord == null) {
            return ResponseMessage.error("疾病记录不存在");
        }
        
        // 已康复的记录不允许编辑
        if ("已康复".equals(existingRecord.getStatus())) {
            return ResponseMessage.error("已康复的记录不允许编辑");
        }
        
        // 更新记录信息
        existingRecord.setDiseaseName(diseaseRecord.getDiseaseName());
        existingRecord.setLivestockCode(diseaseRecord.getLivestockCode());
        existingRecord.setOnsetDate(diseaseRecord.getOnsetDate());
        existingRecord.setSymptoms(diseaseRecord.getSymptoms());
        existingRecord.setTreatment(diseaseRecord.getTreatment());
        
        DiseaseRecord updatedRecord = diseaseRecordDao.save(existingRecord);
        return ResponseMessage.success(updatedRecord, "编辑疾病记录成功");
    }

    @Transactional
    @Override
    public ResponseMessage<DiseaseRecord> recoverDiseaseRecord(Long id) {
        // 检查记录是否存在
        DiseaseRecord diseaseRecord = diseaseRecordDao.findById(id).orElse(null);
        if (diseaseRecord == null) {
            return ResponseMessage.error("疾病记录不存在");
        }
        
        // 检查是否已经是康复状态
        if ("已康复".equals(diseaseRecord.getStatus())) {
            return ResponseMessage.error("该记录已经是康复状态");
        }
        
        // 更新状态为已康复
        diseaseRecord.setStatus("已康复");
        DiseaseRecord updatedRecord = diseaseRecordDao.save(diseaseRecord);
        
        return ResponseMessage.success(updatedRecord, "标记康复成功");
    }
    
    // 自动创建疾病记录（供健康监测调用）
    @Transactional
    public DiseaseRecord autoCreateDiseaseRecord(String livestockCode, String diseaseName, String symptoms) {
        // 检查是否已存在该牲畜的未康复疾病记录
        List<DiseaseRecord> existingRecords = diseaseRecordDao.findByLivestockCodeContainingAndDeletedFalse(livestockCode);
        boolean hasActiveDisease = existingRecords.stream()
                .anyMatch(record -> "治疗中".equals(record.getStatus()));
        
        if (hasActiveDisease) {
            // 已存在治疗中的记录，不再创建
            return null;
        }
        
        DiseaseRecord diseaseRecord = new DiseaseRecord();
        diseaseRecord.setLivestockCode(livestockCode);
        diseaseRecord.setDiseaseName(diseaseName);
        diseaseRecord.setSymptoms(symptoms);
        diseaseRecord.setTreatment("自动检测生成，请尽快处理");
        diseaseRecord.setStatus("治疗中");
        diseaseRecord.setDeleted(false);
        diseaseRecord.setOnsetDate(java.time.LocalDate.now());
        
        return diseaseRecordDao.save(diseaseRecord);
    }
}
