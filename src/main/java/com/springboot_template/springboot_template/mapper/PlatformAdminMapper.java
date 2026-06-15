package com.springboot_template.springboot_template.mapper;

import com.springboot_template.springboot_template.dto.response.PlatformAdminResponseDTO;
import com.springboot_template.springboot_template.model.PlatformAdmin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatformAdminMapper {
    @Mapping(target = "fullName", source = "displayName")
    PlatformAdminResponseDTO toResponseDTO(PlatformAdmin platformAdmin);
}
