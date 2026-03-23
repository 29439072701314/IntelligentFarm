import React from "react";
import { Tag } from "antd";
import { ROLE } from "../../constant";
const TAG_COLOR = {
  [ROLE.ELDER]: "green",
  [ROLE.FAMILY]: "orange",
  [ROLE.CAREGIVER]: "blue",
  [ROLE.ADMIN]: "red",
};
export default function RoleTag({ role }) {
  return (
    <Tag variant="solid" color={TAG_COLOR[role.roleId]}>
      {role.roleName}
    </Tag>
  );
}
