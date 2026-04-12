package com.emergency.auth.controller;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.LoginReq;
import com.emergency.auth.dto.LoginResp;
import com.emergency.auth.service.AuthWorkerService;
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
}
