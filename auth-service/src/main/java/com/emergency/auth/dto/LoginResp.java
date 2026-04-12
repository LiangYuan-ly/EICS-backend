package com.emergency.auth.dto;

import lombok.Data;

@Data
public class LoginResp {
    private String token;
    private AdminUserDto user;
}
