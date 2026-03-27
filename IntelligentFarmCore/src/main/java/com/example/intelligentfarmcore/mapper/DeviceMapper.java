package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.DeviceDTO;
import com.example.intelligentfarmcore.pojo.dto.FarmSummaryDTO;
import com.example.intelligentfarmcore.pojo.entity.Device;
import com.example.intelligentfarmcore.pojo.entity.Farm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    @Mapping(source = "farm", target = "farm", qualifiedByName = "farmToSummaryDTO")
    DeviceDTO toDTO(Device device);

    List<DeviceDTO> toDTOList(List<Device> deviceList);

    @Named("farmToSummaryDTO")
    default FarmSummaryDTO farmToSummaryDTO(Farm farm) {
        if (farm == null) {
            return null;
        }
        return new FarmSummaryDTO(farm.getFarmId(), farm.getFarmName(), farm.getAddress());
    }
}
