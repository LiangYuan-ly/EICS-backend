package com.emergency.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private Integer id;

    @Column(name = "uname")
    private String uname;

    @Column(name = "avatar")
    private String avatar;
}
