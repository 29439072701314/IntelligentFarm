package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.entity.FeedStockRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;

public interface IFeedStockRecordService {
    // 分页查询入库记录列表
    ResponseMessage<?> getRecordList(PageReq pageReq);

    // 根据饲料配方ID查询入库记录
    ResponseMessage<?> getRecordsByFormulaId(Long formulaId);

    // 新增入库记录
    ResponseMessage<?> addRecord(FeedStockRecord record);

    // 删除入库记录
    ResponseMessage<?> deleteRecord(Long id);
}