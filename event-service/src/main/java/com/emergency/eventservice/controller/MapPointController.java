package com.emergency.eventservice.controller;

import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.MapPointDto;
import com.emergency.eventservice.service.MapPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MapPointController {

    @Autowired
    private MapPointService mapPointService;

    @GetMapping("/reported-point")
    public Result<Map<String, List<MapPointDto>>> getReportedPoints(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String incident_status) {
        return mapPointService.getReportedPoints(startTime, endTime, incident_status);
    }

    @GetMapping("/published-point")
    public Result<Map<String, List<MapPointDto>>> getPublishedPoints(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String incident_status) {
        return mapPointService.getPublishedPoints(startTime, endTime, incident_status);
    }

    @GetMapping("/dept-point")
    public Result<Map<String, List<MapPointDto>>> getDeptPoints() {
        return mapPointService.getDeptPoints();
    }

    @GetMapping("/warehouse-point")
    public Result<Map<String, List<MapPointDto>>> getWarehousePoints() {
        return mapPointService.getWarehousePoints();
    }
}
