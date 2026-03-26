package com.example.intelligentfarmcore.pojo.dto;

import com.example.intelligentfarmcore.pojo.entity.User;
import com.example.intelligentfarmcore.pojo.enums.Gender;
import com.example.intelligentfarmcore.pojo.enums.Role;
import java.time.LocalDate;
import java.util.Set;

public class UserDTO {
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

    private RoomSummaryDTO room;
    private HealthDeviceSummaryDTO healthDevice;

    // 老人绑定的所有家属ID列表
    private Set<Long> familyIds;

    // 老人绑定的所有护理人员ID列表
    private Set<Long> caregiverIds;

    // 家属/护理人员绑定的所有老人ID列表
    private Set<Long> elderIds;

    // 审核状态：0-待审核，1-已审核
    private Integer status;

    public UserDTO() {
    }

    public UserDTO(Long userId, String userName, String phone, Role role, String avatar, Gender gender,
            LocalDate birthday, RoomSummaryDTO room, HealthDeviceSummaryDTO healthDeviceSummary, Integer status) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.role = role;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.room = room;
        this.healthDevice = healthDeviceSummary;
        this.status = status;
    }

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.avatar = user.getAvatar();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.status = user.getStatus();
        this.familyIds = user.getFamilyIds();
        this.caregiverIds = user.getCaregiverIds();
        this.elderIds = user.getElderIds();
        if (user.getRoom() != null) {
            this.room = new RoomSummaryDTO(user.getRoom().getRoomId(), user.getRoom().getRoomNumber());
        }
        if (user.getHealthDevice() != null) {
            this.healthDevice = new HealthDeviceSummaryDTO(user.getHealthDevice());
        }
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

    public RoomSummaryDTO getRoom() {
        return room;
    }

    public void setRoom(RoomSummaryDTO room) {
        this.room = room;
    }

    public HealthDeviceSummaryDTO getHealthDevice() {
        return healthDevice;
    }

    public void setHealthDevice(HealthDeviceSummaryDTO healthDevice) {
        this.healthDevice = healthDevice;
    }

    public Set<Long> getFamilyIds() {
        return familyIds;
    }

    public void setFamilyIds(Set<Long> familyIds) {
        this.familyIds = familyIds;
    }

    public Set<Long> getCaregiverIds() {
        return caregiverIds;
    }

    public void setCaregiverIds(Set<Long> caregiverIds) {
        this.caregiverIds = caregiverIds;
    }

    public Set<Long> getElderIds() {
        return elderIds;
    }

    public void setElderIds(Set<Long> elderIds) {
        this.elderIds = elderIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
