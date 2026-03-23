import React, { useState } from "react";
import { apiGetUserList } from "@/services/userApi";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Modal, Button, message } from "antd";
import { getColumns } from "./constant";
import {
  apiDeleteUser,
  apiDeleteUserBatch,
} from "../../../../../services/userApi";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router";
import { ROLE } from "../../../../../constant";
const { confirm } = Modal;
export default function UserList() {
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [form] = Form.useForm();
  const navigate = useNavigate();
  // 搜索前处理
  const handleBeforeSearch = (values) => {
    const [minBirthday, maxBirthday] = values.birthday || [];
    values.birthday = undefined;
    return {
      ...values,
      minBirthday: minBirthday?.format("YYYY-MM-DD"),
      maxBirthday: maxBirthday?.format("YYYY-MM-DD"),
    };
  };
  // 删除用户
  const handleDelete = (record) => {
    confirm({
      title: "提示",
      content: `确认删除用户 ${record.userName} 吗？`,
      onOk: async () => {
        await apiDeleteUser({ userId: record.userId });
        form.getData();
      },
    });
  };
  // 添加用户
  const handleAdd = () => {
    navigate("/home/user/addUser");
  };
  // 批量删除用户
  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning("请选择要删除的账号");
      return;
    }
    confirm({
      title: "提示",
      content: `确认删除选中的 ${selectedRowKeys.length} 个账号吗？`,
      onOk: async () => {
        await apiDeleteUserBatch({ userIds: selectedRowKeys });
        setSelectedRowKeys([]);
        form.getData();
      },
    });
  };
  return (
    <Content title="用户列表">
      <ProTable
        rowKey="userId"
        rowSelection={{
          type: "checkbox",
          selectedRowKeys: selectedRowKeys,
          preserveSelectedRowKeys: true,
          getCheckboxProps: (record) => {
            return {
              disabled: record.role.roleId === ROLE.ADMIN,
            };
          },
          onChange: (selectedRowKeys, selectedRows) => {
            setSelectedRowKeys(selectedRowKeys);
            console.log(selectedRowKeys, selectedRows);
          },
        }}
        form={form}
        api={apiGetUserList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns(handleDelete)}
        extraOptions={[
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            添加
          </Button>,
          <Button
            icon={<DeleteOutlined />}
            type="primary"
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
