import React, { useState, useEffect } from "react";
import { Modal, Table, Descriptions, Button, Spin, Space } from "antd";
import { apiGetFarmDetail } from "@/services/farmApi";
import { useNavigate } from "react-router";

export default function FarmDetailModal({ visible, onClose, farmId }) {
  const [loading, setLoading] = useState(false);
  const [farmDetail, setFarmDetail] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (visible && farmId) {
      fetchFarmDetail();
    }
  }, [visible, farmId]);

  const fetchFarmDetail = async () => {
    setLoading(true);
    try {
      const res = await apiGetFarmDetail(farmId);
      setFarmDetail(res.data);
    } catch (error) {
      console.error("获取农场详情失败:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleViewLivestock = () => {
    onClose();
    navigate(`/home/livestock/livestockList?farmId=${farmId}`);
  };

  const livestockColumns = [
    {
      title: "牲畜名称",
      dataIndex: "livestockName",
      key: "livestockName",
    },
    {
      title: "牲畜类型",
      dataIndex: "livestockType",
      key: "livestockType",
    },
  ];

  const deviceColumns = [
    {
      title: "设备名称",
      dataIndex: "deviceName",
      key: "deviceName",
    },
  ];

  if (loading) {
    return (
      <Modal
        title="农场详情"
        open={visible}
        onCancel={onClose}
        footer={[
          <Button key="close" onClick={onClose}>
            关闭
          </Button>,
        ]}
      >
        <div style={{ textAlign: "center", padding: "20px" }}>
          <Spin size="large" />
        </div>
      </Modal>
    );
  }

  return (
    <Modal
      title="农场详情"
      open={visible}
      onCancel={onClose}
      footer={[
        <Button key="close" onClick={onClose}>
          关闭
        </Button>,
      ]}
      width={800}
    >
      {farmDetail && (
        <div>
          <Descriptions bordered>
            <Descriptions.Item label="农场名称">
              {farmDetail.farm.farmName}
            </Descriptions.Item>
            <Descriptions.Item label="地址">
              {farmDetail.farm.address}
            </Descriptions.Item>
          </Descriptions>
          
          <Space style={{ marginBottom: "10px" }}>
            <h3 style={{ marginTop: "20px" }}>关联牲畜</h3>
            <Button type="link" onClick={handleViewLivestock}>查看全部</Button>
          </Space>
          <Table
            dataSource={farmDetail.livestockList || []}
            columns={livestockColumns}
            rowKey="livestockId"
            pagination={false}
          />
          
          <h3 style={{ marginTop: "20px" }}>关联设备</h3>
          <Table
            dataSource={farmDetail.deviceList || []}
            columns={deviceColumns}
            rowKey="deviceId"
            pagination={false}
          />
        </div>
      )}
    </Modal>
  );
}