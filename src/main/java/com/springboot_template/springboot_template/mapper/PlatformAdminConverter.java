package com.springboot_template.springboot_template.mapper;

import com.springboot_template.springboot_template.dto.request.PlatformAdminRequestDTO;
import com.springboot_template.springboot_template.model.PlatformAdmin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatformAdminConverter {

    @Mapping(target = "displayName", source = "fullName")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seeded", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PlatformAdmin toEntity(PlatformAdminRequestDTO platformAdminRequestDTO);
}
