package com.example.intelligentfarmcore.pojo.request;

import java.util.List;

public class RoomCheckInReq {
    private Long roomId;
    private List<Long> userIds;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
