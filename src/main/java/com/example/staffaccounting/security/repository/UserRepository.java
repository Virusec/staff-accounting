package com.example.staffaccounting.security.repository;

import com.example.staffaccounting.security.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Anatoliy Shikin
 */
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
}
