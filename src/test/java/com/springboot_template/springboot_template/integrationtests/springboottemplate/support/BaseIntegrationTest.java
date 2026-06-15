package com.springboot_template.springboot_template.integrationtests.springboottemplate.support;

import com.springboot_template.springboot_template.model.PlatformAdmin;
import com.springboot_template.springboot_template.model.enums.PlatformRole;
import com.springboot_template.springboot_template.model.enums.PlatformUserStatus;
import com.springboot_template.springboot_template.repository.PlatformAdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();

        String mongoUri = mongoDBContainer.getReplicaSetUrl("platform_template_test");
        registry.add("spring.data.mongodb.uri", () -> mongoUri);
        registry.add("spring.data.mongodb.database", () -> "platform_template_test");
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected PlatformAdminRepository platformAdminRepository;

    @BeforeEach
    void resetIntegrationTestState() {
        platformAdminRepository.deleteAll();
        seedSuperAdmin();
    }

    private void seedSuperAdmin() {
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
        platformAdmin.setCreatedAt(Instant.parse("2026-04-28T10:00:00Z"));
        platformAdmin.setUpdatedAt(Instant.parse("2026-04-28T10:00:00Z"));
        platformAdminRepository.save(platformAdmin);
    }
}
