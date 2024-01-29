package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HistoryOut {
    private Long id;
    @ApiModelProperty(value = "標題名稱", example = "大豐環保")
    private String title;
    @ApiModelProperty(value = "年份", example = "2023")
    private String year;
    @ApiModelProperty(value = "時間", example = "2023-09-01")
    private String time;
    @ApiModelProperty(value = "年份ID", example = "1")
    private Long category;
    @ApiModelProperty(value = "最新消息連結", example = "http://www.explae.com")
    private String url;
//    private String type;
//    private String latitude;
//    private String longitude;
}
