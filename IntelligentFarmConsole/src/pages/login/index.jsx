import React from "react";
import styles from "./index.module.less";
import { Form, Input, Button, Flex } from "antd";
import { LockOutlined, PhoneOutlined, UserOutlined } from "@ant-design/icons";
import { NavLink, useNavigate, useLocation } from "react-router";
import { useState } from "react";
import { PAGE_MODE } from "./constant";
import { apiGetUser, apiLogin, apiRegister } from "../../services/userApi";
import { verifyGeneralFormat, verifyPhone } from "../../utils/verification";
import ProForm from "../../component/ProForm";
import { HTTP_STATUS } from "../../constant";
import GenderSelect from "../../component/GenderSelect";
import { GENDER } from "../../component/GenderSelect/constant";
import RoleSelect from "../../component/RoleSelect";
import { ROLE } from "../../constant";
import { useDispatch } from "react-redux";
import userSlice from "../../stores/user";

export default function Login() {
  const { pathname } = useLocation();
  const dispatch = useDispatch();
  const [pageMode, setPageMode] = useState(() =>
    pathname === "/register" ? PAGE_MODE.REGISTER : PAGE_MODE.LOGIN
  );
  const [form] = Form.useForm();
  const isLogin = pageMode === PAGE_MODE.LOGIN;
  const navigate = useNavigate();

  /**
   * 切换登录注册模式
   * @param {*} formData 切换后表单数据
   */
  const onChangePageMode = (formData) => {
    form.resetFields();
    formData && form.setFieldsValue(formData);
    setPageMode(isLogin ? PAGE_MODE.REGISTER : PAGE_MODE.LOGIN);
  };

  // 登录提交
  const onLoginSubmit = async () => {
    const {
      code,
      data: { token },
    } = await apiLogin(form.getFieldsValue());
    if (code === HTTP_STATUS.OK) {
      localStorage.setItem("token", token);
      const userRes = await apiGetUser();
      dispatch(userSlice.actions.setUser(userRes.data));
      navigate("/home");
    }
  };

  // 注册提交
  const onRegisterSubmit = async () => {
    const formData = form.getFieldsValue();
    // 强制设置为养殖员角色
    formData.role = ROLE.FARMER;
    const res = await apiRegister(formData);
    if (res.code === HTTP_STATUS.OK) {
      onChangePageMode({
        phone: res.data.phone,
      });
      navigate("/login");
    }
  };

  // 表单配置
  const formConfig = {
    formProps: {
      form,
      size: "large",
      labelAlign: "left",
      labelCol: { span: 5 },
      onFinish: isLogin ? onLoginSubmit : onRegisterSubmit,
      initialValues: {
        gender: GENDER.MALE,
        role: ROLE.FARMER,
      },
    },
    items: [
      {
        props: {
          name: "phone",
          label: "手机号",
          rules: [
            {
              validator: (_, value) => {
                if (!verifyPhone(value)) {
                  return Promise.reject(new Error("请输入正确的手机号"));
                }
                return Promise.resolve();
              },
              message: "请输入正确的手机号",
            },
          ],
        },
        render: <Input prefix={<PhoneOutlined />} placeholder="请输入手机号" />,
      },
      {
        props: {
          label: "用户名",
          name: "userName",
          rules: [
            {
              validator: (_, value) => {
                if (!value && !isLogin) {
                  return Promise.reject(new Error("请输入用户名"));
                }
                return Promise.resolve();
              },
              message: "请输入用户名",
            },
          ],
          hidden: isLogin,
        },
        render: <Input prefix={<UserOutlined />} placeholder="请输入用户名" />,
      },
      {
        props: {
          label: "密码",
          name: "password",
          rules: [
            {
              validator: (_, value) => {
                if (!value) {
                  return Promise.reject(new Error("请输入密码"));
                }
                if (isLogin) {
                  return Promise.resolve();
                }
                if (!verifyGeneralFormat(value)) {
                  return Promise.reject(new Error("密码格式错误(8-16位数字或字母组成)"));
                }
                return Promise.resolve();
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
      },
      {
        props: {
          name: "confirmPassword",
          label: "确认密码",
          rules: [
            {
              validator: (_, value) => {
                if (isLogin) {
                  return Promise.resolve();
                }
                if (!value) {
                  return Promise.reject(new Error("请确认密码"));
                }
                const password = form.getFieldValue("password");
                if (value !== password) {
                  return Promise.reject(new Error("两次输入密码不一致"));
                }
                return Promise.resolve();
              },
            },
          ],
          hidden: isLogin,
        },
        render: (
          <Input.Password
            prefix={<LockOutlined />}
            type="password"
            placeholder="请确认密码"
          />
        ),
      },
      {
        props: {
          label: "性别",
          name: "gender",
        },
        render: <GenderSelect />,
        isRender: !isLogin,
      },

      {
        props: {
          label: null,
          labelCol: { span: 0 },
        },
        render: (
          <Button block type="primary" htmlType="submit">
            {pageMode === PAGE_MODE.LOGIN ? "登录" : "注册"}
          </Button>
        ),
      },
      {
        props: {},
        render: (
          <Flex justify="flex-end" align="center">
            <span>{isLogin ? "没有账号？" : "已有账号？"}</span>
            <NavLink
              to={isLogin ? "/register" : "/login"}
              onClick={() => onChangePageMode()}
            >
              {isLogin ? "注册账号" : "登录账号"}
            </NavLink>
          </Flex>
        ),
      },
    ],
  };

  return (
    <div className={styles.background}>
      <div>
        <h1 className={styles.systemTitle}>智能农场养殖系统</h1>
        <div className={styles.login}>
          <h1 className={styles.title}>
            {pageMode === PAGE_MODE.LOGIN ? "用户登录" : "用户注册"}
          </h1>
          <ProForm formConfig={formConfig} />
        </div>
      </div>
    </div>
  );
}
