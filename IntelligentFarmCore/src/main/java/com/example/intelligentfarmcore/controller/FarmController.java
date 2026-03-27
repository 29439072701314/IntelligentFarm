package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.Farm;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.service.interfaces.IFarmService;
import com.example.intelligentfarmcore.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/farm")
public class FarmController {

    @Autowired
    private IFarmService farmService;

    // 分页查询农场列表
    @GetMapping("/list")
    public ResponseMessage<?> getFarmList(PageReq pageReq) {
        return farmService.getFarmList(pageReq);
    }

    // 新增农场
    @PostMapping
    public ResponseMessage<?> addFarm(@Validated @RequestBody Farm farm, @RequestHeader("Authorization") String token) {
        Long userId = JWTUtils.getUserFromToken(token).getUserId();
        return farmService.addFarm(farm, userId);
    }

    // 编辑农场
    @PutMapping("/{id}")
    public ResponseMessage<?> editFarm(@PathVariable("id") Long id, @Validated @RequestBody Farm farm) {
        farm.setFarmId(id);
        return farmService.editFarm(farm);
    }

    // 删除农场
    @DeleteMapping("/{id}")
    public ResponseMessage<?> deleteFarm(@PathVariable("id") Long id) {
        return farmService.deleteFarm(id);
    }

    // 获取农场详情
    @GetMapping("/{id}/detail")
    public ResponseMessage<?> getFarmDetail(@PathVariable("id") Long id) {
        return farmService.getFarmDetail(id);
    }
}