package com.gamestore.address.controllers;

import com.gamestore.address.DTOs.AddressPageResponseDto;
import com.gamestore.address.DTOs.AddressRequestDto;
import com.gamestore.address.DTOs.AddressResponseDto;
import com.gamestore.address.services.AddressService;
import com.gamestore.config.helpers.AuthenticationHelper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController {
    private final AddressService addressService;
    private final AuthenticationHelper authHelper;

    // Solo administradores pueden ver todas las direcciones
    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressPageResponseDto> getAllAddresses(
            @RequestParam(defaultValue = "0") @Min(value = 0) int page,
            @RequestParam(defaultValue = "10") @Min(value = 1) int size) {
        AddressPageResponseDto addresses = addressService.getAllAddresses(page, size);
        return ResponseEntity.ok(addresses);
    }

    // Ver dirección específica - solo admin o propietario
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isAddressOwner(#id, principal)")
    public ResponseEntity<AddressResponseDto> getAddressById(@PathVariable Long id) {
        AddressResponseDto address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    // Ver mi dirección como usuario autenticado
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponseDto> getMyAddress() {
        Long userId = authHelper.getCurrentUserId();
        
        try {
            AddressResponseDto address = addressService.getUserAddress(userId);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear o actualizar mi dirección
    @PutMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponseDto> createOrUpdateAddress(
            @Valid @RequestBody AddressRequestDto dto) {
        Long userId = authHelper.getCurrentUserId();
        AddressResponseDto updatedAddress = addressService.createOrUpdateUserAddress(dto, userId);
        return ResponseEntity.ok(updatedAddress);
    }

    // Eliminar mi dirección
    @DeleteMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMyAddress() {
        Long userId = authHelper.getCurrentUserId();
        
        try {
            Long addressId = addressService.getUserAddress(userId).getId();
            addressService.deleteAddress(addressId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Solo admin puede eliminar cualquier dirección
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id, null); // null indica que es admin
        return ResponseEntity.noContent().build();
    }
}