package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.FeedFormulaDao;
import com.example.intelligentfarmcore.dao.FeedPlanDao;
import com.example.intelligentfarmcore.mapper.FeedPlanMapper;
import com.example.intelligentfarmcore.pojo.dto.FeedPlanDTO;
import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import com.example.intelligentfarmcore.pojo.entity.FeedPlan;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IFeedPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedPlanService implements IFeedPlanService {

    @Autowired
    private FeedPlanDao feedPlanDao;

    @Autowired
    private FeedFormulaDao feedFormulaDao;

    @Autowired
    private FeedPlanMapper feedPlanMapper;

    @Override
    public ResponseMessage<PageRes<FeedPlanDTO>> getPlanList(PageReq pageReq) {
        Pageable pageable = PageRequest.of(pageReq.getPageNumber() - 1, pageReq.getPageSize());
        List<FeedPlan> plans;

        // 如果有日期筛选条件
        if (pageReq.getCondition() != null && pageReq.getCondition().containsKey("date")) {
            LocalDate date = (LocalDate) pageReq.getCondition().get("date");
            plans = feedPlanDao.findByDate(date);
        } else if (pageReq.getCondition() != null && pageReq.getCondition().containsKey("startDate") && pageReq.getCondition().containsKey("endDate")) {
            LocalDate startDate = (LocalDate) pageReq.getCondition().get("startDate");
            LocalDate endDate = (LocalDate) pageReq.getCondition().get("endDate");
            plans = feedPlanDao.findByDateBetween(startDate, endDate);
        } else {
            Page<FeedPlan> planPage = feedPlanDao.findAll(pageable);
            plans = planPage.getContent();
        }

        // 手动分页
        int start = (pageReq.getPageNumber() - 1) * pageReq.getPageSize();
        int end = Math.min(start + pageReq.getPageSize(), plans.size());
        List<FeedPlan> pagePlans = plans.subList(start, end);
        List<FeedPlanDTO> planDTOs = feedPlanMapper.toFeedPlanDTOList(pagePlans);
        int totalPages = (plans.size() + pageReq.getPageSize() - 1) / pageReq.getPageSize();
        PageRes<FeedPlanDTO> pageRes = new PageRes<>((long) plans.size(), totalPages, pageReq.getPageNumber(), pageReq.getPageSize(), planDTOs);
        return ResponseMessage.success(pageRes);
    }

    @Override
    public ResponseMessage<FeedPlan> addPlan(FeedPlan feedPlan) {
        // 设置默认状态为待执行
        feedPlan.setStatus("待执行");
        FeedPlan savedPlan = feedPlanDao.save(feedPlan);
        return ResponseMessage.success(savedPlan, "新增计划成功");
    }

    @Override
    public ResponseMessage<FeedPlan> editPlan(FeedPlan feedPlan) {
        // 检查计划是否存在
        FeedPlan existingPlan = feedPlanDao.findById(feedPlan.getId()).orElse(null);
        if (existingPlan == null) {
            return ResponseMessage.error("计划不存在");
        }
        // 更新计划信息
        existingPlan.setDate(feedPlan.getDate());
        existingPlan.setTime(feedPlan.getTime());
        existingPlan.setArea(feedPlan.getArea());
        existingPlan.setFormulaId(feedPlan.getFormulaId());
        existingPlan.setQuantity(feedPlan.getQuantity());
        FeedPlan updatedPlan = feedPlanDao.save(existingPlan);
        return ResponseMessage.success(updatedPlan, "编辑计划成功");
    }

    @Override
    public ResponseMessage<String> deletePlan(Long id) {
        // 检查计划是否存在
        FeedPlan plan = feedPlanDao.findById(id).orElse(null);
        if (plan == null) {
            return ResponseMessage.error("计划不存在");
        }
        // 删除计划
        feedPlanDao.deleteById(id);
        return ResponseMessage.success("删除计划成功");
    }

    @Override
    public ResponseMessage<FeedPlan> updateStatus(Long id, String status) {
        // 检查计划是否存在
        FeedPlan plan = feedPlanDao.findById(id).orElse(null);
        if (plan == null) {
            return ResponseMessage.error("计划不存在");
        }
        // 更新状态
        plan.setStatus(status);
        FeedPlan updatedPlan = feedPlanDao.save(plan);
        return ResponseMessage.success(updatedPlan, "更新状态成功");
    }

    @Transactional
    @Override
    public ResponseMessage<FeedPlan> executePlan(Long id) {
        // 检查计划是否存在
        FeedPlan plan = feedPlanDao.findById(id).orElse(null);
        if (plan == null) {
            return ResponseMessage.error("计划不存在");
        }

        // 检查计划状态
        if (!"待执行".equals(plan.getStatus())) {
            return ResponseMessage.error("只有待执行的计划才能执行");
        }

        // 获取配方信息
        FeedFormula formula = feedFormulaDao.findById(plan.getFormulaId()).orElse(null);
        if (formula == null) {
            return ResponseMessage.error("配方不存在");
        }

        // 检查库存是否足够
        if (formula.getStock() < plan.getQuantity()) {
            return ResponseMessage.error("库存不足，无法执行计划");
        }

        // 扣减库存
        formula.setStock(formula.getStock() - plan.getQuantity());
        feedFormulaDao.save(formula);

        // 更新计划状态为已完成
        plan.setStatus("已完成");
        FeedPlan updatedPlan = feedPlanDao.save(plan);

        return ResponseMessage.success(updatedPlan, "执行计划成功");
    }
}
