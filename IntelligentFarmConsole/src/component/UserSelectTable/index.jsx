import React, { useState } from "react";
import { Modal, Form, Button, message, Flex, Tag, Space } from "antd";
import ProTable from "@/component/ProTable";
import { apiGetUserList } from "@/services/userApi";
import { getColumns } from "./constant";
import { COLORS, GAP } from "@/constant";

export default function UserSelectTable({
  visible,
  onClose,
  title,
  max,
  selectedUsers = [],
  role,
  onConfirm,
  selectionType = "checkbox",
  getCheckboxProps,
}) {
  const [form] = Form.useForm();
  const [selectedRowKeys, setSelectedRowKeys] = useState(
    selectedUsers.map((user) => user.userId),
  );
  const [queryUsers, setQueryUsers] = useState(selectedUsers);
  const [selectedRows, setSelectedRows] = useState(selectedUsers);

  const handleConfirm = () => {
    if (onConfirm) {
      onConfirm(selectedRowKeys, selectedRows);
    }
    onClose();
  };

  const handleChooseOut = (item) => {
    setSelectedRowKeys(selectedRowKeys.filter((key) => key !== item.userId));
    setSelectedRows(selectedRows.filter((row) => row.userId !== item.userId));
  };

  const handleChooseIn = (keys) => {
    if (max && keys.length > max) {
      message.warning(`最多只能选择${max}人`);
      return;
    }
    setSelectedRowKeys(keys);
    setSelectedRows(queryUsers.filter((item) => keys.includes(item.userId)));
  };
  return (
    visible && (
      <Modal
        title={
          <Flex gap={GAP.SMALL} style={{ paddingBottom: GAP.MEDIUM }}>
            {title}
            {max ? (
              <Tag color={COLORS.PRIMARY}>
                {selectedRowKeys.length}/{max}
              </Tag>
            ) : null}
          </Flex>
        }
        open={visible}
        onCancel={onClose}
        onOk={handleConfirm}
        width={800}
        centered
        footer={[
          <Button key="cancel" onClick={onClose}>
            取消
          </Button>,
          <Button key="confirm" type="primary" onClick={handleConfirm}>
            确定
          </Button>,
        ]}
      >
        <ProTable
          rowKey="userId"
          form={form}
          api={apiGetUserList}
          columns={getColumns()}
          beforeSearch={(values) => ({
            ...values,
            ...(role !== undefined && { role: role }),
          })}
          afterSearch={(data) => {
            setQueryUsers([
              ...queryUsers,
              ...data.filter(
                (item) =>
                  !queryUsers.some((user) => user.userId === item.userId),
              ),
            ]);
            return data;
          }}
          extraOptions={
            <Flex gap={GAP.SMALL}>
              {selectedRows.map((item) => (
                <Tag
                  key={item.userId}
                  color={COLORS.PRIMARY}
                  closeIcon
                  onClose={() => handleChooseOut(item)}
                >
                  {item.userName}
                </Tag>
              ))}
            </Flex>
          }
          rowSelection={{
            type: selectionType,
            selectedRowKeys,
            preserveSelectedRowKeys: true,
            onChange: (keys) => handleChooseIn(keys),
            ...(getCheckboxProps && { getCheckboxProps }),
          }}
          paginationConfig={{
            pageSizeOptions: [5, 10, 20],
          }}
        />
      </Modal>
    )
  );
}
