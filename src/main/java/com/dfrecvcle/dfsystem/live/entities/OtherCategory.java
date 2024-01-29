package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OtherCategory {
    private Long id;
    @ApiModelProperty("名稱")
    private String name;
    @ApiModelProperty("頁面代碼")
    private String code;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("置頂(0:否 1:是)")
    private Integer isTop;
    @ApiModelProperty("刊登(0:否 1:是)")
    private Integer isPublish;
    @ApiModelProperty("啟用(0:否 1:是)")
    private Integer isEnable;
    @ApiModelProperty("圖片位置")
    private String images;
    @ApiModelProperty("描述")
    private String memo;
    @ApiModelProperty("meta相關資訊")
    private String metaData;
    @ApiModelProperty("header相關資訊")
    private String headerCode;
    @ApiModelProperty("刪除(0:否 1:是)")
    private Integer isDelete;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
