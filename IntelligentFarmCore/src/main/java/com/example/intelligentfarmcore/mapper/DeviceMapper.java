package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.DeviceDTO;
import com.example.intelligentfarmcore.pojo.entity.Device;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = { RoomSummaryMapper.class })
public interface DeviceMapper {
    DeviceDTO toDTO(Device device);

    List<DeviceDTO> toDTOList(List<Device> deviceList);
}