package com.emergency.cbr.service.impl;

import com.emergency.cbr.common.PageData;
import com.emergency.cbr.common.Result;
import com.emergency.cbr.dto.IdsReq;
import com.emergency.cbr.dto.PlanDto;
import com.emergency.cbr.entity.Category;
import com.emergency.cbr.entity.Dept;
import com.emergency.cbr.entity.Plan;
import com.emergency.cbr.entity.PlanAttachment;
import com.emergency.cbr.repository.CategoryRepository;
import com.emergency.cbr.repository.DeptRepository;
import com.emergency.cbr.repository.PlanAttachmentRepository;
import com.emergency.cbr.repository.PlanRepository;
import com.emergency.cbr.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanAttachmentRepository planAttachmentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DeptRepository deptRepository;

    @Override
    public Result<PageData<PlanDto>> getPlans(Integer pageNum, Integer pageSize, String planCode, String planTitle,
            Integer planType, String categoryName, Integer planLevel, String deptName, Integer status) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Specification<Plan> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (planCode != null && !planCode.isEmpty())
                predicates.add(cb.like(root.get("planCode"), "%" + planCode + "%"));
            if (planTitle != null && !planTitle.isEmpty())
                predicates.add(cb.like(root.get("planTitle"), "%" + planTitle + "%"));
            if (planType != null)
                predicates.add(cb.equal(root.get("planType"), planType));
            if (planLevel != null)
                predicates.add(cb.equal(root.get("planLevel"), planLevel));
            if (status != null)
                predicates.add(cb.equal(root.get("planStatus"), status));

            if (categoryName != null && !categoryName.isEmpty()) {
                List<String> codes = categoryRepository.findAll().stream()
                        .filter(c -> c.getCategoryName() != null && c.getCategoryName().contains(categoryName))
                        .map(Category::getCategoryCode)
                        .collect(Collectors.toList());
                if (!codes.isEmpty())
                    predicates.add(root.get("categoryCode").in(codes));
                else
                    predicates.add(cb.equal(root.get("categoryCode"), "NON_EXIST"));
            }

            if (deptName != null && !deptName.isEmpty()) {
                List<Integer> dIds = deptRepository.findAll().stream()
                        .filter(d -> d.getDeptName() != null && d.getDeptName().contains(deptName))
                        .map(Dept::getId)
                        .collect(Collectors.toList());
                if (!dIds.isEmpty())
                    predicates.add(root.get("deptId").in(dIds));
                else
                    predicates.add(cb.equal(root.get("deptId"), -1));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Plan> paged = planRepository.findAll(spec, pageable);
        List<PlanDto> dtos = paged.getContent().stream().map(this::convertToDto).collect(Collectors.toList());
        return Result.success(new PageData<>(dtos, paged.getTotalElements()), "查询成功");
    }

    @Override
    @Transactional
    public Result<String> addPlan(PlanDto req) {
        Plan p = new Plan();
        p.setPlanCode(req.getPlanCode());
        p.setPlanTitle(req.getPlanTitle());
        p.setPlanType(req.getPlanType());
        p.setCategoryCode(req.getCategoryCode());
        p.setPlanLevel(req.getPlanLevel());
        p.setPlanRange(req.getPlanRange());
        p.setDeptId(req.getDeptId());

        if (req.getPublishTime() != null && !req.getPublishTime().isEmpty()) {
            try {
                SimpleDateFormat formatter;
                if (req.getPublishTime().length() == 10) {
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                } else {
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                p.setPublishTime(formatter.parse(req.getPublishTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        p.setPlanStatus(req.getStatus() != null ? req.getStatus() : 1);
        p.setPlanUrl(req.getPlanUrl());
        p.setCreateTime(new Date());

        planRepository.save(p);

        if (req.getAttachmentId() != null && req.getAttachmentId() > 0) {
            PlanAttachment attachment = planAttachmentRepository.findById(req.getAttachmentId()).orElse(null);
            if (attachment != null) {
                attachment.setTargetId(p.getId());
                attachment.setTargetType("PLAN");
                planAttachmentRepository.save(attachment);
            }
        }

        return Result.success(null, "新增成功");
    }

    @Override
    @Transactional
    public Result<String> updatePlan(PlanDto req) {
        if (req.getId() == null)
            return Result.error("ID不能为空");
        Plan p = planRepository.findById(req.getId()).orElse(null);
        if (p == null)
            return Result.error("数据不存在");

        if (req.getPlanCode() != null)
            p.setPlanCode(req.getPlanCode());
        if (req.getPlanTitle() != null)
            p.setPlanTitle(req.getPlanTitle());
        if (req.getPlanType() != null)
            p.setPlanType(req.getPlanType());
        if (req.getCategoryCode() != null)
            p.setCategoryCode(req.getCategoryCode());
        if (req.getPlanLevel() != null)
            p.setPlanLevel(req.getPlanLevel());
        if (req.getPlanRange() != null)
            p.setPlanRange(req.getPlanRange());
        if (req.getDeptId() != null)
            p.setDeptId(req.getDeptId());
        if (req.getStatus() != null)
            p.setPlanStatus(req.getStatus());
        if (req.getPlanUrl() != null)
            p.setPlanUrl(req.getPlanUrl());

        if (req.getPublishTime() != null && !req.getPublishTime().isEmpty()) {
            try {
                SimpleDateFormat formatter = req.getPublishTime().length() == 10 ? new SimpleDateFormat("yyyy-MM-dd")
                        : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                p.setPublishTime(formatter.parse(req.getPublishTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        p.setUpdateTime(new Date());
        planRepository.save(p);

        if (req.getAttachmentId() != null && req.getAttachmentId() > 0) {
            PlanAttachment old = planAttachmentRepository.findByTargetIdAndTargetType(p.getId(), "PLAN");
            if (old != null && !old.getId().equals(req.getAttachmentId())) {
                old.setTargetId(null);
                planAttachmentRepository.save(old);
            }

            PlanAttachment attachment = planAttachmentRepository.findById(req.getAttachmentId()).orElse(null);
            if (attachment != null) {
                attachment.setTargetId(p.getId());
                attachment.setTargetType("PLAN");
                planAttachmentRepository.save(attachment);
            }
        }

        return Result.success(null, "修改成功");
    }

    @Override
    public Result<String> deletePlans(IdsReq req) {
        if (req != null && req.getIds() != null) {
            planRepository.deleteAllById(req.getIds());
        }
        return Result.success(null, "删除成功");
    }

    @Override
    public Result<PlanDto> getPlanById(Integer id) {
        Plan p = planRepository.findById(id).orElse(null);
        if (p == null)
            return Result.error("未找到详情");
        return Result.success(convertToDto(p), "查询预案详情成功");
    }

    @Override
    public Result<Map<String, Object>> uploadPlan(MultipartFile file) {
        if (file.isEmpty())
            return Result.error("空文件");
        try {
            String dirPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
            File dir = new File(dirPath);
            if (!dir.exists())
                dir.mkdirs();

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(dirPath + filename);
            file.transferTo(dest);

            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(filename)
                    .toUriString();

            PlanAttachment pa = new PlanAttachment();
            pa.setAttachmentName(file.getOriginalFilename());
            pa.setAttachmentUrl(url);
            pa.setAttachmentType(3);
            pa.setAttachmentSize((int) (file.getSize() / 1024));
            pa.setTargetId(0);
            pa.setTargetType("PLAN");
            pa.setCreateTime(new Date());

            planAttachmentRepository.save(pa);

            Map<String, Object> data = new HashMap<>();
            data.put("id", pa.getId());
            data.put("attachment_name", pa.getAttachmentName());
            data.put("attachment_url", pa.getAttachmentUrl());

            return Result.success(data, "上传成功");
        } catch (IOException e) {
            return Result.error("上传失败");
        }
    }

    @Override
    public Result<String> deletePlanAttachment(Integer id) {
        planAttachmentRepository.deleteById(id);
        return Result.success(null, "操作成功");
    }

    private PlanDto convertToDto(Plan p) {
        PlanDto dto = new PlanDto();
        dto.setId(p.getId());
        dto.setPlanCode(p.getPlanCode());
        dto.setPlanTitle(p.getPlanTitle());
        dto.setPlanType(p.getPlanType());
        dto.setCategoryCode(p.getCategoryCode());
        dto.setPlanLevel(p.getPlanLevel());
        dto.setPlanRange(p.getPlanRange());
        if (p.getPublishTime() != null)
            dto.setPublishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(p.getPublishTime()));
        dto.setDeptId(p.getDeptId());
        dto.setStatus(p.getPlanStatus());
        dto.setPlanUrl(p.getPlanUrl());
        dto.setCreateTime(p.getCreateTime());
        dto.setUpdateTime(p.getUpdateTime());

        if (p.getCategoryCode() != null) {
            Category c = categoryRepository.findById(p.getCategoryCode()).orElse(null);
            if (c != null)
                dto.setCategoryName(c.getCategoryName());
        }

        if (p.getDeptId() != null) {
            Dept d = deptRepository.findById(p.getDeptId()).orElse(null);
            if (d != null) {
                dto.setDeptName(d.getDeptName());
                dto.setDeptCode(d.getDeptCode());
            }
        }

        PlanAttachment pa = planAttachmentRepository.findByTargetIdAndTargetType(p.getId(), "PLAN");
        if (pa != null) {
            dto.setAttachmentId(pa.getId());
            dto.setAttachmentName(pa.getAttachmentName());
            dto.setAttachmentUrl(pa.getAttachmentUrl());
        }

        return dto;
    }
}
