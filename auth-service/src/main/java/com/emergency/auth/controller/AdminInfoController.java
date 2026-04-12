package com.emergency.auth.controller;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.AdminInfoDto;
import com.emergency.auth.dto.AdminInfoUpdateReq;
import com.emergency.auth.dto.ChangePasswordReq;
import com.emergency.auth.service.AdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AdminInfoController {

    @Autowired
    private AdminInfoService adminInfoService;

    @PutMapping("/admininfo/change")
    public Result<String> updateAdminInfo(@RequestBody AdminInfoUpdateReq req) {
        return adminInfoService.updateAdminInfo(req);
    }

    @PostMapping("/admininfo/changepassword")
    public Result<String> changePassword(@RequestBody ChangePasswordReq req) {
        return adminInfoService.changePassword(req);
    }

    @GetMapping("/admininfo")
    public Result<AdminInfoDto> getAdminInfo() {
        return adminInfoService.getAdminInfo();
    }

    @PostMapping("/avatar/upload")
    public Result<String> uploadAvatar(@RequestParam(value = "file", required = false) MultipartFile file) {
        return adminInfoService.uploadAvatar(file);
    }
}
