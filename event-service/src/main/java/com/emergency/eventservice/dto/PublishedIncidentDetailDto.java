package com.emergency.eventservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PublishedIncidentDetailDto {
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
    
    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    
    @JsonProperty("update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
    
    @JsonProperty("incident_status")
    private Integer incidentStatus;
    
    private String remark;
    
    @JsonProperty("admin_name")
    private String adminName;
    
    @JsonProperty("admin_dept_name")
    private String adminDeptName;
    
    @JsonProperty("publish_name")
    private String publishName;
    
    @JsonProperty("publish_dept_name")
    private String publishDeptName;
    
    private List<AttachmentDto> attachments;
}
