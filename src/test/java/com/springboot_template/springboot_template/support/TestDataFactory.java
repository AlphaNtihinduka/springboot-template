package com.springboot_template.springboot_template.support;

import com.springboot_template.springboot_template.model.PlatformAdmin;
import com.springboot_template.springboot_template.model.enums.PlatformRole;
import com.springboot_template.springboot_template.model.enums.PlatformUserStatus;

import java.time.Instant;
import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static PlatformAdmin existingPlatformAdminWithUsername(String username) {
        PlatformAdmin platformAdmin = new PlatformAdmin();
        platformAdmin.setUsername(username);
        platformAdmin.setEmail("existing.admin@bilimy.com");
        platformAdmin.setDisplayName("Existing Admin");
        platformAdmin.setRole(PlatformRole.PLATFORM_ADMIN);
        platformAdmin.setStatus(PlatformUserStatus.ACTIVE);
        platformAdmin.setPermissions(List.of("PLATFORM_USERS_READ"));
        platformAdmin.setSeeded(false);
        return platformAdmin;
    }

    public static PlatformAdmin persistedPlatformAdmin(String id, String username, String email) {
        PlatformAdmin platformAdmin = new PlatformAdmin();
        platformAdmin.setId(id);
        platformAdmin.setUsername(username);
        platformAdmin.setEmail(email);
        platformAdmin.setDisplayName("Platform Owner");
        platformAdmin.setRole(PlatformRole.PLATFORM_ADMIN);
        platformAdmin.setStatus(PlatformUserStatus.ACTIVE);
        platformAdmin.setPermissions(List.of(
                "PLATFORM_USERS_READ",
                "PLATFORM_USERS_WRITE",
                "SERVICES_DEPLOY"
        ));
        platformAdmin.setSeeded(false);
        platformAdmin.setCreatedAt(Instant.parse("2026-04-28T10:00:00Z"));
        platformAdmin.setUpdatedAt(Instant.parse("2026-04-28T10:00:00Z"));
        return platformAdmin;
    }
}
