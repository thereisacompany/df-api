package com.dfrecvcle.dfsystem.live.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemConfigMapper {

    int updateDefaultVideo(@Param("defaultVideo") String defaultVideo);

    String getDefaultVideo();

}
