package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Feedback {

    private Long id;
    @ApiModelProperty("姓名")
    private String username;
    @ApiModelProperty("Email")
    private String mail;
    @ApiModelProperty("電話")
    private String phone;
    @ApiModelProperty("住址")
    private String address;
    @ApiModelProperty("詢問類型")
    private String question;
//    private String title;
    @ApiModelProperty("需求說明")
    private String content;

    private String response;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

}
