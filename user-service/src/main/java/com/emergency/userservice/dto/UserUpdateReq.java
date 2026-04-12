package com.emergency.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserUpdateReq {
    private Integer id;
    private String userid;
    private String uname;
    private String avatar;
    private Integer gender;
    private String phone;
    private String location;

    @JsonProperty("dept_name")
    private String deptName;

    private Integer status;
}
