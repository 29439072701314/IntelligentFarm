import React, { useState, useEffect } from "react";
import { Modal, Form, Input, Button, message, Select } from "antd";

const { Option } = Select;
import { apiAddLivestock, apiEditLivestock } from "@/services/livestockApi";

export default function LivestockEditModal({ visible, onClose, onSuccess, livestock, farmId }) {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (visible) {
      if (livestock) {
        form.setFieldsValue({
          livestockName: livestock.livestockName,
          livestockType: livestock.livestockType,
          healthStatus: livestock.healthStatus || "健康"
        });
      } else {
        form.resetFields();
      }
    }
  }, [visible, livestock, form]);

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      if (livestock) {
        await apiEditLivestock(livestock.livestockId, values);
      } else {
        await apiAddLivestock({ ...values, farmId });
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
      title={livestock ? "编辑牲畜" : "新增牲畜"}
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
        </Button>,
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
      </Form>
    </Modal>
  );
}