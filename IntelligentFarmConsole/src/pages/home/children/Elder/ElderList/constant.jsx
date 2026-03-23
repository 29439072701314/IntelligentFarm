import { Avatar, DatePicker, Flex, Button, Tag } from "antd";
import { BASE_URL } from "@/utils/request";
import { GENDER_LIST } from "@/component/GenderSelect/constant";
import GenderSelect from "@/component/GenderSelect";
import GenderTag from "../../../../../component/GenderTag";
import RoomNumberTag from "../../../../../component/RoomNumberTag";
import { GAP } from "../../../../../constant";
import DeviceNameTag from "../../../../../component/DeviceNameTag";
const { RangePicker } = DatePicker;
export const getColumns = (handleOptionClick) => [
  {
    title: "头像",
    dataIndex: "avatar",
    key: "avatar",
    width: 200,
    render: (url, record) => (
      <Flex align="center" gap={GAP.SMALL}>
        <Avatar src={BASE_URL + url} />
        {record?.room?.roomNumber && (
          <RoomNumberTag roomNumber={record?.room?.roomNumber} />
        )}
      </Flex>
    ),
    formItemProps: {
      render: false,
    },
  },
  {
    title: "用户名",
    dataIndex: "userName",
    key: "userName",
  },
  {
    title: "手机号",
    dataIndex: "phone",
    key: "phone",
  },
  {
    title: "性别",
    dataIndex: "gender",
    key: "gender",
    render: (gender) => <GenderTag gender={gender} />,
    formItemProps: {
      render: () => (
        <GenderSelect
          options={[{ label: "全部", value: "" }, ...GENDER_LIST]}
          defaultValue={""}
        />
      ),
    },
  },
  {
    title: "生日",
    dataIndex: "birthday",
    key: "birthday",
    render: (birthday) => birthday || "-",
    formItemProps: {
      render: () => <RangePicker />,
    },
  },
  {
    title: "年龄",
    key: "age",
    render: (_, record) => {
      const birthday = record.birthday;
      if (!birthday) {
        return null;
      }
      const age = new Date().getFullYear() - new Date(birthday).getFullYear();
      return age;
    },
  },
  {
    title: "健康设备",
    dataIndex: "healthDevice",
    key: "healthDevice",
    width: 150,
    render: (healthDevice) => (
      <DeviceNameTag
        isHealthDevice
        deviceName={healthDevice?.deviceName || "未绑定"}
        {...(healthDevice?.deviceName ? {} : { color: "red" })}
      />
    ),
  },
  {
    title: "绑定操作",
    key: "operation",
    width: 350,
    render: (_, record) => {
      return (
        <Flex gap={GAP.SMALL}>
          <Button
            type="primary"
            onClick={() => handleOptionClick("deviceBind", record)}
          >
            设备
          </Button>
          <Button
            type="primary"
            onClick={() => handleOptionClick("familyBind", record)}
          >
            家属
            <Tag color="orange" variant="solid">
              {record.familyIds?.length || 0}
            </Tag>
          </Button>
          <Button
            type="primary"
            onClick={() => handleOptionClick("caregiverBind", record)}
          >
            护理人员
            <Tag
              color={record.caregiverIds?.length ? "green" : "red"}
              variant="solid"
            >
              {record.caregiverIds?.length ? "有" : "无"}
            </Tag>
          </Button>
        </Flex>
      );
    },
  },
];
export const BIND_OPTION = {
  deviceBind: "deviceBind",
  familyBind: "familyBind",
  caregiverBind: "caregiverBind",
};
