package com.springboot_template.springboot_template.model;

import com.springboot_template.springboot_template.model.enums.PlatformRole;
import com.springboot_template.springboot_template.model.enums.PlatformUserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "platform_admin")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlatformAdmin {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String displayName;

    private PlatformRole role;

    private PlatformUserStatus status;

    private List<String> permissions;

    private Boolean seeded;

    @CreatedDate
    private Instant createdAt;

    private Instant updatedAt;
}
