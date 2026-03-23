import React from "react";
import Content from "@/component/Content";
import ProTable from "../../../../component/ProTable";
import { Form } from "antd";
import { apiDeviceList } from "../../../../services/deviceApi";
import { getColumns } from "./constant";
import DeviceCard from "./component/DeviceCard";
export default function Device() {
  const [form] = Form.useForm();
  return (
    <Content title="设备中心">
      <ProTable
        rowKey="deviceId"
        form={form}
        api={apiDeviceList}
        columns={getColumns()}
        paginationConfig={{
          align: "start",
          pageSizeOptions: [8, 12, 16, 20],
        }}
        masonryConfig={{
          columns: {
            md: 2,
            lg: 2,
            xl: 3,
            xxl: 4,
          },
          itemRender: (item) => <DeviceCard item={item} />,
        }}
      />
    </Content>
  );
}
