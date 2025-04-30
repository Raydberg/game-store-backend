package com.gamestore.auth.services.Impl;

import com.gamestore.auth.DTOs.AuthResponseDto;
import com.gamestore.auth.DTOs.AuthResponseRegisterDto;
import com.gamestore.auth.DTOs.UserRequestDto;
import com.gamestore.auth.jwt.JwtUtils;
import com.gamestore.auth.model.RoleModel;
import com.gamestore.auth.model.UserModel;
import com.gamestore.auth.repository.RoleRepository;
import com.gamestore.auth.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "El usuario con el email " + email + "no existe"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getEnumRole().name())));
        });

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

    public AuthResponseDto loginUser(@Valid AuthLoginRequestDto authDto) {
        String email = authDto.email();
        String password = authDto.password();

        Long id = userRepository.findByEmail(email)
                .map(UserModel::getId)
                .orElseThrow(() -> new UsernameNotFoundException("El Id del usuario con el correo " + email + " no existe"));


        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtUtils.generateJwtToken(authentication);
        return new AuthResponseDto(id, email, "Usuario logeado exitosamente", token, true);


    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

    }

    public AuthResponseRegisterDto createUser(@Valid UserRequestDto authCreateUserDto) {

        String photoUrl = authCreateUserDto.photoUrl();
        String email = authCreateUserDto.email();
        String password = authCreateUserDto.password();
        String username = authCreateUserDto.name();
        String lastName = authCreateUserDto.lastName();
        int phoneNumber = authCreateUserDto.phoneNumber();
        String country = authCreateUserDto.country();
        LocalDateTime birthDate = authCreateUserDto.birthDate();

        List<String> roles = authCreateUserDto.roleDto().roles();


        Set<RoleModel> roleEntities = new HashSet<>(roleRepository.findRoleEntitiesByEnumRoleIn(roles));


        if (roleEntities.isEmpty()) {
            throw new IllegalArgumentException("Los roles especificados no existen");
        }

        UserModel userEntity = UserModel.builder()
                .photoUrl(photoUrl)
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(username)
                .lastName(lastName)
                .phoneNumber((long) phoneNumber)
                .country(country)
                .birthDate(birthDate)
                .registerDate(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .roles(roleEntities)
                .build();

        UserModel userCreated = userRepository.save(userEntity);

        ArrayList<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();

        userCreated.getRoles().forEach(role -> {
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getEnumRole().name())));
        });

        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authoritiesList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getEmail(), userCreated.getPassword(), authoritiesList);
        String accessToken = jwtUtils.generateJwtToken(authentication);


        return new AuthResponseRegisterDto(userCreated.getId(), username, "Usuario registrado exitosamente", accessToken, true);
    }
}