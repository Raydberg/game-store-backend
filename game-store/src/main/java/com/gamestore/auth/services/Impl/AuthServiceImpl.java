package com.gamestore.auth.services.Impl;

import com.gamestore.auth.DTOs.AuthLoginRequestDto;
import com.gamestore.auth.DTOs.AuthResponseDto;
import com.gamestore.auth.DTOs.AuthResponseRegisterDto;
import com.gamestore.auth.DTOs.UserRequestDto;
import com.gamestore.auth.enums.EnumRole;
import com.gamestore.auth.jwt.JwtUtils;
import com.gamestore.auth.model.UserModel;
import com.gamestore.auth.repository.RoleRepository;
import com.gamestore.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public AuthResponseDto login(AuthLoginRequestDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        String token = jwtUtils.generateJwtToken(auth);
        UserModel user = userRepository.findByEmail(dto.email()).get();

        return new AuthResponseDto(
                user.getId(),
                user.getEmail(),
                "Login exitoso",
                token,
                true
        );
    }

    @Transactional
    public AuthResponseRegisterDto register(UserRequestDto dto) {
        UserModel userEntity = UserModel.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .roles(Set.of(
                        roleRepository.findByEnumRole(EnumRole.USER)
                                .orElseThrow(() -> new IllegalStateException("Rol USER no existe"))
                ))
                .build();

        UserModel saved = userRepository.save(userEntity);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                saved.getEmail(), null, List.of()
        );

        String token = jwtUtils.generateJwtToken(auth);

        return new AuthResponseRegisterDto(
                saved.getId(),
                "Usuario registrado exitosamente",
                token,
                true
        );
    }
}