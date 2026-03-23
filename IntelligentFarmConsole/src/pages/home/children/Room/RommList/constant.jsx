import { Button, Space, Tag } from "antd";
import { FieldNumberOutlined } from "@ant-design/icons";
import RoomNumberTag from "../../../../../component/RoomNumberTag";
import DeviceNameTag from "../../../../../component/DeviceNameTag";
import RateTag from "../../../../../component/RateTag";
import { COLORS } from "../../../../../constant";

export const getColumns = (handleDelete, handleEdit) => [
  {
    title: "房间号",
    dataIndex: "roomNumber",
    key: "roomNumber",
    render: (roomNumber) => <RoomNumberTag roomNumber={roomNumber} />,
    sorter: { multiple: 1 },
  },
  {
    title: "设备",
    dataIndex: "device",
    key: "deviceName",
    render: (device) => <DeviceNameTag deviceName={device?.deviceName} />,
    formItemProps: {
      render: false,
    },
  },
  {
    title: "房间容量",
    dataIndex: "maxCapacity",
    key: "maxCapacity",
    render: (maxCapacity) => `${maxCapacity || 0} 人`,
    formItemProps: {
      render: false,
    },
    sorter: { multiple: 2 },
  },
  {
    title: "已住人数",
    dataIndex: "currentCount",
    key: "currentCount",
    render: (currentCount) => `${currentCount || 0} 人`,
    formItemProps: {
      render: false,
    },
    sorter: { multiple: 3 },
  },
  {
    title: "入住率",
    dataIndex: "occupancyRate",
    key: "occupancyRate",
    render: (_occupancyRate, record) => (
      <RateTag
        current={record.currentCount || 0}
        showPercentage
        total={record.maxCapacity || 0}
        variant="solid"
      />
    ),
    formItemProps: {
      render: false,
    },
    sorter: { multiple: 4 },
  },
  {
    title: "操作",
    key: "operation",
    render: (text, record) => (
      <Space>
        <Button type="primary" danger onClick={() => handleDelete(record)}>
          删除
        </Button>
        <Button type="primary" onClick={() => handleEdit(record)}>
          编辑
        </Button>
      </Space>
    ),
  },
];
