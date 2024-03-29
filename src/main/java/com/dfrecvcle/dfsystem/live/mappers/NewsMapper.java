package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.News;
import com.dfrecvcle.dfsystem.live.entities.NewsOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsMapper {

    int insertNews(News record);

    int updateByPrimaryKeySelective(News record);

    List<NewsOut> getListByTypeAndVisible(@Param("year") String year,
                                          @Param("type") Byte type,
                                          @Param("visible") Byte visible,
                                          @Param("title") String title,
                                          @Param("offset") Integer offset,
                                          @Param("rows") Integer rows);

    int getListByTypeANdVisibleCount(@Param("year") String year,
                                    @Param("type") Byte type,
                                     @Param("visible") Byte visible,
                                     @Param("title") String title);

    int getListByTypeANdVisibleBackendCount(@Param("type") Byte type,
                                     @Param("visible") Byte visible,
                                     @Param("title") String title);

    int selectSameTitleCount(@Param("title") String title);

    NewsOut selectByPrimaryKey(@Param("id") Long id, @Param("type") Byte type);

    void deleteByPrimaryKey(Long id);
}
