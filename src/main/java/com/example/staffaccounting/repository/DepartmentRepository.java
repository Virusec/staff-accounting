package com.example.staffaccounting.repository;

import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.projection.DepartmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Anatoliy Shikin
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select d.name as name from Department d")
    List<DepartmentProjection> findAllProjections();
}
