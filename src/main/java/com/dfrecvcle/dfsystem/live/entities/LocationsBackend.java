package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LocationsBackend {
    private Long id;
    @ApiModelProperty("文章標題")
    private String title;
//    @ApiModelProperty("代稱設定")
//    private String code;
    @ApiModelProperty("地區分類")
    private Integer regions;
    @ApiModelProperty("經營分類")
    private Integer type;

//    @ApiModelProperty("文章名稱")
//    private String categoryName;

    @ApiModelProperty("經度")
    private String longitude;
    @ApiModelProperty("緯度")
    private String latitude;
    @ApiModelProperty("網址")
    private String url;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("營業時間")
    private String business_hours;
    @ApiModelProperty("電話")
    private String phone;

//    @ApiModelProperty("刊登日期")
//    private String publishDate;
    @ApiModelProperty("排序")
    private Integer sort;
//    @ApiModelProperty("最新(0:否 1:是)")
//    private Integer isLatest;
//    @ApiModelProperty("精選(0:否 1:是)")
//    private Integer isFeatured;
//    @ApiModelProperty("置頂(0:否 1:是)")
//    private Integer isTop;
//    @ApiModelProperty("刊登(0:否 1:是)")
//    private Integer isPublish;
    @ApiModelProperty("啟用(0:否 1:是)")
    private Integer isEnable;
    @ApiModelProperty("刪除(0:否 1:是)")
    private Integer isDelete;
//    @ApiModelProperty("各類圖片位置")
//    private String images;
//    @ApiModelProperty("內容描述")
//    private String content;
//    @ApiModelProperty("相關賽事設定")
//    private String gameSettings;
//    @ApiModelProperty("熱門關鍵字標籤設定")
//    private String keywordSettings;
//    @ApiModelProperty("相關文章設定")
//    private String articleSettings;
//    @ApiModelProperty("seo佈局相關設定(meta)")
//    private String seoMetaData;
//    @ApiModelProperty("seo佈局相關設定(header)")
//    private String seoHeaderData;
//    @ApiModelProperty("瀏覽次數")
//    private Integer browserCount;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
