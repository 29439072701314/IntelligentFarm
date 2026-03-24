package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.dto.FarmDTO;
import com.example.intelligentfarmcore.pojo.entity.Farm;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;

import java.util.Map;

public interface IFarmService {
    // 分页查询农场列表
    ResponseMessage<PageRes<FarmDTO>> getFarmList(PageReq pageReq);

    // 新增农场
    ResponseMessage<Farm> addFarm(Farm farm, Long userId);

    // 编辑农场
    ResponseMessage<Farm> editFarm(Farm farm);

    // 删除农场
    ResponseMessage<String> deleteFarm(Long farmId);

    // 获取农场详情
    ResponseMessage<Map<String, Object>> getFarmDetail(Long farmId);
}