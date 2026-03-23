package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.dto.RoomDTO;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.AddRoomReq;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.request.RoomCheckInReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController // 接口方法返回对象 ，默认返回JSON格式
@RequestMapping("/room") // localhost:8080/room/**
public class RoomController {
    @Autowired
    IRoomService roomService;

    // 添加房间
    @PostMapping("/add")
    public ResponseMessage<String> addRoom(@RequestBody AddRoomReq room) {
        return roomService.addRoom(room);
    }

    // 编辑房间
    @PostMapping("/edit")
    public ResponseMessage<String> editRoom(@RequestBody AddRoomReq room) {
        return roomService.editRoom(room);
    }

    // 查询房间列表
    @PostMapping("/list")
    public ResponseMessage<PageRes<RoomDTO>> getRoomList(@RequestBody PageReq pageReq) {
        return roomService.getRoomList(pageReq);
    }

    // 删除房间
    @GetMapping("/delete")
    public ResponseMessage<String> deleteRoom(@RequestParam Long roomId) {
        return roomService.deleteRoom(roomId);
    }

    // 批量删除房间
    @PostMapping("/deleteBatch")
    public ResponseMessage<String> deleteBatch(@RequestBody Map<String, Object> requestBody) {
        @SuppressWarnings("unchecked")
        List<Long> roomIds = (List<Long>) requestBody.get("roomIds");
        if (roomIds == null || roomIds.isEmpty()) {
            return ResponseMessage.error("房间ID列表不能为空");
        }
        return roomService.deleteBatch(roomIds);
    }

    // 房间入住（添加用户到房间）
    @PostMapping("/checkIn")
    public ResponseMessage<String> checkIn(@RequestBody RoomCheckInReq roomCheckInReq) {
        return roomService.checkIn(roomCheckInReq);
    }

    // 批量修改房间容量
    @PostMapping("/batchUpdateCapacity")
    public ResponseMessage<String> batchUpdateCapacity(@RequestBody Map<String, Object> requestBody) {
        return roomService.batchUpdateCapacity(requestBody);
    }
}
