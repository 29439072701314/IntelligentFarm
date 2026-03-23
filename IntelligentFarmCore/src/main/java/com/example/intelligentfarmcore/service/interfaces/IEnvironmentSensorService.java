package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.entity.EnvironmentData;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;

import java.util.Map;

public interface IEnvironmentSensorService {
    /**
     * 添加环境传感器数据
     * 
     * @param environmentData 环境传感器对象
     */
    void addEnvironmentSensor(EnvironmentData environmentData);

    /**
     * 获取环境传感器数据列表
     * 
     * @param pageReq 分页请求对象
     * @return 环境传感器数据分页列表
     */
    ResponseMessage<Map<String, Object>> getEnvironmentDataList(PageReq pageReq);
}
