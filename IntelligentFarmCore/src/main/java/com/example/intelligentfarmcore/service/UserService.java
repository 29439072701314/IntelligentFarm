package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.dao.UserDao;
import com.example.intelligentfarmcore.mapper.UserMapper;
import com.example.intelligentfarmcore.pojo.dto.UserDTO;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.entity.User;
import com.example.intelligentfarmcore.pojo.enums.Gender;
import com.example.intelligentfarmcore.pojo.enums.Role;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;
import com.example.intelligentfarmcore.service.interfaces.IUserService;
import com.example.intelligentfarmcore.utils.ConditionUtils;
import com.example.intelligentfarmcore.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service // spring 的 bean 组件
public class UserService implements IUserService {
    @Autowired
    UserDao userDao;
    @Autowired
    UserMapper userMapper;

    @Override
    public User addUser(User user) {
        // 调用 dao 层的方法添加用户
        return userDao.save(user);
    }

    @Override
    public User addUser(User user, boolean isFilter) {
        User addedUser = userDao.save(user);
        if (isFilter) {
            addedUser.setPassword(null);
        }
        return addedUser;
    }

    @Override
    public User getUser(Long userId) {
        // 调用 dao 层的方法查询用户
        return userDao.findById(userId).orElse(null);
    }

    @Override
    public User getUser(Long userId, boolean isFilter) {
        // 调用 dao 层的方法查询用户
        User user = userDao.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        if (isFilter) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public ResponseMessage<PageRes<UserDTO>> getUserList(PageReq pageReq) {
        // 从请求参数中提取条件
        ConditionUtils conditionUtils = new ConditionUtils(pageReq.getCondition());
        // 构建查询条件
        String userName = conditionUtils.getString("userName");
        String phone = conditionUtils.getString("phone");
        Gender gender = conditionUtils.getEnum("gender", Gender.class);
        Role role = conditionUtils.getEnum("role", Role.class);
        LocalDate minBirthday = conditionUtils.getLocalDate("minBirthday");
        LocalDate maxBirthday = conditionUtils.getLocalDate("maxBirthday");

        // 创建Pageable对象
        Pageable pageable = PageableUtils.createPageable(pageReq);
        // 调用 dao 层方法进行条件查询
        Page<User> users = userDao.findByConditions(
                userName,
                phone,
                gender,
                role,
                minBirthday,
                maxBirthday,
                pageable);
        List<UserDTO> userDTOList = userMapper.toUserDTOList(users.getContent());

        PageRes<UserDTO> pageRes = new PageRes<>(users, userDTOList);
        return ResponseMessage.success(pageRes);
    }

    @Override
    public User getUserByPhone(String phone) {
        // 调用 dao 层的方法查询用户
        return userDao.findByPhone(phone);
    }

    @Override
    public User editUser(User user) {
        // 调用 dao 层的方法修改用户
        return userDao.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        // 调用 dao 层的方法删除用户
        userDao.deleteById(userId);
    }

    @Override
    public void deleteBatch(List<Long> userIds) {
        // 调用 dao 层的方法批量删除用户
        userDao.deleteAllById(userIds);
    }

    @Override
    public ResponseMessage<List<UserDTO>> getUserListByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResponseMessage.success(new ArrayList<>());
        }
        List<User> users = (List<User>) userDao.findAllById(userIds);
        List<UserDTO> userDTOList = userMapper.toUserDTOList(users);
        return ResponseMessage.success(userDTOList);
    }
}
