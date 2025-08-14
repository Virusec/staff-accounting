package com.example.staffaccounting.security.auth;

import com.example.staffaccounting.security.model.AppUser;
import com.example.staffaccounting.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Anatoliy Shikin
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthEventsListener {
    private final UserRepository userRepository;

    @Value("${security.auth.max-failures:5}")
    private int maxFailures;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent e) {
        String username = e.getAuthentication().getName();
        userRepository.findByUsername(username).ifPresent(u -> {
            if (u.getFailedAttempts() != 0) {
                u.setFailedAttempts(0);
                userRepository.save(u);
            }
        });
        log.info("LOGIN SUCCESS: {}", username);
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent e) {
        String username = (String) e.getAuthentication().getPrincipal();
        Optional<AppUser> opt = userRepository.findByUsername(username);
        if (opt.isEmpty()) {
            log.warn("LOGIN FAILURE: USER NOT FOUND: {}", username);
            return;
        }
        AppUser u = opt.get();
        int attempts = u.getFailedAttempts() + 1;
        u.setFailedAttempts(attempts);
        if (attempts >= maxFailures) {
            u.setAccountNonLocked(false);
            log.warn("ACCOUNT: {} LOCKED AFTER {} FAILURES", username, attempts);
        } else {
            log.warn("LOGIN FAILURE ({} of {}): {}", attempts, maxFailures, username);
        }
        userRepository.save(u);
    }
}
