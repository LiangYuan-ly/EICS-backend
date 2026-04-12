package com.emergency.notification.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "reported_attachments")
public class ReportedAttachment {
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

    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
}
