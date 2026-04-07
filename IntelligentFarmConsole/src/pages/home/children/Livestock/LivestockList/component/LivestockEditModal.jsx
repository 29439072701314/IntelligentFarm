import React, { useState, useEffect } from "react";
import { Modal, Form, Input, Button, message, Select } from "antd";

const { Option } = Select;
import { apiAddLivestock, apiEditLivestock } from "@/services/livestockApi";
import { apiAddWeightRecord } from "@/services/livestockWeightApi";

export default function LivestockEditModal({ visible, onClose, onSuccess, livestock, farmId }) {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [oldWeight, setOldWeight] = useState(null);

  useEffect(() => {
    if (visible) {
      if (livestock) {
        setOldWeight(livestock.weight);
        form.setFieldsValue({
          livestockName: livestock.livestockName,
          livestockType: livestock.livestockType,
          healthStatus: livestock.healthStatus || "健康",
          weight: livestock.weight,
          farmId: livestock.farmId
        });
      } else {
        setOldWeight(null);
        form.resetFields();
      }
    }
  }, [visible, livestock, form]);

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      if (livestock) {
        // 编辑牲畜
        await apiEditLivestock(livestock.livestockId, values);
        
        // 如果体重发生变化，添加体重记录
        if (values.weight !== oldWeight) {
          await apiAddWeightRecord({
            livestockId: livestock.livestockId,
            weight: values.weight
          });
        }
      } else {
        // 新增牲畜
        const result = await apiAddLivestock({ ...values, farmId });
        
        // 如果有体重值，添加体重记录
        if (values.weight) {
          await apiAddWeightRecord({
            livestockId: result.data.livestockId,
            weight: values.weight
          });
        }
      }
      message.success(livestock ? "编辑成功" : "新增成功");
      onSuccess();
    } catch (error) {
      message.error(error.response?.data?.message || "操作失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      title={livestock ? "编辑牲畜" : (farmId ? "牲畜入库" : "新增牲畜")}
      open={visible}
      onCancel={onClose}
      footer={[
        <Button key="cancel" onClick={onClose}>
          取消
        </Button>,
        <Button
          key="submit"
          type="primary"
          loading={loading}
          onClick={() => form.submit()}
        >
          确定
        </Button>
      ]}
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
      >
        <Form.Item
          name="livestockName"
          label="牲畜名称"
          rules={[{ required: true, message: "请输入牲畜名称" }]}
        >
          <Input placeholder="请输入牲畜名称" />
        </Form.Item>
        <Form.Item
          name="livestockType"
          label="牲畜类型"
          rules={[{ required: true, message: "请输入牲畜类型" }]}
        >
          <Input placeholder="请输入牲畜类型" />
        </Form.Item>
        <Form.Item
          name="weight"
          label="体重 (kg)"
          rules={[{ required: false, message: "请输入体重" }]}
        >
          <Input type="number" placeholder="请输入体重" step="0.1" />
        </Form.Item>
        <Form.Item
          name="healthStatus"
          label="健康状态"
          rules={[{ required: true, message: "请选择健康状态" }]}
        >
          <Select placeholder="请选择健康状态">
            <Option value="健康">健康</Option>
            <Option value="亚健康">亚健康</Option>
            <Option value="患病">患病</Option>
            <Option value="治疗中">治疗中</Option>
          </Select>
        </Form.Item>
        <Form.Item
          name="farmId"
          hidden
        >
          <Input />
        </Form.Item>
      </Form>
    </Modal>
  );
}