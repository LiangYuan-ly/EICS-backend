package com.emergency.notification.controller;

import com.emergency.notification.common.PageData;
import com.emergency.notification.common.Result;
import com.emergency.notification.dto.LoginReq;
import com.emergency.notification.dto.LoginRes;
import com.emergency.notification.dto.MyEventDto;
import com.emergency.notification.dto.NoticeDto;
import com.emergency.notification.service.MiniProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MiniProgramController {

    @Autowired
    private MiniProgramService miniProgramService;

    @GetMapping("/notice/reported/{id}")
    public Result<PageData<NoticeDto>> getReportedFeedback(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        return miniProgramService.getReportedFeedback(id, pageNum, pageSize);
    }

    @PostMapping("/user/login")
    public Result<LoginRes> wxLogin(@RequestBody LoginReq loginReq) {
        return miniProgramService.wxLogin(loginReq);
    }

    @GetMapping("/myevents/{id}")
    public Result<PageData<MyEventDto>> getMyEvents(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        return miniProgramService.getMyEvents(id, pageNum, pageSize);
    }

    @PostMapping("/events/withdraw/{id}")
    public Result<String> withdrawEvent(@PathVariable Integer id) {
        return miniProgramService.withdrawEvent(id);
    }

    @PostMapping("/events/report")
    public Result<String> reportEvent(@RequestBody com.emergency.notification.dto.ReportReq req) {
        return miniProgramService.reportEvent(req);
    }

    @PostMapping("/common/upload")
    public Result<java.util.Map<String, Object>> uploadFile(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        return miniProgramService.uploadFile(file);
    }
}
