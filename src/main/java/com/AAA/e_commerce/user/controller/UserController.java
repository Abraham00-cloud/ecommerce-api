package com.AAA.e_commerce.user.controller;

import com.AAA.e_commerce.user.dto.*;
import com.AAA.e_commerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag( name = "User Api",description = "Endpoints for managing User")
public class UserController {
    private  final UserService service;
    @Operation(summary = "register user")
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody RegisterRequestDto requestDto) {
        return service.registerUser(requestDto);
    }

    @Operation(summary = "login user")
    @PostMapping("/login")
    public  UserResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return service.login(loginRequestDto);
    }

    @Operation(summary = "update User profile")
    @PatchMapping("/{userId}")
    public UserResponseDto updateUserProfile(@PathVariable Long userId, @RequestBody UpdateProfileDto profileDto) {
        return service.updateUserProfile(userId, profileDto);
    }

    @Operation(summary = "change User password")
    @PatchMapping("/userId)")
    public void changePassword(@PathVariable Long userId, @RequestBody UpdatePasswordDto passwordDto) {
        service.changePassword(userId, passwordDto);
    }

    @Operation(summary = "get User profile")
    @GetMapping("/userId)")
    public UserResponseDto getUserProfile(@PathVariable Long userId) {
       return service.getUserProfile(userId);
    }
}
