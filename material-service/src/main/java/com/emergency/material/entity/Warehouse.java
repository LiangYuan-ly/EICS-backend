package com.emergency.material.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "warehouse_code")
    private String warehouseCode;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Column(name = "warehouse_address")
    private String warehouseAddress;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "warehouse_person")
    private String warehousePerson;

    @Column(name = "warehouse_phone")
    private String warehousePhone;

    @Column(name = "warehouse_status")
    private Integer warehouseStatus;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
