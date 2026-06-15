package com.springboot_template.springboot_template.service.impl;

import com.springboot_template.springboot_template.dto.request.PlatformAdminRequestDTO;
import com.springboot_template.springboot_template.dto.response.PlatformAdminResponseDTO;
import com.springboot_template.springboot_template.exception.DuplicatePlatformAdminFieldException;
import com.springboot_template.springboot_template.exception.PlatformAdminNotFoundException;
import com.springboot_template.springboot_template.mapper.PlatformAdminConverter;
import com.springboot_template.springboot_template.mapper.PlatformAdminMapper;
import com.springboot_template.springboot_template.model.PlatformAdmin;
import com.springboot_template.springboot_template.repository.PlatformAdminRepository;
import com.springboot_template.springboot_template.service.PlatformAdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PlatformAdminServiceImpl implements PlatformAdminService {

    private final PlatformAdminRepository platformAdminRepository;
    private final PlatformAdminMapper platformAdminMapper;
    private final PlatformAdminConverter platformAdminConverter;

    @Override
    public PlatformAdminResponseDTO createPlatformAdmin(PlatformAdminRequestDTO platformAdminRequestDTO) {
        validateUniquePlatformAdminFields(platformAdminRequestDTO);

        PlatformAdmin platformAdmin = platformAdminConverter.toEntity(platformAdminRequestDTO);
        Instant now = Instant.now();
        platformAdmin.setSeeded(false);
        platformAdmin.setCreatedAt(now);
        platformAdmin.setUpdatedAt(now);

        PlatformAdmin savedPlatformAdmin = platformAdminRepository.save(platformAdmin);
        return platformAdminMapper.toResponseDTO(savedPlatformAdmin);
    }

    @Override
    public List<PlatformAdminResponseDTO> getPlatformAdmins() {
        return platformAdminRepository.findAll()
                .stream()
                .map(platformAdminMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PlatformAdminResponseDTO getPlatformAdminById(String id) {
        PlatformAdmin platformAdmin = platformAdminRepository.findById(id)
                .orElseThrow(() -> new PlatformAdminNotFoundException(id));
        return platformAdminMapper.toResponseDTO(platformAdmin);
    }

    @Override
    public PlatformAdminResponseDTO updatePlatformAdmin(String id, PlatformAdminRequestDTO platformAdminRequestDTO) {
        PlatformAdmin existingPlatformAdmin = platformAdminRepository.findById(id)
                .orElseThrow(() -> new PlatformAdminNotFoundException(id));
        validateUniquePlatformAdminFieldsForUpdate(id, platformAdminRequestDTO);

        existingPlatformAdmin.setUsername(platformAdminRequestDTO.getUsername());
        existingPlatformAdmin.setEmail(platformAdminRequestDTO.getEmail());
        existingPlatformAdmin.setDisplayName(platformAdminRequestDTO.getFullName());
        existingPlatformAdmin.setRole(platformAdminRequestDTO.getRole());
        existingPlatformAdmin.setStatus(platformAdminRequestDTO.getStatus());
        existingPlatformAdmin.setPermissions(platformAdminRequestDTO.getPermissions());
        existingPlatformAdmin.setUpdatedAt(Instant.now());

        PlatformAdmin savedPlatformAdmin = platformAdminRepository.save(existingPlatformAdmin);
        return platformAdminMapper.toResponseDTO(savedPlatformAdmin);
    }

    @Override
    public void deletePlatformAdmin(String id) {
        PlatformAdmin platformAdmin = platformAdminRepository.findById(id)
                .orElseThrow(() -> new PlatformAdminNotFoundException(id));
        platformAdminRepository.delete(platformAdmin);
    }

    @Override
    public PlatformAdminResponseDTO getPlatformAdminByUsername(String username) {
        PlatformAdmin platformAdmin = platformAdminRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new PlatformAdminNotFoundException(username));
        return platformAdminMapper.toResponseDTO(platformAdmin);
    }

    private void validateUniquePlatformAdminFields(PlatformAdminRequestDTO platformAdminRequestDTO) {
        if (platformAdminRepository.existsByUsernameIgnoreCase(platformAdminRequestDTO.getUsername())) {
            log.warn("Platform admin username already exists: {}", platformAdminRequestDTO.getUsername());
            throw new DuplicatePlatformAdminFieldException("username", platformAdminRequestDTO.getUsername());
        }

        if (platformAdminRepository.existsByEmailIgnoreCase(platformAdminRequestDTO.getEmail())) {
            log.warn("Platform admin email already exists: {}", platformAdminRequestDTO.getEmail());
            throw new DuplicatePlatformAdminFieldException("email", platformAdminRequestDTO.getEmail());
        }
    }

    private void validateUniquePlatformAdminFieldsForUpdate(
            String currentPlatformAdminId,
            PlatformAdminRequestDTO platformAdminRequestDTO
    ) {
        platformAdminRepository.findByUsernameIgnoreCase(platformAdminRequestDTO.getUsername())
                .filter(platformAdmin -> !currentPlatformAdminId.equals(platformAdmin.getId()))
                .ifPresent(platformAdmin -> {
                    log.warn("Platform admin username already exists: {}", platformAdminRequestDTO.getUsername());
                    throw new DuplicatePlatformAdminFieldException("username", platformAdminRequestDTO.getUsername());
                });

        platformAdminRepository.findByEmailIgnoreCase(platformAdminRequestDTO.getEmail())
                .filter(platformAdmin -> !currentPlatformAdminId.equals(platformAdmin.getId()))
                .ifPresent(platformAdmin -> {
                    log.warn("Platform admin email already exists: {}", platformAdminRequestDTO.getEmail());
                    throw new DuplicatePlatformAdminFieldException("email", platformAdminRequestDTO.getEmail());
                });
    }
}
