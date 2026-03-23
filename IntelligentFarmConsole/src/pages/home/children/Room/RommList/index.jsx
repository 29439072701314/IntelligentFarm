import React, { useState } from "react";
import { Form, Button, Modal, Input, message } from "antd";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { getColumns } from "./constant";
import { DeleteOutlined, PlusOutlined, EditOutlined } from "@ant-design/icons";
import {
  apiRoomDelete,
  apiRoomDeleteBatch,
  apiRoomList,
} from "../../../../../services/roomApi";
import RoomEditModal from "./component/RoomEditModal";
import RoomBatchUpdateCapacityModal from "./component/RoomBatchUpdateCapacityModal";
import useModal from "@/hooks/useModal";
import RoomNumberTag from "../../../../../component/RoomNumberTag";
const { confirm } = Modal;
export default function RoomList() {
  const [form] = Form.useForm();
  const [roomEditForm] = Form.useForm();
  const [open, onOpen, onClose] = useModal();
  const [batchModalOpen, onBatchModalOpen, onBatchModalClose] = useModal();
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  // 关闭编辑弹窗
  const handleCloseModal = (isGetData = true) => {
    roomEditForm.resetFields();
    if (isGetData) {
      form.getData();
    }
    onClose();
  };
  // 编辑房间按钮点击事件
  const handleEdit = (record) => {
    roomEditForm.setFieldsValue({
      roomId: record.roomId,
      roomNumber: record.roomNumber,
      deviceId: record.device?.deviceId,
      maxCapacity: record.maxCapacity,
      currentCount: record.currentCount,
    });
    onOpen();
  };
  // 删除房间
  const handleDelete = (record) => {
    console.log(record);
    confirm({
      title: "提示",
      content: (
        <>
          确认删除房间 {<RoomNumberTag roomNumber={record.roomNumber} />} 吗？
        </>
      ),
      onOk: async () => {
        await apiRoomDelete({ roomId: record.roomId });
        setSelectedRowKeys([]);
        form.getData();
      },
      centered: true,
    });
  };
  // 批量删除房间
  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning("请选择要删除的房间");
      return;
    }
    confirm({
      title: "提示",
      content: `确认删除选中的 ${selectedRowKeys.length} 个房间吗？`,
      onOk: async () => {
        await apiRoomDeleteBatch({ roomIds: selectedRowKeys });
        setSelectedRowKeys([]);
        form.getData();
      },
      centered: true,
    });
  };
  // 批量修改房间容量
  const handleBatchUpdateCapacity = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning("请选择要修改容量的房间");
      return;
    }
    onBatchModalOpen();
  };
  // 关闭批量修改容量弹窗
  const handleCloseBatchModal = (isGetData = true) => {
    if (isGetData) {
      form.getData();
      setSelectedRowKeys([]);
    }
    onBatchModalClose();
  };
  return (
    <Content title="房间列表">
      <RoomEditModal
        open={open}
        onClose={handleCloseModal}
        form={roomEditForm}
      />
      <RoomBatchUpdateCapacityModal
        open={batchModalOpen}
        onClose={handleCloseBatchModal}
        selectedRoomIds={selectedRowKeys}
      />
      <ProTable
        rowKey="roomId"
        rowSelection={{
          type: "checkbox",
          selectedRowKeys: selectedRowKeys,
          preserveSelectedRowKeys: true,
          onChange: (selectedRowKeys) => {
            setSelectedRowKeys(selectedRowKeys);
          },
        }}
        form={form}
        api={apiRoomList}
        // beforeSearch={handleBeforeSearch}
        columns={getColumns(handleDelete, handleEdit)}
        extraOptions={[
          <Button type="primary" icon={<PlusOutlined />} onClick={onOpen}>
            添加
          </Button>,
          <Button
            type="primary"
            icon={<EditOutlined />}
            onClick={handleBatchUpdateCapacity}
          >
            批量修改容量
          </Button>,
          <Button
            type="primary"
            icon={<DeleteOutlined />}
            danger
            onClick={handleBatchDelete}
          >
            批量删除
          </Button>,
        ]}
      />
    </Content>
  );
}
