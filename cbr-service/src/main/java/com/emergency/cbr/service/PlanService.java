package com.emergency.cbr.service;

import com.emergency.cbr.common.PageData;
import com.emergency.cbr.common.Result;
import com.emergency.cbr.dto.IdsReq;
import com.emergency.cbr.dto.PlanDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface PlanService {
    Result<PageData<PlanDto>> getPlans(Integer pageNum, Integer pageSize, String planCode, String planTitle, Integer planType, String categoryName, Integer planLevel, String deptName, Integer status);
    Result<String> addPlan(PlanDto req);
    Result<String> updatePlan(PlanDto req);
    Result<String> deletePlans(IdsReq req);
    Result<PlanDto> getPlanById(Integer id);
    Result<Map<String, Object>> uploadPlan(MultipartFile file);
    Result<String> deletePlanAttachment(Integer id);
}
