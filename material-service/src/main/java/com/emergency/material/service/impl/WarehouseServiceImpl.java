package com.emergency.material.service.impl;

import com.emergency.material.common.PageData;
import com.emergency.material.common.Result;
import com.emergency.material.dto.IdsReq;
import com.emergency.material.dto.WarehouseDto;
import com.emergency.material.entity.Warehouse;
import com.emergency.material.repository.WarehouseRepository;
import com.emergency.material.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public Result<PageData<WarehouseDto>> getWarehouses(Integer pageSize, Integer pageNum, String warehouseCode, String warehouseName, String warehouseAddress, String warehousePerson, String warehousePhone, Integer warehouseStatus, Integer warehouseId) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Specification<Warehouse> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (warehouseCode != null && !warehouseCode.isEmpty()) {
                predicates.add(cb.equal(root.get("warehouseCode"), warehouseCode));
            }
            if (warehouseName != null && !warehouseName.isEmpty()) {
                predicates.add(cb.like(root.get("warehouseName"), "%" + warehouseName + "%"));
            }
            if (warehouseAddress != null && !warehouseAddress.isEmpty()) {
                predicates.add(cb.like(root.get("warehouseAddress"), "%" + warehouseAddress + "%"));
            }
            if (warehousePerson != null && !warehousePerson.isEmpty()) {
                predicates.add(cb.like(root.get("warehousePerson"), "%" + warehousePerson + "%"));
            }
            if (warehousePhone != null && !warehousePhone.isEmpty()) {
                predicates.add(cb.like(root.get("warehousePhone"), "%" + warehousePhone + "%"));
            }
            if (warehouseStatus != null) {
                predicates.add(cb.equal(root.get("warehouseStatus"), warehouseStatus));
            }
            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("id"), warehouseId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Warehouse> paged = warehouseRepository.findAll(spec, pageable);
        List<WarehouseDto> dtos = paged.getContent().stream().map(w -> {
            WarehouseDto d = new WarehouseDto();
            d.setId(w.getId());
            d.setWarehouseCode(w.getWarehouseCode());
            d.setWarehouseName(w.getWarehouseName());
            d.setWarehouseAddress(w.getWarehouseAddress());
            d.setWarehousePerson(w.getWarehousePerson());
            d.setWarehousePhone(w.getWarehousePhone());
            d.setWarehouseStatus(w.getWarehouseStatus());
            d.setRemark(w.getRemark());
            d.setLongitude(w.getLongitude());
            d.setLatitude(w.getLatitude());
            d.setCreateTime(w.getCreateTime());
            d.setUpdateTime(w.getUpdateTime());
            return d;
        }).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, paged.getTotalElements()), "查询成功");
    }

    @Override
    public Result<String> deleteWarehouses(IdsReq req) {
        if (req != null && req.getIds() != null && !req.getIds().isEmpty()) {
            warehouseRepository.deleteAllById(req.getIds());
        }
        return Result.success(null, "删除成功");
    }

    @Override
    public Result<String> updateWarehouse(WarehouseDto req) {
        if (req.getId() == null) return Result.error("缺少ID");
        Warehouse w = warehouseRepository.findById(req.getId()).orElse(null);
        if (w == null) return Result.error("仓库不存在");
        
        if (req.getWarehouseCode() != null) w.setWarehouseCode(req.getWarehouseCode());
        if (req.getWarehouseName() != null) w.setWarehouseName(req.getWarehouseName());
        if (req.getWarehouseAddress() != null) w.setWarehouseAddress(req.getWarehouseAddress());
        if (req.getLongitude() != null) w.setLongitude(req.getLongitude());
        if (req.getLatitude() != null) w.setLatitude(req.getLatitude());
        if (req.getWarehousePerson() != null) w.setWarehousePerson(req.getWarehousePerson());
        if (req.getWarehousePhone() != null) w.setWarehousePhone(req.getWarehousePhone());
        if (req.getWarehouseStatus() != null) w.setWarehouseStatus(req.getWarehouseStatus());
        if (req.getRemark() != null) w.setRemark(req.getRemark());
        
        w.setUpdateTime(new Date());
        warehouseRepository.save(w);
        return Result.success(null, "修改成功");
    }

    @Override
    public Result<String> addWarehouse(WarehouseDto req) {
        Warehouse w = new Warehouse();
        w.setWarehouseCode(req.getWarehouseCode());
        w.setWarehouseName(req.getWarehouseName());
        w.setWarehouseAddress(req.getWarehouseAddress());
        w.setLongitude(req.getLongitude());
        w.setLatitude(req.getLatitude());
        w.setWarehousePerson(req.getWarehousePerson());
        w.setWarehousePhone(req.getWarehousePhone());
        w.setWarehouseStatus(req.getWarehouseStatus() != null ? req.getWarehouseStatus() : 1);
        w.setRemark(req.getRemark());
        w.setCreateTime(new Date());
        warehouseRepository.save(w);
        return Result.success(null, "新增成功");
    }
}
