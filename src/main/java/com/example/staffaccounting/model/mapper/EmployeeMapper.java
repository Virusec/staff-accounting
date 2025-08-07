package com.example.staffaccounting.model.mapper;

import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.Employee;
import com.example.staffaccounting.model.dto.EmployeeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author Anatoliy Shikin
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {
//    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRequest employeeRequest, Department department);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(source = "department", target = "department")
    void updateEmployeeFromEmployeeRequest(
            EmployeeRequest employeeRequest,
            @MappingTarget Employee employee,
            Department department
    );
}
