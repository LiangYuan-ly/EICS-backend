package com.emergency.notification.repository;

import com.emergency.notification.entity.ReportedAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface ReportedAttachmentRepository extends JpaRepository<ReportedAttachment, Integer> {
    
    @Modifying
    @Transactional
    @Query("UPDATE ReportedAttachment a SET a.targetId = :targetId, a.targetType = :targetType WHERE a.id IN :ids")
    void updateTargetInfo(Integer targetId, String targetType, List<Integer> ids);
}
