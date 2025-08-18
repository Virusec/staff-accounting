package com.example.staffaccounting.security.controller;

import com.example.staffaccounting.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anatoliy Shikin
 */
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping("/{username}/unlock")
    public ResponseEntity<?> unlock(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(u -> {
                    u.setAccountNonLocked(true);
                    u.setFailedAttempts(0);
                    userRepository.save(u);
                    return ResponseEntity.ok().body(java.util.Map.of("status","unlocked","user", username));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
