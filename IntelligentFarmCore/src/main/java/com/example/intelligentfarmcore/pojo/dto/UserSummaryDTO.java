package com.example.intelligentfarmcore.pojo.dto;

import com.example.intelligentfarmcore.pojo.enums.Gender;
import com.example.intelligentfarmcore.pojo.enums.Role;

import java.time.LocalDate;

public class UserSummaryDTO {
    // 用户ID
    private Long userId;

    // 用户名
    private String userName;

    // 手机号
    private String phone;

    // 角色
    private Role role;

    // 头像
    private String avatar;

    // 性别
    private Gender gender;

    // 出生日期
    private LocalDate birthday;

    public UserSummaryDTO() {
    }

    public UserSummaryDTO(Long userId, String userName, String phone, Role role, String avatar, Gender gender, LocalDate birthday) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.role = role;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
