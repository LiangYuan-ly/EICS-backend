package com.emergency.auth.repository;

import com.emergency.auth.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeptRepository extends JpaRepository<Dept, Integer>, JpaSpecificationExecutor<Dept> {
    Optional<Dept> findByDeptCode(String deptCode);
    Optional<Dept> findByDeptName(String deptName);
}
