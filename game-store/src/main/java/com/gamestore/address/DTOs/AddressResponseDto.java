package com.gamestore.address.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    private Integer id;
    private Long user;
    private String city;
    private String state;
    private String postalCode;

}
