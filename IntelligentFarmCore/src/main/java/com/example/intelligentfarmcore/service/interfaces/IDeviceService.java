package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.dto.DeviceDTO;
import com.example.intelligentfarmcore.pojo.entity.Device;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;

import java.util.List;

public interface IDeviceService {
    /**
     * 更新设备数据
     * 
     * @param device
     * @return 设备ID
     */
    Long updateDeviceData(Device device);

    /**
     * 查询设备列表
     * 
     * @param pageReq 分页请求对象
     * @return 设备列表分页响应对象
     */
    ResponseMessage<PageRes<DeviceDTO>> getDeviceList(PageReq pageReq);

    /**
     * 获取所有设备
     * 
     * @return 所有设备列表
     */
    ResponseMessage<List<DeviceDTO>> getAllDevice();

    /**
     * 绑定设备到农场
     * 
     * @param deviceId 设备ID
     * @param farmId 农场ID
     * @return 绑定结果
     */
    ResponseMessage<String> bindDeviceToFarm(Long deviceId, Long farmId);

    /**
     * 解绑设备与农场
     * 
     * @param deviceId 设备ID
     * @return 解绑结果
     */
    ResponseMessage<String> unbindDeviceFromFarm(Long deviceId);

    /**
     * 根据农场ID查询设备列表
     * 
     * @param farmId 农场ID
     * @return 设备列表
     */
    ResponseMessage<List<DeviceDTO>> getDevicesByFarmId(Long farmId);
}
