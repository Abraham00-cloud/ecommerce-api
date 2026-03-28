package com.AAA.e_commerce.user.mapper;

import com.AAA.e_commerce.user.dto.request.RegisterRequestDto;
import com.AAA.e_commerce.user.dto.response.UserResponseDto;
import com.AAA.e_commerce.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toUser(RegisterRequestDto requestDto) {
        User user = new User();
        user.setEmail(requestDto.email());
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setPassword(requestDto.password());
        return user;
    }

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCart().getId());
    }
}
