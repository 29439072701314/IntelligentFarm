import React, { useEffect, useState } from "react";
import { Result, Button, Skeleton } from "antd";
import { HomeOutlined, RollbackOutlined } from "@ant-design/icons";
import { apiGetUser } from "../../services/userApi";
import userSlice from "../../stores/user";
import { useDispatch } from "react-redux";

export default function Error() {
  const dispatch = useDispatch();
  const [isLoading, setIsLoading] = useState(true);
  useEffect(() => {
    const getUser = async () => {
      try {
        const userRes = await apiGetUser();
        dispatch(userSlice.actions.setUser(userRes.data));
      } catch (error) {
        console.log(error);
      } finally {
        setIsLoading(false);
      }
    };
    getUser();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const handleGoHome = () => {
    // 返回首页
    window.location.href = "/home";
  };

  const handleGoBack = () => {
    // 返回上一页
    window.history.back();
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Skeleton loading={isLoading}>
        <Result
          status="404"
          title="404"
          subTitle="抱歉，您访问的页面不存在。"
          extra={[
            <Button
              type="primary"
              key="home"
              icon={<HomeOutlined />}
              onClick={handleGoHome}
            >
              返回首页
            </Button>,
            <Button
              key="back"
              icon={<RollbackOutlined />}
              onClick={handleGoBack}
            >
              返回上一页
            </Button>,
          ]}
        />
      </Skeleton>
    </div>
  );
}
