package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.FeedFormulaDTO;
import com.example.intelligentfarmcore.pojo.entity.FeedFormula;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedFormulaMapper {

    FeedFormulaDTO toFeedFormulaDTO(FeedFormula feedFormula);

    List<FeedFormulaDTO> toFeedFormulaDTOList(List<FeedFormula> feedFormulas);
}
