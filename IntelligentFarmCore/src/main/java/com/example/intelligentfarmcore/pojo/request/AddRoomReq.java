package com.example.intelligentfarmcore.pojo.request;

import com.example.intelligentfarmcore.pojo.entity.Room;

public class AddRoomReq extends Room {
    private Long deviceId;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}
