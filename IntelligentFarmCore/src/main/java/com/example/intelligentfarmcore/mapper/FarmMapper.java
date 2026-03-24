package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.FarmDTO;
import com.example.intelligentfarmcore.pojo.entity.Farm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FarmMapper {
    FarmMapper INSTANCE = Mappers.getMapper(FarmMapper.class);

    @Mapping(target = "livestockCount", expression = "java(farm.getLivestockList() != null ? farm.getLivestockList().size() : 0)")
    @Mapping(target = "deviceCount", expression = "java(farm.getDeviceList() != null ? farm.getDeviceList().size() : 0)")
    FarmDTO toFarmDTO(Farm farm);

    List<FarmDTO> toFarmDTOList(List<Farm> farms);
}