import React, { useState, useEffect } from "react";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Button, App } from "antd";
import { getColumns } from "./constant";
import { PlusOutlined } from "@ant-design/icons";
import LivestockEditModal from "./component/LivestockEditModal";
import LivestockDetailModal from "./component/LivestockDetailModal";
import { apiGetLivestockList } from "@/services/livestockApi";
import { useLocation } from "react-router";

export default function LivestockList() {
  const [form] = Form.useForm();
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [currentLivestock, setCurrentLivestock] = useState(null);
  const location = useLocation();
  const [farmId, setFarmId] = useState(null);
  const { message } = App.useApp();

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



  return (
    <Content title={farmId ? "农场牲畜列表" : "牲畜列表"}>
      <ProTable
        rowKey="livestockId"
        form={form}
        api={apiGetLivestockList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns(handleEdit, null, handleDetail)}
        extraOptions={[
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            添加
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