package com.springboot_template.springboot_template.unittests.springboottemplate.controller;

import com.springboot_template.springboot_template.controller.PlatformAdminController;
import com.springboot_template.springboot_template.dto.request.PlatformAdminRequestDTO;
import com.springboot_template.springboot_template.dto.response.PlatformAdminResponseDTO;
import com.springboot_template.springboot_template.exception.GlobalExceptionHandler;
import com.springboot_template.springboot_template.exception.PlatformAdminNotFoundException;
import com.springboot_template.springboot_template.service.PlatformAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlatformAdminControllerUnitTest {

    private final PlatformAdminService platformAdminService = mock(PlatformAdminService.class);
    private final PlatformAdminController platformAdminController = new PlatformAdminController(platformAdminService);

    @Test
    void shouldReturnCreatedStatusWhenPlatformAdminIsCreated() {
        PlatformAdminRequestDTO request = new PlatformAdminRequestDTO();
        PlatformAdminResponseDTO response = new PlatformAdminResponseDTO();
        response.setId("admin-1");
        when(platformAdminService.createPlatformAdmin(request)).thenReturn(response);

        var result = platformAdminController.createPlatformAdmin(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldReturnOkStatusWhenPlatformAdminsAreFetched() {
        PlatformAdminResponseDTO response = new PlatformAdminResponseDTO();
        response.setId("admin-1");
        when(platformAdminService.getPlatformAdmins()).thenReturn(List.of(response));

        var result = platformAdminController.getPlatformAdmins();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactly(response);
    }

    @Test
    void shouldReturnOkStatusWhenPlatformAdminIsFetchedById() {
        PlatformAdminResponseDTO response = new PlatformAdminResponseDTO();
        response.setId("admin-1");
        when(platformAdminService.getPlatformAdminById("admin-1")).thenReturn(response);

        var result = platformAdminController.getPlatformAdminById("admin-1");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldReturnOkStatusWhenPlatformAdminIsUpdated() {
        PlatformAdminRequestDTO request = new PlatformAdminRequestDTO();
        PlatformAdminResponseDTO response = new PlatformAdminResponseDTO();
        response.setId("admin-1");
        when(platformAdminService.updatePlatformAdmin("admin-1", request)).thenReturn(response);

        var result = platformAdminController.updatePlatformAdmin("admin-1", request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldReturnNoContentStatusWhenPlatformAdminIsDeleted() {
        var result = platformAdminController.deletePlatformAdmin("admin-1");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void shouldReturnOkStatusWhenPlatformAdminIsFetchedByUsername() {
        PlatformAdminResponseDTO response = new PlatformAdminResponseDTO();
        response.setId("admin-1");
        response.setUsername("platform.owner");
        when(platformAdminService.getPlatformAdminByUsername("platform.owner")).thenReturn(response);

        var result = platformAdminController.getPlatformAdminByUsername("platform.owner");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldReturnNotFoundStatusWhenPlatformAdminDoesNotExist() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(platformAdminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        when(platformAdminService.getPlatformAdminById("missing-admin"))
                .thenThrow(new PlatformAdminNotFoundException("missing-admin"));

        mockMvc.perform(get("/api/v1/platform-admins/missing-admin"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Platform admin not found: missing-admin"))
                .andExpect(jsonPath("$.path").value("/api/v1/platform-admins/missing-admin"));
    }

    @Test
    void shouldReturnBadRequestStatusWhenUpdatingWithInvalidRequest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(platformAdminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        String requestBody = """
                {
                  "username": "",
                  "email": "not-an-email",
                  "fullName": "",
                  "role": null,
                  "status": null,
                  "permissions": []
                }
                """;

        mockMvc.perform(put("/api/v1/platform-admins/admin-1")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundStatusWhenDeletingPlatformAdminDoesNotExist() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(platformAdminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        doThrow(new PlatformAdminNotFoundException("missing-admin"))
                .when(platformAdminService).deletePlatformAdmin("missing-admin");

        mockMvc.perform(delete("/api/v1/platform-admins/missing-admin"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Platform admin not found: missing-admin"))
                .andExpect(jsonPath("$.path").value("/api/v1/platform-admins/missing-admin"));
    }

    @Test
    void shouldReturnNotFoundStatusWhenUsernameDoesNotExist() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(platformAdminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        when(platformAdminService.getPlatformAdminByUsername(any()))
                .thenThrow(new PlatformAdminNotFoundException("missing-admin"));

        mockMvc.perform(get("/api/v1/platform-admins/username/missing-admin"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Platform admin not found: missing-admin"))
                .andExpect(jsonPath("$.path").value("/api/v1/platform-admins/username/missing-admin"));
    }
}
