package com.emergency.eventservice.service;

import com.emergency.eventservice.common.PageData;
import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.PublishedIncidentDetailDto;
import com.emergency.eventservice.dto.PublishedIncidentListDto;
import com.emergency.eventservice.dto.PublishedIncidentSaveReq;
import com.emergency.eventservice.dto.IdsReq;
import org.springframework.web.multipart.MultipartFile;

public interface PublishedIncidentService {
    Result<String> addIncident(PublishedIncidentSaveReq req);
    Result<PageData<PublishedIncidentListDto>> getIncidents(Integer pageNum, Integer pageSize, String incidentTitle, Integer incidentType, Integer incidentLevel, String incidentLocation, Integer incidentRange, String startTime, String endTime, String incidentStatus);
    Result<String> publishIncidents(IdsReq req);
    Result<String> withdrawIncidents(IdsReq req);
    Result<String> updateIncident(PublishedIncidentSaveReq req);
    Result<String> uploadAttachment(MultipartFile file);
    Result<String> deleteIncidents(IdsReq req);
    Result<PublishedIncidentDetailDto> getIncidentDetail(Integer id);
    Result<String> deleteAttachment(Integer id);
}
