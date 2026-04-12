package com.emergency.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class UserListDto {
    private Integer id;
    private String uname;
    private String phone;
    private String location;
    
    @JsonProperty("dept_name")
    private String deptName;
    
    @JsonProperty("dept_address")
    private String deptAddress;
    
    private Integer status;
    
    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    
    @JsonProperty("update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;
}
