package com.hugobumba.cinemasystem.room.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomRequestDTO {
    private Long id;
    private String number;
    private Long cinemaId;
}
