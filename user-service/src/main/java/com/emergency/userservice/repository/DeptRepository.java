package com.emergency.userservice.repository;

import com.emergency.userservice.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeptRepository extends JpaRepository<Dept, Integer> {
    Optional<Dept> findByDeptCode(String deptCode);
    Optional<Dept> findByDeptName(String deptName);
}
