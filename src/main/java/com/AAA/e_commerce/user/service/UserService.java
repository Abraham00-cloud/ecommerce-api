package com.AAA.e_commerce.user.service;

import com.AAA.e_commerce.cart.model.Cart;
import com.AAA.e_commerce.user.dto.request.LoginRequestDto;
import com.AAA.e_commerce.user.dto.request.RegisterRequestDto;
import com.AAA.e_commerce.user.dto.request.UpdatePasswordDto;
import com.AAA.e_commerce.user.dto.request.UpdateProfileDto;
import com.AAA.e_commerce.user.dto.response.LoginResponseDto;
import com.AAA.e_commerce.user.dto.response.UserResponseDto;
import com.AAA.e_commerce.user.mapper.UserMapper;
import com.AAA.e_commerce.user.model.Role;
import com.AAA.e_commerce.user.model.User;
import com.AAA.e_commerce.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public UserResponseDto registerUser(RegisterRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        User user = new User();
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setEmail(requestDto.email());
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRole(Role.USER);

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        User savedUser = userRepository.save(user);
        return mapper.toUserResponseDto(savedUser);
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password()));
        User user =
                userRepository
                        .findByEmail(requestDto.email())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "User not found"));
        String jwtToken = jwtService.generateToken(user.getEmail());
        return new LoginResponseDto(jwtToken, user.getEmail(), user.getRole().name());
    }

    public UserResponseDto updateUserProfile(UpdateProfileDto updateProfileDto) {
        User user = getAuthenticatedUser();
        user.setFirstName(updateProfileDto.firstName());
        user.setLastName(updateProfileDto.lastname());
        User updatedUser = userRepository.save(user);
        return mapper.toUserResponseDto(updatedUser);
    }

    @Transactional
    public void changePassword(UpdatePasswordDto requestDto) {
        User user = getAuthenticatedUser();
        if (!passwordEncoder.matches(requestDto.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Current password is invalid");
        }
        if (!requestDto.newPassword().matches(requestDto.confirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(requestDto.newPassword()));
        userRepository.save(user);
    }

    public UserResponseDto getUserProfile(Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "User not found"));
        return mapper.toUserResponseDto(user);
    }

    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED, "Session expired or invalid"));
    }
}
