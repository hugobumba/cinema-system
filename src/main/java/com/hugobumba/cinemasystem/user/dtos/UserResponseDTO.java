package com.hugobumba.cinemasystem.user.dtos;

import com.hugobumba.cinemasystem.user.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
}
