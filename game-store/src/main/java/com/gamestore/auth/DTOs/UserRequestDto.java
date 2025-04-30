package com.gamestore.auth.DTOs;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
}