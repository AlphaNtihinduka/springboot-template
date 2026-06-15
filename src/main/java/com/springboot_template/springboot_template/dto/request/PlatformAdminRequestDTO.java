package com.springboot_template.springboot_template.dto.request;

import com.springboot_template.springboot_template.model.enums.PlatformRole;
import com.springboot_template.springboot_template.model.enums.PlatformUserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlatformAdminRequestDTO {
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String fullName;

    @NotNull
    private PlatformRole role;

    @NotNull
    private PlatformUserStatus status;

    @NotEmpty
    private List<@NotBlank String> permissions;
}
