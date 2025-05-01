package com.gamestore.address.DTOs;

public record AddressRequestDto(
        String city,
        String state,
        String postalCode
) {
}
