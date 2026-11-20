package com.hugobumba.cinemasystem.cinema;

import com.hugobumba.cinemasystem.cinema.dtos.CinemaRequestDTO;
import com.hugobumba.cinemasystem.cinema.dtos.CinemaResponseDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @PostMapping
    public CinemaResponseDTO createCinema(@RequestBody CinemaRequestDTO request) {
        Cinema cinema = toEntity(request);
        Cinema createdCinema = cinemaService.createCinema(cinema);
        return toResponse(createdCinema);
    }

    @GetMapping("/{id}")
    public CinemaResponseDTO getCinemaById(@PathVariable Long id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        return toResponse(cinema);
    }

    @GetMapping
    public List<CinemaResponseDTO> getAllCinemas(@RequestParam(required = false) String location) {
        List<Cinema> cinemas = location == null || location.isBlank()
                ? cinemaService.listAllCinemas()
                : cinemaService.listCinemasByLocation(location);
        return cinemas.stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public CinemaResponseDTO updateCinema(@PathVariable Long id, @RequestBody CinemaRequestDTO request) {
        Cinema cinema = toEntity(request);
        cinema.setId(id);
        Cinema updatedCinema = cinemaService.updateCinema(cinema);
        return toResponse(updatedCinema);
    }

    @DeleteMapping("/{id}")
    public void deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
    }

    private Cinema toEntity(CinemaRequestDTO request) {
        Cinema cinema = new Cinema();
        cinema.setId(request.getId());
        cinema.setName(request.getName());
        cinema.setLocation(request.getLocation());
        return cinema;
    }

    private CinemaResponseDTO toResponse(Cinema cinema) {
        CinemaResponseDTO response = new CinemaResponseDTO();
        response.setId(cinema.getId());
        response.setName(cinema.getName());
        response.setLocation(cinema.getLocation());
        return response;
    }
}
