package com.hugobumba.cinemasystem.cinema.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CinemaRequestDTO {
    private Long id;
    private String name;
    private String location;
}
