import React, { useState, useEffect } from "react";
import { Modal, Descriptions, Button, Spin, Tag } from "antd";
import { apiGetLivestockDetail } from "@/services/livestockApi";

export default function LivestockDetailModal({ visible, onClose, livestockId }) {
  const [loading, setLoading] = useState(false);
  const [livestockDetail, setLivestockDetail] = useState(null);

  useEffect(() => {
    if (visible && livestockId) {
      fetchLivestockDetail();
    }
  }, [visible, livestockId]);

  const fetchLivestockDetail = async () => {
    setLoading(true);
    try {
      const res = await apiGetLivestockDetail(livestockId);
      setLivestockDetail(res.data);
    } catch (error) {
      console.error("获取牲畜详情失败:", error);
    } finally {
      setLoading(false);
    }
  };

  const getHealthStatusColor = (status) => {
    if (status === '患病') return 'red';
    if (status === '治疗中') return 'orange';
    if (status === '亚健康') return 'yellow';
    return 'green';
  };

  if (loading) {
    return (
      <Modal
        title="牲畜详情"
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
      title="牲畜详情"
      open={visible}
      onCancel={onClose}
      footer={[
        <Button key="close" onClick={onClose}>
          关闭
        </Button>,
      ]}
      width={600}
    >
      {livestockDetail && (
        <Descriptions bordered column={2}>
          <Descriptions.Item label="牲畜编码">
            {livestockDetail.livestockCode}
          </Descriptions.Item>
          <Descriptions.Item label="农场ID">
            {livestockDetail.farmId}
          </Descriptions.Item>
          <Descriptions.Item label="牲畜类型">
            {livestockDetail.type}
          </Descriptions.Item>
          <Descriptions.Item label="健康状态">
            <Tag color={getHealthStatusColor(livestockDetail.healthStatus)}>
              {livestockDetail.healthStatus}
            </Tag>
          </Descriptions.Item>
          <Descriptions.Item label="体重(kg)">
            {livestockDetail.weight}
          </Descriptions.Item>
          <Descriptions.Item label="状态">
            {livestockDetail.status === 0 ? '在栏' : '出栏'}
          </Descriptions.Item>
          <Descriptions.Item label="入场时间" span={2}>
            {livestockDetail.inTime ? new Date(livestockDetail.inTime).toLocaleString() : '-'}
          </Descriptions.Item>
          <Descriptions.Item label="出场时间" span={2}>
            {livestockDetail.outTime ? new Date(livestockDetail.outTime).toLocaleString() : '-'}
          </Descriptions.Item>
        </Descriptions>
      )}
    </Modal>
  );
}