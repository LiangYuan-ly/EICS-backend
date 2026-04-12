package com.emergency.eventservice.controller;

import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.DistributionDto;
import com.emergency.eventservice.dto.PublishTrendDto;
import com.emergency.eventservice.dto.ReportTrendDto;
import com.emergency.eventservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/distribution")
    public Result<DistributionDto> getDistribution(@RequestParam(required = false) String date) {
        return dashboardService.getDistribution(date);
    }

    @GetMapping("/report-trend")
    public Result<ReportTrendDto> getReportTrend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return dashboardService.getReportTrend(startDate, endDate);
    }

    @GetMapping("/publish-trend")
    public Result<PublishTrendDto> getPublishTrend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return dashboardService.getPublishTrend(startDate, endDate);
    }
}
