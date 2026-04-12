package com.emergency.material.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MaterialInoutReq {
    @JsonProperty("material_id")
    private Integer materialId;
    
    @JsonProperty("warehouse_id")
    private Integer warehouseId;
    
    @JsonProperty("in_out")
    private Integer inOut;
    
    private Integer num;
    private String remark;
}
