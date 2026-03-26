package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.dto.FeedFormulaDTO;
import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;

public interface IFeedFormulaService {
    // 分页查询配方列表
    ResponseMessage<PageRes<FeedFormulaDTO>> getFormulaList(PageReq pageReq);

    // 新增配方
    ResponseMessage<FeedFormula> addFormula(FeedFormula feedFormula);

    // 编辑配方
    ResponseMessage<FeedFormula> editFormula(FeedFormula feedFormula);

    // 删除配方
    ResponseMessage<String> deleteFormula(Long id);

    // 更新库存
    ResponseMessage<FeedFormula> updateStock(Long id, Double stock);
}
