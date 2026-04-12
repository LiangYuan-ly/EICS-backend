package com.emergency.userservice.controller;

import com.emergency.userservice.common.PageData;
import com.emergency.userservice.common.Result;
import com.emergency.userservice.dto.*;
import com.emergency.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/usersinfo")
    public Result<PageData<UserListDto>> getUsers(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String uname,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, name = "dept_name") String deptName,
            @RequestParam(required = false) Integer status) {
        return userService.getUsers(pageNum, pageSize, uname, startTime, endTime, phone, location, deptName, status);
    }

    @DeleteMapping("/delusers")
    public Result<String> deleteUsers(@RequestBody DeleteIdsReq req) {
        return userService.deleteUsers(req);
    }

    @PutMapping("/updateusers")
    public Result<String> updateUser(@RequestBody UserUpdateReq req) {
        return userService.updateUser(req);
    }

    @GetMapping("/userinfo/{id}")
    public Result<UserDetailDto> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }
}
