package com.springboot_template.springboot_template.unittests.springboottemplate.service;

import com.springboot_template.springboot_template.dto.request.PlatformAdminRequestDTO;
import com.springboot_template.springboot_template.dto.response.PlatformAdminResponseDTO;
import com.springboot_template.springboot_template.exception.DuplicatePlatformAdminFieldException;
import com.springboot_template.springboot_template.exception.PlatformAdminNotFoundException;
import com.springboot_template.springboot_template.mapper.PlatformAdminConverter;
import com.springboot_template.springboot_template.mapper.PlatformAdminMapper;
import com.springboot_template.springboot_template.model.PlatformAdmin;
import com.springboot_template.springboot_template.model.enums.PlatformRole;
import com.springboot_template.springboot_template.model.enums.PlatformUserStatus;
import com.springboot_template.springboot_template.repository.PlatformAdminRepository;
import com.springboot_template.springboot_template.service.impl.PlatformAdminServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlatformAdminServiceUnitTest {

    private final PlatformAdminRepository platformAdminRepository = mock(PlatformAdminRepository.class);
    private final PlatformAdminMapper platformAdminMapper = mock(PlatformAdminMapper.class);
    private final PlatformAdminConverter platformAdminConverter = mock(PlatformAdminConverter.class);
    private final PlatformAdminServiceImpl platformAdminService = new PlatformAdminServiceImpl(
            platformAdminRepository,
            platformAdminMapper,
            platformAdminConverter
    );

    @Test
    void shouldCreatePlatformAdmin() {
        PlatformAdminRequestDTO request = validRequest();
        PlatformAdmin platformAdmin = new PlatformAdmin();
        PlatformAdminResponseDTO response = new PlatformAdminResponseDTO();
        response.setUsername("platform.owner");

        when(platformAdminConverter.toEntity(request)).thenReturn(platformAdmin);
        when(platformAdminRepository.save(any(PlatformAdmin.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(platformAdminMapper.toResponseDTO(platformAdmin)).thenReturn(response);

        PlatformAdminResponseDTO result = platformAdminService.createPlatformAdmin(request);

        assertThat(result.getUsername()).isEqualTo("platform.owner");
        assertThat(platformAdmin.getCreatedAt()).isNotNull();
        assertThat(platformAdmin.getUpdatedAt()).isNotNull();
        assertThat(platformAdmin.getSeeded()).isFalse();
        verify(platformAdminRepository).save(platformAdmin);
    }

    @Test
    void shouldRejectDuplicatePlatformAdminUsername() {
        PlatformAdminRequestDTO request = validRequest();
        when(platformAdminRepository.existsByUsernameIgnoreCase("platform.owner")).thenReturn(true);

        assertThatThrownBy(() -> platformAdminService.createPlatformAdmin(request))
                .isInstanceOf(DuplicatePlatformAdminFieldException.class)
                .hasMessage("Platform admin username already exists: platform.owner");
    }

    @Test
    void shouldReturnAllPlatformAdmins() {
        PlatformAdmin firstPlatformAdmin = platformAdmin("admin-1", "platform.owner", "platform.owner@bilimy.com");
        PlatformAdmin secondPlatformAdmin = platformAdmin("admin-2", "tenant.ops", "tenant.ops@bilimy.com");
        PlatformAdminResponseDTO firstResponse = response("admin-1", "platform.owner", "platform.owner@bilimy.com");
        PlatformAdminResponseDTO secondResponse = response("admin-2", "tenant.ops", "tenant.ops@bilimy.com");

        when(platformAdminRepository.findAll()).thenReturn(List.of(firstPlatformAdmin, secondPlatformAdmin));
        when(platformAdminMapper.toResponseDTO(firstPlatformAdmin)).thenReturn(firstResponse);
        when(platformAdminMapper.toResponseDTO(secondPlatformAdmin)).thenReturn(secondResponse);

        List<PlatformAdminResponseDTO> result = platformAdminService.getPlatformAdmins();

        assertThat(result).containsExactly(firstResponse, secondResponse);
        verify(platformAdminRepository).findAll();
    }

    @Test
    void shouldReturnPlatformAdminByIdWhenPlatformAdminExists() {
        PlatformAdmin platformAdmin = platformAdmin("admin-1", "platform.owner", "platform.owner@bilimy.com");
        PlatformAdminResponseDTO response = response("admin-1", "platform.owner", "platform.owner@bilimy.com");

        when(platformAdminRepository.findById("admin-1")).thenReturn(Optional.of(platformAdmin));
        when(platformAdminMapper.toResponseDTO(platformAdmin)).thenReturn(response);

        PlatformAdminResponseDTO result = platformAdminService.getPlatformAdminById("admin-1");

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPlatformAdminIdDoesNotExist() {
        when(platformAdminRepository.findById("missing-admin")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> platformAdminService.getPlatformAdminById("missing-admin"))
                .isInstanceOf(PlatformAdminNotFoundException.class)
                .hasMessage("Platform admin not found: missing-admin");
    }

    @Test
    void shouldUpdatePlatformAdminWhenPlatformAdminExists() {
        PlatformAdminRequestDTO request = validRequest();
        request.setUsername("tenant.ops");
        request.setEmail("tenant.ops@bilimy.com");
        request.setFullName("Tenant Operations");
        request.setRole(PlatformRole.SUPPORT_ADMIN);
        request.setStatus(PlatformUserStatus.DISABLED);
        request.setPermissions(List.of("TENANTS_READ", "TENANTS_WRITE"));
        PlatformAdmin platformAdmin = platformAdmin("admin-1", "platform.owner", "platform.owner@bilimy.com");
        PlatformAdminResponseDTO response = response("admin-1", "tenant.ops", "tenant.ops@bilimy.com");
        response.setFullName("Tenant Operations");
        response.setRole(PlatformRole.SUPPORT_ADMIN);
        response.setStatus(PlatformUserStatus.DISABLED);
        response.setPermissions(List.of("TENANTS_READ", "TENANTS_WRITE"));

        when(platformAdminRepository.findById("admin-1")).thenReturn(Optional.of(platformAdmin));
        when(platformAdminRepository.findByUsernameIgnoreCase("tenant.ops")).thenReturn(Optional.empty());
        when(platformAdminRepository.findByEmailIgnoreCase("tenant.ops@bilimy.com")).thenReturn(Optional.empty());
        when(platformAdminRepository.save(platformAdmin)).thenReturn(platformAdmin);
        when(platformAdminMapper.toResponseDTO(platformAdmin)).thenReturn(response);

        PlatformAdminResponseDTO result = platformAdminService.updatePlatformAdmin("admin-1", request);

        assertThat(result).isEqualTo(response);
        assertThat(platformAdmin.getUsername()).isEqualTo("tenant.ops");
        assertThat(platformAdmin.getEmail()).isEqualTo("tenant.ops@bilimy.com");
        assertThat(platformAdmin.getDisplayName()).isEqualTo("Tenant Operations");
        assertThat(platformAdmin.getRole()).isEqualTo(PlatformRole.SUPPORT_ADMIN);
        assertThat(platformAdmin.getStatus()).isEqualTo(PlatformUserStatus.DISABLED);
        assertThat(platformAdmin.getPermissions()).containsExactly("TENANTS_READ", "TENANTS_WRITE");
        verify(platformAdminRepository).save(platformAdmin);
    }

    @Test
    void shouldRejectDuplicatePlatformAdminEmailWhenUpdatingPlatformAdmin() {
        PlatformAdminRequestDTO request = validRequest();
        PlatformAdmin platformAdmin = platformAdmin("admin-1", "platform.owner", "platform.owner@bilimy.com");
        PlatformAdmin duplicatePlatformAdmin = platformAdmin("admin-2", "tenant.ops", "tenant.ops@bilimy.com");

        when(platformAdminRepository.findById("admin-1")).thenReturn(Optional.of(platformAdmin));
        when(platformAdminRepository.findByUsernameIgnoreCase("platform.owner")).thenReturn(Optional.of(platformAdmin));
        when(platformAdminRepository.findByEmailIgnoreCase("platform.owner@bilimy.com")).thenReturn(Optional.of(duplicatePlatformAdmin));

        assertThatThrownBy(() -> platformAdminService.updatePlatformAdmin("admin-1", request))
                .isInstanceOf(DuplicatePlatformAdminFieldException.class)
                .hasMessage("Platform admin email already exists: platform.owner@bilimy.com");

        verify(platformAdminRepository, never()).save(any(PlatformAdmin.class));
    }

    @Test
    void shouldDeletePlatformAdminWhenPlatformAdminExists() {
        PlatformAdmin platformAdmin = platformAdmin("admin-1", "platform.owner", "platform.owner@bilimy.com");
        when(platformAdminRepository.findById("admin-1")).thenReturn(Optional.of(platformAdmin));

        platformAdminService.deletePlatformAdmin("admin-1");

        verify(platformAdminRepository).delete(platformAdmin);
    }

    @Test
    void shouldReturnPlatformAdminByUsernameWhenUsernameExists() {
        PlatformAdmin platformAdmin = platformAdmin("admin-1", "platform.owner", "platform.owner@bilimy.com");
        PlatformAdminResponseDTO response = response("admin-1", "platform.owner", "platform.owner@bilimy.com");

        when(platformAdminRepository.findByUsernameIgnoreCase("platform.owner")).thenReturn(Optional.of(platformAdmin));
        when(platformAdminMapper.toResponseDTO(platformAdmin)).thenReturn(response);

        PlatformAdminResponseDTO result = platformAdminService.getPlatformAdminByUsername("platform.owner");

        assertThat(result).isEqualTo(response);
    }

    private PlatformAdminRequestDTO validRequest() {
        PlatformAdminRequestDTO request = new PlatformAdminRequestDTO();
        request.setUsername("platform.owner");
        request.setEmail("platform.owner@bilimy.com");
        request.setFullName("Platform Owner");
        request.setRole(PlatformRole.PLATFORM_ADMIN);
        request.setStatus(PlatformUserStatus.ACTIVE);
        request.setPermissions(List.of(
                "PLATFORM_USERS_READ",
                "PLATFORM_USERS_WRITE",
                "SERVICES_DEPLOY"
        ));
        return request;
    }

    private PlatformAdmin platformAdmin(String id, String username, String email) {
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
        return platformAdmin;
    }

    private PlatformAdminResponseDTO response(String id, String username, String email) {
        PlatformAdminResponseDTO response = new PlatformAdminResponseDTO();
        response.setId(id);
        response.setUsername(username);
        response.setEmail(email);
        response.setFullName("Platform Owner");
        response.setRole(PlatformRole.PLATFORM_ADMIN);
        response.setStatus(PlatformUserStatus.ACTIVE);
        response.setPermissions(List.of(
                "PLATFORM_USERS_READ",
                "PLATFORM_USERS_WRITE",
                "SERVICES_DEPLOY"
        ));
        response.setSeeded(false);
        return response;
    }
}
