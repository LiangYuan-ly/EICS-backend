package com.emergency.userservice.service;

import com.emergency.userservice.common.PageData;
import com.emergency.userservice.common.Result;
import com.emergency.userservice.dto.*;

public interface UserService {
    Result<PageData<UserListDto>> getUsers(Integer pageNum, Integer pageSize, String uname, String startTime, String endTime, String phone, String location, String deptName, Integer status);
    
    Result<String> deleteUsers(DeleteIdsReq req);
    
    Result<String> updateUser(UserUpdateReq req);
    
    Result<UserDetailDto> getUserById(Integer id);
}
