package com.example.staffaccounting.model.mapper;

import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.dto.DepartmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author Anatoliy Shikin
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    @Mapping(target = "id", ignore = true)
    Department toEntity(DepartmentRequest departmentRequest);

    void updateDepartmentFromDepartmentRequest(DepartmentRequest departmentRequest, @MappingTarget Department department);
}
