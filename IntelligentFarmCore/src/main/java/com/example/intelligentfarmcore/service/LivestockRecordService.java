package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.LivestockRecordDao;
import com.example.intelligentfarmcore.pojo.entity.LivestockRecord;
import com.example.intelligentfarmcore.service.interfaces.ILivestockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivestockRecordService implements ILivestockRecordService {

    @Autowired
    private LivestockRecordDao livestockRecordDao;

    @Override
    public LivestockRecord addRecord(LivestockRecord record) {
        return livestockRecordDao.save(record);
    }

    @Override
    public LivestockRecord getRecordById(Long id) {
        return livestockRecordDao.findById(id).orElse(null);
    }

    @Override
    public List<LivestockRecord> getRecordsByFarmId(Long farmId) {
        return livestockRecordDao.findByFarmId(farmId);
    }

    @Override
    public List<LivestockRecord> getRecordsByLivestockId(Long livestockId) {
        return livestockRecordDao.findByLivestockId(livestockId);
    }

    @Override
    public List<LivestockRecord> getAllRecords() {
        return livestockRecordDao.findAll();
    }
}