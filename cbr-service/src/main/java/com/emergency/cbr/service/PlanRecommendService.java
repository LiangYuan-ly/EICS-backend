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
        if (planRange == null)
            planRange = 0; // 默认值处理
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
        double totalScore = (typeScore * W_TYPE) + (levelScore * W_LEVEL) + (rangeScore * W_RANGE)
                + (textScore * W_TEXT);

        item.setSimilarity_score(Math.round(totalScore * 10000.0) / 10000.0);
        item.setScore_details(new RecommendRes.ScoreDetails(typeScore, levelScore, rangeScore, textScore));

        StringBuilder explanation = new StringBuilder();
        explanation.append(typeScore > 0.9 ? "预案类型完全对口；" : "预案类型不匹配；");
        explanation.append("等级契合度" + (int) (levelScore * 100) + "%；");
        explanation.append("影响范围契合度" + (int) (rangeScore * 100) + "%；");
        explanation.append("语义相关性" + (int) (textScore * 100) + "%");
        item.setExplanation(explanation.toString());

        return item;
    }

    private double calculateTextSimilarity(String eventTitle, String planTitle) {
        if (eventTitle == null || planTitle == null)
            return 0;
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
        sb.append("【决策依据】当前上报事件“").append(req.getTitle()).append("”与预案库中的《").append(best.getTitle())
                .append("》匹配度最高（").append((int) (best.getSimilarity_score() * 100)).append("%）。\n\n");

        sb.append("【专家提示】\n");
        int adviceCount = 1;
        RecommendRes.ScoreDetails scores = best.getScore_details();

        // 针对类型的智能建议
        if (scores.getType_score() < 0.8) {
            sb.append("  ").append(adviceCount++).append(". 预案类型尚未出现完美匹配，建议指挥者结合实际情况对《")
                    .append(best.getTitle()).append("》的处置流程进行灵活变通。\n");
        } else {
            sb.append("  ").append(adviceCount++).append(". 该事件类型与预案高度契合，建议立即参照预案的核心章节启动应急响应。\n");
        }

        // 针对等级的智能建议
        if (req.getLevel() != null && best.getLevel() != null) {
            if (req.getLevel() > best.getLevel()) {
                sb.append("  ").append(adviceCount++).append(". ⚠️ 警告：当前事件等级（").append(req.getLevel())
                        .append("级）高于推荐预案等级（").append(best.getLevel())
                        .append("级）。原有资源可能不足，强烈建议考虑直接申请提级响应或请求增援。\n");
            } else if (req.getLevel() < best.getLevel()) {
                sb.append("  ").append(adviceCount++).append(". 💡 提示：当前事件等级低于预案级别，可根据现场态势适度缩减调派力量，避免救援资源浪费。\n");
            } else {
                sb.append("  ").append(adviceCount++).append(". 事件等级评估（").append(req.getLevel())
                        .append("级）符合预期，请同步通知相关责任组、医疗及后勤保障部门迅速就位。\n");
            }
        }

        // 针对影响范围的智能建议
        if (req.getIncident_range() != null && best.getRange() != null && best.getRange() > 0) {
            if (req.getIncident_range() > best.getRange() * 1.5) {
                sb.append("  ").append(adviceCount++).append(". ⚠️ 关注：当前上报影响范围（").append(req.getIncident_range())
                        .append("）显著超出了该预案评估基准（").append(best.getRange())
                        .append("）。请务必提前扩大周边群众疏散范围并建立外围警戒线。\n");
            }
        }

        // 兜底建议
        sb.append("  ").append(adviceCount).append(". 持续监控现场险情发展，如遇突发情况进一步恶化，请随时准备启用更高层级的总体应急预案。");

        return sb.toString();
    }
}
