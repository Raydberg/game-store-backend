package com.gamestore.auth.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","message","jwt","status"})
public record AuthResponseRegisterDto(
    Long id,
    String message,
    String jwt,
    boolean status
) {
}