package com.example.intelligentfarmcore.pojo.enums;

import com.example.intelligentfarmcore.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role implements ICodeEnum{
    ELDER(0, "老人"),
    FAMILY(1, "家属"),
    CAREGIVER(2,"护理人员"),
    ADMIN(3, "管理员");

    private final int roleId;
    private final String roleName;

    Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    @JsonCreator
    public static Role fromRoleId(Object input) {
        return EnumUtils.fromValue(input, Role.class, Role::getRoleId);
    }

    @Override
    public int getId() {
        return roleId;
    }
}
