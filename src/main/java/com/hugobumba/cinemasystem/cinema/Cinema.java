package com.hugobumba.cinemasystem.cinema;

import com.hugobumba.cinemasystem.room.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cinema {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    private String name;

    private String location;

    @OneToMany(mappedBy = "cinema")
    private Set<Room> rooms = new HashSet<>();
}