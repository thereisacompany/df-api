package com.dfrecvcle.dfsystem.live.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Log {
    private Long id;

    private Long userId;

    private String operation;

    private String clientIp;

    private Date createAt;

    private Byte status;

    private String content;

}