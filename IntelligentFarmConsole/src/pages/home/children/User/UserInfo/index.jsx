import React, { useEffect, useMemo, useState } from "react";
import Content from "@/component/Content";
import { useDispatch, useSelector } from "react-redux";
import userSlice from "@/stores/user";
import { Upload, Button, Image, Input, Form, DatePicker, message } from "antd";
import ProForm from "@/component/ProForm";
import RoleSelect from "@/component/RoleSelect";
import GenderSelect from "@/component/GenderSelect";
import dayjs from "dayjs";
import { apiUpdateUser } from "@/services/userApi";
import { BASE_URL } from "../../../../../utils/request";
import { AVATAR_UPLOAD_URL } from "./constant.jsx";
import { GENDER } from "../../../../../component/GenderSelect/constant.js";
import { ROLE } from "../../../../../constant.js";
import { verifyPhone } from "../../../../../utils/verification.js";
import { apiRegister } from "../../../../../services/userApi.js";

// 用户信息 + 添加账号（管理员权限）
export default function UserInfo(props) {
  const { isAddUser } = props;
  const user = useSelector((state) => state.user);
  const [form] = Form.useForm();
  const [isEdit, setIsEdit] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const dispatch = useDispatch();

  const initialValues = useMemo(
    () =>
      isAddUser
        ? {
            avatar: "/upload/avatar/default.png",
            userName: "",
            phone: "",
            gender: GENDER.MALE,
            role: ROLE.FARMER,
            birthday: null,
          }
        : {
            avatar: user.avatar,
            userName: user.userName,
            phone: user.phone,
            gender: user.gender.genderId,
            role: user.role.roleId,
            birthday: user.birthday ? dayjs(user.birthday, "YYYY-MM-DD") : null,
          },
    [isAddUser, user]
  );
  const [avatarUrl, setAvatarUrl] = useState(initialValues.avatar);
  // 根据不同模式初始化表单值
  useEffect(() => {
    form.resetFields();
    form.setFieldsValue(initialValues);
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setIsEdit(isAddUser);
    setAvatarUrl(initialValues.avatar);
  }, [form, initialValues, isAddUser]);

  // 编辑用户信息
  const handleEdit = async () => {
    if (!isEdit) {
      setIsEdit(!isEdit);
      return;
    }
    try {
      await form.validateFields();
      const data = form.getFieldsValue();
      data.birthday = data.birthday ? data.birthday.format("YYYY-MM-DD") : "";
      const res = await apiUpdateUser(data);
      // 修改状态管理库中的用户信息
      dispatch(userSlice.actions.setUser(res.data));
      setIsEdit(!isEdit);
    } catch (error) {
      messageApi.error(error.message);
    }
  };
  const handleAdd = async () => {
    const values = form.getFieldsValue();
    values.birthday = values.birthday
      ? values.birthday.format("YYYY-MM-DD")
      : "";
    await apiRegister(values);
  };
  // 上传头像
  const handleChange = (info) => {
    if (info.file.status === "done") {
      form.setFieldValue("avatar", info.file.response.data.imageUrl);
      setAvatarUrl(info.file.response.data.imageUrl);
    }
  };

  const formConfig = {
    formProps: {
      form,
      size: "large",
      labelAlign: "left",
      labelCol: { span: 3 },
      wrapperCol: { span: 24 },
      variant: isEdit ? "outlined" : "borderless",
      disabled: !isEdit,
      initialValues: initialValues,
      onFinish: handleAdd,
    },
    items: [
      {
        props: {
          name: "avatar",
          label: "头像",
        },
        render: (
          <>
            <Upload
              name="file"
              listType="picture-card"
              className="avatar-uploader"
              showUploadList={false}
              action={AVATAR_UPLOAD_URL}
              onChange={handleChange}
            >
              <Image preview={!isEdit} src={BASE_URL + avatarUrl} alt="头像" />
            </Upload>
          </>
        ),
      },
      {
        props: {
          name: "userName",
          label: "用户名",
          rules: [
            {
              required: true,
              message: "请输入用户名",
            },
          ],
        },
      },
      {
        props: {
          name: "phone",
          label: "手机号",
          required: true,
          rules: [
            {
              validator: (_, value, callback) => {
                if (!verifyPhone(value)) {
                  callback("请输入正确的手机号");
                }
                callback();
              },
              message: "请输入正确的手机号",
            },
          ],
        },
        render: (
          <Input
            disabled={!isAddUser}
            variant={isAddUser ? "outlined" : "borderless"}
          />
        ),
      },
      isAddUser && {
        props: {
          name: "password",
          label: "密码",
          rules: [
            {
              required: true,
              message: "请输入密码",
            },
          ],
        },
        render: <Input.Password type="password" placeholder="请输入密码" />,
      },
      {
        props: {
          name: "gender",
          label: "性别",
          required: true,
        },
        render: <GenderSelect />,
      },
      {
        props: {
          name: "role",
          label: "角色",
          required: true,
        },
        render: <RoleSelect disabled={!isAddUser} needAdmin />,
      },
      {
        props: {
          name: "birthday",
          label: "生日",
        },
        render: <DatePicker />,
      },
      isAddUser && {
        props: {
          name: "add",
        },
        render: (
          <Button block htmlType="submit" type="primary">
            添加
          </Button>
        ),
      },
    ],
  };

  return (
    <>
      {contextHolder}
      <Content
        title={isAddUser ? "添加账号" : "个人中心"}
        extra={
          isAddUser ? null : (
            <Button type="primary" onClick={handleEdit}>
              {isEdit ? "保存" : "编辑"}
            </Button>
          )
        }
      >
        <ProForm formConfig={formConfig} />
      </Content>
    </>
  );
}
