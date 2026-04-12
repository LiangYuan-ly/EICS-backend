package com.emergency.eventservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PublishedIncidentSaveReq {
    private Integer id;
    
    @JsonProperty("incident_title")
    private String incidentTitle;
    
    @JsonProperty("incident_type")
    private Integer incidentType;
    
    @JsonProperty("incident_level")
    private Integer incidentLevel;
    
    @JsonProperty("incident_content")
    private String incidentContent;
    
    @JsonProperty("incident_location")
    private String incidentLocation;
    
    private Double longitude;
    private Double latitude;
    
    @JsonProperty("incident_range")
    private Integer incidentRange;
    
    @JsonProperty("occurrence_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date occurrenceTime;
    
    @JsonProperty("incident_status")
    private Integer incidentStatus;
    
    private String remark;
    
    private List<String> attachments;
}
