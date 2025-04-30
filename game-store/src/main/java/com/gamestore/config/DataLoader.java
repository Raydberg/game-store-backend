package com.gamestore.config;

import com.gamestore.auth.enums.EnumRole;
import com.gamestore.auth.model.RoleModel;
import com.gamestore.auth.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final RoleRepository roleRepository;
    
    @PostConstruct
    @Transactional
    public void loadData() {
        initRoles();
    }
    
    private void initRoles() {
        if (roleRepository.findByEnumRole(EnumRole.USER).isEmpty()) {
            roleRepository.save(new RoleModel(EnumRole.USER));
        }
        
        if (roleRepository.findByEnumRole(EnumRole.ADMIN).isEmpty()) {
            roleRepository.save(new RoleModel(EnumRole.ADMIN));
        }
    }
}