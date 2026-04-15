package com.emergency.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userid", nullable = false, unique = true, length = 64)
    private String userid;

    @Column(name = "uname", length = 50)
    private String uname;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "deptid")
    private Integer deptid;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
