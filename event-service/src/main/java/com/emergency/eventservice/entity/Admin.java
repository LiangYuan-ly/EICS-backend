package com.emergency.eventservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    private Integer id;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "dept_id")
    private Integer deptId;
}
