package com.emergency.auth.service;

import com.emergency.auth.common.Result;
import com.emergency.auth.dto.*;

import java.util.List;

public interface DeptService {
    Result<PageData<DeptDto>> getDepts(Integer pageNum, Integer pageSize, String deptName, String deptPerson, String deptPhone, String deptAddress, String parentName, Integer deptId);
    
    Result<String> deleteDepts(DeleteIdsReq req);
    
    Result<PageData<DeptBasicDto>> getParents(String parentName);
    
    Result<String> addDept(DeptSaveReq req);
    
    Result<String> updateDept(DeptSaveReq req);
    
    Result<List<DeptTreeDto>> getDeptTree();
    
    Result<PageData<DeptBasicDto>> searchDepts(Integer pageSize, String deptName);
}
