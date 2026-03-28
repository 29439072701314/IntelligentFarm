package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.dto.LivestockDTO;
import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;

public interface ILivestockService {
    // 分页查询牲畜列表
    ResponseMessage<PageRes<LivestockDTO>> getLivestockList(PageReq pageReq);

    // 新增牲畜
    ResponseMessage<Livestock> addLivestock(Livestock livestock);

    // 编辑牲畜
    ResponseMessage<Livestock> editLivestock(Livestock livestock);



    // 获取牲畜详情
    ResponseMessage<Livestock> getLivestockDetail(Long livestockId);
}