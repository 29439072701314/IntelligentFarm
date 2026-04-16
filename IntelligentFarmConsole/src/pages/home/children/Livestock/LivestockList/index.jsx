import React, { useState, useEffect, useRef } from "react";
import ProTable from "@/component/ProTable";
import Content from "@/component/Content";
import { Form, Button, App, Modal, Input, Spin } from "antd";
import { getColumns } from "./constant";
import { PlusOutlined } from "@ant-design/icons";
import LivestockEditModal from "./component/LivestockEditModal";
import LivestockDetailModal from "./component/LivestockDetailModal";
import { apiGetLivestockList, apiInStock, apiOutStock, apiEditLivestock } from "@/services/livestockApi";
import { apiGetWeightDeviceByDeviceName,apiControlDevice } from "@/services/deviceApi";
import { useLocation } from "react-router";

export default function LivestockList() {
  const [form] = Form.useForm();
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [currentLivestock, setCurrentLivestock] = useState(null);
  const [inStockModalVisible, setInStockModalVisible] = useState(false);
  const [outStockModalVisible, setOutStockModalVisible] = useState(false);
  const [weighingModalVisible, setWeighingModalVisible] = useState(false);
  const [currentOperationLivestock, setCurrentOperationLivestock] = useState(null);
  const [operator, setOperator] = useState('');
  const [remark, setRemark] = useState('');
  const [currentWeight, setCurrentWeight] = useState('');
  const [isWeighing, setIsWeighing] = useState(false);
  const weighingIntervalRef = useRef(null);
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

  // 处理称重操作
  const handleWeighing = (record) => {
    setCurrentOperationLivestock(record);
    setCurrentWeight('');
    setRemark('');
    setIsWeighing(false);
    setWeighingModalVisible(true);
  };

  // 开始称重，轮询获取重量传感器数据
  const startWeighing = async () => {
    try {
      // 调用控制接口，开始称重
      await apiControlDevice({ message: 'weight=true', device: 'farm001' });
      message.info('开始称重指令已发送');
    } catch (error) {
      console.error('发送开始称重指令失败:', error);
      message.error('发送开始称重指令失败');
      return;
    }
    
    setIsWeighing(true);
    // 清除之前的定时器（如果有）
    if (weighingIntervalRef.current) {
      clearInterval(weighingIntervalRef.current);
    }
    // 每1秒调用一次API
    weighingIntervalRef.current = setInterval(async () => {
      try {
        const response = await apiGetWeightDeviceByDeviceName('farm001');
        if (response.code === 200 && response.data) {
          const weightDevice = response.data;
          const currentTime = Date.now();
          const deviceTime = weightDevice.time;
          // 判断是否是10秒内的最新数据
          if (currentTime - deviceTime < 10000) {
            // 更新表单中的体重
            setCurrentWeight(weightDevice.weight);
            // 停止轮询
            clearInterval(weighingIntervalRef.current);
            setIsWeighing(false);
            message.success('称重成功');
          }
        }
      } catch (error) {
        console.error('获取重量传感器数据失败:', error);
      }
    }, 1000);
  };

  // 取消称重
  const cancelWeighing = async () => {
    
    // 清除定时器
    if (weighingIntervalRef.current) {
      clearInterval(weighingIntervalRef.current);
    }
    // 重置状态
    setIsWeighing(false);
  };

  // 清除定时器
  useEffect(() => {
    return () => {
      if (weighingIntervalRef.current) {
        clearInterval(weighingIntervalRef.current);
      }
    };
  }, []);

  // 执行称重操作
  const handleWeighingSubmit = async () => {
    if (!currentWeight) {
      message.error('请输入体重');
      return;
    }
    try {
      // 调用编辑API更新牲畜体重
      await apiEditLivestock(currentOperationLivestock.livestockId, {
        weight: currentWeight
      });
      message.success('称重记录保存成功');
      setWeighingModalVisible(false);
      form.getData(); // 重新加载数据
    } catch (error) {
      message.error(error.response?.data?.message || '称重记录保存失败');
    }
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
        columns={getColumns(handleEdit, null, handleDetail, handleInStock, handleOutStock, handleWeighing)}
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
      {/* 称重模态框 */}
      <Modal
        title="牲畜称重"
        open={weighingModalVisible}
        onOk={handleWeighingSubmit}
        onCancel={() => {
          if (weighingIntervalRef.current) {
            clearInterval(weighingIntervalRef.current);
          }
          setIsWeighing(false);
          setWeighingModalVisible(false);
        }}
      >
        <div style={{ marginBottom: 16 }}>
          <label style={{ display: 'block', marginBottom: 8 }}>牲畜名称</label>
          <Input
            value={currentOperationLivestock?.livestockName}
            disabled
          />
        </div>
        <div style={{ marginBottom: 16 }}>
          <label style={{ display: 'block', marginBottom: 8 }}>当前体重(kg)</label>
          <div style={{ display: 'flex', gap: 8 }}>
            <Input
              value={currentWeight}
              onChange={(e) => setCurrentWeight(e.target.value)}
              placeholder="请输入体重"
              type="number"
              disabled={isWeighing}
              style={{ flex: 1 }}
            />
            <Button 
              type={isWeighing ? "default" : "primary"} 
              onClick={isWeighing ? cancelWeighing : startWeighing}
            >
              {isWeighing ? "取消称重" : "开始称重"}
            </Button>
          </div>
          {isWeighing && (
            <div style={{ marginTop: 8, display: 'flex', alignItems: 'center', color: '#1890ff' }}>
              <Spin size="small" style={{ marginRight: 8 }} />
              称重中...
            </div>
          )}
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 8 }}>备注</label>
          <Input.TextArea
            value={remark}
            onChange={(e) => setRemark(e.target.value)}
            placeholder="请输入备注"
            rows={3}
          />
        </div>
      </Modal>
    </Content>
  );
}