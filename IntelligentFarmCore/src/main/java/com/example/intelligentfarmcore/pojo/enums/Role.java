package com.example.intelligentfarmcore.pojo.enums;

import com.example.intelligentfarmcore.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role implements ICodeEnum{
    ADMIN(0, "管理员"),
    FARMER(1, "养殖员");

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
