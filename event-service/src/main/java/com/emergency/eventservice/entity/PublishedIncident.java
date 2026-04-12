package com.emergency.eventservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "published_incidents")
public class PublishedIncident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "incident_title")
    private String incidentTitle;

    @Column(name = "incident_type")
    private Integer incidentType;

    @Column(name = "incident_level")
    private Integer incidentLevel;

    @Column(name = "incident_content")
    private String incidentContent;

    @Column(name = "incident_location")
    private String incidentLocation;

    @Column(name = "incident_range")
    private Integer incidentRange;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "occurrence_time")
    private Date occurrenceTime;

    @Column(name = "incident_status")
    private Integer incidentStatus;

    @Column(name = "remark")
    private String remark;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "publish_id")
    private Integer publishId;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
