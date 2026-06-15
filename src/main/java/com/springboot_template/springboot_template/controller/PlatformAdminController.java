package com.springboot_template.springboot_template.controller;

import com.springboot_template.springboot_template.dto.request.PlatformAdminRequestDTO;
import com.springboot_template.springboot_template.dto.response.PlatformAdminResponseDTO;
import com.springboot_template.springboot_template.service.PlatformAdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/platform-admins")
public class PlatformAdminController {
    private final PlatformAdminService platformAdminService;

    @PostMapping
    public ResponseEntity<PlatformAdminResponseDTO> createPlatformAdmin(
            @Valid @RequestBody PlatformAdminRequestDTO platformAdminRequestDTO
    ) {
        PlatformAdminResponseDTO createdPlatformAdmin = platformAdminService.createPlatformAdmin(platformAdminRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlatformAdmin);
    }

    @GetMapping
    public ResponseEntity<List<PlatformAdminResponseDTO>> getPlatformAdmins() {
        return ResponseEntity.ok(platformAdminService.getPlatformAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatformAdminResponseDTO> getPlatformAdminById(@PathVariable String id) {
        return ResponseEntity.ok(platformAdminService.getPlatformAdminById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatformAdminResponseDTO> updatePlatformAdmin(
            @PathVariable String id,
            @Valid @RequestBody PlatformAdminRequestDTO platformAdminRequestDTO
    ) {
        return ResponseEntity.ok(platformAdminService.updatePlatformAdmin(id, platformAdminRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatformAdmin(@PathVariable String id) {
        platformAdminService.deletePlatformAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<PlatformAdminResponseDTO> getPlatformAdminByUsername(@PathVariable String username) {
        return ResponseEntity.ok(platformAdminService.getPlatformAdminByUsername(username));
    }
}
