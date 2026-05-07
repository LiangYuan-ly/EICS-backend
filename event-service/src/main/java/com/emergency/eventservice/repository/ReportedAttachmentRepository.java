package com.emergency.eventservice.repository;

import com.emergency.eventservice.entity.ReportedAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportedAttachmentRepository extends JpaRepository<ReportedAttachment, Integer> {
    // 根据目标ID和目标类型查询附件
    List<ReportedAttachment> findByTargetIdAndTargetType(Integer targetId, String targetType);
}