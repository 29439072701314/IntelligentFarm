package com.example.intelligentfarmcore.pojo.dto;

/**
 * Room摘要数据传输对象，用于避免循环引用
 */
public class RoomSummaryDTO {
    // 房间ID
    private Long roomId;

    // 房间号
    private String roomNumber;

    // 最大容量
    private Integer maxCapacity;

    // 当前人数
    private Integer currentCount;

    // 入住率
    private Double occupancyRate;

    // 构造方法
    public RoomSummaryDTO() {
    }

    public RoomSummaryDTO(Long roomId, String roomNumber) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
    }

    // 从实体转换为DTO的构造方法

    public RoomSummaryDTO(Long roomId, String roomNumber, Integer maxCapacity, Integer currentCount,
            Double occupancyRate) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.maxCapacity = maxCapacity;
        this.currentCount = currentCount;
        this.occupancyRate = occupancyRate;
    }

    // Getter和Setter方法
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

    public Double getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(Double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }
}