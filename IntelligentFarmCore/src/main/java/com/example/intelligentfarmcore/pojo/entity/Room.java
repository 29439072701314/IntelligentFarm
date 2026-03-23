package com.example.intelligentfarmcore.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_room")
@JsonIgnoreProperties({ "users" })
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    // 房间号
    @Column(name = "room_number")
    private String roomNumber;

    // 房间容量
    @Column(name = "max_capacity")
    private Integer maxCapacity;

    // 房间当前人数
    @Column(name = "current_count")
    private Integer currentCount;

    // 入住率
    @Column(name = "occupancy_rate")
    private Double occupancyRate;

    // 房间内的用户列表（一对多关系）
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<User> users;

    // 房间内的环境传感器数据列表（一对多关系）
    @OneToOne()
    @JoinColumn(name = "device_id", unique = true)
    private Device device;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Double getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(Double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", currentCount=" + currentCount +
                ", occupancyRate=" + occupancyRate +
                ", users=" + users +
                ", device=" + device +
                '}';
    }
}