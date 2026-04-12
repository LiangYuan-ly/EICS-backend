package com.emergency.eventservice.repository;

import com.emergency.eventservice.entity.PublishedIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishedIncidentRepository extends JpaRepository<PublishedIncident, Integer>, JpaSpecificationExecutor<PublishedIncident> {
}
