package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.LivestockDTO;
import com.example.intelligentfarmcore.pojo.entity.Livestock;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LivestockMapper {
    LivestockMapper INSTANCE = Mappers.getMapper(LivestockMapper.class);

    LivestockDTO toLivestockDTO(Livestock livestock);

    List<LivestockDTO> toLivestockDTOList(List<Livestock> livestockList);
}