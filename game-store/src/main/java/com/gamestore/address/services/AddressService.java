package com.gamestore.address.services;

import com.gamestore.address.DTOs.AddressPageResponseDto;
import com.gamestore.address.DTOs.AddressRequestDto;
import com.gamestore.address.DTOs.AddressResponseDto;

public interface AddressService {
    
    // Para administradores - ver todas las direcciones
    AddressPageResponseDto getAllAddresses(int page, int size);
    
    // Para usuarios - crear o actualizar su dirección
    AddressResponseDto createOrUpdateUserAddress(AddressRequestDto dto, Long userId);
    
    // Para usuarios - obtener su dirección
    AddressResponseDto getUserAddress(Long userId);
    
    // Para administradores - obtener dirección por ID
    AddressResponseDto getAddressById(Long addressId);
    
    // Para usuarios/admin - eliminar dirección
    void deleteAddress(Long addressId, Long userId);
}