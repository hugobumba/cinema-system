package com.hugobumba.cinemasystem.movie;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie createMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie is required");
        }
        if (movie.getId() == null) {
            throw new IllegalArgumentException("Movie id is required");
        }
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Movie title is required");
        }
        if (movie.getDescription() == null || movie.getDescription().isBlank()) {
            throw new IllegalArgumentException("Movie description is required");
        }
        if (movie.getGenre() == null) {
            throw new IllegalArgumentException("Movie genre is required");
        }
        if (movie.getAgeRating() == null) {
            throw new IllegalArgumentException("Movie age rating is required");
        }
        if (movie.durationInMinutes() == null || movie.durationInMinutes() <= 0) {
            throw new IllegalArgumentException("Movie duration must be greater than zero");
        }
        return movieRepository.save(movie);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + id));
    }

    public List<Movie> listAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> listMoviesByGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre is required");
        }
        return movieRepository.findByGenre(genre);
    }

    @Transactional
    public Movie updateMovie(Movie movie) {
        if (movie == null || movie.getId() == null) {
            throw new IllegalArgumentException("Movie id is required");
        }
        Movie existingMovie = getMovieById(movie.getId());
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Movie title is required");
        }
        if (movie.getDescription() == null || movie.getDescription().isBlank()) {
            throw new IllegalArgumentException("Movie description is required");
        }
        if (movie.getGenre() == null) {
            throw new IllegalArgumentException("Movie genre is required");
        }
        if (movie.getAgeRating() == null) {
            throw new IllegalArgumentException("Movie age rating is required");
        }
        if (movie.durationInMinutes() == null || movie.durationInMinutes() <= 0) {
            throw new IllegalArgumentException("Movie duration must be greater than zero");
        }
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setPosterUrl(movie.getPosterUrl());
        existingMovie.setGenre(movie.getGenre());
        existingMovie.durationInMinutes(movie.durationInMinutes());
        existingMovie.setAgeRating(movie.getAgeRating());
        return movieRepository.save(existingMovie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = getMovieById(id);
        movieRepository.delete(movie);
    }
}
