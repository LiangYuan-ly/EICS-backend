package com.emergency.material.service.impl;

import com.emergency.material.common.PageData;
import com.emergency.material.common.Result;
import com.emergency.material.dto.CategoryDto;
import com.emergency.material.dto.IdsReq;
import com.emergency.material.dto.MaterialDto;
import com.emergency.material.dto.MaterialInoutReq;
import com.emergency.material.entity.Category;
import com.emergency.material.entity.Material;
import com.emergency.material.entity.MaterialInout;
import com.emergency.material.entity.Warehouse;
import com.emergency.material.repository.CategoryRepository;
import com.emergency.material.repository.MaterialInoutRepository;
import com.emergency.material.repository.MaterialRepository;
import com.emergency.material.repository.WarehouseRepository;
import com.emergency.material.service.MaterialService;
import com.emergency.material.dto.MaterialInoutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private MaterialInoutRepository materialInoutRepository;

    @Override
    public Result<PageData<MaterialDto>> getMaterials(Integer pageSize, Integer pageNum, String materialName,
            String categoryName, String warehouseName, String manufacturer, Integer materialStatus) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        Specification<Material> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (materialName != null && !materialName.isEmpty()) {
                predicates.add(cb.like(root.get("materialName"), "%" + materialName + "%"));
            }
            if (manufacturer != null && !manufacturer.isEmpty()) {
                predicates.add(cb.like(root.get("manufacturer"), "%" + manufacturer + "%"));
            }
            if (materialStatus != null) {
                predicates.add(cb.equal(root.get("materialStatus"), materialStatus));
            }
            if (categoryName != null && !categoryName.isEmpty()) {
                predicates.add(cb.like(root.get("materialCategory"), "%" + categoryName + "%"));
            }
            if (warehouseName != null && !warehouseName.isEmpty()) {
                List<String> wCodes = warehouseRepository.findAll().stream()
                        .filter(w -> w.getWarehouseName() != null && w.getWarehouseName().contains(warehouseName))
                        .map(Warehouse::getWarehouseCode)
                        .collect(Collectors.toList());
                if (wCodes.isEmpty()) {
                    predicates.add(cb.equal(root.get("materialWarehouse"), "NON_EXISTENT"));
                } else {
                    predicates.add(root.get("materialWarehouse").in(wCodes));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Material> paged = materialRepository.findAll(spec, pageable);
        List<MaterialDto> dtos = paged.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, paged.getTotalElements()), "查询成功");
    }

    @Override
    public Result<String> addMaterial(MaterialDto req) {
        Material m = new Material();
        m.setMaterialCode(req.getMaterialCode());
        m.setMaterialName(req.getMaterialName());
        m.setMaterialCategory(getCategoryCodeByName(req.getCategoryName()));
        m.setMaterialWarehouse(req.getWarehouseCode());
        m.setMaterialBatchNo(req.getMaterialBatchNo());
        m.setProducedTime(req.getProduceTime());
        m.setEffectiveTime(req.getEffectiveTime());
        m.setStockQuantity(req.getStockQuantity() != null ? req.getStockQuantity() : 0);
        m.setSecurityQuantity(req.getSecurityQuantity() != null ? req.getSecurityQuantity() : 0);
        m.setSpecification(req.getSpecification());
        m.setUnit(req.getUnit());
        m.setManufacturer(req.getManufacturer());
        m.setMaterialStatus(req.getMaterialStatus() != null ? req.getMaterialStatus() : 1);
        m.setCreateTime(new Date());
        m.setMaterialRemark(req.getMaterialRemark());

        materialRepository.save(m);
        return Result.success(null, "新增成功");
    }

    @Override
    public Result<String> updateMaterial(MaterialDto req) {
        if (req.getId() == null)
            return Result.error("缺少物资ID");
        Material m = materialRepository.findById(req.getId()).orElse(null);
        if (m == null)
            return Result.error("物资不存在");

        if (req.getMaterialCode() != null)
            m.setMaterialCode(req.getMaterialCode());
        if (req.getMaterialName() != null)
            m.setMaterialName(req.getMaterialName());
        if (req.getCategoryName() != null)
            m.setMaterialCategory(getCategoryCodeByName(req.getCategoryName()));
        if (req.getWarehouseCode() != null)
            m.setMaterialWarehouse(req.getWarehouseCode());
        if (req.getMaterialBatchNo() != null)
            m.setMaterialBatchNo(req.getMaterialBatchNo());
        if (req.getProduceTime() != null)
            m.setProducedTime(req.getProduceTime());
        if (req.getEffectiveTime() != null)
            m.setEffectiveTime(req.getEffectiveTime());
        if (req.getStockQuantity() != null)
            m.setStockQuantity(req.getStockQuantity());
        if (req.getSecurityQuantity() != null)
            m.setSecurityQuantity(req.getSecurityQuantity());
        if (req.getSpecification() != null)
            m.setSpecification(req.getSpecification());
        if (req.getUnit() != null)
            m.setUnit(req.getUnit());
        if (req.getManufacturer() != null)
            m.setManufacturer(req.getManufacturer());
        if (req.getMaterialStatus() != null)
            m.setMaterialStatus(req.getMaterialStatus());
        if (req.getMaterialRemark() != null)
            m.setMaterialRemark(req.getMaterialRemark());
        m.setUpdateTime(new Date());

        materialRepository.save(m);
        return Result.success(null, "修改成功");
    }

    @Override
    public Result<String> deleteMaterials(IdsReq req) {
        if (req != null && req.getIds() != null && !req.getIds().isEmpty()) {
            materialRepository.deleteAllById(req.getIds());
        }
        return Result.success(null, "删除成功");
    }

    @Override
    public Result<MaterialDto> getMaterialById(Integer id) {
        Material m = materialRepository.findById(id).orElse(null);
        if (m == null)
            return Result.error("物资未找到");

        MaterialDto dto = convertToDto(m);

        // 查询关联的出入库记录
        List<MaterialInout> inouts = materialInoutRepository.findByMaterialIdOrderByCreateTimeDesc(id);
        List<MaterialInoutDto> inoutDtos = inouts.stream().map(inout -> {
            MaterialInoutDto inoutDto = new MaterialInoutDto();
            inoutDto.setId(inout.getId());
            inoutDto.setInOut(inout.getInOut());
            inoutDto.setNum(inout.getNum());
            inoutDto.setRemark(inout.getRemark());
            inoutDto.setCreateTime(inout.getCreateTime());
            return inoutDto;
        }).collect(Collectors.toList());

        dto.setInOutRecords(inoutDtos);

        return Result.success(dto, "查询成功");
    }

    @Override
    public Result<PageData<CategoryDto>> getCategory(String pageSize, String category) {
        Integer size = null;
        try {
            if (pageSize != null && !pageSize.isEmpty()) {
                size = Integer.parseInt(pageSize);
            }
        } catch (NumberFormatException e) {
            // Use default size if parsing fails
        }

        Specification<Category> spec = (root, query, cb) -> {
            if (category != null && !category.isEmpty()) {
                return cb.or(
                        cb.like(root.get("categoryName"), "%" + category + "%"),
                        cb.like(root.get("categoryCode"), "%" + category + "%"));
            }
            return cb.conjunction();
        };

        Pageable pageable = PageRequest.of(0, (size != null && size > 0) ? size : 10);
        Page<Category> paged = categoryRepository.findAll(spec, pageable);

        List<CategoryDto> dtos = paged.getContent().stream().map(c -> {
            CategoryDto d = new CategoryDto();
            d.setCategoryCode(c.getCategoryCode());
            d.setCategoryName(c.getCategoryName());
            return d;
        }).collect(Collectors.toList());

        return Result.success(new PageData<>(dtos, paged.getTotalElements()), "操作成功");
    }

    @Override
    @Transactional
    public Result<String> inoutMaterial(MaterialInoutReq req) {
        if (req.getMaterialId() == null || req.getWarehouseId() == null || req.getInOut() == null
                || req.getNum() == null) {
            return Result.error("参数不完整");
        }
        Material m = materialRepository.findById(req.getMaterialId()).orElse(null);
        if (m == null)
            return Result.error("物资不存在");

        if (req.getInOut() == 0) { // 出库
            if (m.getStockQuantity() < req.getNum())
                return Result.error("库存不足");
            m.setStockQuantity(m.getStockQuantity() - req.getNum());
        } else { // 入库
            m.setStockQuantity(m.getStockQuantity() + req.getNum());
        }
        materialRepository.save(m);

        MaterialInout record = new MaterialInout();
        record.setMaterialId(req.getMaterialId());
        record.setWarehouseId(req.getWarehouseId());
        record.setInOut(req.getInOut());
        record.setNum(req.getNum());
        record.setRemark(req.getRemark());
        record.setAdminId(1); // placeholder admin
        record.setCreateTime(new Date());

        materialInoutRepository.save(record);
        return Result.success(null, "出入库记录添加成功！");
    }

    private String getCategoryCodeByName(String categoryName) {
        if (categoryName == null)
            return null;
        return categoryRepository.findAll().stream()
                .filter(c -> categoryName.equals(c.getCategoryName()))
                .map(Category::getCategoryCode)
                .findFirst()
                .orElse(categoryName);
    }

    private MaterialDto convertToDto(Material m) {
        MaterialDto d = new MaterialDto();
        d.setId(m.getId());
        d.setMaterialCode(m.getMaterialCode());
        d.setMaterialName(m.getMaterialName());

        if (m.getMaterialCategory() != null) {
            Category c = categoryRepository.findById(m.getMaterialCategory()).orElse(null);
            if (c != null) {
                d.setCategoryName(c.getCategoryName());
            } else {
                d.setCategoryName(m.getMaterialCategory());
            }
        }

        if (m.getMaterialWarehouse() != null) {
            d.setWarehouseCode(m.getMaterialWarehouse());
            Warehouse w = warehouseRepository.findAll().stream()
                    .filter(wh -> m.getMaterialWarehouse().equals(wh.getWarehouseCode()))
                    .findFirst().orElse(null);
            if (w != null) {
                d.setWarehouseName(w.getWarehouseName());
                d.setWarehouseAddress(w.getWarehouseAddress());
                d.setWarehousePerson(w.getWarehousePerson());
                d.setWarehousePhone(w.getWarehousePhone());
                d.setWarehouseStatus(w.getWarehouseStatus());
                d.setMaterialRemark(m.getMaterialRemark());
            }
        }

        d.setMaterialBatchNo(m.getMaterialBatchNo());
        d.setProduceTime(m.getProducedTime());
        d.setEffectiveTime(m.getEffectiveTime());
        d.setStockQuantity(m.getStockQuantity());
        d.setSecurityQuantity(m.getSecurityQuantity());
        d.setSpecification(m.getSpecification());
        d.setUnit(m.getUnit());
        d.setManufacturer(m.getManufacturer());
        d.setMaterialStatus(m.getMaterialStatus());
        d.setCreateTime(m.getCreateTime());
        d.setUpdateTime(m.getUpdateTime());
        d.setMaterialRemark(m.getMaterialRemark());
        return d;
    }
}
