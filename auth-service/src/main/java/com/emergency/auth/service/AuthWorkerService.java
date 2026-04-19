package com.emergency.auth.service;

import com.emergency.auth.common.JwtUtil;
import com.emergency.auth.common.Result;
import com.emergency.auth.dto.AdminUserDto;
import com.emergency.auth.dto.LoginReq;
import com.emergency.auth.dto.LoginResp;
import com.emergency.auth.dto.WorkerRegisterReq;
import com.emergency.auth.entity.Admin;
import com.emergency.auth.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthWorkerService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Result<LoginResp> workerLogin(LoginReq req) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminPhone(req.getAdmin_phone());
        if (optionalAdmin.isEmpty()) {
            return Result.error(400, "该手机号尚未注册");
        }
        
        Admin admin = optionalAdmin.get();
        
        // 此处暂时使用明文密码匹配，若你之后要引入加密可在此处修改（如 BCrypt 的 matches()）
        if (!admin.getAdminPassword().equals(req.getAdmin_password())) {
            return Result.error(400, "密码错误");
        }

        if (admin.getStatus() != null && admin.getStatus() == 0) {
            return Result.error(403, "该账号已被停用");
        }

        String token = JwtUtil.generateToken(admin.getId(), admin.getAdminPhone());
        
        AdminUserDto dto = new AdminUserDto();
        dto.setId(admin.getId());
        // 按照接口文档返回 admin_name 而非实体里的驼峰 adminName
        dto.setAdmin_name(admin.getAdminName());
        dto.setAdmin_avatar(admin.getAdminAvatar());
        
        LoginResp resp = new LoginResp();
        resp.setToken(token);
        resp.setUser(dto);
        
        return Result.success(resp, "登录成功");
    }

    public Result<String> forgetPassword(WorkerRegisterReq req) {
        // 校验验证码
        String redisCode = stringRedisTemplate.opsForValue().get("sms:code:" + req.getAdmin_phone());
        if (redisCode == null) {
            return Result.error(400, "验证码已过期或未获取");
        }
        if (!redisCode.equals(req.getCode())) {
            return Result.error(400, "验证码错误");
        }

        Optional<Admin> optionalAdmin = adminRepository.findByAdminPhone(req.getAdmin_phone());
        if (optionalAdmin.isEmpty()) {
            return Result.error(400, "该手机号未注册，无法操作");
        }
        
        Admin admin = optionalAdmin.get();
        admin.setAdminPassword(req.getAdmin_password()); // 明文处理，与登录逻辑保持一致
        adminRepository.save(admin);
        
        // 成功后删除验证码
        stringRedisTemplate.delete("sms:code:" + req.getAdmin_phone());
        return Result.success("操作成功");
    }
}
