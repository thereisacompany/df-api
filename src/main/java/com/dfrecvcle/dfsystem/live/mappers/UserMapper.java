package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insertUser(User user);

    int updateByPrimaryKeySelective(User user);

    User selectByPrimaryKey(Long id);

    List<User> selectByUsernameOrPassword(@Param("userName") String userName, @Param("password") String password);

    String checkPhoneAndCode(@Param("phone") String phone, @Param("code") int code);

    int updateForgetPwCode(@Param("id") Long id, @Param("pwCode") int pwCode);
}
