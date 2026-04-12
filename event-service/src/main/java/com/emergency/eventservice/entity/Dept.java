package com.emergency.eventservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "depts")
public class Dept {
    @Id
    private Integer id;

    @Column(name = "dept_name")
    private String deptName;
}
