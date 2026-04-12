package com.emergency.cbr.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "depts")
public class Dept {
    @Id
    private Integer id;

    @Column(name = "dept_name")
    private String deptName;
    
    @Column(name = "dept_code")
    private String deptCode;
}
