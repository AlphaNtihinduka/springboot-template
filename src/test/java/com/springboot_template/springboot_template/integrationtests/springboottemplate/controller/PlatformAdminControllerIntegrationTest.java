package com.springboot_template.springboot_template.integrationtests.springboottemplate.controller;

import com.springboot_template.springboot_template.integrationtests.springboottemplate.support.BaseIntegrationTest;
import com.springboot_template.springboot_template.support.JsonFixtureLoader;
import com.springboot_template.springboot_template.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlatformAdminControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreatePlatformAdminAndPersistDocumentInMongo() throws Exception {
        String requestBody = JsonFixtureLoader.load("fixtures/springboot-template/requests/create-platform-admin-valid.json");

        mockMvc.perform(post("/api/v1/platform-admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(nullValue())))
                .andExpect(jsonPath("$.username").value("platform.owner"))
                .andExpect(jsonPath("$.email").value("platform.owner@bilimy.com"))
                .andExpect(jsonPath("$.fullName").value("Platform Owner"))
                .andExpect(jsonPath("$.role").value("PLATFORM_ADMIN"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.permissions", hasSize(3)))
                .andExpect(jsonPath("$.seeded").value(false))
                .andExpect(jsonPath("$.createdAt", not(nullValue())))
                .andExpect(jsonPath("$.updatedAt", not(nullValue())));

        var persistedPlatformAdmins = platformAdminRepository.findAll();
        assertThat(persistedPlatformAdmins).hasSize(2);
        assertThat(persistedPlatformAdmins.stream().anyMatch(admin -> "platform.owner".equals(admin.getUsername()))).isTrue();
        assertThat(persistedPlatformAdmins.stream().anyMatch(admin -> "superadmin".equals(admin.getUsername()))).isTrue();
    }

    @Test
    void shouldReturnConflictWhenPlatformAdminUsernameAlreadyExists() throws Exception {
        platformAdminRepository.save(TestDataFactory.existingPlatformAdminWithUsername("platform.owner"));
        String requestBody = JsonFixtureLoader.load("fixtures/springboot-template/requests/create-platform-admin-valid.json");

        mockMvc.perform(post("/api/v1/platform-admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.path").value("/api/v1/platform-admins"));
    }

    @Test
    void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
        String requestBody = JsonFixtureLoader.load("fixtures/springboot-template/requests/create-platform-admin-missing-username.json");

        mockMvc.perform(post("/api/v1/platform-admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnAllPersistedPlatformAdmins() throws Exception {
        platformAdminRepository.save(TestDataFactory.persistedPlatformAdmin(
                "admin-1",
                "platform.owner",
                "platform.owner@bilimy.com"
        ));
        platformAdminRepository.save(TestDataFactory.persistedPlatformAdmin(
                "admin-2",
                "tenant.ops",
                "tenant.ops@bilimy.com"
        ));

        mockMvc.perform(get("/api/v1/platform-admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("superadmin", "platform.owner", "tenant.ops")));
    }

    @Test
    void shouldReturnPersistedPlatformAdminById() throws Exception {
        platformAdminRepository.save(TestDataFactory.persistedPlatformAdmin(
                "admin-1",
                "platform.owner",
                "platform.owner@bilimy.com"
        ));

        mockMvc.perform(get("/api/v1/platform-admins/admin-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("admin-1"))
                .andExpect(jsonPath("$.username").value("platform.owner"))
                .andExpect(jsonPath("$.email").value("platform.owner@bilimy.com"))
                .andExpect(jsonPath("$.fullName").value("Platform Owner"))
                .andExpect(jsonPath("$.role").value("PLATFORM_ADMIN"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenPlatformAdminIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/platform-admins/missing-admin"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Platform admin not found: missing-admin"))
                .andExpect(jsonPath("$.path").value("/api/v1/platform-admins/missing-admin"));
    }

    @Test
    void shouldUpdatePersistedPlatformAdmin() throws Exception {
        platformAdminRepository.save(TestDataFactory.persistedPlatformAdmin(
                "admin-1",
                "platform.owner",
                "platform.owner@bilimy.com"
        ));
        String requestBody = JsonFixtureLoader.load("fixtures/springboot-template/requests/update-platform-admin-valid.json");

        mockMvc.perform(put("/api/v1/platform-admins/admin-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("admin-1"))
                .andExpect(jsonPath("$.username").value("tenant.ops"))
                .andExpect(jsonPath("$.email").value("tenant.ops@bilimy.com"))
                .andExpect(jsonPath("$.fullName").value("Tenant Operations"))
                .andExpect(jsonPath("$.role").value("SUPPORT_ADMIN"))
                .andExpect(jsonPath("$.status").value("DISABLED"))
                .andExpect(jsonPath("$.permissions", hasSize(2)));

        var persistedPlatformAdmin = platformAdminRepository.findById("admin-1").orElseThrow();
        assertThat(persistedPlatformAdmin.getUsername()).isEqualTo("tenant.ops");
        assertThat(persistedPlatformAdmin.getEmail()).isEqualTo("tenant.ops@bilimy.com");
        assertThat(persistedPlatformAdmin.getDisplayName()).isEqualTo("Tenant Operations");
        assertThat(persistedPlatformAdmin.getCreatedAt()).isEqualTo(java.time.Instant.parse("2026-04-28T10:00:00Z"));
        assertThat(persistedPlatformAdmin.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldDeletePersistedPlatformAdmin() throws Exception {
        platformAdminRepository.save(TestDataFactory.persistedPlatformAdmin(
                "admin-1",
                "platform.owner",
                "platform.owner@bilimy.com"
        ));

        mockMvc.perform(delete("/api/v1/platform-admins/admin-1"))
                .andExpect(status().isNoContent());

        assertThat(platformAdminRepository.findById("admin-1")).isEmpty();
    }

    @Test
    void shouldReturnPersistedPlatformAdminByUsername() throws Exception {
        platformAdminRepository.save(TestDataFactory.persistedPlatformAdmin(
                "admin-1",
                "platform.owner",
                "platform.owner@bilimy.com"
        ));

        mockMvc.perform(get("/api/v1/platform-admins/username/platform.owner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("admin-1"))
                .andExpect(jsonPath("$.username").value("platform.owner"))
                .andExpect(jsonPath("$.email").value("platform.owner@bilimy.com"));
    }
}
