package com.gamestore.users.DTOs;

import java.util.List;

public record UserPageResponse (
        List<UserRequestDto> users,
        int totalPages,
        long totalElements
){
}