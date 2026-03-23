import { Avatar, Button, DatePicker, Flex, Form } from "antd";
import { BASE_URL } from "@/utils/request";
import { GENDER_LIST } from "@/component/GenderSelect/constant";
import { ROLE_LIST } from "@/component/RoleSelect/constant";
import GenderSelect from "@/component/GenderSelect";
import RoleSelect from "@/component/RoleSelect";
import RoleTag from "../../../../../component/RoleTag";
import GenderTag from "../../../../../component/GenderTag";
import RoomNumberTag from "../../../../../component/RoomNumberTag";
import { GAP, ROLE } from "../../../../../constant";
const { RangePicker } = DatePicker;
export const getColumns = (handleDelete) => [
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
    title: "角色",
    dataIndex: "role",
    key: "role",
    render: (role) => <RoleTag role={role} />,
    formItemProps: {
      render: () => (
        <RoleSelect
          options={[{ label: "全部", value: "" }, ...ROLE_LIST]}
          defaultValue={""}
        />
      ),
    },
    sorter: true,
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
    title: "操作",
    key: "operation",
    render: (_, record) => (
      <Button
        disabled={record.role.roleId === ROLE.ADMIN}
        type="primary"
        danger
        onClick={() => handleDelete(record)}
      >
        删除
      </Button>
    ),
  },
];
