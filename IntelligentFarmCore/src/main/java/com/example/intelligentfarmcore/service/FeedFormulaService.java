package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.FeedFormulaDao;
import com.example.intelligentfarmcore.dao.FeedPlanDao;
import com.example.intelligentfarmcore.mapper.FeedFormulaMapper;
import com.example.intelligentfarmcore.pojo.dto.FeedFormulaDTO;
import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import com.example.intelligentfarmcore.pojo.entity.FeedPlan;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IFeedFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedFormulaService implements IFeedFormulaService {

    @Autowired
    private FeedFormulaDao feedFormulaDao;

    @Autowired
    private FeedPlanDao feedPlanDao;

    @Autowired
    private FeedFormulaMapper feedFormulaMapper;

    @Autowired
    private WarningService warningService;

    @Override
    public ResponseMessage<PageRes<FeedFormulaDTO>> getFormulaList(PageReq pageReq) {
        Pageable pageable = PageRequest.of(pageReq.getPageNumber() - 1, pageReq.getPageSize());
        Page<FeedFormula> formulaPage;

        try {
            // 先测试数据库连接
            System.out.println("开始获取配方列表...");
            long count = feedFormulaDao.count();
            System.out.println("配方总数: " + count);
            
            // 如果有搜索条件，根据名称模糊查询
            if (pageReq.getCondition() != null && pageReq.getCondition().containsKey("name")) {
                String name = (String) pageReq.getCondition().get("name");
                System.out.println("搜索名称: " + name);
                List<FeedFormula> formulas = feedFormulaDao.findByNameContaining(name);
                System.out.println("搜索结果数量: " + formulas.size());
                // 手动分页
                int start = (pageReq.getPageNumber() - 1) * pageReq.getPageSize();
                int end = Math.min(start + pageReq.getPageSize(), formulas.size());
                List<FeedFormula> pageFormulas = formulas.subList(start, end);
                System.out.println("分页后数量: " + pageFormulas.size());
                
                // 测试Mapper
                System.out.println("开始转换DTO...");
                List<FeedFormulaDTO> formulaDTOs = feedFormulaMapper.toFeedFormulaDTOList(pageFormulas);
                System.out.println("转换后数量: " + formulaDTOs.size());
                
                int totalPages = (formulas.size() + pageReq.getPageSize() - 1) / pageReq.getPageSize();
                PageRes<FeedFormulaDTO> pageRes = new PageRes<>((long) formulas.size(), totalPages, pageReq.getPageNumber(), pageReq.getPageSize(), formulaDTOs);
                return ResponseMessage.success(pageRes);
            } else {
                System.out.println("获取所有配方...");
                formulaPage = feedFormulaDao.findAll(pageable);
                System.out.println("获取结果数量: " + formulaPage.getContent().size());
                
                // 测试Mapper
                System.out.println("开始转换DTO...");
                List<FeedFormulaDTO> formulaDTOs = feedFormulaMapper.toFeedFormulaDTOList(formulaPage.getContent());
                System.out.println("转换后数量: " + formulaDTOs.size());
                
                PageRes<FeedFormulaDTO> pageRes = new PageRes<>(formulaPage, formulaDTOs);
                return ResponseMessage.success(pageRes);
            }
        } catch (Exception e) {
            System.out.println("获取配方列表失败:");
            e.printStackTrace();
            return ResponseMessage.error("获取配方列表失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseMessage<FeedFormula> addFormula(FeedFormula feedFormula) {
        FeedFormula savedFormula = feedFormulaDao.save(feedFormula);
        return ResponseMessage.success(savedFormula, "新增配方成功");
    }

    @Override
    public ResponseMessage<FeedFormula> editFormula(FeedFormula feedFormula) {
        // 检查配方是否存在
        FeedFormula existingFormula = feedFormulaDao.findById(feedFormula.getId()).orElse(null);
        if (existingFormula == null) {
            return ResponseMessage.error("配方不存在");
        }
        // 更新配方信息
        existingFormula.setName(feedFormula.getName());
        existingFormula.setStage(feedFormula.getStage());
        existingFormula.setDailyRecommendation(feedFormula.getDailyRecommendation());
        existingFormula.setStock(feedFormula.getStock());
        existingFormula.setThreshold(feedFormula.getThreshold());
        FeedFormula updatedFormula = feedFormulaDao.save(existingFormula);
        return ResponseMessage.success(updatedFormula, "编辑配方成功");
    }

    @Override
    public ResponseMessage<String> deleteFormula(Long id) {
        // 检查配方是否存在
        FeedFormula formula = feedFormulaDao.findById(id).orElse(null);
        if (formula == null) {
            return ResponseMessage.error("配方不存在");
        }
        // 检查是否有关联的计划（状态不为取消）
        List<FeedPlan> plans = feedPlanDao.findByFormulaIdAndStatusNot(id, "取消");
        if (!plans.isEmpty()) {
            return ResponseMessage.error("该配方有关联的计划，无法删除");
        }
        // 删除配方
        feedFormulaDao.deleteById(id);
        return ResponseMessage.success("删除配方成功");
    }

    @Transactional
    @Override
    public ResponseMessage<FeedFormula> updateStock(Long id, Double stock) {
        // 检查配方是否存在
        FeedFormula formula = feedFormulaDao.findById(id).orElse(null);
        if (formula == null) {
            return ResponseMessage.error("配方不存在");
        }
        // 保存旧库存
        Double oldStock = formula.getStock();
        // 更新库存
        formula.setStock(stock);
        FeedFormula updatedFormula = feedFormulaDao.save(formula);
        // 检查库存是否低于阈值
        if (stock < formula.getThreshold()) {
            // 生成告警
            warningService.generateWarning("饲料", formula.getId().toString(), "配方" + formula.getName() + "库存低于阈值", "低");
        } else if (oldStock < formula.getThreshold() && stock >= formula.getThreshold()) {
            // 库存恢复正常，消除告警
            warningService.eliminateFeedWarning(formula.getId().toString());
        }
        return ResponseMessage.success(updatedFormula, "更新库存成功");
    }
}
