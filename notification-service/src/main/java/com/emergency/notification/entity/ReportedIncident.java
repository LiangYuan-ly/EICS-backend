package com.emergency.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
}
