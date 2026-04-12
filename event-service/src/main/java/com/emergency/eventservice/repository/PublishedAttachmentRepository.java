package com.emergency.eventservice.repository;

import com.emergency.eventservice.entity.PublishedAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishedAttachmentRepository extends JpaRepository<PublishedAttachment, Integer> {
    List<PublishedAttachment> findByTargetIdAndTargetType(Integer targetId, String targetType);
}
