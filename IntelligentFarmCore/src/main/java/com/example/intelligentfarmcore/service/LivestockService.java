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

        // 如果有农场ID，根据农场ID查询
        if (pageReq.getCondition() != null && pageReq.getCondition().containsKey("farmId")) {
            Long farmId = Long.valueOf(pageReq.getCondition().get("farmId").toString());
            List<Livestock> livestockList;
            
            // 如果有搜索条件，根据农场ID和名称模糊查询
            if (pageReq.getCondition().containsKey("livestockName")) {
                String name = (String) pageReq.getCondition().get("livestockName");
                livestockList = livestockDao.findByFarmIdAndLivestockNameContaining(farmId, name);
            } else {
                livestockList = livestockDao.findByFarmId(farmId);
            }
            
            // 手动分页
            int start = (pageReq.getPageNumber() - 1) * pageReq.getPageSize();
            int end = Math.min(start + pageReq.getPageSize(), livestockList.size());
            List<Livestock> pageLivestock = livestockList.subList(start, end);
            List<LivestockDTO> livestockDTOs = LivestockMapper.INSTANCE.toLivestockDTOList(pageLivestock);

            PageRes<LivestockDTO> pageRes = new PageRes<>(livestockDTOs, livestockList.size());
            return ResponseMessage.success(pageRes);
        } else {
            // 否则查询所有
            livestockPage = livestockDao.findAll(pageable);
            List<LivestockDTO> livestockDTOs = LivestockMapper.INSTANCE.toLivestockDTOList(livestockPage.getContent());
            PageRes<LivestockDTO> pageRes = new PageRes<>(livestockDTOs, (int) livestockPage.getTotalElements());
            return ResponseMessage.success(pageRes);
        }
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
        
        // 如果健康状态从健康变为不健康，自动创建疾病记录
        if (!"健康".equals(oldHealthStatus) && !"健康".equals(updatedLivestock.getHealthStatus())) {
            checkHealthStatusAndCreateDiseaseRecord(updatedLivestock);
        }
        
        return ResponseMessage.success(updatedLivestock, "编辑牲畜成功");
    }

    @Override
    public ResponseMessage<String> deleteLivestock(Long livestockId) {
        // 检查牲畜是否存在
        Livestock livestock = livestockDao.findById(livestockId).orElse(null);
        if (livestock == null) {
            return ResponseMessage.error("牲畜不存在");
        }
        // 删除牲畜
        livestockDao.deleteById(livestockId);
        return ResponseMessage.success("删除牲畜成功");
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
                break;
            case "治疗中":
                diseaseRecordService.autoCreateDiseaseRecord(
                    livestockCode, 
                    "治疗中", 
                    "牲畜正在治疗中"
                );
                break;
            case "亚健康":
                diseaseRecordService.autoCreateDiseaseRecord(
                    livestockCode, 
                    "亚健康", 
                    "牲畜处于亚健康状态，需要关注"
                );
                break;
            // 健康状态不需要创建疾病记录
            case "健康":
            default:
                break;
        }
    }
}
