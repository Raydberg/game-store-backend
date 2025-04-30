package com.gamestore.auth.mappers;

import com.gamestore.auth.DTOs.UserRequestDto;
import com.gamestore.auth.DTOs.UserResponseDto;
import com.gamestore.auth.model.RoleModel;
import com.gamestore.auth.model.UserModel;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserModel toEntity(UserRequestDto dto);

    UserRequestDto toDto(UserModel user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
    UserResponseDto toDtoUser(UserModel userModel);

    @Named("rolesToStringSet")
    default Set<String> rolesToStringSet(Set<RoleModel> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(role -> role.getEnumRole().name())
                .collect(Collectors.toSet());
    }
}