package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackMapper {

    int insertFeedback(Feedback record);

    int updateByPrimaryKeySelective(Feedback record);

    List<Feedback> getListByCreatedByAndCreatedAt(@Param("username") String username,
                                                  @Param("beginDate") String beginDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("offset") Integer offset,
                                                  @Param("rows") Integer rows);

    int getListByCreatedByAndCreatedAtCount(@Param("username") String username,
                                            @Param("beginDate") String beginDate,
                                            @Param("endDate") String endDate);

    Feedback selectByPrimaryKey(Long id);
}
