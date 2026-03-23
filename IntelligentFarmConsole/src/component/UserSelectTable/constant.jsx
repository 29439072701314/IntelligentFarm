import { Avatar } from "antd";
import { BASE_URL } from "@/utils/request";
import GenderTag from "../GenderTag";
import RoleTag from "../RoleTag";
import { GENDER_LIST } from "../GenderSelect/constant";
import GenderSelect from "../GenderSelect";

export const getColumns = () => [
  {
    title: "头像",
    dataIndex: "avatar",
    key: "avatar",
    render: (avatar) => (
      <Avatar
        src={BASE_URL + avatar}
        alt="avatar"
        style={{ width: 40, height: 40 }}
      />
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
    formItemProps: {
      render: false,
    },
  },
  {
    title: "年龄",
    dataIndex: "birthday",
    render: (_, record) => {
      const birthday = record.birthday;
      if (!birthday) {
        return null;
      }
      const age = new Date().getFullYear() - new Date(birthday).getFullYear();
      return age;
    },
    formItemProps: {
      render: false,
    },
    sorter: true,
  },
];
