package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.dto.FeedPlanDTO;
import com.example.intelligentfarmcore.pojo.entity.FeedPlan;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;

public interface IFeedPlanService {
    // 分页查询计划列表
    ResponseMessage<PageRes<FeedPlanDTO>> getPlanList(PageReq pageReq);

    // 新增计划
    ResponseMessage<FeedPlan> addPlan(FeedPlan feedPlan);

    // 编辑计划
    ResponseMessage<FeedPlan> editPlan(FeedPlan feedPlan);

    // 删除计划
    ResponseMessage<String> deletePlan(Long id);

    // 更新状态
    ResponseMessage<FeedPlan> updateStatus(Long id, String status);

    // 执行计划
    ResponseMessage<FeedPlan> executePlan(Long id);
}
