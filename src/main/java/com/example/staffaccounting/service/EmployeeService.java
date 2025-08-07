package com.example.staffaccounting.service;

import com.example.staffaccounting.model.Employee;
import com.example.staffaccounting.model.dto.EmployeeRequest;
import com.example.staffaccounting.model.projection.EmployeeProjection;

import java.util.List;

/**
 * @author Anatoliy Shikin
 */
public interface EmployeeService {
    Employee create(EmployeeRequest employeeRequest);

    Employee update(Long id, EmployeeRequest employeeRequest);

    void delete(Long id);

    Employee findEmployeeByIdOrThrow(Long id);

    List<EmployeeProjection> findByLastName(String lastName);

    List<EmployeeProjection> findAllProjections();

    EmployeeProjection findEmployeeProjectionsById(Long id);
}
