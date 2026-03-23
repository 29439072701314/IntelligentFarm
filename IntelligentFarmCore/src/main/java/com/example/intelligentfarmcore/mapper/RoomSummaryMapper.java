package com.example.intelligentfarmcore.mapper;

import com.example.intelligentfarmcore.pojo.dto.RoomSummaryDTO;
import com.example.intelligentfarmcore.pojo.entity.Room;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomSummaryMapper {
    RoomSummaryDTO toDTO(Room room);
    List<RoomSummaryDTO> toDTOList(List<Room> roomList);
}
