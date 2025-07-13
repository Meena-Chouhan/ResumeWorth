package com.resume.resume_analyzer.repository;

import com.resume.resume_analyzer.entity.User;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


    boolean existsByEmail(@NotBlank(message = "Email is required.") String email);

}
