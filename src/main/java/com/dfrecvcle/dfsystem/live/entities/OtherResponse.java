package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OtherResponse {
    private Long id;
    @ApiModelProperty("名稱")
    private String name;
    @ApiModelProperty("代稱")
    private String code;
    @ApiModelProperty("meta相關資訊")
    private String metaData;
    @ApiModelProperty("header相關資訊")
    private String headerCode;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
