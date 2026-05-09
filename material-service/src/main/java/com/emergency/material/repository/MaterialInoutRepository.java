package com.emergency.material.repository;

import com.emergency.material.entity.MaterialInout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialInoutRepository extends JpaRepository<MaterialInout, Integer> {
    // 根据物资ID查询出入库记录，并按创建时间倒序排列
    List<MaterialInout> findByMaterialIdOrderByCreateTimeDesc(Integer materialId);
}