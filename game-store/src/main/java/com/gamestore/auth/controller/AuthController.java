package com.gamestore.auth.controller;

import com.gamestore.auth.DTOs.AuthLoginRequestDto;
import com.gamestore.auth.DTOs.AuthResponseDto;
import com.gamestore.auth.DTOs.AuthResponseRegisterDto;
import com.gamestore.auth.DTOs.UserRequestDto;
import com.gamestore.auth.services.Impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthLoginRequestDto req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseRegisterDto> register(@Valid @RequestBody UserRequestDto req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }
}