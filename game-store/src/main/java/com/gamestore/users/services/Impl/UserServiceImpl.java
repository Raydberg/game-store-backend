package com.gamestore.users.services.Impl;

import com.gamestore.users.DTOs.UserPageResponse;
import com.gamestore.users.DTOs.UserRequestDto;
import com.gamestore.users.DTOs.UserResponseDto;
import com.gamestore.users.mappers.UserMapper;
import com.gamestore.users.model.UserModel;
import com.gamestore.auth.repository.UserRepository;
import com.gamestore.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserPageResponse getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserModel> usersPage = userRepository.findAll(pageable);

        List<UserResponseDto> userDtos = usersPage.getContent().stream()
                .map(userMapper::toDtoUser)
                .collect(Collectors.toList());

        return new UserPageResponse(userDtos, usersPage.getTotalPages(), usersPage.getTotalElements());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        UserModel user = findUserOrThrow(id);
        return userMapper.toDtoUser(user);
    }

    @Override
    @Transactional
    public UserModel updateUser(Long id, UserRequestDto updatedUserDto) {
        UserModel existingUser = findUserOrThrow(id);

        if (updatedUserDto.getFirstName() != null) {
            existingUser.setFirstName(updatedUserDto.getFirstName());
        }

        if (updatedUserDto.getLastName() != null) {
            existingUser.setLastName(updatedUserDto.getLastName());
        }

        if (updatedUserDto.getEmail() != null) {
            existingUser.setEmail(updatedUserDto.getEmail());
        }

        if (updatedUserDto.getPhone() != null) {
            existingUser.setPhone(updatedUserDto.getPhone());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("Usuario con ID " + id + " no encontrado.");
        }
        userRepository.deleteById(id);
    }

    private UserModel findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con ID " + id + " no encontrado."));
    }
}