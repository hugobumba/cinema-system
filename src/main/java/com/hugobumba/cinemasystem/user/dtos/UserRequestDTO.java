package com.hugobumba.cinemasystem.user.dtos;

import com.hugobumba.cinemasystem.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "User name is required")
    private String name;

    @NotBlank(message = "User email is required")
    @Email(message = "User email must be valid")
    private String email;

    @NotBlank(message = "User password is required")
    private String password;

    @NotNull(message = "User role is required")
    private UserRole role;
}
