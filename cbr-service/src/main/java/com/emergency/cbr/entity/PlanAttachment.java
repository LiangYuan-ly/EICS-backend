package com.emergency.cbr.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "plan_attachments")
public class PlanAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "attachment_name")
    private String attachmentName;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "attachment_type")
    private Integer attachmentType;

    @Column(name = "attachment_size")
    private Integer attachmentSize;

    @Column(name = "target_id")
    private Integer targetId;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "create_time", updatable = false)
    private Date createTime;
}
