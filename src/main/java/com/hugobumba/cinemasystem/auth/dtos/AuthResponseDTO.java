package com.hugobumba.cinemasystem.auth.dtos;

import com.hugobumba.cinemasystem.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private Long userId;
    private String email;
    private UserRole role;
    private String tokenType;
    private String accessToken;
}
