package com.emergency.material.controller;

import com.emergency.material.common.PageData;
import com.emergency.material.common.Result;
import com.emergency.material.dto.CategoryDto;
import com.emergency.material.dto.IdsReq;
import com.emergency.material.dto.MaterialDto;
import com.emergency.material.dto.MaterialInoutReq;
import com.emergency.material.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/materials")
    public Result<PageData<MaterialDto>> getMaterials(
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false, name = "material_name") String materialName,
            @RequestParam(required = false, name = "category_name") String categoryName,
            @RequestParam(required = false, name = "warehouse_name") String warehouseName,
            @RequestParam(required = false, name = "manufacturer") String manufacturer,
            @RequestParam(required = false, name = "material_status") Integer materialStatus) {
        return materialService.getMaterials(pageSize, pageNum, materialName, categoryName, warehouseName, manufacturer,
                materialStatus);
    }

    @PostMapping("/materials/add")
    public Result<String> addMaterial(@RequestBody MaterialDto req) {
        return materialService.addMaterial(req);
    }

    @PutMapping("/materials/update")
    public Result<String> updateMaterial(@RequestBody MaterialDto req) {
        return materialService.updateMaterial(req);
    }

    @DeleteMapping("/materials/del")
    public Result<String> deleteMaterials(@RequestBody IdsReq req) {
        return materialService.deleteMaterials(req);
    }

    @GetMapping("/materials/{id}")
    public Result<MaterialDto> getMaterialById(@PathVariable("id") Integer id) {
        return materialService.getMaterialById(id);
    }

    @GetMapping("/category")
    public Result<PageData<CategoryDto>> getCategory(
            @RequestParam(required = false) String pageSize,
            @RequestParam(required = false) String category) {
        return materialService.getCategory(pageSize, category);
    }

    @PostMapping("/materials/inout")
    public Result<String> inoutMaterial(@RequestBody MaterialInoutReq req) {
        return materialService.inoutMaterial(req);
    }
}
