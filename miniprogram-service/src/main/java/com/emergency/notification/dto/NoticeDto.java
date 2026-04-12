package com.emergency.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class NoticeDto {
    private Integer id;

    @JsonProperty("incident_title")
    private String incidentTitle;

    @JsonProperty("incident_location")
    private String incidentLocation;

    @JsonProperty("update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date updateTime;

    @JsonProperty("incident_status")
    private Integer incidentStatus;

    private String review;
}
