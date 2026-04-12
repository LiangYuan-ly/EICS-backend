package com.emergency.material.service;

import com.emergency.material.common.PageData;
import com.emergency.material.common.Result;
import com.emergency.material.dto.CategoryDto;
import com.emergency.material.dto.IdsReq;
import com.emergency.material.dto.MaterialDto;
import com.emergency.material.dto.MaterialInoutReq;

public interface MaterialService {
    Result<PageData<MaterialDto>> getMaterials(Integer pageSize, Integer pageNum, String materialName, String categoryName, String warehouseName, String manufacturer, Integer materialStatus);
    Result<String> addMaterial(MaterialDto req);
    Result<String> updateMaterial(MaterialDto req);
    Result<String> deleteMaterials(IdsReq req);
    Result<MaterialDto> getMaterialById(Integer id);
    Result<PageData<CategoryDto>> getCategory(String pageSize, String category);
    Result<String> inoutMaterial(MaterialInoutReq req);
}
