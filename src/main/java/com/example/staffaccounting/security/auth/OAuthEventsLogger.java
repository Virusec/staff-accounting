package com.example.staffaccounting.security.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author Anatoliy Shikin
 */
@Component
@Slf4j
public class OAuthEventsLogger {
    @EventListener
    public void onAuthSuccess(AuthenticationSuccessEvent event) {
        if (event.getAuthentication() instanceof OAuth2AuthenticationToken token) {
            log.info("OAUTH LOGIN SUCCESS: client={}, user={}", token.getAuthorizedClientRegistrationId(), token.getName());
        }
    }

    @EventListener
    public void onLogout(LogoutSuccessEvent event) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2AuthenticationToken token) {
            log.info("OAUTH LOGOUT: client={}, user={}, event={}",
                    token.getAuthorizedClientRegistrationId(), token.getName(), event.getClass().getSimpleName());
        } else {
            log.info("LOGOUT event={}", event.getClass().getSimpleName());
        }
    }
}
