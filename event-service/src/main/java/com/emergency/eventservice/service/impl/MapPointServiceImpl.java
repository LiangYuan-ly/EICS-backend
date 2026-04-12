package com.emergency.eventservice.service.impl;

import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.MapPointDto;
import com.emergency.eventservice.service.MapPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapPointServiceImpl implements MapPointService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result<Map<String, List<MapPointDto>>> getReportedPoints(String startTime, String endTime, String incidentStatus) {
        StringBuilder sql = new StringBuilder("SELECT id, longitude, latitude, incident_range, incident_title FROM reported_incidents WHERE deleted = 0 ");
        List<Object> args = new ArrayList<>();

        if (startTime != null && !startTime.isEmpty()) {
            sql.append(" AND occurrence_time >= ? ");
            args.add(startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            sql.append(" AND occurrence_time <= ? ");
            args.add(endTime);
        }
        if (incidentStatus != null && !incidentStatus.isEmpty()) {
            sql.append(" AND incident_status = ? ");
            args.add(incidentStatus);
        }

        List<MapPointDto> list = jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            MapPointDto dto = new MapPointDto();
            dto.setId(rs.getInt("id"));
            dto.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
            dto.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
            dto.setRange(rs.getObject("incident_range") != null ? rs.getInt("incident_range") : null);
            dto.setTitle(rs.getString("incident_title"));
            return dto;
        }, args.toArray());

        Map<String, List<MapPointDto>> data = new HashMap<>();
        data.put("list", list);
        return Result.success(data, "上报事件点位获取成功");
    }

    @Override
    public Result<Map<String, List<MapPointDto>>> getPublishedPoints(String startTime, String endTime, String incidentStatus) {
        StringBuilder sql = new StringBuilder("SELECT id, longitude, latitude, incident_range, incident_title FROM published_incidents WHERE deleted = 0 ");
        List<Object> args = new ArrayList<>();

        if (startTime != null && !startTime.isEmpty()) {
            sql.append(" AND occurrence_time >= ? ");
            args.add(startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            sql.append(" AND occurrence_time <= ? ");
            args.add(endTime);
        }
        if (incidentStatus != null && !incidentStatus.isEmpty()) {
            sql.append(" AND incident_status = ? ");
            args.add(incidentStatus);
        }

        List<MapPointDto> list = jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            MapPointDto dto = new MapPointDto();
            dto.setId(rs.getInt("id"));
            dto.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
            dto.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
            dto.setRange(rs.getObject("incident_range") != null ? rs.getInt("incident_range") : null);
            dto.setTitle(rs.getString("incident_title"));
            return dto;
        }, args.toArray());

        Map<String, List<MapPointDto>> data = new HashMap<>();
        data.put("list", list);
        return Result.success(data, "发布事件点位获取成功");
    }

    @Override
    public Result<Map<String, List<MapPointDto>>> getDeptPoints() {
        String sql = "SELECT id, longitude, latitude, dept_name FROM depts WHERE dept_status = 1";

        List<MapPointDto> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MapPointDto dto = new MapPointDto();
            dto.setId(rs.getInt("id"));
            dto.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
            dto.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
            dto.setTitle(rs.getString("dept_name"));
            return dto;
        });

        Map<String, List<MapPointDto>> data = new HashMap<>();
        data.put("list", list);
        return Result.success(data, "机构点位获取成功");
    }

    @Override
    public Result<Map<String, List<MapPointDto>>> getWarehousePoints() {
        String sql = "SELECT id, longitude, latitude, warehouse_name FROM warehouses WHERE warehouse_status = 1";

        List<MapPointDto> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MapPointDto dto = new MapPointDto();
            dto.setId(rs.getInt("id"));
            dto.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
            dto.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
            dto.setTitle(rs.getString("warehouse_name"));
            return dto;
        });

        Map<String, List<MapPointDto>> data = new HashMap<>();
        data.put("list", list);
        return Result.success(data, "仓库点位获取成功");
    }
}
