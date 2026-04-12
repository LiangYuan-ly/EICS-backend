package com.emergency.auth.dto;

import lombok.Data;

@Data
public class LoginReq {
    private String admin_phone;
    private String admin_password;
}
