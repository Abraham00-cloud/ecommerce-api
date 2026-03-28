package com.AAA.e_commerce.user.controller;

import com.AAA.e_commerce.user.dto.request.LoginRequestDto;
import com.AAA.e_commerce.user.dto.request.RegisterRequestDto;
import com.AAA.e_commerce.user.dto.request.UpdatePasswordDto;
import com.AAA.e_commerce.user.dto.request.UpdateProfileDto;
import com.AAA.e_commerce.user.dto.response.LoginResponseDto;
import com.AAA.e_commerce.user.dto.response.UserResponseDto;
import com.AAA.e_commerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Api", description = "Endpoints for managing User")
public class UserController {
    private final UserService service;

    @Operation(summary = "register user")
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody @Valid RegisterRequestDto requestDto) {
        return service.registerUser(requestDto);
    }

    @Operation(summary = "login user")
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return service.login(loginRequestDto);
    }

    @Operation(summary = "update User profile")
    @PatchMapping("/update/profile")
    public UserResponseDto updateUserProfile(@RequestBody @Valid UpdateProfileDto profileDto) {
        return service.updateUserProfile(profileDto);
    }

    @Operation(summary = "change User password")
    @PatchMapping("/update/password")
    public void changePassword(@RequestBody @Valid UpdatePasswordDto passwordDto) {
        service.changePassword(passwordDto);
    }

    @Operation(summary = "get User profile")
    @GetMapping("/{userId}")
    public UserResponseDto getUserProfile(@PathVariable Long userId) {
        return service.getUserProfile(userId);
    }
}
