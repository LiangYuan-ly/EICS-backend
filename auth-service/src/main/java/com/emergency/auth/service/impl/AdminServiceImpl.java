package com.emergency.auth.service.impl;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.*;
import com.emergency.auth.entity.Admin;
import com.emergency.auth.entity.Dept;
import com.emergency.auth.repository.AdminRepository;
import com.emergency.auth.repository.DeptRepository;
import com.emergency.auth.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Override
    public Result<PageData<AdminDto>> getAdmins(Integer pageNum, Integer pageSize, String adminName, String adminPhone,
            String adminEmail, Integer adminStatus, String deptName, Integer parentDept) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Specification<Admin> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (adminName != null && !adminName.isEmpty()) {
                predicates.add(cb.like(root.get("adminName"), "%" + adminName + "%"));
            }
            if (adminPhone != null && !adminPhone.isEmpty()) {
                predicates.add(cb.like(root.get("adminPhone"), "%" + adminPhone + "%"));
            }
            if (adminEmail != null && !adminEmail.isEmpty()) {
                predicates.add(cb.like(root.get("adminEmail"), "%" + adminEmail + "%"));
            }
            if (adminStatus != null) {
                predicates.add(cb.equal(root.get("status"), adminStatus));
            }

            if ((deptName != null && !deptName.isEmpty()) || parentDept != null) {
                List<Dept> depts = deptRepository.findAll();
                List<Integer> deptIds = depts.stream().filter(d -> {
                    boolean match = true;
                    if (deptName != null && !deptName.isEmpty()
                            && (d.getDeptName() == null || !d.getDeptName().contains(deptName))) {
                        match = false;
                    }
                    if (parentDept != null) {
                        boolean isParent = d.getId().equals(parentDept);
                        boolean isDescendant = false;
                        if (d.getAncestor() != null && !d.getAncestor().isEmpty()) {
                            String[] ancestors = d.getAncestor().split(",");
                            for (String anc : ancestors) {
                                if (anc.trim().equals(parentDept.toString())) {
                                    isDescendant = true;
                                    break;
                                }
                            }
                        }
                        if (!isParent && !isDescendant) {
                            match = false;
                        }
                    }
                    return match;
                }).map(Dept::getId).collect(Collectors.toList());

                if (deptIds.isEmpty()) {
                    predicates.add(cb.disjunction());
                } else {
                    predicates.add(root.get("deptId").in(deptIds));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Admin> adminPage = adminRepository.findAll(spec, pageable);
        List<Admin> admins = adminPage.getContent();

        List<Dept> allDepts = deptRepository.findAll();

        List<AdminDto> dtos = admins.stream().map(a -> {
            AdminDto dto = new AdminDto();
            dto.setId(a.getId());
            dto.setAdminName(a.getAdminName());
            dto.setAdminGender(a.getAdminGender());
            dto.setAdminPhone(a.getAdminPhone());
            dto.setAdminEmail(a.getAdminEmail());
            dto.setAdminLocation(a.getAdminLocation());

            if (a.getDeptId() != null) {
                allDepts.stream().filter(d -> d.getId().equals(a.getDeptId())).findFirst().ifPresent(d -> {
                    dto.setDeptName(d.getDeptName());
                });
            }

            dto.setAdminStatus(a.getStatus());
            dto.setCreateTime(a.getCreateTime());
            dto.setUpdateTime(a.getUpdateTime());
            return dto;
        }).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, adminPage.getTotalElements()), "操作成功");
    }

    @Override
    public Result<String> deleteAdmins(DeleteIdsReq req) {
        if (req != null && req.getIds() != null && !req.getIds().isEmpty()) {
            adminRepository.deleteAllById(req.getIds());
            return Result.success(null, "删除成功");
        }
        return Result.error("参数错误");
    }

    @Override
    public Result<AdminDetailDto> getAdminById(Integer id) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isEmpty()) {
            return Result.error(404, "管理员不存在");
        }
        Admin a = adminOpt.get();
        AdminDetailDto dto = new AdminDetailDto();
        dto.setId(a.getId());
        dto.setAdminName(a.getAdminName());
        dto.setAdminPassword("******"); // Mask password
        dto.setAdminAvatar(a.getAdminAvatar());
        dto.setAdminGender(a.getAdminGender());
        dto.setAdminPhone(a.getAdminPhone());
        dto.setAdminEmail(a.getAdminEmail());
        dto.setAdminLocation(a.getAdminLocation());
        dto.setAdminStatus(a.getStatus());
        dto.setRemark(a.getRemark());
        dto.setCreateTime(a.getCreateTime());
        dto.setUpdateTime(a.getUpdateTime());

        if (a.getDeptId() != null) {
            deptRepository.findById(a.getDeptId()).ifPresent(d -> {
                dto.setDeptName(d.getDeptName());
                dto.setDeptCode(d.getDeptCode());
                dto.setDeptAddress(d.getDeptAddress());
            });
        }
        return Result.success(dto, "操作成功");
    }

    private void populateAdminFromReq(Admin admin, AdminSaveReq req) {
        if (req.getAdminName() != null)
            admin.setAdminName(req.getAdminName());
        if (req.getAdminPassword() != null && !req.getAdminPassword().equals("******")
                && !req.getAdminPassword().isEmpty()) {
            admin.setAdminPassword(req.getAdminPassword());
        }
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
        if (req.getAdminStatus() != null)
            admin.setStatus(req.getAdminStatus());
        if (req.getRemark() != null)
            admin.setRemark(req.getRemark());

        if (req.getDeptCode() != null && !req.getDeptCode().isEmpty()) {
            deptRepository.findByDeptCode(req.getDeptCode()).ifPresent(d -> admin.setDeptId(d.getId()));
        } else if (req.getDeptName() != null && !req.getDeptName().isEmpty()) {
            deptRepository.findByDeptName(req.getDeptName()).ifPresent(d -> admin.setDeptId(d.getId()));
        }

        if (admin.getCreateTime() == null) {
            admin.setCreateTime(new Date());
        }
        admin.setUpdateTime(new Date());
    }

    @Override
    public Result<String> saveAdmin(AdminSaveReq req) {
        Admin admin = new Admin();
        if (req.getId() != null && req.getId() > 0) {
            admin = adminRepository.findById(req.getId()).orElse(new Admin());
        }
        // Ensure admin has a password since non-null constraint
        if (admin.getAdminPassword() == null) {
            admin.setAdminPassword("123456");
        }
        populateAdminFromReq(admin, req);
        adminRepository.save(admin);
        return Result.success(null, "保存成功");
    }

    @Override
    public Result<String> updateAdmin(AdminSaveReq req) {
        if (req.getId() == null || req.getId() <= 0) {
            return Result.error(400, "参数错误，ID不能为空");
        }
        Admin admin = adminRepository.findById(req.getId()).orElse(null);
        if (admin == null) {
            return Result.error(404, "管理员不存在");
        }
        populateAdminFromReq(admin, req);
        adminRepository.save(admin);
        return Result.success(null, "修改成功");
    }

    @Override
    public Result<String> addAdmin(AdminSaveReq req) {
        Admin admin = new Admin();
        if (admin.getAdminPassword() == null && (req.getAdminPassword() == null || req.getAdminPassword().isEmpty())) {
            admin.setAdminPassword("123456");
        }
        populateAdminFromReq(admin, req);
        adminRepository.save(admin);
        return Result.success(null, "提交成功");
    }
}
