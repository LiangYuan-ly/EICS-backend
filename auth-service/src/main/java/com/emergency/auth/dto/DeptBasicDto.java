package com.emergency.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeptBasicDto {
    private Integer id;
    
    @JsonProperty("dept_name")
    private String deptName;
    
    @JsonProperty("dept_code")
    private String deptCode;
    
    @JsonProperty("dept_address")
    private String deptAddress;
}
