package com.hugobumba.cinemasystem.cinema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    Optional<Cinema> findByName(String name);

    List<Cinema> findByLocation(String location);
}
