package com.emergency.auth.dto;

import lombok.Data;

@Data
public class WorkerRegisterReq {
    private String admin_phone;
    private String code;
    private String admin_password;
    private String confirm_password;
}
