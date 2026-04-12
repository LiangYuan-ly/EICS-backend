package com.emergency.cbr.repository;

import com.emergency.cbr.entity.PlanAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanAttachmentRepository extends JpaRepository<PlanAttachment, Integer> {
    PlanAttachment findByTargetIdAndTargetType(Integer targetId, String targetType);
}
