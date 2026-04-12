package com.emergency.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class ReportReq {
    @JsonProperty("incident_title")
    private String incidentTitle;

    @JsonProperty("incident_type")
    private Integer incidentType;

    @JsonProperty("incident_content")
    private String incidentContent;

    @JsonProperty("incident_location")
    private String incidentLocation;

    @JsonProperty("incident_range")
    private Integer incidentRange;

    @JsonProperty("occurrence_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurrenceTime;

    private Double longitude;
    private Double latitude;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("reported_attachments_id")
    private java.util.List<Integer> reportedAttachmentsId;
}
