package com.dfrecvcle.dfsystem.live.entities;

import lombok.Data;

@Data
public class NewsOut {
    private Long id;
    private Byte type;
    private String title;
//    private String pic;
    private String content;
    private String previousid;
    private String nextid;
//    private String description;
    private String date;
    private String year;
//    private Byte visible=0;
//    private String visibleAt;
//    private String createdAt;
//
    private String images;
    private String category;
    private Long categoryid;

//    n.id as id, n.type, n.title, n.content,n.rounddate as date,m.title as category
}
