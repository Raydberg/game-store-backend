package com.gamestore.address.services.Impl;

import com.gamestore.address.DTOs.AddressPageResponseDto;
import com.gamestore.address.DTOs.AddressRequestDto;
import com.gamestore.address.DTOs.AddressResponseDto;
import com.gamestore.address.mappers.AddressMapper;
import com.gamestore.address.model.UserAddress;
import com.gamestore.address.repository.AddressRepository;
import com.gamestore.address.services.AddressService;
import com.gamestore.auth.repository.UserRepository;
import com.gamestore.users.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressPageResponseDto getAllAddresses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserAddress> addressPage = addressRepository.findAll(pageable);

        List<AddressResponseDto> addressesList = addressPage
                .stream()
                .map(addressMapper::toAddressResponseDto)
                .collect(Collectors.toList());

        return new AddressPageResponseDto(
                addressesList,
                page,
                addressPage.getTotalElements()
        );
    }

    @Override
    @Transactional
    public AddressResponseDto createOrUpdateUserAddress(AddressRequestDto dto, Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + userId));

        UserAddress existingAddress = addressRepository.findByUserId(userId).orElse(null);

        if (existingAddress != null) {
            addressMapper.updateAddressFromDto(dto, existingAddress);
            UserAddress updatedAddress = addressRepository.save(existingAddress);
            return addressMapper.toAddressResponseDto(updatedAddress);
        } else {
            UserAddress newAddress = addressMapper.toAddressEntity(dto);
            newAddress.setUser(user);
            UserAddress savedAddress = addressRepository.save(newAddress);
            return addressMapper.toAddressResponseDto(savedAddress);
        }
    }

    @Override
    public AddressResponseDto getUserAddress(Long userId) {
        UserAddress address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada para el usuario con ID: " + userId));

        return addressMapper.toAddressResponseDto(address);
    }

    @Override
    public AddressResponseDto getAddressById(Long addressId) {
        UserAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + addressId));

        return addressMapper.toAddressResponseDto(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId, Long userId) {
        UserAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta dirección");
        }

        addressRepository.delete(address);
    }
}