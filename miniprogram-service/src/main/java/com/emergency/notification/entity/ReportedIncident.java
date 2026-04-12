package com.emergency.notification.entity;

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

    @Column(name = "incident_title")
    private String incidentTitle;

    @Column(name = "incident_location")
    private String incidentLocation;

    @Column(name = "incident_status")
    private Integer incidentStatus;

    @Column(name = "review")
    private String review;

    @Column(name = "incident_type")
    private Integer incidentType;

    @Column(name = "incident_content")
    private String incidentContent;

    @Column(name = "incident_range")
    private Integer incidentRange;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "occurrence_time")
    private Date occurrenceTime;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        updateTime = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}
