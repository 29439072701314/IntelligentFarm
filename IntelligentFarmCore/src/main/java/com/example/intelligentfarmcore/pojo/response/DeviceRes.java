package com.example.intelligentfarmcore.pojo.response;

import com.example.intelligentfarmcore.pojo.entity.Device;
import com.example.intelligentfarmcore.pojo.entity.Room;

public class DeviceRes extends Device {
    Room room;

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }
}
