import React from "react";
import { Modal, Form, InputNumber } from "antd";
import ProForm from "../../../../../../../component/ProForm";
import { UserOutlined } from "@ant-design/icons";
import { apiRoomBatchUpdateCapacity } from "../../../../../../../services/roomApi";

export default function RoomBatchUpdateCapacityModal(props) {
  const { open, onClose, selectedRoomIds } = props;
  const [form] = Form.useForm();

  const handleSubmit = async () => {
    const values = await form.validateFields();
    await apiRoomBatchUpdateCapacity({
      roomIds: selectedRoomIds,
      maxCapacity: values.maxCapacity,
    });
    form.resetFields();
    onClose(true);
  };

  return (
    open && (
      <Modal
        title="批量修改房间容量"
        onOk={handleSubmit}
        open={open}
        onCancel={() => onClose(false)}
        centered
      >
        <ProForm
          formConfig={{
            formProps: {
              form,
              layout: "vertical",
            },
            items: [
              {
                props: {
                  name: "maxCapacity",
                  label: "房间容量",
                  rules: [{ required: true, message: "请输入房间容量" }],
                },
                render: (
                  <InputNumber
                    min={1}
                    prefix={<UserOutlined />}
                    placeholder="请输入房间容量"
                    style={{ width: "100%" }}
                  />
                ),
              },
            ],
          }}
        />
      </Modal>
    )
  );
}
