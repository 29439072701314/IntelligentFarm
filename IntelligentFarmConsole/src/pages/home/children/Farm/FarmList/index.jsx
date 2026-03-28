import React, { useState } from "react";
import { useNavigate } from "react-router";
import { apiGetFarmList, apiDeleteFarm } from "@/services/farmApi";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Modal, Button, message } from "antd";
import { getColumns } from "./constant";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import FarmEditModal from "./component/FarmEditModal";

const { confirm } = Modal;

export default function FarmList() {
  const navigate = useNavigate();
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [form] = Form.useForm();
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [currentFarm, setCurrentFarm] = useState(null);

  // 搜索前处理
  const handleBeforeSearch = (values) => {
    return {
      ...values,
    };
  };

  // 删除农场
  const handleDelete = (record) => {
    confirm({
      title: "提示",
      content: `确认删除农场 ${record.farmName} 吗？`,
      onOk: async () => {
        try {
          await apiDeleteFarm(record.farmId);
          form.getData();
        } catch (error) {
          message.error(error.response?.data?.message || "删除失败");
        }
      },
    });
  };

  // 添加农场
  const handleAdd = () => {
    setCurrentFarm(null);
    setEditModalVisible(true);
  };

  // 编辑农场
  const handleEdit = (record) => {
    setCurrentFarm(record);
    setEditModalVisible(true);
  };

  // 查看牲畜
  const handleViewLivestock = (record) => {
    navigate(`/home/livestock/livestockList?farmId=${record.farmId}`);
  };

  // 批量删除农场
  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning("请选择要删除的农场");
      return;
    }
    confirm({
      title: "提示",
      content: `确认删除选中的 ${selectedRowKeys.length} 个农场吗？`,
      onOk: async () => {
        try {
          for (const farmId of selectedRowKeys) {
            await apiDeleteFarm(farmId);
          }
          setSelectedRowKeys([]);
          form.getData();
        } catch (error) {
          message.error(error.response?.data?.message || "批量删除失败");
        }
      },
    });
  };

  return (
    <Content title="农场列表">
      <ProTable
        rowKey="farmId"
        rowSelection={{
          type: "checkbox",
          selectedRowKeys: selectedRowKeys,
          preserveSelectedRowKeys: true,
          onChange: (selectedRowKeys, selectedRows) => {
            setSelectedRowKeys(selectedRowKeys);
          },
        }}
        form={form}
        api={apiGetFarmList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns(handleEdit, handleDelete, handleViewLivestock)}
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
      <FarmEditModal
        visible={editModalVisible}
        onClose={() => setEditModalVisible(false)}
        onSuccess={() => {
          setEditModalVisible(false);
          form.getData();
        }}
        farm={currentFarm}
      />
    </Content>
  );
}