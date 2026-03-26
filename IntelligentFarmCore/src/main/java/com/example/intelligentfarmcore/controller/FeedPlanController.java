package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.FeedPlan;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.service.interfaces.IFeedPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/feed/plan")
public class FeedPlanController {

    @Autowired
    private IFeedPlanService feedPlanService;

    // 分页查询计划列表
    @GetMapping("/list")
    public ResponseMessage<?> getPlanList(PageReq pageReq) {
        return feedPlanService.getPlanList(pageReq);
    }

    // 新增计划
    @PostMapping
    public ResponseMessage<?> addPlan(@Validated @RequestBody FeedPlan feedPlan) {
        return feedPlanService.addPlan(feedPlan);
    }

    // 编辑计划
    @PutMapping("/{id}")
    public ResponseMessage<?> editPlan(@PathVariable("id") Long id, @Validated @RequestBody FeedPlan feedPlan) {
        feedPlan.setId(id);
        return feedPlanService.editPlan(feedPlan);
    }

    // 删除计划
    @DeleteMapping("/{id}")
    public ResponseMessage<?> deletePlan(@PathVariable("id") Long id) {
        return feedPlanService.deletePlan(id);
    }

    // 更新状态
    @PutMapping("/{id}/status")
    public ResponseMessage<?> updateStatus(@PathVariable("id") Long id, @RequestParam String status) {
        return feedPlanService.updateStatus(id, status);
    }

    // 执行计划
    @PutMapping("/{id}/execute")
    public ResponseMessage<?> executePlan(@PathVariable("id") Long id) {
        return feedPlanService.executePlan(id);
    }
}
