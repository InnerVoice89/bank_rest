package com.example.bankcards.util;


import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
    public static User toEntity(UserDto user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .roles(user.getRoles())
                .password(user.getPassword())
                .build();
    }
}
