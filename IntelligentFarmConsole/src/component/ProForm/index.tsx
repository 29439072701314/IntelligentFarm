import React from "react";
import { Form, FormItemProps, FormProps, Input, Space, SpaceProps } from "antd";
import _ from "lodash";
export interface ProFormProps {
  formConfig: {
    formProps: FormProps;
    items: {
      props: FormItemProps;
      render?: React.ReactNode;
      // 是否渲染(默认渲染)
      isRender?: boolean;
    }[];
    spaceProps: SpaceProps;
  }
}
const ProForm = React.forwardRef((props: ProFormProps, ref) => {
  const { formConfig } = props;
  const { formProps, items, spaceProps } = formConfig;
  return (
    <Form {...formProps}>
      <Space style={{ width: "100%" }} orientation ="vertical" {...spaceProps}>
      {_.compact(items).filter((item) => item.isRender !== false).map((item, index) => (
        item.isRender !== false && (
          <Form.Item key={item.props.name || `form-item-${index}`} {...item.props}>
            {item.render || <Input/>}
          </Form.Item>
        )
      ))}
      </Space>
    </Form>
  );
});

export default ProForm;
