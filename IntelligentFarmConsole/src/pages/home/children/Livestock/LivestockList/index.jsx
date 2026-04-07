import React, { useState, useEffect } from "react";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Button, App, Modal, Input } from "antd";
import { getColumns } from "./constant";
import { PlusOutlined } from "@ant-design/icons";
import LivestockEditModal from "./component/LivestockEditModal";
import LivestockDetailModal from "./component/LivestockDetailModal";
import { apiGetLivestockList, apiInStock, apiOutStock } from "@/services/livestockApi";
import { useLocation } from "react-router";

export default function LivestockList() {
  const [form] = Form.useForm();
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [currentLivestock, setCurrentLivestock] = useState(null);
  const [inStockModalVisible, setInStockModalVisible] = useState(false);
  const [outStockModalVisible, setOutStockModalVisible] = useState(false);
  const [currentOperationLivestock, setCurrentOperationLivestock] = useState(null);
  const [operator, setOperator] = useState('');
  const [remark, setRemark] = useState('');
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

  // 处理入库操作
  const handleInStock = (record) => {
    setCurrentOperationLivestock(record);
    setOperator('');
    setRemark('');
    setInStockModalVisible(true);
  };

  // 处理出库操作
  const handleOutStock = (record) => {
    setCurrentOperationLivestock(record);
    setOperator('');
    setRemark('');
    setOutStockModalVisible(true);
  };

  // 执行入库操作
  const handleInStockSubmit = async () => {
    if (!operator) {
      message.error('请输入操作人');
      return;
    }
    try {
      await apiInStock(currentOperationLivestock.livestockId, operator, remark);
      message.success('入库成功');
      setInStockModalVisible(false);
      form.getData(); // 重新加载数据
    } catch (error) {
      message.error(error.response?.data?.message || '入库失败');
    }
  };

  // 执行出库操作
  const handleOutStockSubmit = async () => {
    if (!operator) {
      message.error('请输入操作人');
      return;
    }
    try {
      await apiOutStock(currentOperationLivestock.livestockId, operator, remark);
      message.success('出库成功');
      setOutStockModalVisible(false);
      form.getData(); // 重新加载数据
    } catch (error) {
      message.error(error.response?.data?.message || '出库失败');
    }
  };

  return (
    <Content title={farmId ? "农场牲畜列表" : "牲畜列表"}>
      <ProTable
        rowKey="livestockId"
        form={form}
        api={apiGetLivestockList}
        beforeSearch={handleBeforeSearch}
        columns={getColumns(handleEdit, null, handleDetail, handleInStock, handleOutStock)}
        extraOptions={[
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            {farmId ? "入库" : "添加"}
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
      {/* 入库模态框 */}
      <Modal
        title="牲畜入库"
        open={inStockModalVisible}
        onOk={handleInStockSubmit}
        onCancel={() => setInStockModalVisible(false)}
      >
        <div style={{ marginBottom: 16 }}>
          <label style={{ display: 'block', marginBottom: 8 }}>操作人</label>
          <Input
            value={operator}
            onChange={(e) => setOperator(e.target.value)}
            placeholder="请输入操作人"
          />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 8 }}>备注</label>
          <Input.TextArea
            value={remark}
            onChange={(e) => setRemark(e.target.value)}
            placeholder="请输入备注"
            rows={4}
          />
        </div>
      </Modal>
      {/* 出库模态框 */}
      <Modal
        title="牲畜出库"
        open={outStockModalVisible}
        onOk={handleOutStockSubmit}
        onCancel={() => setOutStockModalVisible(false)}
      >
        <div style={{ marginBottom: 16 }}>
          <label style={{ display: 'block', marginBottom: 8 }}>操作人</label>
          <Input
            value={operator}
            onChange={(e) => setOperator(e.target.value)}
            placeholder="请输入操作人"
          />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 8 }}>备注</label>
          <Input.TextArea
            value={remark}
            onChange={(e) => setRemark(e.target.value)}
            placeholder="请输入备注"
            rows={4}
          />
        </div>
      </Modal>
    </Content>
  );
}