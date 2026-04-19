package com.emergency.auth.controller;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.LoginReq;
import com.emergency.auth.dto.LoginResp;
import com.emergency.auth.dto.SmsReq;
import com.emergency.auth.dto.WorkerRegisterReq;
import com.emergency.auth.service.AuthWorkerService;
import com.emergency.auth.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/worker/auth")
public class AuthWorkerController {
    
    @Autowired
    private AuthWorkerService authWorkerService;

    @Autowired
    private SmsService smsService;

    @PostMapping("/workerLogin")
    public Result<LoginResp> workerLogin(@RequestBody LoginReq req) {
        if (req.getAdmin_phone() == null || req.getAdmin_phone().trim().isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        if (req.getAdmin_password() == null || req.getAdmin_password().trim().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }
        return authWorkerService.workerLogin(req);
    }

    @PostMapping("/sendCode")
    public Result<String> sendCode(@RequestBody SmsReq req) {
        if (req.getAdmin_phone() == null || req.getAdmin_phone().trim().isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        return smsService.sendSmsCode(req.getAdmin_phone());
    }

    @PostMapping("/workerRegister")
    public Result<String> workerRegister(@RequestBody WorkerRegisterReq req) {
        if (req.getAdmin_phone() == null || req.getAdmin_phone().trim().isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        if (req.getCode() == null || req.getCode().trim().isEmpty()) {
            return Result.error(400, "验证码不能为空");
        }
        if (req.getAdmin_password() == null || req.getAdmin_password().trim().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }
        if (!req.getAdmin_password().equals(req.getConfirm_password())) {
            return Result.error(400, "两次密码输入不一致");
        }
        
        return authWorkerService.forgetPassword(req);
    }
}
