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

    @Autowired
    private WarningService warningService;

    @Override
    public void addEnvironmentSensor(EnvironmentData environmentData) {
        environmentSensorDao.save(environmentData);

        String deviceId = environmentData.getDeviceId().toString();
        boolean hasWarning = false;

        // 检查温度是否超过30℃，如果是，生成环境告警
        if (environmentData.getTemperature() != null && environmentData.getTemperature() > 30) {
            String details = "温度超过30℃，当前温度：" + environmentData.getTemperature() + "℃";
            warningService.generateWarning("环境", deviceId, details, "高");
            hasWarning = true;
        }

        // 检查烟雾浓度是否超过3000，如果是，生成环境告警
        if (environmentData.getGasConcentration() != null && environmentData.getGasConcentration() > 3000) {
            String details = "烟雾浓度超过3000，当前浓度：" + environmentData.getGasConcentration();
            warningService.generateWarning("环境", deviceId, details, "高");
            hasWarning = true;
        }

        // 如果没有告警，消除该设备的环境告警
        if (!hasWarning) {
            warningService.eliminateEnvironmentWarning(deviceId);
        }
    }

    @Override
    public ResponseMessage<Map<String, Object>> getEnvironmentDataList(PageReq pageReq) {
        // 从请求参数中提取条件
        ConditionUtils conditionUtils = new ConditionUtils(pageReq.getCondition());

        // 构建查询条件
        Long deviceId = conditionUtils.getLong("deviceId");
        Long minTimeLong = conditionUtils.getLong("minTime");
        Long maxTimeLong = conditionUtils.getLong("maxTime");
        
        // 设置默认值，确保查询能够正常工作
        long minTime = minTimeLong != null ? minTimeLong : System.currentTimeMillis() - 24 * 60 * 60 * 1000; // 默认查询过去24小时
        long maxTime = maxTimeLong != null ? maxTimeLong : System.currentTimeMillis(); // 默认查询到当前时间

        // 调用 dao 层方法进行条件查询
        List<EnvironmentData> environmentDatas = environmentSensorDao.findByConditions(
                deviceId,
                minTime,
                maxTime);
        // 构建响应数据
        return ResponseMessage.success(Map.of("content", environmentDatas));
    }
}
