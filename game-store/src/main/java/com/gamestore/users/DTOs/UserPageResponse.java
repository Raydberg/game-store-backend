package com.gamestore.users.DTOs;

import java.util.List;

public record UserPageResponse (
        List<UserResponseDto> users,
        int totalPages,
        long totalElements
){
}