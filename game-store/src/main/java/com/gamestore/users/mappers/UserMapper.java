package com.gamestore.users.mappers;

import com.gamestore.users.DTOs.UserRequestDto;
import com.gamestore.users.DTOs.UserResponseDto;
import com.gamestore.auth.model.RoleModel;
import com.gamestore.users.model.UserModel;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserModel toEntity(UserRequestDto dto);

    UserRequestDto toDto(UserModel user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
    UserResponseDto toDtoUser(UserModel userModel);

    @Named("rolesToStringSet")
    default Set<String> rolesToStringSet(Set<RoleModel> roles) {
        return roles == null
                ? Collections.emptySet()
                : roles.stream()
                .map(r -> r.getEnumRole().name())
                .collect(Collectors.toSet());
    }
}