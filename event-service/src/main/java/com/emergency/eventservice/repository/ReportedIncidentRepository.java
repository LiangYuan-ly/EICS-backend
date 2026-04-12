package com.emergency.eventservice.repository;

import com.emergency.eventservice.entity.ReportedIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedIncidentRepository extends JpaRepository<ReportedIncident, Integer>, JpaSpecificationExecutor<ReportedIncident> {
}
