package com.hugobumba.cinemasystem.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class User {
    @Id
    private Long id;

    private String name;

    private String email;

    private String password;

    private UserRole role;
}
