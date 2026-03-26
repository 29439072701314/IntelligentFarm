import React, { useState } from "react";
import { Tabs, Button } from "antd";
import Content from "@/component/Content";
import { PlusOutlined } from "@ant-design/icons";
import FeedFormulaList from "./FeedFormulaList";
import FeedPlanList from "./FeedPlanList";

const { TabPane } = Tabs;

export default function FeedManage() {
  const [activeTab, setActiveTab] = useState("formula");

  const handleTabChange = (key) => {
    setActiveTab(key);
  };

  return (
    <Content title="饲料管理">
      <Tabs activeKey={activeTab} onChange={handleTabChange}>
        <TabPane tab="饲料配方" key="formula">
          <FeedFormulaList />
        </TabPane>
        <TabPane tab="投喂计划" key="plan">
          <FeedPlanList />
        </TabPane>
      </Tabs>
    </Content>
  );
}
