package com.emergency.auth.service;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.*;

public interface AdminService {
    Result<PageData<AdminDto>> getAdmins(Integer pageNum, Integer pageSize, String adminName, String adminPhone, String adminEmail, Integer adminStatus, String deptName, Integer parentDept);
    
    Result<String> deleteAdmins(DeleteIdsReq req);
    
    Result<AdminDetailDto> getAdminById(Integer id);
    
    Result<String> saveAdmin(AdminSaveReq req);
    
    Result<String> updateAdmin(AdminSaveReq req);
    
    Result<String> addAdmin(AdminSaveReq req);
}
