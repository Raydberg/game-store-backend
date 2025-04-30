package com.gamestore.auth.DTOs;


public record AuthResponseDto(
    Long id,
    String email,
    String message,
    String token,
    boolean success
) {
}