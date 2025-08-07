package com.example.staffaccounting.service.impl;

import com.example.staffaccounting.exception.ResourceNotFountException;
import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.dto.DepartmentRequest;
import com.example.staffaccounting.model.mapper.DepartmentMapper;
import com.example.staffaccounting.model.projection.DepartmentProjection;
import com.example.staffaccounting.repository.DepartmentRepository;
import com.example.staffaccounting.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Anatoliy Shikin
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Department createDepartment(DepartmentRequest departmentRequest) {
        Department department = departmentMapper.toEntity(departmentRequest);
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id, DepartmentRequest departmentRequest) {
        Department department = findDepartmentByIdOrThrow(id);
        departmentMapper.updateDepartmentFromDepartmentRequest(departmentRequest, department);
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = findDepartmentByIdOrThrow(id);
        departmentRepository.delete(department);
    }

    @Override
    public Department findDepartmentByIdOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Department", "id", id));
    }

    @Override
    public List<DepartmentProjection> findAllDepartmentProjections() {
        return departmentRepository.findAllProjections();
    }
}
