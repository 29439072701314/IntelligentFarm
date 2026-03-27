import { Button, Space } from "antd";
import { LinkOutlined, DisconnectOutlined } from "@ant-design/icons";
import FarmTag from "../../../../component/FarmTag";

export const getColumns = ({ onBind, onUnbind }) => [
  {
    title: "设备名称",
    dataIndex: "deviceName",
    key: "deviceName",
  },
  {
    title: "所属农场",
    dataIndex: "farm",
    key: "farm",
    render: (farm) => <FarmTag farmName={farm?.farmName} color={farm?.farmName ? "blue" : "orange"} />,
  },
  {
    title: "温度",
    dataIndex: "temperature",
    key: "temperature",
    render: (text) => `${text}℃`,
  },
  {
    title: "湿度",
    dataIndex: "humidity",
    key: "humidity",
    render: (text) => `${text}%`,
  },
  {
    title: "气体浓度",
    dataIndex: "gasConcentration",
    key: "gasConcentration",
    render: (text) => `${text}ppm`,
  },
  {
    title: "操作",
    key: "action",
    render: (_, record) => (
      <Space>
        <Button
          type="link"
          icon={<LinkOutlined />}
          onClick={() => onBind(record)}
        >
          {record.farm ? "更换农场" : "绑定农场"}
        </Button>
        {record.farm && (
          <Button
            type="link"
            danger
            icon={<DisconnectOutlined />}
            onClick={() => onUnbind(record)}
          >
            解绑
          </Button>
        )}
      </Space>
    ),
  },
];
