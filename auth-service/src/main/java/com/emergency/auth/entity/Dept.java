package com.emergency.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "depts")
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dept_name", nullable = false, length = 50)
    private String deptName;

    @Column(name = "dept_code", nullable = false, length = 18, unique = true)
    private String deptCode;

    @Column(name = "dept_status")
    private Integer deptStatus;

    @Column(name = "dept_person", length = 20)
    private String deptPerson;

    @Column(name = "dept_phone", length = 20)
    private String deptPhone;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "dept_address", length = 500)
    private String deptAddress;

    @Column(name = "parent_dept")
    private Integer parentDept;

    @Column(name = "ancestor", length = 255)
    private String ancestor;

    @Column(name = "dept_responsibility", length = 256)
    private String deptResponsibility;
}
