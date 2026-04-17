package com.emergency.cbr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class RecommendRes {
    private List<RecommendationItem> list;
    private String advice;

    @Data
    public static class RecommendationItem {
        private Integer id; // 这里的 ID 现在代表 Plan 的 ID
        private String title; // 预案标题
        private Integer level; // 预案级别
        private Integer range; // 预案适用范围（建议在 Plan 实体中对应添加）
        private String explanation;
        private Integer incident_type; // 预案适用的事件类型
        private Double similarity_score;
        private ScoreDetails score_details;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScoreDetails {
        private Double type_score;
        private Double level_score;
        private Double range_score;
        private Double text_score;
    }
}
