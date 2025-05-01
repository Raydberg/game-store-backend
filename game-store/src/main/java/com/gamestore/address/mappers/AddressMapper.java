package com.gamestore.address.mappers;

import com.gamestore.address.DTOs.AddressRequestDto;
import com.gamestore.address.DTOs.AddressResponseDto;
import com.gamestore.address.model.UserAddress;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
//    @Mapping(target = "user", source = "user.id")
    AddressResponseDto toAddressResponseDto(UserAddress address);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserAddress toAddressEntity(AddressRequestDto dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromDto(AddressRequestDto dto, @MappingTarget UserAddress address);
}