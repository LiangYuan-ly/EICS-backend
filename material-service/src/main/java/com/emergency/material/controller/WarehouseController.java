package com.emergency.material.controller;

import com.emergency.material.common.PageData;
import com.emergency.material.common.Result;
import com.emergency.material.dto.IdsReq;
import com.emergency.material.dto.WarehouseDto;
import com.emergency.material.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/warehouses")
    public Result<PageData<WarehouseDto>> getWarehouses(
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false, name = "warehouse_code") String warehouseCode,
            @RequestParam(required = false, name = "warehouse_name") String warehouseName,
            @RequestParam(required = false, name = "warehouse_address") String warehouseAddress,
            @RequestParam(required = false, name = "warehouse_person") String warehousePerson,
            @RequestParam(required = false, name = "warehouse_phone") String warehousePhone,
            @RequestParam(required = false, name = "warehouse_status") Integer warehouseStatus,
            @RequestParam(required = false, name = "warehouse_id") Integer warehouseId) {
        return warehouseService.getWarehouses(pageSize, pageNum, warehouseCode, warehouseName, warehouseAddress, warehousePerson, warehousePhone, warehouseStatus, warehouseId);
    }

    @DeleteMapping("/admin/delwarehouses")
    public Result<String> deleteWarehouses(@RequestBody IdsReq req) {
        return warehouseService.deleteWarehouses(req);
    }

    @PutMapping("/warehouses/update")
    public Result<String> updateWarehouse(@RequestBody WarehouseDto req) {
        return warehouseService.updateWarehouse(req);
    }

    @PostMapping("/warehouses/add")
    public Result<String> addWarehouse(@RequestBody WarehouseDto req) {
        return warehouseService.addWarehouse(req);
    }
}
