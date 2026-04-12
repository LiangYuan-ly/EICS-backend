package com.emergency.eventservice.dto;

import lombok.Data;

@Data
public class DistributionDto {
    private Integer reported;
    private Integer passed;
    private Integer failed;
    private Integer pending;
}
