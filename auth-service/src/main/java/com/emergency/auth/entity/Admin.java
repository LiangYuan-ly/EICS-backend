package com.emergency.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "admin_name", nullable = false, length = 20)
    private String adminName;

    @Column(name = "admin_password", nullable = false)
    private String adminPassword;

    @Column(name = "admin_avatar")
    private String adminAvatar;

    @Column(name = "admin_gender")
    private Integer adminGender;

    @Column(name = "admin_phone", length = 20, unique = true)
    private String adminPhone;

    @Column(name = "admin_email", length = 64)
    private String adminEmail;

    @Column(name = "admin_location", length = 500)
    private String adminLocation;

    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "remark", length = 256)
    private String remark;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
