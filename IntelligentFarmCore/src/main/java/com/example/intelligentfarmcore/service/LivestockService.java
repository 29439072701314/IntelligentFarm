package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.LivestockDao;
import com.example.intelligentfarmcore.mapper.LivestockMapper;
import com.example.intelligentfarmcore.pojo.dto.LivestockDTO;
import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.entity.LivestockWeightRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.ILivestockService;
import com.example.intelligentfarmcore.service.interfaces.ILivestockRecordService;
import com.example.intelligentfarmcore.service.interfaces.ILivestockWeightRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
        // 手动转换，确保所有字段都正确设置
        List<LivestockDTO> livestockDTOs = pageLivestock.stream()
            .map(LivestockDTO::new)
            .toList();
        PageRes<LivestockDTO> pageRes = new PageRes<>(livestockDTOs, livestockList.size(), pageNumber, pageSize, totalPages);
        return ResponseMessage.success(pageRes);
    }

    @Transactional
    @Override
    public ResponseMessage<Livestock> addLivestock(Livestock livestock) {
        // 设置默认状态为在库
        livestock.setStatus(1);
        // 设置入库时间
        livestock.setInTime(java.time.LocalDateTime.now());
        // 生成牲畜编码 - 使用农场代码+序号格式
        if (livestock.getLivestockCode() == null || livestock.getLivestockCode().trim().isEmpty()) {
            // 从farmId生成前缀
            String prefix = "F" + livestock.getFarmId();
            // 查询该农场的最大序号
            List<Livestock> existingLivestock = livestockDao.findByFarmId(livestock.getFarmId());
            int maxSeq = 0;
            for (Livestock existing : existingLivestock) {
                String existingCode = existing.getLivestockCode();
                if (existingCode != null && existingCode.startsWith(prefix + "_S")) {
                    try {
                        String seqStr = existingCode.substring((prefix + "_S").length());
                        int seq = Integer.parseInt(seqStr);
                        if (seq > maxSeq) {
                            maxSeq = seq;
                        }
                    } catch (Exception e) {
                        // 忽略格式不正确的编码
                    }
                }
            }
            // 生成新序号
            int newSeq = maxSeq + 1;
            String code = prefix + "_S" + String.format("%03d", newSeq);
            livestock.setLivestockCode(code);
        }
        // 确保type和livestockType字段都设置，保持兼容性
        if (livestock.getLivestockType() != null && livestock.getType() == null) {
            livestock.setType(livestock.getLivestockType());
        }
        if (livestock.getType() != null && livestock.getLivestockType() == null) {
            livestock.setLivestockType(livestock.getType());
        }
        // 保存牲畜
        Livestock savedLivestock = livestockDao.save(livestock);
        
        // 强制刷新，确保从数据库重新获取
        livestockDao.flush();
        savedLivestock = livestockDao.findById(savedLivestock.getLivestockId()).orElse(null);
        
        // 检查健康状态，如果不健康，自动创建疾病记录
        checkHealthStatusAndCreateDiseaseRecord(savedLivestock);
        
        // 添加入库记录
        com.example.intelligentfarmcore.pojo.entity.LivestockRecord record = new com.example.intelligentfarmcore.pojo.entity.LivestockRecord();
        record.setFarmId(livestock.getFarmId());
        record.setLivestockId(savedLivestock.getLivestockId());
        record.setOperationType(1); // 1-入库
        record.setOperationTime(java.time.LocalDateTime.now());
        record.setOperator("系统");
        record.setRemark("初始入库");
        livestockRecordService.addRecord(record);
        
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
        
        // 保存旧的健康状态和体重
        String oldHealthStatus = existingLivestock.getHealthStatus();
        Double oldWeight = existingLivestock.getWeight();
        
        // 更新牲畜信息
        existingLivestock.setLivestockName(livestock.getLivestockName());
        existingLivestock.setLivestockType(livestock.getLivestockType());
        existingLivestock.setFarmId(livestock.getFarmId());
        existingLivestock.setHealthStatus(livestock.getHealthStatus());
        existingLivestock.setWeight(livestock.getWeight());
        
        Livestock updatedLivestock = livestockDao.save(existingLivestock);
        
        // 如果体重发生变化，创建体重记录
        if (oldWeight == null || !oldWeight.equals(updatedLivestock.getWeight())) {
            LivestockWeightRecord weightRecord = new LivestockWeightRecord();
            weightRecord.setLivestockId(updatedLivestock.getLivestockId());
            weightRecord.setWeight(updatedLivestock.getWeight());
            livestockWeightRecordService.addRecord(weightRecord);
        }
        
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
    
    @Autowired
    @Lazy
    private ILivestockWeightRecordService livestockWeightRecordService;

    @Autowired
    @Lazy
    private ILivestockRecordService livestockRecordService;

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

    @Override
    public java.util.List<Livestock> getLivestockByFarmId(Long farmId) {
        return livestockDao.findByFarmId(farmId);
    }

    @Transactional
    @Override
    public ResponseMessage<Livestock> inStock(Long livestockId, String operator, String remark) {
        // 检查牲畜是否存在
        Livestock livestock = livestockDao.findById(livestockId).orElse(null);
        if (livestock == null) {
            return ResponseMessage.error("牲畜不存在");
        }

        // 设置牲畜状态为在库
        livestock.setStatus(1);
        livestock.setInTime(java.time.LocalDateTime.now());
        livestock.setOutTime(null);
        Livestock updatedLivestock = livestockDao.save(livestock);

        // 添加入库记录
        com.example.intelligentfarmcore.pojo.entity.LivestockRecord record = new com.example.intelligentfarmcore.pojo.entity.LivestockRecord();
        record.setFarmId(livestock.getFarmId());
        record.setLivestockId(livestockId);
        record.setOperationType(1); // 1-入库
        record.setOperationTime(java.time.LocalDateTime.now());
        record.setOperator(operator);
        record.setRemark(remark);
        livestockRecordService.addRecord(record);

        return ResponseMessage.success(updatedLivestock, "入库成功");
    }

    @Transactional
    @Override
    public ResponseMessage<Livestock> outStock(Long livestockId, String operator, String remark) {
        // 检查牲畜是否存在
        Livestock livestock = livestockDao.findById(livestockId).orElse(null);
        if (livestock == null) {
            return ResponseMessage.error("牲畜不存在");
        }

        // 设置牲畜状态为已出库
        livestock.setStatus(2);
        livestock.setOutTime(java.time.LocalDateTime.now());
        Livestock updatedLivestock = livestockDao.save(livestock);

        // 添加出库记录
        com.example.intelligentfarmcore.pojo.entity.LivestockRecord record = new com.example.intelligentfarmcore.pojo.entity.LivestockRecord();
        record.setFarmId(livestock.getFarmId());
        record.setLivestockId(livestockId);
        record.setOperationType(2); // 2-出库
        record.setOperationTime(java.time.LocalDateTime.now());
        record.setOperator(operator);
        record.setRemark(remark);
        livestockRecordService.addRecord(record);

        return ResponseMessage.success(updatedLivestock, "出库成功");
    }
}
