package com.emergency.cbr.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "plan_code")
    private String planCode;

    @Column(name = "plan_title")
    private String planTitle;

    @Column(name = "plan_type")
    private Integer planType;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "plan_level")
    private Integer planLevel;

    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "publish_time")
    private Date publishTime;

    @Column(name = "plan_status")
    private Integer planStatus;

    @Column(name = "plan_url")
    private String planUrl;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
