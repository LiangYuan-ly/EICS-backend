package com.emergency.news.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "admin_name")
    private String adminName;
}
