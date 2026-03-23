package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.DeviceDao;
import com.example.intelligentfarmcore.mapper.DeviceMapper;
import com.example.intelligentfarmcore.pojo.dto.DeviceDTO;
import com.example.intelligentfarmcore.pojo.entity.Device;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IDeviceService;
import com.example.intelligentfarmcore.utils.ConditionUtils;
import com.example.intelligentfarmcore.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService implements IDeviceService {
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Long updateDeviceData(Device device) {
        // 检查设备是否已存在
        boolean exists = deviceDao.existsDeviceByDeviceName(device.getDeviceName());
        Long deviceId = null;
        if (exists) {
            // 更新设备数据
            Device existingDevice = deviceDao.findDeviceByDeviceName(device.getDeviceName());
            existingDevice.setDeviceData(device);
            deviceId = deviceDao.save(existingDevice).getDeviceId();
        } else {
            // 添加设备
            deviceId = deviceDao.save(device).getDeviceId();
        }
        return deviceId;
    }

    @Override
    public ResponseMessage<PageRes<DeviceDTO>> getDeviceList(PageReq pageReq) {
        // 从请求参数中提取条件
        Pageable pageable = PageableUtils.createPageable(pageReq);
        ConditionUtils conditionUtils = new ConditionUtils(pageReq.getCondition());

        // 构建查询条件
        String deviceName = conditionUtils.getString("deviceName");

        // 调用 dao 层方法进行条件查询
        Page<Device> devices = deviceDao.findByConditions(
                deviceName,
                pageable);
        List<DeviceDTO> deviceDTOS = deviceMapper.toDTOList(devices.getContent());
        PageRes<DeviceDTO> pageRes = new PageRes<>(devices, deviceDTOS);
        return ResponseMessage.success(pageRes);
    }

    @Override
    public ResponseMessage<List<DeviceDTO>> getAllDevice() {
        List<Device> devices = deviceDao.findAll();
        List<DeviceDTO> deviceDTOS = deviceMapper.toDTOList(devices);
        return ResponseMessage.success(deviceDTOS);
    }
}
