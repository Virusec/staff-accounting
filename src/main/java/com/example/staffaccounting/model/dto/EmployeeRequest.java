package com.example.staffaccounting.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * @author Anatoliy Shikin
 */
public record EmployeeRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Position is required")
        String position,
        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be positive")
        BigDecimal salary,
        @NotNull(message = "Department ID is required")
        @Positive(message = "Department ID must be positive")
        Long departmentId
) {
}
