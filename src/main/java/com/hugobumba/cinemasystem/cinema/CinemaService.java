package com.hugobumba.cinemasystem.cinema;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Transactional
    public Cinema createCinema(Cinema cinema) {
        if (cinema == null) {
            throw new IllegalArgumentException("Cinema is required");
        }
        if (cinema.getName() == null || cinema.getName().isBlank()) {
            throw new IllegalArgumentException("Cinema name is required");
        }
        if (cinema.getLocation() == null || cinema.getLocation().isBlank()) {
            throw new IllegalArgumentException("Cinema location is required");
        }
        return cinemaRepository.save(cinema);
    }

    public Cinema getCinemaById(Long id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cinema not found: " + id));
    }

    public List<Cinema> listAllCinemas() {
        return cinemaRepository.findAll();
    }

    public List<Cinema> listCinemasByLocation(String location) {
        return cinemaRepository.findByLocation(location);
    }

    @Transactional
    public Cinema updateCinema(Cinema cinema) {
        if (cinema == null || cinema.getId() == null) {
            throw new IllegalArgumentException("Cinema id is required");
        }
        Cinema existingCinema = getCinemaById(cinema.getId());
        if (cinema.getName() == null || cinema.getName().isBlank()) {
            throw new IllegalArgumentException("Cinema name is required");
        }
        if (cinema.getLocation() == null || cinema.getLocation().isBlank()) {
            throw new IllegalArgumentException("Cinema location is required");
        }

        existingCinema.setName(cinema.getName());
        existingCinema.setLocation(cinema.getLocation());
        return cinemaRepository.save(existingCinema);
    }

    @Transactional
    public void deleteCinema(Long id) {
        Cinema cinema = getCinemaById(id);
        cinemaRepository.delete(cinema);
    }
}
