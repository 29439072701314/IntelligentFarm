package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.FeedStockRecordDao;
import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import com.example.intelligentfarmcore.pojo.entity.FeedStockRecord;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IFeedFormulaService;
import com.example.intelligentfarmcore.service.interfaces.IFeedStockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedStockRecordService implements IFeedStockRecordService {

    @Autowired
    private FeedStockRecordDao feedStockRecordDao;

    @Autowired
    private IFeedFormulaService feedFormulaService;

    @Override
    public ResponseMessage<?> getRecordList(PageReq pageReq) {
        Pageable pageable = PageRequest.of(pageReq.getPageNumber() - 1, pageReq.getPageSize());
        Page<FeedStockRecord> recordPage = feedStockRecordDao.findAll(pageable);
        PageRes<FeedStockRecord> pageRes = new PageRes<>(recordPage.getContent(), (int) recordPage.getTotalElements());
        return ResponseMessage.success(pageRes);
    }

    @Override
    public ResponseMessage<?> getRecordsByFormulaId(Long formulaId) {
        List<FeedStockRecord> records = feedStockRecordDao.findByFormulaId(formulaId);
        return ResponseMessage.success(records);
    }

    @Transactional
    @Override
    public ResponseMessage<?> addRecord(FeedStockRecord record) {
        // 设置入库时间
        record.setCreateTime(LocalDateTime.now());

        // 保存入库记录
        FeedStockRecord savedRecord = feedStockRecordDao.save(record);

        // 更新饲料配方库存
        ResponseMessage<?> response = feedFormulaService.getFormulaById(record.getFormulaId());
        if (response.getCode() == 200) {
            FeedFormula formula = (FeedFormula) response.getData();
            double newStock = formula.getStock() + record.getQuantity();
            feedFormulaService.updateStock(record.getFormulaId(), newStock);
        }

        return ResponseMessage.success(savedRecord, "入库成功");
    }

    @Override
    public ResponseMessage<?> deleteRecord(Long id) {
        feedStockRecordDao.deleteById(id);
        return ResponseMessage.success("删除成功");
    }
}