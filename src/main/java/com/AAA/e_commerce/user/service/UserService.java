package com.AAA.e_commerce.user.service;

import com.AAA.e_commerce.cart.model.Cart;
import com.AAA.e_commerce.user.dto.*;
import com.AAA.e_commerce.user.mapper.UserMapper;
import com.AAA.e_commerce.user.model.User;
import com.AAA.e_commerce.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private UserMapper mapper;
    private PasswordEncoder passwordEncoder;
    public UserResponseDto registerUser(RegisterRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        User user = new User();
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setEmail(requestDto.email());
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        User savedUser = userRepository.save(user);
        return mapper.toUserResponseDto(savedUser);

    }

    public UserResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!passwordEncoder.matches(requestDto.password(), (user.getPassword()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid login credentials");
        }
        return mapper.toUserResponseDto(user);
    }

    public UserResponseDto updateUserProfile(Long userId, UpdateProfileDto updateProfileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setFirstName(updateProfileDto.firstName());
        user.setLastName(updateProfileDto.lastname());
        User updatedUser = userRepository.save(user);
        return mapper.toUserResponseDto(updatedUser);
    }

    public void changePassword(Long userId, UpdatePasswordDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!passwordEncoder.matches(requestDto.currentPassword(), user.getPassword() )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is invalid");
        }
        if (!requestDto.newPassword().matches(requestDto.confirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(requestDto.newPassword()));
        userRepository.save(user);
    }

    public UserResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return mapper.toUserResponseDto(user);

    }


}
