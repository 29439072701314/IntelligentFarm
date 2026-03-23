package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.EnvironmentSensorDao;
import com.example.intelligentfarmcore.pojo.entity.EnvironmentData;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.service.interfaces.IEnvironmentSensorService;
import com.example.intelligentfarmcore.utils.ConditionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EnvironmentSensorService implements IEnvironmentSensorService {
    @Autowired
    private EnvironmentSensorDao environmentSensorDao;

    @Override
    public void addEnvironmentSensor(EnvironmentData environmentData) {
        environmentSensorDao.save(environmentData);
    }

    @Override
    public ResponseMessage<Map<String, Object>> getEnvironmentDataList(PageReq pageReq) {
        // 从请求参数中提取条件
        ConditionUtils conditionUtils = new ConditionUtils(pageReq.getCondition());

        // 构建查询条件
        Long deviceId = conditionUtils.getLong("deviceId");
        long minTime = conditionUtils.getLong("minTime");
        long maxTime = conditionUtils.getLong("maxTime");

        // 调用 dao 层方法进行条件查询
        List<EnvironmentData> environmentDatas = environmentSensorDao.findByConditions(
                deviceId,
                minTime,
                maxTime);
        // 构建响应数据
        return ResponseMessage.success(Map.of("content", environmentDatas));
    }
}
