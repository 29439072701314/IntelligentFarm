package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.FarmDao;
import com.example.intelligentfarmcore.mapper.FarmMapper;
import com.example.intelligentfarmcore.pojo.dto.FarmDTO;
import com.example.intelligentfarmcore.pojo.entity.Farm;

import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IFarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FarmService implements IFarmService {

    @Autowired
    private FarmDao farmDao;

    @Override
    public ResponseMessage<PageRes<FarmDTO>> getFarmList(PageReq pageReq) {
        Pageable pageable = PageRequest.of(pageReq.getPageNumber() - 1, pageReq.getPageSize());
        Page<Farm> farmPage;

        // 如果有搜索条件，根据名称模糊查询
        if (pageReq.getCondition() != null && pageReq.getCondition().containsKey("name")) {
            String name = (String) pageReq.getCondition().get("name");
            List<Farm> farms = farmDao.findByFarmNameContaining(name);
            // 手动分页
            int start = (pageReq.getPageNumber() - 1) * pageReq.getPageSize();
            int end = Math.min(start + pageReq.getPageSize(), farms.size());
            List<Farm> pageFarms = farms.subList(start, end);
            List<FarmDTO> farmDTOs = FarmMapper.INSTANCE.toFarmDTOList(pageFarms);
            int totalPages = (farms.size() + pageReq.getPageSize() - 1) / pageReq.getPageSize();
            PageRes<FarmDTO> pageRes = new PageRes<>((long) farms.size(), totalPages, pageReq.getPageNumber(), pageReq.getPageSize(), farmDTOs);
            return ResponseMessage.success(pageRes);
        } else {
            farmPage = farmDao.findAll(pageable);
            List<FarmDTO> farmDTOs = FarmMapper.INSTANCE.toFarmDTOList(farmPage.getContent());
            PageRes<FarmDTO> pageRes = new PageRes<>(farmPage, farmDTOs);
            return ResponseMessage.success(pageRes);
        }
    }

    @Override
    public ResponseMessage<Farm> addFarm(Farm farm, Long userId) {
        farm.setUserId(userId);
        Farm savedFarm = farmDao.save(farm);
        return ResponseMessage.success(savedFarm, "新增农场成功");
    }

    @Override
    public ResponseMessage<Farm> editFarm(Farm farm) {
        // 检查农场是否存在
        Farm existingFarm = farmDao.findById(farm.getFarmId()).orElse(null);
        if (existingFarm == null) {
            return ResponseMessage.error("农场不存在");
        }
        // 更新农场信息
        existingFarm.setFarmName(farm.getFarmName());
        existingFarm.setAddress(farm.getAddress());
        Farm updatedFarm = farmDao.save(existingFarm);
        return ResponseMessage.success(updatedFarm, "编辑农场成功");
    }

    @Override
    public ResponseMessage<String> deleteFarm(Long farmId) {
        // 检查农场是否存在
        Farm farm = farmDao.findById(farmId).orElse(null);
        if (farm == null) {
            return ResponseMessage.error("农场不存在");
        }
        // 检查是否有关联的牲畜
        long livestockCount = farmDao.countLivestockByFarmId(farmId);
        if (livestockCount > 0) {
            return ResponseMessage.error("该农场有关联的牲畜，无法删除");
        }
        // 检查是否有关联的设备
        long deviceCount = farmDao.countDevicesByFarmId(farmId);
        if (deviceCount > 0) {
            return ResponseMessage.error("该农场有关联的设备，无法删除");
        }
        // 删除农场
        farmDao.deleteById(farmId);
        return ResponseMessage.success("删除农场成功");
    }

    @Override
    public ResponseMessage<Map<String, Object>> getFarmDetail(Long farmId) {
        // 检查农场是否存在
        Farm farm = farmDao.findById(farmId).orElse(null);
        if (farm == null) {
            return ResponseMessage.error("农场不存在");
        }
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("farm", farm);
        result.put("livestockList", farm.getLivestockList());
        result.put("deviceList", farm.getDeviceList());
        return ResponseMessage.success(result);
    }
}