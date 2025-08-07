package com.example.staffaccounting.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Anatoliy Shikin
 */
public record DepartmentRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
