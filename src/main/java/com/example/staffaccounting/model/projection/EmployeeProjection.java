package com.example.staffaccounting.model.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Anatoliy Shikin
 */
public interface EmployeeProjection {
    @Value("#{target.firstName + ' ' +target.lastName}")
    String getFullName();

    String getPosition();

    @Value("#target.department.name")
    String getDepartment();
}
