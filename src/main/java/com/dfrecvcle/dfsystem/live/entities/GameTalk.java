package com.dfrecvcle.dfsystem.live.entities;

import lombok.Data;

@Data
public class GameTalk {

    private Long id;

    private String groupId;

    private String groupName;

    private String roundDate;

    private Integer disband;
}
