package com.emergency.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeptSaveReq {
    private Integer id;
    
    @JsonProperty("dept_name")
    private String deptName;
    
    @JsonProperty("dept_code")
    private String deptCode;
    
    @JsonProperty("dept_status")
    private Integer deptStatus;
    
    @JsonProperty("dept_person")
    private String deptPerson;
    
    @JsonProperty("dept_phone")
    private String deptPhone;
    
    @JsonProperty("dept_address")
    private String deptAddress;
    
    @JsonProperty("parent_name")
    private String parentName;
    
    @JsonProperty("dept_responsibility")
    private String deptResponsibility;

    private Double longitude;
    private Double latitude;
}
