package com.springboot_template.springboot_template.config;

import com.springboot_template.springboot_template.model.PlatformAdmin;
import com.springboot_template.springboot_template.model.enums.PlatformRole;
import com.springboot_template.springboot_template.model.enums.PlatformUserStatus;
import com.springboot_template.springboot_template.repository.PlatformAdminRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;

@Configuration
@AllArgsConstructor
@Slf4j
public class SeedDataConfig {

    private final PlatformAdminRepository platformAdminRepository;

    @Bean
    public CommandLineRunner seedPlatformAdmin() {
        return args -> platformAdminRepository.findByUsernameIgnoreCase("superadmin")
                .or(() -> platformAdminRepository.findByEmailIgnoreCase("superadmin@bilimy.com"))
                .ifPresentOrElse(
                        existingAdmin -> log.info("Platform super admin already seeded: {}", existingAdmin.getUsername()),
                        () -> {
                            Instant now = Instant.now();
                            PlatformAdmin platformAdmin = new PlatformAdmin();
                            platformAdmin.setUsername("superadmin");
                            platformAdmin.setEmail("superadmin@bilimy.com");
                            platformAdmin.setDisplayName("Bilimy Super Admin");
                            platformAdmin.setRole(PlatformRole.SUPER_ADMIN);
                            platformAdmin.setStatus(PlatformUserStatus.ACTIVE);
                            platformAdmin.setPermissions(List.of(
                                    "PLATFORM_USERS_READ",
                                    "PLATFORM_USERS_WRITE",
                                    "TENANTS_READ",
                                    "TENANTS_WRITE",
                                    "SERVICES_DEPLOY"
                            ));
                            platformAdmin.setSeeded(true);
                            platformAdmin.setCreatedAt(now);
                            platformAdmin.setUpdatedAt(now);
                            platformAdminRepository.save(platformAdmin);
                            log.info("Seeded platform super admin user: {}", platformAdmin.getUsername());
                        }
                );
    }
}
