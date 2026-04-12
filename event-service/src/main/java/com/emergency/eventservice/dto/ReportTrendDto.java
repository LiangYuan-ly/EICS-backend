package com.emergency.eventservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReportTrendDto {
    private List<String> dates;
    private List<Integer> reportedSeries;
    private List<Integer> passedSeries;
    private List<Integer> failedSeries;
}
