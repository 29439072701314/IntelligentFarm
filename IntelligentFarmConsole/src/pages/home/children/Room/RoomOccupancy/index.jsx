import React, { useState } from "react";
import Content from "@/component/Content";
import ProTable from "@/component/ProTable";
import { Form, Flex, Tag } from "antd";
import { apiRoomList } from "@/services/roomApi";
import { getColumns } from "./constant";
import RoomCard from "./component/RoomCard";
import UserSelectTable from "@/component/UserSelectTable";
import useModal from "@/hooks/useModal";
import { COLORS, GAP } from "@/constant";
import RoomNumberTag from "@/component/RoomNumberTag";
import { apiRoomCheckIn } from "@/services/roomApi";
import { ROLE } from "../../../../../constant";

export default function RoomOccupancy() {
  const [form] = Form.useForm();
  const [currentRoom, setCurrentRoom] = useState(null);
  const [userSelectVisible, onOpenUserSelect, onCloseUserSelect] = useModal();

  const handleCheckIn = (room) => {
    setCurrentRoom(room);
    onOpenUserSelect();
  };

  const handleClose = (getdata = false) => {
    onCloseUserSelect();
    if (getdata) {
      form.getData(); // 刷新房间列表
    }
  };

  const handleConfirm = async (selectedIds) => {
    await apiRoomCheckIn({
      roomId: currentRoom.roomId,
      userIds: selectedIds,
    });
    handleClose(true);
  };

  const { roomNumber, maxCapacity, users = [] } = currentRoom || {};

  const title = (
    <>为房间 {<RoomNumberTag roomNumber={roomNumber || ""} />} 选择入住用户</>
  );

  return (
    <Content title="入住管理">
      <ProTable
        rowKey="roomId"
        form={form}
        api={apiRoomList}
        columns={getColumns()}
        paginationConfig={{
          align: "start",
          pageSizeOptions: [8, 12, 16, 20],
        }}
        masonryConfig={{
          columns: {
            md: 2,
            lg: 2,
            xl: 3,
            xxl: 4,
          },
          itemRender: (item) => (
            <RoomCard item={item} onCheckIn={handleCheckIn} />
          ),
        }}
      />

      {userSelectVisible && (
        <UserSelectTable
          visible={userSelectVisible}
          onClose={handleClose}
          title={title}
          max={maxCapacity}
          selectedUsers={users}
          role={ROLE.ELDER}
          onConfirm={handleConfirm}
          getCheckboxProps={(record) => ({
            disabled: record.room && record.room.roomId !== currentRoom.roomId,
          })}
        />
      )}
    </Content>
  );
}
