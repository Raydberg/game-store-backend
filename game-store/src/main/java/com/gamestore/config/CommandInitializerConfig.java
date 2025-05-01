package com.gamestore.config;

import com.gamestore.auth.enums.EnumRole;
import com.gamestore.auth.model.RoleModel;
import com.gamestore.auth.repository.RoleRepository;
import com.gamestore.auth.repository.UserRepository;
import com.gamestore.users.model.UserModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandInitializerConfig {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    @Transactional
    public void loadData() {
        log.info("Iniciando carga de datos inicial");
        initRoles();
        if (userRepository.count() == 0) {
            initUsers();
            log.info("Usuarios cargados exitosamente");
        } else {
            log.info("Usuarios ya existen en la base de datos");
        }
    }


    private void initRoles() {
        if (roleRepository.findByEnumRole(EnumRole.USER).isEmpty()) {
            log.info("Creando rol USER");
            roleRepository.save(new RoleModel(EnumRole.USER));
        }
        if (roleRepository.findByEnumRole(EnumRole.ADMIN).isEmpty()) {
            log.info("Creando rol ADMIN");
            roleRepository.save(new RoleModel(EnumRole.ADMIN));
        }
    }

    private void initUsers() {
        log.info("Creating default users");
        Instant now = Instant.now();

        Set<RoleModel> adminRoles = new HashSet<>();
        adminRoles.add(roleRepository.findByEnumRole(EnumRole.ADMIN).orElseThrow());
        adminRoles.add(roleRepository.findByEnumRole(EnumRole.USER).orElseThrow());

        Set<RoleModel> userRoles = new HashSet<>();
        userRoles.add(roleRepository.findByEnumRole(EnumRole.USER).orElseThrow());

        UserModel admin = UserModel.builder()
                .email("brayan@gmail.com")
                .password(passwordEncoder.encode("brayan123"))
                .firstName("Brayan")
                .lastName("Cruz")
                .phone("1234567890")
                .roles(adminRoles)
                .createdAt(now)
                .build();

        userRepository.save(admin);
        log.info("Admin user created: {}", admin.getEmail());

        UserModel user = UserModel.builder()
                .email("ryan@gmail.com")
                .password(passwordEncoder.encode("ryan123"))
                .firstName("Ryan")
                .lastName("Angulo")
                .phone("99999999")
                .roles(userRoles)
                .createdAt(now)
                .build();

        userRepository.save(user);
        log.info("Regular user created: {}", user.getEmail());

        UserModel demoUser = UserModel.builder()
                .email("norma@gmail.com")
                .password(passwordEncoder.encode("norma2025"))
                .firstName("Norma")
                .lastName("Quezada")
                .phone("5555555")
                .roles(userRoles)
                .createdAt(now)
                .build();

        userRepository.save(demoUser);
        log.info("Demo user created: {}", demoUser.getEmail());
    }


}
