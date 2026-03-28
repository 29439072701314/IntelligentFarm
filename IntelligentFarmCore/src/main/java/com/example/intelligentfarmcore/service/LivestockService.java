package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.LivestockDao;
import com.example.intelligentfarmcore.mapper.LivestockMapper;
import com.example.intelligentfarmcore.pojo.dto.LivestockDTO;
import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.ILivestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LivestockService implements ILivestockService {

    @Autowired
    private LivestockDao livestockDao;
    @Autowired
    private DiseaseRecordService diseaseRecordService;

    @Override
    public ResponseMessage<PageRes<LivestockDTO>> getLivestockList(PageReq pageReq) {
        Pageable pageable = PageRequest.of(pageReq.getPageNumber() - 1, pageReq.getPageSize());
        Page<Livestock> livestockPage;
        List<Livestock> livestockList;

        // 获取查询条件
        Map<String, Object> condition = pageReq.getCondition();
        String livestockCode = condition != null ? (String) condition.get("livestockCode") : null;
        String livestockType = condition != null ? (String) condition.get("type") : null;
        String healthStatus = condition != null ? (String) condition.get("healthStatus") : null;
        Double weight = condition != null && condition.get("weight") != null ? 
            Double.valueOf(condition.get("weight").toString()) : null;

        // 先获取所有符合条件的牲畜
        if (condition != null && condition.containsKey("farmId")) {
            Long farmId = Long.valueOf(condition.get("farmId").toString());
            livestockList = livestockDao.findByFarmId(farmId);
        } else {
            livestockList = livestockDao.findAll();
        }
        
        // 过滤条件
        if (livestockCode != null) {
            livestockList = livestockList.stream()
                .filter(livestock -> livestock.getLivestockCode().contains(livestockCode))
                .toList();
        }
        
        if (livestockType != null) {
            livestockList = livestockList.stream()
                .filter(livestock -> livestock.getLivestockType() != null && livestock.getLivestockType().contains(livestockType))
                .toList();
        }
        
        if (healthStatus != null) {
            livestockList = livestockList.stream()
                .filter(livestock -> livestock.getHealthStatus() != null && livestock.getHealthStatus().contains(healthStatus))
                .toList();
        }
        
        if (weight != null) {
            livestockList = livestockList.stream()
                .filter(livestock -> livestock.getWeight() != null && livestock.getWeight().equals(weight))
                .toList();
        }
        
        // 手动分页
        List<Livestock> pageLivestock = new java.util.ArrayList<>();
        int totalPages = 0;
        int pageNumber = pageReq.getPageNumber() != null && pageReq.getPageNumber() > 0 ? pageReq.getPageNumber() : 1;
        int pageSize = pageReq.getPageSize() != null && pageReq.getPageSize() > 0 ? pageReq.getPageSize() : 10;
        
        if (!livestockList.isEmpty()) {
            int start = (pageNumber - 1) * pageSize;
            int end = Math.min(start + pageSize, livestockList.size());
            if (start < livestockList.size() && start >= 0 && end > start) {
                pageLivestock = livestockList.subList(start, end);
            }
            totalPages = (livestockList.size() + pageSize - 1) / pageSize;
        }
        List<LivestockDTO> livestockDTOs = LivestockMapper.INSTANCE.toLivestockDTOList(pageLivestock);
        PageRes<LivestockDTO> pageRes = new PageRes<>(livestockDTOs, livestockList.size());
        return ResponseMessage.success(pageRes);
    }

    @Transactional
    @Override
    public ResponseMessage<Livestock> addLivestock(Livestock livestock) {
        // 保存牲畜
        Livestock savedLivestock = livestockDao.save(livestock);
        
        // 检查健康状态，如果不健康，自动创建疾病记录
        checkHealthStatusAndCreateDiseaseRecord(savedLivestock);
        
        return ResponseMessage.success(savedLivestock, "新增牲畜成功");
    }

    @Transactional
    @Override
    public ResponseMessage<Livestock> editLivestock(Livestock livestock) {
        // 检查牲畜是否存在
        Livestock existingLivestock = livestockDao.findById(livestock.getLivestockId()).orElse(null);
        if (existingLivestock == null) {
            return ResponseMessage.error("牲畜不存在");
        }
        
        // 保存旧的健康状态
        String oldHealthStatus = existingLivestock.getHealthStatus();
        
        // 更新牲畜信息
        existingLivestock.setLivestockName(livestock.getLivestockName());
        existingLivestock.setLivestockType(livestock.getLivestockType());
        existingLivestock.setFarmId(livestock.getFarmId());
        existingLivestock.setHealthStatus(livestock.getHealthStatus());
        
        Livestock updatedLivestock = livestockDao.save(existingLivestock);
        
        // 如果健康状态变为不健康，自动创建疾病记录
        if (!"健康".equals(updatedLivestock.getHealthStatus())) {
            checkHealthStatusAndCreateDiseaseRecord(updatedLivestock);
        } else if ("健康".equals(updatedLivestock.getHealthStatus()) && !"健康".equals(oldHealthStatus)) {
            // 如果健康状态从不健康变为健康，消除告警
            warningService.eliminateLivestockWarning(updatedLivestock.getLivestockCode());
        }
        
        return ResponseMessage.success(updatedLivestock, "编辑牲畜成功");
    }



    @Override
    public ResponseMessage<Livestock> getLivestockDetail(Long livestockId) {
        // 检查牲畜是否存在
        Livestock livestock = livestockDao.findById(livestockId).orElse(null);
        if (livestock == null) {
            return ResponseMessage.error("牲畜不存在");
        }
        return ResponseMessage.success(livestock);
    }
    
    @Autowired
    private WarningService warningService;

    // 检查健康状态并自动创建疾病记录
    private void checkHealthStatusAndCreateDiseaseRecord(Livestock livestock) {
        String healthStatus = livestock.getHealthStatus();
        if (healthStatus == null) {
            return;
        }
        
        String livestockCode = livestock.getLivestockCode();
        if (livestockCode == null || livestockCode.isEmpty()) {
            return;
        }
        
        // 根据健康状态创建相应的疾病记录
        switch (healthStatus) {
            case "患病":
                diseaseRecordService.autoCreateDiseaseRecord(
                    livestockCode, 
                    "健康异常", 
                    "牲畜健康状态为患病，需要检查具体症状"
                );
                // 生成牲畜健康告警
                warningService.generateWarning("牲畜", livestockCode, "牲畜健康状态为患病", "高");
                break;
            case "治疗中":
                diseaseRecordService.autoCreateDiseaseRecord(
                    livestockCode, 
                    "治疗中", 
                    "牲畜正在治疗中"
                );
                // 生成牲畜健康告警
                warningService.generateWarning("牲畜", livestockCode, "牲畜正在治疗中", "中");
                break;
            case "亚健康":
                diseaseRecordService.autoCreateDiseaseRecord(
                    livestockCode, 
                    "亚健康", 
                    "牲畜处于亚健康状态，需要关注"
                );
                // 生成牲畜健康告警
                warningService.generateWarning("牲畜", livestockCode, "牲畜处于亚健康状态", "中");
                break;
            // 健康状态不需要创建疾病记录，但需要消除告警
            case "健康":
                // 健康状态恢复，消除告警
                warningService.eliminateLivestockWarning(livestockCode);
                break;
            default:
                break;
        }
    }
}
