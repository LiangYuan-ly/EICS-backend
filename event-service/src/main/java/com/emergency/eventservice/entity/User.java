package com.emergency.eventservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private Integer id;

    @Column(name = "uname")
    private String uname;
}
