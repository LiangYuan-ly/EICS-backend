package com.emergency.notification.service;

import com.emergency.notification.common.PageData;
import com.emergency.notification.common.Result;
import com.emergency.notification.dto.LoginReq;
import com.emergency.notification.dto.LoginRes;
import com.emergency.notification.dto.MyEventDto;
import com.emergency.notification.dto.NoticeDto;
import com.emergency.notification.dto.UserInfo;
import com.emergency.notification.entity.ReportedIncident;
import com.emergency.notification.entity.User;
import com.emergency.notification.repository.ReportedIncidentRepository;
import com.emergency.notification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MiniProgramService {

    @Autowired
    private ReportedIncidentRepository incidentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.emergency.notification.repository.ReportedAttachmentRepository attachmentRepository;

    public Result<PageData<NoticeDto>> getReportedFeedback(Integer userId, Integer pageNum, Integer pageSize) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        
        Page<ReportedIncident> incidentPage = incidentRepository.findByUserIdAndDeleted(userId, 0, pageable);
        List<NoticeDto> list = new ArrayList<>();
        for (ReportedIncident incident : incidentPage.getContent()) {
            NoticeDto dto = new NoticeDto();
            dto.setId(incident.getId());
            dto.setIncidentTitle(incident.getIncidentTitle());
            dto.setIncidentLocation(incident.getIncidentLocation());
            dto.setUpdateTime(incident.getUpdateTime());
            dto.setIncidentStatus(incident.getIncidentStatus());
            dto.setReview(incident.getReview());
            list.add(dto);
        }
        
        PageData<NoticeDto> pageData = new PageData<>(list, incidentPage.getTotalElements());
        return Result.success(pageData, "操作成功");
    }

    public Result<LoginRes> wxLogin(LoginReq req) {
        String mockOpenId = "openid-" + req.getCode(); // 模拟从小程序 code 换取的 openid
        
        Optional<User> userOpt = userRepository.findByUserid(mockOpenId);
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            // 创建新用户
            user = new User();
            user.setUserid(mockOpenId);
            user.setUname("微信用户-" + req.getCode().substring(0, Math.min(req.getCode().length(), 4)));
            user.setAvatar("https://iph.href.lu/100x100?text=User");
            user.setGender(0);
            user.setStatus(1);
            user = userRepository.save(user);
        }

        LoginRes res = new LoginRes();
        res.setToken("token-" + java.util.UUID.randomUUID().toString());
        
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUname(user.getUname());
        userInfo.setAvatar(user.getAvatar());
        res.setUserInfo(userInfo);
        
        return Result.success(res, "登录成功");
    }

    public Result<PageData<MyEventDto>> getMyEvents(Integer userId, Integer pageNum, Integer pageSize) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<ReportedIncident> incidentPage = incidentRepository.findByUserIdAndDeleted(userId, 0, pageable);
        List<MyEventDto> list = new ArrayList<>();
        for (ReportedIncident incident : incidentPage.getContent()) {
            MyEventDto dto = new MyEventDto();
            dto.setId(incident.getId());
            dto.setIncidentTitle(incident.getIncidentTitle());
            dto.setIncidentStatus(incident.getIncidentStatus());
            dto.setIncidentLocation(incident.getIncidentLocation());
            dto.setOccurrenceTime(incident.getOccurrenceTime());
            dto.setCreateTime(incident.getCreateTime());
            list.add(dto);
        }

        PageData<MyEventDto> pageData = new PageData<>(list, incidentPage.getTotalElements());
        return Result.success(pageData, "查询成功");
    }

    public Result<String> withdrawEvent(Integer incidentId) {
        Optional<ReportedIncident> opt = incidentRepository.findById(incidentId);
        if (opt.isPresent()) {
            ReportedIncident incident = opt.get();
            incident.setDeleted(1);
            incidentRepository.save(incident);
            return Result.success("操作成功");
        }
        return Result.error("事件不存在");
    }

    @org.springframework.transaction.annotation.Transactional
    public Result<String> reportEvent(com.emergency.notification.dto.ReportReq req) {
        ReportedIncident incident = new ReportedIncident();
        incident.setIncidentTitle(req.getIncidentTitle());
        incident.setIncidentType(req.getIncidentType());
        incident.setIncidentContent(req.getIncidentContent());
        incident.setIncidentLocation(req.getIncidentLocation());
        incident.setIncidentRange(req.getIncidentRange());
        incident.setOccurrenceTime(req.getOccurrenceTime());
        incident.setLongitude(req.getLongitude());
        incident.setLatitude(req.getLatitude());
        if (req.getUserId() == null) {
            return Result.error("上报失败：用户ID不能为空");
        }
        incident.setUserId(req.getUserId());
        incident.setIncidentStatus(2); // 2: 待审核
        incident.setDeleted(0);
        
        incident = incidentRepository.save(incident);

        // 如果有附件，关联附件
        if (req.getReportedAttachmentsId() != null && !req.getReportedAttachmentsId().isEmpty()) {
            attachmentRepository.updateTargetInfo(incident.getId(), "REPORT", req.getReportedAttachmentsId());
        }

        return Result.success("上报成功");
    }

    public Result<java.util.Map<String, Object>> uploadFile(org.springframework.web.multipart.MultipartFile file) {
        // 这里简化实现，如果是真实项目需要保存到本地或 OSS
        // 我们这里模拟保存并返回 ID
        com.emergency.notification.entity.ReportedAttachment attachment = new com.emergency.notification.entity.ReportedAttachment();
        String fileName = file.getOriginalFilename();
        attachment.setAttachmentName(fileName);
        attachment.setAttachmentUrl("https://mock.storage.url/file/" + java.util.UUID.randomUUID().toString() + "_" + fileName);
        attachment.setAttachmentType(1); // 默认为图片
        attachment.setAttachmentSize((int) (file.getSize() / 1024));
        attachment.setTargetId(0);
        attachment.setTargetType("TEMP");
        
        attachment = attachmentRepository.save(attachment);
        
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("id", attachment.getId());
        data.put("url", attachment.getAttachmentUrl());
        
        return Result.success(data, "上传成功");
    }
}
