package com.gamestore.auth.configuration;

import com.gamestore.address.repository.AddressRepository;
import com.gamestore.config.helpers.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final AddressRepository addressRepository;
    private final AuthenticationHelper authHelper;

    public boolean isAddressOwner(Long addressId, UserDetails userDetails) {
        if (addressId == null) {
            return false;
        }

        try {
            Long userId = authHelper.getCurrentUserId();
            return addressRepository.findById(addressId)
                    .map(address -> address.getUser().getId().equals(userId))
                    .orElse(false);
        } catch (
                RuntimeException e) {
            return false;
        }
    }
}