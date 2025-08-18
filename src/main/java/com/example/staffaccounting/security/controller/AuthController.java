package com.example.staffaccounting.security.controller;

import com.example.staffaccounting.security.model.dictionary.Role;
import com.example.staffaccounting.security.model.dto.AuthRequest;
import com.example.staffaccounting.security.model.dto.TokenResponse;
import com.example.staffaccounting.security.model.AppUser;
import com.example.staffaccounting.security.repository.UserRepository;
import com.example.staffaccounting.security.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Anatoliy Shikin
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.jwt.access-exp-ms}")
    private long accessExpMs;
    @Value("${security.jwt.refresh-exp-ms}")
    private long refreshExpMs;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );
            UserDetails principal = (UserDetails) auth.getPrincipal();
            String access = jwtUtils.generateToken(principal);
            String refresh = jwtUtils.generateRefreshToken(principal);
            return ResponseEntity.ok(new TokenResponse(access, accessExpMs, refresh, refreshExpMs));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Bad credentials"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No refresh token"));
        }
        String token = authHeader.substring(7);
        if (!jwtUtils.isRefreshToken(token) || jwtUtils.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        }
        String username = jwtUtils.extractUsername(token);
        UserDetails user = userRepository.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .accountLocked(!u.isAccountNonLocked())
                        .authorities("ROLE_" + u.getRole().name())
                        .build())
                .orElse(null);
        if (user == null || !user.isAccountNonLocked()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User locked or not found"));
        }
        String access = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new TokenResponse(access, accessExpMs, null, 0));
    }

    // для создания тестовых юзеров
    @PostMapping("/seed")
    public ResponseEntity<?> seed() {
        if (userRepository.findByUsername("user").isEmpty()) {
            userRepository.save(AppUser.builder()
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.USER)
                    .accountNonLocked(true)
                    .failedAttempts(0)
                    .build());
        }
        if (userRepository.findByUsername("moderator").isEmpty()) {
            userRepository.save(AppUser.builder()
                    .username("moderator")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.MODERATOR)
                    .accountNonLocked(true)
                    .failedAttempts(0)
                    .build());
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.SUPER_ADMIN)
                    .accountNonLocked(true)
                    .failedAttempts(0)
                    .build());
        }
        return ResponseEntity.ok("Ok");
    }
}
