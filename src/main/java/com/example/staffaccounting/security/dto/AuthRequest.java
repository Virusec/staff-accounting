package com.example.staffaccounting.security.dto;

/**
 * @author Anatoliy Shikin
 */
public record AuthRequest(
        String username,
        String password
) {
}
