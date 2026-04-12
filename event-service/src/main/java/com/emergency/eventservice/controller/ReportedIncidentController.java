package com.emergency.eventservice.controller;

import com.emergency.eventservice.common.PageData;
import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.IncidentDetailDto;
import com.emergency.eventservice.dto.IncidentDto;
import com.emergency.eventservice.dto.ReviewReq;
import com.emergency.eventservice.service.ReportedIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReportedIncidentController {

    @Autowired
    private ReportedIncidentService reportedIncidentService;

    @GetMapping("/reported-incidents")
    public Result<PageData<IncidentDto>> getReportedIncidents(
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false, name = "incident_title") String incidentTitle,
            @RequestParam(required = false, name = "incident_type") Integer incidentType,
            @RequestParam(required = false, name = "incident_location") String incidentLocation,
            @RequestParam(required = false, name = "incident_range") Integer incidentRange,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false, name = "incident_status") Integer incidentStatus) {
        return reportedIncidentService.getReportedIncidents(pageSize, pageNum, incidentTitle,
                incidentType, incidentLocation, incidentRange, startTime, endTime, incidentStatus);
    }

    @GetMapping("/reported-incidents/{id}")
    public Result<IncidentDetailDto> getIncidentDetail(@PathVariable Integer id) {
        return reportedIncidentService.getIncidentDetail(id);
    }

    @PutMapping("/reported-incidents/{id}/review")
    public Result<String> reviewIncident(@PathVariable Integer id, @RequestBody ReviewReq req) {
        return reportedIncidentService.reviewIncident(id, req);
    }

    @GetMapping("/reported-incidents-detail/{ids}")
    public Result<PageData<IncidentDetailDto>> getIncidentDetails(@PathVariable String ids) {
        return reportedIncidentService.getIncidentDetails(ids);
    }
}
