package com.gamestore.category.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    Long id;
    String name;
    String description;
    boolean active;
}
