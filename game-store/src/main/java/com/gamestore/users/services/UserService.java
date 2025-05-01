package com.gamestore.users.services;

import com.gamestore.users.DTOs.UserPageResponse;
import com.gamestore.users.DTOs.UserRequestDto;
import com.gamestore.users.DTOs.UserResponseDto;
import com.gamestore.users.model.UserModel;

public interface UserService {

    UserPageResponse getAllUsers(int page, int size);

    UserResponseDto getUserById(Long id);

    UserModel updateUser(Long id, UserRequestDto updatedUserDto);

    UserResponseDto toggleAdminRole(Long id, boolean isAdmin);

    void deleteUser(Long id);
}