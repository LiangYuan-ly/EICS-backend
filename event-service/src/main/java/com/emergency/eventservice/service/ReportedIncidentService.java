package com.emergency.eventservice.service;

import com.emergency.eventservice.common.PageData;
import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.IncidentDetailDto;
import com.emergency.eventservice.dto.IncidentDto;
import com.emergency.eventservice.dto.ReviewReq;

public interface ReportedIncidentService {
    Result<PageData<IncidentDto>> getReportedIncidents(Integer pageSize, Integer pageNum, String incidentTitle, Integer incidentType, String incidentLocation, Integer incidentRange, String startTime, String endTime, Integer incidentStatus);
    
    Result<IncidentDetailDto> getIncidentDetail(Integer id);
    
    Result<String> reviewIncident(Integer id, ReviewReq req);
    
    Result<PageData<IncidentDetailDto>> getIncidentDetails(String ids);
}
