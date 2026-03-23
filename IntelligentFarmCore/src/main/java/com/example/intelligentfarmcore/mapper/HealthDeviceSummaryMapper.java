package com.example.intelligentfarmcore.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.intelligentfarmcore.pojo.dto.HealthDeviceSummaryDTO;
import com.example.intelligentfarmcore.pojo.entity.HealthDevice;

@Mapper(componentModel = "spring")
public interface HealthDeviceSummaryMapper {
    // 映射方法
    public HealthDeviceSummaryDTO mapToDTO(HealthDevice healthDevice);

    List<HealthDeviceSummaryDTO> toDTOList(List<HealthDevice> deviceList);
}
