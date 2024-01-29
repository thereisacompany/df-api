package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.Article;
import com.dfrecvcle.dfsystem.live.entities.HistoryBackend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryBackendMapper {

    int insertArticle(HistoryBackend record);

    int updateByPrimaryKeySelective(HistoryBackend record);

    int updateArticleBrowserCount(Long id, Integer count);

    List<HistoryBackend> getListByExample(@Param("keyword") String keyword,
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
                                       @Param("rows") Integer rows);

    int getListByExampleCount(@Param("keyword") String keyword,
                              @Param("category") List<Integer> category,
                              @Param("hotTag") Long hotTag,
                              @Param("latest") Integer latest,
                              @Param("featured") Integer featured,
                              @Param("publish") Integer publish,
                              @Param("top") Integer top,
                              @Param("delete") Integer delete,
                              @Param("beginDate") String start,
                              @Param("endDate") String end);

    HistoryBackend selectByPrimaryKey(Long id);

    int isExistTitle(@Param("id") Long id,
                     @Param("title") String title);
}
