import { useState, useEffect } from "react";
import { Select, Space } from "antd";
import { apiGetAllDevice } from "@/services/deviceApi";
import DeviceNameTag from "../DeviceNameTag";
import FarmTag from "../FarmTag";

export default function DeviceSelect(props) {
  const {
    onChange,
    value,
    isFilterFarmDevice = true,
    showFirstDevice = false,
    // 是否显示绑定的农场
    showFarm = false,
  } = props;
  const [deviceList, setDeviceList] = useState([]);
  useEffect(() => {
    const getData = async () => {
      const { data } = await apiGetAllDevice();
      if (!data) {
        setDeviceList([]);
        return;
      }
      const finalDeviceList = isFilterFarmDevice
        ? data.filter((item) => item.farm == null || item.deviceId === value)
        : data;
      setDeviceList(finalDeviceList);
      if (showFirstDevice && finalDeviceList.length > 0) {
        onChange(finalDeviceList[0].deviceId);
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
      >
        {deviceList.map((item) => (
          <Select.Option key={item.deviceId} value={item.deviceId}>
            <DeviceNameTag deviceName={item.deviceName} />
          </Select.Option>
        ))}
      </Select>
      {showFarm && (
        <FarmTag
          farmName={
            deviceList.find((item) => item.deviceId === value)?.farm
              ?.farmName || "未绑定"
          }
          color={
            deviceList.find((item) => item.deviceId === value)?.farm
              ? "blue"
              : "orange"
          }
        />
      )}
    </Space>
  );
}
