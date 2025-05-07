package com.gamestore.address.services;

import com.gamestore.address.DTOs.AddressPageResponseDto;
import com.gamestore.address.DTOs.AddressRequestDto;
import com.gamestore.address.DTOs.AddressResponseDto;

public interface AddressService {
    
    AddressPageResponseDto getAllAddresses(int page, int size);
    
    AddressResponseDto createOrUpdateUserAddress(AddressRequestDto dto, Long userId);
    
    AddressResponseDto getUserAddress(Long userId);
    
    AddressResponseDto getAddressById(Long addressId);
    
    void deleteAddress(Long addressId, Long userId);
}