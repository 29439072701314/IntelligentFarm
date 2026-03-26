package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.DiseaseRecordDTO;
import com.example.intelligentfarmcore.pojo.entity.DiseaseRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DiseaseRecordMapper {
    DiseaseRecordMapper INSTANCE = Mappers.getMapper(DiseaseRecordMapper.class);

    DiseaseRecordDTO toDiseaseRecordDTO(DiseaseRecord diseaseRecord);

    List<DiseaseRecordDTO> toDiseaseRecordDTOList(List<DiseaseRecord> diseaseRecords);
}
