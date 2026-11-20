package com.hugobumba.cinemasystem.movie;

import com.hugobumba.cinemasystem.movie.dtos.MovieRequestDTO;
import com.hugobumba.cinemasystem.movie.dtos.MovieResponseDTO;
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
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public MovieResponseDTO createMovie(@RequestBody MovieRequestDTO request) {
        Movie movie = toEntity(request);
        Movie createdMovie = movieService.createMovie(movie);
        return toResponse(createdMovie);
    }

    @GetMapping("/{id}")
    public MovieResponseDTO getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return toResponse(movie);
    }

    @GetMapping("/genre/{genre}")
    public List<MovieResponseDTO> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = movieService.listMoviesByGenre(genre);
        return movies.stream().map(this::toResponse).toList();
    }

    @GetMapping
    public List<MovieResponseDTO> listMovies(@RequestParam(required = false) String genre) {
        List<Movie> movies = genre == null || genre.isBlank()
                ? movieService.listAllMovies()
                : movieService.listMoviesByGenre(genre);
        return movies.stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public MovieResponseDTO updateMovie(@PathVariable Long id, @RequestBody MovieRequestDTO request) {
        Movie movie = toEntity(request);
        movie.setId(id);
        Movie updatedMovie = movieService.updateMovie(movie);
        return toResponse(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    private Movie toEntity(MovieRequestDTO request) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setGenre(request.getGenre());
        movie.setDurationInMinutes(request.getDurationInMinutes());
        movie.setAgeRating(request.getAgeRating());
        return movie;
    }

    private MovieResponseDTO toResponse(Movie movie) {
        MovieResponseDTO response = new MovieResponseDTO();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setPosterUrl(movie.getPosterUrl());
        response.setGenre(movie.getGenre());
        response.setDurationInMinutes(movie.getDurationInMinutes());
        response.setAgeRating(movie.getAgeRating());
        return response;
    }
}
