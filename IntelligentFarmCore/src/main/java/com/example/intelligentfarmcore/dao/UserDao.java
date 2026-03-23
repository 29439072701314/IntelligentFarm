package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.User;
import com.example.intelligentfarmcore.pojo.enums.Gender;
import com.example.intelligentfarmcore.pojo.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE " +
            "(:userName IS NULL OR u.userName LIKE %:userName%) AND " +
            "(:phone IS NULL OR u.phone LIKE %:phone%) AND " +
            "(:gender IS NULL OR u.gender = :gender) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:minBirthday IS NULL OR u.birthday >= :minBirthday) AND " +
            "(:maxBirthday IS NULL OR u.birthday <= :maxBirthday)")
    Page<User> findByConditions(
            @Param("userName") String userName,
            @Param("phone") String phone,
            @Param("gender") Gender gender,
            @Param("role") Role role,
            @Param("minBirthday") LocalDate minBirthday,
            @Param("maxBirthday") LocalDate maxBirthday,
            Pageable pageable
    );

    /**
     * 通过手机号查询用户
     *
     * @param phone 手机号
     * @return 用户对象
     */
    User findByPhone(String phone);
}
