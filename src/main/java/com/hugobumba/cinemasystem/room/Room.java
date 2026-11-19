package com.hugobumba.cinemasystem.room;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Room {
    @Id
    private Long id;

    private Long cinemaId;

    private String number;
}
