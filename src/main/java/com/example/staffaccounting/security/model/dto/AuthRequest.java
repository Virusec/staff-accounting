package com.example.staffaccounting.security.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Anatoliy Shikin
 */
public record AuthRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
