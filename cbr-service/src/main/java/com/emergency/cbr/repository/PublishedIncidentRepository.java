package com.emergency.cbr.repository;

import com.emergency.cbr.entity.PublishedIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishedIncidentRepository extends JpaRepository<PublishedIncident, Integer> {
    List<PublishedIncident> findByDeleted(Integer deleted);
}
