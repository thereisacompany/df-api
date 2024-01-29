package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.News;
import com.dfrecvcle.dfsystem.live.entities.NewsMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsMainMapper {

    int insertNews(NewsMain record);

    int updateByPrimaryKeySelective(NewsMain record);

    List<NewsMain> getListByTypeAndVisible(@Param("visible") Byte visible,
                                       @Param("title") String title,
                                       @Param("offset") Integer offset,
                                       @Param("rows") Integer rows);

    int getListByTypeANdVisibleCount(@Param("visible") Byte visible,
                                     @Param("title") String title);

    int selectSameTitleCount(@Param("title") String title);

    NewsMain selectByPrimaryKey(Long id);

    void deleteByPrimaryKey(Long id);
}
