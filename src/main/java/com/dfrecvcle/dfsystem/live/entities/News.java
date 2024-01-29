package com.dfrecvcle.dfsystem.live.entities;

import lombok.Data;

@Data
public class News {
    private Long id;
    private Byte type;
    private String title;
    private String pic;
    private String content;
    private String description;
    private String rounddate;
    private Byte visible=0;
    private String visibleAt;
//    private String createdAt;
    private String createdBy;
}
