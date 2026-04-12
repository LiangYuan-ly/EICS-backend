package com.emergency.material.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class WarehouseDto {
    private Integer id;
    
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
    
    private String remark;
    private Double longitude;
    private Double latitude;
    
    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    
    @JsonProperty("update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
}
