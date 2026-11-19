package com.hugobumba.cinemasystem.cinema;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Cinema {
    @Id
    private Long id;

    private String name;

    private String location;
}