package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper {

    int insert(Log record);

    int insertSelective(Log record);

    Long getCountByIpAndDate(
            @Param("userId") Long userId,
            @Param("moduleName") String moduleName,
            @Param("clientIp") String clientIp,
            @Param("createTime") String createTime);

}
