package com.emergency.auth.service.impl;

import com.emergency.auth.dto.PageData;
import com.emergency.auth.common.Result;
import com.emergency.auth.dto.*;
import com.emergency.auth.entity.Dept;
import com.emergency.auth.repository.DeptRepository;
import com.emergency.auth.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptRepository deptRepository;

    @Override
    public Result<PageData<DeptDto>> getDepts(Integer pageNum, Integer pageSize, String deptName, String deptPerson,
            String deptPhone, String deptAddress, String parentName, Integer deptId, Integer deptStatus) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Specification<Dept> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (deptName != null && !deptName.isEmpty()) {
                predicates.add(cb.like(root.get("deptName"), "%" + deptName + "%"));
            }
            if (deptPerson != null && !deptPerson.isEmpty()) {
                predicates.add(cb.like(root.get("deptPerson"), "%" + deptPerson + "%"));
            }
            if (deptPhone != null && !deptPhone.isEmpty()) {
                predicates.add(cb.like(root.get("deptPhone"), "%" + deptPhone + "%"));
            }
            if (deptAddress != null && !deptAddress.isEmpty()) {
                predicates.add(cb.like(root.get("deptAddress"), "%" + deptAddress + "%"));
            }
            if (deptId != null) {
                predicates.add(cb.equal(root.get("id"), deptId));
            }
            if (deptStatus != null) {
                predicates.add(cb.equal(root.get("deptStatus"), deptStatus));
            }
            if (parentName != null && !parentName.isEmpty()) {
                List<Dept> depts = deptRepository.findAll();
                List<Integer> pids = depts.stream()
                        .filter(d -> d.getDeptName() != null && d.getDeptName().contains(parentName))
                        .map(Dept::getId)
                        .collect(Collectors.toList());
                if (pids.isEmpty()) {
                    predicates.add(cb.disjunction());
                } else {
                    predicates.add(root.get("parentDept").in(pids));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Dept> deptPage = deptRepository.findAll(spec, pageable);
        List<Dept> allDepts = deptRepository.findAll();

        List<DeptDto> dtos = deptPage.getContent().stream().map(d -> {
            DeptDto dto = new DeptDto();
            dto.setId(d.getId());
            dto.setDeptName(d.getDeptName());
            dto.setDeptCode(d.getDeptCode());
            dto.setDeptStatus(d.getDeptStatus());
            dto.setDeptPerson(d.getDeptPerson());
            dto.setDeptPhone(d.getDeptPhone());
            dto.setDeptAddress(d.getDeptAddress());
            dto.setDeptResponsibility(d.getDeptResponsibility());
            dto.setLongitude(d.getLongitude());
            dto.setLatitude(d.getLatitude());

            if (d.getParentDept() != null && d.getParentDept() != 0) {
                allDepts.stream().filter(p -> p.getId().equals(d.getParentDept())).findFirst().ifPresent(p -> {
                    dto.setParentName(p.getDeptName());
                });
            } else {
                dto.setParentName("总公司");
            }
            return dto;
        }).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, deptPage.getTotalElements()), "查询成功");
    }

    @Override
    public Result<String> deleteDepts(DeleteIdsReq req) {
        if (req != null && req.getIds() != null && !req.getIds().isEmpty()) {
            deptRepository.deleteAllById(req.getIds());
            return Result.success(null, "删除成功");
        }
        return Result.error("参数错误");
    }

    @Override
    public Result<PageData<DeptBasicDto>> getParents(String parentName) {
        Specification<Dept> spec = (root, query, cb) -> {
            if (parentName != null && !parentName.isEmpty()) {
                return cb.like(root.get("deptName"), "%" + parentName + "%");
            }
            return cb.conjunction();
        };
        List<Dept> depts = deptRepository.findAll(spec);
        List<DeptBasicDto> dtos = depts.stream().map(d -> {
            DeptBasicDto dto = new DeptBasicDto();
            dto.setId(d.getId());
            dto.setDeptName(d.getDeptName());
            dto.setDeptCode(d.getDeptCode());
            dto.setDeptAddress(d.getDeptAddress());
            return dto;
        }).collect(Collectors.toList());
        return Result.success(new PageData<>(dtos, (long) dtos.size()), "查询成功");
    }

    private void fillDeptFromReq(Dept dept, DeptSaveReq req) {
        if (req.getDeptName() != null)
            dept.setDeptName(req.getDeptName());
        if (req.getDeptCode() != null)
            dept.setDeptCode(req.getDeptCode());
        if (req.getDeptStatus() != null)
            dept.setDeptStatus(req.getDeptStatus());
        if (req.getDeptPerson() != null)
            dept.setDeptPerson(req.getDeptPerson());
        if (req.getDeptPhone() != null)
            dept.setDeptPhone(req.getDeptPhone());
        if (req.getDeptAddress() != null)
            dept.setDeptAddress(req.getDeptAddress());
        if (req.getDeptResponsibility() != null)
            dept.setDeptResponsibility(req.getDeptResponsibility());
        if (req.getLongitude() != null)
            dept.setLongitude(req.getLongitude());
        if (req.getLatitude() != null)
            dept.setLatitude(req.getLatitude());

        if (req.getParentName() != null && !req.getParentName().isEmpty()) {
            deptRepository.findByDeptName(req.getParentName()).ifPresent(p -> dept.setParentDept(p.getId()));
        } else {
            if (dept.getParentDept() == null) {
                dept.setParentDept(0);
            }
        }
    }

    @Override
    public Result<String> addDept(DeptSaveReq req) {
        Dept dept = new Dept();
        fillDeptFromReq(dept, req);
        deptRepository.save(dept);
        return Result.success(null, "新增机构成功");
    }

    @Override
    public Result<String> updateDept(DeptSaveReq req) {
        if (req.getId() == null || req.getId() <= 0)
            return Result.error("缺少ID");
        Dept dept = deptRepository.findById(req.getId()).orElse(null);
        if (dept == null)
            return Result.error("机构不存在");
        fillDeptFromReq(dept, req);
        deptRepository.save(dept);
        return Result.success(null, "修改机构成功");
    }

    @Override
    public Result<List<DeptTreeDto>> getDeptTree() {
        List<Dept> depts = deptRepository.findAll();
        Map<Integer, List<Dept>> grouped = depts.stream()
                .collect(Collectors.groupingBy(d -> d.getParentDept() != null ? d.getParentDept() : 0));

        List<DeptTreeDto> roots = buildTree(grouped, 0);
        return Result.success(roots, "操作成功");
    }

    private List<DeptTreeDto> buildTree(Map<Integer, List<Dept>> grouped, Integer parentId) {
        List<Dept> children = grouped.getOrDefault(parentId, new ArrayList<>());
        if (children.isEmpty())
            return null;

        return children.stream().map(d -> {
            DeptTreeDto dto = new DeptTreeDto();
            dto.setId(String.valueOf(d.getId()));
            dto.setDeptName(d.getDeptName());
            dto.setChildren(buildTree(grouped, d.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Result<PageData<DeptBasicDto>> searchDepts(Integer pageSize, String deptName) {
        Specification<Dept> spec = (root, query, cb) -> {
            if (deptName != null && !deptName.isEmpty()) {
                return cb.or(
                        cb.like(root.get("deptName"), "%" + deptName + "%"),
                        cb.like(root.get("deptCode"), "%" + deptName + "%"));
            }
            return cb.conjunction();
        };
        Page<Dept> page = deptRepository.findAll(spec,
                PageRequest.of(0, pageSize != null && pageSize > 0 ? pageSize : 10));
        List<DeptBasicDto> dtos = page.getContent().stream().map(d -> {
            DeptBasicDto dto = new DeptBasicDto();
            dto.setId(d.getId());
            dto.setDeptName(d.getDeptName());
            dto.setDeptCode(d.getDeptCode());
            dto.setDeptAddress(d.getDeptAddress());
            return dto;
        }).collect(Collectors.toList());
        return Result.success(new PageData<>(dtos, page.getTotalElements()), "查询成功");
    }
}
