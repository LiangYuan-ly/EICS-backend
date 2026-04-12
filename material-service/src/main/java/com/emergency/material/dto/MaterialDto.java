package com.emergency.material.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class MaterialDto {
    private Integer id;
    
    @JsonProperty("material_code")
    private String materialCode;
    
    @JsonProperty("material_name")
    private String materialName;
    
    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @JsonProperty("warehouse_name")
    private String warehouseName;

    @JsonProperty("warehouse_address")
    private String warehouseAddress;

    @JsonProperty("warehouse_person")
    private String warehousePerson;

    @JsonProperty("warehouse_phone")
    private String warehousePhone;

    @JsonProperty("warehouse_status")
    private Integer warehouseStatus;

    @JsonProperty("warehouse_remark")
    private String warehouseRemark;
    
    @JsonProperty("material_batch_no")
    private String materialBatchNo;
    
    @JsonProperty("produce_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    private Date produceTime;
    
    @JsonProperty("effective_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    private Date effectiveTime;
    
    @JsonProperty("stock_quantity")
    private Integer stockQuantity;
    
    @JsonProperty("security_quantity")
    private Integer securityQuantity;
    
    private String specification;
    private String unit;
    private String manufacturer;
    
    @JsonProperty("material_status")
    private Integer materialStatus;

    @JsonProperty("material_remark")
    private String materialRemark;

    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @JsonProperty("update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
}
