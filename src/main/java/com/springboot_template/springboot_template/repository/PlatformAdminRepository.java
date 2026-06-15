package com.springboot_template.springboot_template.repository;

import com.springboot_template.springboot_template.model.PlatformAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlatformAdminRepository extends MongoRepository<PlatformAdmin, String> {

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    Optional<PlatformAdmin> findByUsernameIgnoreCase(String username);

    Optional<PlatformAdmin> findByEmailIgnoreCase(String email);
}
