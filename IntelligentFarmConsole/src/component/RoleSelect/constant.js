import { ROLE } from "../../constant";

export const ROLE_LIST = [
  { value: ROLE.ELDER, label: "老人" },
  { value: ROLE.FAMILY, label: "家属" },
  { value: ROLE.CAREGIVER, label: "护理人员" },
  { value: ROLE.ADMIN, label: "管理员" },
];

export const ROLE_LIST_NOADMIN = [
  { value: ROLE.ELDER, label: "老人" },
  { value: ROLE.FAMILY, label: "家属" },
  { value: ROLE.CAREGIVER, label: "护理人员" },
];
