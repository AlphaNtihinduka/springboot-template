package com.springboot_template.springboot_template.service;

import com.springboot_template.springboot_template.dto.request.PlatformAdminRequestDTO;
import com.springboot_template.springboot_template.dto.response.PlatformAdminResponseDTO;

import java.util.List;

public interface PlatformAdminService {
    PlatformAdminResponseDTO createPlatformAdmin(PlatformAdminRequestDTO platformAdminRequestDTO);

    List<PlatformAdminResponseDTO> getPlatformAdmins();

    PlatformAdminResponseDTO getPlatformAdminById(String id);

    PlatformAdminResponseDTO updatePlatformAdmin(String id, PlatformAdminRequestDTO platformAdminRequestDTO);

    void deletePlatformAdmin(String id);

    PlatformAdminResponseDTO getPlatformAdminByUsername(String username);
}
