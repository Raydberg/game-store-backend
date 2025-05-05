package com.gamestore.users.DTOs;

import com.gamestore.address.DTOs.AddressResponseDto;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Set<String> roles;
    private AddressResponseDto address; 
    private boolean active;
}