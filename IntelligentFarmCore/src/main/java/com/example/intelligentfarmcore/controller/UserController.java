package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.mapper.UserMapper;
import com.example.intelligentfarmcore.pojo.dto.UserDTO;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.entity.User;
import com.example.intelligentfarmcore.pojo.request.BindCaregiverReq;
import com.example.intelligentfarmcore.pojo.request.BindFamilyReq;
import com.example.intelligentfarmcore.pojo.request.ChangePasswordReq;
import com.example.intelligentfarmcore.pojo.request.DeleteBatchReq;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.LoginRes;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IUserService;
import com.example.intelligentfarmcore.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController // 接口方法返回对象 ，默认返回JSON格式
@RequestMapping("/user") // localhost:8080/user/**
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    UserMapper userMapper;

    // 增加用户
    @PostMapping("/register")
    public ResponseMessage<User> addUser(@Validated @RequestBody User user) {
        // 校验用户是否存在
        User queryUser = userService.getUserByPhone(user.getPhone());
        if (queryUser != null) {
            return ResponseMessage.error("该手机号已被注册");
        }
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar("/upload/avatar/default.png");
        }
        User addedUser = userService.addUser(user, true);
        return ResponseMessage.success(addedUser, "注册成功");
    }

    // 登录
    @PostMapping("/login")
    public ResponseMessage<LoginRes> login(@Validated @RequestBody User user) {
        // 校验用户是否存在
        User queryUser = userService.getUserByPhone(user.getPhone());
        if (queryUser == null) {
            return ResponseMessage.error("该手机号未被注册");
        }
        // 校验密码是否正确
        if (!queryUser.getPassword().equals(user.getPassword())) {
            return ResponseMessage.error("密码错误");
        }
        // 生成JWT Token
        String token = JWTUtils.createToken(queryUser);
        LoginRes loginRes = new LoginRes(token);
        return ResponseMessage.success(loginRes, "登录成功");
    }

    // 查询用户
    @GetMapping("/get")
    public ResponseMessage<UserDTO> getUser(@RequestHeader("Authorization") String token) {
        User user = JWTUtils.getUserFromToken(token);

        User queryUser = userService.getUser(user.getUserId(), true);
        // 校验用户是否存在
        if (queryUser == null) {
            return ResponseMessage.error("用户不存在");
        }
        // 将User转换为UserDTO
        UserDTO userDTO = userMapper.toUserDTO(queryUser);
        return ResponseMessage.success(userDTO);
    }

    // 查询用户列表
    @PostMapping("/list")
    public ResponseMessage<PageRes<UserDTO>> getUserList(@RequestBody PageReq pageReq) {
        return userService.getUserList(pageReq);
    }

    // 通过ids查询用户列表
    @PostMapping("/listByIds")
    public ResponseMessage<List<UserDTO>> getUserListByIds(@RequestBody List<Long> userIds) {
        return userService.getUserListByIds(userIds);
    }

    // 修改密码
    @PostMapping("/changePassword")
    public ResponseMessage<String> changePassword(@Validated @RequestBody ChangePasswordReq changePasswordReq,
            @RequestHeader("Authorization") String token) {
        // 获取用户信息
        User user = JWTUtils.getUserFromToken(token);
        User queryUser = userService.getUserByPhone(user.getPhone());

        // 校验旧密码是否正确
        if (!queryUser.getPassword().equals(changePasswordReq.getOldPassword())) {
            return ResponseMessage.error("旧密码错误");
        }

        // 修改密码
        queryUser.setPassword(changePasswordReq.getNewPassword());
        userService.editUser(queryUser);
        return ResponseMessage.success(null, "密码修改成功");
    }

    // 修改用户
    @PostMapping("/edit")
    public ResponseMessage<User> editUser(@Validated @RequestBody User user,
            @RequestHeader("Authorization") String token) {
        Long userId = JWTUtils.getUserFromToken(token).getUserId();
        user.setUserId(userId);
        // 检验userId是否为空
        if (user.getUserId() == null) {
            return ResponseMessage.error("用户ID不能为空");
        }
        // 校验用户是否存在
        User queryUser = userService.getUser(user.getUserId());
        if (queryUser == null) {
            return ResponseMessage.error("用户不存在");
        }
        queryUser.setAvatar(user.getAvatar());
        queryUser.setUserName(user.getUserName());
        queryUser.setGender(user.getGender());
        queryUser.setBirthday(user.getBirthday());
        userService.editUser(queryUser);
        User resUser = userService.getUser(userId, true);
        return ResponseMessage.success(resUser);
    }

    // 删除用户
    @GetMapping("/delete")
    public ResponseMessage<String> deleteUser(@RequestParam(required = true) Long userId) {
        // 校验用户是否存在
        if (userService.getUser(userId) == null) {
            return ResponseMessage.error("用户不存在");
        }
        userService.deleteUser(userId);
        return ResponseMessage.success("删除成功");
    }

    // 批量删除用户
    @PostMapping("/deleteBatch")
    public ResponseMessage<String> deleteBatch(@RequestBody DeleteBatchReq deleteBatchReq) {
        List<Long> userIds = deleteBatchReq.getUserIds();
        if (userIds.isEmpty()) {
            return ResponseMessage.error("没有要删除的账号");
        }
        userService.deleteBatch(userIds);
        return ResponseMessage.success(null, "批量删除成功");
    }

    // 绑定用户健康设备
    @PostMapping("/bindHealthDevice")
    public ResponseMessage<String> bindHealthDevice(@RequestBody Map<String, Object> map) {
        return userService.bindHealthDevice(map);
    }

    // 绑定家属
    @PostMapping("/bindFamily")
    public ResponseMessage<String> bindFamily(@RequestBody BindFamilyReq req) {
        return userService.bindFamily(req);
    }

    // 绑定护理人员
    @PostMapping("/bindCaregiver")
    public ResponseMessage<String> bindCaregiver(@RequestBody BindCaregiverReq req) {
        return userService.bindCaregiver(req);
    }
}
