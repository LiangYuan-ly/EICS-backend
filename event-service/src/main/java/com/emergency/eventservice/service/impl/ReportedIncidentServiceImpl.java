package com.emergency.eventservice.service.impl;

import com.emergency.eventservice.common.PageData;
import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.IncidentDetailDto;
import com.emergency.eventservice.dto.IncidentDto;
import com.emergency.eventservice.dto.ReviewReq;
import com.emergency.eventservice.entity.Admin;
import com.emergency.eventservice.entity.ReportedIncident;
import com.emergency.eventservice.entity.User;
import com.emergency.eventservice.repository.AdminRepository;
import com.emergency.eventservice.repository.ReportedIncidentRepository;
import com.emergency.eventservice.repository.UserRepository;
import com.emergency.eventservice.service.ReportedIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class ReportedIncidentServiceImpl implements ReportedIncidentService {

    @Autowired
    private ReportedIncidentRepository reportedIncidentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Result<PageData<IncidentDto>> getReportedIncidents(Integer pageSize, Integer pageNum, String incidentTitle, Integer incidentType, String incidentLocation, Integer incidentRange, String startTime, String endTime, Integer incidentStatus) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Specification<ReportedIncident> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));

            if (incidentTitle != null && !incidentTitle.isEmpty()) {
                predicates.add(cb.like(root.get("incidentTitle"), "%" + incidentTitle + "%"));
            }
            if (incidentType != null) {
                predicates.add(cb.equal(root.get("incidentType"), incidentType));
            }
            if (incidentLocation != null && !incidentLocation.isEmpty()) {
                predicates.add(cb.like(root.get("incidentLocation"), "%" + incidentLocation + "%"));
            }
            if (incidentRange != null) {
                predicates.add(cb.equal(root.get("incidentRange"), incidentRange));
            }
            if (incidentStatus != null) {
                predicates.add(cb.equal(root.get("incidentStatus"), incidentStatus));
            }

            try {
                if (startTime != null && !startTime.isEmpty()) {
                    Date start = startTime.contains("T") ? Date.from(Instant.parse(startTime)) : 
                                 new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
                    predicates.add(cb.greaterThanOrEqualTo(root.get("occurrenceTime"), start));
                }
                if (endTime != null && !endTime.isEmpty()) {
                    Date end = endTime.contains("T") ? Date.from(Instant.parse(endTime)) : 
                               new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
                    predicates.add(cb.lessThanOrEqualTo(root.get("occurrenceTime"), end));
                }
            } catch (Exception e) {
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ReportedIncident> incidentPage = reportedIncidentRepository.findAll(spec, pageable);
        List<User> users = userRepository.findAll();

        List<IncidentDto> dtos = incidentPage.getContent().stream().map(inc -> {
            IncidentDto dto = new IncidentDto();
            dto.setId(inc.getId());
            dto.setIncidentTitle(inc.getIncidentTitle());
            dto.setIncidentType(inc.getIncidentType());
            dto.setIncidentLocation(inc.getIncidentLocation());
            dto.setIncidentRange(inc.getIncidentRange());
            dto.setOccurrenceTime(inc.getOccurrenceTime());
            dto.setCreateTime(inc.getCreateTime());
            dto.setIncidentStatus(inc.getIncidentStatus());

            if (inc.getUserId() != null) {
                users.stream().filter(u -> u.getId().equals(inc.getUserId())).findFirst().ifPresent(u -> {
                    dto.setUname(u.getUname());
                });
            }
            return dto;
        }).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, incidentPage.getTotalElements()), "查询成功");
    }

    private IncidentDetailDto mapToDetailDto(ReportedIncident inc, List<User> users, List<Admin> admins) {
        IncidentDetailDto dto = new IncidentDetailDto();
        dto.setId(inc.getId());
        dto.setIncidentTitle(inc.getIncidentTitle());
        dto.setIncidentType(inc.getIncidentType());
        dto.setIncidentContent(inc.getIncidentContent());
        dto.setIncidentLocation(inc.getIncidentLocation());
        dto.setIncidentRange(inc.getIncidentRange());
        dto.setOccurrenceTime(inc.getOccurrenceTime());
        dto.setCreateTime(inc.getCreateTime());
        dto.setUpdateTime(inc.getUpdateTime());
        dto.setIncidentStatus(inc.getIncidentStatus());
        dto.setReview(inc.getReview());
        dto.setLongitude(inc.getLongitude());
        dto.setLatitude(inc.getLatitude());

        if (inc.getUserId() != null) {
            users.stream().filter(u -> u.getId().equals(inc.getUserId())).findFirst().ifPresent(u -> {
                dto.setUname(u.getUname());
            });
        }
        if (inc.getAdminId() != null) {
            admins.stream().filter(a -> a.getId().equals(inc.getAdminId())).findFirst().ifPresent(a -> {
                dto.setAdminName(a.getAdminName());
            });
        }
        return dto;
    }

    @Override
    public Result<IncidentDetailDto> getIncidentDetail(Integer id) {
        ReportedIncident inc = reportedIncidentRepository.findById(id).orElse(null);
        if (inc == null || (inc.getDeleted() != null && inc.getDeleted() == 1)) {
            return Result.error("200", "未查找到相关事件");
        }
        List<User> users = userRepository.findAll();
        List<Admin> admins = adminRepository.findAll();
        
        return Result.success(mapToDetailDto(inc, users, admins));
    }

    @Override
    public Result<String> reviewIncident(Integer id, ReviewReq req) {
        ReportedIncident inc = reportedIncidentRepository.findById(id).orElse(null);
        if (inc == null || (inc.getDeleted() != null && inc.getDeleted() == 1)) {
            return Result.error("未查找到相关事件");
        }
        if (req.getAudit() != null) {
            inc.setIncidentStatus(req.getAudit());
        }
        if (req.getReview() != null) {
            inc.setReview(req.getReview());
        }
        inc.setUpdateTime(new Date());
        reportedIncidentRepository.save(inc);
        return Result.success(null, "提交成功");
    }

    @Override
    public Result<PageData<IncidentDetailDto>> getIncidentDetails(String ids) {
        try {
            List<Integer> idList = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<ReportedIncident> incidents = reportedIncidentRepository.findAllById(idList)
                    .stream()
                    .filter(i -> i.getDeleted() == null || i.getDeleted() == 0)
                    .collect(Collectors.toList());

            List<User> users = userRepository.findAll();
            List<Admin> admins = adminRepository.findAll();

            List<IncidentDetailDto> dtos = incidents.stream()
                    .map(inc -> mapToDetailDto(inc, users, admins))
                    .collect(Collectors.toList());

            return Result.success(new PageData<>(dtos, (long) dtos.size()), "查询成功");
        } catch (Exception e) {
            return Result.error("参数解析错误");
        }
    }
}
