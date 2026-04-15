package com.emergency.material.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "material_code")
    private String materialCode;

    @Column(name = "material_name")
    private String materialName;

    @Column(name = "material_category")
    private String materialCategory;

    @Column(name = "material_warehouse")
    private String materialWarehouse;

    @Column(name = "material_batch_no")
    private String materialBatchNo;

    @Column(name = "produced_time")
    private Date producedTime;

    @Column(name = "effective_time")
    private Date effectiveTime;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "security_quantity")
    private Integer securityQuantity;

    @Column(name = "specification")
    private String specification;

    @Column(name = "unit")
    private String unit;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "material_status")
    private Integer materialStatus;

    @Column(name = "material_remark")
    private String materialRemark;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
