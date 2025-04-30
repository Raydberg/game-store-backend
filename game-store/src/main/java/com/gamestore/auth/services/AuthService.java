package com.gamestore.auth.services;

import com.gamestore.auth.DTOs.AuthLoginRequestDto;
import com.gamestore.auth.DTOs.AuthResponseDto;
import com.gamestore.auth.DTOs.AuthResponseRegisterDto;
import com.gamestore.users.DTOs.UserRequestDto;
import jakarta.validation.Valid;

public interface AuthService {
    AuthResponseDto loginUser(@Valid AuthLoginRequestDto dto);

    AuthResponseRegisterDto createUser(@Valid UserRequestDto dto);
}
