package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.Article;
import com.dfrecvcle.dfsystem.live.entities.LocationsBackend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LocationsBackendMapper {

    int insertArticle(LocationsBackend record);

    int updateByPrimaryKeySelective(LocationsBackend record);

    int updateArticleBrowserCount(Long id, Integer count);

    List<LocationsBackend> getListByExample(@Param("keyword") String keyword,
                                            @Param("category") List<Integer> category,
                                            @Param("hotTag") Long hotTag,
                                            @Param("latest") Integer latest,
                                            @Param("featured") Integer featured,
                                            @Param("publish") Integer publish,
                                            @Param("top") Integer top,
                                            @Param("delete") Integer delete,
                                            @Param("beginDate") String start,
                                            @Param("endDate") String end,
                                            @Param("offset") Integer offset,
                                            @Param("rows") Integer rows,
                                            @Param("regions") List<Integer> regions
                                            );

    int getListByExampleCount(@Param("keyword") String keyword,
                              @Param("category") List<Integer> category,
                              @Param("hotTag") Long hotTag,
                              @Param("latest") Integer latest,
                              @Param("featured") Integer featured,
                              @Param("publish") Integer publish,
                              @Param("top") Integer top,
                              @Param("delete") Integer delete,
                              @Param("beginDate") String start,
                              @Param("endDate") String end,
                              @Param("regions") List<Integer> regions
                              );

    LocationsBackend selectByPrimaryKey(Long id);

    int isExistTitle(@Param("id") Long id,
                     @Param("title") String title);
}
