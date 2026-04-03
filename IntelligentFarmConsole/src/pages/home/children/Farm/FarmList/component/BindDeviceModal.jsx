import React, { useState, useEffect } from "react";
import { Modal, Form, Select, Button, message } from "antd";
import { apiDeviceList, apiBindDeviceToFarm } from "@/services/deviceApi";

const { Option } = Select;

export default function BindDeviceModal({ visible, onClose, onSuccess, farmId }) {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [deviceOptions, setDeviceOptions] = useState([]);

  // 加载未绑定的设备列表
  useEffect(() => {
    if (visible) {
      loadDeviceOptions();
    }
  }, [visible]);

  const loadDeviceOptions = async () => {
    try {
      const res = await apiDeviceList({ pageNumber: 1, pageSize: 100 });
      const list = res.data.content || res.data.list || [];
      // 过滤出未绑定的设备
      const unboundDevices = list.filter(device => !device.farmId || device.farmId === 0);
      setDeviceOptions(unboundDevices.map(device => ({ label: device.deviceName, value: device.deviceId })));
    } catch (error) {
      message.error(error.response?.data?.message || "获取设备列表失败");
    }
  };

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      await apiBindDeviceToFarm({ deviceId: values.deviceId, farmId });
      message.success("绑定设备成功");
      onSuccess();
    } catch (error) {
      message.error(error.response?.data?.message || "绑定设备失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      title="绑定设备"
      open={visible}
      onCancel={onClose}
      footer={[
        <Button key="cancel" onClick={onClose}>
          取消
        </Button>,
        <Button key="submit" type="primary" loading={loading} onClick={() => form.submit()}>
          确定
        </Button>,
      ]}
    >
      <Form form={form} layout="vertical" onFinish={handleSubmit}>
        <Form.Item
          name="deviceId"
          label="设备"
          rules={[{ required: true, message: "请选择设备" }]}
        >
          <Select placeholder="请选择设备">
            {deviceOptions.map(option => (
              <Option key={option.value} value={option.value}>
                {option.label}
              </Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
}
