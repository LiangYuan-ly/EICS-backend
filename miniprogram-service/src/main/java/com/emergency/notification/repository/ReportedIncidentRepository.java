package com.emergency.notification.repository;

import com.emergency.notification.entity.ReportedIncident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedIncidentRepository extends JpaRepository<ReportedIncident, Integer> {
    Page<ReportedIncident> findByUserIdAndDeleted(Integer userId, Integer deleted, Pageable pageable);
}
