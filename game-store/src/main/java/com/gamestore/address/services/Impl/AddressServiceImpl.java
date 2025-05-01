package com.gamestore.address.services.Impl;

import com.gamestore.address.mappers.AddressMapper;
import com.gamestore.address.repository.AddressRepository;
import com.gamestore.address.services.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
}
