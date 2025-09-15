package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Setter
@Builder
@Jacksonized
public class UserDto {

    private long id;
    private String name;
    private String surname;
    private String username;
    private String password;
    @NotNull
    private List<Role> roles;
}
