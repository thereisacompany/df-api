package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BusinessCategoryMapper {

    int insertArticleCategory(BusinessCategory record);

    int updateByPrimaryKeySelective(BusinessCategory record);

    List<BusinessCategory4Article> getListByExample(@Param("keyword") String keyword,
                                                    @Param("beginDate") String start,
                                                    @Param("endDate") String end,
                                                    @Param("isDelete") String isDelete,
                                                    @Param("offset") Integer offset,
                                                    @Param("rows") Integer rows);

    int getListByExampleCount(@Param("keyword") String keyword,
                              @Param("beginDate") String start,
                              @Param("endDate") String end,
                              @Param("isDelete") String isDelete);

    List<BusinessCategory> getListAll();

    BusinessCategory selectByPrimaryKey(Integer id);

    int isExistCategoryName(@Param("id") Long id,
                            @Param("name") String name);
}
