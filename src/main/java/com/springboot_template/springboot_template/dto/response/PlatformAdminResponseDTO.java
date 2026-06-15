package com.springboot_template.springboot_template.dto.response;

import com.springboot_template.springboot_template.model.enums.PlatformRole;
import com.springboot_template.springboot_template.model.enums.PlatformUserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlatformAdminResponseDTO {
    private String id;

    private String username;

    private String email;

    private String fullName;

    private PlatformRole role;

    private PlatformUserStatus status;

    private List<String> permissions;

    private Boolean seeded;

    private Instant createdAt;

    private Instant updatedAt;
}
