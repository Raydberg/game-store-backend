package com.gamestore.users.mappers;

import com.gamestore.address.DTOs.AddressResponseDto;
import com.gamestore.address.mappers.AddressMapper;
import com.gamestore.users.DTOs.UserProfileDto;
import com.gamestore.users.DTOs.UserRequestDto;
import com.gamestore.users.DTOs.UserResponseDto;
import com.gamestore.auth.model.RoleModel;
import com.gamestore.users.model.UserModel;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { AddressMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    protected AddressMapper addressMapper;

    public abstract UserModel toEntity(UserRequestDto dto);

    public abstract UserRequestDto toDto(UserModel user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
    public abstract UserResponseDto toDtoUser(UserModel userModel);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
    @Mapping(target = "address", expression = "java(getFirstAddress(userModel))")
    @Mapping(target = "active", source = "active")

    public abstract UserProfileDto toProfileDto(UserModel userModel);

    @Named("rolesToStringSet")
    public Set<String> rolesToStringSet(Set<RoleModel> roles) {
        return roles == null
                ? Collections.emptySet()
                : roles.stream()
                        .map(r -> r.getEnumRole().name())
                        .collect(Collectors.toSet());
    }

    public AddressResponseDto getFirstAddress(UserModel userModel) {
        if (userModel.getAddresses() == null || userModel.getAddresses().isEmpty()) {
            return null;
        }
        return Optional.ofNullable(userModel.getAddresses().get(0))
                .map(addressMapper::toAddressResponseDto)
                .orElse(null);
    }
}