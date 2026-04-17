package com.emergency.cbr.entity;

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

    @Column(name = "incident_range")
    private Integer incidentRange;

    @Column(name = "incident_status")
    private Integer incidentStatus;

    @Column(name = "deleted")
    private Integer deleted;
}
