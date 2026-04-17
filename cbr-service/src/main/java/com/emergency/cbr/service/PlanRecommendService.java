package com.emergency.cbr.service;

import com.emergency.cbr.common.Result;
import com.emergency.cbr.dto.RecommendReq;
import com.emergency.cbr.dto.RecommendRes;
import com.emergency.cbr.entity.Plan;
import com.emergency.cbr.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanRecommendService {

    @Autowired
    private PlanRepository planRepository;

    // 相似度权重分配 (总和为 1.0)
    private static final double W_TYPE = 0.4;
    private static final double W_LEVEL = 0.2;
    private static final double W_RANGE = 0.2;
    private static final double W_TEXT = 0.2;

    public Result<RecommendRes> recommend(RecommendReq req) {
        // 直接从预案库（Plan Library）获取所有预案
        List<Plan> allPlans = planRepository.findAll();
        
        // 计算每个预案与当前事件的匹配度
        List<RecommendRes.RecommendationItem> items = allPlans.stream()
                .map(plan -> calculateSimilarity(req, plan))
                .sorted(Comparator.comparing(RecommendRes.RecommendationItem::getSimilarity_score).reversed())
                .limit(3)
                .collect(Collectors.toList());

        RecommendRes res = new RecommendRes();
        res.setList(items);
        res.setAdvice(generateAdvice(req, items));

        return Result.success(res, "成功匹配到 " + items.size() + " 个推荐预案");
    }

    private RecommendRes.RecommendationItem calculateSimilarity(RecommendReq req, Plan plan) {
        RecommendRes.RecommendationItem item = new RecommendRes.RecommendationItem();
        item.setId(plan.getId());
        item.setTitle(plan.getPlanTitle());
        item.setLevel(plan.getPlanLevel());
        item.setIncident_type(plan.getPlanType());
        
        // 使用数据库中真实的预案适配范围
        Integer planRange = plan.getPlanRange();
        if (planRange == null) planRange = 0; // 默认值处理
        item.setRange(planRange);

        // 1. 类型得分 (事件类型与预案适用类型匹配度)
        double typeScore = plan.getPlanType().equals(req.getIncident_type()) ? 1.0 : 0.0;
        
        // 2. 等级得分 (等级越接近得分越高)
        double levelDiff = Math.abs(plan.getPlanLevel() - req.getLevel());
        double levelScore = Math.max(0, 1.0 - (levelDiff / 4.0));
        
        // 3. 范围得分 (使用对数缩放计算相似度)
        double rangeScore;
        if (req.getIncident_range() != null && planRange > 0) {
            double r1 = Math.log(req.getIncident_range() + 1);
            double r2 = Math.log(planRange + 1);
            // 差异越小，分数越高
            rangeScore = Math.max(0, 1.0 - (Math.abs(r1 - r2) / 10.0));
        } else {
            rangeScore = 0.0;
        }

        // 4. 标题语义得分
        double textScore = calculateTextSimilarity(req.getTitle(), plan.getPlanTitle());

        // 综合加权得分
        double totalScore = (typeScore * W_TYPE) + (levelScore * W_LEVEL) + (rangeScore * W_RANGE) + (textScore * W_TEXT);

        item.setSimilarity_score(Math.round(totalScore * 10000.0) / 10000.0);
        item.setScore_details(new RecommendRes.ScoreDetails(typeScore, levelScore, rangeScore, textScore));
        
        // 生成匹配解释
        StringBuilder explanation = new StringBuilder();
        explanation.append(typeScore > 0.9 ? "预案类型完全对口；" : "预案类型不匹配；");
        explanation.append("等级契合度" + (int)(levelScore * 100) + "%；");
        explanation.append("语义相关性" + (int)(textScore * 100) + "%");
        item.setExplanation(explanation.toString());

        return item;
    }

    private double calculateTextSimilarity(String eventTitle, String planTitle) {
        if (eventTitle == null || planTitle == null) return 0;
        // 简单的语义相关性模拟：计算事件标题与预案名称的交集
        long count = eventTitle.chars().distinct().filter(c -> planTitle.indexOf(c) != -1).count();
        return (double) count / Math.max(eventTitle.length(), planTitle.length());
    }

    private String generateAdvice(RecommendReq req, List<RecommendRes.RecommendationItem> items) {
        if (items.isEmpty()) {
            return "未能匹配到合适的预案。建议：请手动检查预案库等级或联系指挥中心。";
        }
        
        RecommendRes.RecommendationItem best = items.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append("【决策依据】当前上报事件“").append(req.getTitle()).append("”与预案库中的《").append(best.getTitle()).append("》匹配度最高（").append((int)(best.getSimilarity_score() * 100)).append("%）。\n\n");
        
        sb.append("【专家提示】\n");
        sb.append("  1. 建议立即参照该预案的第 3 章节启动响应。\n");
        sb.append("  2. 请注意当前事件的等级为 ").append(req.getLevel()).append("，务必通知相关责任部门到场。\n");
        sb.append("  3. 若影响范围进一步扩大，请考虑升级至更高等级的总体预案。");
        
        return sb.toString();
    }
}
