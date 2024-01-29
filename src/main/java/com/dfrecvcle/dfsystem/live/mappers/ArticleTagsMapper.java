package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.ArticleTagVo4Article;
import com.dfrecvcle.dfsystem.live.entities.ArticleTags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleTagsMapper {

    int insertArticleTag(ArticleTags record);

    int updateByPrimaryKeySelective(ArticleTags record);

    List<ArticleTagVo4Article> getListByExample(@Param("keyword") String keyword,
                                                @Param("beginDate") String start,
                                                @Param("endDate") String end,
                                                @Param("isDelete") String isDelete,
                                                @Param("offset") Integer offset,
                                                @Param("rows") Integer rows);

    int getListByExampleCount(@Param("keyword") String keyword,
                              @Param("beginDate") String start,
                              @Param("endDate") String end,
                              @Param("isDelete") String isDelete);

    ArticleTags selectByPrimaryKey(Integer id);

    int isExistTagName(@Param("id") Long id,
                       @Param("name") String name);

}
