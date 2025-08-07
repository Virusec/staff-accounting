package com.example.staffaccounting.service.impl;

import com.example.staffaccounting.exception.ResourceNotFountException;
import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.Employee;
import com.example.staffaccounting.model.dto.EmployeeRequest;
import com.example.staffaccounting.model.mapper.EmployeeMapper;
import com.example.staffaccounting.model.projection.EmployeeProjection;
import com.example.staffaccounting.repository.EmployeeRepository;
import com.example.staffaccounting.service.DepartmentService;
import com.example.staffaccounting.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Anatoliy Shikin
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentService departmentService;


    @Override
    public Employee create(EmployeeRequest employeeRequest) {
        Department department = departmentService.findDepartmentByIdOrThrow(employeeRequest.departmentId());
        Employee employee = employeeMapper.toEntity(employeeRequest, department);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Long id, EmployeeRequest employeeRequest) {
        Employee employee = findEmployeeByIdOrThrow(id);
        Department department = departmentService.findDepartmentByIdOrThrow(employeeRequest.departmentId());
        employeeMapper.updateEmployeeFromEmployeeRequest(employeeRequest, employee, department);
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        Employee employee = findEmployeeByIdOrThrow(id);
        employeeRepository.delete(employee);
    }

    @Override
    public Employee findEmployeeByIdOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("Employee", "id", id));
    }

    @Override
    public List<EmployeeProjection> findByLastName(String lastName) {
        return employeeRepository.findProjectsByLastName(lastName);
    }

    @Override
    public List<EmployeeProjection> findAllProjections() {
        return employeeRepository.findAllProjections();
    }

    @Override
    public EmployeeProjection findEmployeeProjectionsById(Long id) {
        return employeeRepository.findProjectsById(id)
                .orElseThrow(() -> new ResourceNotFountException("Employee", "id", id));
    }
}
