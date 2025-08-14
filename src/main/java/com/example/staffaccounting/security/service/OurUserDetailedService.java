package com.example.staffaccounting.security.service;

import com.example.staffaccounting.security.model.AppUser;
import com.example.staffaccounting.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Anatoliy Shikin
 */
@Service
@RequiredArgsConstructor
public class OurUserDetailedService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + username + " not found.")
                );
        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .accountLocked(!appUser.isAccountNonLocked())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name())))
                .build();
    }
//    public void createNewUser(AppUser appUser) {
//        appUser.setRole(appUser.getRole());
//        userRepository.save(appUser);
//    }
}
