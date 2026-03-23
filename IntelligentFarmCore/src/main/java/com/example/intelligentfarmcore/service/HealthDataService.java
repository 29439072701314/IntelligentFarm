package com.example.intelligentfarmcore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.intelligentfarmcore.dao.HealthDataDao;
import com.example.intelligentfarmcore.pojo.entity.HealthData;
import com.example.intelligentfarmcore.service.interfaces.IHealthDataService;

@Service
public class HealthDataService implements IHealthDataService {
    @Autowired
    private HealthDataDao healthDataDao;

    @Override
    public void addHealthData(HealthData healthData) {
        healthDataDao.save(healthData);
    }
}
