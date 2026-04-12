package com.emergency.auth.controller;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.*;
import com.emergency.auth.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/api/admins")
    public Result<PageData<AdminDto>> getAdmins(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, name = "admin_name") String adminName,
            @RequestParam(required = false, name = "admin_phone") String adminPhone,
            @RequestParam(required = false, name = "admin_email") String adminEmail,
            @RequestParam(required = false, name = "admin_status") Integer adminStatus,
            @RequestParam(required = false, name = "dept_name") String deptName,
            @RequestParam(required = false, name = "parent_dept") Integer parentDept) {
        return adminService.getAdmins(pageNum, pageSize, adminName, adminPhone, adminEmail, adminStatus, deptName,
                parentDept);
    }

    @DeleteMapping("/api/admin/deladmins")
    public Result<String> deleteAdmins(@RequestBody DeleteIdsReq req) {
        return adminService.deleteAdmins(req);
    }

    @GetMapping("/api/admins/{id}")
    public Result<AdminDetailDto> getAdminById(@PathVariable Integer id) {
        return adminService.getAdminById(id);
    }

    @PostMapping("/api/admin/saveadmins")
    public Result<String> saveAdmin(@RequestBody AdminSaveReq req) {
        return adminService.saveAdmin(req);
    }

    @PutMapping("/api/admin/updateadmins")
    public Result<String> updateAdmin(@RequestBody AdminSaveReq req) {
        return adminService.updateAdmin(req);
    }

    @PostMapping("/api/admin/addadmins")
    public Result<String> addAdmin(@RequestBody AdminSaveReq req) {
        return adminService.addAdmin(req);
    }
}
