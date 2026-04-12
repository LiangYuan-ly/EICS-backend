package com.emergency.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminSaveReq {
    private Integer id;
    
    @JsonProperty("admin_name")
    private String adminName;
    
    @JsonProperty("admin_password")
    private String adminPassword;
    
    @JsonProperty("admin_avatar")
    private String adminAvatar;
    
    @JsonProperty("admin_gender")
    private Integer adminGender;
    
    @JsonProperty("admin_phone")
    private String adminPhone;
    
    @JsonProperty("admin_email")
    private String adminEmail;
    
    @JsonProperty("admin_location")
    private String adminLocation;
    
    @JsonProperty("dept_name")
    private String deptName;
    
    @JsonProperty("dept_code")
    private String deptCode;
    
    @JsonProperty("dept_address")
    private String deptAddress;
    
    @JsonProperty("admin_status")
    private Integer adminStatus;
    
    @JsonProperty("remark")
    private String remark;
}
