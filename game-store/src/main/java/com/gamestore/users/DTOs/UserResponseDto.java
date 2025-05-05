package com.gamestore.users.DTOs;

import java.util.Set;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Set<String> roles;
    private boolean active;
}