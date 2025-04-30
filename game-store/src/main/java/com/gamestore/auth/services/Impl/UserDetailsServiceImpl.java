package com.gamestore.auth.services.Impl;

import com.gamestore.users.model.UserModel;
import com.gamestore.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email no encontrado: " + email));

        List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getEnumRole().name()))
                .toList();

        return new User(user.getEmail(), user.getPassword(), roles);
    }
}