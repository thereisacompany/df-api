package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleTags {

    private Long id;
    @ApiModelProperty("關鍵字標籤名稱")
    private String name;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("刪除(0:否 1:是)")
    private Integer isDelete;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
