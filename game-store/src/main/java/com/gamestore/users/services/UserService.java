package com.gamestore.users.services;

import com.gamestore.users.DTOs.UserPageResponse;
import com.gamestore.users.DTOs.UserRequestDto;
import com.gamestore.users.DTOs.UserResponseDto;
import com.gamestore.users.mappers.UserMapper;
import com.gamestore.users.model.UserModel;
import com.gamestore.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserPageResponse getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserModel> usersPage = userRepository.findAll(pageable);

        List<UserRequestDto> userDtos = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        return new UserPageResponse(userDtos, usersPage.getTotalPages(), usersPage.getTotalElements());
    }

    public UserResponseDto getUserById(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con ID " + id + " no encontrado."));

        return userMapper.toDtoUser(user);

    }

    public UserModel updateUser(Long id, UserRequestDto updatedUserDto) {
        UserModel updatedUser = userMapper.toEntity(updatedUserDto);
        return userRepository.findById(id)
                .map(user -> {
                    // corregir setters según tu entidad:
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName(updatedUser.getLastName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPhone(updatedUser.getPhone());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con ID " + id + " no encontrado."));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("Usuario con ID " + id + " no encontrado.");
        }
        userRepository.deleteById(id);
    }

}