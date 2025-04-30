package com.gamestore.auth.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message", "jwt", "status"})
public record AuthResponseRegisterDto(
        String message,
        String jwt,
        boolean status) {
}