package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.service.interfaces.IFeedFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/feed/formula")
public class FeedFormulaController {

    @Autowired
    private IFeedFormulaService feedFormulaService;

    // 分页查询配方列表
    @GetMapping("/list")
    public ResponseMessage<?> getFormulaList(PageReq pageReq) {
        return feedFormulaService.getFormulaList(pageReq);
    }

    // 新增配方
    @PostMapping
    public ResponseMessage<?> addFormula(@Validated @RequestBody FeedFormula feedFormula) {
        return feedFormulaService.addFormula(feedFormula);
    }

    // 编辑配方
    @PutMapping("/{id}")
    public ResponseMessage<?> editFormula(@PathVariable("id") Long id, @Validated @RequestBody FeedFormula feedFormula) {
        feedFormula.setId(id);
        return feedFormulaService.editFormula(feedFormula);
    }

    // 删除配方
    @DeleteMapping("/{id}")
    public ResponseMessage<?> deleteFormula(@PathVariable("id") Long id) {
        return feedFormulaService.deleteFormula(id);
    }

    // 更新库存
    @PutMapping("/{id}/stock")
    public ResponseMessage<?> updateStock(@PathVariable("id") Long id, @RequestParam Double stock) {
        return feedFormulaService.updateStock(id, stock);
    }
}
