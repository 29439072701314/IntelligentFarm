import React, { useState, useEffect } from "react";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Modal, Button, message } from "antd";
import { getColumns } from "./constant";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import LivestockEditModal from "./component/LivestockEditModal";
import LivestockDetailModal from "./component/LivestockDetailModal";
import { apiGetLivestockList, apiDeleteLivestock } from "@/services/livestockApi";
import { useLocation } from "react-router";

const { confirm } = Modal;

export default function LivestockList() {
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [form] = Form.useForm();
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [currentLivestock, setCurrentLivestock] = useState(null);
  const location = useLocation();
  const [farmId, setFarmId] = useState(null);

  useEffect(() => {
    // 从URL参数中获取farmId
    const searchParams = new URLSearchParams(location.search);
    const id = searchParams.get('farmId');
    if (id) {
      setFarmId(parseInt(id));
    }
  }, [location.search]);

  // 搜索前处理
  const handleBeforeSearch = (values) => {
    return {
      ...values,
      farmId: farmId,
    };
  };

  // 删除牲畜
  const handleDelete = (record) => {
    confirm({
      title: "提示",
      content: `确认删除牲畜 ${record.livestockName} 吗？`,
      onOk: async () => {
        try {
          await apiDeleteLivestock(record.livestockId);
          form.getData();
        } catch (error) {
          message.error(error.response?.data?.message || "删除失败");
        }
      },
    });
  };

  // 添加牲畜
  const handleAdd = () => {
    setCurrentLivestock(null);
    setEditModalVisible(true);
  };

  // 编辑牲畜
  const handleEdit = (record) => {
    setCurrentLivestock(record);
    setEditModalVisible(true);
  };

  // 查看牲畜详情
  const handleDetail = (record) => {
    setCurrentLivestock(record);
    setDetailModalVisible(true);
  };

  // 批量删除牲畜
  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning("请选择要删除的牲畜");
      return;
    }
    confirm({
      title: "提示",
      content: `确认删除选中的 ${selectedRowKeys.length} 个牲畜吗？`,
      onOk: async () => {
        try {
          for (const livestockId of selectedRowKeys) {
            await apiDeleteLivestock(livestockId);
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
    <Content title={farmId ? "农场牲畜列表" : "牲畜列表"}>
      <ProTable
        rowKey="livestockId"
        rowSelection={{
          type: "checkbox",
          selectedRowKeys: selectedRowKeys,
          preserveSelectedRowKeys: true,
          onChange: (selectedRowKeys, selectedRows) => {
            setSelectedRowKeys(selectedRowKeys);
          },
        }}
        form={form}
        api={apiGetLivestockList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns(handleEdit, handleDelete, handleDetail)}
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
      <LivestockEditModal
        visible={editModalVisible}
        onClose={() => setEditModalVisible(false)}
        onSuccess={() => {
          setEditModalVisible(false);
          form.getData();
        }}
        livestock={currentLivestock}
        farmId={farmId}
      />
      <LivestockDetailModal
        visible={detailModalVisible}
        onClose={() => setDetailModalVisible(false)}
        livestockId={currentLivestock?.livestockId}
      />
    </Content>
  );
}