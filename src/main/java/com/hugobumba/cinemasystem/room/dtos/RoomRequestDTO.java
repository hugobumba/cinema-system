package com.hugobumba.cinemasystem.room.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomRequestDTO {
    private Long id;

    @NotBlank(message = "Room number is required")
    private String number;

    @NotNull(message = "Cinema id is required")
    private Long cinemaId;
}
