package com.emergency.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class AdminDto {
    private Integer id;
    
    @JsonProperty("admin_name")
    private String adminName;
    
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
    
    @JsonProperty("admin_status")
    private Integer adminStatus;
    
    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="UTC")
    private Date createTime;
    
    @JsonProperty("update_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="UTC")
    private Date updateTime;
}
