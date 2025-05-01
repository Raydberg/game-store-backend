package com.gamestore.users.model;

import com.gamestore.address.model.UserAddress;
import com.gamestore.auth.model.RoleModel;
import com.gamestore.cart.model.Cart;
import com.gamestore.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
    @OneToMany(mappedBy = "user")
    private List<UserAddress> addresses;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleModel.class)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();
}
