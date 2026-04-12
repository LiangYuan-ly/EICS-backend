package com.emergency.userservice.service.impl;

import com.emergency.userservice.common.PageData;
import com.emergency.userservice.common.Result;
import com.emergency.userservice.dto.*;
import com.emergency.userservice.entity.Dept;
import com.emergency.userservice.entity.User;
import com.emergency.userservice.repository.DeptRepository;
import com.emergency.userservice.repository.UserRepository;
import com.emergency.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Override
    public Result<PageData<UserListDto>> getUsers(Integer pageNum, Integer pageSize, String uname, String startTime, String endTime, String phone, String location, String deptName, Integer status) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (uname != null && !uname.isEmpty()) {
                predicates.add(cb.like(root.get("uname"), "%" + uname + "%"));
            }
            if (phone != null && !phone.isEmpty()) {
                predicates.add(cb.like(root.get("phone"), "%" + phone + "%"));
            }
            if (location != null && !location.isEmpty()) {
                predicates.add(cb.like(root.get("location"), "%" + location + "%"));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (startTime != null && !startTime.isEmpty()) {
                    Date start = sdf.parse(startTime);
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), start));
                }
                if (endTime != null && !endTime.isEmpty()) {
                    Date end = sdf.parse(endTime);
                    predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), end));
                }
            } catch (Exception e) {
                // Ignore parse errors
            }

            if (deptName != null && !deptName.isEmpty()) {
                List<Dept> depts = deptRepository.findAll();
                List<Integer> deptIds = depts.stream()
                        .filter(d -> d.getDeptName() != null && d.getDeptName().contains(deptName))
                        .map(Dept::getId)
                        .collect(Collectors.toList());

                if (deptIds.isEmpty()) {
                    predicates.add(cb.disjunction());
                } else {
                    predicates.add(root.get("deptid").in(deptIds));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<User> userPage = userRepository.findAll(spec, pageable);
        List<User> users = userPage.getContent();
        
        List<Dept> allDepts = deptRepository.findAll();

        List<UserListDto> dtos = users.stream().map(u -> {
            UserListDto dto = new UserListDto();
            dto.setId(u.getId());
            dto.setUname(u.getUname());
            dto.setPhone(u.getPhone());
            dto.setLocation(u.getLocation());
            dto.setStatus(u.getStatus());
            dto.setCreateTime(u.getCreateTime());
            dto.setUpdateTime(u.getUpdateTime());

            if (u.getDeptid() != null) {
                allDepts.stream().filter(d -> d.getId().equals(u.getDeptid())).findFirst().ifPresent(d -> {
                    dto.setDeptName(d.getDeptName());
                    dto.setDeptAddress(d.getDeptAddress());
                });
            } else {
                dto.setDeptName("");
                dto.setDeptAddress("");
            }

            return dto;
        }).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, userPage.getTotalElements()), "查询成功");
    }

    @Override
    public Result<String> deleteUsers(DeleteIdsReq req) {
        if (req != null && req.getIds() != null && !req.getIds().isEmpty()) {
            userRepository.deleteAllById(req.getIds());
            return Result.success(null, "删除成功");
        }
        return Result.error("参数错误");
    }

    @Override
    public Result<String> updateUser(UserUpdateReq req) {
        if (req.getId() == null || req.getId() <= 0) {
            return Result.error(400, "参数错误，ID不能为空");
        }
        User user = userRepository.findById(req.getId()).orElse(null);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        
        if (req.getUserid() != null) user.setUserid(req.getUserid());
        if (req.getUname() != null) user.setUname(req.getUname());
        if (req.getAvatar() != null) user.setAvatar(req.getAvatar());
        if (req.getGender() != null) user.setGender(req.getGender());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getLocation() != null) user.setLocation(req.getLocation());
        if (req.getStatus() != null) user.setStatus(req.getStatus());

        if (req.getDeptName() != null && !req.getDeptName().isEmpty()) {
            deptRepository.findByDeptName(req.getDeptName()).ifPresent(d -> user.setDeptid(d.getId()));
        }

        user.setUpdateTime(new Date());
        userRepository.save(user);

        return Result.success(null, "修改成功");
    }

    @Override
    public Result<UserDetailDto> getUserById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return Result.error(404, "用户不存在");
        }
        User u = userOpt.get();
        UserDetailDto dto = new UserDetailDto();
        dto.setId(u.getId());
        dto.setUserid(u.getUserid());
        dto.setUname(u.getUname());
        dto.setAvatar(u.getAvatar());
        dto.setGender(u.getGender());
        dto.setPhone(u.getPhone());
        dto.setLocation(u.getLocation());
        dto.setStatus(u.getStatus());

        if (u.getDeptid() != null) {
            deptRepository.findById(u.getDeptid()).ifPresent(d -> {
                dto.setDeptName(d.getDeptName());
                dto.setDeptAddress(d.getDeptAddress());
            });
        } else {
            dto.setDeptName("");
            dto.setDeptAddress("");
        }

        return Result.success(dto, "操作成功");
    }
}
