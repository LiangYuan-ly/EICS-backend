package com.emergency.cbr.controller;

import com.emergency.cbr.common.PageData;
import com.emergency.cbr.common.Result;
import com.emergency.cbr.dto.IdsReq;
import com.emergency.cbr.dto.PlanDto;
import com.emergency.cbr.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/emergency-schemes")
    public Result<PageData<PlanDto>> getPlans(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, name = "plan_code") String planCode,
            @RequestParam(required = false, name = "plan_title") String planTitle,
            @RequestParam(required = false, name = "plan_type") Integer planType,
            @RequestParam(required = false, name = "category_name") String categoryName,
            @RequestParam(required = false, name = "plan_level") Integer planLevel,
            @RequestParam(required = false, name = "dept_name") String deptName,
            @RequestParam(required = false) Integer status) {
        return planService.getPlans(pageNum, pageSize, planCode, planTitle, planType, categoryName, planLevel, deptName, status);
    }

    @PostMapping("/emergency-schemes/add")
    public Result<String> addPlan(@RequestBody PlanDto req) {
        return planService.addPlan(req);
    }

    @GetMapping("/emergency-schemes/{id}")
    public Result<PlanDto> getPlanById(@PathVariable("id") Integer id) {
        return planService.getPlanById(id);
    }

    @PutMapping("/emergency-schemes/update")
    public Result<String> updatePlan(@RequestBody PlanDto req) {
        return planService.updatePlan(req);
    }

    @DeleteMapping("/emergency-schemes/delplans")
    public Result<String> deletePlans(@RequestBody IdsReq req) {
        return planService.deletePlans(req);
    }

    @PostMapping("/plan/upload")
    public Result<Map<String, Object>> uploadPlan(@RequestParam("file") MultipartFile file) {
        return planService.uploadPlan(file);
    }

    @DeleteMapping("/plan/delete/{id}")
    public Result<String> deletePlanAttachment(@PathVariable("id") Integer id) {
        return planService.deletePlanAttachment(id);
    }
}
