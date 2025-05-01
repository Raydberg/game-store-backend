package com.gamestore.category.model;

import com.gamestore.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Builder.Default
    private Instant createAt = Instant.now();

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
