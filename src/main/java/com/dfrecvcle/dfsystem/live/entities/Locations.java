package com.dfrecvcle.dfsystem.live.entities;

import lombok.Data;

@Data
public class Locations {
    private Long id;
    private String title;
    private String address;
    private String phone;
    private String business_hours;
}
