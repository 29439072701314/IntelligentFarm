package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.FeedPlanDTO;
import com.example.intelligentfarmcore.pojo.entity.FeedPlan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedPlanMapper {

    FeedPlanDTO toFeedPlanDTO(FeedPlan feedPlan);

    List<FeedPlanDTO> toFeedPlanDTOList(List<FeedPlan> feedPlans);
}
