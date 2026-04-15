package com.emergency.eventservice.service.impl;

import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.DistributionDto;
import com.emergency.eventservice.dto.PublishTrendDto;
import com.emergency.eventservice.dto.ReportTrendDto;
import com.emergency.eventservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 安全提取日期的通用方法，防止前端传入的时间格式导致解析报错
     */
    private LocalDate parseToLocalDate(String dateStr, LocalDate defaultDate) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return defaultDate;
        }
        try {
            if (dateStr.contains("T")) {
                return OffsetDateTime.parse(dateStr).toLocalDate();
            } else if (dateStr.length() > 10) {
                // 截取前面部分，处理 "2024-01-01 12:00:00" 这类格式
                return LocalDate.parse(dateStr.substring(0, 10));
            } else {
                return LocalDate.parse(dateStr);
            }
        } catch (Exception e) {
            return defaultDate;
        }
    }

    @Override
    public Result<DistributionDto> getDistribution(String date) {
        StringBuilder sql = new StringBuilder("SELECT incident_status, COUNT(*) as cnt FROM reported_incidents WHERE deleted = 0 ");
        List<Object> args = new ArrayList<>();

        if (date != null && !date.isEmpty()) {
            LocalDate targetDate = parseToLocalDate(date, LocalDate.now());
            // 使用纯日期进行精确匹配
            sql.append(" AND DATE(create_time) = ? ");
            args.add(targetDate.toString());
        }
        sql.append(" GROUP BY incident_status");

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString(), args.toArray());

        int passed = 0;
        int failed = 0;
        int pending = 0;

        for (Map<String, Object> row : rows) {
            Integer status = (Integer) row.get("incident_status");
            int count = ((Number) row.get("cnt")).intValue();

            // 【重点提醒】：此处代码认定 incident_status: 3=审核通过, 4=审核不通过
            // 如果你的业务实际定义是 1=通过, 2=不通过，请务必修改这里的数字！
            if (status != null && status == 3) {
                passed += count;
            } else if (status != null && status == 4) {
                failed += count;
            } else {
                pending += count;
            }
        }

        int total = passed + failed + pending;

        DistributionDto dto = new DistributionDto();
        dto.setReported(total);
        dto.setPassed(passed);
        dto.setFailed(failed);
        dto.setPending(pending);

        return Result.success(dto, "某日上报分布获取成功");
    }

    @Override
    public Result<ReportTrendDto> getReportTrend(String startDate, String endDate) {
        LocalDate end = parseToLocalDate(endDate, LocalDate.now());
        LocalDate start = parseToLocalDate(startDate, end.minusDays(6));

        String sql = "SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as dt, incident_status, COUNT(*) as cnt " +
                " FROM reported_incidents WHERE deleted = 0 AND DATE(create_time) >= ? AND DATE(create_time) <= ? " +
                " GROUP BY dt, incident_status";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, start.toString(), end.toString());

        List<String> dates = new ArrayList<>();
        List<Integer> reportedSeries = new ArrayList<>();
        List<Integer> passedSeries = new ArrayList<>();
        List<Integer> failedSeries = new ArrayList<>();

        // 每天遍历，确保图表某一天数据为 0 时也能正常渲染折线，不断层
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            String dtStr = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(dtStr);

            int p = 0, f = 0, pend = 0;
            for (Map<String, Object> row : rows) {
                if (dtStr.equals(String.valueOf(row.get("dt")))) {
                    Integer s = (Integer) row.get("incident_status");
                    int cnt = ((Number) row.get("cnt")).intValue();

                    // 【注意】状态值需和实际字典表对应
                    if (s != null && s == 3) p += cnt;
                    else if (s != null && s == 4) f += cnt;
                    else pend += cnt;
                }
            }
            reportedSeries.add(p + f + pend); // 上报总数 = 通过 + 驳回 + 待审核
            passedSeries.add(p);
            failedSeries.add(f);
        }

        ReportTrendDto dto = new ReportTrendDto();
        dto.setDates(dates);
        dto.setReportedSeries(reportedSeries);
        dto.setPassedSeries(passedSeries);
        dto.setFailedSeries(failedSeries);

        return Result.success(dto, "上报事件趋势获取成功");
    }

    @Override
    public Result<PublishTrendDto> getPublishTrend(String startDate, String endDate) {
        LocalDate end = parseToLocalDate(endDate, LocalDate.now());
        LocalDate start = parseToLocalDate(startDate, end.minusDays(6));

        // 此处只统计发布状态 (默认代码认定 3 为已发布状态)
        String sql = "SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as dt, COUNT(*) as cnt " +
                " FROM published_incidents WHERE deleted = 0 AND incident_status = 3 AND DATE(create_time) >= ? AND DATE(create_time) <= ? " +
                " GROUP BY dt";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, start.toString(), end.toString());

        List<String> dates = new ArrayList<>();
        List<Integer> publishSeries = new ArrayList<>();

        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            String dtStr = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(dtStr);

            int pub = 0;
            for (Map<String, Object> row : rows) {
                if (dtStr.equals(String.valueOf(row.get("dt")))) {
                    pub = ((Number) row.get("cnt")).intValue();
                }
            }
            publishSeries.add(pub);
        }

        PublishTrendDto dto = new PublishTrendDto();
        dto.setDates(dates);
        dto.setPublishSeries(publishSeries);

        return Result.success(dto, "发布事件趋势获取成功");
    }
}