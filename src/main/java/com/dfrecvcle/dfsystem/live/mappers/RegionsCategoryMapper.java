package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.ArticleCategory;
import com.dfrecvcle.dfsystem.live.entities.ArticleCategory4Article;
import com.dfrecvcle.dfsystem.live.entities.RegionsCategory;
import com.dfrecvcle.dfsystem.live.entities.RegionsCategory4Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegionsCategoryMapper {

    int insertArticleCategory(RegionsCategory record);

    int updateByPrimaryKeySelective(RegionsCategory record);

    List<RegionsCategory4Article> getListByExample(@Param("keyword") String keyword,
                                                   @Param("beginDate") String start,
                                                   @Param("endDate") String end,
                                                   @Param("isDelete") String isDelete,
                                                   @Param("offset") Integer offset,
                                                   @Param("rows") Integer rows);

    int getListByExampleCount(@Param("keyword") String keyword,
                              @Param("beginDate") String start,
                              @Param("endDate") String end,
                              @Param("isDelete") String isDelete);

    List<RegionsCategory> getListAll();

    RegionsCategory selectByPrimaryKey(Integer id);

    int isExistCategoryName(@Param("id") Long id,
                            @Param("name") String name);
}
