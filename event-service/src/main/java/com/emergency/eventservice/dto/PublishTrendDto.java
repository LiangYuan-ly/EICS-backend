package com.emergency.eventservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class PublishTrendDto {
    private List<String> dates;
    private List<Integer> publishSeries;
}
