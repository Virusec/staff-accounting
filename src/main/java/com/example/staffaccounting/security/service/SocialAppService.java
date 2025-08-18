package com.example.staffaccounting.security.service;

import com.example.staffaccounting.security.model.AppUser;
import com.example.staffaccounting.security.model.dictionary.Role;
import com.example.staffaccounting.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Anatoliy Shikin
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Profile("oauth")
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Value("${security.oauth2.github.admin-logins:}")
    private String adminLoginsCsv;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String login = asString(oAuth2User.getAttributes().get("login"));
        if (login == null || login.isBlank()) {
            throw new OAuth2AuthenticationException("Missing GitHub login");
        }

        Set<String> admins = Arrays.stream(adminLoginsCsv.split(","))
                .map(String::trim).filter(s -> !s.isBlank()).collect(Collectors.toSet());
        Role role = admins.contains(login) ? Role.ADMIN : Role.USER;

        AppUser user = userRepository.findByUsername(login)
                .map(u -> {
                    u.setRole(role);
                    return u;
                })
                .orElseGet(() -> AppUser.builder()
                        .username(login)
                        .password("{noop}oauth2")
                        .role(role)
                        .accountNonLocked(true)
                        .failedAttempts(0)
                        .build());
        userRepository.save(user);

        Set<GrantedAuthority> auth = new HashSet<>();
        auth.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return new DefaultOAuth2User(auth, oAuth2User.getAttributes(), "id");
    }

    private static String asString(Object v) {
        return v == null ? null : String.valueOf(v);
    }
}

