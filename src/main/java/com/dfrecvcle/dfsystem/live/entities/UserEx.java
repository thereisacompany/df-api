package com.dfrecvcle.dfsystem.live.entities;

import io.swagger.annotations.ApiModelProperty;

public class UserEx extends User{

    @ApiModelProperty("會員id")
    private Long userId;

    @ApiModelProperty("電話")
    private String phone;

    @ApiModelProperty("電話驗證")
    private Byte phoneValid;

    @ApiModelProperty("驗證碼")
    private Integer code;

    @ApiModelProperty("驗證碼過期時間")
    private String codeExpired;

    @ApiModelProperty("tg")
    private String telegram;

    @ApiModelProperty("tg驗證")
    private Byte telegramValid;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCodeExpired(String codeExpired) {
        this.codeExpired = codeExpired;
    }

    public String getCodeExpired() {
        return codeExpired;
    }

    public void setPhoneValid(Byte phoneValid) {
        this.phoneValid = phoneValid;
    }

    public Byte getPhoneValid() {
        return phoneValid;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegramValid(Byte telegramValid) {
        this.telegramValid = telegramValid;
    }

    public Byte getTelegramValid() {
        return telegramValid;
    }
}
