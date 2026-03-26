import { ROLE } from "../../constant";

export const ROLE_LIST = [
  { value: ROLE.ADMIN, label: "管理员" },
  { value: ROLE.FARMER, label: "养殖员" },
];

export const ROLE_LIST_NOADMIN = [
  { value: ROLE.FARMER, label: "养殖员" },
];
