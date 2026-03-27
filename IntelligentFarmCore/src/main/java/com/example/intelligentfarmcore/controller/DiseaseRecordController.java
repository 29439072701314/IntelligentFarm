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
@RequestMapping("/disease")
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
    public ResponseMessage<?> getStatistics(@RequestParam(required = false) String farmId) {
        if (farmId != null && !farmId.isEmpty()) {
            try {
                Long id = Long.valueOf(farmId);
                return diseaseRecordService.getStatisticsByFarmId(id);
            } catch (NumberFormatException e) {
                // 农场ID格式错误，返回所有统计
                return diseaseRecordService.getStatistics();
            }
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
