package com.emergency.notification.dto;

import lombok.Data;

@Data
public class LoginRes {
    private String token;
    private UserInfo userInfo;
}
