package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.DeviceDao;
import com.example.intelligentfarmcore.dao.RoomDao;
import com.example.intelligentfarmcore.dao.UserDao;
import com.example.intelligentfarmcore.mapper.RoomMapper;
import com.example.intelligentfarmcore.pojo.dto.RoomDTO;
import com.example.intelligentfarmcore.pojo.entity.Device;
import com.example.intelligentfarmcore.pojo.entity.Room;
import com.example.intelligentfarmcore.pojo.entity.User;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.request.AddRoomReq;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.request.RoomCheckInReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IRoomService;
import com.example.intelligentfarmcore.utils.CalculationUtils;
import com.example.intelligentfarmcore.utils.ConditionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.intelligentfarmcore.utils.PageableUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoomService implements IRoomService {
    @Autowired
    RoomDao roomDao;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    DeviceDao deviceDao;
    @Autowired
    UserDao userDao;

    @Override
    public ResponseMessage<String> addRoom(AddRoomReq roomReq) {
        String roomNumber = roomReq.getRoomNumber();
        if (roomNumber.isEmpty())
            return ResponseMessage.error("房间号不能为空");
        // 检查房间号是否已存在
        if (roomDao.existsByRoomNumber(roomNumber))
            return ResponseMessage.error("房间号已存在");
        Device device = null;
        if (roomReq.getDeviceId() != null) {
            device = deviceDao.findById(roomReq.getDeviceId()).orElse(null);
        }
        roomReq.setDevice(device);
        Room room = new Room();
        BeanUtils.copyProperties(roomReq, room);
        room.setCurrentCount(0);
        room.setOccupancyRate(0.0);
        roomDao.save(room);
        return ResponseMessage.success(null, "添加房间成功");
    }

    @Override
    public ResponseMessage<String> editRoom(AddRoomReq roomReq) {
        String roomNumber = roomReq.getRoomNumber();
        if (roomNumber.isEmpty())
            return ResponseMessage.error("房间号不能为空");

        // 检查房间是否存在
        Room existingRoom = roomDao.findById(roomReq.getRoomId()).orElse(null);
        if (existingRoom == null) {
            return ResponseMessage.error("房间不存在");
        }

        // 检查房间号是否已存在
        Room queryRoom = roomDao.findByRoomNumber(roomNumber);
        if (queryRoom != null && !queryRoom.getRoomId().equals(roomReq.getRoomId()))
            return ResponseMessage.error("房间号已存在");

        // 检查新容量是否小于当前人数
        Integer currentCount = existingRoom.getCurrentCount() != null ? existingRoom.getCurrentCount() : 0;
        if (roomReq.getMaxCapacity() != null && roomReq.getMaxCapacity() < currentCount) {
            return ResponseMessage.error("房间容量不能小于当前人数，当前人数：" + currentCount + ", 新容量：" + roomReq.getMaxCapacity());
        }

        Device device = null;
        if (roomReq.getDeviceId() != null) {
            device = deviceDao.findById(roomReq.getDeviceId()).orElse(null);
        }

        roomReq.setDevice(device);
        Room room = new Room();
        BeanUtils.copyProperties(roomReq, room);

        // 保留当前人数
        room.setCurrentCount(currentCount);

        // 计算入住率
        Double occupancyRate = CalculationUtils.calculateOccupancyRate(currentCount, roomReq.getMaxCapacity());
        room.setOccupancyRate(occupancyRate);

        roomDao.save(room);
        return ResponseMessage.success(null, "编辑房间成功");
    }

    @Override
    public ResponseMessage<PageRes<RoomDTO>> getRoomList(PageReq pageReq) {
        // 从请求参数中提取条件
        ConditionUtils conditionUtils = new ConditionUtils(pageReq.getCondition());
        // 构建查询条件
        String roomNumber = conditionUtils.getString("roomNumber");

        // 使用PageableUtils创建Pageable对象
        Pageable pageable = PageableUtils.createPageable(pageReq);

        // 调用 dao 层方法进行条件查询
        Page<Room> rooms = roomDao.findByConditions(
                roomNumber,
                pageable);
        PageRes<RoomDTO> pageRes = new PageRes<>(rooms, roomMapper.toDTOList(rooms.getContent()));
        return ResponseMessage.success(pageRes);
    }

    @Override
    public ResponseMessage<String> deleteRoom(Long roomId) {
        // 检查房间是否存在
        Room room = roomDao.findById(roomId).orElse(null);
        if (room == null)
            return ResponseMessage.error("房间不存在");

        if (room.getCurrentCount() > 0)
            return ResponseMessage.error("房间当前有用户入住，请先退房");
        // 如果房间有关联的设备，先解除关联
        if (room.getDevice() != null) {
            Device device = room.getDevice();
            device.setRoom(null); // 解除设备对房间的引用
            deviceDao.save(device); // 保存设备，更新数据库
            room.setDevice(null); // 同时解除房间对设备的引用
        }

        roomDao.deleteById(roomId);
        return ResponseMessage.success(null, "删除房间成功");
    }

    @Override
    public ResponseMessage<String> deleteBatch(List<Long> roomIds) {
        // 检查是否有房间有人入住
        for (Long roomId : roomIds) {
            Room room = roomDao.findById(roomId).orElse(null);
            if (room != null && room.getCurrentCount() != null && room.getCurrentCount() > 0) {
                return ResponseMessage.error("房间 " + room.getRoomNumber() + " 当前有用户入住，请先退房");
            }
        }

        // 解除所有房间对设备的引用
        roomDao.findAllById(roomIds).forEach(room -> {
            if (room.getDevice() != null) {
                Device device = room.getDevice();
                device.setRoom(null); // 解除设备对房间的引用
                deviceDao.save(device); // 保存设备，更新数据库
                room.setDevice(null); // 同时解除房间对设备的引用
            }
        });

        roomDao.deleteAllById(roomIds);
        return ResponseMessage.success(null, "批量删除房间成功, 共删除 " + roomIds.size() + " 个房间");
    }

    @Override
    public ResponseMessage<String> checkIn(RoomCheckInReq roomCheckInReq) {
        Long roomId = roomCheckInReq.getRoomId();
        List<Long> userIds = roomCheckInReq.getUserIds();

        // 参数验证
        if (roomId == null) {
            return ResponseMessage.error("房间ID不能为空");
        }

        // 检查房间是否存在
        Room room = roomDao.findById(roomId).orElse(null);
        if (room == null) {
            return ResponseMessage.error("房间不存在");
        }

        // 检查房间容量是否足够
        Integer maxCapacity = room.getMaxCapacity();
        if (maxCapacity != null && userIds.size() > maxCapacity) {
            return ResponseMessage.error("房间容量不足，最大容量：" + maxCapacity + ", 尝试入住人数：" + userIds.size());
        }

        // 检查用户
        for (Long userId : userIds) {
            User user = userDao.findById(userId).orElse(null);
            if (user == null) {
                return ResponseMessage.error("用户ID " + userId + " 不存在");
            }
            // 检查用户是否已经分配了其他房间
            if (user.getRoom() != null && !user.getRoom().getRoomId().equals(roomId)) {
                return ResponseMessage.error("用户 " + user.getUserName() + " 已经分配了其他房间");
            }
        }

        // 清除房间当前所有用户关联
        List<User> currentUsers = room.getUsers();
        if (currentUsers != null && !currentUsers.isEmpty()) {
            for (User user : currentUsers) {
                user.setRoom(null);
                userDao.save(user);
            }
        }

        // 分配新用户到房间
        for (Long userId : userIds) {
            User user = userDao.findById(userId).orElse(null);
            user.setRoom(room);
            userDao.save(user);
        }

        // 更新房间当前人数
        room.setCurrentCount(userIds.size());

        // 计算入住率
        Double occupancyRate = CalculationUtils.calculateOccupancyRate(userIds.size(), room.getMaxCapacity());
        room.setOccupancyRate(occupancyRate);

        roomDao.save(room);

        return ResponseMessage.success(null, "房间分配成功，共分配 " + userIds.size() + " 名用户");
    }

    @Override
    public ResponseMessage<String> batchUpdateCapacity(Map<String, Object> requestBody) {
        Integer newCapacity = (Integer) requestBody.get("maxCapacity");
        // 安全地处理 roomIds 参数（JSON中的数字可能是Integer）
        List<Long> roomIds = new ArrayList<>();
        Object roomIdsObj = requestBody.get("roomIds");
        if (roomIdsObj instanceof List) {
            for (Object id : (List<?>) roomIdsObj) {
                if (id instanceof Integer) {
                    roomIds.add(((Integer) id).longValue());
                } else if (id instanceof Long) {
                    roomIds.add((Long) id);
                } else if (id instanceof Number) {
                    roomIds.add(((Number) id).longValue());
                }
            }
        }
        // 参数验证
        if (roomIds.isEmpty()) {
            return ResponseMessage.error("房间ID列表不能为空");
        }
        if (newCapacity == null) {
            return ResponseMessage.error("新容量不能为空");
        }
        if (newCapacity < 0) {
            return ResponseMessage.error("房间容量不能为负数");
        }

        // 检查所有房间是否存在
        List<Room> rooms = new ArrayList<>();
        for (Long roomId : roomIds) {
            Room room = roomDao.findById(roomId).orElse(null);
            if (room == null) {
                return ResponseMessage.error("房间ID " + roomId + " 不存在");
            }

            // 检查新容量是否小于当前人数
            if (newCapacity < (room.getCurrentCount())) {
                return ResponseMessage.error("房间 " + room.getRoomNumber() + " 的当前人数(" +
                        room.getCurrentCount() + ")大于新容量(" + newCapacity + ")");
            }

            rooms.add(room);
        }

        // 批量更新房间容量
        for (Room room : rooms) {
            room.setMaxCapacity(newCapacity);

            // 计算入住率
            Double occupancyRate = CalculationUtils.calculateOccupancyRate(room.getCurrentCount(), newCapacity);
            room.setOccupancyRate(occupancyRate);

            roomDao.save(room);
        }

        return ResponseMessage.success(null, "批量更新房间容量成功，共更新 " + rooms.size() + " 个房间");
    }
}