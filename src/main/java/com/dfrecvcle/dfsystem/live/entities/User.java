package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class User {
    @ApiModelProperty("會員id")
    private Long id;

    @ApiModelProperty("帳號")
    private String username;
    @ApiModelProperty("密碼")
    private String password;
    @ApiModelProperty("忘記密碼的驗證碼")
    private Integer pwCode;
    @ApiModelProperty("暱稱")
    private String alias;
    @ApiModelProperty("會員狀態(0停用 1啟用 2刪除")
    private Byte status;
    @ApiModelProperty("會員type")
    private Byte userType;
    @ApiModelProperty("會員等級")
    private Byte level;
    @ApiModelProperty("國籍")
    private String country;
    @ApiModelProperty("生日")
    private String birthday;
    @ApiModelProperty("頭像")
    private String avatar;
    @ApiModelProperty("幣別")
    private String currency;
    @ApiModelProperty("餘額")
    private BigDecimal balance;
    @ApiModelProperty("禁言時間")
    private String banAt;
    @ApiModelProperty("註冊時間")
    private String registerAt;
    @ApiModelProperty("註冊ip")
    private String registerIp;

    @ApiModelProperty("UserSig")
    private String userSig;
}
