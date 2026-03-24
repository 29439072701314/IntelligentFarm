import React, { useState, useEffect } from "react";
import { Modal, Form, Input, Button, message } from "antd";
import { apiAddFarm, apiEditFarm } from "@/services/farmApi";

export default function FarmEditModal({ visible, onClose, onSuccess, farm }) {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (visible) {
      if (farm) {
        form.setFieldsValue({
          farmName: farm.farmName,
          address: farm.address,
        });
      } else {
        form.resetFields();
      }
    }
  }, [visible, farm, form]);

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      if (farm) {
        await apiEditFarm(farm.farmId, values);
      } else {
        await apiAddFarm(values);
      }
      message.success(farm ? "编辑成功" : "新增成功");
      onSuccess();
    } catch (error) {
      message.error(error.response?.data?.message || "操作失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      title={farm ? "编辑农场" : "新增农场"}
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
          name="farmName"
          label="农场名称"
          rules={[{ required: true, message: "请输入农场名称" }]}
        >
          <Input placeholder="请输入农场名称" />
        </Form.Item>
        <Form.Item
          name="address"
          label="地址"
          rules={[{ required: true, message: "请输入地址" }]}
        >
          <Input placeholder="请输入地址" />
        </Form.Item>
      </Form>
    </Modal>
  );
}