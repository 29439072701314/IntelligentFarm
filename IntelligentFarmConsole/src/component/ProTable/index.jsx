import {
  Form,
  Input,
  Table,
  Flex,
  Space,
  Button,
  Pagination,
  Masonry,
  Card,
} from "antd";
import React, { useEffect, useState } from "react";
import { SearchOutlined, ReloadOutlined } from "@ant-design/icons";
import styles from "./index.module.less";

export default function ProTable(props) {
  const {
    columns,
    api,
    form,
    beforeSearch, // 搜索前处理
    afterSearch, // 搜索后处理
    extraOptions, // 额外操作(表头)
    rowSelection, // 行选择
    rowKey, // 行键
    masonryConfig, // 是否使用瀑布流配置
    paginationConfig, // 分页配置
    resetText = "重置",
    size = "small",
  } = props;
  const [data, setData] = useState([]);
  const [page, setPage] = useState({
    pageNumber: 1,
    pageSize: paginationConfig?.pageSizeOptions?.[0] || 10,
    total: 0,
    totalPages: 0,
  });
  const [sorter, setSorter] = useState(
    columns
      .filter((item) => item.defaultSortOrder)
      .map((item) => ({
        field: item?.dataIndex || item?.key,
        order: item.defaultSortOrder,
      })),
  );

  // 处理排序参数
  const processSortParams = (sortParams) => {
    if (!sortParams) return [];

    // 处理数组情况（多个排序条件）
    if (Array.isArray(sortParams)) {
      return sortParams
        .filter((item) => item.field && item.order)
        .map((item) => ({
          field: item.field,
          order: item.order === "ascend" ? "asc" : "desc",
        }));
    }

    // 处理对象情况（单个排序条件）
    if (sortParams.field && sortParams.order) {
      return [
        {
          field: sortParams.field,
          order: sortParams.order === "ascend" ? "asc" : "desc",
        },
      ];
    }

    return [];
  };

  const getData = async (isResetPage = false, sortParams = sorter) => {
    if (!api) return;
    const newPage = isResetPage
      ? {
          ...page,
          pageNumber: 1,
        }
      : page;
    const values = form.getFieldsValue();
    const res = await api({
      ...newPage,
      condition: beforeSearch ? beforeSearch(values) : values,
      sort: processSortParams(sortParams),
    });
    const { content, total, totalPages } = res.data;
    // 通过返回的数据判断查询的页码是否超出总页数, 如果超出, 则重新查询最后一页数据
    if (newPage.pageNumber > totalPages && totalPages > 0) {
      setPage({
        ...newPage,
        pageNumber: totalPages,
      });
      return;
    }
    setPage({
      ...newPage,
      total: total,
      totalPages: totalPages,
    });
    setData(afterSearch ? afterSearch(content) : content);
  };
  // 挂载方法到form，方便在其他地方调用
  form.getData = getData;

  useEffect(() => {
    getData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page.pageNumber, page.pageSize]);

  const handleSearch = () => {
    getData();
  };
  const handleReset = () => {
    form.resetFields();
    getData(true);
  };
  return (
    <Space orientation="vertical" size="middle" style={{ width: "100%" }}>
      <Form
        layout="inline"
        onFinish={handleSearch}
        onReset={handleReset}
        form={form}
      >
        <Flex wrap gap={16}>
          {(columns || [])
            .filter(
              (item) => item.formItemProps?.render !== false && item.dataIndex,
            )
            .map((item) => (
              <Form.Item
                label={item.title}
                key={item.key}
                name={item.dataIndex}
                {...item.formItemProps}
              >
                {item.formItemProps?.render ? (
                  item.formItemProps?.render()
                ) : (
                  <Input />
                )}
              </Form.Item>
            ))}
          <Space>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />}>
              查询
            </Button>
            <Button htmlType="reset" icon={<ReloadOutlined />}>
              {resetText}
            </Button>
          </Space>
        </Flex>
      </Form>
      <Space>{extraOptions}</Space>
      <Card
        className={styles.card}
        style={{ overflowY: "auto", maxHeight: "calc(100vh - 350px)" }}
      >
        {masonryConfig ? (
          <Masonry
            // style={{ margin: 16 }}
            items={data}
            itemRender={(item) => (
              <Card key={item[rowKey]}>
                {columns.map((col) => (
                  <div key={col.dataIndex}>
                    {col.title}: {item[col.dataIndex]}
                  </div>
                ))}
              </Card>
            )}
            gutter={16}
            {...masonryConfig}
          />
        ) : (
          <Table
            columns={columns}
            dataSource={data}
            rowKey={rowKey}
            rowSelection={rowSelection}
            pagination={false}
            sticky
            size={size}
            onChange={(_pagination, _filters, newSorter) => {
              setSorter(newSorter);
              getData(false, newSorter);
            }}
          />
        )}
      </Card>
      <Pagination
        current={page.pageNumber}
        pageSize={page.pageSize}
        total={page.total}
        onChange={(pageNumber, pageSize) => {
          setPage({
            ...page,
            pageNumber,
            pageSize,
          });
        }}
        showSizeChanger
        pageSizeOptions={[10, 20, 30, 40]}
        align="end"
        {...paginationConfig}
      />
    </Space>
  );
}
