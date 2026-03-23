import { useState, useEffect } from "react";
import { Select, Space } from "antd";
import { apiGetAllDevice } from "@/services/deviceApi";
import DeviceNameTag from "../DeviceNameTag";
import RoomNumberTag from "../RoomNumberTag";

export default function DeviceSelect(props) {
  const {
    onChange,
    value,
    isFilterRoomDevice = true,
    showFirstDevice = false,
    // 是否显示绑定的房间号
    showRoom = false,
  } = props;
  const [deviceList, setDeviceList] = useState([]);
  useEffect(() => {
    const getData = async () => {
      const { data } = await apiGetAllDevice();
      if (!data) {
        setDeviceList([]);
        return;
      }
      const finalyDeviceList = isFilterRoomDevice
        ? data.filter((item) => item.room == null || item.deviceId === value)
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
            <DeviceNameTag deviceName={item.deviceName} />
          </Select.Option>
        ))}
      </Select>
      {showRoom && (
        <RoomNumberTag
          roomNumber={
            deviceList.find((item) => item.deviceId === value)?.room
              ?.roomNumber || "未绑定"
          }
          color={
            deviceList.find((item) => item.deviceId === value)?.room
              ? "blue"
              : "orange"
          }
        />
      )}
    </Space>
  );
}
