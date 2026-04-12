package com.emergency.material.service;

import com.emergency.material.common.PageData;
import com.emergency.material.common.Result;
import com.emergency.material.dto.IdsReq;
import com.emergency.material.dto.WarehouseDto;

public interface WarehouseService {
    Result<PageData<WarehouseDto>> getWarehouses(Integer pageSize, Integer pageNum, String warehouseCode,
            String warehouseName, String warehouseAddress, String warehousePerson, String warehousePhone,
            Integer warehouseStatus, Integer warehouseId);

    Result<String> deleteWarehouses(IdsReq req);

    Result<String> updateWarehouse(WarehouseDto req);

    Result<String> addWarehouse(WarehouseDto req);
}
