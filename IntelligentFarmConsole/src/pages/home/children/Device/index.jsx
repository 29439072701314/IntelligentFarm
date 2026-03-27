import React, { useState } from "react";
import Content from "@/component/Content";
import ProTable from "../../../../component/ProTable";
import { Form, Button, Modal, message } from "antd";
import { apiDeviceList, apiBindDeviceToFarm, apiUnbindDeviceFromFarm } from "../../../../services/deviceApi";
import { getColumns } from "./constant";
import DeviceCard from "./component/DeviceCard";
import FarmSelect from "../../../../component/FarmSelect";
import { LinkOutlined, DisconnectOutlined } from "@ant-design/icons";

export default function Device() {
  const [form] = Form.useForm();
  const [bindModalVisible, setBindModalVisible] = useState(false);
  const [currentDevice, setCurrentDevice] = useState(null);
  const [selectedFarmId, setSelectedFarmId] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleBind = (record) => {
    setCurrentDevice(record);
    setSelectedFarmId(record.farm?.farmId);
    setBindModalVisible(true);
  };

  const handleUnbind = async (record) => {
    try {
      const res = await apiUnbindDeviceFromFarm({ deviceId: record.deviceId });
      if (res.code === 200) {
        message.success("解绑成功");
        form.submit();
      } else {
        message.error(res.message || "解绑失败");
      }
    } catch (error) {
      message.error("解绑失败");
    }
  };

  const handleBindConfirm = async () => {
    if (!selectedFarmId) {
      message.error("请选择农场");
      return;
    }
    setLoading(true);
    try {
      const res = await apiBindDeviceToFarm({
        deviceId: currentDevice.deviceId,
        farmId: selectedFarmId,
      });
      if (res.code === 200) {
        message.success("绑定成功");
        setBindModalVisible(false);
        form.submit();
      } else {
        message.error(res.message || "绑定失败");
      }
    } catch (error) {
      message.error("绑定失败");
    } finally {
      setLoading(false);
    }
  };

  const columns = getColumns({
    onBind: handleBind,
    onUnbind: handleUnbind,
  });

  return (
    <Content title="设备中心">
      <ProTable
        rowKey="deviceId"
        form={form}
        api={apiDeviceList}
        columns={columns}
        paginationConfig={{
          align: "start",
          pageSizeOptions: [8, 12, 16, 20],
        }}
        masonryConfig={{
          columns: {
            md: 2,
            lg: 2,
            xl: 3,
            xxl: 4,
          },
          itemRender: (item) => <DeviceCard item={item} />,
        }}
      />
      <Modal
        title="绑定设备到农场"
        open={bindModalVisible}
        onOk={handleBindConfirm}
        onCancel={() => setBindModalVisible(false)}
        confirmLoading={loading}
      >
        <p>设备名称: {currentDevice?.deviceName}</p>
        <FarmSelect
          value={selectedFarmId}
          onChange={setSelectedFarmId}
          placeholder="请选择要绑定的农场"
        />
      </Modal>
    </Content>
  );
}
