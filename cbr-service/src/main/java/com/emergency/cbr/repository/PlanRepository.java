package com.emergency.cbr.repository;

import com.emergency.cbr.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer>, JpaSpecificationExecutor<Plan> {
}
