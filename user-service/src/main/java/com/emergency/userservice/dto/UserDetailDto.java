package com.emergency.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDetailDto {
    private Integer id;
    private String userid;
    private String uname;
    private String avatar;
    private Integer gender;
    private String phone;
    private String location;
    
    @JsonProperty("dept_name")
    private String deptName;
    
    @JsonProperty("dept_address")
    private String deptAddress;
    
    private Integer status;
}
