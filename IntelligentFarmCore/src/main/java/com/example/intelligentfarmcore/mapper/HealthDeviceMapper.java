package com.example.intelligentfarmcore.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.intelligentfarmcore.pojo.dto.HealthDeviceDTO;
import com.example.intelligentfarmcore.pojo.entity.HealthDevice;

@Mapper(componentModel = "spring", uses = { UserSummaryMapper.class })
public interface HealthDeviceMapper {
    HealthDeviceDTO toDTO(HealthDevice healthDevice);

    List<HealthDeviceDTO> toDTOList(List<HealthDevice> healthDevicesList);

}
