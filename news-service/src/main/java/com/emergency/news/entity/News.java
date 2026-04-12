package com.emergency.news.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "news_name", nullable = false)
    private String newsName;

    @Column(name = "publish_time")
    private LocalDateTime publishTime;

    @Column(name = "publish_company")
    private String publishCompany;

    @Column(name = "news_photo")
    private String newsPhoto;

    @Column(name = "news_url", nullable = false)
    private String newsUrl;

    @Column(name = "news_status")
    private Integer newsStatus = 1;

    @Column(name = "deleted")
    private Integer deleted = 0;

    @Column(name = "admin_id")
    private Integer adminId;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
