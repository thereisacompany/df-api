package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LocationsMapper {

    int insertNews(News record);

    int updateByPrimaryKeySelective(News record);

    List<Regions> getListRegionsByTypeAndVisible();
    List<Business> getListBusinessByTypeAndVisible();

    List<LocationsOut> getListLocationByTypeAndVisible(@Param("regions") String regions,
                                                    @Param("businessType") String businessType,
                                                    @Param("title") String title);


    int getListByTypeANdVisibleCount(@Param("type") Byte type,
                                     @Param("visible") Byte visible,
                                     @Param("title") String title);

    int selectSameTitleCount(@Param("title") String title);

    NewsOut selectByPrimaryKey(Long id);

    void deleteByPrimaryKey(Long id);
}
