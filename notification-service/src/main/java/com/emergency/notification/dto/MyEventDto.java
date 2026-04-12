package com.emergency.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class MyEventDto {
    private Integer id;

    @JsonProperty("incident_title")
    private String incidentTitle;

    @JsonProperty("incident_status")
    private Integer incidentStatus;

    @JsonProperty("incident_location")
    private String incidentLocation;

    @JsonProperty("occurrence_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    private Date occurrenceTime;

    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    private Date createTime;
}
