export const getColumns = () => [
  {
    title: "房间号",
    dataIndex: "roomNumber",
    key: "roomNumber",
  },
  {
    title: "入住率",
    dataIndex: "occupancyRate",
    key: "occupancyRate",
    defaultSortOrder: "descend",
    formItemProps: {
      render: false,
    },
  },
];
