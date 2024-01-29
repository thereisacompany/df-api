package com.dfrecvcle.dfsystem.live.mappers;


import com.dfrecvcle.dfsystem.live.entities.HistoryCategory;
import com.dfrecvcle.dfsystem.live.entities.HistoryCategory4History;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryCategoryMapper {

    int insertArticleCategory(HistoryCategory record);

    int updateByPrimaryKeySelective(HistoryCategory record);

    List<HistoryCategory4History> getListByExample(@Param("keyword") String keyword,
                                                   @Param("beginDate") String start,
                                                   @Param("endDate") String end,
                                                   @Param("isDelete") String isDelete,
                                                   @Param("offset") Integer offset,
                                                   @Param("rows") Integer rows);

    int getListByExampleCount(@Param("keyword") String keyword,
                              @Param("beginDate") String start,
                              @Param("endDate") String end,
                              @Param("isDelete") String isDelete);

    List<HistoryCategory> getListAll();

    HistoryCategory selectByPrimaryKey(Integer id);

    int isExistCategoryName(@Param("id") Long id,
                            @Param("name") String name);
}
