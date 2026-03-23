package com.example.intelligentfarmcore.pojo.dto;

import java.util.List;

public class RoomDTO {
    private Long roomId;

    // 房间号
    private String roomNumber;

    // 最大容量
    private Integer maxCapacity;

    // 当前人数
    private Integer currentCount;

    // 入住率
    private Double occupancyRate;

    // 房间内的用户列表（一对多关系）
    private List<UserSummaryDTO> users;

    // 房间内的环境传感器数据列表（一对多关系）
    private DeviceSummaryDTO device;

    public RoomDTO() {
    }

    public RoomDTO(Long roomId, String roomNumber, Integer maxCapacity, Integer currentCount, Double occupancyRate,
            List<UserSummaryDTO> users, DeviceSummaryDTO device) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.maxCapacity = maxCapacity;
        this.currentCount = currentCount;
        this.occupancyRate = occupancyRate;
        this.users = users;
        this.device = device;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    public List<UserSummaryDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserSummaryDTO> users) {
        this.users = users;
    }

    public DeviceSummaryDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceSummaryDTO device) {
        this.device = device;
    }

    public Double getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(Double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }
}
