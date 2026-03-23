import { useState, useEffect } from "react";
import { Select, Space } from "antd";
import DeviceNameTag from "../DeviceNameTag";
import RoomNumberTag from "../RoomNumberTag";
import { apiGetAllHealthDevice } from "@/services/healthDeviceApi";

export default function HealthDeviceSelect(props) {
  const {
    onChange,
    value,
    isFilterUserDevice = true,
    showFirstDevice = false,
  } = props;
  const [deviceList, setDeviceList] = useState([]);
  useEffect(() => {
    const getData = async () => {
      const { data } = await apiGetAllHealthDevice();
      if (!data) {
        setDeviceList([]);
        return;
      }
      const finalyDeviceList = isFilterUserDevice
        ? data.filter((item) => item.user == null || item.deviceId === value)
        : data;
      setDeviceList(finalyDeviceList);
      if (showFirstDevice && finalyDeviceList.length > 0) {
        onChange(finalyDeviceList[0].deviceId);
      }
    };
    getData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <Space>
      <Select
        allowClear
        placeholder={deviceList.length > 0 ? "请选择设备" : "暂无可绑定设备"}
        onChange={onChange}
        value={value}
        style={{ width: 200 }}
        {...props}
      >
        {deviceList.map((item) => (
          <Select.Option key={item.deviceId} value={item.deviceId}>
            <DeviceNameTag isHealthDevice deviceName={item.deviceName} />
          </Select.Option>
        ))}
      </Select>
    </Space>
  );
}
