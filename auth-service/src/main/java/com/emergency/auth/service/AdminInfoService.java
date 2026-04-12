package com.emergency.auth.service;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.AdminInfoDto;
import com.emergency.auth.dto.AdminInfoUpdateReq;
import com.emergency.auth.dto.ChangePasswordReq;
import org.springframework.web.multipart.MultipartFile;

public interface AdminInfoService {
    Result<String> updateAdminInfo(AdminInfoUpdateReq req);
    Result<String> changePassword(ChangePasswordReq req);
    Result<AdminInfoDto> getAdminInfo();
    Result<String> uploadAvatar(MultipartFile file);
}
