package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryMapper {

    int insertNews(News record);

    int updateByPrimaryKeySelective(News record);

    List<Regions> getListRegionsByTypeAndVisible();
    List<Business> getListBusinessByTypeAndVisible();

    List<HistoryOut> getListHistoryByTypeAndVisible(@Param("filter") String filter,@Param("title") String title);


    List<HistoryMainOut> getListMainHistory(@Param("filter") String filter,@Param("title") String title);



    int getListByTypeANdVisibleCount(@Param("type") Byte type,
                                     @Param("visible") Byte visible,
                                     @Param("title") String title);

    int selectSameTitleCount(@Param("title") String title);



    void deleteByPrimaryKey(Long id);
}
