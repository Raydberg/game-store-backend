package com.gamestore.auth.repository;

import com.gamestore.auth.enums.EnumRole;
import com.gamestore.auth.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    List<RoleModel> findRoleEntitiesByEnumRoleIn(List<String> roles);
    Optional<RoleModel> findByEnumRole(EnumRole role);
}