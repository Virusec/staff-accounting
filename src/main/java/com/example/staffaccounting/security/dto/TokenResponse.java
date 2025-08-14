package com.example.staffaccounting.security.dto;

/**
 * @author Anatoliy Shikin
 */
public record TokenResponse(
        String accessToken,
        long accessExpiresInMs,
        String refreshToken,
        long refreshExpiresInMs
) {
}
