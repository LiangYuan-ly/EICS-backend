package com.emergency.cbr.controller;

import com.emergency.cbr.common.Result;
import com.emergency.cbr.dto.RecommendReq;
import com.emergency.cbr.dto.RecommendRes;
import com.emergency.cbr.service.PlanRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cbr")
public class PlanRecommendController {

    @Autowired
    private PlanRecommendService recommendService;

    @PostMapping("/recommend")
    public Result<RecommendRes> getRecommendation(@RequestBody RecommendReq req) {
        return recommendService.recommend(req);
    }
}
