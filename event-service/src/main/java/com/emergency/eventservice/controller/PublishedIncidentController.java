package com.emergency.eventservice.controller;

import com.emergency.eventservice.common.PageData;
import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.IdsReq;
import com.emergency.eventservice.dto.PublishedIncidentDetailDto;
import com.emergency.eventservice.dto.PublishedIncidentListDto;
import com.emergency.eventservice.dto.PublishedIncidentSaveReq;
import com.emergency.eventservice.service.PublishedIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/emergency-events")
public class PublishedIncidentController {

    @Autowired
    private PublishedIncidentService publishedIncidentService;

    @PostMapping("/add")
    public Result<String> addIncident(@RequestBody PublishedIncidentSaveReq req) {
        return publishedIncidentService.addIncident(req);
    }

    @GetMapping
    public Result<PageData<PublishedIncidentListDto>> getIncidents(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, name = "incident_title") String incidentTitle,
            @RequestParam(required = false, name = "incident_type") Integer incidentType,
            @RequestParam(required = false, name = "incident_level") Integer incidentLevel,
            @RequestParam(required = false, name = "incident_location") String incidentLocation,
            @RequestParam(required = false, name = "incident_range") Integer incidentRange,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false, name = "incident_status") String incidentStatus) {
        return publishedIncidentService.getIncidents(pageNum, pageSize, incidentTitle, incidentType, incidentLevel, incidentLocation, incidentRange, startTime, endTime, incidentStatus);
    }

    @PostMapping("/publish")
    public Result<String> publishIncidents(@RequestBody IdsReq req) {
        return publishedIncidentService.publishIncidents(req);
    }

    @PostMapping("/withdraw")
    public Result<String> withdrawIncidents(@RequestBody IdsReq req) {
        return publishedIncidentService.withdrawIncidents(req);
    }

    @PutMapping("/update")
    public Result<String> updateIncident(@RequestBody PublishedIncidentSaveReq req) {
        return publishedIncidentService.updateIncident(req);
    }

    @PostMapping("/upload")
    public Result<String> uploadAttachment(@RequestParam(value = "file", required = false) MultipartFile file) {
        return publishedIncidentService.uploadAttachment(file);
    }

    @DeleteMapping("/del")
    public Result<String> deleteIncidents(@RequestBody IdsReq req) {
        return publishedIncidentService.deleteIncidents(req);
    }

    @GetMapping("/{id}")
    public Result<PublishedIncidentDetailDto> getIncidentDetail(@PathVariable Integer id) {
        return publishedIncidentService.getIncidentDetail(id);
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAttachment(@PathVariable Integer id) {
        return publishedIncidentService.deleteAttachment(id);
    }
}
