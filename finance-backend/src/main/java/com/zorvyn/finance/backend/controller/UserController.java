package com.zorvyn.finance.backend.controller;

import com.zorvyn.finance.backend.dto.UserDTO;
import com.zorvyn.finance.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management",
        description = "ADMIN only - manage users and roles")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users",
            description = "ADMIN only")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID",
            description = "ADMIN only")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user role and status",
            description = "ADMIN only")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(
                userService.updateUser(id, userDTO));
    }

    @Operation(summary = "Delete user",
            description = "ADMIN only")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate user account",
            description = "ADMIN only")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserDTO> activateUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.setUserStatus(id, true));
    }

    @Operation(summary = "Deactivate user account",
            description = "ADMIN only")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserDTO> deactivateUser(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.setUserStatus(id, false));
    }
}