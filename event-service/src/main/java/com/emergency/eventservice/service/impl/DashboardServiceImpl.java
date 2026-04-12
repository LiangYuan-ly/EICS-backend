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

    @Override
    public Result<DistributionDto> getDistribution(String date) {
        StringBuilder sql = new StringBuilder("SELECT incident_status, COUNT(*) as cnt FROM reported_incidents WHERE deleted = 0 ");
        List<Object> args = new ArrayList<>();
        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(create_time) = ? ");
            args.add(date);
        }
        sql.append(" GROUP BY incident_status");

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString(), args.toArray());

        int passed = 0;
        int failed = 0;
        int pending = 0;

        for (Map<String, Object> row : rows) {
            Integer status = (Integer) row.get("incident_status");
            int count = ((Number) row.get("cnt")).intValue();
            
            if (status != null && status == 3) passed += count;
            else if (status != null && status == 4) failed += count;
            else pending += count; 
        }

        int total = passed + failed + pending;

        DistributionDto dto = new DistributionDto();
        dto.setReported(total);
        dto.setPassed(passed);
        dto.setFailed(failed);
        dto.setPending(pending);

        return Result.success(dto, "success");
    }

    @Override
    public Result<ReportTrendDto> getReportTrend(String startDate, String endDate) {
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? 
            (endDate.contains("T") ? OffsetDateTime.parse(endDate).toLocalDate() : LocalDate.parse(endDate)) : 
            LocalDate.now();
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? 
            (startDate.contains("T") ? OffsetDateTime.parse(startDate).toLocalDate() : LocalDate.parse(startDate)) : 
            end.minusDays(6);

        String sql = "SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as dt, incident_status, COUNT(*) as cnt " +
                " FROM reported_incidents WHERE deleted = 0 AND DATE(create_time) >= ? AND DATE(create_time) <= ? " +
                " GROUP BY dt, incident_status";
        
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, start.toString(), end.toString());

        List<String> dates = new ArrayList<>();
        List<Integer> reportedSeries = new ArrayList<>();
        List<Integer> passedSeries = new ArrayList<>();
        List<Integer> failedSeries = new ArrayList<>();

        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            String dtStr = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(dtStr);

            int p = 0, f = 0, pend = 0;
            for (Map<String, Object> row : rows) {
                if (dtStr.equals(row.get("dt").toString())) {
                    Integer s = (Integer) row.get("incident_status");
                    int cnt = ((Number) row.get("cnt")).intValue();
                    if (s != null && s == 3) p += cnt;
                    else if (s != null && s == 4) f += cnt;
                    else pend += cnt;
                }
            }
            reportedSeries.add(p + f + pend);
            passedSeries.add(p);
            failedSeries.add(f);
        }

        ReportTrendDto dto = new ReportTrendDto();
        dto.setDates(dates);
        dto.setReportedSeries(reportedSeries);
        dto.setPassedSeries(passedSeries);
        dto.setFailedSeries(failedSeries);

        return Result.success(dto, "success");
    }

    @Override
    public Result<PublishTrendDto> getPublishTrend(String startDate, String endDate) {
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? 
            (endDate.contains("T") ? OffsetDateTime.parse(endDate).toLocalDate() : LocalDate.parse(endDate)) : 
            LocalDate.now();
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? 
            (startDate.contains("T") ? OffsetDateTime.parse(startDate).toLocalDate() : LocalDate.parse(startDate)) : 
            end.minusDays(6);

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
                if (dtStr.equals(row.get("dt").toString())) {
                    pub = ((Number) row.get("cnt")).intValue();
                }
            }
            publishSeries.add(pub);
        }

        PublishTrendDto dto = new PublishTrendDto();
        dto.setDates(dates);
        dto.setPublishSeries(publishSeries);

        return Result.success(dto, "success");
    }
}
