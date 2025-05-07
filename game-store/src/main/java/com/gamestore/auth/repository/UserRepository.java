package com.gamestore.auth.repository;
import com.gamestore.auth.enums.EnumRole;
import com.gamestore.users.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<UserModel> findAll(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM UserModel u JOIN u.roles r WHERE r.enumRole = :role AND u.active = :active")
    long countByRolesEnumRoleAndActive(@Param("role") EnumRole role, @Param("active") boolean active);
}