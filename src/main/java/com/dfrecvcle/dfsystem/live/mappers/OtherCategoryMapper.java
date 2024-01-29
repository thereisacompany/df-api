package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OtherCategoryMapper {

    int insertArticleCategory(OtherCategory record);

    int updateByPrimaryKeySelective(OtherCategory record);

    List<OtherCategory4Article> getListByExample(@Param("keyword") String keyword,
                                                 @Param("beginDate") String start,
                                                 @Param("endDate") String end,
                                                 @Param("isDelete") String isDelete,
                                                 @Param("offset") Integer offset,
                                                 @Param("rows") Integer rows);

    int getListByExampleCount(@Param("keyword") String keyword,
                              @Param("beginDate") String start,
                              @Param("endDate") String end,
                              @Param("isDelete") String isDelete);

    List<OtherCategory> getListAll();

    List<OtherResponse> getListMetaAll();

    OtherCategory selectByPrimaryKey(Integer id);

    int isExistCategoryName(@Param("id") Long id,
                            @Param("name") String name);
}
