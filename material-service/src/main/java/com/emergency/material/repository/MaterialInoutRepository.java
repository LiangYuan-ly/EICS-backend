package com.emergency.material.repository;

import com.emergency.material.entity.MaterialInout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialInoutRepository extends JpaRepository<MaterialInout, Integer> {
}
