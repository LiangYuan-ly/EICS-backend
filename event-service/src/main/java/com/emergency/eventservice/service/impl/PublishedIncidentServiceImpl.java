package com.emergency.eventservice.service.impl;

import com.emergency.eventservice.common.PageData;
import com.emergency.eventservice.common.Result;
import com.emergency.eventservice.dto.*;
import com.emergency.eventservice.entity.Admin;
import com.emergency.eventservice.entity.Dept;
import com.emergency.eventservice.entity.PublishedAttachment;
import com.emergency.eventservice.entity.PublishedIncident;
import com.emergency.eventservice.repository.AdminRepository;
import com.emergency.eventservice.repository.DeptRepository;
import com.emergency.eventservice.repository.PublishedAttachmentRepository;
import com.emergency.eventservice.repository.PublishedIncidentRepository;
import com.emergency.eventservice.service.PublishedIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;

import jakarta.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;

import org.springframework.data.domain.Sort;

@Service
public class PublishedIncidentServiceImpl implements PublishedIncidentService {

    @Autowired
    private PublishedIncidentRepository publishedIncidentRepository;

    @Autowired
    private PublishedAttachmentRepository publishedAttachmentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Override
    public Result<String> addIncident(PublishedIncidentSaveReq req) {
        PublishedIncident inc = new PublishedIncident();
        inc.setIncidentTitle(req.getIncidentTitle());
        inc.setIncidentType(req.getIncidentType());
        inc.setIncidentLevel(req.getIncidentLevel());
        inc.setIncidentContent(req.getIncidentContent());
        inc.setIncidentLocation(req.getIncidentLocation());
        inc.setLongitude(req.getLongitude());
        inc.setLatitude(req.getLatitude());
        inc.setIncidentRange(req.getIncidentRange());
        inc.setOccurrenceTime(req.getOccurrenceTime());
        inc.setIncidentStatus(req.getIncidentStatus() != null ? req.getIncidentStatus() : 2);
        inc.setRemark(req.getRemark());
        inc.setAdminId(1); // placeholder admin_id
        inc.setCreateTime(new Date());
        inc.setDeleted(0);
        publishedIncidentRepository.save(inc);

        saveAttachments(inc.getId(), req.getAttachments());
        return Result.success(null, "新增成功");
    }

    private void saveAttachments(Integer targetId, List<String> attachmentUrls) {
        if (attachmentUrls != null && !attachmentUrls.isEmpty()) {
            for (String url : attachmentUrls) {
                PublishedAttachment pa = new PublishedAttachment();
                pa.setTargetId(targetId);
                pa.setTargetType("PUBLISH");
                pa.setAttachmentUrl(url);
                String name = url.substring(url.lastIndexOf("/") + 1);
                pa.setAttachmentName(name);
                pa.setAttachmentType(1); // Default type
                pa.setCreateTime(new Date());
                publishedAttachmentRepository.save(pa);
            }
        }
    }

