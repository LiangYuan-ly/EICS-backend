package com.emergency.eventservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapPointDto {
    private Integer id;
    private Double longitude;
    private Double latitude;
    private Integer range;
    private String title;
}
