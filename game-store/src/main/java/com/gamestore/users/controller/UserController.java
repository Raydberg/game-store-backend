package com.gamestore.users.controller;

import com.gamestore.config.helpers.AuthenticationHelper;
import com.gamestore.users.DTOs.UserPageResponse;
import com.gamestore.users.DTOs.UserProfileDto;
import com.gamestore.users.DTOs.UserRequestDto;
import com.gamestore.users.DTOs.UserResponseDto;
import com.gamestore.users.mappers.UserMapper;
import com.gamestore.users.model.UserModel;
import com.gamestore.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authHelper;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> getMyProfile() {
        UserModel currentUser = authHelper.getCurrentUser();
        UserProfileDto profile = userMapper.toProfileDto(currentUser);
        return ResponseEntity.ok(profile);
    }

    @GetMapping
    public ResponseEntity<UserPageResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto
    ) {
        UserModel updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(userMapper.toDtoUser(updated));
    }

    @PatchMapping("/{id}/toggle-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> toggleAdminRole(
            @PathVariable Long id,
            @RequestParam boolean idAdmin

    ) {
        UserResponseDto updateUser = userService.toggleAdminRole(id, idAdmin);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}