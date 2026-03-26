package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.dto.DiseaseRecordDTO;
import com.example.intelligentfarmcore.pojo.entity.DiseaseRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;

import java.util.Map;

public interface IDiseaseRecordService {
    // 分页查询疾病记录列表
    ResponseMessage<PageRes<DiseaseRecordDTO>> getDiseaseList(PageReq pageReq);

    // 获取统计数据
    ResponseMessage<Map<String, Long>> getStatistics();
    
    // 根据农场ID获取统计数据
    ResponseMessage<Map<String, Long>> getStatisticsByFarmId(Long farmId);

    // 新增疾病记录
    ResponseMessage<DiseaseRecord> addDiseaseRecord(DiseaseRecord diseaseRecord);

    // 编辑疾病记录
    ResponseMessage<DiseaseRecord> editDiseaseRecord(DiseaseRecord diseaseRecord);

    // 标记康复
    ResponseMessage<DiseaseRecord> recoverDiseaseRecord(Long id);
}
