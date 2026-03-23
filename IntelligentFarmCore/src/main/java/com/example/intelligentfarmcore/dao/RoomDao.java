package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoomDao extends JpaRepository<Room, Long> {
    boolean existsByRoomNumber(String roomNumber);

    // 分页查询房间列表
    @Query("SELECT r FROM Room r WHERE " +
            "(:roomNumber IS NULL OR r.roomNumber LIKE %:roomNumber%)")
    Page<Room> findByConditions(
            @Param("roomNumber") String roomNumber,
            Pageable pageable
    );

    Room findByRoomNumber(String roomNumber);
}
