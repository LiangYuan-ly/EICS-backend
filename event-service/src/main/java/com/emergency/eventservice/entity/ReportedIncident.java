package com.emergency.eventservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "reported_incidents")
public class ReportedIncident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "incident_title", nullable = false)
    private String incidentTitle;

    @Column(name = "incident_type")
    private Integer incidentType;

    @Column(name = "incident_content", columnDefinition = "TEXT")
    private String incidentContent;

    @Column(name = "incident_location", nullable = false)
    private String incidentLocation;

    @Column(name = "incident_range")
    private Integer incidentRange;

    @Column(name = "occurrence_time", nullable = false)
    private Date occurrenceTime;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "incident_status", nullable = false)
    private Integer incidentStatus;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "review")
    private String review;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
