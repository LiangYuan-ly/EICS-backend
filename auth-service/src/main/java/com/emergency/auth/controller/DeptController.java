package com.emergency.auth.controller;

import com.emergency.auth.dto.PageData;
import com.emergency.auth.common.Result;
import com.emergency.auth.dto.*;
import com.emergency.auth.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/deptslist")
    public Result<PageData<DeptDto>> getDepts(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, name = "dept_name") String deptName,
            @RequestParam(required = false, name = "dept_person") String deptPerson,
            @RequestParam(required = false, name = "dept_phone") String deptPhone,
            @RequestParam(required = false, name = "dept_address") String deptAddress,
            @RequestParam(required = false, name = "parent_name") String parentName,
            @RequestParam(required = false, name = "dept_id") Integer deptId) {
        return deptService.getDepts(pageNum, pageSize, deptName, deptPerson, deptPhone, deptAddress, parentName, deptId);
    }

    @DeleteMapping("/admin/deldepts")
    public Result<String> deleteDepts(@RequestBody DeleteIdsReq req) {
        return deptService.deleteDepts(req);
    }

    @GetMapping("/depts/parents")
    public Result<PageData<DeptBasicDto>> getParents(
            @RequestParam(required = false, name = "parent_name") String parentName) {
        return deptService.getParents(parentName);
    }

    @PostMapping("/depts/add")
    public Result<String> addDept(@RequestBody DeptSaveReq req) {
        return deptService.addDept(req);
    }

    @PutMapping("/depts/update")
    public Result<String> updateDept(@RequestBody DeptSaveReq req) {
        return deptService.updateDept(req);
    }

    @GetMapping("/depts/tree")
    public Result<List<DeptTreeDto>> getDeptTree() {
        return deptService.getDeptTree();
    }

    @GetMapping("/depts")
    public Result<PageData<DeptBasicDto>> searchDepts(
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, name = "dept_name") String deptName) {
        return deptService.searchDepts(pageSize, deptName);
    }
}
