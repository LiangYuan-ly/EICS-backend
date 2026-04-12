package com.emergency.eventservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private Integer id;

    @Column(name = "uname")
    private String uname;
}
