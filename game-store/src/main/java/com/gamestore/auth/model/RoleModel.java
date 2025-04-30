package com.gamestore.auth.model;

import com.gamestore.auth.enums.EnumRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "roles")
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private EnumRole enumRole;

    public RoleModel(EnumRole enumRole) {
        this.enumRole = enumRole;
    }
}