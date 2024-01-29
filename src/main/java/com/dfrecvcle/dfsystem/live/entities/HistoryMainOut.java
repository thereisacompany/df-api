package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HistoryMainOut {
    private Long id;
    @ApiModelProperty(value = "年份", example = "2023")
    private String name;

    private String images;

//    private String type;
//    private String latitude;
//    private String longitude;
}
