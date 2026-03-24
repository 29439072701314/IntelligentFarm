package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.Livestock;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.service.interfaces.ILivestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/livestock")
public class LivestockController {

    @Autowired
    private ILivestockService livestockService;

    // 分页查询牲畜列表
    @GetMapping("/list")
    public ResponseMessage<?> getLivestockList(PageReq pageReq) {
        return livestockService.getLivestockList(pageReq);
    }

    // 新增牲畜
    @PostMapping
    public ResponseMessage<?> addLivestock(@Validated @RequestBody Livestock livestock) {
        return livestockService.addLivestock(livestock);
    }

    // 编辑牲畜
    @PutMapping("/{id}")
    public ResponseMessage<?> editLivestock(@PathVariable("id") Long id, @Validated @RequestBody Livestock livestock) {
        livestock.setLivestockId(id);
        return livestockService.editLivestock(livestock);
    }

    // 删除牲畜
    @DeleteMapping("/{id}")
    public ResponseMessage<?> deleteLivestock(@PathVariable("id") Long id) {
        return livestockService.deleteLivestock(id);
    }

    // 获取牲畜详情
    @GetMapping("/{id}/detail")
    public ResponseMessage<?> getLivestockDetail(@PathVariable("id") Long id) {
        return livestockService.getLivestockDetail(id);
    }
}