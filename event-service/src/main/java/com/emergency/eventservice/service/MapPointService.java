package com.emergency.eventservice.service;

import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.MapPointDto;
import java.util.List;
import java.util.Map;

public interface MapPointService {
    Result<Map<String, List<MapPointDto>>> getReportedPoints(String startTime, String endTime, String incidentStatus);
    Result<Map<String, List<MapPointDto>>> getPublishedPoints(String startTime, String endTime, String incidentStatus);
    Result<Map<String, List<MapPointDto>>> getDeptPoints();
    Result<Map<String, List<MapPointDto>>> getWarehousePoints();
}
