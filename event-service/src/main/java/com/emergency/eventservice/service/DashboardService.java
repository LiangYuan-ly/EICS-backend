package com.emergency.eventservice.service;

import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.DistributionDto;
import com.emergency.eventservice.dto.PublishTrendDto;
import com.emergency.eventservice.dto.ReportTrendDto;

public interface DashboardService {
    Result<DistributionDto> getDistribution(String date);
    Result<ReportTrendDto> getReportTrend(String startDate, String endDate);
    Result<PublishTrendDto> getPublishTrend(String startDate, String endDate);
}
