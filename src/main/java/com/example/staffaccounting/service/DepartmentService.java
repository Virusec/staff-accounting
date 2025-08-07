package com.example.staffaccounting.service;

import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.dto.DepartmentRequest;
import com.example.staffaccounting.model.projection.DepartmentProjection;

import java.util.List;

/**
 * @author Anatoliy Shikin
 */
public interface DepartmentService {
    Department createDepartment(DepartmentRequest departmentRequest);

    Department updateDepartment(Long id, DepartmentRequest departmentRequest);

    void deleteDepartment(Long id);

    Department findDepartmentByIdOrThrow(Long id);

    List<DepartmentProjection> findAllDepartmentProjections();
}
