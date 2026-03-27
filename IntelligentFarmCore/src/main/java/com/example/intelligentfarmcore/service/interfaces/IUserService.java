package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.dto.UserDTO;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.pojo.entity.User;
import com.example.intelligentfarmcore.pojo.request.PageReq;
import com.example.intelligentfarmcore.pojo.response.PageRes;

import java.util.List;

public interface IUserService {
    /**
     * 添加用户
     * 
     * @param user 用户对象
     */
    User addUser(User user);

    User addUser(User user, boolean isFilter);

    /**
     * 查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    User getUser(Long userId);

    /**
     * 查询用户（可选密码过滤）
     * 
     * @param userId   用户ID
     * @param isFilter 是否过滤密码字段
     * @return 用户对象
     */
    User getUser(Long userId, boolean isFilter);

    /**
     * 通过手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户对象
     */
    User getUserByPhone(String phone);

    /**
     * 查询用户列表
     * 
     * @return 用户列表
     */
    ResponseMessage<PageRes<UserDTO>> getUserList(PageReq pageReq);

    /**
     * 修改用户
     * 
     * @param user 用户对象
     */
    User editUser(User user);

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     */
    void deleteBatch(List<Long> userIds);

    /**
     * 通过用户ID列表查询用户列表
     * 
     * @param userIds 用户ID列表
     * @return 用户DTO列表
     */
    ResponseMessage<List<UserDTO>> getUserListByIds(List<Long> userIds);
}
