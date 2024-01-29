package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "更新密碼")
public class UpdPw {
    @ApiModelProperty(value = "會員id")
    private Long userId;
    @ApiModelProperty(value = "舊密碼")
    private String oldpassword;
    @ApiModelProperty(value = "新密碼")
    private String password;
}
