package com.gamestore.auth.DTOs;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
}