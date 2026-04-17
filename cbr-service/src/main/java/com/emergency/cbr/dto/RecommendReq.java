package com.emergency.cbr.dto;

import lombok.Data;

@Data
public class RecommendReq {
    private String title;
    private Integer incident_type;
    private Integer level;
    private Integer incident_range;
}
