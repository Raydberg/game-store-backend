package com.gamestore.address.DTOs;

import java.util.List;

public record AddressPageResponseDto(
        List<AddressResponseDto> dto,
        int page,
        long size
) {
}
