package com.hugobumba.cinemasystem.cinema.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CinemaRequestDTO {
    private Long id;

    @NotBlank(message = "Cinema name is required")
    private String name;

    @NotBlank(message = "Cinema location is required")
    private String location;
}
