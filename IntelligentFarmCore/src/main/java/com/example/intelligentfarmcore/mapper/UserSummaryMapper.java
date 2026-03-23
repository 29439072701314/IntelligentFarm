package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.UserSummaryDTO;
import com.example.intelligentfarmcore.pojo.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserSummaryMapper {
    UserSummaryDTO toDTO(User user);
    List<UserSummaryDTO> toDTOList(List<User> userList);
}
