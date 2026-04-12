package com.emergency.material.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "material_inout")
public class MaterialInout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "material_id")
    private Integer materialId;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "in_out")
    private Integer inOut;

    @Column(name = "num")
    private Integer num;

    @Column(name = "remark")
    private String remark;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "create_time")
    private Date createTime;
}
