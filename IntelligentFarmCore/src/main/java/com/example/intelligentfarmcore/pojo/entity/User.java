package com.example.intelligentfarmcore.pojo.entity;

import com.example.intelligentfarmcore.pojo.enums.Gender;
import com.example.intelligentfarmcore.pojo.enums.Role;
import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Set;

@Table(name = "tb_user")
@Entity
public class User {
    // 用户ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // 用户名
    @Column(name = "user_name")
    private String userName;

    // 密码
    @Column(name = "password")
    private String password;

    // 手机号
    @Column(name = "phone")
    private String phone;

    // 角色
    @Column(name = "role")
    private Role role;

    // 头像
    @Column(name = "avatar")
    private String avatar;

    // 性别
    @Column(name = "gender")
    private Gender gender;

    // 出生日期
    @Column(name = "birthday")
    private LocalDate birthday;

    @ManyToOne()
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne()
    @JoinColumn(name = "health_device_id", unique = true)
    private HealthDevice healthDevice;

    // 老人绑定的所有家属ID列表
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "family_ids", columnDefinition = "json")
    private Set<Long> familyIds;

    // 老人绑定的所有护理人员ID列表
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "caregiver_ids", columnDefinition = "json")
    private Set<Long> caregiverIds;

    // 家属/护理人员绑定的所有老人ID列表
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "elder_ids", columnDefinition = "json")
    private Set<Long> elderIds;

    // 审核状态：0-待审核，1-已审核
    @Column(name = "status")
    private Integer status;

    public HealthDevice getHealthDevice() {
        return healthDevice;
    }

    public void setHealthDevice(HealthDevice healthDevice) {
        this.healthDevice = healthDevice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", status=" + status +
                '}';
    }
}