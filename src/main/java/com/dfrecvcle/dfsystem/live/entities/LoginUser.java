package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用戶登入訊息")
public class LoginUser {
    @ApiModelProperty(name = "username", value = "帳號")
    private String username;
    @ApiModelProperty(name = "password", value = "密碼")
    private String password;
}
