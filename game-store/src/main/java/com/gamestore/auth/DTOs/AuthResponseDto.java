package com.gamestore.auth.DTOs;

public record AuthResponseDto(
        String message,
        String token,
        boolean success) {
}