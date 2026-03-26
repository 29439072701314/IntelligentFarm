package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.entity.DiseaseRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.service.interfaces.IDiseaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/disease")
public class DiseaseRecordController {

    @Autowired
    private IDiseaseRecordService diseaseRecordService;

    // 分页查询疾病记录列表
    @GetMapping("/list")
    public ResponseMessage<?> getDiseaseList(PageReq pageReq) {
        return diseaseRecordService.getDiseaseList(pageReq);
    }

    // 获取统计数据
    @GetMapping("/statistics")
    public ResponseMessage<?> getStatistics(@RequestParam(required = false) Long farmId) {
        if (farmId != null) {
            return diseaseRecordService.getStatisticsByFarmId(farmId);
        } else {
            return diseaseRecordService.getStatistics();
        }
    }

    // 新增疾病记录
    @PostMapping
    public ResponseMessage<?> addDiseaseRecord(@Validated @RequestBody DiseaseRecord diseaseRecord) {
        return diseaseRecordService.addDiseaseRecord(diseaseRecord);
    }

    // 编辑疾病记录
    @PutMapping("/{id}")
    public ResponseMessage<?> editDiseaseRecord(@PathVariable("id") Long id, @Validated @RequestBody DiseaseRecord diseaseRecord) {
        diseaseRecord.setId(id);
        return diseaseRecordService.editDiseaseRecord(diseaseRecord);
    }

    // 标记康复
    @PutMapping("/{id}/recover")
    public ResponseMessage<?> recoverDiseaseRecord(@PathVariable("id") Long id) {
        return diseaseRecordService.recoverDiseaseRecord(id);
    }
}
