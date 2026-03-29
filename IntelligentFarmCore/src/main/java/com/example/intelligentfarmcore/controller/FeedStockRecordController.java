package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.FeedStockRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.service.interfaces.IFeedStockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/feed/stock")
public class FeedStockRecordController {

    @Autowired
    private IFeedStockRecordService feedStockRecordService;

    // 分页查询入库记录列表
    @GetMapping("/list")
    public ResponseMessage<?> getRecordList(PageReq pageReq) {
        return feedStockRecordService.getRecordList(pageReq);
    }

    // 根据饲料配方ID查询入库记录
    @GetMapping("/formula/{id}")
    public ResponseMessage<?> getRecordsByFormulaId(@PathVariable("id") Long formulaId) {
        return feedStockRecordService.getRecordsByFormulaId(formulaId);
    }

    // 新增入库记录
    @PostMapping
    public ResponseMessage<?> addRecord(@Validated @RequestBody FeedStockRecord record) {
        return feedStockRecordService.addRecord(record);
    }

    // 删除入库记录
    @DeleteMapping("/{id}")
    public ResponseMessage<?> deleteRecord(@PathVariable("id") Long id) {
        return feedStockRecordService.deleteRecord(id);
    }
}