package com.emergency.auth.service.impl;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.AdminInfoDto;
import com.emergency.auth.dto.AdminInfoUpdateReq;
import com.emergency.auth.dto.ChangePasswordReq;
import com.emergency.auth.entity.Admin;
import com.emergency.auth.entity.Dept;
import com.emergency.auth.repository.AdminRepository;
import com.emergency.auth.repository.DeptRepository;
import com.emergency.auth.service.AdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DeptRepository deptRepository;

    private Integer getCurrentAdminId() {
        return 1; // Placeholder for logged-in admin ID
    }

    @Override
    public Result<String> updateAdminInfo(AdminInfoUpdateReq req) {
        Admin admin = adminRepository.findById(getCurrentAdminId()).orElse(null);
        if (admin == null)
            return Result.error("管理员不存在");

        if (req.getAdminName() != null)
            admin.setAdminName(req.getAdminName());
        if (req.getAdminAvatar() != null)
            admin.setAdminAvatar(req.getAdminAvatar());
        if (req.getAdminGender() != null)
            admin.setAdminGender(req.getAdminGender());
        if (req.getAdminPhone() != null)
            admin.setAdminPhone(req.getAdminPhone());
        if (req.getAdminEmail() != null)
            admin.setAdminEmail(req.getAdminEmail());
        if (req.getAdminLocation() != null)
            admin.setAdminLocation(req.getAdminLocation());
        if (req.getRemark() != null)
            admin.setRemark(req.getRemark());
        admin.setUpdateTime(new Date());

        adminRepository.save(admin);

        if (admin.getDeptId() != null) {
            Dept dept = deptRepository.findById(admin.getDeptId()).orElse(null);
            if (dept != null) {
                boolean updated = false;
                if (req.getDeptName() != null) {
                    dept.setDeptName(req.getDeptName());
                    updated = true;
                }
                if (req.getDeptAddress() != null) {
                    dept.setDeptAddress(req.getDeptAddress());
                    updated = true;
                }
                if (updated) {
                    deptRepository.save(dept);
                }
            }
        }
        return Result.success(null, "更新成功");
    }

    @Override
    public Result<String> changePassword(ChangePasswordReq req) {
        Admin admin = adminRepository.findById(getCurrentAdminId()).orElse(null);
        if (admin == null)
            return Result.error("管理员不存在");

        if (req.getOldPassword() == null || !req.getOldPassword().equals(admin.getAdminPassword())) {
            return Result.error("旧密码错误");
        }
        if (req.getNewPassword() == null || !req.getNewPassword().equals(req.getCheckPassword())) {
            return Result.error("两次输入的新密码不一致");
        }

        admin.setAdminPassword(req.getNewPassword());
        admin.setUpdateTime(new Date());
        adminRepository.save(admin);
        return Result.success(null, "更新成功");
    }

    @Override
    public Result<AdminInfoDto> getAdminInfo() {
        Admin admin = adminRepository.findById(getCurrentAdminId()).orElse(null);
        if (admin == null)
            return Result.error("管理员不存在");

        AdminInfoDto dto = new AdminInfoDto();
        dto.setAdminName(admin.getAdminName() != null ? admin.getAdminName() : "");
        dto.setAdminAvatar(admin.getAdminAvatar() != null ? admin.getAdminAvatar() : "");
        dto.setAdminGender(admin.getAdminGender() != null ? admin.getAdminGender().toString() : "");
        dto.setAdminPhone(admin.getAdminPhone() != null ? admin.getAdminPhone() : "");
        dto.setAdminEmail(admin.getAdminEmail() != null ? admin.getAdminEmail() : "");
        dto.setAdminLocation(admin.getAdminLocation() != null ? admin.getAdminLocation() : "");
        dto.setRemark(admin.getRemark() != null ? admin.getRemark() : "");

        if (admin.getDeptId() != null) {
            Dept dept = deptRepository.findById(admin.getDeptId()).orElse(null);
            if (dept != null) {
                dto.setDeptName(dept.getDeptName() != null ? dept.getDeptName() : "");
                dto.setDeptAddress(dept.getDeptAddress() != null ? dept.getDeptAddress() : "");
            } else {
                dto.setDeptName("");
                dto.setDeptAddress("");
            }
        } else {
            dto.setDeptName("");
            dto.setDeptAddress("");
        }

        return Result.success(dto, "查询成功");
    }

    @Override
    public Result<String> uploadAvatar(MultipartFile file) {
        if (file == null || file.isEmpty())
            return Result.error("文件不能为空");
        try {
            String dirPath = System.getProperty("user.dir") + "/static/avatar/";
            File dir = new File(dirPath);
            if (!dir.exists())
                dir.mkdirs();

            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.lastIndexOf(".") != -1) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = "admin_" + getCurrentAdminId() + "_" + System.currentTimeMillis() + extension;
            File dest = new File(dirPath + fileName);
            file.transferTo(dest);

            String baseUrl = org.springframework.web.servlet.support.ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .toUriString();
            try {
                String ip = java.net.InetAddress.getLocalHost().getHostAddress();
                baseUrl = baseUrl.replace("localhost", ip).replace("127.0.0.1", ip);
            } catch (Exception e) {
                // ignore
            }
            String fileUrl = baseUrl + "/static/avatar/" + fileName;

            return Result.success(fileUrl, "头像上传成功");
        } catch (IOException e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