    @Override
    public Result<PageData<PublishedIncidentListDto>> getIncidents(Integer pageNum, Integer pageSize, String incidentTitle, Integer incidentType, Integer incidentLevel, String incidentLocation, Integer incidentRange, String startTime, String endTime, String incidentStatus) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Sort sort = Sort.by(Sort.Direction.DESC, "occurrenceTime");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<PublishedIncident> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));

            if (incidentTitle != null && !incidentTitle.isEmpty()) {
                predicates.add(cb.like(root.get("incidentTitle"), "%" + incidentTitle + "%"));
            }
            if (incidentType != null) {
                predicates.add(cb.equal(root.get("incidentType"), incidentType));
            }
            if (incidentLevel != null) {
                predicates.add(cb.equal(root.get("incidentLevel"), incidentLevel));
            }
            if (incidentLocation != null && !incidentLocation.isEmpty()) {
                predicates.add(cb.like(root.get("incidentLocation"), "%" + incidentLocation + "%"));
            }
            if (incidentRange != null) {
                if (incidentRange == 3) {
                    // 小于等于3米
                    predicates.add(cb.lessThanOrEqualTo(root.get("incidentRange"), 3));
                } else if (incidentRange == 10) {
                    // 3米到10米 (大于3且小于等于10)
                    predicates.add(cb.and(
                            cb.greaterThan(root.get("incidentRange"), 3),
                            cb.lessThanOrEqualTo(root.get("incidentRange"), 10)
                    ));
                } else if (incidentRange == 100) {
                    // 10米到100米 (大于10且小于等于100)
                    predicates.add(cb.and(
                            cb.greaterThan(root.get("incidentRange"), 10),
                            cb.lessThanOrEqualTo(root.get("incidentRange"), 100)
                    ));
                } else if (incidentRange == 101) {
                    // 100米以上
                    predicates.add(cb.greaterThan(root.get("incidentRange"), 100));
                }
            }
            if (incidentStatus != null && !incidentStatus.isEmpty()) {
                predicates.add(cb.equal(root.get("incidentStatus"), Integer.parseInt(incidentStatus)));
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
            } catch (Exception e) {}

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<PublishedIncident> paged = publishedIncidentRepository.findAll(spec, pageable);
        List<PublishedIncidentListDto> dtos = paged.getContent().stream().map(inc -> {
            PublishedIncidentListDto dto = new PublishedIncidentListDto();
            dto.setId(inc.getId());
            dto.setIncidentTitle(inc.getIncidentTitle());
            dto.setIncidentType(inc.getIncidentType());
            dto.setIncidentLevel(inc.getIncidentLevel());
            dto.setIncidentLocation(inc.getIncidentLocation());
            dto.setIncidentRange(inc.getIncidentRange());
            dto.setOccurrenceTime(inc.getOccurrenceTime());
            dto.setCreateTime(inc.getCreateTime());
            dto.setIncidentStatus(inc.getIncidentStatus());
            return dto;
        }).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, paged.getTotalElements()), "查询成功");
    }

    @Override
    public Result<String> publishIncidents(IdsReq req) {
        if (req != null && req.getIds() != null) {
            for (Integer id : req.getIds()) {
                publishedIncidentRepository.findById(id).ifPresent(inc -> {
                    inc.setIncidentStatus(3); // Published
                    inc.setPublishId(1); // Placeholder admin publishId
                    inc.setUpdateTime(new Date());
                    publishedIncidentRepository.save(inc);
                });
            }
        }
        return Result.success(null, "操作成功");
    }

    @Override
    public Result<String> withdrawIncidents(IdsReq req) {
        if (req != null && req.getIds() != null) {
            for (Integer id : req.getIds()) {
                publishedIncidentRepository.findById(id).ifPresent(inc -> {
                    inc.setIncidentStatus(4); // Withdrawn
                    inc.setUpdateTime(new Date());
                    publishedIncidentRepository.save(inc);
                });
            }
        }
        return Result.success(null, "操作成功");
    }

    @Override
    public Result<String> updateIncident(PublishedIncidentSaveReq req) {
        if (req.getId() == null) return Result.error("缺少ID");
        PublishedIncident inc = publishedIncidentRepository.findById(req.getId()).orElse(null);
        if (inc == null) return Result.error("事件不存在");

        if (req.getIncidentTitle() != null) inc.setIncidentTitle(req.getIncidentTitle());
        if (req.getIncidentType() != null) inc.setIncidentType(req.getIncidentType());
        if (req.getIncidentLevel() != null) inc.setIncidentLevel(req.getIncidentLevel());
        if (req.getIncidentContent() != null) inc.setIncidentContent(req.getIncidentContent());
        if (req.getIncidentLocation() != null) inc.setIncidentLocation(req.getIncidentLocation());
        if (req.getLongitude() != null) inc.setLongitude(req.getLongitude());
        if (req.getLatitude() != null) inc.setLatitude(req.getLatitude());
        if (req.getIncidentRange() != null) inc.setIncidentRange(req.getIncidentRange());
        if (req.getOccurrenceTime() != null) inc.setOccurrenceTime(req.getOccurrenceTime());
        if (req.getIncidentStatus() != null) inc.setIncidentStatus(req.getIncidentStatus());
        if (req.getRemark() != null) inc.setRemark(req.getRemark());
        inc.setUpdateTime(new Date());
        publishedIncidentRepository.save(inc);

        // 获取数据库中当前事件已经绑定的附件
        List<PublishedAttachment> existingAtts = publishedAttachmentRepository.findByTargetIdAndTargetType(inc.getId(), "PUBLISH");
        List<String> existingUrls = existingAtts.stream()
                .map(PublishedAttachment::getAttachmentUrl)
                .collect(Collectors.toList());

        // 找出真正需要新增的附件 URL
        List<String> urlsToAdd = new ArrayList<>();
        if (req.getAttachments() != null) {
            for (String url : req.getAttachments()) {
                if (!existingUrls.contains(url)) {
                    urlsToAdd.add(url);
                }
            }
        }

        // 保存真正新增的附件
        saveAttachments(inc.getId(), urlsToAdd);

        return Result.success(null, "修改成功");
    }

    @Override
    public Result<String> uploadAttachment(MultipartFile file) {
        if (file == null || file.isEmpty()) return Result.error("文件不能为空");
        try {
            String dirPath = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(dirPath);
            if (!dir.exists()) dir.mkdirs();

            String originalName = file.getOriginalFilename();
            String extension = originalName != null && originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID().toString() + extension;
            File dest = new File(dirPath + fileName);
            file.transferTo(dest);

            String fileUrl = org.springframework.web.servlet.support.ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/uploads/" + fileName)
                    .toUriString();

            return Result.success(fileUrl, "上传成功");
        } catch (IOException e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @Override
    public Result<String> deleteIncidents(IdsReq req) {
        if (req != null && req.getIds() != null) {
            for (Integer id : req.getIds()) {
                publishedIncidentRepository.findById(id).ifPresent(inc -> {
                    inc.setDeleted(1);
                    publishedIncidentRepository.save(inc);
                });
            }
        }
        return Result.success(null, "删除成功");
    }

    @Override
    public Result<PublishedIncidentDetailDto> getIncidentDetail(Integer id) {
        PublishedIncident inc = publishedIncidentRepository.findById(id).orElse(null);
        if (inc == null || (inc.getDeleted() != null && inc.getDeleted() == 1)) {
            return Result.error("未查找到事件");
        }

        PublishedIncidentDetailDto dto = new PublishedIncidentDetailDto();
        dto.setId(inc.getId());
        dto.setIncidentTitle(inc.getIncidentTitle());
        dto.setIncidentType(inc.getIncidentType());
        dto.setIncidentLevel(inc.getIncidentLevel());
        dto.setIncidentContent(inc.getIncidentContent());
        dto.setIncidentLocation(inc.getIncidentLocation());
        dto.setLongitude(inc.getLongitude());
        dto.setLatitude(inc.getLatitude());
        dto.setIncidentRange(inc.getIncidentRange());
        dto.setOccurrenceTime(inc.getOccurrenceTime());
        dto.setCreateTime(inc.getCreateTime());
        dto.setUpdateTime(inc.getUpdateTime());
        dto.setIncidentStatus(inc.getIncidentStatus());
        dto.setRemark(inc.getRemark());

        if (inc.getAdminId() != null) {
            adminRepository.findById(inc.getAdminId()).ifPresent(a -> {
                dto.setAdminName(a.getAdminName());
                if(a.getDeptId() != null) {
                    deptRepository.findById(a.getDeptId()).ifPresent(d -> dto.setAdminDeptName(d.getDeptName()));
                }
            });
        }

        if (inc.getPublishId() != null) {
            adminRepository.findById(inc.getPublishId()).ifPresent(a -> {
                dto.setPublishName(a.getAdminName());
                if(a.getDeptId() != null) {
                    deptRepository.findById(a.getDeptId()).ifPresent(d -> dto.setPublishDeptName(d.getDeptName()));
                }
            });
        }

        List<PublishedAttachment> atts = publishedAttachmentRepository.findByTargetIdAndTargetType(inc.getId(), "PUBLISH");
        List<AttachmentDto> attDtos = atts.stream().map(a -> {
            AttachmentDto addto = new AttachmentDto();
            addto.setId(a.getId());
            addto.setName(a.getAttachmentName());
            addto.setUrl(a.getAttachmentUrl());
            addto.setType(a.getAttachmentType() == 1 ? "image" : "document");
            return addto;
        }).collect(Collectors.toList());
        dto.setAttachments(attDtos);

        return Result.success(dto, "操作成功");
    }

    @Override
    public Result<String> deleteAttachment(Integer id) {
        publishedAttachmentRepository.deleteById(id);
        return Result.success(null, "删除成功");
    }
}
