import React from "react";
import Content from "../../../../../component/Content";
import ProForm from "../../../../../component/ProForm";
import { verifyGeneralFormat } from "../../../../../utils/verification";
import { Form, Input, Button, message } from "antd";
import { LockOutlined } from "@ant-design/icons";
import { apiChangePassword } from "../../../../../services/userApi";
import { isRequestSuccess } from "../../../../../utils/request";
import { logout } from "../../../../../utils/utils";

export default function ChangePassword() {
  const [form] = Form.useForm();
  const getItemConfig = (label, name) => ({
    props: {
      label,
      name,
      rules: [
        {
          validator: (_, value, callback) => {
            if (!value) {
              callback(`请输入${label}`);
            }
            if (!verifyGeneralFormat(value)) {
              callback("密码格式错误(8-16位数字或字母组成)");
            }
            callback();
          },
        },
      ],
    },
    render: (
      <Input.Password
        prefix={<LockOutlined />}
        type="password"
        placeholder="请输入密码"
      />
    ),
  });

  const onFinish = async () => {
    const values = form.getFieldsValue();
    const { oldPassword, newPassword, confirmNewPassword } = values;
    if (newPassword !== confirmNewPassword) {
      message.error("两次输入的新密码不一致");
      return;
    }
    const res = await apiChangePassword({
      oldPassword,
      newPassword,
    });
    if (isRequestSuccess(res)) {
      logout();
      return;
    }
  };

  const formConfig = {
    formProps: {
      form,
      size: "large",
      labelAlign: "left",
      labelCol: { span: 3 },
      wrapperCol: { span: 24 },
      onFinish,
    },
    spaceProps: {
      size: "large",
    },
    items: [
      getItemConfig("旧密码", "oldPassword"),
      getItemConfig("新密码", "newPassword"),
      getItemConfig("确认新密码", "confirmNewPassword"),
      {
        props: {
          label: null,
          labelCol: { span: 0 },
        },
        render: (
          <Button block type="primary" htmlType="submit">
            修改
          </Button>
        ),
      },
    ],
  };
  return (
    <Content title="修改密码">
      <ProForm formConfig={formConfig} />
    </Content>
  );
}
