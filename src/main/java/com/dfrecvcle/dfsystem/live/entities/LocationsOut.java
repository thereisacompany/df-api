package com.dfrecvcle.dfsystem.live.entities;

import lombok.Data;

@Data
public class LocationsOut {
    private Long id;
    private String title;
    private String address;
    private String phone;
    private String btime;
    private String regions;
    private String type;
    private String latitude;
    private String longitude;
    private String regionsname;
}
