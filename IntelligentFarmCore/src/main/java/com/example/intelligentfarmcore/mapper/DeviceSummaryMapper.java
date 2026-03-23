package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.DeviceSummaryDTO;
import com.example.intelligentfarmcore.pojo.entity.Device;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceSummaryMapper {
    DeviceSummaryDTO toDTO(Device device);
    List<DeviceSummaryDTO> toDTOList(List<Device> deviceList);
}
