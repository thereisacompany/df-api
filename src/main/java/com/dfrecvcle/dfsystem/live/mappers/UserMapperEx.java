package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.entities.UserEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapperEx {

    List<User> getUserListByPhone(@Param("phone") String phone, @Param("phoneValid") String valid);

    long getUserByPhone(@Param("phone") String phone);

    String getPhoneAndCode(@Param("phone") String phone, @Param("code") int code);

    List<User> getUserListByUserNameOrAlias(@Param("userName") String userName, @Param("alias") String alias);

    List<UserEx> getAllUserByKey(@Param("phone") String phone,
                               @Param("alias") String alias,
                               @Param("offset") Integer offset,
                               @Param("rows") Integer rows);

    int countAllUserByKey(@Param("phone") String phone, @Param("alias") String alias);

    UserEx getUserById(@Param("id") long id);

    int insertUserEx(UserEx userEx);

    int replaceUserEx(UserEx userEx);

    int updateByPrimaryKeySelective(UserEx userEx);
}
